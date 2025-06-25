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
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignCreateRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignPageRequest;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignResponse;
import com.pig4cloud.pigx.admin.dto.ipAssign.IpAssignUpdateRequest;
import com.pig4cloud.pigx.admin.entity.IpAssignEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.IpAssignMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.IpAssignService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IpAssignServiceImpl extends ServiceImpl<IpAssignMapper, IpAssignEntity> implements IpAssignService {

    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(IpAssignCreateRequest request) {
        doSaveOrUpdate(request, true);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(IpAssignUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("更新失败：ID不能为空");
        }
        doSaveOrUpdate(request, false);
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public IpAssignResponse getDetail(Long id) {
        IpAssignEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("赋权信息不存在");
        }
        return convertToResponse(entity);
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public Page<IpAssignResponse> pageResult(Page page, IpAssignPageRequest request) {
        LambdaQueryWrapper<IpAssignEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(IpAssignEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(IpAssignEntity::getCode, request.getKeyword())
                                .or()
                                .like(IpAssignEntity::getAssignToCode, request.getKeyword()));
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), IpAssignEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), IpAssignEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), IpAssignEntity::getDeptId, request.getCreateByDept());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), IpAssignEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), IpAssignEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<IpAssignEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        Page<IpAssignResponse> resultPage = new Page<>(page.getCurrent(), page.getSize(), entityPage.getTotal());

        List<IpAssignResponse> records = entityPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        resultPage.setRecords(records);
        return resultPage;
    }

    private void doSaveOrUpdate(IpAssignCreateRequest request, boolean isCreate) {
        IpAssignEntity entity = BeanUtil.copyProperties(request, IpAssignEntity.class);

        if (CollUtil.isNotEmpty(request.getProofFileUrl())) {
            request.getProofFileUrl().replaceAll(f -> StrUtil.format(CommonConstants.FILE_GET_URL, f));
            entity.setProofFileUrl(StrUtil.join(";", request.getProofFileUrl()));
        }

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            request.getAttachFileUrl().replaceAll(f -> StrUtil.format(CommonConstants.FILE_GET_URL, f));
            entity.setAttachFileUrl(StrUtil.join(";", request.getAttachFileUrl()));
        }

        List<FileCreateRequest> fileList = CollUtil.newArrayList();

        if (CollUtil.isNotEmpty(request.getProofFileUrl())) {
            request.getProofFileUrl().forEach(fileName -> {
                fileList.add(fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        IpAssignResponse.BIZ_CODE,
                        "赋权",
                        FileBizTypeEnum.INVENTOR_CONSENT.getValue()));
            });
        }

        if (CollUtil.isNotEmpty(request.getAttachFileUrl())) {
            request.getAttachFileUrl().forEach(fileName -> {
                fileList.add(fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        IpAssignResponse.BIZ_CODE,
                        "赋权",
                        FileBizTypeEnum.ASSIGNMENT_REQUEST.getValue()));
            });
        }

        if (!fileList.isEmpty()) {
            fileService.batchCreate(fileList);
        }

        if (!isCreate && request instanceof IpAssignUpdateRequest updateRequest) {
            entity.setId(updateRequest.getId());
            this.updateById(entity);
        } else {
            this.save(entity);
        }
    }

    private IpAssignResponse convertToResponse(IpAssignEntity entity) {
        IpAssignResponse response = BeanUtil.copyProperties(entity, IpAssignResponse.class);
        response.setProofFileUrl(StrUtil.split(entity.getProofFileUrl(), ";"));
        response.setAttachFileUrl(StrUtil.split(entity.getAttachFileUrl(), ";"));
        return response;
    }
}

