package com.zero.wsh.components.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("zero")
public class ZeroEntity implements Serializable {
    private String id;
    private String name;
}
