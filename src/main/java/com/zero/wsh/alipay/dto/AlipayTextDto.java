package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlipayTextDto {
    @SerializedName("msg_type")
    private String msgType;
    private AlipayTextDetails text;
    @Data
    @Builder
    public static class  AlipayTextDetails {
        private String title;
        private String content;
    }
}
