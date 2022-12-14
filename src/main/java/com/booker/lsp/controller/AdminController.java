package com.booker.lsp.controller;

import com.booker.lsp.service.AdminService;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.FileListParam;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author BookerLiu
 * @Date 2022/12/12 15:01
 * @Description 管理controller
 **/
@RestController
@RequestMapping("/adminController")
public class AdminController {


    @Resource
    private AdminService adminService;

    @GetMapping("fileList")
    @ApiModelProperty("文件列表")
    public ServerResponse<PageRes<FileInfoVO>> fileList(FileListParam paramVO) {
        return adminService.fileList(paramVO);
    }

}
