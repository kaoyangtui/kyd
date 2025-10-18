package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.demand.DemandSignupRequest;
import com.pig4cloud.pigx.admin.entity.DemandSignupEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.DemandSignupMapper;
import com.pig4cloud.pigx.admin.service.DemandSignupService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemandSignupServiceImpl extends ServiceImpl<DemandSignupMapper, DemandSignupEntity> implements DemandSignupService {

    @SneakyThrows
    @Override
    @Transactional
    public Boolean signup(DemandSignupRequest request) {
        if (ObjectUtil.isNull(request.getDemandId())) {
            throw new BizException("关联需求ID不能为空");
        }
        DemandSignupEntity entity = CopyUtil.copyProperties(request, DemandSignupEntity.class);
        return this.save(entity);
    }
}
