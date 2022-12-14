package com.booker.lsp.util;

import com.booker.lsp.constant.PropConfConst;
import com.booker.lsp.entity.User;

import java.io.File;

/**
 * @Author BookerLiu
 * @Date 2022/12/9 20:19
 * @Description 公共抽取代码
 **/
public class CommonCode {


    /**
     * 获取用户根目录
     * @param user user
     * @return
     */
    public static String getUserRootDir(User user) {
        return PropConfConst.ROOT_DIR + File.separator
                + user.getUsername() + "_" + user.getId();
    }

    /**
     * 获取用户缓存目录
     * @param user user
     * @return
     */
    public static String getUserTempDir(User user) {
        return PropConfConst.TEMP_DIR + File.separator
                + user.getUsername() + "_" + user.getId();
    }
}
