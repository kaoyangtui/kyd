package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.ResultEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResultMapper;
import com.pig4cloud.pigx.admin.service.ResultService;
import com.pig4cloud.pigx.admin.vo.*;
import com.pig4cloud.pigx.admin.vo.result.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科研成果表
 *
 * @author pigx
 * @date 2025-05-11 15:44:51
 */
@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, ResultEntity> implements ResultService {
    @Override
    public ResultResponse createResult(ResultCreateRequest request) {
        ResultEntity entity = BeanUtil.copyProperties(request, ResultEntity.class);
        entity.setCode("CG" + IdUtil.getSnowflakeNextIdStr());
        if (request.getTransWay() != null && !request.getTransWay().isEmpty()) {
            String transWayString = StrUtil.join(";", request.getTransWay());
            entity.setTransWay(transWayString);
        }
        if (request.getImgUrl() != null && !request.getImgUrl().isEmpty()) {
            String imgUrl = StrUtil.join(";", request.getImgUrl());
            entity.setImgUrl(imgUrl);
        }
        if (request.getFileUrl() != null && !request.getFileUrl().isEmpty()) {
            String fileUrl = StrUtil.join(";", request.getFileUrl());
            entity.setFileUrl(fileUrl);
        }
        save(entity);
        ResultResponse response = BeanUtil.copyProperties(entity, ResultResponse.class);
        response.setTransWay(StrUtil.split(entity.getTransWay(), ";"));
        response.setImgUrl(StrUtil.split(entity.getImgUrl(), ";"));
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }

    @SneakyThrows
    @Override
    public Boolean updateResult(ResultUpdateRequest request) {
        ResultEntity entity = getById(request.getId());
        if (entity == null) {
            throw new BizException("成果不存在");
        }
        BeanUtil.copyProperties(request, entity, CopyOptions.create().ignoreNullValue());
        if (request.getTransWay() != null && !request.getTransWay().isEmpty()) {
            String transWayString = StrUtil.join(";", request.getTransWay());
            entity.setTransWay(transWayString);
        }
        if (request.getImgUrl() != null && !request.getImgUrl().isEmpty()) {
            String imgUrl = StrUtil.join(";", request.getImgUrl());
            entity.setImgUrl(imgUrl);
        }
        if (request.getFileUrl() != null && !request.getFileUrl().isEmpty()) {
            String fileUrl = StrUtil.join(";", request.getFileUrl());
            entity.setFileUrl(fileUrl);
        }
        return updateById(entity);
    }

    @Override
    public IPage<ResultResponse> pageResult(Page reqPage, ResultPageRequest request) {
        LambdaQueryWrapper<ResultEntity> wrapper = Wrappers.lambdaQuery();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResultEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ResultEntity::getName, request.getKeyword())
                    .or().like(StrUtil.isNotBlank(request.getKeyword()), ResultEntity::getCode, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getSubject()), ResultEntity::getSubject, request.getSubject());
            wrapper.eq(StrUtil.isNotBlank(request.getCreateByDept()), ResultEntity::getDeptId, request.getCreateByDept());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResultEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getFlowStatus()), ResultEntity::getFlowStatus, request.getFlowStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getCurrentNodeName()), ResultEntity::getCurrentNodeName, request.getCurrentNodeName());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ResultEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ResultEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        IPage<ResultEntity> resPage = baseMapper.selectPageByScope(reqPage, wrapper, DataScope.of());

        return resPage.convert(entity -> {
            ResultResponse response = BeanUtil.copyProperties(entity, ResultResponse.class);
            response.setTransWay(StrUtil.split(entity.getTransWay(), ";"));
            response.setImgUrl(StrUtil.split(entity.getImgUrl(), ";"));
            response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
            return response;
        });
    }


    @Override
    public Boolean updateShelfStatus(ResultShelfRequest request) {
        return this.update(Wrappers.<ResultEntity>lambdaUpdate()
                .eq(ResultEntity::getId, request.getId())
                .set(ResultEntity::getShelfStatus, request.getStatus()));
    }

    @SneakyThrows
    @Override
    public ResultResponse getDetail(Long id) {
        ResultEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        ResultResponse response = BeanUtil.copyProperties(entity, ResultResponse.class);
        response.setTransWay(StrUtil.split(entity.getTransWay(), ";"));
        response.setImgUrl(StrUtil.split(entity.getImgUrl(), ";"));
        response.setFileUrl(StrUtil.split(entity.getFileUrl(), ";"));
        return response;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeResult(IdListRequest request) {
        List<Long> ids = request.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }
        return this.removeBatchByIds(ids);
    }

}