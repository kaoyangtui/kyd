ALTER TABLE `kyd`.`t_result`
    ADD COLUMN `leader_code` varchar(50) NULL COMMENT '负责人编码' AFTER `shelf_status`,
    ADD COLUMN `leader_name` varchar(50) NULL COMMENT '负责人姓名' AFTER `leader_id`;