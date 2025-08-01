package com.pig4cloud.pigx.admin.dto.match;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "需求供需智能匹配字段DTO")
public class DemandMatchDTO {

    @JSONField(name = "需求名称")
    @Schema(description = "需求名称")
    private String name;

    @JSONField(name = "所属领域")
    @Schema(description = "所属领域")
    private String field;

    @JSONField(name = "需求摘要")
    @Schema(description = "需求摘要")
    private String description;

    @JSONField(name = "需求标签")
    @Schema(description = "需求标签；多个用分号分隔")
    private String tags;

    // 示例 main 方法
    public static void main(String[] args) {
        DemandMatchDTO dto = new DemandMatchDTO();
        dto.setName("基于纺织品定制化生产智能排产系统研发应用");
        dto.setField("其他产业用纺织制成品制造;纺织专用设备制造");
        dto.setDescription("1. 提升生产效率和灵活性\n" +
                "APS排产系统能够根据订单需求和生产能力灵活调整生产顺序，优化车间资源配置，减少停机时间，提升生产效率。对于粘扣工厂而言，APS系统可以实现按需生产，提高生产线利用率，减少资源浪费，从而显著提升整体生产效率。此外，APS系统还能够快速响应市场变化，缩短交货周期，提高客户满意度。\n" +
                "2. 降低生产成本和库存积压\n" +
                "通过合理安排生产计划，APS系统可以减少物料浪费和库存积压，降低生产成本。例如，APS系统能够实现物料需求预测到生产计划执行的全流程自动化，优化库存管理，减少库存成本。这对于粘扣工厂来说，能够有效降低生产成本，提高市场竞争力。\n" +
                "3. 提高产品质量和客户满意度\n" +
                "APS系统通过实时监控生产过程，及时解决瓶颈问题，确保产品质量。例如，在制鞋行业，APS系统显著提升了生产效率和经济效益，解决了长期困扰的排产问题。对于粘扣工厂而言，APS系统同样能够提高产品质量，减少生产事故质量，提升客户满意度。\n" +
                "4. 推动产业链供应链自主创新能力\n" +
                "APS系统作为供应链管理的核心工具，能够实现企业内外部全数据链的整合，提供全局链思维管理。通过APS系统的应用，粘扣工厂可以实现供应链上下游的协同优化，提升供应链的整体效率和响应能力。这不仅有助于增强产业链供应链的自主创新能力，还能推动我市产业转型升级。\n" +
                "5. 促进数字化转型和智能制造\n" +
                "APS系统的应用是智能制造转型的重要组成部分。通过APS系统，粘扣工厂可以实现生产计划的精细化管理，提高生产效率和产品质量。例如，APS系统与ERP系统的深度融合，可以实现生产计划与实际生产的无缝衔接，提高生产透明度和管理效率。这对于推动我市制造业的数字化转型具有重要意义。\n" +
                "6. 应对市场需求变化和订单波动\n" +
                "粘扣工厂面临的需求订单不稳定、工艺变化和设备故障等复杂约束条件。APS系统能够快速适应这些变化，优化生产计划，确保生产顺利进行。例如，在离散型生产中，APS系统能够有效应对订单需求的波动，避免产能过剩或不足。\n" +
                "7. 提高企业竞争力和市场响应速度\n" +
                "APS系统的应用能够提高企业的市场响应速度和竞争力。通过优化生产计划和资源分配，APS系统能够帮助企业快速适应市场需求变化，提高市场竞争力。例如，在汽车零部件行业，APS系统显著提高了生产效率和库存管理能力。\n" +
                "8.打造企业级数字化一体平台\n" +
                "解决系统建设及管理分散，缺乏集成整合，数据无法打通，造成信息孤岛等纺织企业经营痛点，整合ERP、MES、APS等系统，通过物联网、及大数据技术，打通生产、仓储、供应链全链路。聚焦纺织行业特性，支持智能排产、质量追溯、能耗优化及设备实时监控，实现订单敏捷响应与工艺动态调整");
        dto.setTags("纺织");

        // Fastjson 序列化示例
        String jsonString = com.alibaba.fastjson.JSON.toJSONString(dto, true);
        System.out.println(jsonString);

        // Fastjson 反序列化示例
        DemandMatchDTO parsed = com.alibaba.fastjson.JSON.parseObject(jsonString, DemandMatchDTO.class);
        System.out.println(parsed.getName());
    }
}
