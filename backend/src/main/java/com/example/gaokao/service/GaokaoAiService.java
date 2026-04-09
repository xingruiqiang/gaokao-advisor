package com.example.gaokao.service;

import com.example.gaokao.config.AiConfig;
import com.example.gaokao.controller.AiConfigController;
import com.example.gaokao.dto.AiRequest;
import com.example.gaokao.dto.AiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 高考志愿填报AI服务
 * 大学推荐策略：Java从本地JSON数据库筛选候选名单，AI负责分析评价
 */
@Service
public class GaokaoAiService {
    
    private static final Logger log = LoggerFactory.getLogger(GaokaoAiService.class);
    
    private static final String SYSTEM_PROMPT = 
        "你是一位专业的高考志愿填报专家，名叫张雪峰助手。你的职责是根据考生分数从候选大学列表中分析并推荐，" +
        "给出冲刺/稳妥/保底三档，以及每所大学的推荐理由。必须严格从提供的候选名单中选取，禁止编造大学。";

    @Autowired
    private RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // ==================== 分析方法 ====================
    
    public String analyzeScore(Integer totalScore, String province, String selectionType) {
        String prompt = buildScoreAnalysisPrompt(totalScore, province, selectionType);
        return callAi(prompt);
    }
    
    public String recommendUniversities(Integer totalScore, String province, 
                                       String selectionType, String interests) {
        // Java直接从数据库筛选候选大学列表
        List<Map<String, Object>> candidates = loadCandidates(totalScore, province, interests);
        if (candidates.isEmpty()) {
            return "【提示】暂时没有找到适合该分数的大学数据，请换个省份或分数重试。";
        }
        String universityList = formatCandidates(candidates);
        String prompt = buildUniversityPrompt(totalScore, province, selectionType, interests, 
                                              universityList, candidates.size());
        return callAi(prompt);
    }
    
    public String recommendMajors(Integer totalScore, String interests, String careerGoals) {
        String prompt = buildMajorPrompt(totalScore, interests, careerGoals);
        return callAi(prompt);
    }
    
    public String getVolunteerOrder(String universities, String majors) {
        String prompt = buildVolunteerOrderPrompt(universities, majors);
        return callAi(prompt);
    }
    
    // ==================== 本地大学数据库 ====================
    
    private List<Map<String, Object>> loadCandidates(Integer totalScore, String province, String interests) {
        try {
            // 省份别名映射（避免中文字符编码问题）
            String mapped = Map.of(
                "山东", "unis_sd",
                "山西", "unis_sd",
                "河北", "unis_sd",
                "河南", "unis_sd",
                "北京", "unis_sd",
                "天津", "unis_sd"
            ).getOrDefault(province, "unis_sd");
            
            String fileName = mapped + ".json";
            
            // 尝试classpath读取
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                log.warn("未找到大学数据文件: {}, 尝试备用方案", fileName);
                // 备用：当前目录
                File f = new File("backend/src/main/resources/" + fileName);
                if (!f.exists()) {
                    f = new File("src/main/resources/" + fileName);
                }
                if (f.exists()) {
                    is = new FileInputStream(f);
                }
            }
            
            if (is == null) {
                log.error("无法加载大学数据文件: {}", fileName);
                return Collections.emptyList();
            }
            
            String json = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            is.close();
            
            // 解析JSON
            List<Map<String, Object>> all = objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            
            // 按分数筛选：totalScore±80分范围内的大学
            List<Map<String, Object>> candidates = new ArrayList<>();
            for (Map<String, Object> u : all) {
                Object minScoreObj = u.get("min_score");
                if (minScoreObj == null) continue;
                int minScore = ((Number) minScoreObj).intValue();
                // 录取分比考生分数低0~80分 = 有希望报考
                if (minScore >= totalScore - 80 && minScore <= totalScore + 20) {
                    candidates.add(u);
                }
            }
            
            // 按录取分数从高到低排序
            candidates.sort((a, b) -> {
                int sa = ((Number) a.getOrDefault("min_score", 0)).intValue();
                int sb = ((Number) b.getOrDefault("min_score", 0)).intValue();
                return sb - sa;
            });
            
            log.info("分数={} 筛选到 {} 所候选大学", totalScore, candidates.size());
            return candidates;
            
        } catch (Exception e) {
            log.error("读取大学数据失败: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
    
    private String formatCandidates(List<Map<String, Object>> candidates) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < candidates.size(); i++) {
            Map<String, Object> u = candidates.get(i);
            int minScore = ((Number) u.getOrDefault("min_score", 0)).intValue();
            sb.append(String.format("%d. %s|%s|%s|历年录取分约%d分|%s",
                i + 1,
                u.getOrDefault("name", ""),
                u.getOrDefault("city", ""),
                u.getOrDefault("level", ""),
                minScore,
                u.getOrDefault("description", "")
            ));
            sb.append("\n");
        }
        return sb.toString();
    }
    
    // ==================== Prompt构建 ====================
    
    private String buildScoreAnalysisPrompt(Integer totalScore, String province, String selectionType) {
        return String.format(
            "考生信息：%s省，选科：%s，高考总分：%d分\n\n" +
            "请给出：\n" +
            "1. 分数段定位：该分数在%s省属于什么批次（一本/二本），超出或低于批次线多少分\n" +
            "2. 能上什么层次的大学\n" +
            "3. 志愿填报建议（2-3条）\n\n" +
            "200字以内。",
            province, selectionType, totalScore, province);
    }
    
    private String buildUniversityPrompt(Integer totalScore, String province, 
                                          String selectionType, String interests,
                                          String universityList, int total) {
        return String.format(
            "【任务】你是高考志愿填报专家，为考生推荐大学。\n" +
            "【考生信息】省份=%s，选科=%s，总分=%d分，兴趣方向=%s\n" +
            "【候选大学列表】（共%d所，严格从中选择，禁止编造）：\n\n" +
            universityList + "\n" +
            "【输出格式】（每档必须选大学名称，理由必须结合历年分数）：\n" +
            "[冲刺]大学名称|城市|层次|推荐理由（历年录取分数及与考生分数差距）\n" +
            "[稳妥]大学名称|城市|层次|推荐理由\n" +
            "[保底]大学名称|城市|层次|推荐理由\n\n" +
            "禁止推荐不在候选列表中的大学。",
            province, selectionType, totalScore, interests, total);
    }
    
    private String buildMajorPrompt(Integer totalScore, String interests, String careerGoals) {
        return String.format(
            "推荐5个适合的专业，格式如下（每项必填）：\n\n" +
            "1. 专业名|壁垒|就业|理由\n" +
            "2. 专业名|壁垒|就业|理由\n" +
            "3. 专业名|壁垒|就业|理由\n" +
            "4. 专业名|壁垒|就业|理由\n" +
            "5. 专业名|壁垒|就业|理由\n\n" +
            "考生：总分%d，兴趣：%s，职业目标：%s\n" +
            "禁止推荐计算机、金融、管理类专业。专业必须与\"%s\"相关。",
            totalScore, interests, careerGoals, interests);
    }
    
    private String buildVolunteerOrderPrompt(String universities, String majors) {
        return String.format(
            "院校：\n%s\n\n专业：\n%s\n\n请给出志愿排序建议，100字以内。",
            universities, majors);
    }
    
    // ==================== AI调用 ====================
    
    private String callAi(String prompt) {
        AiConfigController.AiConfig aiConfig = AiConfigController.getCurrentConfig();
        
        if ("cloud".equals(aiConfig.provider)) {
            return callCloudAi(prompt, aiConfig);
        } else {
            return callOllama(prompt, aiConfig);
        }
    }
    
    private String callOllama(String prompt, AiConfigController.AiConfig config) {
        try {
            AiRequest request = new AiRequest();
            request.setModel(config.ollama.model);
            
            List<AiRequest.Message> messages = new ArrayList<>();
            messages.add(new AiRequest.Message("system", SYSTEM_PROMPT));
            messages.add(new AiRequest.Message("user", prompt));
            request.setMessages(messages);
            request.setMax_tokens(AiConfig.MAX_TOKENS);
            request.setTemperature(AiConfig.TEMPERATURE);
            request.setStream(false);
            
            String url = config.ollama.baseUrl + "/chat/completions";
            log.info("调用Ollama: {}, 模型: {}", url, config.ollama.model);
            
            AiResponse response = restTemplate.postForObject(url, request, AiResponse.class);
            
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String content = response.getChoices().get(0).getMessage().getContent();
                return content != null ? content : "AI服务暂时没有返回内容";
            }
            return "AI服务暂时不可用";
            
        } catch (RestClientException e) {
            log.error("Ollama调用失败: {}", e.getMessage());
            return handleError(e);
        }
    }
    
    private String callCloudAi(String prompt, AiConfigController.AiConfig config) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("model", config.cloud.model);
            request.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", prompt)
            ));
            request.put("max_tokens", AiConfig.MAX_TOKENS);
            request.put("temperature", AiConfig.TEMPERATURE);
            request.put("stream", false);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (config.cloud.apiKey != null && !config.cloud.apiKey.isEmpty()) {
                headers.set("Authorization", "Bearer " + config.cloud.apiKey);
            }
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            
            log.info("调用云端AI: {}, 模型: {}", config.cloud.baseUrl, config.cloud.model);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                config.cloud.baseUrl + "/chat/completions",
                HttpMethod.POST,
                entity,
                Map.class
            );
            
            if (response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                List<?> choices = (List<?>) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                    Map<?, ?> message = (Map<?, ?>) choice.get("message");
                    if (message != null) {
                        String content = (String) message.get("content");
                        return content != null ? content : "AI服务暂时没有返回内容";
                    }
                }
            }
            return "AI服务暂时不可用";
            
        } catch (HttpClientErrorException e) {
            log.error("云端API错误: {} - {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "【错误】API密钥无效或已过期，请检查配置";
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return "【错误】请求参数错误：" + e.getMessage();
            }
            return "【错误】云端API调用失败：" + e.getStatusCode();
        } catch (RestClientException e) {
            log.error("云端API连接失败: {}", e.getMessage());
            return "【错误】无法连接云端API：" + e.getMessage();
        }
    }
    
    private String handleError(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return "【错误】AI服务异常";
        if (msg.contains("Read timed out")) {
            return "【提示】AI模型响应较慢，请稍后重试";
        } else if (msg.contains("Connection refused")) {
            return "【错误】无法连接Ollama服务，请确保Ollama已启动";
        } else if (msg.contains("timeout")) {
            return "【提示】请求超时，请重试";
        }
        return "【错误】AI服务异常：" + msg;
    }
}
