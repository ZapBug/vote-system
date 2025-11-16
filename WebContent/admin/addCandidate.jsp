<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="bean.User" %>
<%
    // 权限检查
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }

    String msg = (String) session.getAttribute("msg");
    if (msg != null) {
        session.removeAttribute("msg");
    }
%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>添加候选人 - 后台管理</title>
    <link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/svg+xml" href="<%=path%>/assets/favicon.svg">
    <style>
        body {
            padding-top: 70px;
            background-color: #f5f5f5;
        }
        .form-container {
            background-color: #fff;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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
            <a class="navbar-brand" href="<%=path%>/index.jsp">投票系统</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="<%=path%>/index.jsp">首页</a></li>
                <li><a href="<%=path%>/vote_do">投票</a></li>
                <li><a href="<%=path%>/admin/manage">后台管理</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%
                    if (currentUser != null) {
                %>
                <li><a href="#">欢迎, <%= currentUser.getUserName() %> (管理员)</a></li>
                <li><a href="<%=path%>/logout">退出</a></li>
                <% } else { %>
                <li><a href="<%=path%>/login.jsp">登录</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <% if (msg != null) { %>
            <div class="alert alert-info alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <%= msg %>
            </div>
            <% } %>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="form-container">
                <h2 class="text-center">添加候选人</h2>
                <form action="<%=path%>/admin/candidateManage" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="addCandidate">
                    <div class="form-group">
                        <label for="candidateName">候选人姓名</label>
                        <input type="text" class="form-control" id="candidateName" name="candidateName" required>
                    </div>
                    <div class="form-group">
                        <label for="candidateDescription">候选人描述</label>
                        <textarea class="form-control" id="candidateDescription" name="candidateDescription"
                                  rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="candidatePhoto">候选人照片</label>
                        <input type="file" class="form-control" id="candidatePhoto" name="candidatePhoto"
                               accept="image/*">
                        <p class="help-block">请选择候选人照片文件（支持jpg、png、gif格式）</p>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">添加候选人</button>
                    <a href="<%=path%>/admin/manage#candidateManagement"
                       class="btn btn-default btn-block">返回管理页面</a>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- JavaScript -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
    if (typeof jQuery === 'undefined') {
        document.write('<script src="<%=path%>/js/jquery-1.12.4.min.js"><\/script>');
    }
</script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
    if (typeof ($.fn.modal) === 'undefined') {
        var s = document.createElement('script');
        s.src = '<%=path%>/js/bootstrap.min.js';
        document.head.appendChild(s);
    }
</script>

</body>
</html>