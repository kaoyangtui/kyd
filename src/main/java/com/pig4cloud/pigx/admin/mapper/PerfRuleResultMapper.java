package com.pig4cloud.pigx.admin.mapper;

import com.github.houbb.heaven.annotation.reflect.Param;
import com.pig4cloud.pigx.admin.dto.perf.PerfSchemeCellResponse;
import com.pig4cloud.pigx.admin.entity.PerfRuleResultEntity;
import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PerfRuleResultMapper extends PigxBaseMapper<PerfRuleResultEntity> {

    // 每个「IP类型 × 事件」的 分数/件数（按方案）
    @Select("""
                SELECT 
                  ip_type_code   AS ipTypeCode,
                  MAX(ip_type_name)   AS ipTypeName,
                  rule_event_code AS ruleEventCode,
                  MAX(rule_event_name) AS ruleEventName,
                  ROUND(SUM(score_sum), 4) AS scoreSum,
                  SUM(event_count)         AS eventCount
                FROM t_perf_rule_result
                WHERE del_flag='0' AND scheme_id=#{schemeId}
                GROUP BY scheme_id, ip_type_code, rule_event_code
                ORDER BY ip_type_code, rule_event_code
            """)
    List<PerfSchemeCellResponse> selectCellsByScheme(@Param("schemeId") Long schemeId);

    // 总分
    @Select("""
                SELECT COALESCE(ROUND(SUM(score_sum), 4), 0)
                FROM t_perf_rule_result
                WHERE del_flag='0' AND scheme_id=#{schemeId}
            """)
    BigDecimal sumTotalScore(@Param("schemeId") Long schemeId);
}