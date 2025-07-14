package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.patent.PatentClaimCreateRequest;
import com.pig4cloud.pigx.admin.dto.patent.PatentInventorVO;
import com.pig4cloud.pigx.admin.dto.patent.PatentUnClaimRequest;
import com.pig4cloud.pigx.admin.entity.PatentInventorEntity;

import java.util.List;

public interface PatentInventorService extends IService<PatentInventorEntity> {

    void create(String pid, List<String> inventorNames);


    List<PatentInventorVO> listByPid(String pid);


}