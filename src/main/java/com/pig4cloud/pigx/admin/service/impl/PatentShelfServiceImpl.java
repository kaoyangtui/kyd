package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.dto.patent.PatentShelfRequest;
import com.pig4cloud.pigx.admin.entity.PatentShelfEntity;
import com.pig4cloud.pigx.admin.exception.BizException;
import com.pig4cloud.pigx.admin.mapper.PatentShelfMapper;
import com.pig4cloud.pigx.admin.service.PatentShelfService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 专利上下架
 *
 * @author pigx
 * @date 2025-06-03 22:04:46
 */
@Service
public class PatentShelfServiceImpl extends ServiceImpl<PatentShelfMapper, PatentShelfEntity> implements PatentShelfService {
    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean shelf(PatentShelfRequest req) {
        if (req == null || StrUtil.isBlank(req.getPid())) {
            throw new BizException("参数不完整");
        }
        // 查询当前专利上下架记录
        PatentShelfEntity old = this.getOne(new LambdaQueryWrapper<PatentShelfEntity>()
                .eq(PatentShelfEntity::getPid, req.getPid()));

        if (old == null) {
            // 新增
            PatentShelfEntity entity = new PatentShelfEntity();
            entity.setId(IdUtil.getSnowflakeNextId()); // 用你的ID生成器
            entity.setPid(req.getPid());
            entity.setShelfStatus(req.getShelfStatus());
            entity.setShelfTime(req.getShelfTime() != null ? req.getShelfTime() : LocalDateTime.now());
            entity.setCooperationMode(req.getCooperationMode());
            entity.setCooperationAmount(req.getCooperationAmount());
            return this.save(entity);
        } else {
            // 更新
            old.setShelfStatus(req.getShelfStatus());
            old.setShelfTime(req.getShelfTime() != null ? req.getShelfTime() : LocalDateTime.now());
            old.setCooperationMode(req.getCooperationMode());
            old.setCooperationAmount(req.getCooperationAmount());
            return this.updateById(old);
        }
    }

}