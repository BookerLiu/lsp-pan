package com.booker.lsp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 播放记录表
 * </p>
 *
 * @author BookerLiu
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_play_log")
@ApiModel(value="PlayLog对象", description="播放记录表")
public class PlayLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件md5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "播放时长")
    @TableField("play_duration")
    private Integer playDuration;

    @ApiModelProperty(value = "记录截图")
    @TableField("thumbnail")
    private String thumbnail;

    @ApiModelProperty(value = "记录日期")
    @TableField("log_date")
    private Date logDate;


}
