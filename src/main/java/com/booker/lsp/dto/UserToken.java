package com.booker.lsp.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/12/6 10:24
 * @Description 用户用于不同验证的各种token
 **/
@Data
@ApiModel("用户用于不同验证的各种token")
public class UserToken {


    private String token;

    private String downToken;

    private String playToken;
}
