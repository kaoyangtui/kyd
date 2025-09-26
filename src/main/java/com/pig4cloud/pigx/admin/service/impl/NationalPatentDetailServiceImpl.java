package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.NationalPatentDetailEntity;
import com.pig4cloud.pigx.admin.mapper.NationalPatentDetailMapper;
import com.pig4cloud.pigx.admin.service.NationalPatentDetailService;
import com.pig4cloud.pigx.admin.utils.CodeUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 全国专利搜索扩展信息表
 *
 * @author pigx
 * @date 2025-09-26 11:22:17
 */
@Service
public class NationalPatentDetailServiceImpl extends ServiceImpl<NationalPatentDetailMapper, NationalPatentDetailEntity> implements NationalPatentDetailService {

    @Override
    public void upsertBatchFromMessages(List<String> messages, int batchSize) {
        if (CollUtil.isEmpty(messages)) return;

        List<NationalPatentDetailEntity> candidates = new ArrayList<>(messages.size());
        for (String msg : messages) {
            NationalPatentDetailEntity e = JSONUtil.toBean(msg, NationalPatentDetailEntity.class);
            e.setTenantId(1L);
            e.setCitationForwardCountry(CodeUtils.formatCodes(e.getCitationForwardCountry()));
            candidates.add(e);
        }

        Map<String, NationalPatentDetailEntity> byPid = candidates.stream()
                .filter(it -> StrUtil.isNotBlank(it.getPid()))
                .collect(Collectors.toMap(NationalPatentDetailEntity::getPid, it -> it, (a, b) -> a, LinkedHashMap::new));

        if (byPid.isEmpty()) return;

        List<String> pids = new ArrayList<>(byPid.keySet());
        List<NationalPatentDetailEntity> existList = this.list(new LambdaQueryWrapper<NationalPatentDetailEntity>()
                .in(NationalPatentDetailEntity::getPid, pids));
        Set<String> existPidSet = existList.stream().map(NationalPatentDetailEntity::getPid).collect(Collectors.toSet());

        List<NationalPatentDetailEntity> toInsert = new ArrayList<>();
        List<NationalPatentDetailEntity> toUpdate = new ArrayList<>();

        for (Map.Entry<String, NationalPatentDetailEntity> en : byPid.entrySet()) {
            String pid = en.getKey();
            NationalPatentDetailEntity cur = en.getValue();
            if (existPidSet.contains(pid)) {
                NationalPatentDetailEntity old = existList.stream().filter(x -> pid.equals(x.getPid())).findFirst().orElse(null);
                if (old != null) {
                    cur.setId(old.getId());
                    toUpdate.add(cur);
                }
            } else {
                cur.setId(null);
                toInsert.add(cur);
            }
        }

        if (CollUtil.isNotEmpty(toInsert)) {
            this.saveBatch(toInsert, Math.max(200, batchSize));
        }
        if (CollUtil.isNotEmpty(toUpdate)) {
            this.updateBatchById(toUpdate, Math.max(200, batchSize));
        }
    }

}