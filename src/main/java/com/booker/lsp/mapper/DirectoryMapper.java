package com.booker.lsp.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booker.lsp.entity.Directory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.booker.lsp.vo.DirectoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
public interface DirectoryMapper extends BaseMapper<Directory> {


    Page<DirectoryVO> queryFileListByParentID(Page<Directory> page,
                                              @Param("pid") String pid,
                                              @Param("userId") String userId);

    List<Directory> queryDirAllParentById(@Param("userId") String userId,
                                          @Param("id") String id);

    List<Directory> queryDirAllSonById(@Param("userId") String userId,
                                       @Param("id") String id);

    int updateStatusByIds(@Param("userId") String userId,
                          @Param("idList") List<String> ids,
                          @Param("status") String status);

    int updateFileNameById(@Param("userId") String userId,
                           @Param("id") String id,
                           @Param("fileName") String fileName);

}
