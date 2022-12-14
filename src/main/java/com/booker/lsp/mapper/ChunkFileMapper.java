package com.booker.lsp.mapper;

import com.booker.lsp.entity.ChunkFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
public interface ChunkFileMapper extends BaseMapper<ChunkFile> {


    ChunkFile queryLastChunkFile(@Param("md5") String md5,
                                 @Param("userId") String userId);

}
