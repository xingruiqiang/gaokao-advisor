package com.example.gaokao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 根路径控制器
 * 访问 localhost:8080 时返回友好提示
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "running");
        result.put("message", "高考志愿填报AI系统后端运行正常");
        result.put("frontend", "请访问前端页面：http://localhost:3000");
        result.put("api", "API接口地址：http://localhost:8080/api/gaokao/**");
        return result;
    }
}
