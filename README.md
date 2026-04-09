# 高考志愿填报AI软件

## 项目概述

基于张雪峰高考志愿填报理念，结合AI大模型，为考生提供智能化的志愿填报建议。

## 核心功能

### 1. 分数分析
- 输入高考总分及各科成绩
- 分数段定位
- 位次估算（基于历年数据）
- 竞争优势分析

### 2. AI志愿填报建议
- **院校推荐**：冲刺/稳妥/保底三个梯度
- **专业选择**：基于就业导向、专业壁垒、地域优势
- **志愿排序**：按照张雪峰的填报策略排序

### 3. 数据查询
- 历年录取分数线
- 各院校专业录取数据
- 就业率、薪资数据参考

## 张雪峰填报志愿核心理念

1. **专业优先**：
   - 选择好专业比选择好学校更重要
   - 专业壁垒（医学、法学、师范等）
   - 就业导向（计算机、电子信息、金融等）

2. **城市选择**：
   - 经济发达城市（北上广深）
   - 省会城市
   - 避免偏远地区

3. **院校层次**：
   - 985/211/双一流优先
   - 行业特色院校（如财经类、医药类）
   - 省属重点院校

4. **保底策略**：
   - 2-3所冲刺院校
   - 3-4所稳妥院校
   - 2-3所保底院校

## 技术架构

### 后端
- Spring Boot 3.2.4
- JPA + SQLite
- Ollama 大模型集成
- RESTful API

### 前端
- Vue 3 + Element Plus
- 响应式设计
- AI对话界面

### 数据库设计

#### 核心表结构

1. **user** - 用户信息
2. **score** - 高考成绩
3. **university** - 院校信息
4. **major** - 专业信息
5. **admission_data** - 历年录取数据
6. **ai_advice** - AI建议记录

## 使用流程

1. 输入高考成绩（总分+单科）
2. 选择省份、选科组合
3. AI分析分数定位
4. 获取院校/专业推荐
5. 查看详细分析报告
6. 导出志愿填报方案

## 快速开始

### 后端启动
```bash
cd backend
mvn spring-boot:run
```

### 前端启动
```bash
cd frontend
npm install
npm run dev
```

## AI模型配置

### 配置文件位置

```
backend/ai-config.json
```

### 配置内容说明

| 字段 | 说明 | 示例值 |
|------|------|--------|
| `provider` | 当前使用的 AI 提供商 | `"cloud"` 或 `"ollama"` |
| `ollama.baseUrl` | 本地 Ollama 服务地址 | `"http://localhost:11434/v1"` |
| `ollama.model` | 本地模型名称 | `"qwen:0.5b"` |
| `cloud.provider` | 云端提供商名称 | `"deepseek"` |
| `cloud.apiKey` | 云端 API 密钥 | `"sk-xxx..."` |
| `cloud.baseUrl` | 云端 API 地址 | `"https://api.deepseek.com/v1"` |
| `cloud.model` | 云端模型名称 | `"deepseek-chat"` |

### 配置示例

```json
{
  "provider": "cloud",
  "ollama": {
    "baseUrl": "http://localhost:11434/v1",
    "model": "qwen:0.5b"
  },
  "cloud": {
    "provider": "deepseek",
    "apiKey": "sk-your-api-key-here",
    "baseUrl": "https://api.deepseek.com/v1",
    "model": "deepseek-chat"
  }
}
```

### 切换 AI 来源

**方式一：直接编辑文件**
将 `provider` 改为 `"ollama"`（本地）或 `"cloud"`（云端），然后重启后端生效。

**方式二：前端设置面板**
访问 http://localhost:3001 → 设置 → AI配置，在界面中修改参数。

### 支持的模型

| 类型 | 模型 | 特点 |
|------|------|------|
| 云端（推荐） | `deepseek-chat` | 速度快、质量好、成本低 |
| 云端 | `gpt-4o` / `gpt-4o-mini` | OpenAI 官方模型 |
| 本地（快速） | `qwen:0.5b` | 无需联网，但质量较弱 |
| 本地（高质量） | `qwen3:8b` / `deepseek-r1:1.5b` | 需要较大内存 |

> ⚠️ 本地模型 `qwen:0.5b` 参数量较小，响应快但推荐质量不如云端模型，建议使用 DeepSeek 云端服务。

## 开发计划

- [x] 项目架构设计
- [x] 后端框架搭建
- [x] AI服务开发（支持本地Ollama + 云端DeepSeek）
- [x] 前端页面开发
- [ ] 数据库表设计
- [ ] 集成测试
