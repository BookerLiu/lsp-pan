package com.booker.lsp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_file_info")
@ApiModel(value="FileInfo对象", description="文件信息表")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String fileName;

    @ApiModelProperty(value = "简介")
    private String brief;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;

    @ApiModelProperty(value = "文件MD5")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "大小")
    private Long length;

    @ApiModelProperty(value = "时长")
    private Integer duration;

    @ApiModelProperty(value = "格式")
    private String format;

    @ApiModelProperty(value = "文件夹id")
    private String dirId;

    @ApiModelProperty(value = "自定义分类id")
    private String customTypeId;

    @ApiModelProperty(value = "文件类型, 1 视频, 2 图片, 3 文本, 4 文档, 0 其它")
    private String fileType;

    @ApiModelProperty(value = "状态 0 有效 1删除")
    private String status;

    @ApiModelProperty(value = "状态更新时间")
    private Date statusDate;

    @ApiModelProperty(value = "文件加密标志 Y 加密, N 未加密, 默认N")
    private String fileEncryptFlag;

    @ApiModelProperty(value = "上传人ID")
    private String userId;

    @ApiModelProperty(value = "上传/扫描日期")
    private Date upDate;

    @ApiModelProperty(value = "共享   N 私有, Y 共享")
    private String share;


}
