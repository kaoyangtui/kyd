USE pigxx_boot;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for jf_comment
-- ----------------------------
DROP TABLE IF EXISTS `jf_comment`;
CREATE TABLE `jf_comment` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `run_node_id` bigint(20) DEFAULT NULL COMMENT '运行节点ID',
  `remark` varchar(512) DEFAULT NULL COMMENT '审批意见',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `run_job_id` bigint(20) DEFAULT NULL COMMENT '运行任务ID',
  `node_job_id` bigint(20) DEFAULT NULL COMMENT '任务定义ID',
  `flow_node_id` bigint(20) NOT NULL COMMENT '节点定义ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='节点批注管理';

-- ----------------------------
-- Records of jf_comment
-- ----------------------------
BEGIN;
INSERT INTO `jf_comment` VALUES (1631234697665445890, 1631234697145352194, 'TestZeroCode', 1, 1631234697510256642, '【发起】 意见: 无', 1, '2023-03-02 18:07:14', 1631234697665445890, 1630965408190459906, 1677686588872000008, 1);
INSERT INTO `jf_comment` VALUES (1631234697678028801, 1631234697145352194, 'TestZeroCode', 1, 1631234697522839553, '【同意】 意见: 无', 1, '2023-03-02 18:11:55', 1631234697678028801, 1630965408257568770, 1677686594395000009, 1);
INSERT INTO `jf_comment` VALUES (1631234697699000322, 1631234697145352194, 'TestZeroCode', 1, 1631234697552199681, '【同意】 意见: 无', 1, '2023-07-03 16:27:22', 1631234697699000322, 1677686667002000014, 1677686614774000011, 1);
INSERT INTO `jf_comment` VALUES (1631234945217462274, 1631234944961609729, 'TestKey', 1, 1631234945141964801, '【发起】 意见: 无', 1, '2023-03-02 18:08:12', 1631234945217462274, 1630953145756839937, 1677683136126000020, 1);
INSERT INTO `jf_comment` VALUES (1631235003824472065, 1631235003606368258, 'FormPermission', 1, 1631235003757363202, '【发起】 意见: 无', 1, '2023-03-02 18:08:26', 1631235003824472065, 1630971561175707649, 1677688514110000002, 1);
INSERT INTO `jf_comment` VALUES (1631235155675054081, 1631235155431784450, 'AskLeave', 1, 1631235155578585090, '【发起】 意见: 无', 1, '2023-03-02 18:09:03', 1631235155675054081, 1631163656494673921, 1677733473617000054, 1);
INSERT INTO `jf_comment` VALUES (1683828100609384449, 1683828100328366082, 'DynamicSignature', 1, 1683828100538081282, '【发起】 意见: 无', 1, '2023-07-25 21:14:38', 1683828100609384449, 1683825416573591553, 1690289583078000001, 1);
INSERT INTO `jf_comment` VALUES (1691395083500548098, 1691395082670075906, 'TestDingDing', 1, 1691395083261472769, '【发起】 意见: 无', 1, '2023-08-15 18:23:07', 1691395083500548098, 1691389179627593730, 1692022816944000026, 1);
INSERT INTO `jf_comment` VALUES (1691435166312198146, 1691435166140231682, 'TestDingGate', 1, 1691435166253477889, '【发起】 意见: 无', 1, '2023-08-15 21:02:24', 1691435166312198146, 1691434275613995010, 1692103817782000001, 1);
INSERT INTO `jf_comment` VALUES (1700039727675420674, 1700039727486676993, 'HandoverFlow', 1, 1700039727616700417, '【发起】 意见: 无', 1, '2023-09-08 14:53:51', 1700039727675420674, 1631157370713567233, 1677732851137000033, 1);
INSERT INTO `jf_comment` VALUES (1728770563639672834, 1728770563404791810, 'CustomVuePage', 1, 1728770563576758274, '【发起】 意见: 无', 1, '2023-11-26 21:39:56', 1728770563639672834, 1728769069985103874, 1701005092143000003, 1);
INSERT INTO `jf_comment` VALUES (1729036577472299009, 1729036577254195202, 'JDShopParSonFlow', 1, 1729036577426161665, '【发起】 意见: 无', 1, '2023-11-27 15:16:59', 1729036577472299009, 1728785543856455681, 1701008233487000040, 1);
INSERT INTO `jf_comment` VALUES (1729036577480687617, 1729036577254195202, 'JDShopParSonFlow', 1, 1729036577438744578, '【同意】 意见: 无', 1, '2023-11-27 15:17:15', 1729036577480687617, 1728785543965507585, 1701008237478000043, 1);
INSERT INTO `jf_comment` VALUES (1729036577489076225, 1729036577254195202, 'JDShopParSonFlow', 1, 1729036577451327490, '【同意】 意见: 无', 1, '2023-11-27 15:17:41', 1729036577489076225, 1728797600450400257, 1701012098212000019, 1);
INSERT INTO `jf_comment` VALUES (1729036765834297345, 1729036763657453570, 'JDShopSonFlow', 1, 1729036765796548610, '【发起】 意见: 无', 1, '2023-11-27 15:17:44', 1729036765834297345, 1728790528639959041, 1701010386424000058, 1);
INSERT INTO `jf_comment` VALUES (1791286392830316545, 1791030389211779073, 'SubFormTest', 1, 1791286380461314049, '【发起】 意见: 无', 1, '2024-05-17 09:55:31', 1791286380561977345, 1739522295011074049, 1703568882593000001, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_def_flow
-- ----------------------------
DROP TABLE IF EXISTS `jf_def_flow`;
CREATE TABLE `jf_def_flow` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` varchar(2) NOT NULL DEFAULT '-1' COMMENT '状态 -1 暂存 0 作废 1 发布',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `update_user` bigint(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `flow_name` varchar(255) NOT NULL COMMENT '流程名称',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
  `group_name` varchar(255) NOT NULL COMMENT '分组名称',
  `form_id` bigint(20) DEFAULT NULL COMMENT '表单ID',
  `query_order` varchar(255) DEFAULT NULL COMMENT '更新工单信息接口',
  `update_order` varchar(255) DEFAULT NULL COMMENT '查询工单信息接口',
  `query_method` varchar(255) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `update_method` varchar(255) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `from_type` char(1) DEFAULT '1' COMMENT '流程设计来源 0 一键快捷设计 1 定制化开发设计',
  `auto_layout` varchar(10) DEFAULT NULL COMMENT '自动布局方向',
  `allow_job_link` char(1) DEFAULT '0' COMMENT '是否允许任务连线到其他节点 0否 1是',
  `is_job_separated` char(1) DEFAULT '0' COMMENT '是否允许节点与任务分离显示 0否 1是',
  `is_simple_mode` char(1) DEFAULT '1' COMMENT '设计模式：1 简单模式 0 专业模式',
  `connector` varchar(64) DEFAULT 'rounded' COMMENT '连线样式',
  `router` varchar(64) DEFAULT 'normal' COMMENT '连线路由',
  `is_independent` char(1) DEFAULT '0' COMMENT '配置数据独立 0否 1是',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程定义管理';

-- ----------------------------
-- Records of jf_def_flow
-- ----------------------------
BEGIN;
INSERT INTO `jf_def_flow` VALUES (1660711059233000001, 'AskLeave', 1, '2022-09-25 14:11:57', '1', '流程条件days采用SpEL表达式，如0<#days && #days<=3。条件值来源工单days的值', '0', 1, '2024-03-17 14:35:15', '请假工单', 1, 1, '测试单独开发特殊业务逻辑', NULL, NULL, NULL, NULL, NULL, '1', NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1661171611773000001, 'HandoverFlow', 1, '2022-09-21 21:18:22', '1', '接收人审批节点receive_user取值于工单逻辑分配的参与者，流程条件isNeedReceive取值于工单逻辑设置的流程条件中的值', '0', 1, '2024-03-17 14:29:43', '工作交接', 1, 1, '工作交接', NULL, NULL, NULL, NULL, NULL, '1', NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1663698819878000001, 'TestZeroCode', 1, '2022-09-23 02:33:47', '1', '流程条件isGoEnd取值来自工单#order.isGoEnd的值', '0', 1, '2024-03-17 16:22:28', '一键发布无需编码', 1, 1, '一键发布无需编码', 1572932685786275842, NULL, NULL, NULL, NULL, '0', 'TB', '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1663850423578000013, 'TestKey', 1, '2022-09-24 20:40:47', '1', '节点任务create_user来源当前用户#user.userId，流程条件isGoEnd取值来自工单#order.isGoEnd的值', '0', 1, '2024-03-17 16:12:30', '测试自动建表存储', 1, 1, '测试自动建表存储', 1576909046599172097, NULL, NULL, NULL, NULL, '0', NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1668790077997000002, 'FormPermission', 1, '2022-11-19 00:56:48', '1', '表单/审批页面字段权限，设置审批用户可以看到或操作哪些页面字段', '0', 1, '2024-03-16 23:25:58', '表单/审批页面字段权限', 1, 1, '表单/审批页面字段权限', 1593649608467542018, NULL, NULL, NULL, NULL, '0', NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1690289416119000001, 'DynamicSignature', 1, '2023-07-25 21:03:58', '1', '动态加减签常用于动态计算下一节点不确定任务数和办理人员', '0', 1, '2024-03-17 16:25:26', '动态加减签-复杂场景', 1, 1, '动态加减签-复杂场景', 1683825630990606338, NULL, NULL, NULL, NULL, '0', 'TB', '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1692022814348000008, 'TestDingDing', 1, '2023-08-15 17:59:39', '1', '钉钉UI模式支持普通用户操作，一套代码同时支持简单模式和专业模式', '0', 1, '2024-05-17 14:56:15', '测试钉钉UI模式', 1, 1, '测试钉钉UI模式', 1691096635442229249, NULL, NULL, NULL, NULL, '0', 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1692103815657000001, 'TestDingGate', 1, '2023-08-15 20:58:48', '1', '测试钉钉网关模式，支持串行网关、并行网关', '0', 1, '2023-08-15 23:31:54', '测试钉钉网关模式', 1, 1, '测试钉钉网关模式', 1691434590803357698, NULL, NULL, NULL, NULL, '0', 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1701005091303000002, 'CustomVuePage', 1, '2023-11-26 21:34:00', '1', '与表单设计器完全解耦，支持主表单可自定义Vue页面', '0', 1, '2023-11-26 21:47:29', '自定义主表单Vue页面', 1, 1, '自定义主表单Vue页面', 1701005090330000001, NULL, NULL, NULL, NULL, '0', 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1701008232801000039, 'JDShopParSonFlow', 1, '2023-11-26 22:39:28', '1', '用于测试父子流程，由父流程某个节点触发子流程的发起，当子流程完结后会反向通知父流程', '0', 1, '2024-05-17 14:54:20', '京东购物父子流程', 1, 1, '京东购物父子流程', 1701008232320000038, '/order-base/order/flow-inst-id', '/order-base/update-order-info', 'GET', 'PUT', '0', NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1701010376317000057, 'JDShopSonFlow', 1, '2023-11-26 22:59:16', '1', '京东购物子流程，当子流程完结后会反向通知父流程', '0', 1, '2024-05-17 14:55:01', '京东购物子流程', 1, 1, 'JDShopSonFlow', 1701010375704000056, NULL, NULL, NULL, NULL, '0', NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_def_flow` VALUES (1703563526494000002, 'SubFormTest', 1, '2023-12-26 13:43:29', '1', '父子表查询新增删除，常用于子表数据单独查询、新增、删除等自定义操作', '0', 1, '2024-05-16 16:56:41', '父子表查询新增删除', 1, 1, '父子表查询新增删除', 1703563525573000001, NULL, NULL, 'GET', 'PUT', '0', NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_dist_person
-- ----------------------------
DROP TABLE IF EXISTS `jf_dist_person`;
CREATE TABLE `jf_dist_person` (
  `id` bigint(20) NOT NULL,
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `code` varchar(64) NOT NULL COMMENT '工单CODE，便于业务人员查看',
  `role_id` bigint(20) NOT NULL COMMENT '参与者角色ID',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_key` varchar(64) NOT NULL COMMENT '动态办理人KEY',
  `job_type` varchar(2) NOT NULL COMMENT '任务类型 -1无 0用户 1角色 2岗位 3部门',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序值',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分配参与者';

-- ----------------------------
-- Records of jf_dist_person
-- ----------------------------
BEGIN;
INSERT INTO `jf_dist_person` VALUES (1631234945066467330, 1631234944961609729, 'BGSQ-GD-20230302-00294', 1, 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'create_user', '0', NULL, NULL, 1);
INSERT INTO `jf_dist_person` VALUES (1683828100441612289, 1683828100328366082, 'BGSQ-GD-20230725-00606', 1, 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'create_user', '0', NULL, NULL, 1);
INSERT INTO `jf_dist_person` VALUES (1683828942376837121, 1683828100328366082, 'BGSQ-GD-20230725-00606', 1, 'DynamicSignature', 1, '2023-11-28 12:07:24', NULL, NULL, 'dynamic_signature', '0', NULL, NULL, 1);
INSERT INTO `jf_dist_person` VALUES (1683828942389420034, 1683828100328366082, 'BGSQ-GD-20230725-00606', 2, 'DynamicSignature', 1, '2023-11-28 12:07:24', NULL, NULL, 'dynamic_signature', '1', NULL, NULL, 1);
INSERT INTO `jf_dist_person` VALUES (1700039727557980162, 1700039727486676993, 'GZJJ-GD-20230908-00673', 1, 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'receive_user', '0', NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_flow_clazz
-- ----------------------------
DROP TABLE IF EXISTS `jf_flow_clazz`;
CREATE TABLE `jf_flow_clazz` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `flow_node_id` bigint(20) DEFAULT NULL COMMENT '节点ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `clazz` varchar(255) DEFAULT NULL COMMENT '监听类',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `methods` varchar(255) DEFAULT '0' COMMENT '方法名称，多个逗号分隔，且有顺序',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `val_type` char(1) DEFAULT NULL COMMENT '条件模式：0简单模式 1SpEL模式 2专业模式 3Http模式',
  `operator` varchar(2) DEFAULT NULL COMMENT '运算符：0等于 1不等于 2大于 3大于等于 4小于 5小于等于 6包含 7不包含',
  `var_key_val` varchar(255) DEFAULT NULL COMMENT '取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )',
  `var_val` varchar(255) DEFAULT NULL COMMENT '校验值',
  `type` varchar(1) NOT NULL DEFAULT '0' COMMENT '事件类型 0 节点事件 1 全局事件',
  `http_url` varchar(255) DEFAULT NULL COMMENT 'http请求地址',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `flow_inst_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='节点监听事件设置';

-- ----------------------------
-- Records of jf_flow_clazz
-- ----------------------------
BEGIN;
INSERT INTO `jf_flow_clazz` VALUES (1769249737294483458, NULL, 'HandoverFlow', 1, '2024-03-17 14:29:43', 1661171611773000001, 'handoverFlowEndListener', 1, 'finish', '0', NULL, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_flow_node
-- ----------------------------
DROP TABLE IF EXISTS `jf_flow_node`;
CREATE TABLE `jf_flow_node` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `pc_todo_url` varchar(512) DEFAULT NULL COMMENT 'PC待办页面路径（多个逗号隔开）',
  `pc_finish_url` varchar(512) DEFAULT NULL COMMENT 'PC完成页面路径（多个逗号隔开）',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `mp_todo_url` varchar(512) DEFAULT NULL COMMENT 'MP待办页面路径（多个逗号隔开）',
  `mp_finish_url` varchar(512) DEFAULT NULL COMMENT 'MP完成页面路径（多个逗号隔开）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `node_name` varchar(255) NOT NULL COMMENT '节点名称',
  `node_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '节点类型 -1 发起 0 串行、1 并行、2 结束 3 虚拟',
  `timeout` int(11) DEFAULT NULL COMMENT '超时时间 0不限制',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `reject_type` varchar(1) DEFAULT '0' COMMENT '驳回类型 0 依次返回  1 直接返回',
  `is_continue` char(1) NOT NULL DEFAULT '0' COMMENT '不满足自身条件是否继续流转下一节点 0 否  1 是',
  `is_auto_next` char(1) NOT NULL DEFAULT '1' COMMENT '是否自动流转下一节点  0否 1是',
  `job_btns` varchar(512) DEFAULT NULL COMMENT '审批按钮权限',
  `approve_method` char(1) NOT NULL DEFAULT '1' COMMENT '多人审批方式：1会签、2或签、3依次审批',
  `is_auto_audit` char(1) DEFAULT NULL COMMENT '是否自动审批 0否 1是',
  `is_pass_same` char(1) DEFAULT NULL COMMENT '相同审批人自动通过 0：否 1：是',
  `carbon_copy` varchar(512) DEFAULT NULL COMMENT '抄送用户ID',
  `sub_def_flow_id` bigint(20) DEFAULT NULL COMMENT '节点子流程定义ID',
  `is_gateway` char(1) DEFAULT '0' COMMENT '是否为网关 0否 1是',
  `description` varchar(255) DEFAULT NULL COMMENT '节点描述',
  `position_size` varchar(255) DEFAULT NULL COMMENT '节点位置大小',
  `node_approve_method` char(1) NOT NULL DEFAULT '1' COMMENT '多节点审批方式：1会签、2或签、3依次审批',
  `node_group_id` bigint(20) DEFAULT NULL COMMENT '多节点组ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程节点设置';

-- ----------------------------
-- Records of jf_flow_node
-- ----------------------------
BEGIN;
INSERT INTO `jf_flow_node` VALUES (1677683136126000020, 1663850423578000013, 'TestKey', '1,2,4', '1,2,4', 1, '2023-03-01 23:28:26', 1, '2024-03-17 16:12:30', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 398,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677683152440000021, 1663850423578000013, 'TestKey', '2,1,4', '1,2,4', 1, '2023-03-01 23:28:26', 1, '2024-03-17 16:12:30', NULL, NULL, '0', '控制节点大小 (1)', '0', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 576,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 152\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677683283642000025, 1663850423578000013, 'TestKey', '1,2,4', '1,2,4', 1, '2023-03-01 23:28:26', 1, '2024-03-17 16:12:30', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 1119,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 80\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677683305541000026, 1663850423578000013, 'TestKey', '1,2,4', '1,2,4', 1, '2023-03-01 23:28:26', 1, '2024-03-17 16:12:30', NULL, NULL, '0', '串行 (1)', '0', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 871,\n                \"y\": 74\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 105\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677683307338000027, 1663850423578000013, 'TestKey', '1,2,4', '1,2,4', 1, '2023-03-01 23:28:26', 1, '2024-03-17 16:12:30', NULL, NULL, '0', '串行 (1)', '0', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 870,\n                \"y\": 268\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 99\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686588872000008, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686594395000009, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '干大事的 (1)', '0', 0, 2, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\":{\"x\":601,\"y\":301},\"size\":{\"height\":40,\"width\":122}}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686602089000010, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '串行 (1)', '0', 0, 5, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686614774000011, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '并行 (2)', '1', 0, 3, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686618687000012, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '并行 (1)', '1', 0, 4, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686627943000013, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '结束', '2', 0, 6, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677686693563000018, 1663698819878000001, 'TestZeroCode', '1,2,4', '1,2,4', 1, '2023-03-02 00:17:10', 1, '2024-03-17 16:22:28', NULL, NULL, '0', '虚拟', '3', 0, -1, '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677688514110000002, 1668790077997000002, 'FormPermission', '1,2,4', '1,2,4', 1, '2023-03-02 00:41:37', 1, '2024-03-16 23:25:58', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 419,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 84\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677688590244000006, 1668790077997000002, 'FormPermission', '3,2,1', '1,2,4', 1, '2023-03-02 00:41:37', 1, '2024-03-16 23:25:58', NULL, NULL, '0', '表单权限 (1)', '1', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 645,\n                \"y\": 150\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 143\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677688592089000007, 1668790077997000002, 'FormPermission', '1,2,4', '1,2,4', 1, '2023-03-02 00:41:37', 1, '2024-03-16 23:25:58', NULL, NULL, '0', '自定义权限 (1)', '1', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 893,\n                \"y\": 149\n            },\n            \"size\": {\n                \"height\": 42,\n                \"width\": 167\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677688593826000008, 1668790077997000002, 'FormPermission', '17,1,2', '1,2,4', 1, '2023-03-02 00:41:37', 1, '2024-03-16 23:25:58', NULL, NULL, '0', '审批页面权限 (1)', '1', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 790,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 155\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677688595646000009, 1668790077997000002, 'FormPermission', '1,2,4', '1,2,4', 1, '2023-03-02 00:41:37', 1, '2024-03-16 23:25:58', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 1195,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 82\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677732851137000033, 1661171611773000001, 'HandoverFlow', '1,2', '1,2', 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 338,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677732865656000034, 1661171611773000001, 'HandoverFlow', '1,2,11', '1,2', 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', NULL, NULL, '0', '分配接收人', '0', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 536,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 124\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677732899572000038, 1661171611773000001, 'HandoverFlow', '1,2', '1,2', 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', NULL, NULL, '0', '虚拟', '3', 0, -1, '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 541,\n                \"y\": 92\n            },\n            \"size\": {\n                \"height\": 42,\n                \"width\": 92\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677732907023000039, 1661171611773000001, 'HandoverFlow', '1,2', '1,2', 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 1034,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677732909827000040, 1661171611773000001, 'HandoverFlow', '9,1,2', '1,2', 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', NULL, NULL, '0', '接收人审批 (1)', '0', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 762,\n                \"y\": 92\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 143\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677733473617000054, 1660711059233000001, 'AskLeave', '1,2', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 212,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 38,\n                \"width\": 78\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677733476722000055, 1660711059233000001, 'AskLeave', '1,2', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '修改工单 (1)', '3', 0, -1, '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 381,\n                \"y\": 101\n            },\n            \"size\": {\n                \"height\": 38,\n                \"width\": 122\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677733503522000058, 1660711059233000001, 'AskLeave', '1,2,12', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '部门经理 (2)', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 384,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 37,\n                \"width\": 121\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677733629144000065, 1660711059233000001, 'AskLeave', '1,2,13', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '财务 (1)', '0', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 976,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 103\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677734357077000072, 1660711059233000001, 'AskLeave', '1,2', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '自动结束', '2', 0, 1, '0', '0', '1', '', '1', '1', NULL, '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 1176,\n                \"y\": 222\n            },\n            \"size\": {\n                \"height\": 35,\n                \"width\": 84\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677734581812000077, 1660711059233000001, 'AskLeave', '1,2,12', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '部门总监 (1)', '1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 701,\n                \"y\": 119\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 128\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1677734587498000078, 1660711059233000001, 'AskLeave', '1,2,13', '1,2', 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', NULL, NULL, '0', '人事总监 (1)', '1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 700,\n                \"y\": 313\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 128\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1690289583078000001, 1690289416119000001, 'DynamicSignature', '1,2,4', '1,2,4', 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1690289584183000002, 1690289416119000001, 'DynamicSignature', '1,2,4,7', '2,1,4,8', 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', NULL, NULL, '0', '分配下一节点不确定任务数和办理人员', '0', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 577.5,\n                \"y\": 299\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 298\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1690289585185000003, 1690289416119000001, 'DynamicSignature', '1,2,4', '1,2,4', 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', NULL, NULL, '0', '两个节点不一定挨着', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 569.5,\n                \"y\": 438\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 176\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1690289586509000004, 1690289416119000001, 'DynamicSignature', '1,2,4', '1,2,4', 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', NULL, NULL, '0', '动态加减签节点：动态计算当前节点不确定任务数和办理人员', '1', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 510,\n                \"y\": 579\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 433\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1690289589195000005, 1690289416119000001, 'DynamicSignature', '1,2,4', '1,2,4', 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692022816944000026, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692022816948000027, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692022819404000029, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '串行节点', '0', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', '你想描述点啥？请输入你的描述', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692023728415000023, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '并行节点', '1', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692023731352000025, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '并行节点', '1', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692023738815000028, 1692022814348000008, 'TestDingDing', '1,2,4', '1,2,4', 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', NULL, NULL, '0', '串行节点', '0', 0, 1, '0', '0', '1', NULL, '1', '0', NULL, NULL, NULL, '0', '用于流程中同时只有一个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103817782000001, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:48', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103817786000002, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103853707000012, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '并行节点', '1', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103856654000014, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '并行节点', '1', 0, 1, '0', '0', '1', '', '1', '0', NULL, NULL, NULL, '0', '你想描述点啥？请输入你的描述', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103865631000019, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '并行网关', '1', NULL, 1, '0', '0', '1', NULL, '1', '1', NULL, NULL, NULL, '1', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1692103869912000021, 1692103815657000001, 'TestDingGate', '1,2,4', '1,2,4', 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', NULL, NULL, '0', '并行网关', '1', NULL, 1, '0', '0', '1', NULL, '1', '1', NULL, NULL, NULL, '1', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701005092143000003, 1701005091303000002, 'CustomVuePage', '18,1,2', '19,1,2', 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:29', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', '', '1', NULL, NULL, '', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701005092149000004, 1701005091303000002, 'CustomVuePage', '18,1,2', '19,1,2', 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:29', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701005109127000006, 1701005091303000002, 'CustomVuePage', '18,1,2', '19,1,2', 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:29', NULL, NULL, '0', '请查看开始节点', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', '开始节点【PC待办页面】配置《自定义主表单Vue页面》', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701005445080000008, 1701005091303000002, 'CustomVuePage', '18,1,2', '19,1,2', 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:29', NULL, NULL, '0', '请查看表单设置', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', '第三步表单设置中输入了《组件路径》', NULL, '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701008233487000040, 1701008232801000039, 'JDShopParSonFlow', '1,2,4', '1,2,4', 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701008233489000041, 1701008232801000039, 'JDShopParSonFlow', '1,2,4', '1,2,4', 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 790\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701008237478000043, 1701008232801000039, 'JDShopParSonFlow', '3,1,2', '1,2,4', 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', NULL, NULL, '0', '添加商品并付款', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', '支付金额大于0则会发起《收发货子流程》', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701008249074000045, 1701008232801000039, 'JDShopParSonFlow', '1,2,4', '1,2,4', 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', NULL, NULL, '0', '支付校验', '0', NULL, 1, '0', '0', '1', NULL, '1', '1', NULL, '', NULL, '1', NULL, '{\"position\": {\n                \"x\": 555,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 110\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701010386424000058, 1701010376317000057, 'JDShopSonFlow', '1,2,4', '1,2,4', 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701010386427000059, 1701010376317000057, 'JDShopSonFlow', '1,2,4', '1,2,4', 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', NULL, NULL, '0', '自动结束', '2', 0, 1, '0', '0', '1', '', '1', '1', NULL, '', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 650\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701010449439000061, 1701010376317000057, 'JDShopSonFlow', '3,1,2', '1,2,4', 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', NULL, NULL, '0', '发货', '0', 0, 1, '0', '1', '1', '', '1', '0', '0', '', NULL, '0', '卖家发货，等待买家收货确认', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701010451663000063, 1701010376317000057, 'JDShopSonFlow', '3,1,2', '1,2,4', 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', NULL, NULL, '0', '收货', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', '买家收货，若未收到货则重新发货', '{\"position\": {\n                \"x\": 510,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1701012098212000019, 1701008232801000039, 'JDShopParSonFlow', '3,1,2', '1,2,4', 1, '2023-11-26 23:27:22', 1, '2024-05-17 14:54:20', NULL, NULL, '0', '收发货子流程确认', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', 1701010376317000057, '0', '当确认后会发起《京东购物子流程JDShopSonFlow》', '{\"position\": {\n                \"x\": 510,\n                \"y\": 615\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1703568882593000001, 1703563526494000002, 'SubFormTest', '1,2,4', '1,2,4', 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', NULL, NULL, '0', '开始', '-1', 0, 1, '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1703568882606000002, 1703563526494000002, 'SubFormTest', '1,2,4', '1,2,4', 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', NULL, NULL, '0', '结束', '2', 0, 1, '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 650\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1703569033526000004, 1703563526494000002, 'SubFormTest', '3,1,2', '1,2,4', 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', NULL, NULL, '0', '串行节点', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', '', NULL, '0', '用于流程中同时只有一个分支执行的场景', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_flow_node` VALUES (1703569048970000006, 1703563526494000002, 'SubFormTest', '1,2,4', '1,2,4', 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', NULL, NULL, '0', '串行节点', '0', 0, 1, '0', '0', '1', '', '1', '0', '0', NULL, NULL, '0', '用于流程中同时只有一个分支执行的场景', '{\"position\": {\n                \"x\": 510,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_flow_node_rel
-- ----------------------------
DROP TABLE IF EXISTS `jf_flow_node_rel`;
CREATE TABLE `jf_flow_node_rel` (
  `id` bigint(20) NOT NULL,
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `from_flow_node_id` bigint(20) NOT NULL COMMENT '来源节点ID',
  `to_flow_node_id` bigint(20) DEFAULT NULL COMMENT '到达节点ID',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `from_node_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '来源节点类型 -1 发起 0 单行、1 并行、2 结束 3 虚拟',
  `to_node_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '到达节点类型 -1 发起 0 单行、1 并行、2 结束 3 虚拟',
  `var_key_val` varchar(255) DEFAULT NULL COMMENT '变量KEY取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )',
  `type` varchar(10) NOT NULL DEFAULT '0' COMMENT '连线类型 0 节点到节点 1 节点到任务',
  `to_node_job_id` bigint(20) DEFAULT NULL COMMENT '到达任务ID',
  `val_type` char(1) DEFAULT NULL COMMENT '条件模式：0简单模式 1SpEL模式 2专业模式 3Http模式',
  `label` varchar(255) DEFAULT NULL COMMENT '连线名称',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `flow_inst_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `vertices` varchar(255) DEFAULT NULL COMMENT '连线顶点',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程条件管理';

-- ----------------------------
-- Records of jf_flow_node_rel
-- ----------------------------
BEGIN;
INSERT INTO `jf_flow_node_rel` VALUES (1677729871153000004, 1668790077997000002, 'FormPermission', 1677688514110000002, 1677688590244000006, 1, '2023-03-02 12:06:35', 1, '2024-03-16 23:25:58', '0', '-1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 602,\n        \"y\": 293\n    },\n    {\n        \"x\": 602,\n        \"y\": 170\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677729878776000005, 1668790077997000002, 'FormPermission', 1677688514110000002, 1677688593826000008, 1, '2023-03-02 12:06:35', 1, '2024-03-16 23:25:58', '0', '-1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677729881848000006, 1668790077997000002, 'FormPermission', 1677688590244000006, 1677688592089000007, 1, '2023-03-02 12:06:35', 1, '2024-03-16 23:25:58', '0', '1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677729884400000007, 1668790077997000002, 'FormPermission', 1677688592089000007, 1677688595646000009, 1, '2023-03-02 12:06:35', 1, '2024-03-16 23:25:58', '0', '1', '2', NULL, '0', NULL, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 1088,\n        \"y\": 170\n    },\n    {\n        \"x\": 1088,\n        \"y\": 292.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677729886660000008, 1668790077997000002, 'FormPermission', 1677688593826000008, 1677688595646000009, 1, '2023-03-02 12:06:36', 1, '2024-03-16 23:25:58', '0', '1', '2', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731525533000006, 1663850423578000013, 'TestKey', 1677683136126000020, 1677683152440000021, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731527455000007, 1663850423578000013, 'TestKey', 1677683152440000021, NULL, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '0', '0', NULL, '1', 1677683161676000022, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731529527000008, 1663850423578000013, 'TestKey', 1677683152440000021, 1677683305541000026, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '0', '0', '#isGoEnd == 1', '0', NULL, '1', 'isGoEnd == 1', NULL, NULL, '[\n    {\n        \"x\": 779,\n        \"y\": 190.5\n    },\n    {\n        \"x\": 779,\n        \"y\": 93.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731531881000009, 1663850423578000013, 'TestKey', 1677683152440000021, 1677683307338000027, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '0', '0', '#isGoEnd != 1', '0', NULL, '1', 'isGoEnd != 1', NULL, NULL, '[\n    {\n        \"x\": 779,\n        \"y\": 190.5\n    },\n    {\n        \"x\": 779,\n        \"y\": 289\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731534383000010, 1663850423578000013, 'TestKey', 1677683305541000026, 1677683283642000025, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '0', '2', NULL, '0', NULL, '1', '', NULL, NULL, '[\n    {\n        \"x\": 1039,\n        \"y\": 93.5\n    },\n    {\n        \"x\": 1039,\n        \"y\": 190.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731536310000011, 1663850423578000013, 'TestKey', 1677683307338000027, 1677683283642000025, 1, '2023-03-02 12:34:34', 1, '2024-03-17 16:12:30', '0', '0', '2', NULL, '0', NULL, '1', '', NULL, NULL, '[\n    {\n        \"x\": 1036,\n        \"y\": 288.5\n    },\n    {\n        \"x\": 1036,\n        \"y\": 190.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731717715000015, 1663698819878000001, 'TestZeroCode', 1677686588872000008, 1677686594395000009, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731720058000016, 1663698819878000001, 'TestZeroCode', 1677686594395000009, 1677686618687000012, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '0', '1', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731722210000017, 1663698819878000001, 'TestZeroCode', 1677686594395000009, 1677686614774000011, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '0', '1', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731729295000019, 1663698819878000001, 'TestZeroCode', 1677686618687000012, 1677686602089000010, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731732198000020, 1663698819878000001, 'TestZeroCode', 1677686614774000011, NULL, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '1', '0', NULL, '1', 1677686667002000014, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731739042000021, 1663698819878000001, 'TestZeroCode', 1677686614774000011, NULL, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '1', '0', NULL, '1', 1677686669770000015, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677731793516000022, 1663698819878000001, 'TestZeroCode', 1677686602089000010, 1677686627943000013, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '0', '2', '#isGoEnd != 3', '0', NULL, '1', 'isGoEnd != 3', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732008040000024, 1663698819878000001, 'TestZeroCode', 1677686602089000010, 1677686594395000009, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '0', '0', '#isGoEnd == 3', '0', NULL, '1', 'isGoEnd == 3', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732331762000025, 1663698819878000001, 'TestZeroCode', 1677686614774000011, 1677686602089000010, 1, '2023-03-02 12:51:02', 1, '2024-03-17 16:22:28', '0', '1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732940875000042, 1661171611773000001, 'HandoverFlow', 1677732865656000034, 1677732909827000040, 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', '0', '0', '0', '#var.isNeedReceive == 1', '0', NULL, '1', '需接收人审批', NULL, NULL, '[\n    {\n        \"x\": 704,\n        \"y\": 218.5\n    },\n    {\n        \"x\": 704,\n        \"y\": 112.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732947909000043, 1661171611773000001, 'HandoverFlow', 1677732851137000033, 1677732865656000034, 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', '0', '-1', '0', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732949638000044, 1661171611773000001, 'HandoverFlow', 1677732865656000034, NULL, 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', '0', '0', '0', NULL, '1', 1677732876691000035, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732955555000045, 1661171611773000001, 'HandoverFlow', 1677732909827000040, 1677732907023000039, 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', '0', '0', '2', NULL, '0', NULL, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 949,\n        \"y\": 112.5\n    },\n    {\n        \"x\": 949,\n        \"y\": 218.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677732961229000046, 1661171611773000001, 'HandoverFlow', 1677732865656000034, 1677732907023000039, 1, '2023-03-02 12:59:57', 1, '2024-03-17 14:29:43', '0', '0', '2', '#var.isNeedReceive != 1', '0', NULL, '1', '无需接收人审批', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677733542300000061, 1660711059233000001, 'AskLeave', 1677733473617000054, 1677733503522000058, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '-1', '0', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677733547848000063, 1660711059233000001, 'AskLeave', 1677733503522000058, NULL, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '0', '0', NULL, '1', 1677733501738000057, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 444.5,\n        \"y\": 304\n    },\n    {\n        \"x\": 523,\n        \"y\": 304\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677733570509000064, 1660711059233000001, 'AskLeave', 1677733503522000058, NULL, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '0', '0', NULL, '1', 1677733500235000056, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 444.5,\n        \"y\": 304\n    },\n    {\n        \"x\": 355,\n        \"y\": 304\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677734363559000073, 1660711059233000001, 'AskLeave', 1677733629144000065, 1677734357077000072, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '0', '4', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677734604626000079, 1660711059233000001, 'AskLeave', 1677733503522000058, 1677734587498000078, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '0', '1', '0<#days && #days<=3', '0', NULL, '1', '大于0且小于等于3（SpEL表达式）', NULL, NULL, '[\n    {\n        \"x\": 602,\n        \"y\": 237.5\n    },\n    {\n        \"x\": 602,\n        \"y\": 333\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677734607939000080, 1660711059233000001, 'AskLeave', 1677733503522000058, 1677734581812000077, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '0', '1', '#days>3', '0', NULL, '1', '请假大于3天', NULL, NULL, '[\n    {\n        \"x\": 602,\n        \"y\": 237.5\n    },\n    {\n        \"x\": 602,\n        \"y\": 139\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677734610022000081, 1660711059233000001, 'AskLeave', 1677734581812000077, 1677733629144000065, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '1', '0', NULL, '0', NULL, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 907,\n        \"y\": 139\n    },\n    {\n        \"x\": 907,\n        \"y\": 239.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1677734611786000082, 1660711059233000001, 'AskLeave', 1677734587498000078, 1677733629144000065, 1, '2023-03-02 13:24:56', 1, '2024-03-17 14:35:15', '0', '1', '0', NULL, '0', NULL, NULL, '', NULL, NULL, '[\n    {\n        \"x\": 908,\n        \"y\": 333\n    },\n    {\n        \"x\": 908,\n        \"y\": 239.5\n    }\n]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1690289964265000006, 1690289416119000001, 'DynamicSignature', 1690289583078000001, 1690289584183000002, 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1690289967853000007, 1690289416119000001, 'DynamicSignature', 1690289584183000002, 1690289585185000003, 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', '0', '0', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1690289969350000008, 1690289416119000001, 'DynamicSignature', 1690289585185000003, 1690289586509000004, 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', '0', '0', '1', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1690289971655000009, 1690289416119000001, 'DynamicSignature', 1690289586509000004, 1690289589195000005, 1, '2023-07-25 21:03:58', 1, '2024-03-17 16:25:26', '0', '1', '2', '#input104728 != 1', '0', NULL, '1', 'isGoEnd != 1', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692022816950000028, 1692022814348000008, 'TestDingDing', 1692023728415000023, 1692023738815000028, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692022819416000030, 1692022814348000008, 'TestDingDing', 1692022816944000026, 1692022819404000029, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692023728437000024, 1692022814348000008, 'TestDingDing', 1692022819404000029, 1692023728415000023, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '0', '1', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692023731371000026, 1692022814348000008, 'TestDingDing', 1692023731352000025, 1692023738815000028, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692023731371000027, 1692022814348000008, 'TestDingDing', 1692022819404000029, 1692023731352000025, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '0', '1', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692023738848000029, 1692022814348000008, 'TestDingDing', 1692023738815000028, 1692022816948000027, 1, '2023-08-15 17:59:40', 1, '2024-05-17 14:56:15', '0', '0', '2', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103850969000011, 1692103815657000001, 'TestDingGate', 1692103853707000012, 1692103869912000021, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103856670000015, 1692103815657000001, 'TestDingGate', 1692103856654000014, 1692103869912000021, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103860003000017, 1692103815657000001, 'TestDingGate', 1692103865631000019, 1692103853707000012, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103860003000018, 1692103815657000001, 'TestDingGate', 1692103865631000019, 1692103856654000014, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103865651000020, 1692103815657000001, 'TestDingGate', 1692103817782000001, 1692103865631000019, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '-1', '1', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1692103869934000022, 1692103815657000001, 'TestDingGate', 1692103869912000021, 1692103817786000002, 1, '2023-08-15 20:58:51', 1, '2023-08-15 23:31:54', '0', '1', '2', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1700921821949000001, 1690289416119000001, 'DynamicSignature', 1690289586509000004, 1690289584183000002, 1, '2023-11-25 22:23:30', 1, '2024-03-17 16:25:26', '0', '1', '0', '#input104728 == 1', '0', NULL, '1', 'isGoEnd == 1', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701005092151000005, 1701005091303000002, 'CustomVuePage', 1701005445080000008, 1701005092149000004, 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:30', '0', '0', '2', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701005109139000007, 1701005091303000002, 'CustomVuePage', 1701005092143000003, 1701005109127000006, 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:30', '0', '-1', '0', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701005445099000009, 1701005091303000002, 'CustomVuePage', 1701005109127000006, 1701005445080000008, 1, '2023-11-26 21:34:00', 1, '2023-11-26 21:47:30', '0', '0', '0', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701008233491000042, 1701008232801000039, 'JDShopParSonFlow', 1701008289224000047, 1701008233489000041, 1, '2023-11-26 22:39:28', 1, '2023-11-26 22:44:30', '1', '1', '2', NULL, '0', NULL, NULL, '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701008237486000044, 1701008232801000039, 'JDShopParSonFlow', 1701008233487000040, 1701008237478000043, 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701008249081000046, 1701008232801000039, 'JDShopParSonFlow', 1701008237478000043, 1701008249074000045, 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', '0', '0', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701008298656000049, 1701008232801000039, 'JDShopParSonFlow', 1701008249074000045, 1701008237478000043, 1, '2023-11-26 22:39:28', 1, '2024-05-17 14:54:20', '0', '0', '0', '#link.', '0', NULL, '0', '未支付: 支付金额 <= 0', NULL, NULL, '[\n                {\n                    \"x\": 766,\n                    \"y\": 495\n                },\n                {\n                    \"x\": 766,\n                    \"y\": 342.5\n                }\n            ]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701010386428000060, 1701010376317000057, 'JDShopSonFlow', 1701010451663000063, 1701010386427000059, 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', '0', '0', '4', '#isReceived == 1', '0', NULL, '1', '已收到货 isReceived == 1', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701010449450000062, 1701010376317000057, 'JDShopSonFlow', 1701010386424000058, 1701010449439000061, 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701010451676000064, 1701010376317000057, 'JDShopSonFlow', 1701010449439000061, 1701010451663000063, 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', '0', '0', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701010634174000067, 1701010376317000057, 'JDShopSonFlow', 1701010451663000063, 1701010449439000061, 1, '2023-11-26 22:59:16', 1, '2024-05-17 14:55:01', '0', '0', '0', '#isReceived != 1', '0', NULL, '1', '未收到货 isReceived != 1', NULL, NULL, '[\n                {\n                    \"x\": 768,\n                    \"y\": 517.5\n                },\n                {\n                    \"x\": 768,\n                    \"y\": 342.5\n                }\n            ]', 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701012100023000020, 1701008232801000039, 'JDShopParSonFlow', 1701008249074000045, 1701012098212000019, 1, '2023-11-26 23:27:22', 1, '2024-05-17 14:54:20', '0', '0', '0', '#link.', '0', NULL, '0', '已支付: 支付金额 > 0', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1701012103643000021, 1701008232801000039, 'JDShopParSonFlow', 1701012098212000019, 1701008233489000041, 1, '2023-11-26 23:27:22', 1, '2024-05-17 14:54:20', '0', '0', '2', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1703568882608000003, 1703563526494000002, 'SubFormTest', 1703569048970000006, 1703568882606000002, 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', '0', '0', '2', '#mainInfoId != 0', '0', NULL, '1', '主表信息ID != 0', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1703569033559000005, 1703563526494000002, 'SubFormTest', 1703568882593000001, 1703569033526000004, 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', '0', '-1', '0', NULL, '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1703569048991000007, 1703563526494000002, 'SubFormTest', 1703569033526000004, 1703569048970000006, 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', '0', '0', '0', '', '0', NULL, '1', '', NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_node_rel` VALUES (1703569192910000008, 1703563526494000002, 'SubFormTest', 1703569048970000006, 1703569033526000004, 1, '2023-12-26 13:43:29', 1, '2024-05-16 16:56:41', '0', '0', '0', '#mainInfoId == 0', '0', NULL, '1', '主表信息ID == 0', NULL, NULL, '[\n                {\n                    \"x\": 762,\n                    \"y\": 517.5\n                },\n                {\n                    \"x\": 762,\n                    \"y\": 342.5\n                }\n            ]', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_flow_rule
-- ----------------------------
DROP TABLE IF EXISTS `jf_flow_rule`;
CREATE TABLE `jf_flow_rule` (
  `id` bigint(20) NOT NULL,
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `flow_node_id` bigint(20) DEFAULT NULL COMMENT '节点定义ID',
  `type` char(1) NOT NULL COMMENT '数据类型：0人员规则 1条件规则 2父子流程 3监听事件 4查询/更新表单Http参数 5保存子流程表单Http接口 6更新子流程表单Http接口 7更新父流程表单Http接口',
  `val_type` char(1) NOT NULL COMMENT '数据值类型：0简单模式 1SpEL模式 2专业模式 3Http模式',
  `group_id` bigint(20) DEFAULT NULL COMMENT '同组条件ID',
  `groups_type` char(1) DEFAULT NULL COMMENT '条件组关系：0 且 1 或',
  `group_type` char(1) DEFAULT NULL COMMENT '组内条件关系：0 且 1 或',
  `var_key_val` varchar(255) DEFAULT NULL COMMENT '条件取值来源：#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )',
  `operator` varchar(2) DEFAULT NULL COMMENT '运算符：0等于 1不等于 2大于 3大于等于 4小于 5小于等于 6包含 7不包含',
  `var_val` varchar(255) DEFAULT NULL COMMENT '变量值',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `flow_node_rel_id` bigint(20) DEFAULT NULL COMMENT '节点连线ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '参与者角色ID',
  `param_from` char(1) DEFAULT NULL COMMENT '参数来源：0请求头、1传参、2回参',
  `param_val_type` char(1) DEFAULT NULL COMMENT '参数值类型：0表单字段、1SpEL表达式、2固定值',
  `target_prop` varchar(255) DEFAULT NULL COMMENT '目标属性',
  `param_type` char(1) DEFAULT NULL COMMENT '参数类型：0json、1form、2query',
  `flow_clazz_id` bigint(20) DEFAULT NULL COMMENT '监听事件ID',
  `http_url` varchar(255) DEFAULT NULL COMMENT 'http请求地址',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `job_type` varchar(2) DEFAULT NULL COMMENT '任务类型 -1无 0用户 1角色 2岗位 3部门',
  `flow_inst_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='条件/人员规则';

-- ----------------------------
-- Records of jf_flow_rule
-- ----------------------------
BEGIN;
INSERT INTO `jf_flow_rule` VALUES (1791361592951328770, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '2', '3', NULL, NULL, NULL, '#form.commodity', NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, '1', '0', '#form.commodity', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592951328771, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '2', '3', NULL, NULL, NULL, '#form.shipAddress', NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, '1', '1', '#form.shipAddress', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592955523073, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '2', '3', NULL, NULL, NULL, '五栋1单元', NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, '1', '2', '#form.toAddress', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592955523074, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '2', '3', NULL, NULL, NULL, '#form.isShipped', NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, '2', '0', '#form.isShipped', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592959717378, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '2', '3', NULL, NULL, NULL, '#form.isReceived', NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, '2', '0', '#form.isReceived', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592963911681, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '5', '3', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/order-base/start-sub-flow', 'POST', NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592972300290, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '6', '3', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2024-05-17 14:54:20', 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/order-base/restart-sub-flow', 'PUT', NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361592976494594, 'JDShopParSonFlow', 1701008232801000039, 1701012098212000019, '7', '3', NULL, NULL, NULL, NULL, NULL, NULL, 1, '2024-05-17 14:54:20', 1, '2024-05-17 14:54:20', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/order-base/back-par-flow', 'PUT', NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361593110712321, 'JDShopParSonFlow', 1701008232801000039, NULL, '1', '0', 1791361593102323713, '0', '0', '#form.paymentAmount', '5', '0', 1, '2024-05-17 14:54:20', NULL, NULL, 1701008298656000049, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `jf_flow_rule` VALUES (1791361593152655361, 'JDShopParSonFlow', 1701008232801000039, NULL, '1', '0', 1791361593148461057, '0', '0', '#form.paymentAmount', '2', '0', 1, '2024-05-17 14:54:20', NULL, NULL, 1701012100023000020, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_flow_variable
-- ----------------------------
DROP TABLE IF EXISTS `jf_flow_variable`;
CREATE TABLE `jf_flow_variable` (
  `id` bigint(20) NOT NULL,
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `var_key` varchar(64) NOT NULL COMMENT '变量KEY',
  `var_val` varchar(64) NOT NULL COMMENT '变量值',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `code` varchar(64) NOT NULL COMMENT '工单CODE，便于业务人员查看',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程参数管理';

-- ----------------------------
-- Records of jf_flow_variable
-- ----------------------------
BEGIN;
INSERT INTO `jf_flow_variable` VALUES (1631234697216655361, 1631234697145352194, 'flowInstId', '1631234697145352194', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697225043970, 1631234697145352194, 'formId', '1572932685786275842', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697237626882, 1631234697145352194, 'code', 'BGSQ-GD-20230302-00151', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697250209793, 1631234697145352194, 'createUser', '1', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697266987009, 1631234697145352194, 'id', '1631234697128574978', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697275375618, 1631234697145352194, 'flowKey', 'TestZeroCode', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234697279569922, 1631234697145352194, 'isGoEnd', '666', 'TestZeroCode', 1, '2023-03-02 18:07:13', NULL, NULL, 'BGSQ-GD-20230302-00151', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234944990969858, 1631234944961609729, 'flowInstId', '1631234944961609729', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945003552770, 1631234944961609729, 'formId', '1576909046599172097', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945016135682, 1631234944961609729, 'code', 'BGSQ-GD-20230302-00294', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945020329985, 1631234944961609729, 'createUser', '1', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945041301505, 1631234944961609729, 'id', '1631234944122748930', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945049690114, 1631234944961609729, 'flowKey', 'TestKey', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631234945058078722, 1631234944961609729, 'isGoEnd', '55', 'TestKey', 1, '2023-03-02 18:08:12', NULL, NULL, 'BGSQ-GD-20230302-00294', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235003652505601, 1631235003606368258, 'formId', '1593649608467542018', 'FormPermission', 1, '2023-03-02 18:08:26', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235003681865729, 1631235003606368258, 'id', '1631235003606368257', 'FormPermission', 1, '2023-03-02 18:08:26', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155456950273, 1631235155431784450, 'flowInstId', '1631235155431784450', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155465338882, 1631235155431784450, 'leaveDays', '6', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155473727489, 1631235155431784450, 'code', 'QJ-GD-20230302-00563', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155477921793, 1631235155431784450, 'createUser', '1', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155494699009, 1631235155431784450, 'id', '1631235155427590146', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1631235155503087617, 1631235155431784450, 'flowKey', 'AskLeave', 'AskLeave', 1, '2023-03-02 18:09:03', NULL, NULL, 'QJ-GD-20230302-00563', 1);
INSERT INTO `jf_flow_variable` VALUES (1675782719745064962, 1631235003606368258, 'code', 'BGSQ-GD-20230302-00016', 'FormPermission', 1, '2023-07-03 16:25:10', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1675782719749259265, 1631235003606368258, 'flowKey', 'FormPermission', 'FormPermission', 1, '2023-07-03 16:25:10', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1675782719753453570, 1631235003606368258, 'flowInstId', '1631235003606368258', 'FormPermission', 1, '2023-07-03 16:25:10', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1675782719766036481, 1631235003606368258, 'createUser', '1', 'FormPermission', 1, '2023-07-03 16:25:10', NULL, NULL, 'BGSQ-GD-20230302-00016', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100370309121, 1683828100328366082, 'flowInstId', '1683828100328366082', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100378697730, 1683828100328366082, 'formId', '1683825630990606338', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100387086338, 1683828100328366082, 'code', 'BGSQ-GD-20230725-00606', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100391280642, 1683828100328366082, 'createUser', '1', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100408057857, 1683828100328366082, 'id', '1683828100307394561', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1683828100412252161, 1683828100328366082, 'flowKey', 'DynamicSignature', 'DynamicSignature', 1, '2023-07-25 21:14:38', NULL, NULL, 'BGSQ-GD-20230725-00606', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082833653761, 1691395082670075906, 'flowInstId', '1691395082670075906', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082867208193, 1691395082670075906, 'formId', '1691096635442229249', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082883985410, 1691395082670075906, 'code', 'BGSQ-GD-20230815-00224', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082909151234, 1691395082670075906, 'createUser', '1', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082942705665, 1691395082670075906, 'id', '1691395082628132865', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691395082963677185, 1691395082670075906, 'flowKey', 'TestDingDing', 'TestDingDing', 1, '2023-08-15 18:23:07', NULL, NULL, 'BGSQ-GD-20230815-00224', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166165397506, 1691435166140231682, 'flowInstId', '1691435166140231682', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166173786114, 1691435166140231682, 'formId', '1691434590803357698', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166177980417, 1691435166140231682, 'code', 'BGSQ-GD-20230815-00654', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166182174721, 1691435166140231682, 'createUser', '1', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166190563329, 1691435166140231682, 'id', '1691435166140231681', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1691435166194757634, 1691435166140231682, 'flowKey', 'TestDingGate', 'TestDingGate', 1, '2023-08-15 21:02:24', NULL, NULL, 'BGSQ-GD-20230815-00654', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727503454209, 1700039727486676993, 'isNeedReceive', '1', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727511842817, 1700039727486676993, 'flowInstId', '1700039727486676993', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727520231425, 1700039727486676993, 'code', 'GZJJ-GD-20230908-00673', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727528620033, 1700039727486676993, 'createUser', '1', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727545397250, 1700039727486676993, 'id', '1700039688559341569', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1700039727549591553, 1700039727486676993, 'flowKey', 'HandoverFlow', 'HandoverFlow', 1, '2023-09-08 14:53:51', NULL, NULL, 'GZJJ-GD-20230908-00673', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770563446734850, 1728770563404791810, 'formId', '1701005090330000001', 'CustomVuePage', 1, '2023-11-26 21:39:56', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770563471900673, 1728770563404791810, 'id', '1728770563388014594', 'CustomVuePage', 1, '2023-11-26 21:39:56', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770807223877634, 1728770563404791810, 'code', 'BGSQ-GD-20231126-00956', 'CustomVuePage', 1, '2023-11-26 21:40:54', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770807228071938, 1728770563404791810, 'flowKey', 'CustomVuePage', 'CustomVuePage', 1, '2023-11-26 21:40:54', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770807236460546, 1728770563404791810, 'flowInstId', '1728770563404791810', 'CustomVuePage', 1, '2023-11-26 21:40:54', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1728770807240654850, 1728770563404791810, 'createUser', '1', 'CustomVuePage', 1, '2023-11-26 21:40:54', NULL, NULL, 'BGSQ-GD-20231126-00956', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036577279361026, 1729036577254195202, 'formId', '1701008232320000038', 'JDShopParSonFlow', 1, '2023-11-27 15:16:59', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036577296138243, 1729036577254195202, 'id', '1729036577254195201', 'JDShopParSonFlow', 1, '2023-11-27 15:16:59', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036742245531649, 1729036577254195202, 'code', 'BGSQ-GD-20231127-00313', 'JDShopParSonFlow', 1, '2023-11-27 15:17:38', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036742249725954, 1729036577254195202, 'flowKey', 'JDShopParSonFlow', 'JDShopParSonFlow', 1, '2023-11-27 15:17:38', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036742258114561, 1729036577254195202, 'flowInstId', '1729036577254195202', 'JDShopParSonFlow', 1, '2023-11-27 15:17:38', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036742262308867, 1729036577254195202, 'createUser', '1', 'JDShopParSonFlow', 1, '2023-11-27 15:17:38', NULL, NULL, 'BGSQ-GD-20231127-00313', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765721051137, 1729036763657453570, 'flowInstId', '1729036763657453570', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765729439745, 1729036763657453570, 'formId', '1701010375704000056', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765733634050, 1729036763657453570, 'code', 'BGSQ-GD-20231127-00715', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765737828354, 1729036763657453570, 'createUser', '1', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765750411266, 1729036763657453570, 'id', '1729036765700079617', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1729036765754605569, 1729036763657453570, 'flowKey', 'JDShopSonFlow', 'JDShopSonFlow', 1, '2023-11-27 15:17:44', NULL, NULL, 'BGSQ-GD-20231127-00715', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380297736194, 1791030389211779073, 'flowInstId', '1791030389211779073', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380306124802, 1791030389211779073, 'formId', '1703563525573000001', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380314513410, 1791030389211779073, 'code', 'BGSQ-GD-20240516-00985', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380322902017, 1791030389211779073, 'createUser', '1', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380335484929, 1791030389211779073, 'id', '1791030389140475905', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
INSERT INTO `jf_flow_variable` VALUES (1791286380343873537, 1791030389211779073, 'flowKey', 'SubFormTest', 'SubFormTest', 1, '2024-05-17 09:55:28', NULL, NULL, 'BGSQ-GD-20240516-00985', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_form_option
-- ----------------------------
DROP TABLE IF EXISTS `jf_form_option`;
CREATE TABLE `jf_form_option` (
  `id` bigint(20) NOT NULL,
  `def_flow_id` bigint(20) DEFAULT NULL COMMENT '流程定义ID',
  `flow_node_id` bigint(20) DEFAULT NULL COMMENT '节点定义ID',
  `prop_id` varchar(125) DEFAULT NULL COMMENT '属性唯一ID',
  `label` varchar(255) DEFAULT NULL COMMENT '属性名称',
  `prop` varchar(125) DEFAULT NULL COMMENT '属性名',
  `sub_form` varchar(125) DEFAULT NULL COMMENT '子表单属性名',
  `prop_type` varchar(64) DEFAULT NULL COMMENT '属性类型',
  `print_info` text COMMENT '打印信息',
  `type` char(1) NOT NULL COMMENT '数据类型：0字段定义 1权限配置 2打印设计',
  `perm_type` varchar(2) DEFAULT NULL COMMENT '权限类型：-1隐藏 0只读 1可编辑',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `form_type` char(1) NOT NULL COMMENT '页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面',
  `form_id` bigint(20) NOT NULL COMMENT '表单ID',
  `form_name` varchar(255) DEFAULT NULL COMMENT '表单名称',
  `path` varchar(128) DEFAULT NULL COMMENT '页面路径（动态组件）',
  `flow_key` varchar(255) DEFAULT NULL COMMENT '业务KEY',
  `flow_inst_id` bigint(20) DEFAULT NULL COMMENT '流程实例ID',
  `form_tabs_id` bigint(20) DEFAULT NULL COMMENT '页面ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单/页面操作表';

-- ----------------------------
-- Records of jf_form_option
-- ----------------------------
BEGIN;
INSERT INTO `jf_form_option` VALUES (1769022299394297857, 1668790077997000002, 1677688590244000006, 'input11209', '用户名', 'userName', NULL, 'input', NULL, '1', '1', 1, '2024-03-16 23:25:58', NULL, NULL, '1', 1593649608467542018, NULL, NULL, 'FormPermission', NULL, 3, 1);
INSERT INTO `jf_form_option` VALUES (1769022299402686466, 1668790077997000002, 1677688590244000006, 'radio49338', '爱好', 'hobby', NULL, 'radio', NULL, '1', '0', 1, '2024-03-16 23:25:58', NULL, NULL, '1', 1593649608467542018, NULL, NULL, 'FormPermission', NULL, 3, 1);
INSERT INTO `jf_form_option` VALUES (1769022299499155458, 1668790077997000002, 1677688593826000008, 'input60479', '用户名', 'userName', NULL, 'input', NULL, '1', '0', 1, '2024-03-16 23:25:58', NULL, NULL, '0', 17, '审批页面字段权限', '/jsonflow/tabs-option/flow', 'FormPermission', NULL, 17, 1);
INSERT INTO `jf_form_option` VALUES (1769024415814934530, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '<p>发起人：${createUser}</p>\n<p>工单编号：${code}</p>\n<p>发起时间：${createTime}</p>\n<p>完成时间：${finishTime}</p>\n<p>&nbsp;</p>\n<p>用户名：${userName}</p>\n<p>date：${date101110}</p>\n<p>number：${number86249}</p>', '2', NULL, 1, '2024-03-16 23:34:22', NULL, NULL, '0', 17, '审批页面字段权限', '/jsonflow/tabs-option/flow', NULL, NULL, NULL, 1);
INSERT INTO `jf_form_option` VALUES (1791035871901110274, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '<p>主表信息1：${mainInfoId}</p>\n<p>主表信息2：${daterange53963}</p>\n<p>&nbsp;</p>\n<p>消息通知管理：</p>\n<table style=\"border-collapse: collapse; width: 100.187%;\" border=\"1\">\n<tbody>\n<tr>\n<td style=\"width: 17.8653%;\">流程KEY</td>\n<td style=\"width: 15.4334%;\">消息类型</td>\n<td style=\"width: 15.5269%;\">关联主表ID</td>\n<td style=\"width: 16.7429%;\">通知人员</td>\n<td style=\"width: 17.8653%;\">任务类型</td>\n<td style=\"width: 16.5558%;\">通知状态</td>\n</tr>\n<tr>\n<td style=\"width: 17.8653%;\">${subFormData.flowKey}</td>\n<td style=\"width: 15.4334%;\">${subFormData.type}</td>\n<td style=\"width: 15.5269%;\">${subFormData.data}</td>\n<td style=\"width: 16.7429%;\">${subFormData.userId}</td>\n<td style=\"width: 17.8653%;\">${subFormData.jobType}</td>\n<td style=\"width: 16.5558%;\">${subFormData.status}</td>\n</tr>\n</tbody>\n</table>\n<p>&nbsp;</p>\n<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\n<tbody>\n<tr>\n<td style=\"width: 48.6613%;\">流程KEY</td>\n<td style=\"width: 48.6613%;\">消息类型</td>\n</tr>\n<tr>\n<td style=\"width: 48.6613%;\">${subFormData.flowKey}</td>\n<td style=\"width: 48.6613%;\">${subFormData.type}</td>\n</tr>\n</tbody>\n</table>\n<p>&nbsp;</p>\n<p>发起人：${createUser}</p>\n<p>工单编号：${code}</p>\n<p>发起时间：${createTime}</p>\n<p>完成时间：${finishTime}</p>', '2', NULL, 1, '2024-03-16 23:33:46', NULL, NULL, '1', 1703563525573000001, '父子表查询新增删除', NULL, NULL, NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_node_job
-- ----------------------------
DROP TABLE IF EXISTS `jf_node_job`;
CREATE TABLE `jf_node_job` (
  `id` bigint(20) NOT NULL,
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程ID',
  `flow_node_id` bigint(20) DEFAULT NULL COMMENT '节点ID',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `timeout` int(11) DEFAULT NULL COMMENT '超时时间 0不限制',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_skip_rejected` char(1) NOT NULL DEFAULT '0' COMMENT '被驳回后是否可跳过 0 否  1 是',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `job_name` varchar(255) NOT NULL COMMENT '任务名称',
  `user_key` varchar(64) DEFAULT NULL COMMENT '动态办理人KEY',
  `user_key_val` varchar(255) DEFAULT NULL COMMENT '#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )',
  `is_now_run` char(1) NOT NULL DEFAULT '0' COMMENT '被分配后是否立即运行 0否 1是',
  `from_type` char(1) NOT NULL DEFAULT '0' COMMENT '任务来源类型 0默认任务 1不分离任务 2分离任务',
  `dist_flow_node_id` bigint(20) DEFAULT NULL COMMENT '待分配办理人节点',
  `val_type` varchar(2) DEFAULT '-2' COMMENT '人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `position_size` varchar(255) DEFAULT NULL COMMENT '节点位置大小',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='节点任务设置';

-- ----------------------------
-- Records of jf_node_job
-- ----------------------------
BEGIN;
INSERT INTO `jf_node_job` VALUES (1630953145756839937, 1663850423578000013, 1677683136126000020, 'TestKey', 1, '2023-03-01 23:28:26', 0, 1, 1, '2024-03-17 16:12:30', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630953145933000706, 1663850423578000013, 1677683283642000025, 'TestKey', 1, '2023-03-01 23:28:26', 0, 1, 1, '2024-03-17 16:12:30', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630953146029469698, 1663850423578000013, 1677683305541000026, 'TestKey', 1, '2023-03-01 23:28:26', 0, 1, 1, '2024-03-17 16:12:30', '0', '0', '串行 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630953146130132994, 1663850423578000013, 1677683307338000027, 'TestKey', 1, '2023-03-01 23:28:26', 0, 1, 1, '2024-03-17 16:12:30', '0', '0', '串行 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408190459906, 1663698819878000001, 1677686588872000008, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408257568770, 1663698819878000001, 1677686594395000009, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '干大事的 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408320483330, 1663698819878000001, 1677686602089000010, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '串行 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408421146625, 1663698819878000001, 1677686618687000012, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '并行 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408505032706, 1663698819878000001, 1677686627943000013, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630965408580530178, 1663698819878000001, 1677686693563000018, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '虚拟', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630971561175707649, 1668790077997000002, 1677688514110000002, 'FormPermission', 1, '2023-03-02 00:41:37', 0, 1, 1, '2024-03-16 23:25:58', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630971561221844993, 1668790077997000002, 1677688590244000006, 'FormPermission', 1, '2023-03-02 00:41:37', 0, 1, 1, '2024-03-16 23:25:58', '0', '0', '表单权限 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630971561267982338, 1668790077997000002, 1677688592089000007, 'FormPermission', 1, '2023-03-02 00:41:37', 0, 1, 1, '2024-03-16 23:25:58', '0', '0', '自定义权限 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630971561314119682, 1668790077997000002, 1677688593826000008, 'FormPermission', 1, '2023-03-02 00:41:37', 0, 1, 1, '2024-03-16 23:25:58', '0', '0', '审批页面权限 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1630971561360257026, 1668790077997000002, 1677688595646000009, 'FormPermission', 1, '2023-03-02 00:41:37', 0, 1, 1, '2024-03-16 23:25:58', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631157370713567233, 1661171611773000001, 1677732851137000033, 'HandoverFlow', 1, '2023-03-02 12:59:57', 0, 1, 1, '2024-03-17 14:29:43', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631157370805841922, 1661171611773000001, 1677732899572000038, 'HandoverFlow', 1, '2023-03-02 12:59:57', 0, 1, 1, '2024-03-17 14:29:43', '0', '0', '虚拟', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631157370872950785, 1661171611773000001, 1677732907023000039, 'HandoverFlow', 1, '2023-03-02 12:59:57', 0, 1, 1, '2024-03-17 14:29:43', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631157370935865345, 1661171611773000001, 1677732909827000040, 'HandoverFlow', 1, '2023-03-02 12:59:57', 0, 1, 1, '2024-03-17 14:29:43', '0', '0', '接收人审批 (1)', 'receive_user', NULL, '0', '0', NULL, '-1', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656494673921, 1660711059233000001, 1677733473617000054, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656540811265, 1660711059233000001, 1677733476722000055, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '修改工单 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656612114434, 1660711059233000001, 1677733629144000065, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '财务 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656662446082, 1660711059233000001, 1677734357077000072, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2023-08-15 21:58:08', '0', '1', '自动结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656708583426, 1660711059233000001, 1677734581812000077, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '部门总监 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1631163656754720769, 1660711059233000001, 1677734587498000078, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '人事总监 (1)', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1677683161676000022, 1663850423578000013, 1677683152440000021, 'TestKey', 1, '2023-03-01 23:28:26', 0, 1, 1, '2024-03-17 16:12:30', '0', '0', '任务', 'create_user', NULL, '0', '2', NULL, '-1', NULL, '{\"position\": {\n                \"x\": 611,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 82\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1677686667002000014, 1663698819878000001, 1677686614774000011, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '任务', NULL, NULL, '0', '2', NULL, '-2', NULL, '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1677686669770000015, 1663698819878000001, 1677686614774000011, 'TestZeroCode', 1, '2023-03-02 00:17:10', 0, 1, 1, '2024-03-17 16:22:28', '0', '0', '任务', NULL, NULL, '0', '2', NULL, '-2', NULL, '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1677732876691000035, 1661171611773000001, 1677732865656000034, 'HandoverFlow', 1, '2023-03-02 12:59:57', 0, 1, 1, '2024-03-17 14:29:43', '0', '0', '任务', NULL, NULL, '0', '2', NULL, '-2', NULL, '{\"position\": {\n                \"x\": 556,\n                \"y\": 336\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 84\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1677733500235000056, 1660711059233000001, 1677733503522000058, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '经理', NULL, NULL, '0', '2', NULL, '-2', NULL, '{\"position\": {\n                \"x\": 317,\n                \"y\": 355\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 76\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1677733501738000057, 1660711059233000001, 1677733503522000058, 'AskLeave', 1, '2023-03-02 13:24:56', 0, 1, 1, '2024-03-17 14:35:15', '0', '0', '副经理', NULL, NULL, '0', '2', NULL, '-2', NULL, '{\"position\": {\n                \"x\": 473,\n                \"y\": 355\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 100\n            }}', 1);
INSERT INTO `jf_node_job` VALUES (1683825416573591553, 1690289416119000001, 1690289583078000001, 'DynamicSignature', 1, '2023-07-25 21:03:58', 0, 1, 1, '2024-03-17 16:25:26', '0', '0', '开始', 'create_user', '#user.userId', '0', '0', NULL, '2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1683825416653283329, 1690289416119000001, 1690289584183000002, 'DynamicSignature', 1, '2023-07-25 21:03:58', 0, 1, 1, '2024-03-17 16:25:26', '0', '0', '分配下一节点不确定任务数和办理人员', NULL, NULL, '0', '0', 1690289586509000004, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1683825416724586497, 1690289416119000001, 1690289585185000003, 'DynamicSignature', 1, '2023-07-25 21:03:58', 0, 1, 1, '2024-03-17 16:25:26', '0', '0', '两个节点不一定挨着', 'dynamic_user', '#distActorServiceImpl.getUserDeptLeaderId(Long#1,Integer#1,String#0)', '0', '0', NULL, '2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1683825416808472578, 1690289416119000001, 1690289586509000004, 'DynamicSignature', 1, '2023-07-25 21:03:58', 0, 1, 1, '2024-03-17 16:25:26', '0', '0', '动态加减签节点：动态计算当前节点不确定任务数和办理人员', 'dynamic_signature', NULL, '0', '0', NULL, '-1', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1683825416879775746, 1690289416119000001, 1690289589195000005, 'DynamicSignature', 1, '2023-07-25 21:03:58', 0, 1, 1, '2024-03-17 16:25:26', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1691389179627593730, 1692022814348000008, 1692022816944000026, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1691389179719868418, 1692022814348000008, 1692022816948000027, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1691389180093161474, 1692022814348000008, 1692023738815000028, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '串行节点', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1691434275613995010, 1692103815657000001, 1692103817782000001, 'TestDingGate', 1, '2023-08-15 20:58:51', 0, 1, 1, '2023-08-15 23:31:54', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1691434275689492481, 1692103815657000001, 1692103817786000002, 'TestDingGate', 1, '2023-08-15 20:58:51', 0, 1, 1, '2023-08-15 23:31:54', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692022821130000031, 1692022814348000008, 1692022819404000029, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '任务11', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692023740611000030, 1692022814348000008, 1692023728415000023, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '任务22', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692023741895000031, 1692022814348000008, 1692023731352000025, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '任务33', 'create_user', '#user.userId', '0', '1', NULL, '2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692029577119000011, 1692022814348000008, 1692022819404000029, 'TestDingDing', 1, '2023-08-15 17:59:40', 0, 1, 1, '2024-05-17 14:56:15', '0', '0', '任务66', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692103875747000023, 1692103815657000001, 1692103853707000012, 'TestDingGate', 1, '2023-08-15 20:58:51', 0, 1, 1, '2023-08-15 23:31:54', '0', '0', '任务11', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692103877320000024, 1692103815657000001, 1692103856654000014, 'TestDingGate', 1, '2023-08-15 20:58:51', 0, 1, 1, '2023-08-15 23:31:54', '0', '0', '任务22', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1692104437412000028, 1692103815657000001, 1692103856654000014, 'TestDingGate', 1, '2023-08-15 21:00:57', 0, 1, 1, '2023-08-15 23:31:54', '0', '0', '任务66', NULL, NULL, '0', '1', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728769069985103874, 1701005091303000002, 1701005092143000003, 'CustomVuePage', 1, '2023-11-26 21:34:00', 0, 1, 1, '2023-11-26 21:47:29', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728769070035435521, 1701005091303000002, 1701005092149000004, 'CustomVuePage', 1, '2023-11-26 21:34:00', 0, 1, 1, '2023-11-26 21:47:29', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728769070085767170, 1701005091303000002, 1701005109127000006, 'CustomVuePage', 1, '2023-11-26 21:34:00', 0, 1, 1, '2023-11-26 21:47:29', '0', '0', '请查看开始节点', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728769070131904513, 1701005091303000002, 1701005445080000008, 'CustomVuePage', 1, '2023-11-26 21:34:00', 0, 1, 1, '2023-11-26 21:47:29', '0', '0', '请查看表单设置', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728785543856455681, 1701008232801000039, 1701008233487000040, 'JDShopParSonFlow', 1, '2023-11-26 22:39:28', 0, 1, 1, '2024-05-17 14:54:20', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728785543915175938, 1701008232801000039, 1701008233489000041, 'JDShopParSonFlow', 1, '2023-11-26 22:39:28', 0, 1, 1, '2024-05-17 14:54:20', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728785543965507585, 1701008232801000039, 1701008237478000043, 'JDShopParSonFlow', 1, '2023-11-26 22:39:28', 0, 1, 1, '2024-05-17 14:54:20', '0', '0', '添加商品并付款', 'shop_suer', '#user.userId', '0', '0', NULL, '2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728790528639959041, 1701010376317000057, 1701010386424000058, 'JDShopSonFlow', 1, '2023-11-26 22:59:16', 0, 1, 1, '2024-05-17 14:55:01', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728790528690290689, 1701010376317000057, 1701010386427000059, 'JDShopSonFlow', 1, '2023-11-26 22:59:16', 0, 1, 1, '2023-11-27 00:27:57', '0', '1', '自动结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728790528740622338, 1701010376317000057, 1701010449439000061, 'JDShopSonFlow', 1, '2023-11-26 22:59:16', 0, 1, 1, '2024-05-17 14:55:01', '0', '0', '发货', 'sell_user', '#user.userId', '0', '0', NULL, '2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728790528786759681, 1701010376317000057, 1701010451663000063, 'JDShopSonFlow', 1, '2023-11-26 22:59:16', 0, 1, 1, '2024-05-17 14:55:01', '0', '0', '收货', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1728797600450400257, 1701008232801000039, 1701012098212000019, 'JDShopParSonFlow', 1, '2023-11-26 23:27:22', 0, 1, 1, '2024-05-17 14:54:20', '0', '0', '收发货子流程确认', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1739522295011074049, 1703563526494000002, 1703568882593000001, 'SubFormTest', 1, '2023-12-26 13:43:29', 0, 1, 1, '2024-05-16 16:56:41', '0', '0', '开始', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1739522295078182913, 1703563526494000002, 1703568882606000002, 'SubFormTest', 1, '2023-12-26 13:43:29', 0, 1, 1, '2024-05-16 16:56:41', '0', '0', '结束', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1739522295141097474, 1703563526494000002, 1703569033526000004, 'SubFormTest', 1, '2023-12-26 13:43:29', 0, 1, 1, '2024-05-16 16:56:41', '0', '0', '串行节点', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
INSERT INTO `jf_node_job` VALUES (1739522295220789249, 1703563526494000002, 1703569048970000006, 'SubFormTest', 1, '2023-12-26 13:43:29', 0, 1, 1, '2024-05-16 16:56:41', '0', '0', '串行节点', NULL, NULL, '0', '0', NULL, '-2', NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_run_flow
-- ----------------------------
DROP TABLE IF EXISTS `jf_run_flow`;
CREATE TABLE `jf_run_flow` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `initiator_id` bigint(20) NOT NULL COMMENT '流程发起人ID',
  `order_id` bigint(20) NOT NULL COMMENT '工单ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(2) NOT NULL DEFAULT '0' COMMENT '流程状态 -2：撤回 -1：发起 0：运行中（正常） 1：完结 2：作废 3终止',
  `invalid_reason` varchar(512) DEFAULT NULL COMMENT '作废原因',
  `code` varchar(64) NOT NULL COMMENT '工单CODE，便于业务人员查看',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
  `form_id` bigint(20) DEFAULT NULL COMMENT '表单ID',
  `group_name` varchar(255) NOT NULL COMMENT '分组名称',
  `par_flow_inst_id` bigint(20) DEFAULT NULL COMMENT '父流程实例ID',
  `query_order` varchar(255) DEFAULT NULL COMMENT '更新工单信息接口',
  `update_order` varchar(255) DEFAULT NULL COMMENT '查询工单信息接口',
  `query_method` varchar(255) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `update_method` varchar(255) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `from_type` char(1) DEFAULT '1' COMMENT '流程设计来源 0 一键快捷设计 1 定制化开发设计',
  `flow_name` varchar(255) NOT NULL COMMENT '流程名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `auto_layout` varchar(10) DEFAULT NULL COMMENT '自动布局方向',
  `allow_job_link` char(1) DEFAULT '0' COMMENT '是否允许任务连线到其他节点 0否 1是',
  `is_job_separated` char(1) DEFAULT '0' COMMENT '是否允许节点与任务分离显示 0否 1是',
  `is_simple_mode` char(1) DEFAULT '1' COMMENT '设计模式：1 简单模式 0 专业模式',
  `connector` varchar(64) DEFAULT 'rounded' COMMENT '连线样式',
  `router` varchar(64) DEFAULT 'normal' COMMENT '连线路由',
  `is_independent` char(1) DEFAULT '0' COMMENT '配置数据独立 0否 1是',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程管理';

-- ----------------------------
-- Records of jf_run_flow
-- ----------------------------
BEGIN;
INSERT INTO `jf_run_flow` VALUES (1631234697145352194, 1663698819878000001, 1, 1631234697128574978, 'TestZeroCode', '2023-03-02 18:07:13', NULL, 1, '2022-09-23 02:33:47', 1, '2023-03-02 13:41:26', '0', NULL, 'BGSQ-GD-20230302-00151', 1, 1572932685786275842, '一键发布无需编码', NULL, NULL, NULL, NULL, NULL, '0', '一键发布无需编码', '流程条件isGoEnd取值来自工单#order.isGoEnd的值', 1, 'TB', '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1631234944961609729, 1663850423578000013, 1, 1631234944122748930, 'TestKey', '2023-03-02 18:08:12', NULL, 1, '2022-09-24 20:40:47', 1, '2023-03-02 13:03:36', '0', NULL, 'BGSQ-GD-20230302-00294', 1, 1576909046599172097, '自动建表存储', NULL, NULL, NULL, NULL, NULL, '0', '测试自动建表存储', '节点任务create_user来源当前用户#user.userId，流程条件isGoEnd取值来自工单#order.isGoEnd的值', 1, NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1631235003606368258, 1668790077997000002, 1, 1631235003606368257, 'FormPermission', '2023-03-02 18:08:26', NULL, 1, '2022-11-19 00:56:48', 1, '2023-03-02 12:15:54', '0', NULL, 'BGSQ-GD-20230302-00016', 1, 1593649608467542018, '表单/审批页面字段权限', NULL, NULL, NULL, NULL, NULL, '0', '表单/审批页面字段权限', '表单/审批页面字段权限，设置审批用户可以看到或操作哪些页面字段', 1, NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1631235155431784450, 1660711059233000001, 1, 1631235155427590146, 'AskLeave', '2023-03-02 18:09:03', NULL, 1, '2022-09-25 14:11:57', 1, '2023-03-02 13:26:38', '0', NULL, 'QJ-GD-20230302-00563', 1, NULL, '请假工单', NULL, NULL, NULL, NULL, NULL, '1', '请假工单', '流程条件days采用SpEL表达式，如0<#days && #days<=3。条件值来源工单days的值', 1, NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1683828100328366082, 1690289416119000001, 1, 1683828100307394561, 'DynamicSignature', '2023-07-25 21:14:38', NULL, 1, '2023-07-25 21:03:58', 1, '2023-07-25 21:10:54', '0', NULL, 'BGSQ-GD-20230725-00606', 1, 1683825630990606338, '动态加减签-复杂场景', NULL, NULL, NULL, NULL, NULL, '0', '动态加减签-复杂场景', '动态加减签常用于动态计算下一节点不确定任务数和办理人员', 1, 'TB', '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1691395082670075906, 1692022814348000008, 1, 1691395082628132865, 'TestDingDing', '2023-08-15 18:23:07', NULL, 1, '2023-08-15 17:59:39', 1, '2023-08-15 18:05:32', '0', NULL, 'BGSQ-GD-20230815-00224', 1, 1691096635442229249, '测试钉钉UI模式', NULL, NULL, NULL, NULL, NULL, '0', '测试钉钉UI模式', '钉钉UI模式支持普通用户操作，一套代码同时支持简单模式和专业模式', 1, 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1691435166140231682, 1692103815657000001, 1, 1691435166140231681, 'TestDingGate', '2023-08-15 21:02:24', NULL, 1, '2023-08-15 20:58:48', 1, '2023-08-15 21:00:57', '0', NULL, 'BGSQ-GD-20230815-00654', 1, 1691434590803357698, '测试钉钉网关模式', NULL, NULL, NULL, NULL, NULL, '0', '测试钉钉网关模式', '测试钉钉网关模式，支持串行网关、并行网关', 1, 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1700039727486676993, 1661171611773000001, 1, 1700039688559341569, 'HandoverFlow', '2023-09-08 14:53:51', NULL, 1, '2022-09-21 21:18:22', 1, '2023-08-15 21:59:31', '0', NULL, 'GZJJ-GD-20230908-00673', 1, NULL, '工作交接', NULL, NULL, NULL, NULL, NULL, '1', '工作交接', '接收人审批节点receive_user取值于工单逻辑分配的参与者，流程条件isNeedReceive取值于工单逻辑设置的流程条件中的值', 1, NULL, '0', '1', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1728770563404791810, 1701005091303000002, 1, 1728770563388014594, 'CustomVuePage', '2023-11-26 21:39:56', NULL, 1, '2023-11-26 21:39:56', NULL, NULL, '0', NULL, 'BGSQ-GD-20231126-00956', 1, 1701005090330000001, '自定义主表单Vue页面', NULL, NULL, NULL, NULL, NULL, '0', '自定义主表单Vue页面', '与表单设计器完全解耦，支持主表单可自定义Vue页面', 1, 'TB', '0', '0', '1', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1729036577254195202, 1701008232801000039, 1, 1729036577254195201, 'JDShopParSonFlow', '2023-11-27 15:16:59', NULL, 1, '2023-11-27 15:16:59', 1, '2023-11-27 00:27:38', '0', NULL, 'BGSQ-GD-20231127-00313', 1, 1701008232320000038, '京东购物父子流程', NULL, NULL, NULL, NULL, NULL, '0', '京东购物父子流程', '用于测试父子流程，由父流程某个节点触发子流程的发起，当子流程完结后会反向通知父流程', 1, NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1729036763657453570, 1701010376317000057, 1, 1729036765700079617, 'JDShopSonFlow', '2023-11-27 15:17:44', NULL, 1, '2023-11-27 15:17:44', 1, '2023-11-27 09:58:10', '0', NULL, 'BGSQ-GD-20231127-00715', 1, 1701010375704000056, '京东购物子流程', 1729036577254195202, NULL, NULL, NULL, NULL, '0', '京东购物子流程', '京东购物子流程，当子流程完结后会反向通知父流程', 1, NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
INSERT INTO `jf_run_flow` VALUES (1791030389211779073, 1703563526494000002, 1, 1791030389140475905, 'SubFormTest', '2024-05-17 09:55:28', NULL, 1, '2024-05-17 09:55:28', 1, '2024-05-16 16:56:41', '0', NULL, 'BGSQ-GD-20240516-00985', 1, 1703563525573000001, '父子表查询新增删除', NULL, NULL, NULL, 'GET', 'PUT', '0', '父子表查询新增删除', '父子表查询新增删除，常用于子表数据单独查询、新增、删除等自定义操作', 1, NULL, '0', '0', '0', 'rounded', 'normal', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_run_job
-- ----------------------------
DROP TABLE IF EXISTS `jf_run_job`;
CREATE TABLE `jf_run_job` (
  `id` bigint(20) NOT NULL,
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `run_node_id` bigint(20) DEFAULT NULL COMMENT '节点ID',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '办理人(用户ID）',
  `role_id` bigint(20) DEFAULT NULL COMMENT '参与者角色ID',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `flow_node_id` bigint(20) NOT NULL COMMENT '节点定义ID',
  `status` varchar(2) DEFAULT '-1' COMMENT '任务状态 -1：未开始 0：办理中 1：结束  2：驳回中 3：跳过 9：被驳回',
  `timeout` int(11) DEFAULT NULL COMMENT '任务时限 0不限制',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `suspension` char(1) DEFAULT '0' COMMENT '是否挂起（0否 1是）',
  `is_skip_rejected` char(1) NOT NULL DEFAULT '0' COMMENT '被驳回后是否可跳过 0 否  1 是',
  `job_type` varchar(2) DEFAULT '-1' COMMENT '任务类型 -1无 0用户 1角色 2岗位 3部门',
  `node_job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `suspension_reason` varchar(512) DEFAULT NULL COMMENT '挂起原因',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `user_key` varchar(64) DEFAULT NULL COMMENT '动态办理人KEY',
  `is_now_run` char(1) NOT NULL DEFAULT '0' COMMENT '被分配后是否立即运行 0否 1是',
  `signature_type` char(1) DEFAULT NULL COMMENT '加签类型：1前加签、2后加签、3并签',
  `signature_id` bigint(20) DEFAULT NULL COMMENT '加签关联前置任务ID',
  `is_read` char(1) DEFAULT NULL COMMENT '是否已阅 0：否 1：是',
  `user_key_val` varchar(255) DEFAULT NULL COMMENT '#anyKey表示表单的字段 ( 默认#form.前缀 ), 例如#userId ( 加前缀#var.anyKey表示从流程条件中取值，#user.anyKey表示从当前用户中取值 )',
  `dist_flow_node_id` bigint(20) DEFAULT NULL COMMENT '待分配办理人节点',
  `val_type` varchar(2) DEFAULT '-2' COMMENT '人员模式：-2普通模式 -1分配模式 0简单模式 1固定模式 2专业模式 3Http模式',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'http请求类型：0GET、1POST、2PUT、3DELETE',
  `job_name` varchar(255) NOT NULL COMMENT '任务名称',
  `from_type` char(1) NOT NULL DEFAULT '0' COMMENT '任务来源类型 0默认任务 1不分离任务 2分离任务',
  `position_size` varchar(255) DEFAULT NULL COMMENT '节点位置大小',
  `belong_type` char(1) DEFAULT '0' COMMENT '归属类型 0普通任务 1抄送任务 2传阅任务',
  `is_config_job` char(1) NOT NULL DEFAULT '0' COMMENT '是否为配置任务：0 否 1 是',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务记录';

-- ----------------------------
-- Records of jf_run_job
-- ----------------------------
BEGIN;
INSERT INTO `jf_run_job` VALUES (1631234697665445890, 1631234697145352194, 1631234697510256642, 'TestZeroCode', '2023-03-02 18:06:13', '2023-03-02 18:07:13', 1, 1, 1, '2023-03-02 18:07:13', 1677686588872000008, '1', 0, 1, '0', '0', '0', 1630965408190459906, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697678028801, 1631234697145352194, 1631234697522839553, 'TestZeroCode', '2023-03-02 18:07:14', '2023-03-02 18:11:55', 1, 1, 1, '2023-03-02 18:07:13', 1677686594395000009, '1', 0, 1, '0', '0', '0', 1630965408257568770, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '干大事的 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697686417409, 1631234697145352194, 1631234697531228161, 'TestZeroCode', NULL, NULL, 1, 1, 1, '2023-03-02 18:07:13', 1677686602089000010, '-1', 0, 1, '0', '0', '0', 1630965408320483330, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '串行 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697699000322, 1631234697145352194, 1631234697552199681, 'TestZeroCode', '2023-03-02 18:11:55', '2023-07-03 16:27:22', 1, 1, 1, '2023-03-02 18:07:13', 1677686614774000011, '1', 0, 1, '0', '0', '0', 1677686667002000014, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务', '2', '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697707388929, 1631234697145352194, 1631234697552199681, 'TestZeroCode', '2023-03-02 18:11:55', NULL, NULL, NULL, 1, '2023-03-02 18:07:13', 1677686614774000011, '0', 0, 1, '0', '0', '-1', 1677686669770000015, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务', '2', '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697724166146, 1631234697145352194, 1631234697564782593, 'TestZeroCode', '2023-03-02 18:11:55', NULL, 1, 1, 1, '2023-03-02 18:07:13', 1677686618687000012, '0', 0, 1, '0', '0', '0', 1630965408421146625, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '并行 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697732554753, 1631234697145352194, 1631234697577365505, 'TestZeroCode', NULL, NULL, 1, 1, 1, '2023-03-02 18:07:13', 1677686627943000013, '-1', 0, 1, '0', '0', '0', 1630965408505032706, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234697745137665, 1631234697145352194, 1631234697589948417, 'TestZeroCode', NULL, NULL, 1, 1, 1, '2023-03-02 18:07:13', 1677686693563000018, '-1', 0, 1, '0', '0', '0', 1630965408580530178, NULL, 1663698819878000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '虚拟', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234945217462274, 1631234944961609729, 1631234945141964801, 'TestKey', '2023-03-02 18:07:12', '2023-03-02 18:08:12', 1, 1, 1, '2023-03-02 18:08:12', 1677683136126000020, '1', 0, 1, '0', '0', '0', 1630953145756839937, NULL, 1663850423578000013, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234945230045186, 1631234944961609729, 1631234945150353410, 'TestKey', '2023-03-02 18:08:13', NULL, 1, 1, 1, '2023-03-02 18:08:12', 1677683152440000021, '0', 0, 1, '0', '0', '0', 1677683161676000022, NULL, 1663850423578000013, 'create_user', '0', NULL, NULL, NULL, NULL, NULL, '-1', NULL, '任务', '2', '{\"position\": {\n                \"x\": 611,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 82\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234945238433793, 1631234944961609729, 1631234945162936322, 'TestKey', NULL, NULL, 1, 1, 1, '2023-03-02 18:08:12', 1677683283642000025, '-1', 0, 1, '0', '0', '0', 1630953145933000706, NULL, 1663850423578000013, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234945246822402, 1631234944961609729, 1631234945171324929, 'TestKey', NULL, NULL, 1, 1, 1, '2023-03-02 18:08:12', 1677683305541000026, '-1', 0, 1, '0', '0', '0', 1630953146029469698, NULL, 1663850423578000013, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '串行 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631234945251016705, 1631234944961609729, 1631234945175519234, 'TestKey', NULL, NULL, 1, 1, 1, '2023-03-02 18:08:12', 1677683307338000027, '-1', 0, 1, '0', '0', '0', 1630953146130132994, NULL, 1663850423578000013, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '串行 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235003824472065, 1631235003606368258, 1631235003757363202, 'FormPermission', '2023-03-02 18:07:26', '2023-03-02 18:08:26', 1, 1, 1, '2023-03-02 18:08:26', 1677688514110000002, '1', 0, 1, '0', '0', '0', 1630971561175707649, NULL, 1668790077997000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235003832860674, 1631235003606368258, 1631235003765751810, 'FormPermission', '2023-03-02 18:08:26', NULL, 1, 1, 1, '2023-03-02 18:08:26', 1677688590244000006, '0', 0, 1, '0', '0', '0', 1630971561221844993, NULL, 1668790077997000002, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '表单权限 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235003845443586, 1631235003606368258, 1631235003778334722, 'FormPermission', NULL, NULL, 1, 1, 1, '2023-03-02 18:08:26', 1677688592089000007, '-1', 0, 1, '0', '0', '0', 1630971561267982338, NULL, 1668790077997000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '自定义权限 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235003849637890, 1631235003606368258, 1631235003782529026, 'FormPermission', '2023-03-02 18:08:26', NULL, 1, 1, 1, '2023-03-02 18:08:26', 1677688593826000008, '0', 0, 1, '0', '0', '0', 1630971561314119682, NULL, 1668790077997000002, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '审批页面权限 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235003858026497, 1631235003606368258, 1631235003790917633, 'FormPermission', NULL, NULL, 1, 1, 1, '2023-03-02 18:08:26', 1677688595646000009, '-1', 0, 1, '0', '0', '0', 1630971561360257026, NULL, 1668790077997000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155675054081, 1631235155431784450, 1631235155578585090, 'AskLeave', '2023-03-02 18:08:03', '2023-03-02 18:09:03', 1, 1, 1, '2023-03-02 18:09:03', 1677733473617000054, '1', 0, 1, '0', '0', '0', 1631163656494673921, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155683442689, 1631235155431784450, 1631235155591168001, 'AskLeave', NULL, NULL, 1, 1, 1, '2023-03-02 18:09:03', 1677733476722000055, '-1', 0, 1, '0', '0', '0', 1631163656540811265, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '修改工单 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155691831297, 1631235155431784450, 1631235155603750913, 'AskLeave', '2023-03-02 18:09:03', NULL, 1, 1, 1, '2023-03-02 18:09:03', 1677733503522000058, '0', 0, 1, '0', '0', '0', 1677733500235000056, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '经理', '2', '{\"position\": {\n                \"x\": 317,\n                \"y\": 355\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 76\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155700219906, 1631235155431784450, 1631235155603750913, 'AskLeave', '2023-03-02 18:09:03', NULL, NULL, NULL, 1, '2023-03-02 18:09:03', 1677733503522000058, '0', 0, 1, '0', '0', '-1', 1677733501738000057, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '副经理', '2', '{\"position\": {\n                \"x\": 473,\n                \"y\": 355\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 100\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155704414210, 1631235155431784450, 1631235155612139522, 'AskLeave', NULL, NULL, 1, 1, 1, '2023-03-02 18:09:03', 1677733629144000065, '-1', 0, 1, '0', '0', '0', 1631163656612114434, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '财务 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155716997122, 1631235155431784450, 1631235155628916738, 'AskLeave', NULL, NULL, 1, 1, 1, '2023-03-02 18:09:03', 1677734581812000077, '-1', 0, 1, '0', '0', '0', 1631163656708583426, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '部门总监 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1631235155725385729, 1631235155431784450, 1631235155633111042, 'AskLeave', NULL, NULL, 1, 1, 1, '2023-03-02 18:09:03', 1677734587498000078, '-1', 0, 1, '0', '0', '0', 1631163656754720769, NULL, 1660711059233000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '人事总监 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1683828100609384449, 1683828100328366082, 1683828100538081282, 'DynamicSignature', '2023-07-25 21:13:38', '2023-07-25 21:14:38', 1, 1, 1, '2023-07-25 21:14:38', 1690289583078000001, '1', 0, 1, '0', '0', '0', 1683825416573591553, NULL, 1690289416119000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1683828100613578754, 1683828100328366082, 1683828100546469889, 'DynamicSignature', '2023-07-25 21:14:38', NULL, NULL, 1, 1, '2023-07-25 21:14:38', 1690289584183000002, '0', 0, 1, '0', '0', '0', 1683825416653283329, NULL, 1690289416119000001, NULL, '0', NULL, NULL, '1', NULL, 1690289586509000004, '-2', NULL, '分配下一节点不确定任务数和办理人员', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1683828100621967362, 1683828100328366082, 1683828100550664193, 'DynamicSignature', NULL, NULL, 1, 1, 1, '2023-07-25 21:14:38', 1690289585185000003, '-1', 0, 1, '0', '0', '0', 1683825416724586497, NULL, 1690289416119000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '两个节点不一定挨着', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1683828100630355969, 1683828100328366082, 1683828100559052802, 'DynamicSignature', NULL, NULL, NULL, 2, 1, '2023-07-25 21:14:38', 1690289586509000004, '-1', 0, 1, '0', '0', '1', 1683825416808472578, NULL, 1690289416119000001, 'dynamic_signature', '0', NULL, NULL, NULL, NULL, NULL, '-1', NULL, '动态加减签节点：动态计算当前节点不确定任务数和办理人员', '0', NULL, '0', '0', 1);
INSERT INTO `jf_run_job` VALUES (1683828100634550273, 1683828100328366082, 1683828100567441409, 'DynamicSignature', NULL, NULL, 1, 1, 1, '2023-07-25 21:14:38', 1690289589195000005, '-1', 0, 1, '0', '0', '0', 1683825416879775746, NULL, 1690289416119000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1683828918137954305, 1683828100328366082, 1683828100559052802, 'DynamicSignature', NULL, NULL, 1, 1, 1, '2023-07-25 21:17:53', 1690289586509000004, '-1', 0, 1, '0', '0', '0', 1683825416808472578, NULL, 1690289416119000001, 'dynamic_signature', '0', NULL, NULL, NULL, NULL, NULL, '-1', NULL, '动态加减签节点：动态计算当前节点不确定任务数和办理人员', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083500548098, 1691395082670075906, 1691395083261472769, 'TestDingDing', '2023-08-15 18:22:07', '2023-08-15 18:23:07', 1, 1, 1, '2023-08-15 18:23:07', 1692022816944000026, '1', 0, 1, '0', '0', '0', 1691389179627593730, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083525713922, 1691395082670075906, 1691395083282444290, 'TestDingDing', NULL, NULL, 1, 1, 1, '2023-08-15 18:23:07', 1692022816948000027, '-1', 0, 1, '0', '0', '0', 1691389179719868418, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083550879746, 1691395082670075906, 1691395083311804418, 'TestDingDing', '2023-08-15 18:23:07', NULL, 1, 1, 1, '2023-08-15 18:23:07', 1692022819404000029, '0', 0, 1, '0', '0', '0', 1692022821130000031, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务11', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083580239874, 1691395082670075906, 1691395083311804418, 'TestDingDing', '2023-08-15 18:23:07', NULL, NULL, 2, 1, '2023-08-15 18:23:07', 1692022819404000029, '0', 0, 1, '0', '0', '1', 1692029577119000011, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务66', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083605405698, 1691395082670075906, 1691395083345358850, 'TestDingDing', NULL, NULL, NULL, 2, 1, '2023-08-15 18:23:07', 1692023728415000023, '-1', 0, 1, '0', '0', '1', 1692023740611000030, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务22', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083622182914, 1691395082670075906, 1691395083362136066, 'TestDingDing', NULL, NULL, NULL, NULL, 1, '2023-08-15 18:23:07', 1692023731352000025, '-1', 0, 1, '0', '0', '-1', 1692023741895000031, NULL, 1692022814348000008, 'create_user', '0', NULL, NULL, NULL, NULL, NULL, '-1', NULL, '任务33', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691395083638960130, 1691395082670075906, 1691395083395690498, 'TestDingDing', NULL, NULL, 1, 1, 1, '2023-08-15 18:23:07', 1692023738815000028, '-1', 0, 1, '0', '0', '0', 1691389180093161474, NULL, 1692022814348000008, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '串行节点', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691435166312198146, 1691435166140231682, 1691435166253477889, 'TestDingGate', '2023-08-15 21:01:24', '2023-08-15 21:02:24', 1, 1, 1, '2023-08-15 21:02:24', 1692103817782000001, '1', 0, 1, '0', '0', '0', 1691434275613995010, NULL, 1692103815657000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691435166320586753, 1691435166140231682, 1691435166261866498, 'TestDingGate', NULL, NULL, 1, 1, 1, '2023-08-15 21:02:24', 1692103817786000002, '-1', 0, 1, '0', '0', '0', 1691434275689492481, NULL, 1692103815657000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691435166324781058, 1691435166140231682, 1691435166266060801, 'TestDingGate', '2023-08-15 21:02:24', NULL, NULL, NULL, 1, '2023-08-15 21:02:24', 1692103853707000012, '0', 0, 1, '0', '0', '-1', 1692103875747000023, NULL, 1692103815657000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务11', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691435166328975362, 1691435166140231682, 1691435166270255105, 'TestDingGate', '2023-08-15 21:02:24', NULL, NULL, 2, 1, '2023-08-15 21:02:24', 1692103856654000014, '0', 0, 1, '0', '0', '1', 1692103877320000024, NULL, 1692103815657000001, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '任务22', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1691435166333169666, 1691435166140231682, 1691435166270255105, 'TestDingGate', '2023-08-15 21:02:24', NULL, 1562990710920388610, 1562990710920388610, 1, '2023-08-15 21:02:24', 1692103856654000014, '0', 0, 1, '0', '0', '0', 1692104437412000028, NULL, 1692103815657000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '任务66', '1', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1700039727675420674, 1700039727486676993, 1700039727616700417, 'HandoverFlow', '2023-09-08 14:52:51', '2023-09-08 14:53:51', 1, 1, 1, '2023-09-08 14:53:51', 1677732851137000033, '1', 0, 1, '0', '0', '0', 1631157370713567233, NULL, 1661171611773000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1700039727683809281, 1700039727486676993, 1700039727629283330, 'HandoverFlow', '2023-09-08 14:53:53', NULL, 1, 1, 1, '2023-09-08 14:53:51', 1677732865656000034, '0', 0, 1, '0', '0', '0', 1677732876691000035, NULL, 1661171611773000001, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '任务', '2', '{\"position\": {\n                \"x\": 556,\n                \"y\": 336\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 84\n            }}', '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1700039727688003586, 1700039727486676993, 1700039727633477633, 'HandoverFlow', NULL, NULL, 1, 1, 1, '2023-09-08 14:53:51', 1677732899572000038, '-1', 0, 1, '0', '0', '0', 1631157370805841922, NULL, 1661171611773000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '虚拟', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1700039727696392193, 1700039727486676993, 1700039727641866242, 'HandoverFlow', NULL, NULL, 1, 1, 1, '2023-09-08 14:53:51', 1677732907023000039, '-1', 0, 1, '0', '0', '0', 1631157370872950785, NULL, 1661171611773000001, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1700039727700586497, 1700039727486676993, 1700039727646060546, 'HandoverFlow', NULL, NULL, NULL, NULL, 1, '2023-09-08 14:53:51', 1677732909827000040, '-1', 0, 1, '0', '0', '-1', 1631157370935865345, NULL, 1661171611773000001, 'receive_user', '0', NULL, NULL, NULL, NULL, NULL, '-1', NULL, '接收人审批 (1)', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1728770563639672834, 1728770563404791810, 1728770563576758274, 'CustomVuePage', '2023-11-26 21:38:56', '2023-11-26 21:39:56', 1, 1, 1, '2023-11-26 21:39:56', 1701005092143000003, '1', 0, 1, NULL, '0', '0', 1728769069985103874, NULL, 1701005091303000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1728770563648061441, 1728770563404791810, 1728770563585146881, 'CustomVuePage', NULL, NULL, NULL, NULL, 1, '2023-11-26 21:39:56', 1701005092149000004, '-1', 0, 1, '0', '0', '-1', 1728769070035435521, NULL, 1701005091303000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1728770563652255746, 1728770563404791810, 1728770563593535490, 'CustomVuePage', '2023-11-26 21:39:56', NULL, 1, 1, 1, '2023-11-26 21:39:56', 1701005109127000006, '0', 0, 1, '0', '0', '0', 1728769070085767170, NULL, 1701005091303000002, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '请查看开始节点', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1728770563660644353, 1728770563404791810, 1728770563601924097, 'CustomVuePage', NULL, NULL, NULL, NULL, 1, '2023-11-26 21:39:56', 1701005445080000008, '-1', 0, 1, '0', '0', '-1', 1728769070131904513, NULL, 1701005091303000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '请查看表单设置', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036577472299009, 1729036577254195202, 1729036577426161665, 'JDShopParSonFlow', '2023-11-27 15:15:59', '2023-11-27 15:16:59', 1, 1, 1, '2023-11-27 15:16:59', 1701008233487000040, '1', 0, 1, NULL, '0', '0', 1728785543856455681, NULL, 1701008232801000039, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036577476493313, 1729036577254195202, 1729036577438744577, 'JDShopParSonFlow', NULL, NULL, NULL, NULL, 1, '2023-11-27 15:16:59', 1701008233489000041, '-1', 0, 1, '0', '0', '-1', 1728785543915175938, NULL, 1701008232801000039, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036577480687617, 1729036577254195202, 1729036577438744578, 'JDShopParSonFlow', '2023-11-27 15:16:59', '2023-11-27 15:17:15', 1, 1, 1, '2023-11-27 15:16:59', 1701008237478000043, '1', 0, 1, '0', '0', '0', 1728785543965507585, NULL, 1701008232801000039, 'shop_suer', '0', NULL, NULL, '1', '#user.userId', NULL, '2', NULL, '添加商品并付款', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036577489076225, 1729036577254195202, 1729036577451327490, 'JDShopParSonFlow', '2023-11-27 15:17:18', '2023-11-27 15:17:41', 1, 1, 1, '2023-11-27 15:16:59', 1701012098212000019, '1', 0, 1, '0', '0', '0', 1728797600450400257, NULL, 1701008232801000039, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '收发货子流程确认', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036765834297345, 1729036763657453570, 1729036765796548610, 'JDShopSonFlow', '2023-11-27 15:16:44', '2023-11-27 15:17:44', 1, 1, 1, '2023-11-27 15:17:44', 1701010386424000058, '1', 0, 1, NULL, '0', '0', 1728790528639959041, NULL, 1701010376317000057, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036765842685953, 1729036763657453570, 1729036765809131521, 'JDShopSonFlow', '2023-11-27 15:17:44', NULL, 1, 1, 1, '2023-11-27 15:17:44', 1701010449439000061, '0', 0, 1, '0', '0', '0', 1728790528740622338, NULL, 1701010376317000057, 'sell_user', '0', NULL, NULL, '1', '#user.userId', NULL, '2', NULL, '发货', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1729036765846880258, 1729036763657453570, 1729036765817520129, 'JDShopSonFlow', NULL, NULL, NULL, NULL, 1, '2023-11-27 15:17:44', 1701010451663000063, '-1', 0, 1, '0', '0', '-1', 1728790528786759681, NULL, 1701010376317000057, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '收货', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1791286380561977345, 1791030389211779073, 1791286380461314049, 'SubFormTest', '2024-05-17 09:55:18', '2024-05-17 09:55:31', 1, 1, 1, '2024-05-17 09:55:28', 1703568882593000001, '1', 0, 1, NULL, '0', '0', 1739522295011074049, NULL, 1703563526494000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '开始', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1791286380578754562, 1791030389211779073, 1791286380478091265, 'SubFormTest', NULL, NULL, NULL, NULL, 1, '2024-05-17 09:55:28', 1703568882606000002, '-1', 0, 1, '0', '0', '-1', 1739522295078182913, NULL, 1703563526494000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '结束', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1791286380587143170, 1791030389211779073, 1791286380486479874, 'SubFormTest', '2024-05-17 09:55:31', NULL, 1, 1, 1, '2024-05-17 09:55:28', 1703569033526000004, '0', 0, 1, '0', '0', '0', 1739522295141097474, NULL, 1703563526494000002, NULL, '0', NULL, NULL, '1', NULL, NULL, '-2', NULL, '串行节点', '0', NULL, '0', '1', 1);
INSERT INTO `jf_run_job` VALUES (1791286380595531777, 1791030389211779073, 1791286380499062785, 'SubFormTest', NULL, NULL, NULL, NULL, 1, '2024-05-17 09:55:28', 1703569048970000006, '-1', 0, 1, '0', '0', '-1', 1739522295220789249, NULL, 1703563526494000002, NULL, '0', NULL, NULL, NULL, NULL, NULL, '-2', NULL, '串行节点', '0', NULL, '0', '1', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_run_node
-- ----------------------------
DROP TABLE IF EXISTS `jf_run_node`;
CREATE TABLE `jf_run_node` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `flow_node_id` bigint(20) NOT NULL COMMENT '节点ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `node_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '节点类型 -1 发起 0 单行、1 并行、2 结束 3 虚拟',
  `timeout` int(11) DEFAULT NULL COMMENT '任务时限 0不限制',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `suspension` char(1) DEFAULT '0' COMMENT '是否挂起（0否 1是）',
  `status` varchar(2) DEFAULT '-1' COMMENT '节点状态 -1：未开始 0：运行中 1：结束  2：驳回中 3：跳过  9：被驳回',
  `reject_type` varchar(1) DEFAULT '0' COMMENT '驳回类型 0 依次返回  1 直接返回',
  `is_continue` char(1) NOT NULL DEFAULT '0' COMMENT '不满足自身条件是否继续流转下一节点 0 否  1 是',
  `is_auto_next` char(1) NOT NULL DEFAULT '1' COMMENT '是否自动流转下一节点  0否 1是',
  `job_btns` varchar(512) DEFAULT NULL COMMENT '审批按钮权限',
  `approve_method` char(1) NOT NULL DEFAULT '1' COMMENT '多人审批方式：1会签、2或签、3依次审批',
  `is_auto_audit` char(1) DEFAULT NULL COMMENT '是否自动审批 0否 1是',
  `is_pass_same` char(1) DEFAULT NULL COMMENT '相同审批人自动通过 0：否 1：是',
  `carbon_copy` varchar(512) DEFAULT NULL COMMENT '抄送用户ID',
  `sub_def_flow_id` bigint(20) DEFAULT NULL COMMENT '节点子流程定义ID',
  `sub_flow_inst_id` bigint(20) DEFAULT NULL COMMENT '节点子流程实例ID',
  `to_run_node_ids` varchar(512) DEFAULT NULL COMMENT '开启的节点ID',
  `from_run_node_ids` varchar(512) DEFAULT NULL COMMENT '来源的节点ID',
  `next_status` char(1) DEFAULT '0' COMMENT '下一步节点操作状态 0：未操作 1：已操作',
  `curr_status` char(1) DEFAULT '0' COMMENT '当前节点操作状态 0：未操作 1：已操作',
  `sub_flow_status` varchar(2) DEFAULT NULL COMMENT '子流程状态',
  `pc_todo_url` varchar(512) DEFAULT NULL COMMENT 'PC待办页面路径（多个逗号隔开）',
  `pc_finish_url` varchar(512) DEFAULT NULL COMMENT 'PC完成页面路径（多个逗号隔开）',
  `node_name` varchar(255) NOT NULL COMMENT '节点名称',
  `sub_run_job_id` bigint(20) DEFAULT NULL COMMENT '开启子流程的任务ID',
  `is_gateway` char(1) DEFAULT '0' COMMENT '是否为网关 0否 1是',
  `description` varchar(255) DEFAULT NULL COMMENT '节点描述',
  `position_size` varchar(255) DEFAULT NULL COMMENT '节点位置大小',
  `node_approve_method` char(1) NOT NULL DEFAULT '1' COMMENT '多节点审批方式：1会签、2或签、3依次审批',
  `node_group_id` bigint(20) DEFAULT NULL COMMENT '多节点组ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='运行节点管理';

-- ----------------------------
-- Records of jf_run_node
-- ----------------------------
BEGIN;
INSERT INTO `jf_run_node` VALUES (1631234697510256642, 1631234697145352194, 1677686588872000008, 'TestZeroCode', '2023-03-02 18:06:13', '2023-03-02 18:07:14', 1, '2023-03-02 18:07:13', 1663698819878000001, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697522839553, 1631234697145352194, 1677686594395000009, 'TestZeroCode', '2023-03-02 18:07:14', '2023-03-02 18:11:55', 1, '2023-03-02 18:07:13', 1663698819878000001, '0', 0, 2, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '干大事的 (1)', NULL, '0', NULL, '{\"position\":{\"x\":601,\"y\":301},\"size\":{\"height\":40,\"width\":122}}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697531228161, 1631234697145352194, 1677686602089000010, 'TestZeroCode', NULL, NULL, 1, '2023-03-02 18:07:13', 1663698819878000001, '0', 0, 5, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行 (1)', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697552199681, 1631234697145352194, 1677686614774000011, 'TestZeroCode', '2023-03-02 18:11:55', NULL, 1, '2023-03-02 18:07:13', 1663698819878000001, '1', 0, 3, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行 (2)', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697564782593, 1631234697145352194, 1677686618687000012, 'TestZeroCode', '2023-03-02 18:11:55', NULL, 1, '2023-03-02 18:07:13', 1663698819878000001, '1', 0, 4, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行 (1)', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697577365505, 1631234697145352194, 1677686627943000013, 'TestZeroCode', NULL, NULL, 1, '2023-03-02 18:07:13', 1663698819878000001, '2', 0, 6, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234697589948417, 1631234697145352194, 1677686693563000018, 'TestZeroCode', NULL, NULL, 1, '2023-03-02 18:07:13', 1663698819878000001, '3', 0, -1, '0', '-1', '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '虚拟', NULL, '0', NULL, '{\"position\": {\n                \"x\": 782,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 83\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234945141964801, 1631234944961609729, 1677683136126000020, 'TestKey', '2023-03-02 18:07:12', '2023-03-02 18:08:13', 1, '2023-03-02 18:08:12', 1663850423578000013, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 398,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234945150353410, 1631234944961609729, 1677683152440000021, 'TestKey', '2023-03-02 18:08:13', NULL, 1, '2023-03-02 18:08:12', 1663850423578000013, '0', 0, 1, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '2,1,4', '1,2,4', '控制节点大小 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 576,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 152\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234945162936322, 1631234944961609729, 1677683283642000025, 'TestKey', NULL, NULL, 1, '2023-03-02 18:08:12', 1663850423578000013, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 1119,\n                \"y\": 171\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 80\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234945171324929, 1631234944961609729, 1677683305541000026, 'TestKey', NULL, NULL, 1, '2023-03-02 18:08:12', 1663850423578000013, '0', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 871,\n                \"y\": 74\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 105\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631234945175519234, 1631234944961609729, 1677683307338000027, 'TestKey', NULL, NULL, 1, '2023-03-02 18:08:12', 1663850423578000013, '0', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 870,\n                \"y\": 268\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 99\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235003757363202, 1631235003606368258, 1677688514110000002, 'FormPermission', '2023-03-02 18:07:26', '2023-03-02 18:08:26', 1, '2023-03-02 18:08:26', 1668790077997000002, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 419,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 84\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235003765751810, 1631235003606368258, 1677688590244000006, 'FormPermission', '2023-03-02 18:08:26', NULL, 1, '2023-03-02 18:08:26', 1668790077997000002, '1', 0, 1, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '3,2,1', '1,2,4', '表单权限 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 645,\n                \"y\": 150\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 143\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235003778334722, 1631235003606368258, 1677688592089000007, 'FormPermission', NULL, NULL, 1, '2023-03-02 18:08:26', 1668790077997000002, '1', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '自定义权限 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 893,\n                \"y\": 149\n            },\n            \"size\": {\n                \"height\": 42,\n                \"width\": 167\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235003782529026, 1631235003606368258, 1677688593826000008, 'FormPermission', '2023-03-02 18:08:26', NULL, 1, '2023-03-02 18:08:26', 1668790077997000002, '1', 0, 1, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '17,1,2', '1,2,4', '审批页面权限 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 790,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 155\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235003790917633, 1631235003606368258, 1677688595646000009, 'FormPermission', NULL, NULL, 1, '2023-03-02 18:08:26', 1668790077997000002, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 1195,\n                \"y\": 273\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 82\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155578585090, 1631235155431784450, 1677733473617000054, 'AskLeave', '2023-03-02 18:08:03', '2023-03-02 18:09:03', 1, '2023-03-02 18:09:03', 1660711059233000001, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 212,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 38,\n                \"width\": 78\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155591168001, 1631235155431784450, 1677733476722000055, 'AskLeave', NULL, NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '3', 0, -1, '0', '-1', '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '修改工单 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 381,\n                \"y\": 101\n            },\n            \"size\": {\n                \"height\": 38,\n                \"width\": 122\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155603750913, 1631235155431784450, 1677733503522000058, 'AskLeave', '2023-03-02 18:09:03', NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '0', 0, 1, '0', '0', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,12', '1,2', '部门经理 (2)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 384,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 37,\n                \"width\": 121\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155612139522, 1631235155431784450, 1677733629144000065, 'AskLeave', NULL, NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '0', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,13', '1,2', '财务 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 976,\n                \"y\": 219\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 103\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155620528129, 1631235155431784450, 1677734357077000072, 'AskLeave', NULL, NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '自动结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 1176,\n                \"y\": 222\n            },\n            \"size\": {\n                \"height\": 35,\n                \"width\": 84\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155628916738, 1631235155431784450, 1677734581812000077, 'AskLeave', NULL, NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '1', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,12', '1,2', '部门总监 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 701,\n                \"y\": 119\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 128\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1631235155633111042, 1631235155431784450, 1677734587498000078, 'AskLeave', NULL, NULL, 1, '2023-03-02 18:09:03', 1660711059233000001, '1', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,13', '1,2', '人事总监 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 700,\n                \"y\": 313\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 128\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1683828100538081282, 1683828100328366082, 1690289583078000001, 'DynamicSignature', '2023-07-25 21:13:38', '2023-07-25 21:14:38', 1, '2023-07-25 21:14:38', 1690289416119000001, '-1', 0, 1, '0', '1', '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1683828100546469889, 1683828100328366082, 1690289584183000002, 'DynamicSignature', '2023-07-25 21:14:38', NULL, 1, '2023-07-25 21:14:38', 1690289416119000001, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4,7', '2,1,4,8', '分配下一节点不确定任务数和办理人员', NULL, '0', NULL, '{\"position\": {\n                \"x\": 577.5,\n                \"y\": 299\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 298\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1683828100550664193, 1683828100328366082, 1690289585185000003, 'DynamicSignature', NULL, NULL, 1, '2023-07-25 21:14:38', 1690289416119000001, '0', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '两个节点不一定挨着', NULL, '0', NULL, '{\"position\": {\n                \"x\": 569.5,\n                \"y\": 438\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 176\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1683828100559052802, 1683828100328366082, 1690289586509000004, 'DynamicSignature', NULL, NULL, 1, '2023-07-25 21:14:38', 1690289416119000001, '1', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '动态加减签节点：动态计算当前节点不确定任务数和办理人员', NULL, '0', NULL, '{\"position\": {\n                \"x\": 510,\n                \"y\": 579\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 433\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1683828100567441409, 1683828100328366082, 1690289589195000005, 'DynamicSignature', NULL, NULL, 1, '2023-07-25 21:14:38', 1690289416119000001, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083261472769, 1691395082670075906, 1692022816944000026, 'TestDingDing', '2023-08-15 18:22:07', '2023-08-15 18:23:07', 1, '2023-08-15 18:23:07', 1692022814348000008, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083282444290, 1691395082670075906, 1692022816948000027, 'TestDingDing', NULL, NULL, 1, '2023-08-15 18:23:07', 1692022814348000008, '2', 0, 1, '0', '-1', '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083311804418, 1691395082670075906, 1692022819404000029, 'TestDingDing', '2023-08-15 18:23:07', NULL, 1, '2023-08-15 18:23:07', 1692022814348000008, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行节点', NULL, '0', '你想描述点啥？请输入你的描述', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083345358850, 1691395082670075906, 1692023728415000023, 'TestDingDing', NULL, NULL, 1, '2023-08-15 18:23:07', 1692022814348000008, '1', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行节点', NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083362136066, 1691395082670075906, 1692023731352000025, 'TestDingDing', NULL, NULL, 1, '2023-08-15 18:23:07', 1692022814348000008, '1', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行节点', NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691395083395690498, 1691395082670075906, 1692023738815000028, 'TestDingDing', NULL, NULL, 1, '2023-08-15 18:23:07', 1692022814348000008, '0', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行节点', NULL, '0', '用于流程中同时只有一个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166253477889, 1691435166140231682, 1692103817782000001, 'TestDingGate', '2023-08-15 21:01:24', '2023-08-15 21:02:24', 1, '2023-08-15 21:02:24', 1692103815657000001, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166261866498, 1691435166140231682, 1692103817786000002, 'TestDingGate', NULL, NULL, 1, '2023-08-15 21:02:24', 1692103815657000001, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166266060801, 1691435166140231682, 1692103853707000012, 'TestDingGate', '2023-08-15 21:02:24', NULL, 1, '2023-08-15 21:02:24', 1692103815657000001, '1', 0, 1, '0', '0', '0', '0', '1', NULL, '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行节点', NULL, '0', '用于流程中同时可有多个分支执行的场景', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166270255105, 1691435166140231682, 1692103856654000014, 'TestDingGate', '2023-08-15 21:02:24', NULL, 1, '2023-08-15 21:02:24', 1692103815657000001, '1', 0, 1, '0', '0', '0', '0', '1', NULL, '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行节点', NULL, '0', '你想描述点啥？请输入你的描述', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166278643713, 1691435166140231682, 1692103865631000019, 'TestDingGate', '2023-08-15 21:02:24', '2023-08-15 21:02:24', 1, '2023-08-15 21:02:24', 1692103815657000001, '1', NULL, 1, '0', '1', '0', '0', '1', NULL, '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行网关', NULL, '1', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1691435166282838018, 1691435166140231682, 1692103869912000021, 'TestDingGate', NULL, NULL, 1, '2023-08-15 21:02:24', 1692103815657000001, '1', NULL, 1, '0', '-1', '0', '0', '1', NULL, '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '并行网关', NULL, '1', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1700039727616700417, 1700039727486676993, 1677732851137000033, 'HandoverFlow', '2023-09-08 14:52:51', '2023-09-08 14:53:53', 1, '2023-09-08 14:53:51', 1661171611773000001, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 338,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1700039727629283330, 1700039727486676993, 1677732865656000034, 'HandoverFlow', '2023-09-08 14:53:53', NULL, 1, '2023-09-08 14:53:51', 1661171611773000001, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,11', '1,2', '分配接收人', NULL, '0', NULL, '{\"position\": {\n                \"x\": 536,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 124\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1700039727633477633, 1700039727486676993, 1677732899572000038, 'HandoverFlow', NULL, NULL, 1, '2023-09-08 14:53:51', 1661171611773000001, '3', 0, -1, '0', '-1', '1', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '虚拟', NULL, '0', NULL, '{\"position\": {\n                \"x\": 541,\n                \"y\": 92\n            },\n            \"size\": {\n                \"height\": 42,\n                \"width\": 92\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1700039727641866242, 1700039727486676993, 1677732907023000039, 'HandoverFlow', NULL, NULL, 1, '2023-09-08 14:53:51', 1661171611773000001, '2', 0, 1, '0', '-1', '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2', '1,2', '结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 1034,\n                \"y\": 199\n            },\n            \"size\": {\n                \"height\": 39,\n                \"width\": 81\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1700039727646060546, 1700039727486676993, 1677732909827000040, 'HandoverFlow', NULL, NULL, 1, '2023-09-08 14:53:51', 1661171611773000001, '0', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '9,1,2', '1,2', '接收人审批 (1)', NULL, '0', NULL, '{\"position\": {\n                \"x\": 762,\n                \"y\": 92\n            },\n            \"size\": {\n                \"height\": 41,\n                \"width\": 143\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1728770563576758274, 1728770563404791810, 1701005092143000003, 'CustomVuePage', '2023-11-26 21:38:56', '2023-11-26 21:39:56', 1, '2023-11-26 21:39:56', 1701005091303000002, '-1', 0, 1, '0', '1', '0', '0', '1', '', '1', NULL, NULL, '', NULL, NULL, NULL, NULL, '0', '0', NULL, '18,1,2', '19,1,2', '开始', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1728770563585146881, 1728770563404791810, 1701005092149000004, 'CustomVuePage', NULL, NULL, 1, '2023-11-26 21:39:56', 1701005091303000002, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '18,1,2', '19,1,2', '结束', NULL, '0', NULL, NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1728770563593535490, 1728770563404791810, 1701005109127000006, 'CustomVuePage', '2023-11-26 21:39:56', NULL, 1, '2023-11-26 21:39:56', 1701005091303000002, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', '0', '', NULL, NULL, NULL, NULL, '0', '0', NULL, '18,1,2', '19,1,2', '请查看开始节点', NULL, '0', '开始节点【PC待办页面】配置《自定义主表单Vue页面》', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1728770563601924097, 1728770563404791810, 1701005445080000008, 'CustomVuePage', NULL, NULL, 1, '2023-11-26 21:39:56', 1701005091303000002, '0', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', '0', '', NULL, NULL, NULL, NULL, '0', '0', NULL, '18,1,2', '19,1,2', '请查看表单设置', NULL, '0', '第三步表单设置中输入了《组件路径》', NULL, '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036577426161665, 1729036577254195202, 1701008233487000040, 'JDShopParSonFlow', '2023-11-27 15:15:59', '2023-11-27 15:16:59', 1, '2023-11-27 15:16:59', 1701008232801000039, '-1', 0, 1, '0', '1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036577438744577, 1729036577254195202, 1701008233489000041, 'JDShopParSonFlow', NULL, NULL, 1, '2023-11-27 15:16:59', 1701008232801000039, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 790\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036577438744578, 1729036577254195202, 1701008237478000043, 'JDShopParSonFlow', '2023-11-27 15:16:59', '2023-11-27 15:17:15', 1, '2023-11-27 15:16:59', 1701008232801000039, '0', 0, 1, '0', '1', '0', '0', '1', '', '1', '0', '0', '', NULL, NULL, NULL, NULL, '0', '0', NULL, '3,1,2', '1,2,4', '添加商品并付款', NULL, '0', '支付金额大于0则会发起《收发货子流程》', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036577447133185, 1729036577254195202, 1701008249074000045, 'JDShopParSonFlow', '2023-11-27 15:17:15', '2023-11-27 15:17:15', 1, '2023-11-27 15:16:59', 1701008232801000039, '0', NULL, 1, '0', '1', '0', '0', '1', NULL, '1', '1', NULL, '', NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '支付校验', NULL, '1', NULL, '{\"position\": {\n                \"x\": 555,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 110\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036577451327490, 1729036577254195202, 1701012098212000019, 'JDShopParSonFlow', '2023-11-27 15:17:18', '2023-11-27 15:17:45', 1, '2023-11-27 15:16:59', 1701008232801000039, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', '0', '', 1701010376317000057, 1729036763657453570, NULL, NULL, '0', '0', '0', '3,1,2', '1,2,4', '收发货子流程确认', 1729036577489076225, '0', '当确认后会发起《京东购物子流程JDShopSonFlow》', '{\"position\": {\n                \"x\": 510,\n                \"y\": 615\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036765796548610, 1729036763657453570, 1701010386424000058, 'JDShopSonFlow', '2023-11-27 15:16:44', '2023-11-27 15:17:44', 1, '2023-11-27 15:17:44', 1701010376317000057, '-1', 0, 1, '0', '1', '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036765800742913, 1729036763657453570, 1701010386427000059, 'JDShopSonFlow', NULL, NULL, 1, '2023-11-27 15:17:44', 1701010376317000057, '2', 0, 1, '0', '-1', '0', '0', '1', '', '1', '1', NULL, '', NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '自动结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 650\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036765809131521, 1729036763657453570, 1701010449439000061, 'JDShopSonFlow', '2023-11-27 15:17:44', NULL, 1, '2023-11-27 15:17:44', 1701010376317000057, '0', 0, 1, '0', '0', '0', '1', '1', '', '1', '0', '0', '', NULL, NULL, NULL, NULL, '0', '0', NULL, '3,1,2', '1,2,4', '发货', NULL, '0', '卖家发货，等待买家收货确认', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1729036765817520129, 1729036763657453570, 1701010451663000063, 'JDShopSonFlow', NULL, NULL, 1, '2023-11-27 15:17:44', 1701010376317000057, '0', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', '0', '', NULL, NULL, NULL, NULL, '0', '0', NULL, '3,1,2', '1,2,4', '收货', NULL, '0', '买家收货，若未收到货则重新发货', '{\"position\": {\n                \"x\": 510,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1791286380461314049, 1791030389211779073, 1703568882593000001, 'SubFormTest', '2024-05-17 09:55:18', '2024-05-17 09:55:31', 1, '2024-05-17 09:55:28', 1703563526494000002, '-1', 0, 1, '0', '1', '0', '0', '1', '', '1', NULL, NULL, NULL, NULL, NULL, '1791286380486479874', NULL, '0', '1', NULL, '1,2,4', '1,2,4', '开始', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 160\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1791286380478091265, 1791030389211779073, 1703568882606000002, 'SubFormTest', NULL, NULL, 1, '2024-05-17 09:55:28', 1703563526494000002, '2', 0, 1, '0', '-1', '0', '0', '1', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '结束', NULL, '0', NULL, '{\"position\": {\n                \"x\": 565,\n                \"y\": 650\n            },\n            \"size\": {\n                \"height\": 40,\n                \"width\": 90\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1791286380486479874, 1791030389211779073, 1703569033526000004, 'SubFormTest', '2024-05-17 09:55:31', NULL, 1, '2024-05-17 09:55:28', 1703563526494000002, '0', 0, 1, '0', '0', '0', '0', '1', '', '1', '0', '0', '', NULL, NULL, NULL, '1791286380461314049', '0', '0', NULL, '3,1,2', '1,2,4', '串行节点', NULL, '0', '用于流程中同时只有一个分支执行的场景', '{\"position\": {\n                \"x\": 510,\n                \"y\": 300\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
INSERT INTO `jf_run_node` VALUES (1791286380499062785, 1791030389211779073, 1703569048970000006, 'SubFormTest', NULL, NULL, 1, '2024-05-17 09:55:28', 1703563526494000002, '0', 0, 1, '0', '-1', '0', '0', '1', '', '1', '0', '0', NULL, NULL, NULL, NULL, NULL, '0', '0', NULL, '1,2,4', '1,2,4', '串行节点', NULL, '0', '用于流程中同时只有一个分支执行的场景', '{\"position\": {\n                \"x\": 510,\n                \"y\": 475\n            },\n            \"size\": {\n                \"height\": 75,\n                \"width\": 200\n            }}', '1', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_run_reject
-- ----------------------------
DROP TABLE IF EXISTS `jf_run_reject`;
CREATE TABLE `jf_run_reject` (
  `id` bigint(20) NOT NULL,
  `def_flow_id` bigint(20) NOT NULL COMMENT '流程定义ID',
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程ID',
  `from_run_node_id` bigint(20) NOT NULL COMMENT '来源节点ID',
  `from_run_job_id` bigint(20) NOT NULL COMMENT '来源任务ID',
  `flow_key` varchar(64) NOT NULL COMMENT '工单流程KEY',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` char(1) NOT NULL COMMENT '节点状态 0：驳回中 1：结束',
  `user_id` bigint(20) NOT NULL COMMENT '驳回人(用户ID）',
  `to_run_node_id` bigint(20) NOT NULL COMMENT '到达节点ID',
  `handle_user_id` bigint(20) NOT NULL COMMENT '被驳回人(用户ID）',
  `from_flow_node_id` bigint(20) DEFAULT NULL COMMENT '来源流程节点ID',
  `to_flow_node_id` bigint(20) DEFAULT NULL COMMENT '到达流程节点ID',
  `to_run_job_id` bigint(20) NOT NULL COMMENT '达到任务ID',
  `remark` varchar(512) DEFAULT NULL COMMENT '驳回意见',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='驳回任务记录';

-- ----------------------------
-- Records of jf_run_reject
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for jf_tabs_option
-- ----------------------------
DROP TABLE IF EXISTS `jf_tabs_option`;
CREATE TABLE `jf_tabs_option` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `icon` varchar(64) DEFAULT NULL COMMENT '图标',
  `label` varchar(32) DEFAULT NULL COMMENT '页面标题',
  `path` varchar(128) DEFAULT NULL COMMENT '页面路径（动态组件）',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY或其他页面KEY',
  `sort` int(11) DEFAULT '1' COMMENT '排序值',
  `is_active` char(1) DEFAULT '0' COMMENT '默认展示页面（0 否 1是）',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_view` char(1) DEFAULT '0' COMMENT '是否为查看页面 0否 1是',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `form_info` longtext COMMENT '表单信息',
  `group_name` varchar(255) NOT NULL COMMENT '分组名称',
  `version` int(11) NOT NULL DEFAULT '1' COMMENT '版本',
  `status` varchar(2) NOT NULL DEFAULT '-1' COMMENT '状态 -1 暂存 0 作废 1 发布',
  `style` varchar(64) DEFAULT NULL COMMENT '页面样式',
  `is_auto_audit` char(1) DEFAULT '0' COMMENT '提交时自动审批 0否 1是',
  `type` char(1) DEFAULT '0' COMMENT '页面类型：0审批页面设计 1一键快捷设计 2自定义Vue页面',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='流程中tabs页面';

-- ----------------------------
-- Records of jf_tabs_option
-- ----------------------------
BEGIN;
INSERT INTO `jf_tabs_option` VALUES (1, 'iconfont icon-bolangneng', '查看审批过程', '/jsonflow/comment/flow', 'comment', 200, '1', 1, '2020-02-24 22:49:58', 1, '2023-05-17 22:29:52', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (2, 'iconfont icon-diqiu1', '查看流程实例图', '/jsonflow/flow-design/view', 'flow-info', 300, '1', 1, '2020-02-24 22:49:58', 1, '2023-05-17 22:30:27', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (3, 'iconfont icon-zhongduancanshuchaxun', '修改办公申请工单', '/order/run-application/flow', 'RunApplication', 90, '1', 1, '2020-08-27 11:14:24', 1, '2023-12-26 15:28:02', '0', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '1', 1);
INSERT INTO `jf_tabs_option` VALUES (4, 'iconfont icon-xianshimima', '查看办公申请工单', '/order/run-application/flow', 'RunApplication', 100, '1', 1, '2020-08-27 12:02:33', 1, '2023-05-17 22:27:40', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '1', 1);
INSERT INTO `jf_tabs_option` VALUES (7, 'iconfont icon-icon-', '分配参与者', '/jsonflow/dist-person/flow', 'dist-person', 99, '1', 1, '2020-08-28 12:45:25', 1, '2023-05-17 22:29:09', '0', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (8, 'iconfont icon-icon-', '查看分配参与者', '/jsonflow/dist-person/flow', 'dist-person', 99, '1', 1, '2020-08-28 14:52:58', 1, '2023-05-17 22:28:20', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (9, 'iconfont icon-icon-', '修改任务交接工单', '/order/handover-node-record/flow', 'HandoverFlow', 100, '1', 1, '2020-09-11 23:58:12', 1, '2023-05-17 22:28:59', '0', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (10, 'iconfont icon-icon-', '查看任务交接工单', '/order/handover-node-record/flow', 'HandoverFlow', 100, '1', 1, '2020-09-11 23:59:12', 1, '2023-05-17 22:29:03', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (11, 'iconfont icon-icon-', '工作/任务交接分配', '/order/handover-flow/distribution', 'HandoverFlow', 90, '1', 1, '2020-09-12 00:01:23', 1, '2023-05-17 22:28:53', '0', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (12, 'iconfont icon-zhongduancanshuchaxun', '修改请假工单', '/order/ask-leave/flow', 'AskLeave', 90, '1', 1, '2020-09-13 11:14:24', 1, '2023-05-17 22:27:33', '0', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (13, 'iconfont icon-xianshimima', '查看请假工单', '/order/ask-leave/flow', 'AskLeave', 100, '1', 1, '2020-09-14 12:02:33', 1, '2023-05-17 22:27:53', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (15, 'iconfont icon-bolangneng', '审批过程时间线', '/jsonflow/comment/timeline', 'comment', 200, '1', 1, '2020-09-15 22:49:58', 1, '2023-05-17 22:29:58', '1', '0', NULL, '审批页面分组名称', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (17, 'iconfont icon-zhongduancanshuchaxun', '审批页面字段权限', '/jsonflow/tabs-option/flow', 'FormPermission', 1, '1', 1, '2022-11-19 01:31:34', 1, '2024-01-03 22:47:34', '0', '0', '{\"formConfig\": {\"size\": \"\", \"cssCode\": \"\", \"refName\": \"vForm\", \"functions\": \"\", \"modelName\": \"formData\", \"rulesName\": \"rules\", \"labelAlign\": \"label-left-align\", \"labelWidth\": 80, \"layoutType\": \"PC\", \"customClass\": \"\", \"dataSources\": [], \"jsonVersion\": 3, \"labelPosition\": \"left\", \"onFormCreated\": \"\", \"onFormMounted\": \"\", \"onFormDataChange\": \"\"}, \"widgetList\": [{\"id\": \"input60479\", \"key\": 65354, \"icon\": \"text-field\", \"type\": \"input\", \"options\": {\"name\": \"userName\", \"size\": \"\", \"type\": \"text\", \"label\": \"用户名\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"clearable\": true, \"maxLength\": null, \"minLength\": null, \"onCreated\": \"\", \"onMounted\": \"\", \"buttonIcon\": \"custom-search\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"prefixIcon\": \"\", \"suffixIcon\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"appendButton\": false, \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"showPassword\": false, \"showWordLimit\": false, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\", \"onAppendButtonClick\": \"\", \"appendButtonDisabled\": false}, \"formItemFlag\": true}, {\"id\": \"date101110\", \"key\": 50008, \"icon\": \"date-field\", \"type\": \"date\", \"options\": {\"name\": \"date101110\", \"size\": \"\", \"type\": \"date\", \"label\": \"date\", \"format\": \"YYYY-MM-DD\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"disabled\": false, \"editable\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"clearable\": true, \"labelWrap\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"placeholder\": \"\", \"valueFormat\": \"YYYY-MM-DD\", \"defaultValue\": null, \"labelTooltip\": null, \"requiredHint\": \"\", \"autoFullWidth\": true, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"daterange68122\", \"key\": 107998, \"icon\": \"date-range-field\", \"type\": \"date-range\", \"options\": {\"name\": \"daterange68122\", \"size\": \"\", \"type\": \"daterange\", \"label\": \"date-range\", \"format\": \"YYYY-MM-DD\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"disabled\": false, \"editable\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"clearable\": true, \"labelWrap\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"valueFormat\": \"YYYY-MM-DD\", \"defaultValue\": null, \"labelTooltip\": null, \"requiredHint\": \"\", \"autoFullWidth\": true, \"endPlaceholder\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"startPlaceholder\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"input94727\", \"key\": 75125, \"icon\": \"text-field\", \"type\": \"input\", \"alias\": \"\", \"options\": {\"name\": \"input94727\", \"size\": \"\", \"type\": \"text\", \"label\": \"input\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"onInput\": \"\", \"disabled\": false, \"onChange\": \"\", \"readonly\": false, \"required\": false, \"clearable\": true, \"labelWrap\": false, \"maxLength\": null, \"minLength\": null, \"onCreated\": \"\", \"onMounted\": \"\", \"buttonIcon\": \"custom-search\", \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"prefixIcon\": \"\", \"suffixIcon\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"placeholder\": \"\", \"appendButton\": false, \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"showPassword\": false, \"showWordLimit\": false, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\", \"onAppendButtonClick\": \"\", \"appendButtonDisabled\": false}, \"formItemFlag\": true}, {\"id\": \"select35193\", \"key\": 112100, \"icon\": \"select-field\", \"type\": \"select\", \"options\": {\"name\": \"select35193\", \"size\": \"\", \"label\": \"select\", \"dsName\": \"\", \"hidden\": false, \"onBlur\": \"\", \"remote\": false, \"onFocus\": \"\", \"disabled\": false, \"labelKey\": \"label\", \"multiple\": false, \"onChange\": \"\", \"required\": false, \"valueKey\": \"value\", \"clearable\": true, \"dsEnabled\": false, \"labelWrap\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"filterable\": false, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"allowCreate\": false, \"columnWidth\": \"200px\", \"customClass\": \"\", \"dataSetName\": \"\", \"labelHidden\": false, \"optionItems\": [{\"label\": \"select 1\", \"value\": 1}, {\"label\": \"select 2\", \"value\": 2}, {\"label\": \"select 3\", \"value\": 3}], \"placeholder\": \"\", \"defaultValue\": \"\", \"labelTooltip\": null, \"requiredHint\": \"\", \"multipleLimit\": 0, \"onRemoteQuery\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"automaticDropdown\": false, \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"rate65425\", \"key\": 30129, \"icon\": \"rate-field\", \"type\": \"rate\", \"options\": {\"max\": 5, \"name\": \"rate65425\", \"label\": \"rate\", \"hidden\": false, \"disabled\": false, \"onChange\": \"\", \"required\": false, \"showText\": false, \"allowHalf\": false, \"labelWrap\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"showScore\": false, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": [], \"labelHidden\": false, \"defaultValue\": null, \"labelTooltip\": null, \"lowThreshold\": 2, \"requiredHint\": \"\", \"highThreshold\": 4, \"labelIconClass\": null, \"validationHint\": \"\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}, {\"id\": \"number86249\", \"key\": 83150, \"icon\": \"number-field\", \"type\": \"number\", \"options\": {\"max\": 100000000000, \"min\": -100000000000, \"name\": \"number86249\", \"size\": \"\", \"step\": 1, \"label\": \"number\", \"hidden\": false, \"onBlur\": \"\", \"onFocus\": \"\", \"disabled\": false, \"onChange\": \"\", \"required\": false, \"labelWrap\": false, \"onCreated\": \"\", \"onMounted\": \"\", \"precision\": 0, \"labelAlign\": \"\", \"labelWidth\": null, \"onValidate\": \"\", \"validation\": \"\", \"columnWidth\": \"200px\", \"customClass\": \"\", \"labelHidden\": false, \"placeholder\": \"\", \"defaultValue\": 0, \"labelTooltip\": null, \"requiredHint\": \"\", \"labelIconClass\": null, \"validationHint\": \"\", \"controlsPosition\": \"right\", \"labelIconPosition\": \"rear\"}, \"formItemFlag\": true}]}', '表单/审批页面字段权限', 1, '1', '577px', '1', '0', 1);
INSERT INTO `jf_tabs_option` VALUES (18, 'iconfont icon-zhongduancanshuchaxun', '修改自定义主表单Vue页面', '/order/custom-form/flow', 'custom-form', 1, '1', 1, '2022-12-02 11:02:33', 1, '2024-01-03 22:32:44', '0', '0', NULL, '自定义主表单Vue页面', 1, '1', NULL, '0', '2', 1);
INSERT INTO `jf_tabs_option` VALUES (19, 'iconfont icon-zhongduancanshuchaxun', '查看自定义主表单Vue页面', '/order/custom-form/flow', 'custom-form', 1, '1', 1, '2022-12-02 11:02:33', 1, '2023-11-26 21:43:13', '1', '0', NULL, '自定义主表单Vue页面', 1, '1', NULL, '0', '2', 1);
COMMIT;

-- ----------------------------
-- Table structure for jf_ws_notice
-- ----------------------------
DROP TABLE IF EXISTS `jf_ws_notice`;
CREATE TABLE `jf_ws_notice` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `flow_key` varchar(255) NOT NULL COMMENT '业务KEY',
  `type` char(1) DEFAULT NULL COMMENT '消息类型 0-个人消息 1-群消息',
  `data` varchar(1024) DEFAULT NULL COMMENT '消息数据',
  `job_type` char(1) DEFAULT NULL COMMENT '任务类型（0-个人任务 1-组任务）',
  `status` varchar(2) DEFAULT NULL COMMENT '通知状态 -1发起 0流程中 1结束',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '办理人(用户ID）',
  `flow_inst_id` bigint(20) NOT NULL COMMENT '流程实例ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='消息通知管理';

-- ----------------------------
-- Records of jf_ws_notice
-- ----------------------------
BEGIN;
INSERT INTO `jf_ws_notice` VALUES (1631234698315563009, 'TestZeroCode', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试在线一键设计与发起,审批节点: 干大事的 (1)', '0', '0', 1, '2023-03-02 18:07:14', 1, 1631234697145352194, 1);
INSERT INTO `jf_ws_notice` VALUES (1631234945490092034, 'TestKey', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试自动建表存储,审批节点: 任务', '0', '0', 1, '2023-03-02 18:08:13', 1, 1631234944961609729, 1);
INSERT INTO `jf_ws_notice` VALUES (1631235004092907522, 'FormPermission', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 表单/审批页面字段权限,审批节点: 表单权限 (1),审批页面权限 (1)', '0', '0', 1, '2023-03-02 18:08:27', 1, 1631235003606368258, 1);
INSERT INTO `jf_ws_notice` VALUES (1631235155964461057, 'AskLeave', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 请假工单,审批节点: 经理,副经理', '0', '0', 1, '2023-03-02 18:09:03', 1, 1631235155431784450, 1);
INSERT INTO `jf_ws_notice` VALUES (1631235877154062337, 'TestZeroCode', '0', '(审批)您有新的待办任务,工单类型: 测试在线一键设计与发起,审批节点: 并行 (1),任务,任务', '0', '0', 1, '2023-03-02 18:11:55', 1, 1631234697145352194, 1);
INSERT INTO `jf_ws_notice` VALUES (1683828101116895234, 'DynamicSignature', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 动态加减签-复杂场景,审批节点: 分配下一节点不确定任务数和办理人员', '0', '0', 1, '2023-07-25 21:14:38', 1, 1683828100328366082, 1);
INSERT INTO `jf_ws_notice` VALUES (1691395084817559554, 'TestDingDing', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉UI模式,审批节点: 任务11', '0', '0', 1, '2023-08-15 18:23:07', 1, 1691395082670075906, 1);
INSERT INTO `jf_ws_notice` VALUES (1691395084888862722, 'TestDingDing', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉UI模式,审批节点: 任务66', '0', '0', 1, '2023-08-15 18:23:07', 1, 1691395082670075906, 1);
INSERT INTO `jf_ws_notice` VALUES (1691395084914028546, 'TestDingDing', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉UI模式,审批节点: 任务66', '0', '0', 1544884308251308034, '2023-08-15 18:23:07', 1544884308251308034, 1691395082670075906, 1);
INSERT INTO `jf_ws_notice` VALUES (1691395084930805761, 'TestDingDing', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉UI模式,审批节点: 任务66', '0', '0', 1562990710920388610, '2023-08-15 18:23:07', 1562990710920388610, 1691395082670075906, 1);
INSERT INTO `jf_ws_notice` VALUES (1691435166656131074, 'TestDingGate', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉网关模式,审批节点: 任务11', '0', '0', 1, '2023-08-15 21:02:24', 1, 1691435166140231682, 1);
INSERT INTO `jf_ws_notice` VALUES (1691435166677102594, 'TestDingGate', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉网关模式,审批节点: 任务66', '0', '0', 1562990710920388610, '2023-08-15 21:02:24', 1562990710920388610, 1691435166140231682, 1);
INSERT INTO `jf_ws_notice` VALUES (1691435166698074113, 'TestDingGate', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉网关模式,审批节点: 任务22', '0', '0', 1, '2023-08-15 21:02:24', 1, 1691435166140231682, 1);
INSERT INTO `jf_ws_notice` VALUES (1691435166706462721, 'TestDingGate', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉网关模式,审批节点: 任务22', '0', '0', 1544884308251308034, '2023-08-15 21:02:24', 1544884308251308034, 1691435166140231682, 1);
INSERT INTO `jf_ws_notice` VALUES (1691435166714851330, 'TestDingGate', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 测试钉钉网关模式,审批节点: 任务22', '0', '0', 1562990710920388610, '2023-08-15 21:02:24', 1562990710920388610, 1691435166140231682, 1);
INSERT INTO `jf_ws_notice` VALUES (1700039736777060353, 'HandoverFlow', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 工作交接工单,审批节点: 任务', '0', '0', 1, '2023-09-08 14:53:53', 1, 1700039727486676993, 1);
INSERT INTO `jf_ws_notice` VALUES (1729036577677819906, 'JDShopParSonFlow', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 京东购物父子流程,审批节点: 添加商品并付款', '0', '0', 1, '2023-11-27 15:16:59', 1, 1729036577254195202, 1);
INSERT INTO `jf_ws_notice` VALUES (1729036766035623938, 'JDShopSonFlow', '0', '(审批)您有新的待办任务, 发起人: 管理员,工单类型: 京东购物子流程,审批节点: 发货', '0', '0', 1, '2023-11-27 15:17:44', 1, 1729036763657453570, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
