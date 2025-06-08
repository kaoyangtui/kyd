package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyCreateRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyPageRequest;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyResponse;
import com.pig4cloud.pigx.admin.dto.plantVariety.PlantVarietyUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.PlantVarietyEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PlantVarietyMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.PlantVarietyService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantVarietyServiceImpl extends ServiceImpl<PlantVarietyMapper, PlantVarietyEntity> implements PlantVarietyService {

    private final FileService fileService;
    private final CompleterService completerService;
    private final OwnerService ownerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(PlantVarietyCreateRequest request) {
        PlantVarietyEntity entity = BeanUtil.copyProperties(request, PlantVarietyEntity.class);
        entity.setCode(ParamResolver.getStr(PlantVarietyResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));
            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PlantVarietyResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.PLANT_VARIETY.getValue()
                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
        }

        request.getCompleters().forEach(completer -> {
            if (completer.getCompleterLeader() == 1) {
                entity.setLeaderCode(completer.getCompleterNo());
                entity.setLeaderName(completer.getCompleterName());
            }
        });

        this.save(entity);
        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PlantVarietyUpdateRequest request) {
        PlantVarietyEntity entity = BeanUtil.copyProperties(request, PlantVarietyEntity.class);

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));
            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        PlantVarietyResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.PLANT_VARIETY.getValue()
                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
        }

        request.getCompleters().forEach(completer -> {
            if (completer.getCompleterLeader() == 1) {
                entity.setLeaderCode(completer.getCompleterNo());
                entity.setLeaderName(completer.getCompleterName());
            }
        });

        this.updateById(entity);
        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @SneakyThrows
    @Override
    public PlantVarietyResponse getDetail(Long id) {
        PlantVarietyEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        PlantVarietyResponse response = BeanUtil.copyProperties(entity, PlantVarietyResponse.class);
        response.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        response.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return response;
    }

    @Override
    public IPage<PlantVarietyResponse> pageResult(Page<PlantVarietyEntity> page, PlantVarietyPageRequest request) {
        LambdaQueryWrapper<PlantVarietyEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(PlantVarietyEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(PlantVarietyEntity::getName, request.getKeyword())
                                .or()
                                .like(PlantVarietyEntity::getRightNo, request.getKeyword())
                );
            }
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), PlantVarietyEntity::getDeptId, request.getDeptId());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), PlantVarietyEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), PlantVarietyEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), PlantVarietyEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), PlantVarietyEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<PlantVarietyEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return resPage.convert(entity -> {
            PlantVarietyResponse response = BeanUtil.copyProperties(entity, PlantVarietyResponse.class);
            response.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
            return response;
        });
    }
}
