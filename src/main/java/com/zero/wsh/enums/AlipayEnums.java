package com.zero.wsh.enums;

public enum AlipayEnums {
    ALIPAY_GATEWAY("https://openapi.alipay.com/gateway.do"),
    FORMAT("json"),
    CHARSET("GBK"),
    SIGN_TYPE("RSA2"),
    JPG("jpg"),
    ALIPAY_OFFLINE_MATERIAL_IMAGE_UPLOAD_RESPONSE("alipay_offline_material_image_upload_response");
    private String key;

    AlipayEnums(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
