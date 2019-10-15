package com.zero.wsh.utils;

import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultUtil {
    private static final String SUCCESS_MSG = "获取成功";
    private static final String HANDLE_SUCCESS_MSG = "操作成功";

    private static Map success(int code, String msg, Object data) {
        Map<String, Object> resultMap = new LinkedHashMap<>(3);
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        resultMap.put("data", data);
        return resultMap;
    }

    public static Map success(String msg, Object data) {
        return ResultUtil.success(HttpStatus.OK.value(), msg, data);
    }

    public static Map success(Object data) {
        return ResultUtil.success(SUCCESS_MSG, data);
    }

    public static Map success(String msg) {
        return ResultUtil.success(msg, null);
    }

    public static Map success() {
        return ResultUtil.success(HANDLE_SUCCESS_MSG, null);
    }

    public static Map failed(int code, String msg, Throwable ex) {
        String data = null;
        if (null != ex) {
            data = ex.getMessage();
            ex.printStackTrace();
        }
        return ResultUtil.success(code, msg, data);
    }

    public static Map failed(int code, String msg) {
        return ResultUtil.failed(code, msg, null);
    }

    public static Map failed(String msg) {
        return ResultUtil.failed(HttpStatus.BAD_REQUEST.value(), msg);
    }

    public static Map failed(String msg, Throwable ex) {
        return ResultUtil.failed(HttpStatus.BAD_REQUEST.value(), msg, ex);
    }

    public static Map warning(String msg, Throwable ex) {
        String data = null;
        if (null != ex) {
            data = ex.getMessage();
        }
        return ResultUtil.success(HttpStatus.BAD_REQUEST.value(), msg, data);
    }
}
