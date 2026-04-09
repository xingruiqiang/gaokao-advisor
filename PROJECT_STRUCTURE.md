# 项目结构文档

## 目录结构

```
gaokao-advisor/
│
├── README.md                    # 项目总体说明
├── QUICKSTART.md               # 快速启动指南（必读）
├── START.bat                   # Windows启动脚本
├── PROJECT_STRUCTURE.md        # 本文件 - 项目结构说明
│
├── backend/                    # 后端 Spring Boot 项目
│   │
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/gaokao/
│   │       │   │
│   │       │   ├── GaokaoAdvisorApplication.java   # 主启动类
│   │       │   │
│   │       │   ├── config/                        # 配置层
│   │       │   │   └── AiConfig.java             # AI配置（Ollama）
│   │       │   │
│   │       │   ├── controller/                    # 控制器层
│   │       │   │   └── GaokaoAiController.java   # AI接口控制器
│   │       │   │
│   │       │   ├── dto/                           # 数据传输对象
│   │       │   │   ├── AiRequest.java            # AI请求DTO
│   │       │   │   └── AiResponse.java           # AI响应DTO
│   │       │   │
│   │       │   ├── entity/                        # 实体类（JPA）
│   │       │   │   ├── University.java            # 院校实体
│   │       │   │   └── Major.java                 # 专业实体
│   │       │   │
│   │       │   └── service/                       # 服务层
│   │       │       └── GaokaoAiService.java      # AI核心服务
│   │       │
│   │       └── resources/
│   │           ├── application.yml               # Spring Boot配置
│   │           └── schema.sql                    # 数据库初始化脚本
│   │
│   ├── pom.xml                                     # Maven依赖配置
│   └── .gitignore
│
├── frontend/                   # 前端 Vue 3 项目
│   │
│   ├── src/
│   │   ├── api/                        # API接口封装
│   │   │   └── index.js               # 所有API方法
│   │   │
│   │   ├── App.vue                     # 主组件
│   │   └── main.js                     # 入口文件
│   │
│   ├── index.html                       # HTML模板
│   ├── package.json                     # npm依赖配置
│   ├── vite.config.js                   # Vite构建配置
│   └── .gitignore
│
└── data/                       # 数据库文件目录（运行时创建）
    └── gaokao.db               # SQLite数据库
```

---

## 后端详细说明

### 1. 配置层 (config/)

#### AiConfig.java
- 配置 Ollama 大模型接口
- RestTemplate Bean
- API 基础URL和默认模型配置

### 2. 控制器层 (controller/)

#### GaokaoAiController.java
提供以下REST API接口：

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| `/api/gaokao/analyze-score` | POST | 分析分数 |
| `/api/gaokao/recommend-universities` | POST | 院校推荐 |
| `/api/gaokao/recommend-majors` | POST | 专业推荐 |
| `/api/gaokao/volunteer-order` | POST | 志愿排序 |
| `/api/gaokao/comprehensive-analysis` | POST | 综合分析（一键生成） |

### 3. 服务层 (service/)

#### GaokaoAiService.java
**核心AI分析服务，包含以下方法：**

1. **analyzeScore()** - 分数分析
   - 输入：总分、省份、选科类型
   - 输出：分数段、位次、竞争力分析

2. **recommendUniversities()** - 院校推荐
   - 输入：总分、省份、选科类型、兴趣偏好
   - 输出：三梯度院校推荐（冲刺/稳妥/保底）

3. **recommendMajors()** - 专业推荐
   - 输入：总分、兴趣偏好、职业目标
   - 输出：就业导向的专业建议

4. **getVolunteerOrder()** - 志愿排序
   - 输入：院校列表、专业列表
   - 输出：志愿排序策略

5. **内部Prompt构建方法：**
   - `buildScoreAnalysisPrompt()`
   - `buildUniversityRecommendationPrompt()`
   - `buildMajorRecommendationPrompt()`
   - `buildVolunteerOrderPrompt()`

6. **callAi()** - 调用Ollama API

### 4. 实体层 (entity/)

#### University.java
院校实体类，包含以下字段：
- name: 院校名称
- level: 院校层次（985/211/双一流/普通本科）
- type: 院校类型（综合/理工/师范/财经/医药等）
- province/city: 所在地
- is_985/is_211/is_double_first_class: 院校标签
- employment_rate: 就业率
- description: 院校描述

#### Major.java
专业实体类，包含以下字段：
- name: 专业名称
- category: 专业类别（工学/理学/医学/法学等）
- degree_type: 学位类型（学士/硕士/博士）
- employment_rate: 就业率
- avg_salary: 平均薪资
- is_barrier: 是否有专业壁垒
- description: 专业描述

### 5. 数据库设计 (resources/schema.sql)

**核心表结构：**

1. **user** - 用户信息表
2. **score** - 高考成绩表
3. **university** - 院校信息表
4. **major** - 专业信息表
5. **university_major** - 院校专业关联表
6. **admission_data** - 历年录取数据表
7. **ai_advice** - AI建议记录表
8. **user_favorite** - 用户收藏表

**初始数据：**
- 10所985院校（北大、清华、复旦、上交大、浙大、南大、武大、华科、中大、川大）
- 10个热门专业（计算机、软件工程、人工智能、电子信息、临床医学、法学、金融学、会计学、数学、英语）

---

## 前端详细说明

### 1. API层 (api/index.js)

封装了所有后端API调用：

```javascript
analyzeScore(params)              // 分析分数
recommendUniversities(params)     // 院校推荐
recommendMajors(params)           // 专业推荐
volunteerOrder(params)            // 志愿排序
comprehensiveAnalysis(params)     // 综合分析
```

### 2. 主组件 (App.vue)

**UI布局：**
- **左侧面板**：信息录入表单
  - 省份选择（31个省份）
  - 选科类型（物理类/历史类/综合类）
  - 高考总分（数字输入）
  - 兴趣偏好（文本框）
  - 职业目标（文本框）
  - 操作按钮（生成方案/重置）

- **右侧面板**：AI分析结果（三个标签页）
  - 分数分析
  - 院校推荐
  - 专业推荐

- **底部面板**：张雪峰核心理念展示
  - 专业优先
  - 专业壁垒
  - 就业导向
  - 城市优先

**核心方法：**
- `handleComprehensiveAnalysis()` - 一键生成完整方案
- `handleReset()` - 重置表单

---

## AI Prompt 模板设计

### 张雪峰填报志愿理念集成

所有AI Prompt都包含以下核心原则：

1. **专业优先**
   - 好专业 > 好学校
   - 专业壁垒（医学、法学、师范、会计）
   - 就业导向（计算机、电子信息、金融）

2. **城市优先**
   - 经济发达城市（北上广深）
   - 省会城市
   - 避免偏远地区

3. **院校层次**
   - 985/211/双一流优先
   - 行业特色院校（财经、师范、医药）
   - 省属重点院校

4. **保底策略**
   - 2-3所 冲刺院校
   - 3-4所 稳妥院校
   - 2-3所 保底院校

---

## 配置文件说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8080                    # 后端服务端口

spring:
  datasource:
    url: jdbc:sqlite:./data/gaokao.db    # SQLite数据库
  jpa:
    hibernate:
      ddl-auto: update                   # 自动更新表结构

ai:
  ollama:
    base-url: http://localhost:11434/v1  # Ollama服务地址
    model: qwen2:7b                      # 使用的模型
```

### 前端配置 (vite.config.js)

```javascript
server: {
  port: 3000,                    # 前端服务端口
  proxy: {
    '/api': {
      target: 'http://localhost:8080',  # 代理到后端
      changeOrigin: true
    }
  }
}
```

---

## 依赖说明

### 后端依赖 (pom.xml)

```xml
<!-- Spring Boot Web -->
spring-boot-starter-web

<!-- JPA -->
spring-boot-starter-data-jpa

<!-- SQLite -->
sqlite-jdbc

<!-- SQLite Dialect -->
hibernate-community-dialects

<!-- Lombok -->
lombok
```

### 前端依赖 (package.json)

```json
{
  "vue": "^3.3.4",
  "element-plus": "^2.4.4",
  "axios": "^1.6.2",
  "@element-plus/icons-vue": "^2.3.1",
  "vite": "^5.0.8"
}
```

---

## 启动流程

1. **启动 Ollama 服务**
   ```bash
   ollama serve
   ollama pull qwen2:7b
   ```

2. **启动后端**
   ```bash
   cd backend
   mkdir data
   mvn spring-boot:run
   ```

3. **启动前端**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **访问系统**
   ```
   http://localhost:3000
   ```

---

## 扩展开发

### 添加新的AI分析功能

1. 在 `GaokaoAiService.java` 添加新方法
2. 在 `GaokaoAiController.java` 添加接口
3. 在 `frontend/src/api/index.js` 添加API调用
4. 在 `App.vue` 添加UI组件

### 添加数据库表

1. 在 `schema.sql` 添加表结构
2. 创建对应的 Entity 类
3. 创建 Repository 接口
4. 编写 Service 业务逻辑

### 添加新的专业/院校数据

编辑 `schema.sql`，在 INSERT 语句中添加数据。

---

## 常见问题

详见 `QUICKSTART.md` 中的"常见问题"章节。
