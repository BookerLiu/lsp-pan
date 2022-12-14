package com.booker.lsp.config;

import com.booker.lsp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author BookerLiu
 * @Date 2022/10/21 16:22
 * @Description security 配置
 **/

@Component
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private UserService userService;

    @Resource
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    // 指定密码的加密方式
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 配置用户及其对应的角色
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    // 配置基于数据库的 URL 访问权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setSecurityMetadataSource(accessMustRoles());
//                        object.setAccessDecisionManager(rolesCheck());
//                        return object;
//                    }
//                })
                .and().formLogin()  // 开启登录表单功能
                .loginPage("/login") // 使用自定义的登录页面，不再使用SpringSecurity提供的默认登录页
                .loginProcessingUrl("/login") // 配置登录请求处理接口，自定义登录页面、移动端登录都使用该接口
//                .usernameParameter("name") // 修改认证所需的用户名的参数名（默认为username）
//                .passwordParameter("passwd") // 修改认证所需的密码的参数名（默认为password）
                // 定义登录成功的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
                .successHandler(new CustomAuthSuccessHandler())
                // 定义登录失败的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
                .failureHandler(new CustomAuthFailHandler())
                .permitAll() // 允许访问登录表单、登录接口
                .and()
                .sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.NEVER) //关闭session
                .and().csrf().disable(); // 关闭csrf

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public CustomFilterInvocationSecurityMetadataSource accessMustRoles() {
        return new CustomFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public CustomAccessDecisionManager rolesCheck() {
        return new CustomAccessDecisionManager();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 解决authenticationManager 无法注入
        return super.authenticationManagerBean();
    }



}
