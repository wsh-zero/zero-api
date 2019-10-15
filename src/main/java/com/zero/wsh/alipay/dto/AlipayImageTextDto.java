package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlipayImageTextDto {
    @SerializedName("msg_type")
    private String msgType;
    private List<AlipayImageTextDetails> articles;

    @Data
    @Builder
    public static class AlipayImageTextDetails {
        private String title;
        private String desc;
        @SerializedName("image_url")
        private String imageUrl;
        private String url;
        @SerializedName("action_name")
        private String actionName;
    }
}
