package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内容安全策略过滤器
 * 用于增强Web应用安全性，防止XSS等攻击
 */
@WebFilter("/*")
public class CSPFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化操作（如果需要）
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置内容安全策略头，限制资源加载来源
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://ajax.googleapis.com https://maxcdn.bootstrapcdn.com; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "font-src 'self'; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none';");

        // 防止点击劫持
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // 防止MIME类型嗅探
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // 启用XSS保护
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // 强制HTTPS（如果需要）
        // httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 清理操作（如果需要）
    }
}