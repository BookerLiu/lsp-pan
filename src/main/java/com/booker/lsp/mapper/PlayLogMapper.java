package com.booker.lsp.mapper;

import com.booker.lsp.entity.PlayLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 播放记录表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-12-08
 */
public interface PlayLogMapper extends BaseMapper<PlayLog> {


    PlayLog selectByUserIdAndMd5(@Param("userId") String userId,
                                 @Param("md5") String md5);

}
