package com.booker.lsp.vo.query;

import com.booker.lsp.vo.common.QueryPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author BookerLiu
 * @Date 2022/11/29 20:33
 * @Description
 **/

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("云盘列表")
public class QueryCloudParam  extends QueryPage {

    @ApiModelProperty("父id")
    private String parentId;
}
