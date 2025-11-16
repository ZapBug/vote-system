<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="bean.User" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>投票系统 - 首页</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/svg+xml" href="assets/favicon.svg">
    <style>

        html, body {
            height: 100%;
            margin: 0;
        }
        body {
            padding-top: 60px;
            background-color: #f8f9fa;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .main-content-container {
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        .jumbotron {
            background: linear-gradient(135deg, #6f86d6, #48c6ef);
            color: white;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 800px;
            text-align: center;
        }
        .jumbotron-content {
            padding: 40px;
        }
        .jumbotron h1 {
            margin-top: 0;
            font-size: 48px;
        }
        .jumbotron .btn {
            margin: 10px 5px;
            padding: 12px 24px;
            font-size: 18px;
        }
        .footer {
            flex-shrink: 0;
            padding: 20px 0;
            background-color: #343a40;
            color: white;
            text-align: center;
        }
    </style>
</head>
<body>
<!-- 导航栏 -->
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
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
            <%
                User user = (User) session.getAttribute("user");
            %>
            <ul class="nav navbar-nav">
                <li class="active"><a href="index.jsp">首页</a></li>
                <li><a href="<%=request.getContextPath()%>/vote_do">投票</a></li>
                <%
                    if (user != null && "admin".equals(user.getUserRole())) {
                %>
                <li><a href="<%=request.getContextPath()%>/admin/manage">后台管理</a></li>
                <%
                    }
                %>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%
                    if (user != null) {
                %>
                <li><a href="#">欢迎, <%= user.getUserName() %>
                </a></li>
                <li><a href="<%=request.getContextPath()%>/logout">退出</a></li>
                <%
                } else {
                %>
                <li><a href="login.jsp">登录</a></li>
                <li><a href="register.jsp">注册</a></li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</nav>

<!-- 消息提示区被移动到这里，并修改了style -->
<div style="position: absolute; top: 70px; left: 50%; transform: translateX(-50%); width: 90%; max-width: 800px; z-index: 10;">
    <%
        String msg = (String) request.getAttribute("msg");
        if (msg != null) {
    %>
    <div class="alert alert-info alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <%= msg %>
    </div>
    <%
        }
    %>
</div>

<!-- 主内容区域 -->
<div class="container main-content-container">
    <!-- Jumbotron欢迎区 -->
    <div class="jumbotron">
        <div class="jumbotron-content">
            <h1>欢迎来到投票系统</h1>
            <br/>
            <%
                if (user != null) {
            %>
            <p><a class="btn btn-primary btn-lg" href="<%=request.getContextPath()%>/vote_do" role="button">开始投票
                &raquo;</a></p>
            <%
            } else {
            %>
            <p>
                <a class="btn btn-primary btn-lg" href="login.jsp" role="button">立即登录</a>
                <a class="btn btn-success btn-lg" href="register.jsp" role="button">注册账户</a>
            </p>
            <%
                }
            %>
        </div>
    </div>
</div>

<!-- 页脚 -->
<div class="footer">
    <p style="margin: 0;">&copy; <span id="currentYear"></span> 投票系统. All rights reserved by ZapBug</p>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>