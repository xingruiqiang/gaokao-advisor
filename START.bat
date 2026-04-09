@echo off
chcp 65001 >nul
echo ========================================
echo 高考志愿填报AI系统 - 快速启动脚本
echo ========================================
echo.

echo [1/3] 检查 Java 环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java 未安装，请先安装 JDK 17+
    pause
    exit /b 1
)
echo ✅ Java 环境正常
echo.

echo [2/3] 检查 Node.js 环境...
node -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Node.js 未安装，请先安装 Node.js 16+
    pause
    exit /b 1
)
echo ✅ Node.js 环境正常
echo.

echo [3/3] 检查 Ollama 服务...
curl -s http://localhost:11434/api/tags >nul 2>&1
if errorlevel 1 (
    echo ⚠️  Ollama 服务未运行，请先启动 Ollama
    echo    运行命令：ollama serve
    pause
    exit /b 1
)
echo ✅ Ollama 服务正常
echo.

echo ========================================
echo 环境检查完成！
echo ========================================
echo.

echo 请按以下步骤启动系统：
echo.
echo 【步骤1】启动后端服务（新打开一个 PowerShell）
echo cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\backend
echo mkdir data
echo mvn spring-boot:run
echo.
echo 【步骤2】启动前端服务（再打开一个 PowerShell）
echo cd C:\Users\lvping\WorkBuddy\Claw\gaokao-advisor\frontend
echo npm install
echo npm run dev
echo.
echo 【步骤3】在浏览器访问
echo http://localhost:3000
echo.

echo ========================================
echo 按任意键打开详细文档...
pause >nul
start "" "QUICKSTART.md"
