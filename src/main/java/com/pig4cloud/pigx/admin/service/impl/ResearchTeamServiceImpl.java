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
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.admin.utils.ExportFieldHelper;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                        .like(ResearchTeamEntity::getResearchTags, request.getKeyword())
                        .or()
                        .like(ResearchTeamEntity::getIntro, request.getKeyword())
                );
            }
            wrapper.eq(ObjectUtil.isNotNull(request.getLeaderCode()), ResearchTeamEntity::getLeaderCode, request.getLeaderCode());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResearchTeamEntity::getShelfStatus, request.getShelfStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getDeptId()), ResearchTeamEntity::getDeptId, request.getDeptId());
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

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ResearchTeamEntity::getCreateTime);
        }

        IPage<ResearchTeamEntity> result = baseMapper.selectPage(page, wrapper);
        return result.convert(entity -> {
            ResearchTeamResponse response = CopyUtil.copyProperties(entity, ResearchTeamResponse.class);
            String code = entity.getCode();
            response.setResearchTags(StrUtil.split(entity.getResearchTags(), ";"));
            response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
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
        this.lambdaUpdate()
                .eq(ResearchTeamEntity::getId, id)
                .setSql("view_count = ifnull(view_count,0) + 1")
                .update();
        String code = entity.getCode();
        ResearchTeamResponse response = CopyUtil.copyProperties(entity, ResearchTeamResponse.class);
        response.setResearchTags(StrUtil.split(entity.getResearchTags(), ";"));
        response.setCompleters(completerService.lambdaQuery().eq(CompleterEntity::getCode, code).list());
        return response;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ResearchTeamCreateRequest request) {
        ResearchTeamEntity entity = CopyUtil.copyProperties(request, ResearchTeamEntity.class);
        entity.setCode(ParamResolver.getStr(ResearchTeamResponse.BIZ_CODE) + IdUtil.getSnowflakeNextIdStr());
        entity.setResearchTags(StrUtil.join(";", request.getResearchTags()));
        this.save(entity);
        String code = entity.getCode();
        // 关联团队成员（completers）
        completerService.replaceCompleters(code, request.getCompleters());
        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(ResearchTeamUpdateRequest request) {
        // 1) 先取业务编码（只查 code 一列）
        String code = this.lambdaQuery()
                .select(ResearchTeamEntity::getCode)
                .eq(ResearchTeamEntity::getId, request.getId())
                .oneOpt()
                .map(ResearchTeamEntity::getCode)
                .orElseThrow(() -> new BizException("团队不存在或已删除"));

        // 2) 组装要更新的字段（null 不会被 MP 更新，默认 NOT_NULL 策略）
        ResearchTeamEntity entity = CopyUtil.copyProperties(request, ResearchTeamEntity.class);
        entity.setResearchTags(
                CollUtil.isEmpty(request.getResearchTags()) ? null : String.join(";", request.getResearchTags())
        );

        boolean ok = this.updateById(entity);
        if (!ok) {
            throw new BizException("更新失败");
        }

        // 3) 使用已获取的 code 处理协作者
        completerService.replaceCompleters(code, request.getCompleters());
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
        return this.lambdaUpdate()
                .in(ResearchTeamEntity::getId, ids)
                .set(ResearchTeamEntity::getShelfStatus, shelfStatus)
                .set(ResearchTeamEntity::getShelfTime, LocalDateTime.now())
                .update();
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
