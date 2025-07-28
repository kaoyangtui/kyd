package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.user.*;
import com.pig4cloud.pigx.admin.entity.UserEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.UserMapper;
import com.pig4cloud.pigx.admin.service.UserService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final UserMapper userMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendCode(String contactInfo) {
        //String code = RandomUtil.randomNumbers(6);
        String code = "123456";
        // 存 redis, 5分钟有效
        redisTemplate.opsForValue().set("user:code:" + contactInfo, code, 5, TimeUnit.MINUTES);
        // TODO: 这里调短信/邮箱网关发送 code
    }

    @SneakyThrows
    @Override
    @Transactional
    public boolean register(UserRegisterRequest request) {
        // 1. 验证码校验
        String redisKey = "user:code:" + request.getMobile();
        String rightCode = redisTemplate.opsForValue().get(redisKey);
        if (!StrUtil.equals(rightCode, request.getCode())) {
            throw new BizException("验证码错误");
        }
        // 2. 密码一致性校验
        if (!StrUtil.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new BizException("两次输入密码不一致");
        }
        // 3. 判断已注册
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getMobile, request.getMobile())
        );
        if (count > 0) {
            throw new BizException("该账号已注册");
        }
        // 4. 写库（密码加密）
        UserEntity entity = new UserEntity();
        entity.setUsername(request.getMobile());
        entity.setMobile(request.getMobile());
        entity.setContactInfo(request.getMobile());
        entity.setPassword(SecureUtil.md5(request.getPassword()));
        entity.setCreateTime(LocalDateTime.now());
        userMapper.insert(entity);
        // 5. 验证码失效
        redisTemplate.delete(redisKey);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean resetPwdStep1(ResetPwdStep1Request request) {
        String redisKey = "user:code:" + request.getMobile();
        String rightCode = redisTemplate.opsForValue().get(redisKey);
        if (!StrUtil.equals(rightCode, request.getCode())) {
            throw new BizException("验证码错误");
        }
        // 验证码校验通过，前端允许进入设置密码页
        return true;
    }

    @SneakyThrows
    @Override
    @Transactional
    public boolean resetPwdStep2(ResetPwdStep2Request request) {
        if (!StrUtil.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new BizException("两次输入密码不一致");
        }
        UserEntity user = userMapper.selectOne(
                new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getMobile, request.getMobile())
        );
        if (user == null) {
            throw new BizException("用户不存在");
        }
        user.setPassword(SecureUtil.md5(request.getPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return true;
    }

    @SneakyThrows
    @Override
    @Transactional
    public void updateProfile(UserProfileUpdateRequest request) {
        // 假设通过安全上下文获取当前用户ID
        Long userId = request.getId();
        if (userId == null) {
            userId = SecurityUtils.getUser().getId();
        }

        UserEntity entity = this.getById(userId);
        if (entity == null) {
            throw new BizException("用户不存在");
        }

        entity.setContactInfo(request.getContactInfo());
        entity.setOrganization(request.getOrganization());
        entity.setProvinceCode(request.getProvinceCode());
        entity.setProvinceName(request.getProvinceName());
        entity.setCityCode(request.getCityCode());
        entity.setCityName(request.getCityName());
        entity.setDistrictCode(request.getDistrictCode());
        entity.setDistrictName(request.getDistrictName());
        entity.setAddress(request.getAddress());
        entity.setUpdateTime(LocalDateTime.now());
        this.updateById(entity);
    }


    @SneakyThrows
    @Override
    @Transactional
    public Boolean updatePassword(UpdatePasswordRequest request) {
        if (!StrUtil.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new BizException("两次输入密码不一致");
        }

        UserEntity user = userMapper.selectById(SecurityUtils.getUser().getId());
        if (user == null) {
            throw new BizException("用户不存在");
        }
        if (!StrUtil.equalsIgnoreCase(user.getPassword(), SecureUtil.md5(request.getOldPassword()))) {
            throw new BizException("原密码不正确");
        }

        user.setPassword(SecureUtil.md5(request.getNewPassword()));
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return true;
    }

}
