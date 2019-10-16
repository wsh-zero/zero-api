package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlipayFailResponse {
    private Integer code;
    private String msg;
    @SerializedName("sub_code")
    private String subCode;
    @SerializedName("sub_msg")
    private String subMsg;
}
