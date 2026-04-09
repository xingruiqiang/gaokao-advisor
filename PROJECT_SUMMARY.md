# 高考志愿填报AI系统 - 项目总结

## 🎉 项目完成情况

**创建时间：** 2026年4月8日
**开发状态：** ✅ 核心功能开发完成，待启动测试

---

## 📋 项目概述

基于张雪峰高考志愿填报理念，结合AI大模型，为考生提供智能化的志愿填报建议系统。

### 核心亮点

- ✅ **AI智能分析**：分数定位、位次估算、竞争力评估
- ✅ **梯度推荐**：冲刺/稳妥/保底三梯度院校推荐
- ✅ **专业导向**：基于就业前景、专业壁垒的专业推荐
- ✅ **一键生成**：完整填报方案一键输出
- ✅ **张雪峰理念**：专业优先、城市优先、保底策略

---

## 🏗️ 技术架构

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.4 | 核心框架 |
| JPA | - | ORM框架 |
| SQLite | - | 轻量级数据库 |
| Ollama | qwen2:7b | 本地大模型 |

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.3.4 | 前端框架 |
| Element Plus | 2.4.4 | UI组件库 |
| Vite | 5.0.8 | 构建工具 |
| Axios | 1.6.2 | HTTP客户端 |

---

## 📁 项目结构

```
gaokao-advisor/
├── backend/                    # 后端 Spring Boot
│   ├── src/main/java/com/example/gaokao/
│   │   ├── GaokaoAdvisorApplication.java
│   │   ├── config/AiConfig.java
│   │   ├── controller/GaokaoAiController.java
│   │   ├── dto/AiRequest.java
│   │   ├── dto/AiResponse.java
│   │   ├── entity/University.java
│   │   ├── entity/Major.java
│   │   └── service/GaokaoAiService.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── schema.sql
│   └── pom.xml
│
├── frontend/                   # 前端 Vue 3
│   ├── src/
│   │   ├── api/index.js
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── README.md                   # 项目总体说明
├── QUICKSTART.md              # 快速启动指南 ⭐
├── PROJECT_STRUCTURE.md       # 项目结构说明
├── PROJECT_SUMMARY.md         # 本文件
└── START.bat                  # Windows启动脚本
```

---

## 🚀 快速启动

### 前置要求
- ✅ JDK 17+
- ✅ Maven 3.6+
- ✅ Node.js 16+ 和 npm
- ✅ Ollama（本地大模型服务）

### 启动步骤

#### 1. 启动 Ollama 服务
```bash
ollama serve
ollama pull qwen2:7b
```

#### 2. 启动后端（新打开 PowerShell）
```bash
cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\backend
mkdir data
mvn spring-boot:run
```

#### 3. 启动前端（再打开 PowerShell）
```bash
cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\frontend
npm install
npm run dev
```

#### 4. 访问系统
打开浏览器访问：**http://localhost:3000**

---

## 🧠 AI 核心功能

### 1. 分数分析
**功能：** 根据高考总分、省份、选科类型，分析考生的分数定位和竞争力。

**分析维度：**
- 分数段定位（一本线/二本线相对位置）
- 位次估算
- 竞争优势分析
- 可报考院校层次

### 2. 院校推荐
**功能：** 基于分数、省份、选科类型，推荐合适的院校。

**推荐策略（张雪峰理念）：**
- **2-3所 冲刺院校**：分数略高，冲击名校
- **3-4所 稳妥院校**：分数匹配，有把握
- **2-3所 保底院校**：分数较低，确保有学上

**选择标准：**
- 优先推荐经济发达城市（北上广深、省会）
- 优先推荐985/211/双一流院校
- 优先推荐行业特色院校（财经、师范、医药）
- 避免偏远地区院校

### 3. 专业推荐
**功能：** 基于分数、兴趣偏好、职业目标，推荐适合的专业。

**推荐原则（张雪峰理念）：**

1. **专业壁垒优先**
   - 医学（临床医学、口腔医学）
   - 法学（需要司法考试）
   - 师范（需要教师资格证）
   - 会计（需要CPA等证书）

2. **就业导向优先**
   - 计算机科学与技术
   - 软件工程
   - 人工智能
   - 电子信息工程

3. **薪资水平优先**
   - 金融学
   - IT相关
   - 高端制造业

4. **稳定性优先**
   - 师范类
   - 医学类
   - 国企相关专业

**避免推荐：**
- ❌ 纯理论学科（除非明确表示读研）
- ❌ 就业率低的专业
- ❌ 没有专业壁垒的通用型专业

### 4. 志愿排序
**功能：** 根据院校和专业推荐，生成志愿排序策略。

**排序策略（张雪峰理念）：**
- 专业优先于学校
- 经济发达城市优先
- 985/211/双一流优先
- 合理搭配冲刺、稳妥、保底

---

## 📊 数据库设计

### 核心表结构

| 表名 | 说明 | 初始数据 |
|------|------|---------|
| `user` | 用户信息 | - |
| `score` | 高考成绩 | - |
| `university` | 院校信息 | 10所985院校 |
| `major` | 专业信息 | 10个热门专业 |
| `university_major` | 院校专业关联 | - |
| `admission_data` | 历年录取数据 | - |
| `ai_advice` | AI建议记录 | - |
| `user_favorite` | 用户收藏 | - |

### 初始数据示例

**10所985院校：**
- 北京大学、清华大学、复旦大学、上海交通大学
- 浙江大学、南京大学、武汉大学、华中科技大学
- 中山大学、四川大学

**10个热门专业：**
- 计算机科学与技术、软件工程、人工智能
- 电子信息工程、临床医学、法学
- 金融学、会计学、数学与应用数学、英语

---

## 🎨 界面功能

### 左侧面板 - 信息录入
- 省份选择（31个省份）
- 选科类型（物理类/历史类/综合类）
- 高考总分（0-750）
- 兴趣偏好（文本框）
- 职业目标（文本框）
- **一键生成完整方案**按钮
- **重置**按钮

### 右侧面板 - AI分析结果
- **分数分析**标签页：分数定位、位次、竞争力
- **院校推荐**标签页：三梯度院校推荐
- **专业推荐**标签页：就业导向专业建议

### 底部面板 - 核心理念展示
- 专业优先
- 专业壁垒
- 就业导向
- 城市优先

---

## 🛠️ API 接口说明

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| `/api/gaokao/analyze-score` | POST | 分析分数 |
| `/api/gaokao/recommend-universities` | POST | 院校推荐 |
| `/api/gaokao/recommend-majors` | POST | 专业推荐 |
| `/api/gaokao/volunteer-order` | POST | 志愿排序 |
| `/api/gaokao/comprehensive-analysis` | POST | 综合分析（一键生成） |

---

## 📝 Prompt 模板设计

所有AI Prompt都严格遵循张雪峰填报理念，包含以下核心原则：

1. **专业优先原则**
2. **专业壁垒原则**
3. **就业导向原则**
4. **城市优先原则**
5. **保底策略原则**

具体Prompt实现见 `GaokaoAiService.java` 中的四个方法：
- `buildScoreAnalysisPrompt()`
- `buildUniversityRecommendationPrompt()`
- `buildMajorRecommendationPrompt()`
- `buildVolunteerOrderPrompt()`

---

## ⚙️ 配置说明

### 后端配置（application.yml）
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:sqlite:./data/gaokao.db

ai:
  ollama:
    base-url: http://localhost:11434/v1
    model: qwen2:7b
```

### 前端配置（vite.config.js）
```javascript
server: {
  port: 3000,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 📚 文档说明

| 文档名称 | 说明 | 重要性 |
|---------|------|--------|
| `README.md` | 项目总体说明 | ⭐⭐⭐ |
| `QUICKSTART.md` | 快速启动指南（必读） | ⭐⭐⭐⭐⭐ |
| `PROJECT_STRUCTURE.md` | 项目结构详细说明 | ⭐⭐⭐ |
| `PROJECT_SUMMARY.md` | 本文件 - 项目总结 | ⭐⭐⭐ |
| `START.bat` | Windows环境检查脚本 | ⭐⭐ |

---

## 🔧 常见问题

### Q1: AI 返回 "AI服务暂时不可用"
**解决：** 检查 Ollama 服务是否正常运行
```bash
ollama list
ollama serve
```

### Q2: 启动报错 "path to './data/gaokao.db' does not exist"
**解决：** 创建数据目录
```bash
cd backend
mkdir data
```

### Q3: Maven 下载依赖缓慢
**解决：** 配置阿里云镜像源（见 `QUICKSTART.md`）

### Q4: 前端安装依赖失败
**解决：** 切换 npm 镜像源
```bash
npm config set registry https://registry.npmmirror.com
```

---

## 📊 项目统计

### 代码文件统计
- **后端 Java 文件：** 8个
- **前端 Vue 文件：** 3个
- **配置文件：** 4个
- **文档文件：** 4个

### 功能统计
- **API 接口：** 5个
- **数据库表：** 8个
- **初始数据：**
  - 院校：10所
  - 专业：10个

### AI Prompt 统计
- **分数分析 Prompt：** 1个
- **院校推荐 Prompt：** 1个
- **专业推荐 Prompt：** 1个
- **志愿排序 Prompt：** 1个

---

## 🎯 下一步优化方向

### 短期优化
- [ ] 添加历年录取数据查询功能
- [ ] 集成更详细的专业就业数据
- [ ] 添加用户收藏和历史记录功能
- [ ] 支持批量导入院校和专业数据

### 中期优化
- [ ] 添加志愿填报方案导出（PDF/Excel）
- [ ] 支持多用户登录和数据隔离
- [ ] 添加AI建议的评价和反馈机制
- [ ] 优化AI Prompt，提高建议准确性

### 长期优化
- [ ] 集成更多大模型（GPT-4、Claude等）
- [ ] 开发移动端APP
- [ ] 添加志愿填报社区功能
- [ ] 实时同步各省录取数据

---

## 🎉 项目亮点总结

1. **AI驱动的智能分析**：基于Ollama本地大模型，保护隐私，响应快速
2. **张雪峰理念深度集成**：专业优先、城市优先、保底策略
3. **梯度推荐策略**：冲刺/稳妥/保底三梯度，科学合理
4. **一键生成方案**：用户体验友好，操作简单
5. **技术栈现代化**：Spring Boot 3 + Vue 3，前后端分离
6. **文档完善**：详细的启动指南和项目说明
7. **轻量级部署**：SQLite数据库，无需额外安装数据库服务

---

## 📞 技术支持

如遇问题，请参考：
1. `QUICKSTART.md` - 快速启动指南
2. `PROJECT_STRUCTURE.md` - 项目结构说明
3. 本文件 - 常见问题章节

---

**祝金榜题名！🎓**

*项目开发完成时间：2026年4月8日*
