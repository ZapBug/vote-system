<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page import="bean.Candidate" %>
<%@ page import="bean.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>投票 - 投票系统</title>
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
        .container {
            flex: 1;
        }
        .candidate-card {
            margin-bottom: 20px;
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }
        .candidate-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }
        .candidate-photo {
            height: 200px;
            object-fit: cover;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
        }
        .candidate-info {
            padding: 15px;
        }
        .vote-count {
            font-size: 18px;
            font-weight: bold;
            color: #007bff;
        }
        .vote-btn {
            margin-top: 10px;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
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
<%
    // 检查用户是否已登录，并获取user对象
    User user = (User) session.getAttribute("user");
    if (user == null) {
        // 如果未登录，重定向到登录页面
        response.sendRedirect("login.jsp");
        return; // 必须返回，停止执行此页面
    }
%>
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
                <li><a href="index.jsp">首页</a></li>
                <li class="active"><a href="<%=request.getContextPath()%>/vote_do">投票</a></li>
                <%-- 增加管理员入口判断 --%>
                <%
                    if (user != null && "admin".equals(user.getUserRole())) {
                %>
                <li><a href="<%=request.getContextPath()%>/admin/manage">后台管理</a></li>
                <%
                    }
                %>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%-- 直接使用顶部定义的user变量，不再重新定义 --%>
                <%
                    if (user != null) {
                %>
                <li><a href="#">欢迎, <%= user.getUserName() %>
                </a></li>
                <li><a href="<%=request.getContextPath()%>/logout">退出</a></li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <!-- 消息提示 -->
    <div class="row" style="margin-top: 20px;">
        <div class="col-md-12">
            <%-- 建议: 消息也从session获取，以适配重定向 --%>
            <%
                String msg = (String) session.getAttribute("msg");
                if (msg != null) {
                    session.removeAttribute("msg"); // 显示后即焚
            %>
            <div class="alert alert-success alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <%= msg %>
            </div>
            <%
                }
            %>
        </div>
    </div>

    <!-- 页面标题 -->
    <div class="header">
        <h1>校花校草投票中心</h1>
        <p>为您喜爱的候选人投上宝贵的一票</p>
    </div>

    <!-- 候选人列表 -->
    <div class="row">
        <%
            List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                for (Candidate candidate : candidates) {
        %>
        <div class="col-md-4">
            <div class="card candidate-card">
                <%
                    String photoPath = candidate.getCandidatePhoto();
                    if (photoPath == null || photoPath.isEmpty()) {
                        photoPath = "img/default.jpg"; // 建议使用一个通用的默认图片
                    }
                %>
                <img src="<%= photoPath %>" class="card-img-top candidate-photo"
                     alt="<%= candidate.getCandidateName() %>">
                <div class="candidate-info">
                    <h4><%= candidate.getCandidateName() %>
                    </h4>
                    <p><%= candidate.getCandidateDescription() != null ? candidate.getCandidateDescription() : "暂无描述" %>
                    </p>
                    <p class="vote-count">当前票数：<%= candidate.getVoteCount() %>
                    </p>
                    <form action="<%=request.getContextPath()%>/candidate_vote" method="post">
                        <input type="hidden" value="<%=candidate.getCandidateId()%>" name="vote_num">
                        <button type="submit" class="btn btn-primary btn-block vote-btn">为TA投票</button>
                    </form>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="col-md-12">
            <div class="alert alert-warning" role="alert">
                <h4>暂无候选人信息</h4>
                <p>请等待管理员添加候选人信息。</p>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>

<!-- 页脚 -->
<div class="footer">
    <%-- 页脚居中修复建议 --%>
    <p style="margin: 0;">&copy; <span id="currentYear"></span> 投票系统. All rights reserved by ZapBug</p>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>