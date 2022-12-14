package com.booker.lsp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.booker.lsp.mapper.FileInfoMapper;
import com.booker.lsp.service.AdminService;
import com.booker.lsp.vo.FileInfoVO;
import com.booker.lsp.vo.common.PageRes;
import com.booker.lsp.vo.common.ServerResponse;
import com.booker.lsp.vo.query.FileListParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author BookerLiu
 * @Date 2022/12/12 15:11
 * @Description 管理Service
 **/
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private FileInfoMapper fileInfoMapper;


    @Override
    public ServerResponse<PageRes<FileInfoVO>> fileList(FileListParam paramVO) {
        Page<FileInfoVO> page = new Page<>(paramVO.getPageNumber(), paramVO.getPageSize());

        if (StringUtils.isNotEmpty(paramVO.getFileName())) {
            paramVO.setFileName("%" + paramVO.getFileName().trim() + "%");
        }
        if (StringUtils.isNotEmpty(paramVO.getUpDateStart())) {
            paramVO.setUpDateStart(paramVO.getUpDateStart() + " 00:00:00");
        }
        if (StringUtils.isNotEmpty(paramVO.getUpDateEnd())) {
            paramVO.setUpDateEnd(paramVO.getUpDateEnd() + " 23:59:59");
        }

        Page<FileInfoVO> fileInfoVOPage = fileInfoMapper.queryFileList(page, paramVO);
        PageRes<FileInfoVO> pageRes = PageRes.fromPage(fileInfoVOPage, FileInfoVO.class);
        return ServerResponse.success(pageRes);
    }
}
