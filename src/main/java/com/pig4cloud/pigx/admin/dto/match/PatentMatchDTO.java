package com.pig4cloud.pigx.admin.dto.match;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "专利供需智能匹配字段DTO")
public class PatentMatchDTO {

    @JSONField(name = "专利名称")
    @Schema(description = "名称")
    private String title;

    @JSONField(name = "摘要")
    @Schema(description = "摘要")
    private String abs;

    @JSONField(name = "国民经济分类")
    @Schema(description = "国民经济分类")
    private String nec;

    @JSONField(name = "关键词")
    @Schema(description = "关键词")
    private String patentWords;

    @JSONField(name = "名称关键词")
    @Schema(description = "名称关键词")
    private String titleKey;

    @JSONField(name = "独权关键词")
    @Schema(description = "独权关键词")
    private String clKey;

    @JSONField(name = "背景关键词")
    @Schema(description = "背景关键词")
    private String bgKey;

    public static void main(String[] args) {
        PatentMatchDTO patentInfoMatchDTO = new PatentMatchDTO();
        patentInfoMatchDTO.setTitle("表面具有连续片层状微纳米结构的聚己内酯丝素蛋白电纺纤维膜及其制备方法和应用");
        patentInfoMatchDTO.setAbs("本发明公开了一种表面具有连续片层状微纳米结构的聚己内酯丝素蛋白电纺纤维膜及其制备方法和应用。所述制备方法包括：步骤(1)将丝素蛋白与聚己内酯溶于六氟异丙醇中，在室温下搅拌溶解，制成混和液；步骤(2)将混合液进行静电纺丝，制备聚己内酯丝素蛋白混纺膜；步骤(3)聚己内酯丝素蛋白混纺膜真空干燥后，与PCL单体、辛酸亚锡催化剂在氮气保护下进行加热反应，制备得到聚己内酯丝素蛋白电纺纤维膜。所述制备方法得到的聚己内酯丝素蛋白电纺纤维膜具有表层片状微纳米结构纤维形貌，比表面积和纤维直径大大增加，增加电纺膜由疏水性向亲水性转变的可能性，大大提高了纤维比表面积，空间维持能力、动态力学性能和降解性能大幅度提高。");
        patentInfoMatchDTO.setNec("棉织造加工;棉印染精加工;毛织造加工;毛染整精加工;麻染整精加工;缫丝加工;绢纺和丝织加工;丝印染精加工;化纤织物染整精加工;针织或钩针编织物印染精加工;纺织带和帘子布制造;服饰制造;皮革鞣制加工;羽毛(绒)加工;化学试剂和助剂制造;专项化学用品制造;纺织专用设备制造;其他仪器仪表制造业;医疗实验室及医用消毒设备和器具制造;其他专用设备制造;服务消费机器人制造;康复辅具制造;医疗、外科及兽医用器械制造");
        patentInfoMatchDTO.setPatentWords("聚己内酯;丝素蛋白;制备;电纺纤维膜;微纳米结构;纤维;辛酸亚锡催化剂;制备方法和应用;动态力学性能;六氟异丙醇;氮气保护;加热反应;降解性能;搅拌溶解;静电纺丝;空间维持;纤维形貌;电纺膜;混和液;混合液;连续片;亲水性;疏水性");
        patentInfoMatchDTO.setTitleKey("制备方法和应用;电纺纤维膜;微纳米结构;聚己内酯;丝素蛋白;连续片");
        patentInfoMatchDTO.setClKey("聚己内酯;丝素蛋白;电纺纤维膜;制备;微纳米结构;连续片;静电纺丝;辛酸亚锡;漂洗;煮沸;碳酸氢钠水溶液;辛酸亚锡催化剂;组织工程支架;六氟异丙醇;氮气保护;电池工业;纺丝电压;加热反应;搅拌溶解;去离子水;天然蚕丝;混和液;混合液;聚合度;摩尔比;再生膜;质量比;纺丝;取出;应用;诱导;重复");
        patentInfoMatchDTO.setBgKey("聚己内酯;丝素蛋白;电纺纤维膜;微纳米结构;连续片;制备;电子扫描显微镜;静电纺丝;纤维形貌;辛酸亚锡;片状结构;电纺膜;断裂力;可吸收;纤维;三维空间结构;改性;机械性能;纤维结构;混合液;扫描图;辛酸亚锡催化剂;制备方法和应用;观察表面;交联改性;产率;蚕丝;规整;降解;漂洗;取出;优选;煮沸;氮气保护环境;动态力学性能;空间网状结构;力学性能检测;分子量检测;六氟异丙醇;表面规则;测试样本;二次手术;纺丝电压;降解能力;降解性能;开环聚合;空间维持;去离子水;扫描电镜;天然蚕丝");

        // Fastjson 序列化示例
        String jsonString = JSON.toJSONString(patentInfoMatchDTO, true);
        System.out.println(jsonString);
    }
}
