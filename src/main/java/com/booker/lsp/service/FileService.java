package com.booker.lsp.service;

import com.alibaba.fastjson2.JSONObject;
import com.booker.lsp.vo.DeleteFileVO;
import com.booker.lsp.vo.FileUpInfo;
import com.booker.lsp.vo.common.ServerResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 17:54
 * @Description 文件上传
 **/
public interface FileService {


    @Transactional(rollbackFor = Exception.class)
    ServerResponse<String> fileUp(FileUpInfo upInfo) throws Exception;


    ServerResponse<JSONObject> checkFileExist(String md5);

    @Transactional(rollbackFor = Exception.class)
    ServerResponse<String> mergeChunkFile(String md5) throws Exception;

    //下载文件
    void downloadFile(String token, HttpServletRequest request, HttpServletResponse response);

    ServerResponse<String> getDownloadToken(String md5);

    /**
     * 批量删除文件
     * @param vo 文件id数组
     * @return
     */
    ServerResponse<String> deleteFiles(DeleteFileVO vo);

}
