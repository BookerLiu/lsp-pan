package com.booker.lsp.util;

import com.booker.lsp.vo.common.ServerResponse;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author BookerLiu
 * @Date 2022/12/6 16:12
 * @Description resp util
 **/
@Log4j2
public class ResponseUtil {


    /**
     * 写错误日志
     * @param response response
     * @param status 状态码
     * @param code 错误码
     * @param msg 错误信息
     * @throws IOException
     */
    public static void writerErrorMsg(HttpServletResponse response, Integer status, String code, String msg) {
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            if (status != null) response.setStatus(status);
            writer.write(ServerResponse.fail(code, msg).toString());
            writer.flush();
        } catch (Exception e) {
            log.error("write msg error", e);
        }
    }

    /**
     * 写错误日志
     * @param response response
     * @param code 错误码
     * @param msg 错误信息
     * @throws IOException
     */
    public static void writerErrorMsg(HttpServletResponse response, String code, String msg) {
        writerErrorMsg(response, null, code, msg);
    }
}
