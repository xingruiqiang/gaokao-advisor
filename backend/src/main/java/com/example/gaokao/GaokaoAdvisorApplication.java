package com.example.gaokao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 高考志愿填报AI应用主类
 */
@SpringBootApplication
public class GaokaoAdvisorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GaokaoAdvisorApplication.class, args);
        System.out.println("========================================");
        System.out.println("高考志愿填报AI系统启动成功！");
        System.out.println("访问地址：http://localhost:8080");
        System.out.println("========================================");
    }
}
