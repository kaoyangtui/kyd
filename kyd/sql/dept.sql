ALTER TABLE `kyd`.`t_result`
    MODIFY COLUMN `dept_id` bigint NULL DEFAULT NULL COMMENT '所属组织ID' AFTER `create_by`,
    ADD COLUMN `dept_name` varchar (255) NULL COMMENT '组织名称' AFTER `dept_id`;