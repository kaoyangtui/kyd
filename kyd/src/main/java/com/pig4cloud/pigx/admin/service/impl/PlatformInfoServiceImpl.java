package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoCreateRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoPageRequest;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoResponse;
import com.pig4cloud.pigx.admin.dto.platformInfo.PlatformInfoUpdateRequest;
import com.pig4cloud.pigx.admin.entity.PlatformInfoEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
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

    @Override
    public IPage<PlatformInfoResponse> pageResult(Page page, PlatformInfoPageRequest request) {
        LambdaQueryWrapper<PlatformInfoEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PlatformInfoEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(PlatformInfoEntity::getTitle, request.getKeyword())
                        .or()
                        .like(PlatformInfoEntity::getContent, request.getKeyword())
                );
            }
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<PlatformInfoEntity> entityPage = this.page(page, wrapper);
        return entityPage.convert(entity -> BeanUtil.copyProperties(entity, PlatformInfoResponse.class));
    }

    @SneakyThrows
    @Override
    public PlatformInfoResponse getDetail(Long id) {
        PlatformInfoEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return BeanUtil.copyProperties(entity, PlatformInfoResponse.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createInfo(PlatformInfoCreateRequest request) {
        PlatformInfoEntity entity = BeanUtil.copyProperties(request, PlatformInfoEntity.class);
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(PlatformInfoUpdateRequest request) {
        PlatformInfoEntity entity = BeanUtil.copyProperties(request, PlatformInfoEntity.class);
        return this.updateById(entity);
    }

}
