package com.booker.lsp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/11/5 15:22
 * @Description 页面tab页
 **/

@Data
@ApiModel("页面tab页")
public class TabVO {

    @ApiModelProperty(value = "唯一ID")
    private String id;

    @ApiModelProperty(value = "父节点ID")
    private String parentId;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类等级 从0开始")
    private Integer level;

    @ApiModelProperty(value = "是否为父节点有子集  Y 是,  N 否")
    private String parentFlag;
}
