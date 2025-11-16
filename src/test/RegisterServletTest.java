package test;

import controller.RegisterServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * RegisterServlet测试类
 * 用于测试用户注册Servlet的功能
 */
public class RegisterServletTest {

    private RegisterServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    // 模拟Service层
    private UserService mockUserService;

    @Before

    public void setUp() throws Exception {

        servlet = new RegisterServlet();

        request = mock(HttpServletRequest.class);

        response = mock(HttpServletResponse.class);

        requestDispatcher = mock(RequestDispatcher.class);

        mockUserService = mock(UserService.class);


        // 设置通用的模拟行为

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);


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
            Method doPostMethod = RegisterServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
            doPostMethod.setAccessible(true);
            doPostMethod.invoke(servlet, request, response);
        } catch (InvocationTargetException e) {
            fail("doPost method threw an unexpected exception: " + e.getTargetException().toString());
        } catch (Exception e) {
            fail("Failed to invoke doPost method via reflection: " + e.getMessage());
        }
    }

    /**
     * 测试场景：任何一个必要的参数为空或null时，都应该转发回注册页面
     */
    @Test
    public void testDoPostWithMissingParameters() throws ServletException, IOException {
        // 模拟 userId 为空
        when(request.getParameter("userId")).thenReturn("");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("age")).thenReturn("25");
        when(request.getParameter("sex")).thenReturn("男");

        invokeDoPost();

        // 验证因为参数缺失而转发
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    /**
     * 测试场景：输入的数据格式不合法时（例如年龄不是数字），应该转发回注册页面
     */
    @Test
    public void testDoPostWithInvalidDataFormat() throws ServletException, IOException {
        // 模拟 age 格式不正确
        when(request.getParameter("userId")).thenReturn("123456");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("age")).thenReturn("abc"); // 非法年龄
        when(request.getParameter("sex")).thenReturn("男");

        invokeDoPost();

        // 验证因为数据格式错误而转发
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    /**
     * 测试场景：所有数据都合法，并且Service层返回注册成功
     */
    @Test
    public void testDoPostSuccessfulRegistration() throws ServletException, IOException {
        // 模拟所有参数都有效
        when(request.getParameter("userId")).thenReturn("123456");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("age")).thenReturn("25");
        when(request.getParameter("sex")).thenReturn("男");

        // 模拟Service层注册成功
        when(mockUserService.register(123456, "testuser", "password123", "25", "男")).thenReturn(true);

        invokeDoPost();

        // 验证注册成功后，设置成功消息并转发到登录页面
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    /**
     * 测试场景：所有数据都合法，但Service层返回注册失败（例如用户ID已存在）
     */
    @Test
    public void testDoPostFailedRegistration() throws ServletException, IOException {
        // 模拟所有参数都有效
        when(request.getParameter("userId")).thenReturn("123456");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("age")).thenReturn("25");
        when(request.getParameter("sex")).thenReturn("男");

        // 模拟Service层注册失败
        when(mockUserService.register(123456, "testuser", "password123", "25", "男")).thenReturn(false);

        invokeDoPost();

        // 验证注册失败后，设置失败消息并转发回注册页面
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/register.jsp");
        verify(requestDispatcher).forward(request, response);
    }
}