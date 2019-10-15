package com.zero.wsh.utils;

import com.google.common.base.Throwables;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;

/**
 * 统一处理错误异常
 */
@RestControllerAdvice
public class ExceptionUtil {
    /**
     * 所有验证框架异常捕获处理
     *
     * @return
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map validationExceptionHandler(Exception exception) {
        BindingResult bindResult = null;
        if (exception instanceof BindException) {
            bindResult = ((BindException) exception).getBindingResult();
        } else if (exception instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }
        String msg;
        if (bindResult != null && bindResult.hasErrors()) {
            msg = bindResult.getAllErrors().get(0).getDefaultMessage();
            assert msg != null;
            if (msg.contains("NumberFormatException")) {
                msg = "参数类型错误！";
            }
        } else {
            msg = "系统繁忙，请稍后重试...";
        }
        return ResultUtil.warning(msg, exception);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map exception(HttpRequestMethodNotSupportedException ex) {
        return ResultUtil.warning("请求方法不支持", ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map exception(MissingServletRequestParameterException ex) {
        return ResultUtil.warning("接口必须参数未传", ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map exception(Exception ex, HttpServletRequest request) {
        return resultFormat(ex, request);
    }

    private <T extends Throwable> Map resultFormat(T ex, HttpServletRequest request) {
        this.insertExceptionLog(ex, request);
        return ResultUtil.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器出错!", ex);
    }

    private void insertExceptionLog(Throwable ex, HttpServletRequest request) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println("Request Url:");
        printWriter.println(request.getRequestURI());
        printWriter.println();
        printWriter.println("Request Header:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            printWriter.print(key);
            printWriter.print("=");
            printWriter.print(request.getHeader(key));
            printWriter.println();
        }
        printWriter.println();
        printWriter.println("Request Parameter:");
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            printWriter.print(entry.getKey());
            printWriter.print("=");
            for (String s : entry.getValue()) {
                printWriter.print(s);
                printWriter.print(";");
            }
            printWriter.println();
        }
        printWriter.println();
        printWriter.println(Throwables.getRootCause(ex).getMessage());
        printWriter.println();
        ex.printStackTrace(printWriter);
    }
}
