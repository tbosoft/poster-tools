package com.github.tbosoft.service;

import com.beust.jcommander.internal.Lists;
import com.github.tbosoft.config.PosterConfig;
import com.github.tbosoft.contracts.Result;
import com.github.tbosoft.contracts.Uploader;
import com.github.tbosoft.enums.FormatEnum;
import com.github.tbosoft.kernal.ColorTools;
import com.github.tbosoft.kernal.CreateResult;
import com.github.tbosoft.kernal.Drawable;
import com.github.tbosoft.kernal.Poster;
import com.qiniu.util.Md5;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public final class PosterManager {



    private PosterConfig posterConfig;

    private Uploader yunUploader;

    public PosterManager(PosterConfig posterConfig){
        this.posterConfig = posterConfig;
    }
    public PosterManager(PosterConfig posterConfig,Uploader yunUploader){
        this.posterConfig = posterConfig;
        this.yunUploader =yunUploader;
    }

    public Result getPoster(Poster poster){


        List<Drawable> allList = Lists.newArrayList();
        if(poster.getImages() != null){
            allList.addAll(poster.getImages());
        }
        if(poster.getBlocks() != null){
            allList.addAll(poster.getBlocks());
        }
        if(poster.getLines() != null){
            allList.addAll(poster.getLines());
        }
        if(poster.getTexts() != null){
            allList.addAll(poster.getTexts());
        }
        return getPoster(allList);
    }

    /**
     * 传入需要绘制的item数组
     * @param drawList
     * @return
     * @throws IOException
     */

    public CreateResult getPoster(List<Drawable> drawList) {

        try {
            // 初始化图片
            BufferedImage image = new BufferedImage(posterConfig.getWidth(), posterConfig.getHeight(), posterConfig.getFormat().equals(FormatEnum.Format.PNG.getValue()) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_3BYTE_BGR);

            // create graphics
            Graphics2D gd = image.createGraphics();

            // 初始化画布层级 map
            Map<Integer, List<Drawable>> indexMap = new Hashtable<>();
            List<Drawable> drawables;

            // 如果有背景，画个矩形做背景
            if (posterConfig.getBackgroundColor() != null) {
                gd.setColor(ColorTools.String2Color(posterConfig.getBackgroundColor()));
                gd.fillRect(0, 0, posterConfig.getWidth(), posterConfig.getHeight());
            }
            for (Drawable item : drawList) {
                push2map(indexMap, item);
            }


            // 按 index 顺序执行绘画过程
            for (Integer index : indexMap.keySet()) {
                drawables = indexMap.get(index);
                if (drawables != null) {
                    for (Drawable drawable : drawables) {
                        drawable.draw(gd, posterConfig.getWidth(), posterConfig.getHeight());
                    }
                }
            }


            gd.dispose();

            // 创建临时文件
            File file = File.createTempFile(Md5.md5(drawList.toString().getBytes()), "." + posterConfig.getFormat());
            ImageIO.write(image, posterConfig.getFormat(), file); // 把文件写入图片
            file.deleteOnExit(); // 使用完后删除文件
            CreateResult result = new CreateResult(file);
            //上传服务器
            if (yunUploader != null) {
                result = yunUploader.upload(file);
            }
            return result;

        } catch (IOException e) {
            return CreateResult.fail("create error");
        }

    }

    private void push2map(Map<Integer, List<Drawable>> indexMap, Drawable drawable) {
        List<Drawable> drawables = indexMap.get(drawable.getZIndex());
        drawables = drawables == null ? new Vector<>() : drawables;
        drawables.add(drawable);
        indexMap.put(drawable.getZIndex(), drawables);
    }





}
