package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.dto.perf.PerfParticipantDTO;
import com.pig4cloud.pigx.admin.entity.CompleterEntity;

import java.util.List;

public interface CompleterService extends IService<CompleterEntity> {

    void replaceCompleters(String code, List<CompleterEntity> completers);

    List<PerfParticipantDTO> toParticipants(List<CompleterEntity> list);
}