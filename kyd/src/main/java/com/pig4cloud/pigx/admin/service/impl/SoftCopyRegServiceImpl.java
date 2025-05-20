package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegCompleterEntity;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegOwnerEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyRegMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyRegCompleterService;
import com.pig4cloud.pigx.admin.service.SoftCopyRegOwnerService;
import com.pig4cloud.pigx.admin.service.SoftCopyRegService;
import com.pig4cloud.pigx.admin.vo.softCopyReg.SoftCopyRegCreateRequest;
import com.pig4cloud.pigx.admin.vo.softCopyReg.SoftCopyRegPageRequest;
import com.pig4cloud.pigx.admin.vo.softCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.vo.softCopyReg.SoftCopyRegUpdateRequest;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SoftCopyRegServiceImpl extends ServiceImpl<SoftCopyRegMapper, SoftCopyRegEntity> implements SoftCopyRegService {

    private final SoftCopyRegOwnerService ownerService;
    private final SoftCopyRegCompleterService completerService;

    @Override
    public IPage<SoftCopyRegResponse> pageResult(Page reqPage, SoftCopyRegPageRequest request) {
        LambdaQueryWrapper<SoftCopyRegEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(StrUtil.isNotBlank(request.getKeyword()), w ->
                w.like(SoftCopyRegEntity::getName, request.getKeyword())
                        .or().like(SoftCopyRegEntity::getRegNo, request.getKeyword()));
        wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), SoftCopyRegEntity::getDeptId, request.getDeptId());
        wrapper.ge(StrUtil.isNotBlank(request.getBeginCertDate()), SoftCopyRegEntity::getCertDate, request.getBeginCertDate());
        wrapper.le(StrUtil.isNotBlank(request.getEndCertDate()), SoftCopyRegEntity::getCertDate, request.getEndCertDate());

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            reqPage.setSize(request.getEndNo() - request.getStartNo() + 1);
            reqPage.setCurrent(1);
        } else if (request.getIds() != null && !request.getIds().isEmpty()) {
            reqPage.setSize(request.getIds().size());
            reqPage.setCurrent(1);
        }

        Page<SoftCopyRegEntity> page = baseMapper.selectPageByScope(
                reqPage,
                wrapper,
                DataScope.of()
        );

        return page.convert(entity -> BeanUtil.copyProperties(entity, SoftCopyRegResponse.class));
    }

    @Override
    public SoftCopyRegResponse getDetail(Long id) {
        SoftCopyRegEntity entity = this.getById(id);
        SoftCopyRegResponse response = BeanUtil.copyProperties(entity, SoftCopyRegResponse.class);
        response.setOwners(ownerService.lambdaQuery().eq(SoftCopyRegOwnerEntity::getSoftCopyRegId, id).list());
        response.setCompleters(completerService.lambdaQuery().eq(SoftCopyRegCompleterEntity::getSoftCopyRegId, id).list());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createReg(SoftCopyRegCreateRequest request) {
        SoftCopyRegEntity entity = BeanUtil.copyProperties(request.getMain(), SoftCopyRegEntity.class);
        this.save(entity);

        List<SoftCopyRegOwnerEntity> ownerEntities = BeanUtil.copyToList(request.getOwners(), SoftCopyRegOwnerEntity.class);
        ownerService.replaceOwners(entity.getId(), ownerEntities);

        List<SoftCopyRegCompleterEntity> completerEntities = BeanUtil.copyToList(request.getCompleters(), SoftCopyRegCompleterEntity.class);
        completerService.replaceCompleters(entity.getId(), completerEntities);

        return Boolean.TRUE;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateReg(SoftCopyRegUpdateRequest request) {
        SoftCopyRegEntity entity = BeanUtil.copyProperties(request.getMain(), SoftCopyRegEntity.class);
        this.updateById(entity);

        List<SoftCopyRegOwnerEntity> ownerEntities = BeanUtil.copyToList(request.getOwners(), SoftCopyRegOwnerEntity.class);
        ownerService.replaceOwners(entity.getId(), ownerEntities);

        List<SoftCopyRegCompleterEntity> completerEntities = BeanUtil.copyToList(request.getCompleters(), SoftCopyRegCompleterEntity.class);
        completerService.replaceCompleters(entity.getId(), completerEntities);

        return Boolean.TRUE;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRegs(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }
}
