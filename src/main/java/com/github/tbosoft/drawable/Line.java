package com.github.tbosoft.drawable;

import com.github.tbosoft.kernal.ColorTools;
import com.github.tbosoft.kernal.Drawable;
import lombok.Data;


import java.awt.*;
import java.io.IOException;

@Data
public class Line extends Drawable {

    /**
     * z index 值
     */
    private int index = 2;
    public int getZIndex() {
        return index;
    }


    private int startX;// 开始 x 坐标

    private int endX; // 结束 x 坐标

    private int startY; // 开始 y 坐标

    private int endY; // 结束 y 坐标


    private int width = 1; // 宽度

    private String color = "#000000"; // 颜色

    @Override
    public void draw(Graphics2D gd, int posterWidth, int posterHeight) throws IOException {
        if (width > 0) {
            gd.setStroke(new BasicStroke((float) width));
            gd.setPaint(ColorTools.String2Color(color)); // 设置画笔颜色
            gd.drawLine(startX, startY, endX, endY); // 划线
        }
    }
}
