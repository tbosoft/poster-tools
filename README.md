# poster-tools
海报SDK

## 详细文档
## 新版功能更丰富
* 基于 java 开发，部署和二次开发更方便
* 图片可以上传到 公共 CDN，不占用主机磁盘，且速度更快


## 组件参数解释

### Posterconfig字段

| 字段            | 类型                     | 必填 | 描述                                       |
| --------------- | ------------------------ | ---- | ------------------------------------------ |
| width           | Number(单位:px)          | 是   | 画布宽度                                   |
| height          | Number(单位:px)          | 是   | 画布高度                                   |
| backgroundColor | String                   | 否   | 画布颜色                                   |                                |

### blocks字段

| 字段名          | 类型             | 必填 | 描述                                   |
| --------------- | ---------------- | ---- | -------------------------------------- |
| x               | Number(单位:px) | 是   | 块的坐标                               |
| y               | Number(单位:px) | 是   | 块的坐标                               |
| width           | Number(单位:px) | 否   | 如果内部有文字，由文字宽度和内边距决定 |
| height          | Number(单位:px) | 是   |                                        |
| paddingLeft     | Number(单位:px) | 否   | 内左边距                               |
| paddingRight    | Number(单位:px) | 否   | 内右边距                               |
| borderWidth     | Number(单位:px) | 否   | 边框宽度                               |
| borderColor     | String           | 否   | 边框颜色                               |
| backgroundColor | String           | 否   | 背景颜色                               |
| borderRadius    | Number(单位:px) | 否   | 圆角                                   |
| text            | Object           | 否   | 块里面可以填充文字，参考texts字段解释  |
| index          | Int              | 否   | 层级，越大越高                         |

### texts字段

| 字段名         | 类型             | 必填 | 描述                                                         |
| -------------- | ---------------- | ---- | ------------------------------------------------------------ |
| x              | Number(单位:px) | 是   | 坐标                                                         |
| y              | Number(单位:px) | 是   | 坐标                                                         |
| text           | String\|Object   | 是   | 当Object类型时，参数为text字段的参数，marginLeft、marginRight这两个字段可用（示例请看下文） |
| fontSize       | Number(单位:px) | 是   | 文字大小                                                     |
| color          | String           | 否   | 颜色                                                         |
| lineHeight     | Number(单位:px) | 否   | 行高                                                         |
| lineNum        | Int              | 否   | 根据宽度换行，最多的行数                                     |
| width          | Number(单位:px) | 否   | 没有指定为画布宽度，默认为x轴右边所有宽度                                           |
| baseLine       | String           | 否   | top\| middle\|bottom基线对齐方式                             |
| textAlign      | String           | 否   | left\|center\|right对齐方式                                  |
| index         | Int              | 否   | 层级，越大越高                                               |
| fontFamily     | String           | 否   | 默认字体为'pingfangtf' ，支持自定义字体      |

### images字段

| 字段         | 类型             | 必填 | 描述                                      |
| ------------ | ---------------- | ---- | ----------------------------------------- |
| x            | Number(单位:px) | 是   | 右上角的坐标                              |
| y            | Number(单位:px) | 是   | 右上角的坐标                              |
| url          | String           | 是   | 图片url（**需要添加到下载白名单域名中**）也支持本地图片 |
| width        | Number(单位:px) | 是   | 宽度（**会根据图片的尺寸同比例缩放**）    |
| height       | Number(单位:px) | 是   | 高度（**会根据图片的尺寸同比例缩放**）    |
| borderRadius | Number(单位:px) | 否   | 圆角，跟css一样                           |
| index       | Int              | 否   | 层级，越大越高                            |
| qrCode       | Bool              | 否   | 是否二维码图片，如果是，url内容就是二维码内容  |

### lines字段

| 字段   | 类型             | 必填 | 描述           |
| ------ | ---------------- | ---- | -------------- |
| startX | Number(单位:px) | 是   | 起始坐标       |
| startY | Number(单位:px) | 是   | 起始坐标       |
| endX   | Number(单位:px) | 是   | 终结坐标       |
| endY   | Number(单位:px) | 是   | 终结坐标       |
| width  | Number(单位:px) | 是   | 线的宽度       |
| color  | String           | 否   | 线的颜色       |
| index | Int              | 否   | 层级，越大越高 |
