package com.booker.lsp.constant;

/**
 * @Author BookerLiu
 * @Date 2022/12/6 10:29
 * @Description token key
 **/
public class TokenConst {


    /**
     * 默认验证 key
     */
    public final static String DEFULT_KEY = ":token";
    //验证token 超时时间
    public final static int DEFAULT_TIMEOUT = 60 * 60 * 2;
    /**
     * 设置最大超时 key
     */
    public final static String MAX_TIMEOUT_KEY = ":maxTokenTimeOut";
    //最大超时时间
    public final static int MAX_TIMEOUT = 60 * 60 * 24;

    /**
     * 下载 key
     * 下载时单独使用key 每个用户每个文件单独一个key
     */
    public final static String DOWN_KEY = ":downToken";
    //下载token 超时时间
    public final static int DOWN_TIMEOUT = 60 * 60 * 24 * 3;

}
