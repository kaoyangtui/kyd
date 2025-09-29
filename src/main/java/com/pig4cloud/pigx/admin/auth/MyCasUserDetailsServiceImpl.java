package com.pig4cloud.pigx.admin.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.XmlUtil;
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
        String username = "admin";
//        String username = validateTicket(ticket);
//        if (StrUtil.isBlank(username)) {
//            throw new BizException("用户不存在");
//        }
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
            UserInfo userInfo = cache.get(username, UserInfo.class);
            userInfo.setPassword(ticket);
            return getUserDetails(Optional.ofNullable(userInfo));
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        R<UserInfo> result = sysUserService.getUserInfo(userDTO);
        result.getData().setPassword(ticket);
        Optional<UserInfo> userInfoOptional = RetOps.of(result).getData();
        userInfoOptional.ifPresent(userInfo -> Objects.requireNonNull(cache).put(username, userInfo));
        return getUserDetails(userInfoOptional);

    }

    public UserDetails getUserDetails(Optional<UserInfo> userInfoOptional) {
        // @formatter:off
        return  userInfoOptional
                .map(this::convertUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(MsgUtils.getSecurityMessage(NOTFOUND_USER_ERROR_CODE)));
        // @formatter:on
    }

     public UserDetails convertUserDetails(UserInfo info) {
        Set<String> dbAuthsSet = new HashSet<>();

        // 维护角色列表
        info.getRoleList().forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role.getRoleId()));

        // 维护权限列表
        dbAuthsSet.addAll(info.getPermissions());
        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));


        // 构造security用户
        Long deptId = null;
        String deptName = null;
        if (CollUtil.isNotEmpty(info.getDeptList())) {
            deptId = CollUtil.getFirst(info.getDeptList()).getDeptId();
            deptName = CollUtil.getFirst(info.getDeptList()).getName();
        }
        return new PigxUser(info.getUserId(), info.getUsername(), deptId, deptName, info.getPhone(), info.getAvatar(),
                info.getNickname(), info.getName(), info.getEmail(), info.getTenantId(),
                SecurityConstants.NOOP + info.getPassword()
                , true, true, UserTypeEnum.TOB.getStatus()
                , !CommonConstants.STATUS_LOCK.equals(info.getPasswordExpireFlag()) // 密码过期判断
                , !CommonConstants.STATUS_LOCK.equals(info.getLockFlag()), authorities);
    }

    // check-token 使用
    @Override
    public UserDetails loadUserByUser(PigxUser pigxUser) {
        // 根据 pigxUser 里面的信息 查询对应表 返回 UserDetails,  根据实际情况修改
        return this.loadUserByUsername(pigxUser.getUsername());
    }


    @Override
    public boolean support(String clientId, String grantType) {
        return "cas".equals(clientId);
    }

    /**
     * 排序值
     *
     * @return 排序值
     */
    @Override
    public int getOrder() {
        return 2;
    }


    public String validateTicket(String ticket) {
        String casHost = casProperties.getHost();
        String service = casProperties.getService();

        if (StrUtil.hasBlank(casHost, ticket, service)) {
            throw new IllegalArgumentException("casHost, ticket and service must not be blank");
        }

        try {
            String base = StrUtil.endWith(casHost, "/") ? casHost : casHost + "/";
            String encodedService = URLUtil.encodeAll(service, java.nio.charset.StandardCharsets.UTF_8);
            String url = base + "serviceValidate?ticket=" + URLUtil.encodeAll(ticket, java.nio.charset.StandardCharsets.UTF_8)
                    + "&service=" + encodedService;

            String response = HttpUtil.get(url);
            if (StrUtil.isBlank(response)) {
                return null;
            }

            Document doc = XmlUtil.parseXml(response);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expr = "//*[local-name() = 'user']"; // 使用 local-name 绕过命名空间
            NodeList nodeList = (NodeList) xpath.evaluate(expr, doc, XPathConstants.NODESET);

            if (nodeList == null || nodeList.getLength() == 0) {
                return null;
            }

            String username = nodeList.item(0).getTextContent();
            return StrUtil.isBlank(username) ? null : username.trim();

        } catch (Exception e) {
            throw new RuntimeException("CAS ticket 验证失败", e);
        }
    }

}
