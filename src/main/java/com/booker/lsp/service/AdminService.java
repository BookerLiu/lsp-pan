package com.booker.lsp.service;

import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.FileListParam;

/**
 * @Author BookerLiu
 * @Date 2022/12/12 15:11
 * @Description 管理service
 **/
public interface AdminService {



    ServerResponse<PageRes<FileInfoVO>> fileList(FileListParam paramVO);
}
