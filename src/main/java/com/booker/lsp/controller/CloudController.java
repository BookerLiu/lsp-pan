package com.booker.lsp.controller;

import com.booker.lsp.service.CloudService;
import com.booker.lsp.vo.DirectoryVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryCloudParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author BookerLiu
 * @Date 2022/11/29 20:18
 * @Description CloudController
 **/

@RestController
@RequestMapping("/cloudController")
public class CloudController {

    @Resource
    private CloudService cloudService;


    @GetMapping("/getFileListByParentID")
    @ApiOperation(value = "根据父id查询文件/文件夹列表")
    public ServerResponse<PageRes<DirectoryVO>> getFileListByParentID(QueryCloudParam param) {
        PageRes<DirectoryVO> fileListByParentID = cloudService.getFileListByParentID(param);
        return ServerResponse.success(fileListByParentID);
    }


    @GetMapping("/createDir")
    @ApiOperation(value = "根据父id创建文件夹")
    public ServerResponse<DirectoryVO> createDir(@RequestParam(value = "parentId", required = false) String parentId,
                                                 @RequestParam("fileName") String dirName) {
        return cloudService.createDir(parentId, dirName);
    }

    @PutMapping("/rename")
    @ApiOperation(value = "重命名")
    public ServerResponse<String> rename(@RequestParam("id") String id,
                                         @RequestParam("fileName") String fileName) {
        return cloudService.rename(id, fileName);
    }



}
