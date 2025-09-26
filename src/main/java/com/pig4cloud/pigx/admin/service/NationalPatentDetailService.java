package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.NationalPatentDetailEntity;

import java.util.List;

public interface NationalPatentDetailService extends IService<NationalPatentDetailEntity> {

    void upsertBatchFromMessages(List<String> messages, int batchSize);

}