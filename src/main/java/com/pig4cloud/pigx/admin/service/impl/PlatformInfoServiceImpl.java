package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PlatformInfoEntity;
import com.pig4cloud.pigx.admin.mapper.PlatformInfoMapper;
import com.pig4cloud.pigx.admin.service.PlatformInfoService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhaoliang
 */
@Service
@RequiredArgsConstructor
public class PlatformInfoServiceImpl extends ServiceImpl<PlatformInfoMapper, PlatformInfoEntity> implements PlatformInfoService {


    @SneakyThrows
    @Override
    public PlatformInfoResponse getDetail() {
        PlatformInfoEntity entity = this.lambdaQuery().one();
        if (entity == null) {
            return null;
        }
        return BeanUtil.copyProperties(entity, PlatformInfoResponse.class);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(PlatformInfoUpdateRequest request) {
        PlatformInfoEntity entity = this.lambdaQuery().one();
        if (entity == null) {
            entity = BeanUtil.copyProperties(request, PlatformInfoEntity.class);
            return this.save(entity);
        } else {
            BeanUtil.copyProperties(request, entity);
            return this.updateById(entity);
        }
    }

}
