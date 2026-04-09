package com.example.gaokao.controller;

import com.example.gaokao.service.GaokaoAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 高考AI控制器
 */
@RestController
@RequestMapping("/api/gaokao")
@CrossOrigin(origins = "*")
public class GaokaoAiController {
    
    @Autowired
    private GaokaoAiService gaokaoAiService;
    
    /**
     * 分析分数
     */
    @PostMapping("/analyze-score")
    public Map<String, Object> analyzeScore(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Integer totalScore = (Integer) params.get("totalScore");
            String province = (String) params.get("province");
            String selectionType = (String) params.get("selectionType");
            
            if (totalScore == null || totalScore <= 0) {
                result.put("success", false);
                result.put("message", "总分必须大于0");
                return result;
            }
            
            String analysis = gaokaoAiService.analyzeScore(totalScore, province, selectionType);
            
            result.put("success", true);
            result.put("data", analysis);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "分析失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 院校推荐
     */
    @PostMapping("/recommend-universities")
    public Map<String, Object> recommendUniversities(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Integer totalScore = (Integer) params.get("totalScore");
            String province = (String) params.get("province");
            String selectionType = (String) params.get("selectionType");
            String interests = (String) params.getOrDefault("interests", "无特殊偏好");
            
            String recommendation = gaokaoAiService.recommendUniversities(
                totalScore, province, selectionType, interests);
            
            result.put("success", true);
            result.put("data", recommendation);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "推荐失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 专业推荐
     */
    @PostMapping("/recommend-majors")
    public Map<String, Object> recommendMajors(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Integer totalScore = (Integer) params.get("totalScore");
            String interests = (String) params.getOrDefault("interests", "无特殊偏好");
            String careerGoals = (String) params.getOrDefault("careerGoals", "暂无明确职业目标");
            
            String recommendation = gaokaoAiService.recommendMajors(
                totalScore, interests, careerGoals);
            
            result.put("success", true);
            result.put("data", recommendation);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "推荐失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 志愿排序建议
     */
    @PostMapping("/volunteer-order")
    public Map<String, Object> volunteerOrder(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String universities = (String) params.get("universities");
            String majors = (String) params.get("majors");
            
            String order = gaokaoAiService.getVolunteerOrder(universities, majors);
            
            result.put("success", true);
            result.put("data", order);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "排序失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 综合分析（一键生成完整方案）
     */
    @PostMapping("/comprehensive-analysis")
    public Map<String, Object> comprehensiveAnalysis(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Integer totalScore = (Integer) params.get("totalScore");
            String province = (String) params.get("province");
            String selectionType = (String) params.get("selectionType");
            String interests = (String) params.getOrDefault("interests", "无特殊偏好");
            String careerGoals = (String) params.getOrDefault("careerGoals", "暂无明确职业目标");
            
            // 生成完整分析报告
            Map<String, String> analysis = new HashMap<>();
            analysis.put("scoreAnalysis", gaokaoAiService.analyzeScore(totalScore, province, selectionType));
            analysis.put("universityRecommendation", 
                gaokaoAiService.recommendUniversities(totalScore, province, selectionType, interests));
            analysis.put("majorRecommendation", 
                gaokaoAiService.recommendMajors(totalScore, interests, careerGoals));
            
            result.put("success", true);
            result.put("data", analysis);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "综合分析失败：" + e.getMessage());
        }
        
        return result;
    }
}
