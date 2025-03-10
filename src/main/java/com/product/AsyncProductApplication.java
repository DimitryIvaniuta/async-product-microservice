package com.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  // Enable asynchronous processing
public class AsyncProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncProductApplication.class, args);
    }

}