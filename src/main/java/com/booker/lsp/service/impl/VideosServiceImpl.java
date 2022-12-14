package com.booker.lsp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booker.lsp.constant.FileType;
import com.booker.lsp.constant.TokenConst;
import com.booker.lsp.entity.CustomType;
import com.booker.lsp.entity.FileInfo;
import com.booker.lsp.entity.PlayLog;
import com.booker.lsp.entity.User;
import com.booker.lsp.mapper.CustomTypeMapper;
import com.booker.lsp.mapper.FileInfoMapper;
import com.booker.lsp.mapper.PlayLogMapper;
import com.booker.lsp.service.VideosService;
import com.booker.lsp.util.*;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.PlayLogVO;
import com.booker.lsp.vo.TabVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryVideoParam;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 11:15
 * @Description videos servcie
 **/
@Log4j2
@Service
public class VideosServiceImpl implements VideosService {


    @Resource
    private CustomTypeMapper customTypeMapper;
    @Resource
    private FileInfoMapper fileInfoMapper;
    @Resource
    private PlayLogMapper logMapper;

    @Override
    public List<TabVO> getVideoTypes() {
        User user = SecurityUtil.getCurrentUser();

        List<CustomType> list = customTypeMapper.getUserCustomTypesByFileType(user.getId(), FileType.VIDEO.id);

        CustomType type = new CustomType();
        type.setName("未分类");
        type.setId("noType");
        list.add(0, type);

        type = new CustomType();
        type.setName("全部");
        type.setId("all");
        list.add(0, type);

        return BeanUtil.copyToList(list, TabVO.class);
    }


    @Override
    public PageRes<FileInfoVO> getVideos(QueryVideoParam queryVideoParam) {
        User user = SecurityUtil.getCurrentUser();

        Page<FileInfo> page = new Page<>(queryVideoParam.getPageNumber(), queryVideoParam.getPageSize());
        page = fileInfoMapper.getFileInfosByFileType(page, user.getId(), FileType.VIDEO, queryVideoParam.getCustomTypeId());
        return  PageRes.fromPage(page, FileInfoVO.class);
    }


    @Override
    public void playVideo(String md5,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {

            outputStream = response.getOutputStream();
            response.reset();

            //获取请求头中Range的值
            String range = request.getHeader(HttpHeaders.RANGE);

            FileInfo fileInfo = fileInfoMapper.queryOneFileByMd5(md5, null);


            String filePath = fileInfo.getPath();
            //打开文件
            if (FileUtil.exist(fileInfo.getPath())) {

                response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                response.setHeader(HttpHeaders.CONTENT_TYPE, "video/" + fileInfo.getFormat());

                //使用RandomAccessFile读取文件
                targetFile = new RandomAccessFile(filePath, "r");
                long fileLength = fileInfo.getLength();

                //本次请求大小
                long requestSize = fileLength;
                //分段下载
                if (StringUtils.isNotEmpty(range)
                        && range.contains("=")
                        && range.contains("-")) {
                    //从Range中提取需要获取数据的开始和结束位置
                    long start = 0, end = 0;
//                    bytes=470917454-561849910
                    String[] ranges = range.split("=")[1].split("-");
                    start = Long.parseLong(ranges[0]);
                    if (ranges.length > 1) end = Long.parseLong(ranges[1]);

                    if (end != 0 && end > start) {
                        requestSize = end - start + 1;
                    }
                    long length;
                    if (end > 0) {
                        length = end - start + 1;
                    } else {
                        length = fileLength - start;
                        end = fileLength - 1;
                    }
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(length));
                    response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);

                    //断点传输下载返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(start);
                } else {
                    //如果Range为空则下载整个视频
                    //设置文件长度
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                }

                //从磁盘读取数据流返回
                byte[] cache = new byte[4096];
                try {
                    while (requestSize > 0) {
                        int len = targetFile.read(cache);
                        outputStream.write(cache, 0, len);
                        outputStream.flush();
                        requestSize -= len;
                    }
                } catch (IOException e) {
                    log.error("", e);
                    // tomcat原话。写操作IO异常几乎总是由于客户端主动关闭连接导致，所以直接吃掉异常打日志
                    //比如使用video播放视频时经常会发送Range为0- 的范围只是为了获取视频大小，之后就中断连接了
                }
            } else {
                ResponseUtil.writerErrorMsg(response, "500", "文件不存在!");
            }

        } catch (Exception e) {
            log.error("文件传输错误", e);
        } finally {
            FileUtil.closeStream(targetFile, outputStream);
        }
    }

    @Override
    public ServerResponse<String> playLog(PlayLogVO logVO) {
        try {

            User user = SecurityUtil.getCurrentUser();

            PlayLog log = logMapper.selectByUserIdAndMd5(user.getId(), logVO.getMd5());
            if (log != null) {
                BeanUtil.copyProperties(logVO, log);
                LambdaUpdateWrapper<PlayLog> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(PlayLog::getMd5, log.getMd5())
                        .eq(PlayLog::getUserId, log.getUserId());
                logMapper.update(log, wrapper);
            } else {
                log = new PlayLog();
                BeanUtil.copyProperties(logVO, log);
                log.setUserId(user.getId());
                log.setLogDate(new Date());
                logMapper.insert(log);
            }
            return ServerResponse.success("success!");
        } catch (Exception e) {
            log.error("存储播放记录失败", e);
            return ServerResponse.fail("500", "系统异常!");
        }
    }


}
