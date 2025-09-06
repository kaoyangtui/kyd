package com.pig4cloud.pigx.admin.jsonflow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.heaven.annotation.reflect.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JfRunFlowMapper {

    @Select(value = {
            "<script>",
            "select",
            "  t1.code,",
            "  t1.flow_key,",
            "  t3.flow_name,",
            "  t1.order_name,",
            "  t1.create_user_name,",
            "  t1.dept_name,",
            "  t1.create_time,",
            "  t1.status,",
            "  case when t2.node_name is null and t1.status = 1 then '结束' else t2.node_name end as node_name",
            "from jf_run_flow t1",
            "left join jf_run_node t2 on t1.id = t2.flow_inst_id and t2.status = 0",
            "left join jf_def_flow t3 on t1.def_flow_id = t3.id and t3.del_flag = 0",
            "<where>",
            "  <if test='req.code != null and req.code != \"\"'>",
            "    and t1.code like concat('%', #{req.code}, '%')",
            "  </if>",
            "  <if test='req.flowKey != null and req.flowKey != \"\"'>",
            "    and t1.flow_key like concat('%', #{req.flowKey}, '%')",
            "  </if>",
            "  <if test='req.orderName != null and req.orderName != \"\"'>",
            "    and t1.order_name like concat('%', #{req.orderName}, '%')",
            "  </if>",
            "  <if test='req.createUserName != null and req.createUserName != \"\"'>",
            "    and t1.create_user_name like concat('%', #{req.createUserName}, '%')",
            "  </if>",
            "  <if test='req.createUser != null'>",
            "    and t1.create_user = #{req.createUser}",
            "  </if>",
            "  <if test='req.deptName != null and req.deptName != \"\"'>",
            "    and t1.dept_name like concat('%', #{req.deptName}, '%')",
            "  </if>",
            "  <if test='req.status != null'>",
            "    and t1.status = #{req.status}",
            "  </if>",
            "  <if test='req.startTime != null'>",
            "    and t1.create_time &gt;= #{req.startTime}",
            "  </if>",
            "  <if test='req.endTime != null'>",
            "    and t1.create_time &lt;= #{req.endTime}",
            "  </if>",

            // 🔽 新增：支持 BasePageQuery 里的条件
            "  <if test='req.ids != null and req.ids.size > 0'>",
            "    and t1.id in",
            "    <foreach collection='req.ids' item='id' open='(' separator=',' close=')'>",
            "      #{id}",
            "    </foreach>",
            "  </if>",
            "  <if test='req.startNo != null and req.endNo != null'>",
            "    and t1.id between #{req.startNo} and #{req.endNo}",
            "  </if>",

            "</where>",
            "order by t1.create_time desc",
            "</script>"
    })
    Page<MyFlowResponse> page(Page<MyFlowResponse> page, @Param("req") MyFlowRequest req);
}
