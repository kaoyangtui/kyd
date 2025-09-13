package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.admin.dto.sys.SysUserPageRequest;

public interface SystemService {

    IPage<UserVO> pageUserVO(Page page, SysUserPageRequest req);

}
