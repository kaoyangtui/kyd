package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.SoftCopyRegEntity;
import com.pig4cloud.pigx.admin.vo.SoftCopyReg.SoftCopyRegCreateRequest;
import com.pig4cloud.pigx.admin.vo.SoftCopyReg.SoftCopyRegPageRequest;
import com.pig4cloud.pigx.admin.vo.SoftCopyReg.SoftCopyRegResponse;
import com.pig4cloud.pigx.admin.vo.SoftCopyReg.SoftCopyRegUpdateRequest;

import java.util.List;

public interface SoftCopyRegService extends IService<SoftCopyRegEntity> {

    IPage<SoftCopyRegResponse> pageResult(SoftCopyRegPageRequest request);

    SoftCopyRegResponse getDetail(Long id);

    Boolean createReg(SoftCopyRegCreateRequest request);

    Boolean updateReg(SoftCopyRegUpdateRequest request);

    Boolean removeRegs(List<Long> ids);

}
