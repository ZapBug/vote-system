package controller;

import bean.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录Servlet
 * 处理用户登录请求
 */
@WebServlet("/login_do")
public class LoginServlet extends HttpServlet {

    // 将UserService对象提升为成员变量，只在Servlet初始化时创建一次
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    // 用于测试的setter方法
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 处理POST请求，执行用户登录逻辑
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户输入的登录信息
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // 输入验证
        if (userId == null || userId.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>用户ID不能为空</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>密码不能为空</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 解析用户ID
        int userID;
        try {
            userID = Integer.parseInt(userId.trim());
            if (userID <= 0) {
                request.setAttribute("msg", "<font color='red'>用户ID必须是正整数</font>");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "<font color='red'>用户ID格式不正确</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 清理密码输入
        password = password.trim();

        // 验证密码长度
        if (password.length() < 6 || password.length() > 100) {
            request.setAttribute("msg", "<font color='red'>密码长度必须在6-100个字符之间</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 使用成员变量进行登录验证
        User user = this.userService.login(userID, password);

        if (user != null) {
            // 登录成功，使用PRG模式 (Post-Redirect-Get)
            // 1. 将用户信息保存到Session中
            request.getSession().setAttribute("user", user);

            // 处理"记住我"功能
            String rememberMeParam = request.getParameter("rememberMe");
            if ("true".equals(rememberMeParam)) {
                // 创建cookie以记住用户登录
                javax.servlet.http.Cookie userIdCookie = new javax.servlet.http.Cookie("rememberedUserId", String.valueOf(user.getUserId()));
                javax.servlet.http.Cookie userNameCookie = new javax.servlet.http.Cookie("rememberedUserName", user.getUserName());
                javax.servlet.http.Cookie userRoleCookie = new javax.servlet.http.Cookie("rememberedUserRole", user.getUserRole());

                // 设置cookie有效期为30天（秒）
                userIdCookie.setMaxAge(30 * 24 * 60 * 60); // 30天
                userNameCookie.setMaxAge(30 * 24 * 60 * 60); // 30天
                userRoleCookie.setMaxAge(30 * 24 * 60 * 60); // 30天

                // 设置cookie路径为根目录，以便在应用程序中都可用
                userIdCookie.setPath("/");
                userNameCookie.setPath("/");
                userRoleCookie.setPath("/");

                // 添加cookie到响应
                response.addCookie(userIdCookie);
                response.addCookie(userNameCookie);
                response.addCookie(userRoleCookie);
            }

            // 2. 将成功消息也暂存到Session中，以便重定向后可以显示
            request.getSession().setAttribute("msg", "<font color='green'>登录成功</font>");
            // 3. 重定向到首页，这会更新浏览器地址栏，并防止刷新时重复提交
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            // 登录失败，转发回登录页面并显示错误
            request.setAttribute("msg", "<font color='red'>登录失败，用户ID或密码错误</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    /**
     * 处理GET请求，重定向到登录页面
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}