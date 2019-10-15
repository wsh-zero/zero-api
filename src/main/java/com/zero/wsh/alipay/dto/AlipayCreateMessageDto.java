package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName("could_comment")
    private AlipayEnums couldComment;
    @SerializedName("content_id")
    private String contentId;
}
