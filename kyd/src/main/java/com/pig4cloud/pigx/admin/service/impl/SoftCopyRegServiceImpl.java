package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.constants.FileBizTypeEnum;
import com.pig4cloud.pigx.admin.dto.file.FileCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegCreateRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegPageRequest;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.dto.softCopyReg.SoftCopyRegUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.OwnerEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.SoftCopyRegMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.service.OwnerService;
import com.pig4cloud.pigx.admin.service.SoftCopyRegService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SoftCopyRegServiceImpl extends ServiceImpl<SoftCopyRegMapper, SoftCopyRegEntity> implements SoftCopyRegService {

    private final FileService fileService;
    private final OwnerService ownerService;
    private final CompleterService completerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(SoftCopyRegCreateRequest request) {
        SoftCopyRegEntity entity = BeanUtil.copyProperties(request, SoftCopyRegEntity.class);
        entity.setCode(ParamResolver.getStr(SoftCopyRegResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));

            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        SoftCopyRegResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.SOFT_COPY.getValue()
                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
        }

        request.getCompleters().forEach(c -> {
            if (c.getCompleterLeader() == 1) {
                entity.setLeaderCode(c.getCompleterNo());
                entity.setLeaderName(c.getCompleterName());
            }
        });

        this.save(entity);
        String code = entity.getCode();
        completerService.replaceCompleters(code, request.getCompleters());
        ownerService.replaceOwners(code, request.getOwners());
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(SoftCopyRegUpdateRequest request) {
        SoftCopyRegEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("数据不存在");
        }

        BeanUtil.copyProperties(request, entity);

        if (CollUtil.isNotEmpty(request.getCertFileUrl())) {
            entity.setCertFileUrl(StrUtil.join(";", request.getCertFileUrl()));

            List<FileCreateRequest> fileList = Lists.newArrayList();
            request.getCertFileUrl().forEach(fileName -> {
                FileCreateRequest file = fileService.getFileCreateRequest(
                        fileName,
                        entity.getCode(),
                        SoftCopyRegResponse.BIZ_CODE,
                        entity.getName(),
                        FileBizTypeEnum.SOFT_COPY.getValue()

                );
                fileList.add(file);
            });
            fileService.batchCreate(fileList);
        }

        request.getCompleters().forEach(c -> {
            if (c.getCompleterLeader() == 1) {
                entity.setLeaderCode(c.getCompleterNo());
                entity.setLeaderName(c.getCompleterName());
            }
        });

        this.updateById(entity);
        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        ownerService.replaceOwners(entity.getCode(), request.getOwners());
        return Boolean.TRUE;
    }

    @Override
    public IPage<SoftCopyRegResponse> pageResult(Page page, SoftCopyRegPageRequest request) {
        LambdaQueryWrapper<SoftCopyRegEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(SoftCopyRegEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w ->
                        w.like(SoftCopyRegEntity::getName, request.getKeyword())
                                .or()
                                .like(SoftCopyRegEntity::getRegNo, request.getKeyword())
                );
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), SoftCopyRegEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), SoftCopyRegEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), SoftCopyRegEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), SoftCopyRegEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), SoftCopyRegEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<SoftCopyRegEntity> entityPage = this.baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        return entityPage.convert(entity -> {
            SoftCopyRegResponse res = BeanUtil.copyProperties(entity, SoftCopyRegResponse.class);
            res.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
            return res;
        });
    }

    @SneakyThrows
    @Override
    public SoftCopyRegResponse getDetail(Long id) {
        SoftCopyRegEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }

        SoftCopyRegResponse res = BeanUtil.copyProperties(entity, SoftCopyRegResponse.class);
        res.setCertFileUrl(StrUtil.split(entity.getCertFileUrl(), ";"));
        res.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        res.setOwners(ownerService.lambdaQuery().eq(OwnerEntity::getCode, entity.getCode()).list());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }
}
