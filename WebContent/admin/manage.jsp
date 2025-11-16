<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="bean.Candidate" %>
<%@ page import="bean.User" %>
<%@ page import="bean.VoteRecord" %>
<%@ page import="java.util.List" %>
<% String path = request.getContextPath(); %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>后台管理 - 投票系统</title>
    <link href="<%=path%>/css/bootstrap.min.css" rel="stylesheet">
    <link rel="icon" type="image/svg+xml" href="<%=path%>/assets/favicon.svg">
    <style>
        body {
            padding-top: 70px; /* 为固定的导航栏留出足够空间 */
            background-color: #f5f5f5;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .main-container {
            flex: 1;
        }
        .footer {
            padding: 20px 0;
            background-color: #222; /* 使用 Bootstrap 3 的深色 */
            color: #9d9d9d;
            text-align: center;
            margin-top: 40px;
        }
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        .vote-count {
            font-weight: bold;
            color: #337ab7; /* Bootstrap 3 的主色调 */
        }
        .tab-content {
            padding: 20px;
            border: 1px solid #ddd;
            border-top: none;
            background-color: #fff;
            border-radius: 0 0 4px 4px;
        }
    </style>
</head>
<body>
<!-- Bootstrap 3 导航栏 -->
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
                <li class="active"><a href="<%=path%>/admin/manage">后台管理</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <%
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                %>
                <li><a href="#">欢迎, <%= user.getUserName() %> (管理员)</a></li>
                <li><a href="<%=path%>/logout">退出</a></li>
                <% } else { %>
                <li><a href="<%=path%>/login.jsp">登录</a></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<!-- [修复] 主容器包裹所有页面主体内容 -->
<div class="container main-container">
    <!-- 消息提示 -->
    <div class="row">
        <div class="col-md-12">
            <%
                String msg = (String) session.getAttribute("msg");
                if (msg != null) {
                    session.removeAttribute("msg");
            %>
            <div class="alert alert-info alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <%= msg %>
            </div>
            <% } %>
        </div>
    </div>

    <div class="header">
        <h1>后台管理系统</h1>
        <p>查看投票结果和管理系统</p>
    </div>

    <%
        // 获取所有数据，供各个tab使用
        List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates");
        List<User> users = (List<User>) request.getAttribute("users");
        List<VoteRecord> voteRecords = (List<VoteRecord>) request.getAttribute("voteRecords");
    %>

    <!-- Bootstrap 3 标签页导航 -->
    <ul class="nav nav-tabs">
        <li class="active"><a href="#voteResults" data-toggle="tab">投票结果</a></li>
        <li><a href="#userManagement" data-toggle="tab">用户管理</a></li>
        <li><a href="#candidateManagement" data-toggle="tab">候选人管理</a></li>
        <li><a href="#voteRecords" data-toggle="tab">投票记录</a></li>
    </ul>

    <div class="tab-content">
        <!-- 投票结果统计 -->
        <div class="tab-pane fade in active" id="voteResults">
            <h4>投票结果统计</h4>
            <%
                if (candidates != null && !candidates.isEmpty()) {
                    int totalVotes = 0;
                    for (Candidate candidate : candidates) {
                        totalVotes += candidate.getVoteCount();
                    }
            %>
            <p><strong>总票数：</strong><span class="vote-count" style="font-size: 1.5em;"><%= totalVotes %></span></p>
            <div class="action-buttons text-right" style="margin-bottom: 15px;">
                <form action="<%=path%>/admin/candidateManage" method="post" style="display: inline;"
                      onsubmit="return confirm('确定要重置所有候选人的票数吗？这将删除所有投票记录！')">
                    <input type="hidden" name="action" value="resetAllVoteCounts">
                    <button type="submit" class="btn btn-warning">重置所有票数</button>
                </form>
            </div>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>候选人</th>
                        <th>描述</th>
                        <th>票数</th>
                        <th>得票率</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (Candidate candidate : candidates) {
                            double percentage = totalVotes > 0 ? (candidate.getVoteCount() * 100.0 / totalVotes) : 0;
                    %>
                    <tr>
                        <td><%= candidate.getCandidateName() %></td>
                        <td><%= candidate.getCandidateDescription() != null ? candidate.getCandidateDescription() : "暂无描述" %></td>
                        <td><span class="vote-count"><%= candidate.getVoteCount() %></span></td>
                        <td><%= String.format("%.2f", percentage) %>%</td>
                        <td>
                            <form action="<%=path%>/admin/candidateManage" method="post" style="display: inline;"
                                  onsubmit="return confirm('确定要重置该候选人的票数吗？')">
                                <input type="hidden" name="action" value="resetVoteCount">
                                <input type="hidden" name="candidateId" value="<%= candidate.getCandidateId() %>" />
                                <button type="submit" class="btn btn-warning btn-sm">重置票数</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <div class="action-buttons text-center">
                <a href="<%=path%>/vote_do" class="btn btn-primary">返回投票页面</a>
                <a href="<%=path%>/admin/manage" class="btn btn-default">刷新数据</a>
            </div>
            <% } else { %>
            <div class="alert alert-warning">暂无候选人信息。</div>
            <% } %>
        </div>

        <!-- 用户管理 -->
        <div class="tab-pane fade" id="userManagement">
            <h4>用户管理</h4>
            <%
                if (users != null && !users.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>用户名</th>
                        <th>年龄</th>
                        <th>性别</th>
                        <th>角色</th>
                        <th>注册时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (User u : users) { %>
                    <tr>
                        <td><%= u.getUserId() %></td>
                        <td><%= u.getUserName() %></td>
                        <td><%= u.getUserAge() %></td>
                        <td><%= u.getUserSex() %></td>
                        <td><%= u.getUserRole() %></td>
                        <td><%= u.getRegisterTime() != null ? u.getRegisterTime().toString().substring(0, 19) : "未知" %></td>
                        <td>
                            <% if (!"admin".equals(u.getUserRole())) { %>
                            <form action="manage_do.jsp" method="post" style="display: inline;"
                                  onsubmit="return confirm('确定删除用户 <%= u.getUserName() %> 吗？')">
                                <input type="hidden" name="action" value="deleteUser">
                                <input type="hidden" name="userId" value="<%= u.getUserId() %>" />
                                <button type="submit" class="btn btn-danger btn-sm">删除</button>
                            </form>
                            <% } else { %>
                            <span class="text-muted">不可删除</span>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-warning">暂无用户信息。</div>
            <% } %>
        </div>

        <!-- 候选人管理 -->
        <div class="tab-pane fade" id="candidateManagement">
            <h4>候选人管理</h4>
            <div class="action-buttons text-right" style="margin-bottom: 15px;">
                <a href="addCandidate.jsp" class="btn btn-primary">添加候选人</a>
            </div>
            <% if (candidates != null && !candidates.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>姓名</th>
                        <th>描述</th>
                        <th>票数</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Candidate candidate : candidates) { %>
                    <tr>
                        <td><%= candidate.getCandidateId() %></td>
                        <td><%= candidate.getCandidateName() %></td>
                        <td><%= candidate.getCandidateDescription() != null && !candidate.getCandidateDescription().isEmpty() ? candidate.getCandidateDescription() : "暂无描述" %></td>
                        <td><%= candidate.getVoteCount() %></td>
                        <td>
                            <button class="btn btn-info btn-sm"
                                    onclick="editCandidate(<%= candidate.getCandidateId() %>, '<%= candidate.getCandidateName() %>', '<%= candidate.getCandidateDescription() != null ? candidate.getCandidateDescription().replace("\"", "\\\"") : "" %>', '<%= candidate.getCandidatePhoto() != null ? candidate.getCandidatePhoto() : "" %>')">
                                编辑
                            </button>
                            <form action="<%=path%>/admin/candidateManage" method="post" style="display: inline;"
                                  onsubmit="return confirm('确定删除候选人 <%= candidate.getCandidateName() %> 吗？')">
                                <input type="hidden" name="action" value="deleteCandidate">
                                <input type="hidden" name="candidateId" value="<%= candidate.getCandidateId() %>" />
                                <button type="submit" class="btn btn-danger btn-sm">删除</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-warning">暂无候选人信息。</div>
            <% } %>
        </div>

        <!-- 投票记录 -->
        <div class="tab-pane fade" id="voteRecords">
            <h4>投票记录</h4>
            <%
                if (voteRecords != null && !voteRecords.isEmpty()) {
            %>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>记录ID</th>
                        <th>用户ID</th>
                        <th>候选人ID</th>
                        <th>投票时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (VoteRecord record : voteRecords) { %>
                    <tr>
                        <td><%= record.getVoteId() %></td>
                        <td><%= record.getUserId() %></td>
                        <td><%= record.getCandidateId() %></td>
                        <td><%= record.getVoteTime() != null ? record.getVoteTime().toString().substring(0, 19) : "未知" %></td>
                        <td>
                            <form action="manage_do.jsp" method="post" style="display: inline;"
                                  onsubmit="return confirm('确定删除这条投票记录吗？')">
                                <input type="hidden" name="action" value="deleteVoteRecord">
                                <input type="hidden" name="voteId" value="<%= record.getVoteId() %>" />
                                <button type="submit" class="btn btn-danger btn-sm">删除</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-warning">暂无投票记录。</div>
            <% } %>
        </div>
    </div> <!-- .tab-content 闭合 -->
</div> <!-- [修复] .container.main-container 在这里正确地闭合 -->
<!-- [修复] 之前这里多余的 </div> 已经被删除 -->

<!-- 编辑候选人模态框 -->
<div class="modal fade" id="editCandidateModal" tabindex="-1" role="dialog" aria-labelledby="editCandidateModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="editCandidateModalLabel">编辑候选人</h4>
            </div>
            <div class="modal-body">
                <form id="editCandidateForm" action="<%=path%>/admin/candidateManage" method="post"
                      enctype="multipart/form-data">
                    <input type="hidden" name="action" value="updateCandidate">
                    <input type="hidden" id="editCandidateId" name="candidateId">
                    <input type="hidden" id="currentPhoto" name="currentPhoto">
                    <div class="form-group">
                        <label for="editCandidateName">候选人姓名</label>
                        <input type="text" class="form-control" id="editCandidateName" name="candidateName" required>
                    </div>
                    <div class="form-group">
                        <label for="editCandidateDescription">候选人描述</label>
                        <textarea class="form-control" id="editCandidateDescription" name="candidateDescription"
                                  rows="3"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="editCandidatePhoto">候选人照片</label>
                        <input type="file" class="form-control" id="editCandidatePhoto" name="candidatePhoto"
                               accept="image/*">
                        <p class="help-block">请选择候选人照片文件（支持jpg、png、gif格式），留空则不修改照片</p>
                        <p id="currentPhotoDisplay"></p>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="submitEditForm()">保存更改</button>
            </div>
        </div>
    </div>
</div>

<footer class="footer">
    <div class="container">
        <p>&copy; <span id="currentYear"></span> 投票系统. All rights reserved by ZapBug</p>
    </div>
</footer>

<!-- 把这段放在 body 结束前（你现在的位置是对的） -->
<!-- 先尝试用 CDN（若你偏好本地可换回），并提供本地回退 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
    // 如果 CDN 的 jQuery 没加载，再回退到站点本地的 jquery（路径根据你的项目）
    if (typeof jQuery === 'undefined') {
        document.write('<script src="<%=path%>/js/jquery-1.12.4.min.js"><\/script>');
    }
</script>

<!-- Bootstrap 3.3.7 from CDN 回退到本地 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
    // 如果 bootstrap 的插件方法不存在，回退到本地文件
    if (typeof ($.fn.modal) === 'undefined' || typeof ($.fn.tab) === 'undefined') {
        var s = document.createElement('script');
        s.src = '<%=path%>/js/bootstrap.min.js';
        document.head.appendChild(s);
    }
</script>

<script>
    // 填充年份（保留）
    document.getElementById("currentYear").textContent = new Date().getFullYear();

    // 编辑候选人函数
    function editCandidate(id, name, description, photo) {
        document.getElementById('editCandidateId').value = id;
        document.getElementById('editCandidateName').value = name;
        document.getElementById('editCandidateDescription').value = description;
        // 设置当前照片路径
        document.getElementById('currentPhoto').value = photo;
        // 清空文件输入框
        document.getElementById('editCandidatePhoto').value = '';
        // 显示当前照片信息
        var currentPhotoDisplayElement = document.getElementById('currentPhotoDisplay');
        if (photo && photo.trim() !== '') {
            currentPhotoDisplayElement.innerHTML = '当前照片：<img src="<%=path%>/' + photo + '" style="max-width: 100px; max-height: 100px;">';
        } else {
            currentPhotoDisplayElement.innerHTML = '当前没有照片';
        }
        $('#editCandidateModal').modal('show');
    }

    // 提交编辑表单
    function submitEditForm() {
        document.getElementById('editCandidateForm').submit();
    }

    // 防护脚本：如果 Bootstrap 的 tab/collapse 插件仍不可用（例如 bootstrap.js 仍加载失败）
    // 则用最小的 jQuery 逻辑实现 tabs 切换 & navbar 折叠（保证功能可用，便于继续调试）
    (function ($) {
        $(function () {
            var bootstrapTabWorks = typeof ($.fn.tab) !== 'undefined';
            var bootstrapCollapseWorks = typeof ($.fn.collapse) !== 'undefined';

            if (!bootstrapTabWorks) {
                // 简单实现 tab 切换（不依赖 bootstrap.js）
                $('.nav.nav-tabs a').on('click', function (e) {
                    e.preventDefault();
                    var target = $(this).attr('href');
                    // 切换 tab 标记
                    $(this).closest('li').addClass('active').siblings().removeClass('active');
                    // 切换内容面板
                    $('.tab-pane').removeClass('in active');
                    $(target).addClass('in active');
                    // update URL hash without jumping (optional)
                    if (history && history.replaceState) {
                        history.replaceState(null, null, target);
                    } else {
                        location.hash = target;
                    }
                });
            }

            if (!bootstrapCollapseWorks) {
                // 简单实现移动端 navbar 折叠（汉堡）功能（不依赖 bootstrap.js）
                $('.navbar-toggle').on('click', function () {
                    var $nav = $($(this).attr('data-target') || '#navbar');
                    $nav.toggleClass('in'); // bootstrap 3 的折叠类名为 in
                });
                // 当点击 navbar 内链接时自动收起（便于移动端体验）
                $('.navbar-nav li a').on('click', function () {
                    var $nav = $('#navbar');
                    if ($nav.hasClass('in')) {
                        $nav.removeClass('in');
                    }
                });
            }
        });
    })(jQuery);
</script>
</body>
</html>