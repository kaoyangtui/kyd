package com.pig4cloud.pigx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.entity.PatentLogEntity;

/**
 * @author zhaoliang
 */
public interface CniprService {

    Page<PatentLogEntity> page(int from,
                                int size);

    Page<PatentLogEntity> page(String exp,
                               String dbs,
                               int option,
                               String order,
                               int from,
                               int size,
                               String displayCols,
                               boolean highLight,
                               boolean isDbAgg);
}