package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlipaySuccessResponse {
    private String code;
    private String msg;
    @Data
    public class AlipayUploadImage {
        @SerializedName("image_id")
        private String imageId;
        @SerializedName("image_url")
        private String imageUrl;
    }

    @Data
    public class AlipayCreateOrUpdateMessage {
        @SerializedName("content_id")
        private String contentId;
        @SerializedName("content_url")
        private String contentUrl;
    }

    @Data
    public class AlipaySendTotal {
        @SerializedName("message_id")
        private String messageId;
    }
}
