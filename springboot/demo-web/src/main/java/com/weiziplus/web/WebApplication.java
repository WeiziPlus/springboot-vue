package com.weiziplus.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot启动类
 *
 * @author wanglongwei
 * @date 2020/05/28 09/35
 */
@ComponentScan("com.weiziplus")
@MapperScan("com.weiziplus")
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
