package com.flechazo.jnyzdairy.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 图片处理工具类
 */
@Component
public class ImageUtils {

    /**
     * 添加文字水印
     */
    public void addTextWatermark(Path sourcePath, String text) throws IOException {
        // 读取原图片
        BufferedImage sourceImage = ImageIO.read(sourcePath.toFile());
        
        // 创建图片缓存对象
        BufferedImage resultImage = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // 获取Graphics2D对象
        Graphics2D graphics = resultImage.createGraphics();
        
        // 绘制原图
        graphics.drawImage(sourceImage, 0, 0, null);
        
        // 设置水印文字样式
        graphics.setFont(new Font("Arial", Font.BOLD, 30));
        graphics.setColor(new Color(255, 255, 255, 128)); // 半透明白色
        
        // 计算水印位置（右下角）
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = sourceImage.getWidth() - fontMetrics.stringWidth(text) - 10;
        int y = sourceImage.getHeight() - fontMetrics.getHeight() + 10;
        
        // 绘制水印
        graphics.drawString(text, x, y);
        graphics.dispose();

        // 保存图片
        String format = getImageFormat(sourcePath.toString());
        ImageIO.write(resultImage, format, sourcePath.toFile());
    }

    /**
     * 添加图片水印
     */
    public void addImageWatermark(Path sourcePath, String watermarkPath) throws IOException {
        // 读取水印图片
        BufferedImage watermark = ImageIO.read(new ClassPathResource(watermarkPath).getInputStream());
        
        // 添加水印
        Thumbnails.of(sourcePath.toFile())
                .scale(1.0)
                .watermark(Positions.BOTTOM_RIGHT, watermark, 0.5f)
                .toFile(sourcePath.toFile());
    }

    /**
     * 压缩图片
     */
    public void compressImage(Path sourcePath, int width, int height) throws IOException {
        Thumbnails.of(sourcePath.toFile())
                .size(width, height)
                .keepAspectRatio(true)
                .toFile(sourcePath.toFile());
    }

    /**
     * 获取图片格式
     */
    private String getImageFormat(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extension.toLowerCase();
    }
} 