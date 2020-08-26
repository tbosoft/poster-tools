package com.github.tbosoft.drawable;

import com.github.tbosoft.kernal.ColorTools;
import com.github.tbosoft.kernal.Drawable;
import com.github.tbosoft.kernal.ResourceUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import sun.font.FontDesignMetrics;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Data
public class Text extends Drawable {

    private static final String DEFAULT_FONT = "Default";
    // 程序当前运行目录
    private static final String current = System.getProperty("user.dir");

    /**
     * 字体路径
     */
    private static String fontsPath = current + "/fonts";


    static Map<String, Font> drawFonts = new HashMap<>();

    public static Font getDrawFont(String name, Integer fontSize,boolean systemFont,boolean bold) {
        if(StringUtils.isNotEmpty(name) && !systemFont){
            Font tempFont = drawFonts.get(name);
            if (tempFont == null) {
                try {
                    tempFont = Font.createFont(Font.TRUETYPE_FONT, ResourceUtils.getFontFile(name,fontsPath)).deriveFont((float)fontSize);
                } catch (Exception e) {
                    tempFont = new Font(DEFAULT_FONT, Font.PLAIN, fontSize);
                }
                drawFonts.put(name, tempFont);
                return tempFont;
            }

            if (tempFont.getSize() != fontSize) {
                return tempFont.deriveFont((float)fontSize);
            }
        }

        return new Font("".equals(name)?DEFAULT_FONT:name, bold?Font.BOLD:Font.PLAIN, fontSize);
    }

    @Override
    public void draw(Graphics2D gd, int posterWidth, int posterHeight) {
        Font drawFont = getDrawFont(font, fontSize,systemFont,bold);

        // 设置字体和颜色
        gd.setFont(drawFont);
        gd.setColor(ColorTools.String2Color(color));
        //消除锯齿状 - 由微信群友【菠萝蜜】提供
        gd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // 文本域实际宽度
        int textWidth = width + x > posterWidth ? posterWidth - x : (width == 0 ? posterWidth : width);
        // 当前行数
        int currentNum = 0;
        // 当前行应该取到第几个文字
        int lineTextIndex;

        // 剩余没有画的图片
        String surplus = text;
        // 当前行文本
        String lineText;
        // 是否最后一行
        boolean lastLine;
        // 是否需要省略号
        boolean ellipsis = textOverflow.equals("ellipsis");

        // x 轴偏移量
        int offsetX = 0;

        // 当前行文本宽度
        int lineTextWidth;
        lineHeight = lineHeight <= 0 ? fontSize : lineHeight;

        // 一行一行画
        while (surplus.length() > 0 && currentNum < lineNum) {
            lineTextWidth = getWordWidth(drawFont, surplus);
            lastLine = currentNum + 1 == lineNum;
            // 放不下的时候,就要对文本进行裁剪了
            if (lineTextWidth > textWidth) {
                lineTextIndex = interceptALine(surplus, drawFont, textWidth, lastLine, ellipsis);
                lineText = surplus.substring(0, lineTextIndex); // 截取当前行文本
                surplus = surplus.substring(lineTextIndex); // 记录剩余文本
                lineTextWidth = getWordWidth(drawFont, lineText); // 重新计算宽度
            } else {
                lineText = surplus;
                surplus = ""; // 画完了
            }

            if (textAlign.equals("center")) {
                offsetX = (textWidth - lineTextWidth) / 2;
            }
            if (textAlign.equals("right")) {
                offsetX = (textWidth - lineTextWidth);
            }

            if (lastLine && ellipsis && surplus.length() > 0) {
                lineText = lineText + "...";
            }

            gd.drawString(lineText, x + offsetX, y + fontSize + (lineHeight - fontSize) / 2 + currentNum * lineHeight);

            //文本的修饰线条  绘制：
            int startX=0;// 开始 x 坐标
            int endX=0; // 结束 x 坐标
            int startY=0; // 开始 y 坐标
            int endY=0; // 结束 y 坐标

            //log.info("开始进行 文本修饰线条 绘制：textDecoration:{} ",textDecoration);
            gd.setStroke(new BasicStroke((float) textDecorationLineWith));
            gd.setPaint(ColorTools.String2Color(color)); // 设置画笔颜色

            //中划线
            if(textDecoration.equals("line-through")){
                startX=x + offsetX;
                startY=y + lineHeight / 2 + textDecorationOffsetY + currentNum * lineHeight;
                endX=startX+lineTextWidth;
                endY=startY;

                gd.drawLine(startX, startY, endX, endY); // 划线
            }
            //下划线
            else if(textDecoration.equals("underline")){
                startX=x + offsetX;
                startY=(y + fontSize + (lineHeight - fontSize) / 2 + currentNum * lineHeight) + textDecorationOffsetY;
                endX=startX+lineTextWidth;
                endY=startY;

                gd.drawLine(startX, startY, endX, endY); // 划线
            }


            currentNum++;
        }
    }

    /**
     * 截取一行的文本，获取索引
     *
     * @param surplus  剩余文本
     * @param drawFont 字体
     * @param lastLine 是否最后一行
     * @param ellipsis 是否溢出显示省略号
     * @return int 返回索引
     */
    private int interceptALine(String surplus, Font drawFont, int textWidth, boolean lastLine, boolean ellipsis) {
        int fullTextWidth = getWordWidth(drawFont, surplus);
        // 如果是最后一行且溢出且需要省略号，则预留省略号宽度
        textWidth = fullTextWidth > textWidth && lastLine && ellipsis ? textWidth - getWordWidth(drawFont, "...") : textWidth;

        // 默认值,后面试用循环不断调整为最佳值
        int lineIndex = surplus.length() - (fullTextWidth - textWidth) / fontSize;

        String lineText = surplus.substring(0, lineIndex);
        int lineTextWidth = getWordWidth(drawFont, lineText);

        // 循环到文本宽度小于文本域宽度为止
        while (lineTextWidth > textWidth) {
            lineIndex--;
            lineText = surplus.substring(0, lineIndex);
            lineTextWidth = getWordWidth(drawFont, lineText);
        }

        return lineIndex;
    }

    /**
     * 获取文本所占宽度
     *
     * @param font    字体
     * @param content 文本内容
     * @return int 返回宽度
     */
    private static int getWordWidth(Font font, String content) {
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        int width = 0;
        for (int i = 0; i < content.length(); i++) {
            width += metrics.charWidth(content.charAt(i));
        }
        return width;
    }

    /**
     * z index 值
     */
    private int index = 1;

    public int getZIndex() {
        return index;
    }


    private int x = 0;//  x 坐标

    private int y = 0; // y 坐标

    private Integer fontSize = 24; // 字体大小

    private Integer width = 0; // 文本域宽度

    private Integer lineHeight = 0; // 行高


    private Integer lineNum = 1; // 行数


    private String color = "#000000"; // 颜色


    private String text; // 文本内容

    private Integer opacity = 1; // 透明度

    /**
     * 是否是系统字体
     */
    private boolean systemFont = true;

    /**
     * 字体加粗
     */
    private boolean bold = false;

    private String textAlign = "left"; // 文本对齐方式
    private String font; // 字体
    private String textOverflow = "ellipsis"; // 文本溢出默认省略号

     /*
   text-decoration下划线CSS单词值参数： 暂时只支持 中划线、下划线 和 无装饰
   none:无装饰
   blink:闪烁
   underline:下划线
   line-through:贯穿线 中划线
   overline:上划线
   */

    private String textDecoration="none";
    private int textDecorationLineWith=1; //默认线宽
    private int textDecorationOffsetY=2;  //默认线条 Y轴偏移量,主要起美观协调作用

}
