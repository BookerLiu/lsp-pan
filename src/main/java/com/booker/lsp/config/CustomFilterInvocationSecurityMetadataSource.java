package com.booker.lsp.config;

import cn.hutool.core.text.AntPathMatcher;
import com.booker.lsp.entity.Role;
import com.booker.lsp.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author BookerLiu
 * @Date 2022/11/7 10:29
 * @Description filter
 **/
@Component
public class CustomFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

    // 创建一个AntPathMatcher，主要用来实现ant风格的URL匹配。
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 从参数中提取出当前请求的URL
        String requestUrl = ((FilterInvocation) object).getRequestUrl();

        // 大量请求时不要频繁请求数据库 可以将信息缓存在Redis或者其他缓存数据库中。
        List<Role> roles = roleMenuMapper.getRoleByMenuUri(requestUrl);

        if (roles != null && !roles.isEmpty()) {
            return  roles.stream()
                    .map(role -> new SecurityConfig(role.getName().trim()))
                    .collect(Collectors.toList());
        }

        // 如果当前请求的URL不存在相应的模式，就假设该请求登录后即可访问，即直接返回 ROLE_LOGIN.
        return SecurityConfig.createList("login");
    }

    // 该方法用来返回所有定义好的权限资源，Spring Security在启动时会校验相关配置是否正确。
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // 如果不需要校验，那么该方法直接返回null即可。
        return null;
    }

    // supports方法返回类对象是否支持校验。
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}