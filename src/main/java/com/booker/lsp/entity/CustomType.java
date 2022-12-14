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
 * 自定义分类表
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_custom_type")
@ApiModel(value="CustomType对象", description="自定义分类表")
public class CustomType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    private String id;

    @ApiModelProperty(value = "父节点ID")
    private String parentId;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类等级 从0开始")
    private Integer level;

    @ApiModelProperty(value = "是否为父节点  Y 是,  N 否")
    private String parentFlag;

    @ApiModelProperty(value = "创建人ID")
    private String userId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否共享 Y 是, N 否")
    private String shareFlag;

    @ApiModelProperty(value = "归属文件类型")
    private String ownFileType;


}
