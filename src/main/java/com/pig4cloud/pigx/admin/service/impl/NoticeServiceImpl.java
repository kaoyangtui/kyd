package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.CommonConstants;
import com.pig4cloud.pigx.admin.dto.notice.NoticeCreateRequest;
import com.pig4cloud.pigx.admin.dto.notice.NoticePageRequest;
import com.pig4cloud.pigx.admin.dto.notice.NoticeResponse;
import com.pig4cloud.pigx.admin.dto.notice.NoticeUpdateRequest;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.entity.NoticeEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.NoticeMapper;
import com.pig4cloud.pigx.admin.service.NoticeService;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(NoticeCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(NoticeUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public NoticeResponse getDetail(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<NoticeResponse> pageResult(Page page, NoticePageRequest request) {
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(NoticeEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(NoticeEntity::getTitle, request.getKeyword())
                        .or()
                        .like(NoticeEntity::getContent, request.getKeyword()));
            }
            wrapper.eq(StrUtil.isNotBlank(request.getCreateBy()), NoticeEntity::getCreateBy, request.getCreateBy());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), NoticeEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), NoticeEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), NoticeEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(NoticeEntity::getCreateTime);
        }

        IPage<NoticeEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    private void doSaveOrUpdate(NoticeCreateRequest request, boolean isCreate) {
        NoticeEntity entity = CopyUtil.copyProperties(request, NoticeEntity.class);

        if (CollUtil.isNotEmpty(request.getFileUrl())) {
            entity.setFileUrl(StrUtil.join(";", request.getFileUrl()));
        }

        if (!isCreate && request instanceof NoticeUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private NoticeResponse convertToResponse(NoticeEntity entity) {
        NoticeResponse response = CopyUtil.copyProperties(entity, NoticeResponse.class);
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }
}
