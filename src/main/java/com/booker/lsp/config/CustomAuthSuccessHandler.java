package com.booker.lsp.config;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.booker.lsp.constant.TokenConst;
import com.booker.lsp.entity.User;
import com.booker.lsp.util.FileUtil;
import com.booker.lsp.util.JwtTokenUtil;
import com.booker.lsp.util.RedisUtil;
import com.booker.lsp.vo.UserVO;
import com.booker.lsp.vo.common.ServerResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author BookerLiu
 * @Date 2022/11/7 16:27
 * @Description 认证成功处理
 **/
@Log4j2
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // 可以跳转到指定页面
        // resp.sendRedirect("/index");

        // 也可以返回一段JSON提示
        // 获取当前登录用户的信息，在登录成功后，将当前登录用户的信息一起返回给客户端
        PrintWriter out = null;
        try {

            httpServletResponse.setContentType("application/json;charset=utf-8");
            out = httpServletResponse.getWriter();

            User user = (User)authentication.getPrincipal();
            httpServletResponse.setStatus(200);
            String token = JwtTokenUtil.generateToken(user.getUsername());

            //存储token
            RedisUtil.set(user.getUsername() + TokenConst.DEFULT_KEY, token, TokenConst.DEFAULT_TIMEOUT);
            //设置token最大超时时间
            RedisUtil.set(user.getUsername() + TokenConst.MAX_TIMEOUT_KEY, token, TokenConst.MAX_TIMEOUT);

            //保存到spring security上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);

            JSONObject json = new JSONObject();
            json.put("token", token);

            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);

            json.put("principal", userVO);

            ServerResponse<JSONObject> success = ServerResponse.success(json);
            out.write(success.toString());
            out.flush();
        } catch (Exception e) {
            log.error("auth success error", e);
            ServerResponse<String> fail = ServerResponse.fail("500", "系统异常!");
            out.write(fail.toString());
            out.flush();
        } finally {
            FileUtil.closeStream(out);
        }

    }
}
