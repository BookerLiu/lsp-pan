package com.booker.lsp.service;

import com.booker.lsp.entity.User;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.PlayLogVO;
import com.booker.lsp.vo.TabVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.QueryPage;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryVideoParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 10:25
 * @Description video service
 **/
public interface VideosService {


    List<TabVO> getVideoTypes();


    PageRes<FileInfoVO> getVideos(QueryVideoParam queryVideoParam);

    void playVideo(String md5, HttpServletRequest request, HttpServletResponse response);

    ServerResponse<String> playLog(PlayLogVO logVO);

}
