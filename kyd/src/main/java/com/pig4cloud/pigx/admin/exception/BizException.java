package com.pig4cloud.pigx.admin.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 自定义业务异常
 * @author zhaoliang
 */
public class BizException extends Exception {

    private static final long serialVersionUID = 1L;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Object... args) {
        super(StrUtil.format(message, args));
    }

    public BizException(String message, Throwable cause, Object... args) {
        super(StrUtil.format(message, args), cause);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
