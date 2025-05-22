package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.entity.ResultCompleterEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhaoliang
 */
public interface ResultCompleterService extends IService<ResultCompleterEntity> {

    void replaceCompleters(Long resultId, List<ResultCompleterEntity> entities);
}