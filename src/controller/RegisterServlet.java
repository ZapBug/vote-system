package controller;

import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户注册Servlet
 * 处理用户注册请求
 */
@WebServlet("/register_do")

public class RegisterServlet extends HttpServlet {


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
     * 处理POST请求，执行用户注册逻辑
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置请求编码格式
        request.setCharacterEncoding("utf-8");

        // 获取用户输入的注册信息
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String age = request.getParameter("age");
        String sex = request.getParameter("sex");

        // 输入验证
        if (userId == null || userId.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>用户ID不能为空</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>用户名不能为空</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>密码不能为空</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (age == null || age.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>年龄不能为空</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (sex == null || sex.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>性别不能为空</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 数据清理和验证
        Integer userID;
        Integer userAge;

        try {
            userID = Integer.parseInt(userId.trim());
            if (userID <= 0) {
                request.setAttribute("msg", "<font color='red'>用户ID必须是正整数</font>");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "<font color='red'>用户ID格式不正确</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            userAge = Integer.parseInt(age.trim());
            if (userAge < 0 || userAge > 150) {
                request.setAttribute("msg", "<font color='red'>年龄必须在0-150之间</font>");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "<font color='red'>年龄格式不正确</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 清理用户名和密码
        username = username.trim();
        password = password.trim();

        // 验证用户名长度
        if (username.length() < 3 || username.length() > 50) {
            request.setAttribute("msg", "<font color='red'>用户名长度必须在3-50个字符之间</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 验证密码长度
        if (password.length() < 6 || password.length() > 100) {
            request.setAttribute("msg", "<font color='red'>密码长度必须在6-100个字符之间</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 验证性别值
        if (!("男".equals(sex) || "女".equals(sex) || "M".equalsIgnoreCase(sex) || "F".equalsIgnoreCase(sex))) {
            request.setAttribute("msg", "<font color='red'>性别值不正确</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 使用成员变量进行注册
        boolean isSuccess = this.userService.register(userID, username, password, age, sex);

        if (isSuccess) {
            // 注册成功，跳转到登录页面
            request.setAttribute("msg", "<font color='green'>注册成功，请登录</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            // 注册失败，返回注册页面
            request.setAttribute("msg", "<font color='red'>注册失败，用户ID可能已存在</font>");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}