package test;

import bean.Candidate;
import bean.User;
import bean.VoteRecord;
import controller.AdminManageServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CandidateService;
import service.UserService;
import service.VoteRecordService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * AdminManageServlet测试类
 * 用于测试后台管理Servlet的功能
 */
public class AdminManageServletTest {

    private AdminManageServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;

    // 模拟所有依赖的Service层
    private CandidateService mockCandidateService;
    private UserService mockUserService;
    private VoteRecordService mockVoteRecordService;

    @Before
    public void setUp() throws Exception {
        servlet = new AdminManageServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);

        // 创建所有Service的模拟对象
        mockCandidateService = mock(CandidateService.class);
        mockUserService = mock(UserService.class);
        mockVoteRecordService = mock(VoteRecordService.class);

        // 设置通用的模拟行为
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        // 使用setter方法将所有模拟Service注入到Servlet实例中
        servlet.setCandidateService(mockCandidateService);
        servlet.setUserService(mockUserService);
        servlet.setVoteRecordService(mockVoteRecordService);
    }

    @After
    public void tearDown() { /* Cleanup */ }

    /**
     * 辅助方法：通过反射调用受保护的 doGet 方法
     */
    private void invokeDoGet() {
        try {
            Method doGetMethod = AdminManageServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
            doGetMethod.setAccessible(true);
            doGetMethod.invoke(servlet, request, response);
        } catch (InvocationTargetException e) {
            fail("doGet method threw an unexpected exception: " + e.getTargetException().toString());
        } catch (Exception e) {
            fail("Failed to invoke doGet method via reflection: " + e.getMessage());
        }
    }

    @Test
    public void testDoGetWithoutLogin() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        invokeDoGet();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithNonAdminUser() throws Exception {
        User nonAdminUser = new User();
        nonAdminUser.setUserRole("user"); // 角色不是 "admin"
        when(session.getAttribute("user")).thenReturn(nonAdminUser);
        invokeDoGet();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/index.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithAdminUserSuccessfully() throws Exception {
        // 1. 准备数据 (Arrange)
        User adminUser = new User();
        adminUser.setUserRole("admin");
        when(session.getAttribute("user")).thenReturn(adminUser);

        // 准备模拟返回的数据
        List<Candidate> mockCandidates = new ArrayList<>();
        List<User> mockUsers = new ArrayList<>();
        List<VoteRecord> mockVoteRecords = new ArrayList<>();

        // 模拟Service层的行为
        when(mockCandidateService.getAllCandidates()).thenReturn(mockCandidates);
        when(mockUserService.getAllUsers()).thenReturn(mockUsers);
        when(mockVoteRecordService.getAllVoteRecords()).thenReturn(mockVoteRecords);

        // 2. 执行动作 (Act)
        invokeDoGet();

        // 3. 断言/验证 (Assert / Verify)
        // 验证Service方法被调用
        verify(mockCandidateService).getAllCandidates();
        verify(mockUserService).getAllUsers();
        verify(mockVoteRecordService).getAllVoteRecords();

        // 验证数据被正确地设置到request属性中
        verify(request).setAttribute("candidates", mockCandidates);
        verify(request).setAttribute("users", mockUsers);
        verify(request).setAttribute("voteRecords", mockVoteRecords);

        // 验证转发到了正确的管理页面
        verify(request).getRequestDispatcher("/admin/manage.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithAdminUserWhenServiceFails() throws Exception {
        // 1. 准备数据 (Arrange)
        User adminUser = new User();
        adminUser.setUserRole("admin");
        when(session.getAttribute("user")).thenReturn(adminUser);

        // 模拟其中一个Service调用时抛出异常
        when(mockCandidateService.getAllCandidates()).thenThrow(new RuntimeException("Database connection failed"));

        // 2. 执行动作 (Act)
        invokeDoGet();

        // 3. 断言/验证 (Assert / Verify)
        // 验证Servlet的catch块是否正确工作：设置错误消息并转发到首页
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/index.jsp");
        verify(requestDispatcher).forward(request, response);
    }
}