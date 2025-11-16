package test;

import bean.User;
import controller.LoginServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * LoginServlet测试类
 * 用于测试用户登录Servlet的功能
 */
public class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;

    // 模拟Service层
    private UserService mockUserService;

    @Before

    public void setUp() throws Exception {

        servlet = new LoginServlet();

        request = mock(HttpServletRequest.class);

        response = mock(HttpServletResponse.class);

        session = mock(HttpSession.class);

        requestDispatcher = mock(RequestDispatcher.class);

        mockUserService = mock(UserService.class);


        // 设置通用的模拟行为

        when(request.getSession()).thenReturn(session);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(request.getContextPath()).thenReturn(""); // 模拟 getContextPath


        // 使用setter方法注入模拟的 UserService

        servlet.setUserService(mockUserService);

    }

    @After
    public void tearDown() {
        // 清理资源
    }

    /**
     * 辅助方法：通过反射调用受保护的 doPost 方法
     */
    private void invokeDoPost() {
        try {
            Method doPostMethod = LoginServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
            doPostMethod.setAccessible(true);
            doPostMethod.invoke(servlet, request, response);
        } catch (InvocationTargetException e) {
            fail("doPost method threw an unexpected exception: " + e.getTargetException().toString());
        } catch (Exception e) {
            fail("Failed to invoke doPost method via reflection: " + e.getMessage());
        }
    }

    /**
     * 测试场景：用户ID或密码为空时，都应该转发回登录页面
     */
    @Test
    public void testDoPostWithMissingCredentials() {
        // 模拟 userId 为空
        when(request.getParameter("userId")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password123");

        invokeDoPost();

        // 验证因为参数缺失而转发
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/login.jsp");
        try {
            verify(requestDispatcher).forward(request, response);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * 测试场景：用户ID格式不正确时，应该转发回登录页面
     */
    @Test
    public void testDoPostWithInvalidUserIdFormat() {
        when(request.getParameter("userId")).thenReturn("abc"); // 非法ID
        when(request.getParameter("password")).thenReturn("password123");

        invokeDoPost();

        // 验证因为数据格式错误而转发
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/login.jsp");
        try {
            verify(requestDispatcher).forward(request, response);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * 测试场景：凭据正确，Service层返回User对象，表示登录成功
     */
    @Test
    public void testDoPostSuccessfulLogin() throws IOException {
        // 1. 准备数据 (Arrange)
        when(request.getParameter("userId")).thenReturn("123456");
        when(request.getParameter("password")).thenReturn("password123");

        User fakeUser = new User();
        fakeUser.setUserId(123456);
        fakeUser.setUserName("testuser");

        // 模拟Service层登录成功
        when(mockUserService.login(123456, "password123")).thenReturn(fakeUser);

        // 2. 执行动作 (Act)
        invokeDoPost();

        // 3. 断言/验证 (Assert / Verify)
        // 验证用户信息被保存到Session中
        verify(session).setAttribute("user", fakeUser);
        // 验证成功消息被保存到Session中
        verify(session).setAttribute(eq("msg"), anyString());
        // 验证执行了重定向到首页
        verify(response).sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * 测试场景：凭据格式正确，但Service层返回null，表示登录失败
     */
    @Test
    public void testDoPostFailedLogin() throws IOException {
        // 1. 准备数据 (Arrange)
        when(request.getParameter("userId")).thenReturn("123456");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        // 模拟Service层登录失败
        when(mockUserService.login(123456, "wrongpassword")).thenReturn(null);

        // 2. 执行动作 (Act)
        invokeDoPost();

        // 3. 断言/验证 (Assert / Verify)
        // 验证设置了失败消息
        verify(request).setAttribute(eq("msg"), anyString());
        // 验证转发回了登录页面
        verify(request).getRequestDispatcher("/login.jsp");
        try {
            verify(requestDispatcher).forward(request, response);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // 验证从未向Session中设置user属性
        verify(session, never()).setAttribute(eq("user"), any(User.class));
    }
}