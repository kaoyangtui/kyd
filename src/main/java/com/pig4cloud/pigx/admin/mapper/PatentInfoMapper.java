package com.pig4cloud.pigx.admin.mapper;

import com.pig4cloud.pigx.admin.dto.patent.PatentSearchResponse;
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
                    t1.*, t2.cooperation_mode, t2.cooperation_amount
                FROM
                    t_patent_info t1
                INNER JOIN t_patent_shelf t2 ON t1.pid = t2.pid 
                WHERE t1.del_flag = 0
                  AND t2.shelf_status = 1
                  <if test='keyword != null and keyword != ""'>
                    AND MATCH(app_number, pub_number, inventor_name, patent_words, title_key, cl_key, bg_key)
                    AGAINST(#{keyword} IN NATURAL LANGUAGE MODE)
                  </if>
                  ${ipcWhere}
                  ${cooperationModeWhere}
                  ${orderBy}
                LIMIT #{offset}, #{pageSize}
                </script>
            """)
    List<PatentSearchResponse> searchPatent(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("orderBy") String orderBy,
            @Param("ipcWhere") String ipcWhere,
            @Param("cooperationModeWhere") String cooperationModeWhere
    );

    @Select("""
                <script>
                SELECT
                    count(0)
                FROM
                    t_patent_info t1
                INNER JOIN t_patent_shelf t2 ON t1.pid = t2.pid 
                WHERE t1.del_flag=0 
                  and t2.shelf_status = 1 
                  <if test='keyword != null and keyword != ""'>
                    AND MATCH(app_number, pub_number, inventor_name, patent_words, title_key, cl_key, bg_key)
                    AGAINST(#{keyword} IN NATURAL LANGUAGE MODE)
                  </if>
                  ${ipcWhere}
                  ${cooperationModeWhere}
                </script>
            """)
    int countSearch(@Param("keyword") String keyword,
                    @Param("ipcWhere") String ipcWhere,
                    @Param("cooperationModeWhere") String cooperationModeWhere);

}