package com.booker.lsp.mapper;

import com.booker.lsp.constant.FileType;
import com.booker.lsp.entity.CustomType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 自定义分类表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
public interface CustomTypeMapper extends BaseMapper<CustomType> {

    List<CustomType> getUserCustomTypesByFileType(@Param("userId") String userId,
                                                  @Param("fileType") String fileType);

}
