package com.booker.lsp.vo.common;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 17:30
 * @Description 统一返回数据
 **/

@ApiModel("统一返回数据")
@Data
public class ServerResponse<T> {

    private String code;

    private String msg;

    private T data;

    public ServerResponse() {

    }

    public ServerResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> ServerResponse<T> success() {
        return new ServerResponse<>("200", "", null);
    }

    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<>("200", "", data);
    }

    public static <T> ServerResponse<T> fail(String code, String msg) {
        return new ServerResponse<>(code, msg, null);
    }

    public static <T> ServerResponse<T> fail(String code, String msg, T data) {
        return new ServerResponse<>(code, msg, data);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
