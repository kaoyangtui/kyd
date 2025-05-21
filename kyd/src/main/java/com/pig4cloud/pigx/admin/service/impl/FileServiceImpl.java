package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.FileEntity;
import com.pig4cloud.pigx.admin.entity.IcLayoutEntity;
import com.pig4cloud.pigx.admin.mapper.FileMapper;
import com.pig4cloud.pigx.admin.service.FileService;
import com.pig4cloud.pigx.admin.vo.file.*;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(FileCreateRequest request) {
        FileEntity entity = BeanUtil.copyProperties(request, FileEntity.class);
        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchCreate(List<FileCreateRequest> requestList) {
        if (CollUtil.isEmpty(requestList)) {
            return Boolean.TRUE;
        }

        // 提取所有 code 字段，非空去重
        List<String> codeList = requestList.stream()
                .map(FileCreateRequest::getCode)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .toList();

        if (CollUtil.isNotEmpty(codeList)) {
            // 先删除已有的相同 code 的记录
            this.remove(Wrappers.<FileEntity>lambdaQuery().in(FileEntity::getCode, codeList));
        }

        // 执行批量保存
        List<FileEntity> entityList = BeanUtil.copyToList(requestList, FileEntity.class);
        return this.saveBatch(entityList);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(FileUpdateRequest request) {
        FileEntity entity = BeanUtil.copyProperties(request, FileEntity.class);
        return this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<FileResponse> pageResult(Page page, FilePageRequest request) {
        var wrapper = Wrappers.<FileEntity>lambdaQuery()
                .in(ObjectUtil.isNotEmpty(request.getIds()), FileEntity::getId, request.getIds())
                .like(StrUtil.isNotBlank(request.getCode()), FileEntity::getCode, request.getCode())
                .eq(StrUtil.isNotBlank(request.getApplyType()), FileEntity::getApplyType, request.getApplyType())
                .like(StrUtil.isNotBlank(request.getSubjectName()), FileEntity::getSubjectName, request.getSubjectName())
                .eq(StrUtil.isNotBlank(request.getBizType()), FileEntity::getBizType, request.getBizType())
                .like(StrUtil.isNotBlank(request.getFileName()), FileEntity::getFileName, request.getFileName())
                .eq(StrUtil.isNotBlank(request.getFileType()), FileEntity::getFileType, request.getFileType())
                .eq(StrUtil.isNotBlank(request.getCreateBy()), FileEntity::getCreateBy, request.getCreateBy())
                .eq(StrUtil.isNotBlank(request.getDeptId()), FileEntity::getDeptId, request.getDeptId())
                .ge(StrUtil.isNotBlank(request.getBeginTime()), FileEntity::getCreateTime, request.getBeginTime())
                .le(StrUtil.isNotBlank(request.getEndTime()), FileEntity::getCreateTime, request.getEndTime());

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        Page<IcLayoutEntity> resPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        return resPage.convert(e -> BeanUtil.copyProperties(e, FileResponse.class));
    }
}