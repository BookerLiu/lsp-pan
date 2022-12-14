package com.booker.lsp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 17:22
 * @Description 文件上传信息
 **/

@ApiModel("文件上传信息")
@Data
public class FileUpInfo {

    @ApiModelProperty("文件/分块文件")
    private MultipartFile blob = null;

    @ApiModelProperty("文件大小")
    private Long length;

    @ApiModelProperty("分块文件序号")
    private Integer chunkNum;

    @ApiModelProperty("分块文件数量")
    private Integer chunkCount;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件MD5")
    private String md5;

    @ApiModelProperty("简介")
    private String brief;

    @ApiModelProperty(value = "文件夹id")
    private String dirId;

    @ApiModelProperty(value = "是否共享 Y 共享, N 否")
    private String share;

//    @ApiModelProperty("自定义分类")
//    private String customType;


}
