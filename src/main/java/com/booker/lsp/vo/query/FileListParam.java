package com.booker.lsp.vo.query;

import com.booker.lsp.vo.common.QueryPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/12/12 15:05
 * @Description 查询文件列表
 **/

@Data
@ApiModel("查询文件列表")
public class FileListParam extends QueryPage {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("上传日期起")
    private String upDateStart;

    @ApiModelProperty("上传日期止")
    private String upDateEnd;

    @ApiModelProperty("文件类型 enum FileType")
    private String fileType;

}
