-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 , 默认租户 1

-- 菜单SQL
insert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861501, '-1', '/admin/result/index', '', '0', 'icon-bangzhushouji', '0', null , '8', null , '科研成果表管理', 1);

-- 菜单对应按钮SQL
insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861502,1747056861501, 'admin_result_view', '1', null, '1',  '0', null, '0', null, '科研成果表查看', 1);

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861503,1747056861501, 'admin_result_add', '1', null, '1',  '0', null, '1', null, '科研成果表新增', 1);

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861504,1747056861501, 'admin_result_edit', '1', null, '1',  '0', null, '2', null, '科研成果表修改', 1);

insert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861505,1747056861501, 'admin_result_del', '1', null, '1',  '0', null, '3', null, '科研成果表删除', 1);

insert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name, tenant_id)
values (1747056861506,1747056861501, 'admin_result_export', '1', null, '1',  '0', null, '3', null, '导入导出', 1);