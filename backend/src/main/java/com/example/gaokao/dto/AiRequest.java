package com.example.gaokao.dto;

import java.util.List;

/**
 * AI请求DTO - OpenAI Chat Completions API 格式
 */
public class AiRequest {
    
    private String model;
    private List<Message> messages;
    private Integer max_tokens;
    private Float temperature;
    private Boolean stream;
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    public Integer getMax_tokens() { return max_tokens; }
    public void setMax_tokens(Integer max_tokens) { this.max_tokens = max_tokens; }
    public Float getTemperature() { return temperature; }
    public void setTemperature(Float temperature) { this.temperature = temperature; }
    public Boolean getStream() { return stream; }
    public void setStream(Boolean stream) { this.stream = stream; }
    
    public static class Message {
        private String role;
        private String content;
        
        public Message() {}
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
