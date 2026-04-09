# 高考志愿填报AI系统 - 快速启动指南

## 🎯 系统简介

基于张雪峰高考志愿填报理念，结合AI大模型，为考生提供智能化的志愿填报建议。

**核心功能：**
- ✅ 分数智能分析（定位、位次、竞争力）
- ✅ 院校梯度推荐（冲刺/稳妥/保底）
- ✅ 专业就业导向推荐
- ✅ 志愿排序策略建议
- ✅ 一键生成完整方案

---

## 📋 前置要求

### 1. 已安装环境

- **JDK 17+**（建议使用 Oracle JDK 或 OpenJDK）
- **Maven 3.6+**
- **Node.js 16+** 和 **npm**
- **Ollama 本地大模型服务**（已安装并运行中）

### 2. Ollama 模型准备

确保已安装 Ollama 并下载模型：

```bash
# 安装 Ollama（如果还没安装）
# 访问：https://ollama.ai/download

# 拉取中文模型
ollama pull qwen2:7b

# 或者使用其他中文模型
ollama pull qwen:14b

# 启动 Ollama 服务
ollama serve
```

---

## 🚀 快速启动

### 步骤1：启动后端服务

```bash
# 进入后端目录
cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\backend

# 创建数据目录
mkdir data

# 启动 Spring Boot
mvn spring-boot:run
```

**启动成功后，会看到：**
```
========================================
高考志愿填报AI系统启动成功！
访问地址：http://localhost:8080
========================================
```

### 步骤2：启动前端服务

**打开新的 PowerShell 窗口**

```bash
# 进入前端目录
cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

**启动成功后，会看到：**
```
VITE v5.x.x  ready in xxx ms

➜  Local:   http://localhost:3000/
➜  Network: use --host to expose
```

### 步骤3：访问系统

在浏览器打开：**http://localhost:3000**

---

## 💡 使用说明

### 1. 基本信息录入

在左侧表单中填写：
- **省份**：选择考生所在省份
- **选科类型**：物理类/历史类/综合类
- **高考总分**：输入实际得分（0-750）
- **兴趣偏好**：喜欢的学科或领域
- **职业目标**：未来想从事的职业

### 2. 生成AI建议

点击 **"一键生成完整方案"** 按钮，系统会：
1. 分析分数定位和竞争力
2. 推荐冲刺/稳妥/保底院校
3. 推荐适合的专业

### 3. 查看分析结果

在右侧标签页中查看：
- **分数分析**：分数段、位次、竞争优势
- **院校推荐**：三梯度院校推荐
- **专业推荐**：就业导向的专业建议

---

## 🧠 AI 核心理念（张雪峰风格）

### 专业优先原则
- ✅ 好专业 > 好学校
- ✅ 专业壁垒优先（医学、法学、师范）
- ✅ 就业导向优先（计算机、电子信息、金融）

### 城市选择原则
- ✅ 经济发达城市（北上广深）
- ✅ 省会城市
- ❌ 避免偏远地区

### 院校层次选择
- ✅ 985/211/双一流优先
- ✅ 行业特色院校（财经、师范、医药）
- ✅ 省属重点院校

### 保底策略
- 2-3所 冲刺院校
- 3-4所 稳妥院校
- 2-3所 保底院校

---

## 🛠️ 技术架构

### 后端技术栈
- Spring Boot 3.2.4
- JPA + SQLite
- Ollama 集成（本地大模型）
- RESTful API

### 前端技术栈
- Vue 3
- Element Plus
- Vite
- Axios

### 核心功能模块
1. **GaokaoAiService**：AI分析服务
2. **GaokaoAiController**：API接口控制器
3. **数据库设计**：用户、成绩、院校、专业、录取数据

---

## 📁 项目结构

```
gaokao-advisor/
├── backend/                    # 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/gaokao/
│   │   │   │   ├── config/          # 配置类
│   │   │   │   ├── controller/      # 控制器
│   │   │   │   ├── dto/             # 数据传输对象
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── service/         # 服务层
│   │   │   │   └── GaokaoAdvisorApplication.java
│   │   │   └── resources/
│   │   │       ├── application.yml  # 配置文件
│   │   │       └── schema.sql       # 数据库脚本
│   └── pom.xml
│
├── frontend/                   # 前端
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── App.vue             # 主组件
│   │   └── main.js             # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
│
├── README.md
└── QUICKSTART.md              # 本文件
```

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

**如需修改 Ollama 配置：**
- 修改 `ai.ollama.base-url` 为你的 Ollama 服务地址
- 修改 `ai.ollama.model` 为你使用的模型名称

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

**如后端端口修改，同步更新 `target` 配置。**

---

## 🔧 常见问题

### Q1: AI 返回 "AI服务暂时不可用"

**原因**：Ollama 服务未启动或模型未下载

**解决**：
```bash
# 检查 Ollama 是否运行
ollama list

# 启动 Ollama 服务
ollama serve

# 下载模型
ollama pull qwen2:7b
```

### Q2: 启动报错 "path to './data/gaokao.db' does not exist"

**原因**：数据库目录未创建

**解决**：
```bash
cd backend
mkdir data
```

### Q3: Maven 下载依赖缓慢

**解决**：配置 Maven 镜像源

在 `~/.m2/settings.xml` 中添加：
```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

### Q4: 前端安装依赖失败

**解决**：切换 npm 镜像源

```bash
npm config set registry https://registry.npmmirror.com
npm install
```

---

## 📊 数据说明

### 初始数据

系统初始化时会自动创建测试数据：
- 10所 985 院校（北大、清华、复旦等）
- 10个热门专业（计算机、软件工程、人工智能等）

### 扩展数据

如需添加更多院校和专业数据，编辑 `schema.sql` 文件后重启后端。

---

## 🎨 界面预览

系统包含以下功能模块：

1. **信息录入面板**
   - 省份选择
   - 选科类型
   - 高考总分
   - 兴趣与职业目标

2. **AI分析结果**
   - 分数分析（分数段、位次、竞争力）
   - 院校推荐（冲刺/稳妥/保底）
   - 专业推荐（就业导向、专业壁垒）

3. **核心理念展示**
   - 张雪峰四大填报原则

---

## 📞 技术支持

如遇问题，请检查：
1. Ollama 服务是否正常运行
2. 后端是否启动（访问 http://localhost:8080）
3. 前端是否启动（访问 http://localhost:3000）
4. 浏览器控制台是否有错误信息

---

## 📝 后续优化方向

- [ ] 添加历年录取数据查询功能
- [ ] 集成更详细的专业就业数据
- [ ] 支持批量导入院校和专业数据
- [ ] 添加用户收藏和历史记录功能
- [ ] 导出志愿填报方案为PDF

---

**祝金榜题名！🎉**
