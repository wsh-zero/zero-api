package com.zero.wsh.alipay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlipayImageTextDto {
    @JsonProperty("msg_type")
    private String msgType;
    private List<AlipayImageTextDetails> articles;

    @Data
    @Builder
    public static class AlipayImageTextDetails {
        private String title;
        private String desc;
        @JsonProperty("image_url")
        private String imageUrl;
        private String url;
        @JsonProperty("action_name")
        private String actionName;
    }
}
