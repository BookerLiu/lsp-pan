package com.booker.lsp.config;

import com.booker.lsp.vo.common.ServerResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author BookerLiu
 * @Date 2022/11/7 16:42
 * @Description 认证失败处理器
 **/
public class CustomAuthFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // 可以跳转到指定页面
        // resp.sendRedirect("/index");

        // 也可以返回一段JSON提示
        // 获取当前登录用户的信息，在登录成功后，将当前登录用户的信息一起返回给客户端
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setStatus(200);

        String msg;
        if (e instanceof LockedException) {
            msg = "账户被锁定，登录失败!";
        } else if (e instanceof BadCredentialsException) {
            msg = "账户名或密码输入错误，登录失败!";
        } else if (e instanceof DisabledException) {
            msg = "账户被禁用，登录失败!";
        } else if (e instanceof AccountExpiredException) {
            msg = "账户已过期，登录失败!";
        } else if (e instanceof CredentialsExpiredException) {
            msg = "密码已过期，登录失败!";
        } else if (e instanceof UsernameNotFoundException) {
            msg = "账户不存在，登录失败!";
        } else {
            msg = "登录失败!";
        }

        ServerResponse<Object> fail = ServerResponse.fail("401", msg);
        out.write(fail.toString());
        out.flush();
        out.close();
    }
}
