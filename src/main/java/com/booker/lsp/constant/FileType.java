package com.booker.lsp.constant;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 15:34
 * @Description 文件类型
 **/

public enum FileType {


    VIDEO("1", "视频"),
    PIC("2", "图片"),
    TEXT("3", "文本"),
    DOC("4", "文档"),
    PKG("5", "压缩包"),
    OTHER("0", "其它");



    public String id;
    public String name;

    FileType (String id, String name) {
       this.id = id;
       this.name = name;
    }
}
