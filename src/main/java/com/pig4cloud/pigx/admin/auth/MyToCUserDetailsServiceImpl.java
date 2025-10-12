package com.pig4cloud.pigx.admin.auth;

import com.pig4cloud.pigx.admin.entity.UserEntity;
import com.pig4cloud.pigx.admin.service.UserService;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.service.PigxUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class MyToCUserDetailsServiceImpl implements PigxUserDetailsService {

    private final UserService userService;

    // 登录流程使用
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userService.lambdaQuery()
                .eq(UserEntity::getUsername, username)
                .one();
        return new PigxUser(user.getId(), user.getUsername(),
                1L,
                "",
                user.getMobile(),
                "",
                user.getNickname(),
                "", "", 1L,
                "{noop}" + user.getPassword(),
                true,
                true,
                "1",
                true,
                true,
                AuthorityUtils.NO_AUTHORITIES, "");
    }

    // check-token 使用
    @Override
    public UserDetails loadUserByUser(PigxUser pigxUser) {
        // 根据 pigxUser 里面的信息 查询对应表 返回 UserDetails,  根据实际情况修改
        return this.loadUserByUsername(pigxUser.getUsername());
    }


    @Override
    public boolean support(String clientId, String grantType) {
        return "custom".equals(clientId);
    }

    /**
     * 排序值
     *
     * @return 排序值
     */
    @Override
    public int getOrder() {
        return 1;
    }

}
