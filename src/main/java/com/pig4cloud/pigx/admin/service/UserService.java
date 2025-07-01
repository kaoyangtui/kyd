package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.user.ResetPwdStep1Request;
import com.pig4cloud.pigx.admin.dto.user.ResetPwdStep2Request;
import com.pig4cloud.pigx.admin.dto.user.UserProfileUpdateRequest;
import com.pig4cloud.pigx.admin.dto.user.UserRegisterRequest;
import com.pig4cloud.pigx.admin.entity.UserEntity;
import jakarta.validation.Valid;

public interface UserService extends IService<UserEntity> {
    void sendCode(String contactInfo);

    boolean register(UserRegisterRequest request);

    boolean resetPwdStep1(ResetPwdStep1Request request);

    boolean resetPwdStep2(ResetPwdStep2Request request);

    void updateProfile(@Valid UserProfileUpdateRequest request);
}