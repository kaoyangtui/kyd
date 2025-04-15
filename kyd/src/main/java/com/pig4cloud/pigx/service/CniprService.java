package com.pig4cloud.pigx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.entity.PatentInfoEntity;

/**
 * @author zhaoliang
 */
public interface CniprService {

    Page<PatentInfoEntity> page(int from,
                                int size);

    Page<PatentInfoEntity> page(String exp,
                                String dbs,
                                int option,
                                String order,
                                int from,
                                int size,
                                String displayCols,
                                boolean highLight,
                                boolean isDbAgg);
}