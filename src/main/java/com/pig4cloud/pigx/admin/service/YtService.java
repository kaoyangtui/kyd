package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.entity.PatentLogEntity;

/**
 * @author zhaoliang
 */
public interface YtService {


    /**
     * sf1-v1，专利概览检索接口
     */
    Page<PatentLogEntity> page(String exp,
                               String dbs,
                               int option,
                               String order,
                               int from,
                               int size,
                               String displayCols,
                               boolean highLight,
                               boolean isDbAgg);

    /**
     * pi16，获取专利插图路径接口,返回资源路径
     */
    String imgUrl(String pid, String resourceName);

    /**
     * pi12，获取专利摘要附图接口，返回资源路径
     */
    String absUrl(String pid);

    /**
     * pi11，获取专利全文图片PDF接口，返回资源路径
     */
    String pdfUrl(String pid);
}