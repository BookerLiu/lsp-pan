package com.booker.lsp.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author BookerLiu
 * @Date 2022/12/2 11:51
 * @Description yml配置信息
 **/

@Component
public class PropConfConst {

    //pan 根目录
    public static String ROOT_DIR;

    //缓存目录
    public static String TEMP_DIR;


    @Value("${fileUpConfig.rootDir}")
    public void setUserDir(String userDir) {
        ROOT_DIR = userDir;
    }

    @Value("${fileUpConfig.tempDir}")
    public void setTempDir(String tempDir) {
        TEMP_DIR = tempDir;
    }
}
