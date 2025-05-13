package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegCompleterEntity;
import com.pig4cloud.pigx.admin.mapper.SoftCopyRegCompleterMapper;
import com.pig4cloud.pigx.admin.service.SoftCopyRegCompleterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 软著登记完成人信息
 *
 * @author pigx
 * @date 2025-05-13 10:53:39
 */
@Service
public class SoftCopyRegCompleterServiceImpl extends ServiceImpl<SoftCopyRegCompleterMapper, SoftCopyRegCompleterEntity> implements SoftCopyRegCompleterService {
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean replaceCompleters(Long regId, List<SoftCopyRegCompleterEntity> completers) {
        // 先删
        this.lambdaUpdate().eq(SoftCopyRegCompleterEntity::getSoftCopyRegId, regId).remove();

        // 再插
        if (CollUtil.isNotEmpty(completers)) {
            completers.forEach(completer -> {
                completer.setSoftCopyRegId(regId);
                completer.setId(null);
            });
            this.saveBatch(completers);
        }

        return Boolean.TRUE;
    }

}