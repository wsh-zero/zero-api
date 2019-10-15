package com.zero.wsh.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.request.AlipayOfflineMaterialImageUploadRequest;
import com.alipay.api.request.AlipayOpenPublicMessageContentCreateRequest;
import com.alipay.api.request.AlipayOpenPublicMessageTotalSendRequest;
import com.alipay.api.response.AlipayOfflineMaterialImageUploadResponse;
import com.alipay.api.response.AlipayOpenPublicMessageContentCreateResponse;
import com.alipay.api.response.AlipayOpenPublicMessageTotalSendResponse;
import com.zero.wsh.alipay.dto.AlipaySuccessResponse;
import com.zero.wsh.enums.AlipayEnums;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class AlipayUtils {
    public static String uploadImage(AlipayClient alipayClient, File file) {
        AlipayOfflineMaterialImageUploadRequest request = new AlipayOfflineMaterialImageUploadRequest();
        request.setImageType(AlipayEnums.JPG.getKey());
        request.setImageName(UUID.randomUUID().toString());
        request.setImageContent(new FileItem(file));
        String failMsg;
        try {
            AlipayOfflineMaterialImageUploadResponse response = alipayClient.execute(request);
            file.delete();
            if (response.isSuccess()) {
                Map<String, Object> map = GsonUtil.gsonToMaps(response.getBody());
                AlipaySuccessResponse.AlipayUploadImage alipaySuccessResponse = GsonUtil.gsonToBean(GsonUtil.toJson(map.get(AlipayEnums.ALIPAY_OFFLINE_MATERIAL_IMAGE_UPLOAD_RESPONSE.getKey())), AlipaySuccessResponse.AlipayUploadImage.class);
                return response.getBody();
            }
            failMsg = response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            failMsg = String.format("上传失败:%s", e.getMessage());
        }
        return failMsg;
    }

    public static String createMessage(AlipayClient alipayClient, String bizContent) {
        AlipayOpenPublicMessageContentCreateRequest request = new AlipayOpenPublicMessageContentCreateRequest();
        request.setBizContent("{" +
                "\"title\":\"里面的标题1\"," +
                "\"cover\":\"https:\\/\\/oalipay-dl-django.alicdn.com\\/rest\\/1.0\\/image?fileIds=3XwqJu4hStm5Tk22D1OPlgAAACMAAQED&zoom=original\"," +
                "\"content\":\"<html>这里是富文本1<h1>正确1</h1></html>\"," +
                "\"could_comment\":\"T\"" +
                "  }");
        String failMsg;
        try {
            AlipayOpenPublicMessageContentCreateResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response.getBody();
            }
            failMsg = String.format("创建图文消息内容失败 code=%s msg= %s", response.getCode(), response.getMsg());
        } catch (Exception e) {
            failMsg = String.format("创建图文消息内容失败:%s", e.getMessage());
        }
        return failMsg;
    }

    /**
     * 构造群发图文消息
     *
     * @return
     */
    public static String sendTotal(AlipayClient alipayClient, String bizContent) {
        // 使用SDK，构建群发请求模型
        AlipayOpenPublicMessageTotalSendRequest request = new AlipayOpenPublicMessageTotalSendRequest();
//        String msg = "{\"articles\":[{\"action_name\":\"第一个链接文字\",\"desc\":\"第一个内容\",\"image_url\":\"https:\\/\\/oalipay-dl-django.alicdn.com\\/rest\\/1.0\\/image?fileIds=jsDwluVSRDiYVQITyrVzcwAAACMAAQED&zoom=original\",\"title\":\"第一个标题\",\"url\":\"alipays:\\/\\/platformapi\\/startapp?appId=20000909&url=%2Fwww%2Fmsg.html%3FpublicId%3D2019101168301261%26msgId%3D201910116830126108844e0e-a1b3-4fd4-bb7f-1b58252fa8e7%26sourceId%3DOPENAPI_ITMC\"},{\"action_name\":\"第二个链接文字\",\"desc\":\"第二个内容\",\"title\":\"第二个标题\",\"image_url\":\"https:\\/\\/oalipay-dl-django.alicdn.com\\/rest\\/1.0\\/image?fileIds=jsDwluVSRDiYVQITyrVzcwAAACMAAQED&zoom=original\",\"url\":\"alipays:\\/\\/platformapi\\/startapp?appId=20000909&url=%2Fwww%2Fmsg.html%3FpublicId%3D2019101168301261%26msgId%3D201910116830126110a767f5-c155-482a-bd04-1685d4e5de09%26sourceId%3DOPENAPI_ITMC\"}],\"msg_type\":\"image-text\"}";
        request.setBizContent(bizContent);

        String failMsg;
        try {
            AlipayOpenPublicMessageTotalSendResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return response.getBody();
            }
            failMsg = String.format("消息发送失败 code=%s msg= %s", response.getCode(), response.getMsg());
        } catch (AlipayApiException e) {
            failMsg = String.format("消息发送失败:%s", e.getMessage());
        }
        return failMsg;

    }
}
