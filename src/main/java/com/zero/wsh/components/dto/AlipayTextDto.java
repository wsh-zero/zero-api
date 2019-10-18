package com.zero.wsh.components.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AlipayTextDto {
    @JsonProperty("msg_type")
    private String msgType;
    private AlipayTextDetails text;

    @Data
    @Builder
    public static class AlipayTextDetails {
        private String title;
        private String content;
    }
}
