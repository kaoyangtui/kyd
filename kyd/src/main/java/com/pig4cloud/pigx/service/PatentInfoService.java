package com.pig4cloud.pigx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.entity.PatentInfoEntity;
import com.pig4cloud.pigx.model.request.PatentSearchListReq;
import com.pig4cloud.pigx.model.response.PatentSearchListRes;

/**
 * @author zhaoliang
 */
public interface PatentInfoService extends IService<PatentInfoEntity> {

    IPage<PatentSearchListRes> searchList(PatentSearchListReq req);
}