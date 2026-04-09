-- 高考志愿填报系统数据库设计
-- SQLite 数据库

-- 1. 用户信息表
CREATE TABLE user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL,
    province VARCHAR(50) NOT NULL COMMENT '省份',
    selection_type VARCHAR(20) NOT NULL COMMENT '选科类型：物理类/历史类/综合类',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 高考成绩表
CREATE TABLE score (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_score INTEGER NOT NULL COMMENT '总分',
    chinese_score INTEGER COMMENT '语文',
    math_score INTEGER COMMENT '数学',
    english_score INTEGER COMMENT '英语',
    physics_score INTEGER COMMENT '物理',
    chemistry_score INTEGER COMMENT '化学',
    biology_score INTEGER COMMENT '生物',
    history_score INTEGER COMMENT '历史',
    geography_score INTEGER COMMENT '地理',
    politics_score INTEGER COMMENT '政治',
    exam_year INTEGER NOT NULL COMMENT '考试年份',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 3. 院校信息表
CREATE TABLE university (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '院校名称',
    level VARCHAR(20) COMMENT '层次：985/211/双一流/普通本科',
    type VARCHAR(50) COMMENT '类型：综合/理工/师范/财经/医药/农林/政法',
    province VARCHAR(50) NOT NULL COMMENT '所在省份',
    city VARCHAR(50) COMMENT '所在城市',
    is_985 BOOLEAN DEFAULT 0,
    is_211 BOOLEAN DEFAULT 0,
    is_double_first_class BOOLEAN DEFAULT 0,
    employment_rate DECIMAL(5,2) COMMENT '就业率',
    description TEXT COMMENT '院校描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. 专业信息表
CREATE TABLE major (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '专业名称',
    category VARCHAR(50) COMMENT '专业类别：工学/理学/文学/法学/医学/经济学/管理学等',
    degree_type VARCHAR(20) COMMENT '学位类型：学士/硕士/博士',
    employment_rate DECIMAL(5,2) COMMENT '就业率',
    avg_salary DECIMAL(10,2) COMMENT '平均薪资',
    is_barrier BOOLEAN DEFAULT 0 COMMENT '是否有专业壁垒',
    description TEXT COMMENT '专业描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. 院校专业关联表
CREATE TABLE university_major (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    university_id INTEGER NOT NULL,
    major_id INTEGER NOT NULL,
    is_key_major BOOLEAN DEFAULT 0 COMMENT '是否重点专业',
    enrollment_plan INTEGER COMMENT '招生计划',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (major_id) REFERENCES major(id)
);

-- 6. 历年录取数据表
CREATE TABLE admission_data (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    university_id INTEGER NOT NULL,
    major_id INTEGER,
    province VARCHAR(50) NOT NULL COMMENT '录取省份',
    year INTEGER NOT NULL COMMENT '年份',
    min_score INTEGER COMMENT '最低分',
    avg_score INTEGER COMMENT '平均分',
    max_score INTEGER COMMENT '最高分',
    min_rank INTEGER COMMENT '最低位次',
    max_rank INTEGER COMMENT '最高位次',
    enrollment_count INTEGER COMMENT '录取人数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (major_id) REFERENCES major(id)
);

-- 7. AI建议记录表
CREATE TABLE ai_advice (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    score_id INTEGER NOT NULL,
    advice_type VARCHAR(20) NOT NULL COMMENT '建议类型：院校推荐/专业选择/志愿排序',
    advice_content TEXT NOT NULL COMMENT 'AI建议内容',
    ai_model VARCHAR(50) COMMENT '使用的AI模型',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (score_id) REFERENCES score(id)
);

-- 8. 用户收藏表
CREATE TABLE user_favorite (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    university_id INTEGER,
    major_id INTEGER,
    type VARCHAR(20) NOT NULL COMMENT '收藏类型：院校/专业',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (major_id) REFERENCES major(id)
);

-- 索引优化
CREATE INDEX idx_admission_university ON admission_data(university_id);
CREATE INDEX idx_admission_province_year ON admission_data(province, year);
CREATE INDEX idx_admission_score ON admission_data(min_score, avg_score, max_score);
CREATE INDEX idx_user_score ON score(user_id);

-- 初始化一些测试数据
INSERT INTO university (name, level, type, province, city, is_985, is_211, is_double_first_class, employment_rate, description)
VALUES 
('北京大学', '985', '综合', '北京', '北京', 1, 1, 1, 98.5, '中国顶尖综合类大学'),
('清华大学', '985', '理工', '北京', '北京', 1, 1, 1, 98.3, '中国顶尖理工类大学'),
('复旦大学', '985', '综合', '上海', '上海', 1, 1, 1, 97.8, '江南第一学府'),
('上海交通大学', '985', '综合', '上海', '上海', 1, 1, 1, 98.0, '理工强校'),
('浙江大学', '985', '综合', '浙江', '杭州', 1, 1, 1, 97.5, '综合性研究型大学'),
('南京大学', '985', '综合', '江苏', '南京', 1, 1, 1, 97.2, '历史悠久的综合性大学'),
('武汉大学', '985', '综合', '湖北', '武汉', 1, 1, 1, 96.5, '珞珈山上的百年名校'),
('华中科技大学', '985', '理工', '湖北', '武汉', 1, 1, 1, 97.0, '工科强校'),
('中山大学', '985', '综合', '广东', '广州', 1, 1, 1, 96.8, '华南地区综合性大学'),
('四川大学', '985', '综合', '四川', '成都', 1, 1, 1, 96.3, '西南地区综合性大学');

INSERT INTO major (name, category, degree_type, employment_rate, avg_salary, is_barrier, description)
VALUES 
('计算机科学与技术', '工学', '学士', 96.5, 12000, 1, '热门专业，就业面广'),
('软件工程', '工学', '学士', 97.0, 13000, 1, '互联网行业急需'),
('人工智能', '工学', '学士', 98.0, 15000, 1, '前沿技术专业'),
('电子信息工程', '工学', '学士', 95.5, 11000, 1, '电子通信行业核心'),
('临床医学', '医学', '学士', 94.5, 9000, 1, '专业壁垒高，稳定'),
('法学', '法学', '学士', 92.0, 8000, 1, '专业壁垒高，需要考证'),
('金融学', '经济学', '学士', 93.5, 10000, 0, '金融行业核心'),
('会计学', '管理学', '学士', 94.0, 9500, 1, '需要专业证书'),
('数学与应用数学', '理学', '学士', 91.5, 8500, 1, '基础学科，转行容易'),
('英语', '文学', '学士', 90.0, 7500, 0, '通用语言技能');
