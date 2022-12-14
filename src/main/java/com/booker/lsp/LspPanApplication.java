package com.booker.lsp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.booker.lsp.mapper")
public class LspPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LspPanApplication.class, args);
    }

}
