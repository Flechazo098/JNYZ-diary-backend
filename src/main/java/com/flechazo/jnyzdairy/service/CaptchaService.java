package com.flechazo.jnyzdairy.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 验证码服务类，提供生成验证码文本和图片的功能。
 *
 * @author Flechazo
 */
@Service
@Slf4j
public class CaptchaService {

    private final DefaultKaptcha captchaProducer;

    /**
     * 构造函数，用于依赖注入。
     *
     * @param captchaProducer 用于生成验证码的组件
     */
    public CaptchaService(DefaultKaptcha captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

    /**
     * 生成验证码文本。
     * <p>
     * 该方法使用 {@code DefaultKaptcha} 组件来创建一个新的验证码文本，并记录生成的日志信息。
     * 如果在生成过程中发生错误，则记录错误日志并抛出运行时异常。
     *
     * @return 生成的验证码文本字符串
     * @throws RuntimeException 如果生成验证码文本失败
     */
    public String generateCaptchaText() {
        try {
            String text = captchaProducer.createText();
            log.debug("Generated captcha text: {}", text);
            return text;
        } catch (Exception e) {
            log.error("Error generating captcha text", e);
            throw new RuntimeException("Failed to generate captcha text", e);
        }
    }

    /**
     * 根据给定的验证码文本生成对应的验证码图片。
     * <p>
     * 该方法接收一个验证码文本字符串作为参数，使用 {@code DefaultKaptcha} 组件生成相应的验证码图片，
     * 并将其转换为字节数组返回。如果生成图片或转换过程失败，则记录错误日志并抛出 IO 异常。
     *
     * @param code 验证码文本字符串
     * @return 包含验证码图片数据的字节数组
     * @throws IOException 如果生成验证码图片或转换为字节数组失败
     */
    public byte[] generateCaptchaImage(String code) throws IOException {
        try {
            BufferedImage image = captchaProducer.createImage(code);
            if (image == null) {
                throw new RuntimeException("Failed to create captcha image");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (!ImageIO.write(image, "jpg", outputStream)) {
                throw new RuntimeException("Failed to write captcha image");
            }

            byte[] imageBytes = outputStream.toByteArray();
            log.debug("Generated captcha image size: {} bytes", imageBytes.length);
            return imageBytes;
        } catch (Exception e) {
            log.error("Error generating captcha image", e);
            throw new IOException("Failed to generate captcha image", e);
        }
    }
}