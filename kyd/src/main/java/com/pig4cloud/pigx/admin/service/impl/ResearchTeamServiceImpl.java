package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.IdListRequest;
import com.pig4cloud.pigx.admin.dto.researchTeam.*;
import com.pig4cloud.pigx.admin.entity.ResearchTeamEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.service.ResearchTeamService;
import com.pig4cloud.pigx.admin.mapper.ResearchTeamMapper;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhaoliang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResearchTeamServiceImpl extends ServiceImpl<ResearchTeamMapper, ResearchTeamEntity> implements ResearchTeamService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResearchTeamResponse createTeam(ResearchTeamCreateRequest request) {
        ResearchTeamEntity entity = BeanUtil.copyProperties(request, ResearchTeamEntity.class);
        this.save(entity);

        //researchTeamMemberService.replaceMembers(entity.getId(), request.getMemberList());

        return buildResponse(entity);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTeam(ResearchTeamUpdateRequest request) {
        ResearchTeamEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("科研团队不存在");
        }

        BeanUtil.copyProperties(request, entity, CopyOptions.create().ignoreNullValue());
        this.updateById(entity);

        //researchTeamMemberService.replaceMembers(entity.getId(), request.getMemberList());

        return Boolean.TRUE;
    }

    @SneakyThrows
    @Override
    public ResearchTeamResponse getDetail(Long id) {
        ResearchTeamEntity entity = this.getById(id);
        if (entity == null) {
            throw new BizException("科研团队不存在");
        }

        return buildResponse(entity);
    }

    @Override
    public IPage<ResearchTeamResponse> pageResult(Page page, ResearchTeamPageRequest request) {
        var wrapper = Wrappers.lambdaQuery(ResearchTeamEntity.class);

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ResearchTeamEntity::getId, request.getIds());
        } else {
            wrapper.like(StrUtil.isNotBlank(request.getName()), ResearchTeamEntity::getName, request.getName());
            wrapper.eq(StrUtil.isNotBlank(request.getLeader()), ResearchTeamEntity::getLeader, request.getLeader());
            wrapper.eq(ObjectUtil.isNotNull(request.getShelfStatus()), ResearchTeamEntity::getShelfStatus, request.getShelfStatus());
        }

        if (ObjectUtil.isNotNull(request.getStartNo()) && ObjectUtil.isNotNull(request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        IPage<ResearchTeamEntity> entityPage = this.baseMapper.selectPageByScope(page, wrapper, DataScope.of());

        return entityPage.convert(this::buildResponse);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTeam(IdListRequest request) {
        List<Long> ids = request.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw new BizException("ID列表不能为空");
        }

        this.removeBatchByIds(ids);
        //researchTeamMemberService.removeByTeamIds(ids);

        return Boolean.TRUE;
    }

    @Override
    public Boolean updateShelfStatus(ResearchTeamShelfRequest request) {
        return this.update(Wrappers.lambdaUpdate(ResearchTeamEntity.class)
                .eq(ResearchTeamEntity::getId, request.getId())
                .set(ResearchTeamEntity::getShelfStatus, request.getShelfStatus()));
    }

    private ResearchTeamResponse buildResponse(ResearchTeamEntity entity) {
        return BeanUtil.copyProperties(entity, ResearchTeamResponse.class);
    }
}
