package com.pig4cloud.pigx.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceivePageRequest;
import com.pig4cloud.pigx.admin.dto.demandReceive.DemandReceiveResponse;
import com.pig4cloud.pigx.admin.entity.DemandReceiveEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DemandReceiveMapper extends BaseMapper<DemandReceiveEntity> {

    @Results(id = "DemandReceiveResponseMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "demand_id", property = "demandId"),
            @Result(column = "name", property = "name"),
            @Result(column = "type", property = "type"),
            @Result(column = "field", property = "field"),
            @Result(column = "valid_start", property = "validStart"),
            @Result(column = "valid_end", property = "validEnd"),
            @Result(column = "budget", property = "budget"),
            @Result(column = "description", property = "description"),
            @Result(column = "create_user_name", property = "createUserName"),
            @Result(column = "dept_name", property = "deptName"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "read_flag", property = "readFlag")
    })
    @Select({
            "<script>",
            "SELECT",
            "  dr.id,",
            "  dr.demand_id,",
            "  dr.read_flag,",
            "  d.name,",
            "  d.type,",
            "  d.field,",
            "  d.valid_start,",
            "  d.valid_end,",
            "  d.budget,",
            "  d.description,",
            "  d.create_user_name,",
            "  sd.name AS dept_name,",
            "  d.create_time",
            "FROM t_demand_receive dr",
            "JOIN t_demand d ON d.id = dr.demand_id",
            "LEFT JOIN sys_dept sd ON sd.dept_id = d.dept_id AND sd.del_flag = '0'",
            "WHERE dr.receive_user_id = #{userId}",
            "  AND d.del_flag = '0'",
            "<if test='req.readFlag != null'>",
            "  AND dr.read_flag = #{req.readFlag}",
            "</if>",
            "<if test='req.ids != null and req.ids.size() > 0'>",
            "  AND dr.id IN",
            "  <foreach collection='req.ids' item='id' open='(' separator=',' close=')'>",
            "    #{id}",
            "  </foreach>",
            "</if>",
            "<if test='req.name != null and req.name != \"\"'>",
            "  AND d.name LIKE CONCAT('%', #{req.name}, '%')",
            "</if>",
            "<if test='req.type != null and req.type != \"\"'>",
            "  AND d.type = #{req.type}",
            "</if>",
            "<if test='req.field != null and req.field != \"\"'>",
            "  AND d.field = #{req.field}",
            "</if>",
            "<if test='req.createUserId != null'>",
            "  AND d.create_user_id = #{req.createUserId}",
            "</if>",
            "<if test='req.startTime != null'>",
            "  AND d.create_time &gt;= #{req.startTime}",
            "</if>",
            "<if test='req.endTime != null'>",
            "  AND d.create_time &lt;= #{req.endTime}",
            "</if>",
            "ORDER BY d.create_time DESC",
            "</script>"
    })
    IPage<DemandReceiveResponse> selectReceivePage(Page<?> page,
                                                   @Param("req") DemandReceivePageRequest req,
                                                   @Param("userId") Long userId);
}
