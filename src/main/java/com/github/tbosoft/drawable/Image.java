package com.github.tbosoft.drawable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.github.tbosoft.kernal.Drawable;
import com.github.tbosoft.kernal.ResourceUtils;
import lombok.Data;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public class Image extends Drawable {

    @Override
    public void draw(Graphics2D gd, int posterWidth, int posterHeight) throws IOException {
        BufferedImage image;

        if (qrCode) {
            // 是二维码图片的话创建一个二维码
            try {
                image = createQrCode(url, width, height, qrCodeMargin);
            } catch (WriterException e) {
                throw new IOException("生成二维码失败", e);
            }
        } else {
            // 获取图片
            image = ResourceUtils.getImage(url);
        }

        // 如果宽高不合适，先缩放
        if (image.getWidth() != width || image.getHeight() != height) {

            image = ResourceUtils.zoomOutImage(image, width, height);
//          image = resize(image, width, height);
        }

        // 处理圆角
        if (borderRadius > 0) {
            image = ResourceUtils.setRadius(image, borderRadius * 4, 0, 0);
        }

        // 画图
        gd.drawImage(image, x, y, width, height, new ImageObserver() {
            @Override
            public boolean imageUpdate(java.awt.Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });
    }

    /**
     * 缩放图片
     *
     * @param image  需要缩放的图片
     * @param width  宽
     * @param height 高
     * @return BufferedImage
     */
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        java.awt.Image img = image.getScaledInstance(width, height, java.awt.Image.SCALE_FAST);

        BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = newBufferedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        return newBufferedImage;
    }

    /**
     * z index 值
     */
    private int index = 4;
    public int getZIndex() {
        return index;
    }

    /**
     * x 值
     */
    private Integer x;

    /**
     * y 值
     */
    private Integer y;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * border radius
     */
    private Integer borderRadius = 0;

    /**
     * url
     */
    private String url;

    /**
     * 是否是二维码图片
     */
    private boolean qrCode = false;

    /**
     * 二维码边距
     */
    private int qrCodeMargin = 2;

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @param margin  二维码边距
     * @return BufferedImage 返回图片
     * @throws WriterException 异常
     */
    public static BufferedImage createQrCode(String content, int width, int height, int margin) throws WriterException {

        Map<EncodeHintType, Comparable> hints = new HashMap<>();

        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 字符串编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 纠错等级
        hints.put(EncodeHintType.MARGIN, margin); // 图片边距
        QRCodeWriter writer = new QRCodeWriter();

        return MatrixToImageWriter.toBufferedImage(writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints));
    }

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return BufferedImage 返回图片
     * @throws WriterException 异常
     */
    public static BufferedImage createQrCode(String content, int width, int height) throws WriterException {
        return createQrCode(content, width, height, 2);
    }

}
