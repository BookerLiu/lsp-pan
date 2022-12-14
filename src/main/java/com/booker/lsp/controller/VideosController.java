package com.booker.lsp.controller;

import com.booker.lsp.service.VideosService;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.PlayLogVO;
import com.booker.lsp.vo.TabVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryVideoParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/5 10:41
 * @Description 视频controller
 **/

@Api("video controller")
@RestController
@RequestMapping("/videosController")
public class VideosController {


    @Resource
    private VideosService videosService;


    @ApiOperation("获取视频自定义分类")
    @GetMapping("/getVideoCustomTypes")
    public ServerResponse<List<TabVO>> getVideoCustomTypes() {
        List<TabVO> list = videosService.getVideoTypes();
        return ServerResponse.success(list);
    }

    @ApiOperation("获取视频列表")
    @GetMapping("/getVideos")
    public ServerResponse<PageRes<FileInfoVO>> getVideos(QueryVideoParam queryVideoParam) {
        PageRes<FileInfoVO> videos = videosService.getVideos(queryVideoParam);
        return ServerResponse.success(videos);
    }


    @ApiOperation("播放视频")
    @GetMapping("/playVideo")
    public void playVideo(@RequestParam("md5") String md5,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        videosService.playVideo(md5, request, response);
    }


    @ApiOperation("存储播放记录")
    @PostMapping("/playLog")
    public ServerResponse<String> playLog(@RequestBody PlayLogVO logVO) {
        return videosService.playLog(logVO);
    }





}
