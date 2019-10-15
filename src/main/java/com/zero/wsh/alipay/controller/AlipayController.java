package com.zero.wsh.alipay.controller;

import com.alipay.api.AlipayClient;
import com.zero.wsh.alipay.AlipayFactory;
import com.zero.wsh.alipay.dto.AlipayCreateMessageDto;
import com.zero.wsh.enums.AlipayEnums;
import com.zero.wsh.utils.AlipayUtils;
import com.zero.wsh.utils.FileUtils;
import com.zero.wsh.utils.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/alipay/")
@Api(tags = "生活号相关接口")
public class AlipayController {
    //2019101168301261 测试3
    //2019101168319217 测试2
    //2019100968226631 测试1
    //2019100968229570 测试
    //2019092667830183   王善华生活号
    private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNEliBQU8q1FXWSv/Z89z/JjIvDLvkFIal7Y9+YOKfhxZmXzE1CkNjgxP93yTUoV1oylWrjqcT0UrEIE9Rm199zSoDwu5HxThvXfsb510e6IJu7HUF1onSMlLlC64PTgLOhREv9myelnqzaeJn7e+BNrwnEkzP3g5F8K/78zAiSpqgxU4jeJGy0g6jjyfzae8Jg5NhZoWWGOA4qKxN2ZkUaqfih2AAbhikV+bGKpCbkYuVrxTOzOBQWlINirh6q5a66qscimJUBkOdkVX0pybdnIrLiOEujhFbhb8PqCpSzsoWw9W8j9IPoHHgcqBXMZQjBb4AN8KJS/1Af5Qi+70DAgMBAAECggEAcLWsorCUWOrHHexUGEXfVrektrSc1bCUl7OCxv0Pc1eRzP7XKMJnbvVGHrqPCZdXMycdZXEXCh1Yd/Q8iz3xAGjP3at7hzoKSegzfWrPmqc3tvoS0TREbv0n/7niOnYghfII9mITDG274OaDLngKuBkMIMddOWL30mdno4v79Biizl0VdXZWymSNlbm0eByWcjoLdRqaCt4G1N8eA3c0m+EL0iT0kxahIMIUDj1ix4F3Nx7N4cNG2fAlh/NXXI1xEdimRrXhDVSqh9lfzZQ4fMOFK3vjov3/itqKted7df95GROs2C3tjj99iuz88yfYvh0b139t5yDHoEj+UsTSUQKBgQDp6PtYcfVFuHJMIxZG/otFA8BJJVTSGY29Tr+2Oak7Ms+bX48XAQZHgHUkD7A9UuHt897/jLaZFcyE4aNZgOCrTVAwQOsfOxMcbu1gz5SQULzueQ+cg4vua/9UR4QSRgT/aDVKrf3aA0f26gdtMXtlAoYiTbJPjKUCVPY3Z331SQKBgQCaZOYn//wHIWrBa4QUK83HfD+mzMcIlaEa0Br7iEQFS2xC5byDD5KqDfY+pVvbLE7qN68snFv3B/xQK6ZyV5Y3c+lrVojCMcN4Udt8NliG3vPGFIrxHj8+P0B0qlr+BJHnT1jOnZ4cJhOw84WTyQZBAvgEnX8mZpnzqg2oxPX76wKBgFgnaQqFMDXNdK4DDzNEXUJuTnqCqjvgS43L7u6DPqdD2AArfGbkCPTLkdo8W2RRJzqhjSpsYqjBEWoHC6QAsSt+ILl+zd3gSqyNlcLFmKzm8L1DPMH0xbjeVMTziu/EGcAUCvmRJzmRwP5qZtAdNO71i43EEG5wiAOf94cb5tmZAoGAcZHm+2WBrn0WWlEMRwoxxJvQ9pGLlvT6HQ8Xyg7iekKQ0GTN1lqOOCktCcf5Wa+uHPv5D4e03nIxV4fEWDe1d/T7rslor/CLKOaN3UDcvdki2IIj8lmdi9xbPXZSmKgj61KisNmghHQ959w6Vgt5Xjkg18RC06yFcoeUpKC9kSECgYEArm37DuIFzyezrwQOZ13dQrXE78ZUav2V/OzK1ehXLTBF5zAw5Xk4Db4ZWDPSfPkJ/zcKKMxzxBhFFIjkpXTkH1NxEqMv3Ud6xW1493dm1QUmqxI4b8fcxRKJZI+6CjvuuCLzSd2iSQGKbeRuLhY1jrgiJ1nHrOPgExWq++Wv3MM=";
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgavm9h2011Q2tjKcpfzEhcJdIoJCcp8ltFv0tMUcHGU6HkHQ/+PI/kH1Blr0d5J2h5UCHTUy69/7D/khe1DP75hx9u8L070Kd5lq2qpgqnUXX/vn6q2po7mtEsGCN79BYK1kJXZTh30/bzQ9rCzJnXtmWsdFxdYltguPYbGg3MUpiDUXH3HVS1ZYjDlCU6QjchHOOvFmHtKKxLC3KRONvFnGEuu0R4bIDnbgGnVhBoTaHrhp3LTc/uroOzZvL7yuWuHFEUbWKr1RERVW1TvQ1j1Xg0KdURn+I7E4nKmWA5obdbarB+LW6Yd4pqOMVIXwDOIEXiFcNY7/ityA/jj3uwIDAQAB";

    /**
     * @param appId
     * @return
     * @ApiImplicitParams({
     * @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "李四"),
     * @ApiImplicitParam(name = "address", value = "用户地址", defaultValue = "深圳", required = true)
     * }
     * )
     */
    @ApiOperation("上传图片接口")
    @PostMapping("upload/image")
    @ApiImplicitParam(name = "appId", value = "appId")
    public Object uploadImage(@RequestParam(defaultValue = "2019100968229570") String appId, MultipartFile multipartFile) {
        AlipayClient alipayClient = AlipayFactory.getAlipayClient(appId, PRIVATE_KEY, ALIPAY_PUBLIC_KEY);
        return AlipayUtils.uploadImage(alipayClient, FileUtils.multFileToFile(multipartFile));
    }

    @ApiOperation("创建图文消息接口")
    @PostMapping("create/message")
    @ApiImplicitParam(name = "appId", value = "appId")
    public Object createMessage(@RequestParam(defaultValue = "2019100968229570") String appId) {
        AlipayClient alipayClient = AlipayFactory.getAlipayClient(appId, PRIVATE_KEY, ALIPAY_PUBLIC_KEY);
        AlipayCreateMessageDto build = AlipayCreateMessageDto.builder()
                .title("标题")
                .content("内容")
                .cover("https://oalipay-dl-django.alicdn.com/rest/1.0/image?fileIds=7840I6k5Tn-hksT4TwmDDQAAACMAAQED&zoom=original")
                .couldComment(AlipayEnums.T)
                .build();
        String bizContent = GsonUtil.toJson(build);
        return AlipayUtils.createMessage(alipayClient, bizContent);
    }

    @ApiOperation("修改图文消息接口")
    @PostMapping("update/message")
    @ApiImplicitParam(name = "appId", value = "appId")
    public Object updateMessage(@RequestParam(defaultValue = "2019100968229570") String appId) {
        AlipayClient alipayClient = AlipayFactory.getAlipayClient(appId, PRIVATE_KEY, ALIPAY_PUBLIC_KEY);
        AlipayCreateMessageDto build = AlipayCreateMessageDto.builder()
                .title("标题")
                .content("内容")
                .cover("https://oalipay-dl-django.alicdn.com/rest/1.0/image?fileIds=7840I6k5Tn-hksT4TwmDDQAAACMAAQED&zoom=original")
                .couldComment(AlipayEnums.T)
                .contentId("1")
                .build();
        String bizContent = GsonUtil.toJson(build);
        return AlipayUtils.updateMessage(alipayClient, bizContent);
    }

    @ApiOperation("发送图文消息")
    @PostMapping("send/total")
    @ApiImplicitParam(name = "appId", value = "appId")
    public Object sendTotal(@RequestParam(defaultValue = "2019100968229570") String appId) {
        AlipayClient alipayClient = AlipayFactory.getAlipayClient(appId, PRIVATE_KEY, ALIPAY_PUBLIC_KEY);
        String bizContent = "";
        return AlipayUtils.sendTotal(alipayClient, bizContent);
    }
}
