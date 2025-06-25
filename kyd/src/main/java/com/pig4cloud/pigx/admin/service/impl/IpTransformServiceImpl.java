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
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipTransform.IpTransformCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipTransform.IpTransformPageRequest;
import com.pig4cloud.pigx.admin.dto.ipTransform.IpTransformResponse;
import com.pig4cloud.pigx.admin.dto.ipTransform.IpTransformUpdateRequest;
import com.pig4cloud.pigx.admin.entity.IpTransformEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.IpTransformMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.IpTransformService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IpTransformServiceImpl extends ServiceImpl<IpTransformMapper, IpTransformEntity> implements IpTransformService {

    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IpTransformCreateRequest request) {
        doSaveOrUpdate(request, true);
        return true;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IpTransformUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return true;
    }

    @SneakyThrows
    @Override
    public IpTransformResponse getDetail(Long id) {
        IpTransformEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<IpTransformResponse> pageResult(Page page, IpTransformPageRequest request) {
        LambdaQueryWrapper<IpTransformEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IpTransformEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(IpTransformEntity::getName, request.getKeyword())
                                .or()
                                .like(IpTransformEntity::getCode, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IpTransformEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IpTransformEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), IpTransformEntity::getDeptId, request.getCreateByDept());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IpTransformEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IpTransformEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<IpTransformEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(this::convertToResponse);
    }

    private void doSaveOrUpdate(IpTransformCreateRequest request, boolean isCreate) {
        IpTransformEntity entity = BeanUtil.copyProperties(request, IpTransformEntity.class);
        entity.setIpCode(StrUtil.join(";", request.getIpCode()));

        if (CollUtil.isNotEmpty(request.getConsentFileUrl())) {
            request.getConsentFileUrl().replaceAll(f -> StrUtil.format(CommonConstants.FILE_GET_URL, f));
            entity.setConsentFileUrl(StrUtil.join(";", request.getConsentFileUrl()));
        }

        if (CollUtil.isNotEmpty(request.getPromiseFileUrl())) {
            request.getPromiseFileUrl().replaceAll(f -> StrUtil.format(CommonConstants.FILE_GET_URL, f));
            entity.setPromiseFileUrl(StrUtil.join(";", request.getPromiseFileUrl()));
        }

        List<FileCreateRequest> fileList = Lists.newArrayList();

        if (CollUtil.isNotEmpty(request.getConsentFileUrl())) {
            request.getConsentFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName, entity.getCode(),
                            IpTransformResponse.BIZ_CODE, entity.getName(),
                            FileBizTypeEnum.INVENTOR_CONSENT.getValue())));
        }

        if (CollUtil.isNotEmpty(request.getPromiseFileUrl())) {
            request.getPromiseFileUrl().forEach(fileName ->
                    fileList.add(fileService.getFileCreateRequest(fileName, entity.getCode(),
                            IpTransformResponse.BIZ_CODE, entity.getName(),
                            FileBizTypeEnum.TRANSFORM_COMMITMENT.getValue())));
        }

        if (!fileList.isEmpty()) {
            fileService.batchCreate(fileList);
        }

        if (!isCreate && request instanceof IpTransformUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private IpTransformResponse convertToResponse(IpTransformEntity entity) {
        IpTransformResponse response = BeanUtil.copyProperties(entity, IpTransformResponse.class);
        response.setIpCode(StrUtil.split(entity.getIpCode(), ";"));
        response.setConsentFileUrl(StrUtil.split(entity.getConsentFileUrl(), ";"));
        response.setPromiseFileUrl(StrUtil.split(entity.getPromiseFileUrl(), ";"));
        return response;
    }
}
