package com.booker.lsp.config;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author BookerLiu
 * @Date 2022/11/21 16:14
 * @Description
 **/
@ControllerAdvice
public class ResponseBodyAnalysis implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        try {
            if (body != null) {
                String bodyStr = body.toString();
                if (bodyStr.startsWith("{") && bodyStr.endsWith("}")) {
                    //为了能够使用 JSONObject 的字段注解
                    return JSONObject.parseObject(bodyStr);
                }
            }
        } catch (Exception e) {

        }

        return body;

    }
}
