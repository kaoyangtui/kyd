CREATE VIEW v_all_application AS
SELECT
    '软著' AS type,
    create_time,
    dept_id,
    create_user_name,
    flow_status,
    del_flag
FROM t_soft_copy_reg

UNION ALL

SELECT
    '植物新品种' AS type,
    create_time,
    dept_id,
    create_user_name,
    flow_status,
    del_flag
FROM t_plant_variety

UNION ALL

SELECT
    '集成电路布图' AS type,
    create_time,
    dept_id,
    create_user_name,
    flow_status,
    del_flag
FROM t_ic_layout

UNION ALL

SELECT
    '标准' AS type,
    create_time,
    dept_id,
    create_user_name,
    flow_status,
    del_flag
FROM t_standard;
