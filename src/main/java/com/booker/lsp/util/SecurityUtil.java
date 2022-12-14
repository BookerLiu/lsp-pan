package com.booker.lsp.util;

import com.booker.lsp.entity.Role;
import com.booker.lsp.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 9:57
 * @Description security工具
 **/
public class SecurityUtil {



    static boolean debug = false;

    /**
     * 获取当前登录用户
     * @return
     */
    public static User getCurrentUser() {
        if (debug) {
            User user = new User();
            user.setId("rootid");
            Role role = new Role();
            role.setName("ADMIN");
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(role);
            user.setUserRoles(userRoles);
            return user;
        }

        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 检查当前用户是否为管理员
     * @return
     */
    public static boolean checkIsAdmin() {
        User user = getCurrentUser();
        List<Role> userRoles = user.getUserRoles();
        for (Role userRole : userRoles) {
            if (userRole.getName().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
