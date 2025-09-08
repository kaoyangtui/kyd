package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;
import com.pig4cloud.pigx.admin.mapper.CompleterMapper;
import com.pig4cloud.pigx.admin.service.CompleterService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 完成人
 *
 * @author pigx
 * @date 2025-05-23 14:31:27
 */
@RequiredArgsConstructor
@Service
public class CompleterServiceImpl extends ServiceImpl<CompleterMapper, CompleterEntity> implements CompleterService {

    private final SysDeptService sysDeptService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void replaceCompleters(String code, List<CompleterEntity> entities) {
        this.remove(Wrappers.<CompleterEntity>lambdaQuery()
                .eq(CompleterEntity::getCode, code));
        if (CollUtil.isNotEmpty(entities)) {
            entities.forEach(e -> {
                e.setId(null);
                e.setCode(code);
                e.setCompleterDeptName(sysDeptService.getById(e.getCompleterDeptId()).getName());
            });
            this.saveBatch(entities);
        }
    }

    /**
     * 完成人 -> 参与人 DTO 映射
     */
    @Override
    public List<PerfParticipantDTO> toParticipants(List<CompleterEntity> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(c -> PerfParticipantDTO.builder()
                        .userId(null) // 若有学工号映射用户ID，可在此补充
                        .userCode(c.getCompleterNo())
                        .userName(c.getCompleterName())
                        .deptId(c.getCompleterDeptId())
                        .deptName(c.getCompleterDeptName())
                        .priority(null) // 软著完成人未提供顺位字段，可扩展
                        .isLeader(c.getCompleterLeader())
                        .build())
                .toList();
    }
}