package com.pig4cloud.pigx.exception;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author zhaoliang
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public R handleBizException(BizException ex) {
        // 这里可以构建自定义的响应对象
        return R.failed(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("参数校验失败：");
        for (FieldError error : fieldErrors) {
            errorMessage.append("[").append(error.getField()).append("]")
                    .append(error.getDefaultMessage()).append("; ");
        }
        // 移除最后的分号和空格
        if (!fieldErrors.isEmpty()) {
            errorMessage.setLength(errorMessage.length() - 2);
        }
        return R.failed(errorMessage.toString());
    }

}
