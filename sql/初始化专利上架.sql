INSERT INTO `kyd`.`t_patent_shelf` (id,`pid`, `shelf_status`, `shelf_time`, `cooperation_mode`, `cooperation_amount`, `create_user_id`, `create_user_name`)
select id,pid,1,NOW(),'技术转让',FLOOR(1 + (RAND() * 1000)),1,'管理员'
from t_patent_info
where not exists(select 0 from t_patent_shelf where pid=t_patent_info.pid)