package com.booker.lsp.config;


import com.booker.lsp.constant.AuthCodeConst;
import com.booker.lsp.constant.TokenConst;
import com.booker.lsp.service.UserService;
import com.booker.lsp.util.JwtTokenUtil;
import com.booker.lsp.util.RedisUtil;
import com.booker.lsp.util.ResponseUtil;
import com.booker.lsp.vo.common.ServerResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author BookerLiu
 * @Date 2022/11/7 16:04
 * @Description 请求鉴权
 **/
@Log4j2
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {



    @Resource
    private UserService userService;


    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            //获取请求url
            String requestURI = request.getRequestURI();

            //登录 或 下载文件不做处理
            if (requestURI.contains("login") || requestURI.contains("downloadFile")) {
                filterChain.doFilter(request, response);
                return;
            }

            //获取请求token
            String token = request.getHeader("token");
            String tokenParam = request.getParameter("token");
            if (tokenParam != null) {
                token = tokenParam;
            }

            //判断是否传入token
            if (StringUtils.isEmpty(token)) {
                ResponseUtil.writerErrorMsg(response, 401, AuthCodeConst.NO_LOGIN, "未登录!");
                return;
            }

            //根据token获取用户名
            String username = JwtTokenUtil.getOriginalStrFromToken(token);

            //获取redis token 查看是否超时
            String redisToken = RedisUtil.get(username + TokenConst.DEFULT_KEY);
            if (StringUtils.isEmpty(redisToken) || !token.equals(redisToken)) {
                //已经超时 删除
                RedisUtil.delete(username + TokenConst.MAX_TIMEOUT_KEY);
                ResponseUtil.writerErrorMsg(response, 401, AuthCodeConst.LOGIN_TIME_OUT, "登录失效!");
                return;
            }

            //如果不超过最大超时时间  更新redisToken 超时时间
            String maxTimeOutToken = RedisUtil.get(username + TokenConst.MAX_TIMEOUT_KEY);
            if (StringUtils.isNotEmpty(maxTimeOutToken)) {
                RedisUtil.expire(username + TokenConst.DEFULT_KEY, TokenConst.DEFAULT_TIMEOUT);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (username != null && authentication == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                //验证token
                if (!JwtTokenUtil.validateToken(token, userDetails)) {
                    ResponseUtil.writerErrorMsg(response, 401, AuthCodeConst.LOGIN_TIME_OUT, "鉴权失败!");
                    return;
                }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                //保存到spring security上下文中
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("jwt token 鉴权异常", e);
            int status = 401;
            String msg;
            if (e instanceof MalformedJwtException) {
                msg = "鉴权失败!";
            } else if (e instanceof ExpiredJwtException) {
                msg = "登录失效, 请重新登录!";
            } else {
                status = 500;
                msg = "系统异常!";
            }
            ResponseUtil.writerErrorMsg(response, status, String.valueOf(status), msg);
        }
    }





//
//    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
//        // parse the token.
//        try {
//            String userJson = Jwts.parserBuilder().setSigningKey(tokenKey).build()
//                    .parseClaimsJws(authorization.replace(BEARER, "")).getBody().getSubject();
//            User user = JSONObject.parseObject(userJson, User.class);
//            if (user != null) {
//                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
//            }
//        } catch (MalformedJwtException | ExpiredJwtException e) {
//            //token解析失败,token过期
//            return null;
//        }
//        return null;
//    }


}
