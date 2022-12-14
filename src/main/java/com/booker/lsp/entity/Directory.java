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
 * 
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_directory")
@ApiModel(value="Directory对象", description="")
public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Date createDate;

    @ApiModelProperty(value = "0 有效, 1 删除")
    private String status;

    @ApiModelProperty(value = "状态更新时间")
    private Date statusDate;


}
