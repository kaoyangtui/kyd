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
import com.pig4cloud.pigx.admin.dto.exportExecute.ExportFieldListResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamCreateRequest;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamPageRequest;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamResponse;
import com.pig4cloud.pigx.admin.dto.researchTeam.ResearchTeamUpdateRequest;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.entity.ResearchTeamEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ResearchTeamMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.ResearchTeamService;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchTeamServiceImpl extends ServiceImpl<ResearchTeamMapper, ResearchTeamEntity> implements ResearchTeamService {

    private final CompleterService completerService;

    @Override
    public IPage<ResearchTeamResponse> pageResult(Page page, ResearchTeamPageRequest request) {
        LambdaQueryWrapper<ResearchTeamEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchTeamEntity::getId, request.getIds());
        } else {
            if (StrUtil.isNotBlank(request.getKeyword())) {
                wrapper.and(w -> w
                        .like(ResearchTeamEntity::getName, request.getKeyword())
                        .or()
                        .like(ResearchTeamEntity::getLeader, request.getKeyword())
                );
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResearchTeamEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(StrUtil.isNotBlank(request.getDeptId()), ResearchTeamEntity::getDeptId, request.getDeptId());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ResearchTeamEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ResearchTeamEntity::getCreateTime, request.getEndTime());
        }

        // ids优先，支持startNo/endNo
        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<ResearchTeamEntity> result = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return result.convert(entity -> {
            ResearchTeamResponse response = BeanUtil.copyProperties(entity, ResearchTeamResponse.class);
            response.setResearchTags(StrUtil.split(entity.getResearchTags(), ";"));
            response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
            return response;
        });
    }

    @SneakyThrows
    @Override
    public ResearchTeamResponse getDetail(Long id) {
        ResearchTeamEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("科研团队不存在");
        }
        ResearchTeamResponse response = BeanUtil.copyProperties(entity, ResearchTeamResponse.class);
        response.setResearchTags(StrUtil.split(entity.getResearchTags(), ";"));
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, entity.getCode()).list());
        return response;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ResearchTeamCreateRequest request) {
        ResearchTeamEntity entity = BeanUtil.copyProperties(request, ResearchTeamEntity.class);
        entity.setCode(ParamResolver.getStr(ResearchTeamResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        entity.setResearchTags(StrUtil.join(";", request.getResearchTags()));

        this.save(entity);

        // 关联团队成员（completers）
        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ResearchTeamUpdateRequest request) {
        ResearchTeamEntity entity = BeanUtil.copyProperties(request, ResearchTeamEntity.class);
        entity.setResearchTags(StrUtil.join(";", request.getResearchTags()));

        this.updateById(entity);

        completerService.replaceCompleters(entity.getCode(), request.getCompleters());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean shelfByIds(List<Long> ids, Integer shelfStatus) {
        return this.updateBatchById(
                this.listByIds(ids).stream().peek(e -> e.setShelfStatus(shelfStatus)).toList()
        );
    }

    @Override
    public List<ResearchTeamResponse> exportList(ResearchTeamPageRequest request) {
        Page<ResearchTeamEntity> page = new Page<>();
        IPage<ResearchTeamResponse> resultPage = this.pageResult(page, request);
        return resultPage.getRecords();
    }

    @Override
    public ExportFieldListResponse exportFields() {
        return ExportFieldHelper.buildExportFieldList(
                ResearchTeamResponse.BIZ_CODE,
                ResearchTeamResponse.class
        );
    }
}
