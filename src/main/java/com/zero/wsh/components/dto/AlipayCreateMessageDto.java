package com.zero.wsh.components.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zero.wsh.enums.AlipayEnums;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlipayCreateMessageDto {
    private String title;
    //封面图片
    private String cover;
    private String content;
    @JsonProperty("could_comment")
    private AlipayEnums couldComment;
    @JsonProperty("content_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String contentId;
}
