package com.github.tbosoft.kernal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片工具类
 *
 * @author qbhy
 */

public class ResourceUtils {




    public static String withTail(String path) {
        return path + (path.endsWith("/") ? "" : "/");
    }

    /**
     * 从字体库中获取字体
     *
     * @param font
     *
     * @return File
     *
     * @throws IOException
     */
    public static File getFontFile(String font,String path) throws IOException {

        // 从用户自定义的目录中找
        File fontFile = new File(withTail(path)+font + ".ttf");
        if (fontFile.exists()) {
            return fontFile;
        }

        // 找不到的话从默认字体库中找
        fontFile = new File(ResourceUtils.getResourcePath("fonts/" + font));
        if (fontFile.exists()) {
            return fontFile;
        }

        // 实在找不到就抛异常
        throw new IOException(font + " font not found!");
    }

    /**
     * 获取图片
     *
     * @param url
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage getImage(String url) throws IOException {
        if (url.contains("://")) {
            return getImageFromUrl(url);
        }else{
            File srcfile = new File(url);
            if (!srcfile.exists())
            {
                throw new IOException("file not exists!");
            }
            return ImageIO.read(srcfile);
        }
    }
    /**
     * 通过 URL 获取图片并缓存到本地文件夹中
     *
     * @param url
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage getImageFromUrl(String url) throws IOException {
        return getImageFromUrl(url, false);
    }

    public static BufferedImage getImageFromUrl(String url, Boolean update) throws IOException {


        BufferedImage image = ImageIO.read(new URL(url));
        return image;
    }


    /** 实现缩放后的截图
     *
     * @param image
     *            缩放后的图像
     * @param subImageBounds
     *            要截取的子图的范围
     * @throws IOException */
    private static BufferedImage saveSubImage(BufferedImage image, Rectangle subImageBounds) throws IOException
    {
        if (subImageBounds.x < 0 || subImageBounds.y < 0)
        {
            throw new IOException("Bad subimage bounds");
        }
        //修正宽度
        if(subImageBounds.width + subImageBounds.x > image.getWidth()){
            subImageBounds.width = image.getWidth() - subImageBounds.x;
        }
        //修正高度
        if(subImageBounds.height + subImageBounds.y > image.getHeight()){
            subImageBounds.height = image.getHeight() - subImageBounds.y;
        }
        BufferedImage subImage = image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width,
                subImageBounds.height);
        return subImage;
    }


    public static BufferedImage rize(BufferedImage srcBufImage, int width, int height)
    {
        BufferedImage bufTarget = null;
        int type = srcBufImage.getType();
        double sx = 1d;
        double sy = 1d;
        if(width > srcBufImage.getWidth()){
            sx = (double) width / srcBufImage.getWidth();
            sy = (double) height / srcBufImage.getHeight();

        }else{
            sx = (double) srcBufImage.getWidth() / width;
            sy = (double) srcBufImage.getHeight() / height;
        }



        if (type == BufferedImage.TYPE_CUSTOM)
        {
            ColorModel cm = srcBufImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            bufTarget = new BufferedImage(cm, raster, alphaPremultiplied, null);
        }
        else
        {
            bufTarget = new BufferedImage(width, height, type);
        }

        Graphics2D g = bufTarget.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(srcBufImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return bufTarget;
    }

    /** 实现图像的等比缩放和缩放后的截取，如果高度的值和宽度一样，则缩放按设置的值缩放 (只控制宽度的大小，高度的值设置不生效(只有高度的值和宽度的一样才生效)， 高度自动按比例缩放；如果缩放的图片小于你设置的值则保存原图大小)
     *
     * @param srcImage
     *            要缩放图片文件的路径
     * @param width
     *            要截取宽度
     * @param hight
     *            要截取的高度
     * @throws IOException */

    public static BufferedImage zoomOutImage(BufferedImage srcImage, int width, int hight) throws IOException
    {



        if (srcImage.getHeight() > 0 && srcImage.getWidth() > 0) //
        {

            if (width > 0 || hight > 0)
            {
                // 原图的大小
                int sw = srcImage.getWidth();
                int sh = srcImage.getHeight();

                // 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
                if ((sw > width && sh > hight) || (sw < width && sh < hight))
                {
                    srcImage = rize(srcImage, width, hight);
                }
                 else
                {

                    BufferedImage newBufferedImage = new BufferedImage(width, hight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = newBufferedImage.createGraphics();
                    int x = srcImage.getWidth()/2- width/2;
                    graphics.drawImage(saveSubImage(srcImage, new Rectangle(x, 0, width, hight)), 0, 0, null);
                    graphics.dispose();
                    return newBufferedImage;
                }
            }
            // 缩放后的图像的宽和高
            int w = srcImage.getWidth();
            int h = srcImage.getHeight();


            // 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
            if (w == width)
            {
                // 计算 X轴坐标
                int x = 0;
                int y = h / 2 - hight / 2;
                return saveSubImage(srcImage, new Rectangle(x, y, width, hight));


            }
            // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
            else if (h == hight)
            {
                // 计算X轴坐标
                int x = w / 2 - width / 2;
                int y = 0;
                return saveSubImage(srcImage, new Rectangle(x, y, width, hight));
            }
        }
        return null;
    }


    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @param radius
     * @param border
     * @param padding
     * @return
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if (border != 0) {
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
        }
        gs.dispose();
        return image;
    }

    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @return BufferedImage
     */
    public static BufferedImage setRadius(BufferedImage srcImage) {
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 6;
        return setRadius(srcImage, radius, 2, 5);
    }

    /**
     * 图片切圆角
     *
     * @param srcImage
     * @param radius
     * @return BufferedImage
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();

        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }

    /**
     * 获取资源路径
     *
     * @param resourceName
     * @return String
     * @throws IOException
     */
    public static String getResourcePath(String resourceName) throws IOException {
        URL url = Drawable.class.getClassLoader().getResource(resourceName);
        if (url != null) {
            return url.getPath();
        }

        throw new IOException(resourceName + " resource not found!");
    }
}
