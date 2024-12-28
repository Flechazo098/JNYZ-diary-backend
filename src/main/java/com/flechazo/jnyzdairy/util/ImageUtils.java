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
 * 图片处理工具类，提供图片水印添加、压缩等功能。
 *
 * @author Flechazo
 */
@Component
public class ImageUtils {

    /**
     * 添加文字水印到图片上。
     * <p>
     * 该方法读取原始图片文件，创建一个新的带有指定文字水印的图片，并保存覆盖原文件。
     * 水印将被放置在图片的右下角，使用半透明白色字体显示。
     *
     * @param sourcePath 原始图片的路径
     * @param text       要添加的文字水印内容
     * @throws IOException 如果读取或写入图片时发生错误
     */
    public void addTextWatermark(Path sourcePath, String text) throws IOException {
        /* 读取原图片 */
        BufferedImage sourceImage = ImageIO.read(sourcePath.toFile());

        /* 创建图片缓存对象 */
        BufferedImage resultImage = new BufferedImage(
                sourceImage.getWidth(),
                sourceImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        /* 获取Graphics2D对象 */
        Graphics2D graphics = resultImage.createGraphics();

        /* 绘制原图 */
        graphics.drawImage(sourceImage, 0, 0, null);

        /* 设置水印文字样式 */
        graphics.setFont(new Font("Arial", Font.BOLD, 30));
        // 半透明白色
        graphics.setColor(new Color(255, 255, 255, 128));

        /* 计算水印位置（右下角） */
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = sourceImage.getWidth() - fontMetrics.stringWidth(text) - 10;
        int y = sourceImage.getHeight() - fontMetrics.getHeight() + 10;

        /* 绘制水印 */
        graphics.drawString(text, x, y);
        graphics.dispose();

        /* 保存图片 */
        String format = getImageFormat(sourcePath.toString());
        ImageIO.write(resultImage, format, sourcePath.toFile());
    }

    /**
     * 添加图片水印到图片上。
     * <p>
     * 该方法读取一个水印图片，并将其以一定的透明度添加到目标图片的右下角。
     *
     * @param sourcePath   原始图片的路径
     * @param watermarkPath 水印图片的路径（相对于classpath）
     * @throws IOException 如果读取或写入图片时发生错误
     */
    public void addImageWatermark(Path sourcePath, String watermarkPath) throws IOException {
        /* 读取水印图片 */
        BufferedImage watermark = ImageIO.read(new ClassPathResource(watermarkPath).getInputStream());

        /* 添加水印 */
        Thumbnails.of(sourcePath.toFile())
                .scale(1.0)
                .watermark(Positions.BOTTOM_RIGHT, watermark, 0.5f)
                .toFile(sourcePath.toFile());
    }

    /**
     * 压缩图片。
     * <p>
     * 该方法根据给定的宽度和高度调整图片大小，并保持纵横比不变。压缩后的图片会覆盖原文件。
     *
     * @param sourcePath 原始图片的路径
     * @param width      目标宽度
     * @param height     目标高度
     * @throws IOException 如果读取或写入图片时发生错误
     */
    public void compressImage(Path sourcePath, int width, int height) throws IOException {
        Thumbnails.of(sourcePath.toFile())
                .size(width, height)
                .keepAspectRatio(true)
                .toFile(sourcePath.toFile());
    }

    /**
     * 获取图片格式。
     * <p>
     * 该私有辅助方法用于从文件名中提取图片格式（例如 "jpg" 或 "png"），并返回小写的格式字符串。
     *
     * @param fileName 文件名
     * @return 图片格式的小写字符串
     */
    private String getImageFormat(String fileName) {
        // 获取文件扩展名并转换为小写
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extension.toLowerCase();
    }
}