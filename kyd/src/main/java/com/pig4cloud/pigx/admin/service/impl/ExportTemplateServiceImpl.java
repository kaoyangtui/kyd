package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.ExportTemplateEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ExportTemplateMapper;
import com.pig4cloud.pigx.admin.service.ExportTemplateService;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportTemplateCreateRequest;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportTemplatePageRequest;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportTemplateResponse;
import com.pig4cloud.pigx.admin.vo.exportExecute.ExportTemplateUpdateRequest;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户导出字段模板配置表
 *
 * @author pigx
 * @date 2025-05-12 09:07:01
 */
@Service
public class ExportTemplateServiceImpl extends ServiceImpl<ExportTemplateMapper, ExportTemplateEntity> implements ExportTemplateService {
    @Override
    public IPage<ExportTemplateResponse> pageTemplate(ExportTemplatePageRequest request) {
        LambdaQueryWrapper<ExportTemplateEntity> wrapper = Wrappers.lambdaQuery();

        wrapper.eq(StrUtil.isNotBlank(request.getBizCode()), ExportTemplateEntity::getBizCode, request.getBizCode())
                .like(StrUtil.isNotBlank(request.getTemplateName()), ExportTemplateEntity::getTemplateName, request.getTemplateName());

        Page<ExportTemplateEntity> page = baseMapper.selectPageByScope(
                new Page<>(request.getCurrent(), request.getSize()),
                wrapper,
                DataScope.of()
        );

        return page.convert(e -> BeanUtil.copyProperties(e, ExportTemplateResponse.class));
    }

    @Override
    public ExportTemplateResponse getDetail(Long id) {
        ExportTemplateEntity entity = this.getById(id);
        return Optional.ofNullable(entity)
                .map(e -> BeanUtil.copyProperties(e, ExportTemplateResponse.class))
                .orElse(null);
    }

    @Override
    public Boolean createTemplate(ExportTemplateCreateRequest request) {
        ExportTemplateEntity entity = BeanUtil.copyProperties(request, ExportTemplateEntity.class);
        return this.save(entity);
    }

    @Override
    public Boolean updateTemplate(ExportTemplateUpdateRequest request) {
        ExportTemplateEntity entity = BeanUtil.copyProperties(request, ExportTemplateEntity.class);
        return this.updateById(entity);
    }

    @Override
    public Boolean removeTemplates(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setDefault(Long id) {
        ExportTemplateEntity target = this.getById(id);
        if (ObjectUtil.isNull(target)) {
            throw new BizException("模板不存在");
        }
        LambdaUpdateWrapper<ExportTemplateEntity> resetWrapper = Wrappers.lambdaUpdate();
        resetWrapper.eq(ExportTemplateEntity::getBizCode, target.getBizCode())
                .eq(ExportTemplateEntity::getCreateBy, target.getCreateBy())
                .set(ExportTemplateEntity::getIsDefault, 0);
        this.update(resetWrapper);

        ExportTemplateEntity update = new ExportTemplateEntity();
        update.setId(id);
        update.setIsDefault(1);
        return this.updateById(update);
    }
}