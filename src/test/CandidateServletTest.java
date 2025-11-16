package test;

import bean.User;
import controller.CandidateServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CandidateService;
import service.VoteRecordService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * CandidateServlet测试类
 * 用于测试候选人投票Servlet的功能
 */
public class CandidateServletTest {

    private CandidateServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;

    private CandidateService mockCandidateService;
    private VoteRecordService mockVoteRecordService;

    @Before
    public void setUp() throws Exception {
        servlet = new CandidateServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
        mockCandidateService = mock(CandidateService.class);
        mockVoteRecordService = mock(VoteRecordService.class);

        // 设置通用的模拟行为
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        // 模拟 getContextPath() 的行为，防止返回 null 导致 URL 错误
        when(request.getContextPath()).thenReturn("");

        // 使用setter方法注入模拟的Service，隔离数据库
        servlet.setCandidateService(mockCandidateService);
        servlet.setVoteRecordService(mockVoteRecordService);
    }

    @After
    public void tearDown() {
        // 清理资源
    }

    /**
     * 辅助方法：通过反射调用受保护的 doPost 方法，并处理异常
     */
    private void invokeDoPost() {
        try {
            Method doPostMethod = CandidateServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
            doPostMethod.setAccessible(true);
            doPostMethod.invoke(servlet, request, response);
        } catch (InvocationTargetException e) {
            // 如果doPost内部抛出未捕获的异常，这里会捕获。让测试失败并打印根本原因。
            fail("doPost method threw an unexpected exception: " + e.getTargetException().toString());
        } catch (Exception e) {
            fail("Failed to invoke doPost method via reflection: " + e.getMessage());
        }
    }

    @Test
    public void testDoPostWithoutLogin() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        invokeDoPost();
        // 验证调用了正确的URL进行重定向
        verify(response).sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Test
    public void testDoPostWithEmptyVoteNumber() throws Exception {
        User user = new User();
        user.setUserId(1);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("");
        invokeDoPost();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithNullVoteNumber() throws Exception {
        User user = new User();
        user.setUserId(1);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn(null);
        invokeDoPost();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithInvalidVoteNumber() throws Exception {
        User user = new User();
        user.setUserId(1);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("abc");
        invokeDoPost();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithNegativeVoteNumber() throws Exception {
        User user = new User();
        user.setUserId(1);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("-123");
        invokeDoPost();
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithValidVoteNumberAndUserAlreadyVoted() throws Exception {
        User user = new User();
        user.setUserId(123456);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("1");

        when(mockVoteRecordService.hasVoted(123456, 1)).thenReturn(true);
        when(mockCandidateService.getAllCandidates()).thenReturn(new ArrayList<>());

        invokeDoPost();

        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).setAttribute(eq("candidates"), anyList());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithValidVoteNumberAndSuccessfulVoting() throws Exception {
        User user = new User();
        user.setUserId(123456);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("1");

        when(mockVoteRecordService.hasVoted(123456, 1)).thenReturn(false);
        // 假设 addVoteRecord 返回 boolean 表示成功/失败
        when(mockVoteRecordService.addVoteRecord(anyInt(), anyInt())).thenReturn(true);
        when(mockCandidateService.addVoteNum(1)).thenReturn(true);

        invokeDoPost();

        verify(session).setAttribute(eq("msg"), anyString());
        verify(response).sendRedirect(request.getContextPath() + "/vote_do");
    }

    @Test
    public void testDoPostWithValidVoteNumberAndFailedVoting() throws Exception {
        User user = new User();
        user.setUserId(123456);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("1");

        when(mockVoteRecordService.hasVoted(123456, 1)).thenReturn(false);
        // 假设 addVoteRecord 返回 boolean
        when(mockVoteRecordService.addVoteRecord(anyInt(), anyInt())).thenReturn(true);
        when(mockCandidateService.addVoteNum(1)).thenReturn(false); // 模拟增加票数失败
        when(mockCandidateService.getAllCandidates()).thenReturn(new ArrayList<>());

        invokeDoPost();

        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).setAttribute(eq("candidates"), anyList());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithValidVoteNumberAndExceptionDuringVoting() throws Exception {
        User user = new User();
        user.setUserId(123456);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("vote_num")).thenReturn("1");

        when(mockVoteRecordService.hasVoted(123456, 1)).thenReturn(false);
        // 模拟调用 addVoteRecord 时抛出异常
        when(mockVoteRecordService.addVoteRecord(anyInt(), anyInt())).thenThrow(new RuntimeException("Database error"));
        when(mockCandidateService.getAllCandidates()).thenReturn(new ArrayList<>());

        invokeDoPost();

        // 验证Servlet的catch块是否正确工作
        verify(request).setAttribute(eq("msg"), anyString());
        verify(request).setAttribute(eq("candidates"), anyList());
        verify(request).getRequestDispatcher("/vote_do");
        verify(requestDispatcher).forward(request, response);
    }
}