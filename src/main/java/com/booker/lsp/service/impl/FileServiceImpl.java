package com.booker.lsp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.booker.lsp.constant.FileType;
import com.booker.lsp.constant.PropConfConst;
import com.booker.lsp.constant.TokenConst;
import com.booker.lsp.entity.*;
import com.booker.lsp.mapper.ChunkFileMapper;
import com.booker.lsp.mapper.ChunkInfoMapper;
import com.booker.lsp.mapper.DirectoryMapper;
import com.booker.lsp.mapper.FileInfoMapper;
import com.booker.lsp.service.FileService;
import com.booker.lsp.util.*;
import com.booker.lsp.vo.DeleteFileVO;
import com.booker.lsp.vo.FileUpInfo;
import com.booker.lsp.vo.common.ServerResponse;
import com.sun.deploy.net.URLEncoder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ws.schild.jave.info.MultimediaInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 17:55
 * @Description 文件上传
 **/

@Log4j2
@Service
public class FileServiceImpl implements FileService {


    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private ChunkInfoMapper chunkInfoMapper;

    @Resource
    private ChunkFileMapper chunkFileMapper;

    @Resource
    private DirectoryMapper directoryMapper;


    @Override
    public ServerResponse<JSONObject> checkFileExist(String md5) {
        User user = SecurityUtil.getCurrentUser();

        JSONObject json = new JSONObject();
        json.put("exist", "N");
        json.put("cutExist", "N");
        json.put("cutNum", 0);
        Integer count = fileInfoMapper.queryCountByMd5AndUser(md5, null);
        if (count != null && count > 0) {
            json.put("exist", "Y");
        } else {
            ChunkFile chunkFile = chunkFileMapper.queryLastChunkFile(md5, user.getId());
            if (chunkFile != null) {
                json.put("cutExist", "Y");
                json.put("cutNum", chunkFile.getNum());
            }
        }
        return ServerResponse.success(json);
    }


    //合并分块文件
    @Override
    public ServerResponse<String> mergeChunkFile(String md5) throws Exception {
        RandomAccessFile readRAF = null;
        RandomAccessFile writeRAF = null;

        String targetFilePath = null;
        try {
            User user = SecurityUtil.getCurrentUser();
            ChunkInfo chunkInfo = chunkInfoMapper.queryChunkInfoByMd5(md5, user.getId());

            if (chunkInfo != null) {
                List<ChunkFile> chunkFileList = chunkInfo.getChunkFileList();
                ChunkFile chunkFile = chunkFileList.get(chunkFileList.size() - 1);
                if (chunkFile.getNum() + 1 == chunkInfo.getChunkCount()) {
                    //合并文件
                    String fileName = chunkInfo.getFileName();
                    String userDir = CommonCode.getUserTempDir(user);
                    if (!StringUtils.isEmpty(chunkInfo.getDirId())) {
                        List<Directory> directories = directoryMapper.queryDirAllParentById(user.getId(), chunkInfo.getDirId());
                        for (Directory directory : directories) {
                            userDir += File.separator + directory.getFileName();
                        }
                    }

                    if (!FileUtil.exist(userDir)) FileUtil.mkdir(userDir);

                    targetFilePath = userDir + File.separator +
                            FileUtil.getNoSufName(fileName) + "_"
                            + UUID.randomUUID().toString() + "." + FileUtil.getSuffix(fileName);

                    File targetFile = new File(targetFilePath);
                    targetFile.createNewFile();
                    //用于写文件
                    writeRAF = new RandomAccessFile(targetFile, "rw");
                    //指针指向文件顶端
                    writeRAF.seek(0);
                    //缓冲区
                    byte[] b = new byte[1024];

                    //合并文件
                    File cFile = null;
                    for (ChunkFile fileInfo : chunkFileList) {
                        cFile = new File(fileInfo.getPath());
                        readRAF = new RandomAccessFile(cFile, "rw");
                        int len = -1;
                        while ((len = readRAF.read(b)) != -1) {
                            writeRAF.write(b, 0, len);
                        }
                        readRAF.close();
                    }
                    writeRAF.close();


                    //添加至文件信息表
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(fileName);
                    fileInfo.setMd5(chunkInfo.getMd5());
                    fileInfo.setPath(targetFilePath);
                    fileInfo.setUpDate(new Date());
                    fileInfo.setUserId(chunkInfo.getUserId());
                    fileInfo.setLength(chunkInfo.getLength());
//                    fileInfo.setCustomTypeId(chunkInfo.getCustomTypeId());
                    fileInfo.setBrief(chunkInfo.getBrief());
                    fileInfo.setFormat(FileUtil.getSuffix(targetFilePath));
                    fileInfo.setDirId(chunkInfo.getDirId());
                    fileInfo.setShare(chunkInfo.getShare());

                    fileInfo.setFileType(FileType.OTHER.id);
                    //判断文件是否为音频/视频文件
                    MultimediaInfo avFile = FileUtil.isAVFile(targetFilePath);
                    if (avFile != null) {
                        fileInfo.setFileType(FileType.VIDEO.id);
                        fileInfo.setThumbnail(FileUtil.getScreenBase64(targetFilePath, 1000));
                    } else if (FileUtil.isImage(targetFile)) {
                        //图片文件
                        fileInfo.setFileType(FileType.PIC.id);
                        //转缩略图
                        fileInfo.setThumbnail(
                                FileUtil.imgToBase64String(
                                        FileUtil.resize(targetFile, 150, 100),
                                        FileUtil.getSuffix(targetFile)
                                )
                        );
                    }

                    fileInfoMapper.insert(fileInfo);


                    Directory dir = new Directory();
                    dir.setId(UUID.randomUUID().toString());
                    dir.setUserId(user.getId());
                    dir.setMd5(chunkInfo.getMd5());
                    dir.setFileName(fileName);
                    dir.setParentId(StringUtils.isEmpty(chunkInfo.getDirId()) ? null : chunkInfo.getDirId());
                    dir.setCreateDate(new Date());
                    dir.setFileFlag("Y");

                    directoryMapper.insert(dir);


                    //删除分块文件记录
                    Map<String, Object> mapParams = new HashMap<>();
                    mapParams.put("md5", md5);
                    chunkFileMapper.deleteByMap(mapParams);
                    chunkInfoMapper.deleteByMap(mapParams);

                    //删除分块文件
                    String tempDirPath = chunkFile.getPath();
                    if (tempDirPath.contains("\\")) {
                        tempDirPath = tempDirPath.substring(0, tempDirPath.lastIndexOf("\\"));
                    } else {
                        tempDirPath = tempDirPath.substring(0, tempDirPath.lastIndexOf("/"));
                    }
                    FileUtil.del(tempDirPath);


                    return ServerResponse.success("合并完成");
                } else {
                    //文件没有上传完整
                    return ServerResponse.fail("202", "文件不完整!");
                }
            } else {
                return ServerResponse.fail("201", "系统异常!");
            }
        } catch (Exception e) {
            if (targetFilePath != null) FileUtil.del(targetFilePath);

            log.error("合并文件异常", e);
            throw e;
        } finally {
            FileUtil.closeStream(readRAF, writeRAF);
        }

    }


    @Override
    public ServerResponse<String> fileUp(FileUpInfo upInfo) throws Exception {
        InputStream inputStream = null;
        try {
            User user = SecurityUtil.getCurrentUser();

            //检查文件是否已经上传过
            FileInfo fileInfo = fileInfoMapper.queryOneFileByMd5(upInfo.getMd5(), null);

            //没有上传过
            if (fileInfo == null) {
                String fileName = upInfo.getFileName();
                //分片上传
                if (upInfo.getChunkCount() > 1) {

                    //第一个分块文件携带文件信息
                    if (upInfo.getChunkNum() == 0) {
                        ChunkInfo chunkInfo = new ChunkInfo();
                        BeanUtil.copyProperties(upInfo, chunkInfo);
                        chunkInfo.setUserId(user.getId());
                        chunkInfoMapper.insert(chunkInfo);
                    }

                    String userTempDir = CommonCode.getUserTempDir(user) + File.separator + upInfo.getMd5();
                    if (!FileUtil.exist(userTempDir)) {
                        FileUtil.mkdir(userTempDir);
                    }
                    String filePath = userTempDir + File.separator + upInfo.getMd5() + "_" + upInfo.getChunkNum();
                    inputStream = upInfo.getBlob().getInputStream();
                    FileUtil.writeFromStream(inputStream, filePath);
                    inputStream.close();


                    ChunkFile chunkFile = new ChunkFile();
                    chunkFile.setMd5(upInfo.getMd5());
                    chunkFile.setNum(upInfo.getChunkNum());
//                    cutFileInfo.setCustomTypeId(upInfo.get)
                    chunkFile.setUpDate(new Date());
                    chunkFile.setPath(filePath);
                    chunkFileMapper.insert(chunkFile);
                } else {
                    //文件大小不需要分片
                    if (!FileUtil.exist(PropConfConst.ROOT_DIR)) {
                        FileUtil.mkdir(PropConfConst.ROOT_DIR);
                    }
                    String userDir = CommonCode.getUserRootDir(user);
                    if (!StringUtils.isEmpty(upInfo.getDirId())) {
                        List<Directory> directories = directoryMapper.queryDirAllParentById(user.getId(), upInfo.getDirId());
                        for (Directory directory : directories) {
                            userDir += File.separator + directory.getFileName();
                        }
                    }

                    if (!FileUtil.exist(userDir)) FileUtil.mkdir(userDir);

                    String targetFilePath = userDir + File.separator +
                            FileUtil.getNoSufName(fileName) + "_"
                            + UUID.randomUUID().toString() + "." + FileUtil.getSuffix(fileName);

                    inputStream = upInfo.getBlob().getInputStream();
                    FileUtil.writeFromStream(inputStream, targetFilePath);


                    fileInfo = new FileInfo();
                    fileInfo.setFileName(upInfo.getFileName());

                    fileInfo.setFileType(FileType.OTHER.id);
                    //判断文件是否为音频/视频文件
                    File upFile = new File(targetFilePath);
                    MultimediaInfo avFile = FileUtil.isAVFile(targetFilePath);
                    if (avFile != null) {
                        fileInfo.setFileType(FileType.VIDEO.id);
                        fileInfo.setThumbnail(FileUtil.getScreenBase64(targetFilePath, 1000));
                    } else if (FileUtil.isImage(upFile)) {
                        //图片文件
                        fileInfo.setFileType(FileType.PKG.id);
                        //转缩略图
                        fileInfo.setThumbnail(
                                FileUtil.imgToBase64String(
                                        FileUtil.resize(upFile, 150, 100),
                                        FileUtil.getSuffix(upFile)
                                )
                        );
                    }
                    fileInfo.setMd5(upInfo.getMd5());
                    fileInfo.setPath(targetFilePath);
                    fileInfo.setDirId(upInfo.getDirId());


                    fileInfo.setLength(upInfo.getLength());
                    fileInfo.setUserId(user.getId());
                    fileInfo.setUpDate(new Date());
                    fileInfo.setShare(upInfo.getShare());

                    fileInfoMapper.insert(fileInfo);

                    //插入文件夹表
                    Directory dir = new Directory();
                    dir.setId(UUID.randomUUID().toString());
                    dir.setUserId(user.getId());
                    dir.setMd5(upInfo.getMd5());
                    dir.setFileName(upInfo.getFileName());
                    dir.setParentId(StringUtils.isEmpty(upInfo.getDirId()) ? null : upInfo.getDirId());
                    dir.setCreateDate(new Date());
                    dir.setFileFlag("Y");

                    directoryMapper.insert(dir);

                }
            } else {
//                //TODO 文件已经上传过, 查看当前用户是否拥有此文件
//                int ownFileCount = fileInfoMapper.queryCountByMd5AndUser(upInfo.getMd5(), user.getId());
                //将用户上传记录指向文件
                fileInfo.setFileName(upInfo.getFileName());
                fileInfo.setUserId(user.getId());
                fileInfo.setUpDate(new Date());
                fileInfoMapper.insert(fileInfo);
            }
        } catch (Exception e) {
            log.error("文件上传异常", e);
            throw e;
        } finally {
            FileUtil.closeStream(inputStream);
        }
        return ServerResponse.success("上传成功!");
    }

    @Override
    public void downloadFile(String token,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.reset();

            //验证参数
            if (StringUtils.isEmpty(token)) {
                log.error("无权下载, token为空!");
                ResponseUtil.writerErrorMsg(response, 401, "401", "无下载权限!");
                return;
            }

//            验证token
//            username=md5=uuid
            String usernameAndMd5 = JwtTokenUtil.getOriginalStrFromToken(token);

            //验证下载token是否有效
            String redisToken = RedisUtil.get(usernameAndMd5);
            if (StringUtils.isEmpty(redisToken) || !redisToken.equals(token)) {
                log.error("下载链接过期, token: {}", token);
                ResponseUtil.writerErrorMsg(response, 401, "401", "下载链接过期!");
                return;
            }

            //获取文件MD5
            String md5 = usernameAndMd5.substring(
                    usernameAndMd5.lastIndexOf(TokenConst.DOWN_KEY)
                            + TokenConst.DOWN_KEY.length() + 1
            );

            //获取请求头中Range的值
            String range = request.getHeader(HttpHeaders.RANGE);

            FileInfo fileInfo = fileInfoMapper.queryOneFileByMd5(md5, null);

//            FileInfo fileInfo = new FileInfo();
//            fileInfo.setPath("D:\\迅雷下载\\[电影天堂www.dytt89.com]霸王别姬-1993_蓝光国粤双语中字.mp4");

            log.info("{}:请求下载:{}", usernameAndMd5, fileInfo.getPath());

            //是否下载了完整的文件
//            boolean isDone = false;

            String filePath = fileInfo.getPath();
            //打开文件
            if (FileUtil.exist(fileInfo.getPath())) {

                response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(fileInfo.getFileName(), "UTF-8"));

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

                    //最后一段文件块
//                    if (end == fileLength - 1) isDone = true;

                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(length));
                    response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);

                    //断点传输下载返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(start);
                } else {
                    //如果Range为空则下载整个视频
                    //设置文件长度
//                    isDone = true;
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                }

                //从磁盘读取数据流返回
                byte[] cache = new byte[4096];
                try {
//                    boolean firstReq = true;
                    while (requestSize > 0) {
//                        int len = targetFile.read(cache);
//                        if (requestSize < cache.length) {
//                            outputStream.write(cache, 0, (int) requestSize);
//                            outputStream.flush();
//                        } else {
//                            outputStream.write(cache, 0, len);
//                            outputStream.flush();
//                            if (len < cache.length) {
//                                break;
//                            }
//                        }
                        int len = targetFile.read(cache);
                        outputStream.write(cache, 0, len);
                        outputStream.flush();

                        // 分段下载请求时  第一次请求一般只接收文件大小  而不是下载数据
                        // 如果文件过小 客户端还没来得及关闭第一次连接  文件就输出完毕
                        // 那么就不会抛出IOException  会输出完成  删除redis中的授权
                        // 从而导致后续的下载权限不足  所以在第一次下载的时候暂停3秒
                        // 经测试  3秒可以应对大多数下载器
//                        if (requestSize == fileLength && firstReq) {
//                            Thread.sleep(3000);
//                            firstReq = false;
//                        }

                        requestSize -= len;
                    }
                    //文件下载完成  删除下载授权token
//                    if (isDone) {
//                        log.info("{} {}下载完毕, 删除授权!", fileInfo.getFileName(), usernameAndMd5);
//                        RedisUtil.delete(usernameAndMd5);
//                    }
                } catch (IOException e) {
//                    log.error("", e);
                    // tomcat原话。写操作IO异常几乎总是由于客户端主动关闭连接导致，所以直接吃掉异常打日志
                    //比如使用video播放视频时经常会发送Range为0- 的范围只是为了获取视频大小，之后就中断连接了
                }
            } else {
//                throw new RuntimeException("文件路径有误");
                ResponseUtil.writerErrorMsg(response, "500", "文件不存在!");
            }
        } catch (Exception e) {
            log.error("文件传输错误", e);
            ResponseUtil.writerErrorMsg(response, "500", "系统异常!");
        } finally {
            FileUtil.closeStream(targetFile, outputStream);
        }
    }

    /**
     * 获取下载授权token
     *
     * @param md5 文件MD5
     * @return
     */
    @Override
    public ServerResponse<String> getDownloadToken(String md5) {
        try {
            User user = SecurityUtil.getCurrentUser();
            String key = user.getUsername() + TokenConst.DOWN_KEY + ":" + md5;
            String token = RedisUtil.get(key);
            if (StringUtils.isEmpty(token)) {
                token = JwtTokenUtil.generateToken(key);
                RedisUtil.set(key, token, TokenConst.DOWN_TIMEOUT);
            }
            return ServerResponse.success(token);
        } catch (Exception e) {
            return ServerResponse.fail("500", "Redis Error!");
        }
    }


    @Override
    public ServerResponse<String> deleteFiles(DeleteFileVO vo) {
        try {

            //TODO 支持删除文件 和 pan 文件

            User user = SecurityUtil.getCurrentUser();


            //存储第一级目录和文件 用作回收站功能
            // 只修改第一级为删除状态即可
            List<String> firstLevelIdList = new ArrayList<>();
            List<Directory> firstLevelDirList = new ArrayList<>();

            //存储文件md5 //file_info表删除状态要全部改为删除  否则在video界面会查询出来
            List<String> fileMd5List = new ArrayList<>();

            //存储可真正删除的文件md5
            List<String> permanetDeleteFileList = new ArrayList<>();

            boolean permanent = "Y".equals(vo.getPermanentFlag());
            List<String> forFileMd5List;
            for (String id : vo.getIdList()) {
                List<Directory> dirList = directoryMapper.queryDirAllSonById(user.getId(), id);

                //存储要删除的第一级目录和文件
                if (!dirList.isEmpty()) {
                    firstLevelIdList.add(dirList.get(0).getId());
                    firstLevelDirList.add(dirList.get(0));
                }

                //选中了 永久删除
                if (permanent) {
                    forFileMd5List = new ArrayList<>();
                    //循环筛选
                    for (Directory directory : dirList) {
                        fileMd5List.add(directory.getMd5()); //稍后修改文件信息表
                        forFileMd5List.add(directory.getMd5()); //存储当前选中目录及子目录/文件
                    }

                    if (!forFileMd5List.isEmpty()) {
                        //查询和其它用户共有的文件
                        List<FileInfo> ownAllFileInfos = fileInfoMapper.queryOwnAllFileListByMd5List(forFileMd5List);

                        //筛选可彻底删除的文件
                        for (Directory dir : dirList) {
                            boolean dirOwnFlag = false;
                            for (FileInfo fileInfo : ownAllFileInfos) {
                                if (fileInfo.getMd5().equals(dir.getMd5())) {
                                    dirOwnFlag = true;
                                    break;
                                }
                            }
                            //只有自己拥有此文件 可以彻底删除
                            if (!dirOwnFlag) {
                                permanetDeleteFileList.add(dir.getMd5());
                            }
                        }

                    }
                }

            }


            //0 正常 1 回收站 2彻底删除
            //删除dir表记录
            String deleteStatus = permanent ? "2" : "1";
            if(!firstLevelIdList.isEmpty())
                directoryMapper.updateStatusByIds(user.getId(), firstLevelIdList, deleteStatus);
            if (!fileMd5List.isEmpty())
                fileInfoMapper.updateStatusByMd5List(user.getId(), fileMd5List, deleteStatus);

            if (permanent) {
                //先删除文件
                List<FileInfo> fileInfos = fileInfoMapper.queryFileListByMd5List(user.getId(), permanetDeleteFileList);
                for (FileInfo fileInfo : fileInfos) {
                    FileUtil.del(fileInfo.getPath());
                }
                //删除空目录
                for (Directory directory : firstLevelDirList) {
                    if ("N".equals(directory.getFileFlag())) {
                        FileUtil.deleteEmptyDirs(CommonCode.getUserRootDir(user)
                                + File.separator
                                + directory.getFileName());
                    }
                }
            }

            return ServerResponse.success("操作成功!");
        } catch (Exception e) {
            log.error("删除文件出错", e);
            return ServerResponse.fail("500", "系统异常!");
        }


    }


}
