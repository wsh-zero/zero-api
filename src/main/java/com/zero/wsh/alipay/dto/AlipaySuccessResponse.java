package com.zero.wsh.alipay.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AlipaySuccessResponse {
    @Data
    public class AlipayUploadImage {
        private String code;
        private String msg;
        @SerializedName("image_id")
        private String imageId;
        @SerializedName("image_url")
        private String imageUrl;
    }

    @Data
    public class AlipayCreateOrUpdateMessage {
        private String code;
        private String msg;
        @SerializedName("content_id")
        private String contentId;
        @SerializedName("content_url")
        private String contentUrl;
    }

    @Data
    public class AlipaySendTotal {
        private String code;
        private String msg;
        @SerializedName("message_id")
        private String messageId;
    }
}
