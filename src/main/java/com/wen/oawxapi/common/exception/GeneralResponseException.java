package com.wen.oawxapi.common.exception;

import com.wen.oawxapi.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: 7wen
 * @Date: 2023-05-22 15:44
 * @description: 通用全局返回异常处理
 */
@Slf4j
@RestControllerAdvice
public class GeneralResponseException {
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e) {
        //打印异常报告
        log.error("捕获到异常,错误堆栈信息如下:" + e.toString());
        //如果需要异常堆栈 解除注释:
//        getStackTrace(e);
        if (e instanceof MethodArgumentNotValidException) {
            //如果属于参数校验异常
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            return R.error(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
        } else if (e instanceof CustomException) {
            //如果属于自定义异常
            CustomException customException = (CustomException) e;
            return R.error(customException.getMsg());
        } else if (e instanceof UnauthorizedException) {
            //如果属于权限相关异常
            return R.error("你没有相关权限");
        } else {
            //如果属于其他异常
            return R.error("服务器出现异常,请联系管理员");
        }
    }

    public void getStackTrace(Exception e) {
        //获取换行符
        String lineSeparatorStr = System.getProperty("line.separator");
        StringBuilder exStr = new StringBuilder();
        StackTraceElement[] trace = e.getStackTrace();
//        获取堆栈信息并输出为打印的形式
        for (StackTraceElement s : trace) {
            exStr.append("\tat " + s + "\r\n");
        }
        //打印error级别的堆栈日志
        log.error("捕获到异常,错误堆栈信息如下:" + e.toString() + lineSeparatorStr + exStr);
    }
}
