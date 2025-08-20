package com.pig4cloud.pigx.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pig4cloud.pigx.admin.dto.patent.PatentSearchResponse;
import com.pig4cloud.pigx.admin.dto.patent.PatentTypeSummaryVO;
import com.pig4cloud.pigx.admin.entity.PatentInfoEntity;
import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PatentInfoMapper extends PigxBaseMapper<PatentInfoEntity> {

    @Select("""
                <script>
                SELECT
                    t1.*, t2.cooperation_mode, t2.cooperation_amount, t3.draws as cover
                FROM
                    t_patent_info t1
                INNER JOIN t_patent_shelf t2 ON t1.pid = t2.pid 
                LEFT JOIN t_patent_detail_cache t3 ON t1.pid = t3.pid
                WHERE ${whereSql}
                ${orderBy}
                LIMIT #{offset}, #{pageSize}
                </script>
            """)
    List<PatentSearchResponse> searchPatent(
            @Param("whereSql") String whereSql,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("orderBy") String orderBy
    );


    @Select("""
                <script>
                SELECT
                    count(0)
                FROM
                    t_patent_info t1
                INNER JOIN t_patent_shelf t2 ON t1.pid = t2.pid 
                LEFT JOIN t_patent_detail_cache t3 ON t1.pid = t3.pid
                WHERE ${whereSql}
                </script>
            """)
    int countSearch(@Param("whereSql") String whereSql);


    @Select("""
            SELECT pat_type, count(0) AS totalAmount
            FROM t_patent_info
            ${ew.customSqlSegment}
            GROUP BY pat_type
            """)
    List<PatentTypeSummaryVO> selectGroupSum(@Param(Constants.WRAPPER) Wrapper<PatentInfoEntity> wrapper);

}