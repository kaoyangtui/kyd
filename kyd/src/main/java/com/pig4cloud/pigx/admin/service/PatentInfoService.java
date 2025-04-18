package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.admin.model.request.PatentSearchListReq;
import com.pig4cloud.pigx.admin.model.response.PatentSearchListRes;

/**
 * @author zhaoliang
 */
public interface PatentInfoService extends IService<PatentInfoEntity> {

    IPage<PatentSearchListRes> searchList(PatentSearchListReq req);
}