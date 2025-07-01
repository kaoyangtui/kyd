package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;

/**
 * @author zhaoliang
 */
public interface CniprService {


    void fetchAllPatents(String exp,
                         String dbs,
                         int option,
                         String order,
                         String displayCols,
                         boolean highLight,
                         boolean isDbAgg);

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