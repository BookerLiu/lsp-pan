package com.booker.lsp.vo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 11:44
 * @Description 分页参数
 **/

@Data
public class QueryPage implements Serializable {


    private int pageNumber = 1;
    private int pageSize = 10;


}
