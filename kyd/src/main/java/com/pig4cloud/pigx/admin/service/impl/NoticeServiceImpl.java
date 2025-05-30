package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.notice.*;
import com.pig4cloud.pigx.admin.entity.NoticeEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.NoticeMapper;
import com.pig4cloud.pigx.admin.service.NoticeService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

    @Override
    public IPage<NoticeResponse> pageResult(Page page, NoticePageRequest request) {
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(NoticeEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), NoticeEntity::getTitle, request.getKeyword())
                    .or().like(StrUtil.isNotBlank(request.getKeyword()), NoticeEntity::getContent, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), NoticeEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), NoticeEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), NoticeEntity::getCreateTime, request.getEndTime());
        }
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }
        IPage<NoticeEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(entity -> {
            NoticeResponse response = BeanUtil.copyProperties(entity, NoticeResponse.class);
            response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
            return response;
        });
    }

    @SneakyThrows
    @Override
    public NoticeResponse getDetail(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        NoticeResponse response = BeanUtil.copyProperties(entity, NoticeResponse.class);
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(NoticeCreateRequest request) {
        NoticeEntity entity = BeanUtil.copyProperties(request, NoticeEntity.class);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(NoticeUpdateRequest request) {
        NoticeEntity entity = BeanUtil.copyProperties(request, NoticeEntity.class);
        entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

}
