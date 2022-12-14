package com.booker.lsp.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 11:37
 * @Description 文件信息
 **/

@Data
@ApiModel("文件信息")
public class FileInfoVO {


    @ApiModelProperty(value = "名称")
    private String fileName;

    @ApiModelProperty(value = "简介")
    private String brief;

    @ApiModelProperty(value = "封面图片")
    private String thumbnail;

    @ApiModelProperty(value = "文件MD5")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "大小")
    private Long length;

    @ApiModelProperty(value = "格式")
    private String format;

    @ApiModelProperty(value = "文件类型, 1 视频, 2 图片, 3 文本, 4 文档, 0 其它")
    private String fileType;

    @ApiModelProperty(value = "状态")
    private String fileStatus;

    @ApiModelProperty(value = "文件加密标志 Y 加密, N 未加密, 默认N")
    private String fileEncryptFlag;

    @ApiModelProperty(value = "上传人ID")
    private String userId;

    @ApiModelProperty(value = "上传人用户名")
    private String username;

    @ApiModelProperty(value = "上传/扫描日期")
    @JSONField(format = "yyyy-MM-dd")
    private Date upDate;

    @ApiModelProperty(value = "0 私有, 1 共享")
    private String ownStatus;

}
