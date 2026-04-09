package com.example.gaokao.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/ai-config")
@CrossOrigin(origins = "*")
public class AiConfigController {
    
    private static final String CONFIG_FILE = "ai-config.json";
    private static AiConfigController instance;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private AiConfig config = new AiConfig();
    
    @PostConstruct
    public void init() {
        instance = this;
        loadConfig();
    }
    
    @GetMapping
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        AiConfig displayConfig = cloneConfig(config);
        if (displayConfig.cloud != null && displayConfig.cloud.apiKey != null) {
            displayConfig.cloud.apiKey = maskApiKey(displayConfig.cloud.apiKey);
        }
        result.put("data", displayConfig);
        return result;
    }
    
    @PostMapping
    public Map<String, Object> saveConfig(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (params.containsKey("provider")) {
                config.provider = (String) params.get("provider");
            }
            if (params.containsKey("ollama") && params.get("ollama") instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> ollamaMap = (Map<String, Object>) params.get("ollama");
                config.ollama.baseUrl = getString(ollamaMap, "baseUrl", config.ollama.baseUrl);
                config.ollama.model = getString(ollamaMap, "model", config.ollama.model);
            }
            if (params.containsKey("cloud") && params.get("cloud") instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> cloudMap = (Map<String, Object>) params.get("cloud");
                config.cloud.provider = getString(cloudMap, "provider", config.cloud.provider);
                String newKey = getString(cloudMap, "apiKey", "");
                if (newKey != null && !newKey.contains("*")) {
                    config.cloud.apiKey = newKey;
                }
                config.cloud.baseUrl = getString(cloudMap, "baseUrl", config.cloud.baseUrl);
                config.cloud.model = getString(cloudMap, "model", config.cloud.model);
            }
            saveConfigToFile();
            result.put("success", true);
            result.put("message", "Config saved, restart to take effect");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Save failed: " + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/test")
    public Map<String, Object> testConnection(@RequestBody AiConfig testConfig) {
        Map<String, Object> result = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> request = new HashMap<>();
            request.put("model", testConfig.cloud.model);
            request.put("messages", List.of(Map.of("role", "user", "content", "reply OK")));
            request.put("max_tokens", 50);
            request.put("stream", false);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (testConfig.cloud.apiKey != null && !testConfig.cloud.apiKey.isEmpty()) {
                headers.set("Authorization", "Bearer " + testConfig.cloud.apiKey);
            }
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                testConfig.cloud.baseUrl + "/chat/completions",
                HttpMethod.POST, entity, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                result.put("success", true);
                result.put("message", "Connection OK");
            } else {
                result.put("success", false);
                result.put("message", "Response error: " + response.getStatusCode());
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Connection failed: " + e.getMessage());
        }
        return result;
    }
    
    @PostMapping("/switch")
    public Map<String, Object> switchProvider(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            String provider = params.get("provider");
            if (provider == null || (!provider.equals("cloud") && !provider.equals("ollama"))) {
                result.put("success", false);
                result.put("message", "provider must be cloud or ollama");
                return result;
            }
            config.provider = provider;
            saveConfigToFile();
            result.put("success", true);
            String desc = provider.equals("cloud") ? "DeepSeek Cloud" : "Local Ollama";
            result.put("message", "Switched to: " + desc + " (effective on next call)");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Update failed: " + e.getMessage());
        }
        return result;
    }
    
    public static AiConfig getCurrentConfig() {
        return instance != null ? instance.config : new AiConfig();
    }
    
    private void loadConfig() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            config = new AiConfig();
            saveConfigToFile();
            return;
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
            JsonNode root = objectMapper.readTree(json);
            config.provider = getText(root, "provider", "ollama");
            JsonNode ollamaNode = root.get("ollama");
            if (ollamaNode != null && ollamaNode.isObject()) {
                config.ollama.baseUrl = getText(ollamaNode, "baseUrl", config.ollama.baseUrl);
                config.ollama.model = getText(ollamaNode, "model", config.ollama.model);
            }
            JsonNode cloudNode = root.get("cloud");
            if (cloudNode != null && cloudNode.isObject()) {
                config.cloud.provider = getText(cloudNode, "provider", config.cloud.provider);
                config.cloud.apiKey = getText(cloudNode, "apiKey", "");
                config.cloud.baseUrl = getText(cloudNode, "baseUrl", config.cloud.baseUrl);
                config.cloud.model = getText(cloudNode, "model", config.cloud.model);
            }
        } catch (Exception e) {
            config = new AiConfig();
        }
    }
    
    private String getText(JsonNode node, String field, String defaultValue) {
        JsonNode n = node.get(field);
        return (n != null && !n.isNull()) ? n.asText() : defaultValue;
    }
    
    private void saveConfigToFile() {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("provider", config.provider);
            Map<String, String> ollamaMap = new LinkedHashMap<>();
            ollamaMap.put("baseUrl", config.ollama.baseUrl);
            ollamaMap.put("model", config.ollama.model);
            map.put("ollama", ollamaMap);
            Map<String, String> cloudMap = new LinkedHashMap<>();
            cloudMap.put("provider", config.cloud.provider);
            cloudMap.put("apiKey", config.cloud.apiKey);
            cloudMap.put("baseUrl", config.cloud.baseUrl);
            cloudMap.put("model", config.cloud.model);
            map.put("cloud", cloudMap);
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
                bw.write(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getString(Map<String, Object> map, String key, String defaultValue) {
        Object v = map.get(key);
        return v != null ? v.toString() : defaultValue;
    }
    
    private String maskApiKey(String key) {
        if (key == null || key.length() <= 8) return "******";
        return key.substring(0, 6) + "****" + key.substring(key.length() - 4);
    }
    
    private AiConfig cloneConfig(AiConfig src) {
        AiConfig c = new AiConfig();
        c.provider = src.provider;
        c.ollama = new OllamaConfig();
        c.ollama.baseUrl = src.ollama.baseUrl;
        c.ollama.model = src.ollama.model;
        c.cloud = new CloudConfig();
        c.cloud.provider = src.cloud.provider;
        c.cloud.apiKey = maskApiKey(src.cloud.apiKey);
        c.cloud.baseUrl = src.cloud.baseUrl;
        c.cloud.model = src.cloud.model;
        return c;
    }
    
    public static class AiConfig {
        public String provider = "ollama";
        public OllamaConfig ollama = new OllamaConfig();
        public CloudConfig cloud = new CloudConfig();
    }
    
    public static class OllamaConfig {
        public String baseUrl = "http://localhost:11434/v1";
        public String model = "qwen:0.5b";
    }
    
    public static class CloudConfig {
        public String provider = "deepseek";
        public String apiKey = "";
        public String baseUrl = "https://api.deepseek.com/v1";
        public String model = "deepseek-chat";
    }
}
