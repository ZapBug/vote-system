package filter;

import bean.User;
import service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 认证过滤器，用于处理"记住我"功能
 * 该过滤器将检查记住的登录cookie，如果会话不存在则自动登录
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码（如果需要）
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // 检查用户是否未登录（没有会话用户）但有记住我cookie
        if (session == null || session.getAttribute("user") == null) {
            // 尝试使用记住我cookie自动登录
            autoLoginWithRememberMe(httpRequest);
        }

        // 继续过滤器链
        chain.doFilter(request, response);
    }

    /**
     * 使用记住我cookie自动登录
     * 验证用户仍然存在于数据库中以确保安全性
     */
    private void autoLoginWithRememberMe(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return;
            }

            String rememberedUserId = null;
            String rememberedUserName = null;

            // 提取记住我cookie
            for (Cookie cookie : cookies) {
                if ("rememberedUserId".equals(cookie.getName())) {
                    rememberedUserId = cookie.getValue();
                } else if ("rememberedUserName".equals(cookie.getName())) {
                    rememberedUserName = cookie.getValue();
                }
            }

            // 如果所有必需的cookie都存在，则验证用户并创建会话
            if (rememberedUserId != null && rememberedUserName != null) {
                try {
                    int userId = Integer.parseInt(rememberedUserId);

                    // 通过服务验证用户是否存在于数据库中
                    UserService userService = new UserService();
                    User user = userService.getUserById(userId);

                    // 仅在用户存在且用户名匹配时自动登录
                    if (user != null && rememberedUserName.equals(user.getUserName())) {
                        // 在会话中设置验证的用户以模拟登录
                        request.getSession(true).setAttribute("user", user);
                    }
                } catch (NumberFormatException e) {
                    // 如果userId不是有效数字，则忽略cookie
                } catch (Exception e) {
                    // 处理数据库操作期间的其他异常
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // 如果处理cookie时出错，则继续不自动登录
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // 清理代码（如果需要）
    }
}