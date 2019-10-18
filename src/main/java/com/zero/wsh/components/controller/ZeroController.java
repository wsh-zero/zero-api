package com.zero.wsh.components.controller;

import com.zero.wsh.components.mapper.ZeroMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/zero")
@Api(tags = "测试接口")
public class ZeroController {
    @Resource
    private ZeroMapper zeroMapper;

    @RequestMapping(value = {""})
    @ApiOperation("查询")
    public Object zero() {
        return zeroMapper.selectList(null);
    }
}
