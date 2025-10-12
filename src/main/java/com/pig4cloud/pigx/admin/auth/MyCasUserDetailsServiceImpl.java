package com.pig4cloud.pigx.admin.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.config.CasProperties;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.UserTypeEnum;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.service.PigxUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.*;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyCasUserDetailsServiceImpl implements PigxUserDetailsService {

    private final SysUserService sysUserService;
    private final CasProperties casProperties;
    private final CacheManager cacheManager;

    // 登录流程使用
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String ticket) {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (cache != null && cache.get(ticket) != null) {
            log.info("[CAS-LOGIN] cache hit for username={}", ticket);
            UserInfo userInfo = cache.get(ticket, UserInfo.class);
            if (userInfo == null) {
                log.warn("[CAS-LOGIN] cache entry for username={} is null, will refetch", ticket);
            } else {
                userInfo.setPassword(ticket); // 不记录明文
                return getUserDetails(Optional.ofNullable(userInfo));
            }
        } else {
            log.info("[CAS-LOGIN] cache miss for username={}", ticket);
        }
        final String ticketMask = maskTicket(ticket);
        log.info("[CAS-LOGIN] loadUserByUsername start, ticketLen={}, ticketHash8={} ,ticketMask={}",
                StrUtil.length(ticket), shortHash(ticket), ticketMask);

        long t0 = System.currentTimeMillis();
        String username = validateTicket(ticket);
        long t1 = System.currentTimeMillis();
        log.info("[CAS-LOGIN] validateTicket done, cost={}ms, username={}", (t1 - t0), username);

        if (StrUtil.isBlank(username)) {
            log.warn("[CAS-LOGIN] validateTicket returned blank username, ticketMask={}", ticketMask);
            throw new BizException("用户不存在");
        }


        // 远程用户信息查询
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        long t2 = System.currentTimeMillis();
        R<UserInfo> result = sysUserService.getUserInfo(userDTO);
        long t3 = System.currentTimeMillis();
        log.info("[CAS-LOGIN] sysUserService.getUserInfo cost={}ms, retCode={}, retMsg={}",
                (t3 - t2), result.getCode(), result.getMsg());

        if (result.getData() == null) {
            log.warn("[CAS-LOGIN] getUserInfo returned null data for username={}", username);
        } else {
            // 写入敏感字段但不打印
            result.getData().setPassword(ticket);
        }

        Optional<UserInfo> userInfoOptional = RetOps.of(result).getData();
        userInfoOptional.ifPresent(userInfo -> {
            try {
                if (cache != null) {
                    cache.put(username, userInfo);
                    log.info("[CAS-LOGIN] cache put success for username={}", username);
                } else {
                    log.warn("[CAS-LOGIN] cacheManager.getCache returned null for {}", CacheConstants.USER_DETAILS);
                }
            } catch (Exception e) {
                log.error("[CAS-LOGIN] cache put failed for username={}, err={}", username, e.getMessage(), e);
            }
        });

        UserDetails ud = getUserDetails(userInfoOptional);
        log.info("[CAS-LOGIN] loadUserByUsername success for username={}, authorities={}",
                username, String.join(",", authoritiesToStrings(ud.getAuthorities())));
        return ud;
    }

    public UserDetails getUserDetails(Optional<UserInfo> userInfoOptional) {
        return userInfoOptional
                .map(this::convertUserDetails)
                .orElseThrow(() -> {
                    log.warn("[CAS-LOGIN] getUserDetails: userInfoOptional empty");
                    return new UsernameNotFoundException(MsgUtils.getSecurityMessage(NOTFOUND_USER_ERROR_CODE));
                });
    }

    public UserDetails convertUserDetails(UserInfo info) {
        Set<String> dbAuthsSet = new HashSet<>();

        info.getRoleList().forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role.getRoleId()));
        dbAuthsSet.addAll(info.getPermissions());
        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));

        Long deptId = null;
        String deptName = null;
        if (CollUtil.isNotEmpty(info.getDeptList())) {
            deptId = CollUtil.getFirst(info.getDeptList()).getDeptId();
            deptName = CollUtil.getFirst(info.getDeptList()).getName();
        }

        log.info("[CAS-LOGIN] convertUserDetails username={}, userId={}, tenantId={}, deptId={}, roles={}, permsCount={}",
                info.getUsername(), info.getUserId(), info.getTenantId(), deptId,
                info.getRoleList() != null ? info.getRoleList().size() : 0,
                info.getPermissions() != null ? info.getPermissions().size() : 0);

        return new PigxUser(
                info.getUserId(), info.getUsername(), deptId, deptName, info.getPhone(), info.getAvatar(),
                info.getNickname(), info.getName(), info.getEmail(), info.getTenantId(),
                // 切记：不要在日志里打印这个字段
                SecurityConstants.NOOP + info.getPassword(),
                true, true, UserTypeEnum.TOB.getStatus(),
                !CommonConstants.STATUS_LOCK.equals(info.getPasswordExpireFlag()),
                !CommonConstants.STATUS_LOCK.equals(info.getLockFlag()),
                authorities, info.getCode()
        );
    }

    // check-token 使用
    @Override
    public UserDetails loadUserByUser(PigxUser pigxUser) {
        log.info("[CAS-LOGIN] loadUserByUser called, username={}", pigxUser != null ? pigxUser.getUsername() : "null");
        return this.loadUserByUsername(pigxUser.getUsername());
    }

    @Override
    public boolean support(String clientId, String grantType) {
        boolean ok = "cas".equals(clientId);
        log.info("[CAS-LOGIN] support? clientId={}, grantType={}, result={}", clientId, grantType, ok);
        return ok;
    }

    @Override
    public int getOrder() {
        int order = 2;
        log.info("[CAS-LOGIN] getOrder={}", order);
        return order;
    }

    public String validateTicket(String ticket) {
        String casHost = casProperties.getHost();
        String service = casProperties.getService();

        if (StrUtil.hasBlank(casHost, ticket, service)) {
            log.error("[CAS-LOGIN] validateTicket params blank: casHostBlank={}, ticketBlank={}, serviceBlank={}",
                    StrUtil.isBlank(casHost), StrUtil.isBlank(ticket), StrUtil.isBlank(service));
            throw new IllegalArgumentException("casHost, ticket and service must not be blank");
        }

        try {
            String base = StrUtil.endWith(casHost, "/") ? casHost : casHost + "/";
            String encodedService = URLUtil.encodeAll(service, java.nio.charset.StandardCharsets.UTF_8);
            String url = base + "serviceValidate?ticket=" + URLUtil.encodeAll(ticket, java.nio.charset.StandardCharsets.UTF_8)
                    + "&service=" + encodedService;

            log.info("[CAS-LOGIN] validateTicket request -> host={}, serviceLen={}, ticketLen={}, ticketHash8={}, url={}",
                    casHost, service.length(), ticket.length(), shortHash(ticket), url);

            long t0 = System.currentTimeMillis();
            String response = HttpUtil.get(url);
            long t1 = System.currentTimeMillis();
            log.info("[CAS-LOGIN] validateTicket http done, cost={}ms, respBlank={}",
                    (t1 - t0), StrUtil.isBlank(response));

            if (StrUtil.isBlank(response)) {
                log.warn("[CAS-LOGIN] validateTicket empty response");
                return null;
            }

            // 只截取一小段，避免日志爆炸/泄敏
            String respPreview = response.substring(0, Math.min(200, response.length())).replaceAll("\\s+", " ");
            log.info("[CAS-LOGIN] validateTicket respPreview(<=200)={}", respPreview);

            Document doc = XmlUtil.parseXml(response);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expr = "//*[local-name() = 'user']";
            NodeList nodeList = (NodeList) xpath.evaluate(expr, doc, XPathConstants.NODESET);

            if (nodeList == null || nodeList.getLength() == 0) {
                log.warn("[CAS-LOGIN] validateTicket: user node not found");
                return null;
            }

            String username = nodeList.item(0).getTextContent();
            String uname = StrUtil.isBlank(username) ? null : username.trim();
            log.info("[CAS-LOGIN] validateTicket parsed username={}", uname);
            return uname;

        } catch (Exception e) {
            log.error("[CAS-LOGIN] CAS ticket 验证失败: {} ", e.getMessage(), e);
            throw new RuntimeException("CAS ticket 验证失败", e);
        }
    }

    /* ========================= 私有工具方法 ========================= */

    private static String maskTicket(String ticket) {
        if (ticket == null) return "null";
        int len = ticket.length();
        if (len <= 6) return "***";
        return ticket.substring(0, 3) + "***" + ticket.substring(len - 3);
    }

    private static String shortHash(String s) {
        if (s == null) return "null";
        // 使用 md5 取前 8 位作为日志哈希标识
        return SecureUtil.md5(s).substring(0, 8);
    }

    private static List<String> authoritiesToStrings(Collection<? extends GrantedAuthority> auths) {
        if (auths == null) return Collections.emptyList();
        List<String> list = new ArrayList<>(auths.size());
        for (GrantedAuthority a : auths) {
            list.add(a.getAuthority());
        }
        return list;
    }
}
