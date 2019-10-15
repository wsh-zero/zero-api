package com.zero.wsh.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.zero.wsh.enums.AlipayEnums;

public class AlipayFactory {
    /**
     * 获得API调用客户端
     *
     * @return
     */
    public static AlipayClient getAlipayClient(String appId, String privateKey, String alipayPublicKey) {
        return new DefaultAlipayClient(AlipayEnums.ALIPAY_GATEWAY.getKey(), appId,
                privateKey, AlipayEnums.FORMAT.getKey(), AlipayEnums.CHARSET.getKey(), alipayPublicKey, AlipayEnums.SIGN_TYPE.getKey());
    }
}
