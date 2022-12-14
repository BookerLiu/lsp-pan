package com.booker.lsp.controller;

import com.alibaba.fastjson2.JSONObject;
import com.booker.lsp.service.FileService;
import com.booker.lsp.vo.DeleteFileVO;
import com.booker.lsp.vo.FileUpInfo;
import com.booker.lsp.vo.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 16:09
 * @Description 文件上传
 **/
@Api(tags = "文件上传")
@RequestMapping("/fileController")
@RestController
public class FileController {


    @Resource
    private FileService fileService;


    @ApiOperation(value = "检查文件是否已上传")
    @GetMapping("/checkFileExist")
    public ServerResponse<JSONObject> checkFileExist(@RequestParam("md5") String md5) {
        return fileService.checkFileExist(md5);
    }


    @ApiOperation(value = "文件上传")
    @PostMapping("/fileUp")
    public ServerResponse<String> fileUp(FileUpInfo upInfo) {
        //判断md5是否已存在库中
        try {
            return fileService.fileUp(upInfo);
        } catch (Exception e) {
            return ServerResponse.fail("500", "系统异常!");
        }
    }


    @ApiOperation(value = "合并文件")
    @GetMapping("/mergeCutFile")
    public ServerResponse<String> mergeCutFile(@RequestParam("md5") String md5) {
        try {
            return fileService.mergeChunkFile(md5);
        } catch (Exception e) {
            return ServerResponse.fail("500", "系统异常!");
        }
    }

    @ApiOperation(value = "获取文件下载授权")
    @GetMapping("/getDownloadToken")
    public ServerResponse<String> getDownloadToken(@RequestParam("md5") String md5) {
        return fileService.getDownloadToken(md5);
    }



    @ApiOperation(value = "下载文件")
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestParam("token") String token) {
       fileService.downloadFile(token, request, response);
    }


    @PostMapping("/deleteFiles")
    @ApiOperation(value = "批量删除文件/文件夹")
    public ServerResponse<String> deleteFiles(@RequestBody DeleteFileVO vo) {
        return fileService.deleteFiles(vo);
    }




}
