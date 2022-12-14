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
@TableName("f_chunk_file")
@ApiModel(value="ChunkFile对象", description="")
public class ChunkFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "原文件md5")
    private String md5;

    @ApiModelProperty(value = "分块文件md5")
    private String chunkMd5;

    @ApiModelProperty(value = "分块文件编号")
    private Integer num;

    @ApiModelProperty(value = "分块文件路径")
    private String path;

    @ApiModelProperty(value = "上传时间")
    private Date upDate;


}
