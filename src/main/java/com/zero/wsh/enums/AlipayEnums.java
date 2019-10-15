package com.zero.wsh.enums;

public enum AlipayEnums {
    ALIPAY_GATEWAY("https://openapi.alipay.com/gateway.do"),
    FORMAT("json"),
    CHARSET("GBK"),
    SIGN_TYPE("RSA2"),
    JPG("jpg"),
    ALIPAY_OFFLINE_MATERIAL_IMAGE_UPLOAD_RESPONSE("alipay_offline_material_image_upload_response"),
    ALIPAY_OPEN_PUBLIC_MESSAGE_CONTENT_CREATE_RESPONSE("alipay_open_public_message_content_create_response"),
    ALIPAY_OPEN_PUBLIC_MESSAGE_CONTENT_MODIFY_RESPONSE("alipay_open_public_message_content_modify_response"),
    T("T"),
    F("F");
    private String key;

    AlipayEnums(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
