package com.github.tbosoft.kernal;

import com.beust.jcommander.internal.Lists;
import com.github.tbosoft.drawable.Block;
import com.github.tbosoft.drawable.Image;
import com.github.tbosoft.drawable.Line;
import com.github.tbosoft.drawable.Text;
import lombok.Data;

import java.util.List;

@Data
public class Poster {
    /**
     * 文本列表
     */
    private List<Text> texts;

    /**
     * 图片列表
     */
    private List<Image> images;

    /**
     * 矩形列表
     */
    private List<Block> blocks;

    /**
     * 线列表
     */
    private List<Line> lines;


}
