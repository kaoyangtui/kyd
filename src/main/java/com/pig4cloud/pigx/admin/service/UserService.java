package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.user.*;
import com.pig4cloud.pigx.admin.entity.UserEntity;
import jakarta.validation.Valid;

public interface UserService extends IService<UserEntity> {
    void sendCode(String contactInfo);

    boolean register(UserRegisterRequest request);

    boolean resetPwdStep1(ResetPwdStep1Request request);

    boolean resetPwdStep2(ResetPwdStep2Request request);

    void updateProfile(@Valid UserProfileUpdateRequest request);

    Boolean updatePassword(UpdatePasswordRequest request);

    IPage<UserResponse> pageResult(Page page, UserPageRequest request);

    Boolean removeUser(IdListRequest request);
}