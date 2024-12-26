package com.pig4cloud.pigx.mapper;

import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import com.pig4cloud.pigx.entity.Draft;
import org.apache.ibatis.annotations.Mapper;

/**
 * 草稿管理表，用于管理用户草稿数据
 *
 * @author zl
 * @date 2024-12-26 14:33:33
 */
@Mapper
public interface DraftMapper extends PigxBaseMapper<Draft> {


}