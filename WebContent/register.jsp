<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="bean.User" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>注册 - 投票系统</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/svg+xml" href="assets/favicon.svg">
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }
        body {
            padding-top: 60px;
            background-color: #f5f5f5;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .container {
            flex: 1;
            display: flex;
            align-items: center;
        }
        .form-registration {
            max-width: 400px;
            padding: 15px;
            margin: auto;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .form-registration .form-registration-heading {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }
        .form-registration .form-control {
            position: relative;
            height: auto;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            padding: 10px;
            font-size: 16px;
            margin-bottom: 10px;
        }
        .form-registration .form-control:focus {
            z-index: 2;
        }
        .radio-group {
            margin-bottom: 15px;
        }
        .radio-group label {
            margin-right: 15px;
            font-weight: normal;
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
            <ul class="nav navbar-nav">

            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                %>
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

<div class="container">
    <form class="form-registration" action="<%=request.getContextPath()%>/register_do" method="post">
        <h2 class="form-registration-heading">用户注册</h2>

        <!-- 消息提示 -->
        <%
            String msg = (String) request.getAttribute("msg");
            if (msg != null) {
        %>
        <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>
            <%= msg %>
        </div>
        <%
            }
        %>

        <label for="userId" class="sr-only">用户ID</label>
        <input type="text" id="userId" name="userId" class="form-control" placeholder="用户ID" required autofocus>

        <label for="username" class="sr-only">用户名</label>
        <input type="text" id="username" name="username" class="form-control" placeholder="用户名" required>

        <label for="password" class="sr-only">密码</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>

        <label for="age" class="sr-only">年龄</label>
        <input type="number" id="age" name="age" class="form-control" placeholder="年龄" min="1" max="150" required>

        <div class="radio-group">
            <label>性别：</label>
            <label class="radio-inline">
                <input type="radio" name="sex" value="M" required> 男
            </label>
            <label class="radio-inline">
                <input type="radio" name="sex" value="F" required> 女
            </label>
            <label class="radio-inline">
                <input type="radio" name="sex" value="Other" required> 其他
            </label>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit">注册</button>

        <p class="text-center">
            已有账户？<a href="login.jsp">立即登录</a>
        </p>
    </form>
</div>

<!-- 页脚 -->
<div class="footer">

    <p>&copy; <span id="currentYear"></span> 投票系统. All rights reserved by ZapBug</p>

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>

</html>