package com.booker.lsp.service.impl;

import com.booker.lsp.entity.Role;
import com.booker.lsp.entity.User;
import com.booker.lsp.mapper.RoleMapper;
import com.booker.lsp.mapper.UserMapper;
import com.booker.lsp.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author BookerLiu
 * @Date 2022/10/28 20:16
 * @Description 用户service
 **/
@Log4j2
@Service
public class UserServiceImpl implements UserService {


    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.queryUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }
        // 我的数据库用户密码没加密，这里手动设置
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

        System.out.println("加密后的密码：" + encodePassword);

        String password = user.getPassword();

        user.setPassword(encodePassword);
        List<Role> userRoles = roleMapper.queryRolesByUserId(user.getId());
        user.setUserRoles(userRoles);
        return user;
    }
}
