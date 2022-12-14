package com.booker.lsp.controller;

import com.booker.lsp.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author BookerLiu
 * @Date 2022/10/21 16:09
 * @Description
 **/
@RestController
public class TestController {

    @GetMapping("/admin/hello")
    public String admin() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }
    @GetMapping("/db/hello")
    public String db() {
        return "hello db";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString() + "=" +  UUID.randomUUID().toString().length());
    }
}
