package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户登出Servlet
 * 处理用户登出请求
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * 处理GET请求，执行用户登出逻辑
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 清除"记住我"cookie
        javax.servlet.http.Cookie userIdCookie = new javax.servlet.http.Cookie("rememberedUserId", null);
        javax.servlet.http.Cookie userNameCookie = new javax.servlet.http.Cookie("rememberedUserName", null);

        // 设置cookie立即过期
        userIdCookie.setMaxAge(0);
        userNameCookie.setMaxAge(0);

        // 设置cookie路径以匹配它们被设置的位置
        userIdCookie.setPath("/");
        userNameCookie.setPath("/");

        // 添加cookie到响应以清除它们
        response.addCookie(userIdCookie);
        response.addCookie(userNameCookie);

        // 登出后最好使用重定向
        // 1. 将消息暂存到新Session中（如果需要在首页显示的话）
        // 为了简化，这里我们直接重定向，首页可以不显示登出消息
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * POST请求直接调用GET处理
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}