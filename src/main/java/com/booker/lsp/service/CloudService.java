package com.booker.lsp.service;

import com.booker.lsp.vo.DeleteFileVO;
import com.booker.lsp.vo.DirectoryVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryCloudParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/29 20:23
 * @Description cloud service
 **/
public interface CloudService {


    PageRes<DirectoryVO> getFileListByParentID(QueryCloudParam param);

    /**
     * 创建文件夹
     * @param parentId 父文件夹id
     * @return
     */
    ServerResponse<DirectoryVO> createDir(String parentId, String dirName);


    @Transactional(rollbackFor = Exception.class)
    ServerResponse<String> rename(String id, String fileName);
}
