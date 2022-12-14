package com.booker.lsp.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
@Data
@ApiModel("文件/文件夹vo")
public class DirectoryVO  {


    private String id;

    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ApiModelProperty(value = "层级  从0开始")
    private Integer level;

    @ApiModelProperty(value = "文件/文件夹名称")
    private String fileName;

    @ApiModelProperty(value = "Y 文件, N 文件夹")
    private String fileFlag;

    @ApiModelProperty(value = "文件md5, file_flag='Y'时有此字段")
    private String md5;

    @ApiModelProperty(value = "所属用户id")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty(value = "0 有效, 1 删除")
    private String status;

    @ApiModelProperty(value = "文件大小")
    private Long length;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;

    @ApiModelProperty(value = "是否共享 Y 共享, N 否")
    private String share;


}
