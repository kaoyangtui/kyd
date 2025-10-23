package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.constants.ConsultTypeEnum;
import com.pig4cloud.pigx.admin.dto.consult.*;
import com.pig4cloud.pigx.admin.entity.*;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.ConsultMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.admin.utils.CopyUtil;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends ServiceImpl<ConsultMapper, ConsultEntity> implements ConsultService {

    private final ExpertService expertService;
    private final SysUserService sysUserService;
    private final PatentInfoService patentInfoService;
    private final ResultService resultService;
    private final DemandService demandService;
    private final DemandInService demandInService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ConsultCreateRequest request) {
        // 基础实体
        ConsultEntity entity = CopyUtil.copyProperties(request, ConsultEntity.class);
        entity.setStatus(0);
        entity.setReplyStatus(0);

        // 兜底的参数校验
        String targetCode = StrUtil.trimToEmpty(request.getTargetCode());
        if (StrUtil.isBlank(targetCode)) {
            throw new BizException("目标编码不能为空");
        }

        // 分发处理
        ConsultTypeEnum type = ConsultTypeEnum.of(request.getType());
        switch (type) {
            case PATENT -> {
                PatentInfoEntity p = patentInfoService.lambdaQuery()
                        .eq(PatentInfoEntity::getPid, targetCode)
                        .one();
                notNull(p, "专利不存在");
                // 负责人来自 leaderCode
                fillOwnerByUserCodeIfPresent(p.getLeaderCode(), entity);
            }
            case RESULT -> {
                ResultEntity r = resultService.lambdaQuery()
                        .eq(ResultEntity::getId, targetCode)
                        .one();
                notNull(r, "成果不存在");
                fillOwnerByUserCodeIfPresent(r.getLeaderCode(), entity);
            }
            case EXPERT -> {
                ExpertEntity e = expertService.getById(targetCode);
                notNull(e, "专家不存在");
                // 专家本身就是系统用户编码
                fillOwnerByUserCodeIfPresent(e.getCode(), entity);
            }
            case DEMAND -> {
                DemandEntity d = demandService.lambdaQuery()
                        .eq(DemandEntity::getId, targetCode)
                        .one();
                notNull(d, "企业需求不存在");
                // 创建人即责任人
                fillOwnerByUserCodeIfPresent(d.getCreateBy(), entity);
            }
            case DEMAND_IN -> {
                DemandInEntity di = demandInService.lambdaQuery()
                        .eq(DemandInEntity::getId, targetCode)
                        .one();
                notNull(di, "校内需求不存在");
                fillOwnerByUserCodeIfPresent(di.getCreateBy(), entity);
            }
            default -> throw new BizException("咨询类型错误");
        }

        return this.save(entity);
    }

    /* ====================== 私有工具方法 ====================== */

    /**
     * 如果 code 非空，查询并把用户信息回填到 entity（查询不到则忽略）
     */
    private void fillOwnerByUserCodeIfPresent(String userCode, ConsultEntity entity) {
        if (StrUtil.isBlank(userCode)) {
            return;
        }
        SysUser user = getUserByCode(userCode);
        if (user != null) {
            applyOwner(user, entity);
        }
    }

    /**
     * 仅取必要字段，避免 select * 带来额外负担
     */
    private SysUser getUserByCode(String code) {
        return sysUserService.lambdaQuery()
                .select(SysUser::getUserId, SysUser::getCode, SysUser::getUsername,
                        SysUser::getDeptId, SysUser::getDeptName)
                .eq(SysUser::getCode, code)
                .one();
    }

    /**
     * 把“归属/创建者/部门”等通用字段写入实体
     */
    private void applyOwner(SysUser user, ConsultEntity entity) {
        entity.setDeptId(user.getDeptId());
        entity.setDeptName(user.getDeptName());
        entity.setCreateBy(user.getCode());
        entity.setCreateUserId(user.getUserId());
        entity.setCreateUserName(user.getUsername());
    }

    /**
     * 小型断言工具，读起来更顺滑
     */
    private void notNull(Object obj, String msg) throws BizException {
        if (ObjectUtil.isNull(obj)) throw new BizException(msg);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean update(ConsultUpdateRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        ConsultEntity entity = CopyUtil.copyProperties(request, ConsultEntity.class);
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    @Transactional
    public Boolean reply(ConsultReplyRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new BizException("ID不能为空");
        }
        ConsultEntity entity = this.getById(request.getId());
        if (entity == null) {
            throw new BizException("数据不存在");
        }
        entity.setReplyBy(SecurityUtils.getUser().getNickname());
        entity.setReplyTime(LocalDateTime.now());
        entity.setReplyContent(request.getReplyContent());
        entity.setReplyStatus(1);
        return this.updateById(entity);
    }

    @SneakyThrows
    @Override
    public ConsultResponse getDetail(Long id) {
        ConsultEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        ConsultResponse consultResponse = CopyUtil.copyProperties(entity, ConsultResponse.class);
        consultResponse.setTypeName(ConsultTypeEnum.of(entity.getType()).getValue());
        return consultResponse;
    }

    @Override
    public Boolean removeByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public IPage<ConsultResponse> pageResult(Page page, ConsultPageRequest request) {
        LambdaQueryWrapper<ConsultEntity> wrapper = new LambdaQueryWrapper<>();

        if (CollUtil.isNotEmpty(request.getIds())) {
            wrapper.in(ConsultEntity::getId, request.getIds());
        } else {
            wrapper.eq(null != request.getUserId(), ConsultEntity::getUserId, request.getUserId());
            wrapper.like(StrUtil.isNotBlank(request.getKeyword()), ConsultEntity::getContent, request.getKeyword());
            wrapper.eq(StrUtil.isNotBlank(request.getType()), ConsultEntity::getType, request.getType());
            wrapper.eq(ObjectUtil.isNotNull(request.getStatus()), ConsultEntity::getStatus, request.getStatus());
            wrapper.eq(ObjectUtil.isNotNull(request.getReplyStatus()), ConsultEntity::getReplyStatus, request.getReplyStatus());
            wrapper.ge(StrUtil.isNotBlank(request.getBeginTime()), ConsultEntity::getCreateTime, request.getBeginTime());
            wrapper.le(StrUtil.isNotBlank(request.getEndTime()), ConsultEntity::getCreateTime, request.getEndTime());
        }

        if (ObjectUtil.isAllNotEmpty(request.getStartNo(), request.getEndNo())) {
            page.setSize(request.getEndNo() - request.getStartNo() + 1);
            page.setCurrent(1);
        } else if (CollUtil.isNotEmpty(request.getIds())) {
            page.setSize(request.getIds().size());
            page.setCurrent(1);
        }

        if (CollUtil.isEmpty(page.orders())) {
            wrapper.orderByDesc(ConsultEntity::getCreateTime);
        }

        IPage<ConsultEntity> entityPage = baseMapper.selectPageByScope(page, wrapper, DataScope.of());
        return entityPage.convert(e -> {
            ConsultResponse consultResponse = CopyUtil.copyProperties(e, ConsultResponse.class);
            consultResponse.setTypeName(ConsultTypeEnum.of(e.getType()).getValue());
            return consultResponse;
        });
    }

    @SneakyThrows
    @Override
    public ConsultResponse getDetailRead(Long id) {
        ConsultEntity entity = this.getById(id);
        if (ObjectUtil.isNull(entity)) {
            throw new BizException("数据不存在");
        }
        if (null == entity.getStatus() || entity.getStatus() == 0) {
            this.lambdaUpdate()
                    .eq(ConsultEntity::getId, id)
                    .set(ConsultEntity::getStatus, 1)
                    .update();
        }
        return CopyUtil.copyProperties(entity, ConsultResponse.class);
    }
}