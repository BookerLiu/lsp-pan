package com.booker.lsp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/12/9 19:52
 * @Description 删除云盘文件
 **/

@Data
@ApiModel("删除云盘文件")
public class DeleteFileVO {


    private String permanentFlag;

    private List<String> idList;
}
