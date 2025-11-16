# 投票系统 (Vote System)



[English](README.md) | [中文](README_zh.md)



这是一个基于 Java Servlet 和 MyBatis 的 Web 投票系统，支持用户注册、登录、投票以及后台管理功能。

## 功能特性

- **用户管理**
  - 用户注册
  - 用户登录/登出
- **投票功能**
  - 查看候选人列表和当前票数
  - 进行投票
- **后台管理**
  - 查看所有投票结果
  - 用户管理
  - 候选人管理
  - 投票记录管理

## 技术栈



- **后端**: Java Servlet

- **前端**: JSP, Bootstrap

- **数据库**: MySQL

- **ORM框架**: MyBatis

- **JDK版本**: Java 8 或更高版本

## 项目结构

```
vote-system/
├── sql/                 # 数据库脚本
├── src/                 # 源代码
│   ├── bean/            # 实体类
│   ├── config/          # MyBatis配置
│   ├── controller/      # Servlet控制器
│   ├── dao/             # 数据访问对象
│   ├── filter/          # 过滤器
│   ├── mapper/          # MyBatis映射文件
│   ├── service/         # 业务逻辑层
│   ├── test/            # 测试代码
│   ├── util/            # 工具类
│   ├── db.properties    # 数据库配置文件
│   └── sqlMapConfig.xml # MyBatis主配置文件
├── WebContent/          # Web资源
│   ├── admin/           # 管理员页面
│   ├── css/             # 样式文件
│   ├── img/             # 图片资源
│   ├── js/              # JavaScript文件
│   ├── index.jsp        # 首页
│   ├── login.jsp        # 登录页
│   ├── register.jsp     # 注册页
│   ├── vote.jsp         # 投票页
│   └── WEB-INF/
└── README.md
```

## 数据库配置

在 `src/db.properties` 文件中配置数据库连接信息:

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/vote-system?useSSL=false&characterEncoding=UTF-8
username=root
password=root
```

## 安装与运行

1. 克隆项目到本地
2. 创建数据库 `vote-system`
3. 执行 `sql/database_schema.sql` 脚本创建表结构
4. 配置 `src/db.properties` 中的数据库连接信息
5. 将项目部署到支持 Servlet 的 Web 服务器（如 Tomcat）

## 安全特性

- 使用 CSP (Content Security Policy) 过滤器增强 Web 安全性

## 许可证

本项目采用 MIT 协议。

---

## 联系我

让我们一起学习，共同进步。

![微信公众号二维码](qrcode.png)
