package com.flechazo.jnyzdairy.dto;

import lombok.Data;

/**
 * 验证码结果DTO，用于封装验证码信息及其图像数据。
 *
 * @author Flechazo
 */
@Data
public class CaptchaResult {

    /**
     * 验证码字符串，用户需要输入以完成验证。
     */
    private String code;

    /**
     * 验证码图像的字节数组，用于在网络中传输图像数据。
     */
    private byte[] imageBytes;

    /**
     * 构造函数，初始化验证码结果对象。
     *
     * @param code        验证码字符串
     * @param imageBytes  验证码图像的字节数组
     */
    public CaptchaResult(String code, byte[] imageBytes) {
        this.code = code;
        this.imageBytes = imageBytes;
    }
}