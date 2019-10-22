package com.zero.wsh.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableInfo {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 数据类型名
     */
    private String typeName;
    /**
     * 类型的长度
     */
    private int columnSize;
    /**
     * 小数点
     */
    private int decimalDigits;
    /**
     * 是否允许为空
     */
    private String isNullAble;
    /**
     * 描述
     */
    private String remarks;
    /**
     * 默认值
     */
    private String columnDef;
    /**
     * 自动递增
     */
    private String isAutoincrement;
    /**
     * 是否主键
     */
    private String isPrimaryKey;
}
