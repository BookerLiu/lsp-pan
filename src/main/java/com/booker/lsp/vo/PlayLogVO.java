package com.booker.lsp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/12/8 11:06
 * @Description 播放记录
 **/

@Data
@ApiModel("播放记录")
public class PlayLogVO {

    @ApiModelProperty("文件md5")
    private String md5;

    @ApiModelProperty("播放时长")
    private int playDuration;
}
