package com.example.peach;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan(basePackages = "com.example.peach.dao")
public class PeachoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeachoneApplication.class, args);
        }

}
