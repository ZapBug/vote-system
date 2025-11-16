<%-- JSP页面指令：设置页面语言、内容类型和编码 --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%-- 导入Java Bean类，以便在页面中使用User对象 --%>
<%@ page import="bean.User" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>登录 - 投票系统</title>
    <!-- 引入Bootstrap CSS框架，用于快速美化页面 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/svg+xml" href="assets/favicon.svg">
    <!-- 自定义CSS样式，用于实现特定布局（如垂直居中和粘性页脚） -->
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }
        body {
            padding-top: 60px; /* 为固定的导航栏留出空间 */
            background-color: #f5f5f5;
            display: flex; /* 使用Flexbox布局 */
            flex-direction: column; /* 子元素垂直排列 */
            min-height: 100vh; /* 最小高度为整个视口高度 */
        }
        .container {
            flex: 1; /* 让主内容区域占据所有剩余空间 */
            display: flex;
            align-items: center; /* 垂直居中表单 */
        }
        .form-signin {
            max-width: 330px;
            padding: 15px;
            margin: auto; /* 水平居中 */
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin .checkbox {
            font-weight: normal;
        }
        .form-signin .form-control {
            position: relative;
            height: auto;
            box-sizing: border-box;
            padding: 10px;
            font-size: 16px;
        }
        .form-signin .form-control:focus {
            z-index: 2;
        }

        .form-signin input[type="text"] {
            margin-bottom: -1px;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 0;
        }
        .form-signin input[type="password"] {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }
        .form-signin-heading {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }
        .footer {
            flex-shrink: 0; /* 防止页脚在内容多时被压缩 */
            padding: 20px 0;
            background-color: #343a40;
            color: white;
            text-align: center;
        }
    </style>
</head>
<body>
<!-- 响应式导航栏，固定在页面顶部 -->
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <!-- 移动端“汉堡”菜单按钮 -->
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.jsp">投票系统</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%-- 这是JSP的核心部分：动态内容展示 --%>
                <%
                    // 从session中获取名为"user"的对象
                    User user = (User) session.getAttribute("user");
                    // 判断用户是否已登录
                    if (user != null) {
                %>
                <!-- 如果用户已登录 (session中存在user对象)，显示欢迎和退出链接 -->
                <li><a href="#">欢迎, <%= user.getUserName() %>
                </a></li>
                <li><a href="<%=request.getContextPath()%>/logout">退出</a></li>
                <%
                } else {
                %>

                <%
                    }
                %>
            </ul>
        </div>
    </div>
</nav>

<!-- 主内容区域 -->
<div class="container">
    <!-- 登录表单，提交到 /login_do Servlet进行处理 -->
    <form class="form-signin" action="<%=request.getContextPath()%>/login_do" method="post">
        <h2 class="form-signin-heading">请登录</h2>

        <!-- 消息提示区 -->
        <%
            // 检查request中是否有错误消息 (通常由Servlet在登录失败时设置)
            String msg = (String) request.getAttribute("msg");
            if (msg != null) {
        %>
        <!-- 如果有错误消息，则显示一个红色的、可关闭的警告框 -->
        <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>
            <%= msg %>
        </div>
        <%
            }
        %>

        <!-- 用户ID输入框 -->
        <label for="userId" class="sr-only">用户ID</label>
        <input type="text" id="userId" name="userId" class="form-control" placeholder="用户ID" required autofocus>
        <br/>
        <!-- 密码输入框 -->
        <label for="password" class="sr-only">密码</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>

        <!-- 登录按钮 -->
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>

        <div class="checkbox">
            <label>
                <input type="checkbox" name="rememberMe" value="true"> 记住我
            </label>
        </div>

        <p class="text-center">
            还没有账户？<a href="register.jsp">立即注册</a>
        </p>
    </form>
</div>

<!-- 页面页脚 -->
<div class="footer">

    <p>&copy; <span id="currentYear"></span> 投票系统. All rights reserved by ZapBug</p>

</div>

<!-- 引入jQuery和Bootstrap的JavaScript文件，jQuery必须在Bootstrap之前引入 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>