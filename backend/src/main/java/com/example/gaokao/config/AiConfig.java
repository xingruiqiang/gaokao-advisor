package com.example.gaokao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * AI配置类
 */
@Configuration
public class AiConfig {
    
    /**
     * Ollama API基础URL
     */
    public static final String OLLAMA_BASE_URL = "http://localhost:11434/v1";
    
    /**
     * 默认模型 - qwen:0.5b 最快
     * 可选：qwen:0.5b(最快), qwen2:7b(较快), deepseek-r1:1.5b(较慢)
     */
    public static final String DEFAULT_MODEL = "qwen:0.5b";
    
    /**
     * API超时时间（毫秒）- 单次请求90秒
     */
    public static final int TIMEOUT_MS = 90000;
    
    /**
     * 最大输出token数 - 800足够输出完整推荐
     */
    public static final int MAX_TOKENS = 800;
    
    /**
     * 温度参数
     */
    public static final float TEMPERATURE = 0.7f;
    
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(TIMEOUT_MS);
        return new RestTemplate(factory);
    }
}
