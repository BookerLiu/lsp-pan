package com.booker.lsp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

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
@TableName("f_chunk_info")
@ApiModel(value="ChunkInfo对象", description="")
public class ChunkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件md5")
    private String md5;

    @ApiModelProperty(value = "文件大小")
    private Long length;

    @ApiModelProperty(value = "分块文件数量")
    private Integer chunkCount;

    @ApiModelProperty(value = "简介")
    private String brief;

    @ApiModelProperty(value = "文件夹id")
    private String dirId;

    @ApiModelProperty(value = "上传人id")
    private String userId;

    @ApiModelProperty(value = "是否共享 Y 共享, N 否")
    private String share;

    @ApiModelProperty(value = "分块文件list")
    private List<ChunkFile> chunkFileList;


}
