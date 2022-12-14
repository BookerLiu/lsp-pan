package com.booker.lsp.vo.query;

import com.booker.lsp.vo.common.QueryPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 16:16
 * @Description 查询video参数
 **/

@Data
@ApiModel("查询video参数")
public class QueryVideoParam extends QueryPage {

    @ApiModelProperty("自定义分类id")
    private String customTypeId;
}
