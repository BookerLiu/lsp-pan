package com.booker.lsp.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booker.lsp.constant.FileType;
import com.booker.lsp.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.query.FileListParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件信息表 Mapper 接口
 * </p>
 *
 * @author BookerLiu
 * @since 2022-11-29
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {


    Page<FileInfo> getFileInfosByFileType(Page<FileInfo> page,
                                          @Param("userId") String userId,
                                          @Param("fileType") FileType fileType,
                                          @Param("customTypeId") String customTypeId
    );


    FileInfo queryOneFileByMd5(@Param("md5") String md5,
                               @Param("userId") String userId);



    Integer queryCountByMd5AndUser(@Param("md5") String md5,
                                   @Param("userId") String userId);

    int updateStatusByMd5List(@Param("userId") String userId,
                              @Param("md5List") List<String> md5List,
                              @Param("status") String status);

    List<FileInfo> queryFileListByMd5List(@Param("userId") String userId,
                                          @Param("md5List") List<String> md5List);

    //查询共同拥有文件列表
    List<FileInfo> queryOwnAllFileListByMd5List(@Param("md5List") List<String> md5List);



    Page<FileInfoVO> queryFileList(Page<FileInfoVO> page,
                                   @Param("params")FileListParam params);

    int updateFileNameByMD5(@Param("userId") String userId,
                            @Param("md5") String md5,
                            @Param("fileName") String fileName);
}
