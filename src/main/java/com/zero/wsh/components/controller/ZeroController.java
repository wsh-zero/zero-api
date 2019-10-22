package com.zero.wsh.components.controller;

import com.zero.wsh.utils.DatabaseUtil;
import com.zero.wsh.utils.GenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/zero")
@Api(tags = "测试接口")
public class ZeroController {

    @RequestMapping(value = {""})
    @ApiOperation("查询")
    public Object zero(String tableName) {
        //查询列信息
        List<Map<String, String>> columns = DatabaseUtil.getColumnNames(tableName);
        //生成代码
        GenUtils.generatorCode(columns);

        return null;
    }
}
