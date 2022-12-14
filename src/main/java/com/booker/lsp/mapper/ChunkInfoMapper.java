package com.booker.lsp.mapper;

import com.booker.lsp.entity.ChunkInfo;
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
public interface ChunkInfoMapper extends BaseMapper<ChunkInfo> {


    ChunkInfo queryChunkInfoByMd5(@Param("md5") String md5,
                                  @Param("userId") String userId);

}
