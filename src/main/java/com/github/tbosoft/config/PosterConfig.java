package com.github.tbosoft.config;

import com.github.tbosoft.enums.FormatEnum;
import lombok.Data;

@Data
public class PosterConfig {

    private Integer width;

    /**
     * 画布高度
     */

    private Integer height;

    /**
     * 画布背景颜色
     */
    private String backgroundColor = null;


    /**
     * 生成图片的格式
     */
    private String format = FormatEnum.Format.PNG.getValue();
}
