package com.booker.lsp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booker.lsp.entity.Directory;
import com.booker.lsp.entity.User;
import com.booker.lsp.mapper.DirectoryMapper;
import com.booker.lsp.mapper.FileInfoMapper;
import com.booker.lsp.service.CloudService;
import com.booker.lsp.util.*;
import com.booker.lsp.vo.DirectoryVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.QueryCloudParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author BookerLiu
 * @Date 2022/11/29 20:27
 * @Description cloud service
 **/

@Log4j2
@Service
public class CloudServiceImpl implements CloudService {


    @Resource
    private DirectoryMapper directoryMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;


    @Override
    public PageRes<DirectoryVO> getFileListByParentID(QueryCloudParam param) {
        User user = SecurityUtil.getCurrentUser();

        Page<Directory> page = new Page<>(param.getPageNumber(), param.getPageSize());
        Page<DirectoryVO> pageRes = directoryMapper.queryFileListByParentID(page, param.getParentId(), user.getId());

        PageRes<DirectoryVO> directoryVOPageRes = PageRes.fromPage(pageRes, DirectoryVO.class);

        return directoryVOPageRes;
    }


    @Override
    public ServerResponse<DirectoryVO> createDir(String parentId, String dirName) {
        User user = SecurityUtil.getCurrentUser();

        dirName = dirName.trim();
        String path = CommonCode.getUserRootDir(user);
        if (!StringUtils.isEmpty(parentId)) {
            List<Directory> directories = directoryMapper.queryDirAllParentById(user.getId(), parentId);
            for (Directory directory : directories) {
                if (dirName.equals(directory.getFileName())) {
                    //不可创建同名目录
                    return ServerResponse.fail("200", "不可创建同名目录");
                }
                path += File.separator + directory.getFileName();
            }
        }
        FileUtil.mkdir(path);

        Directory dir = new Directory();
        dir.setCreateDate(new Date());
        dir.setFileFlag("N");
        dir.setFileName(dirName);
        dir.setParentId(StringUtils.isEmpty(parentId) ? null : parentId);
        dir.setUserId(user.getId());
        dir.setId(UUID.randomUUID().toString());

        directoryMapper.insert(dir);

        DirectoryVO vo = new DirectoryVO();
        BeanUtil.copyProperties(dir, vo);
        return ServerResponse.success(vo);
    }

    @Override
    public ServerResponse<String> rename(String id, String fileName) {

        User user = SecurityUtil.getCurrentUser();

        LambdaQueryWrapper<Directory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Directory::getId, id)
                .eq(Directory::getUserId, user.getId());
        Directory directory = directoryMapper.selectOne(wrapper);
        if (directory != null) {
            directoryMapper.updateFileNameById(user.getId(), id, fileName);
            fileInfoMapper.updateFileNameByMD5(user.getId(), directory.getMd5(), fileName);
        }

        return ServerResponse.success("修改成功!");
    }


}
