ALTER TABLE `kyd`.`t_result`
    ADD COLUMN `leader_id` bigint NULL COMMENT '负责人 ID' AFTER `shelf_status`,
ADD COLUMN `leader_name` varchar(50) NULL COMMENT '负责人姓名' AFTER `leader_id`;