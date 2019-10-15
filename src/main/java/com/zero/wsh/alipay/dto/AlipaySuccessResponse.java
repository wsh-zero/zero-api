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
}
