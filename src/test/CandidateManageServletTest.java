package test;

import bean.Candidate;
import bean.User;
import controller.CandidateManageServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import service.CandidateService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidateManageServletTest {

    @InjectMocks
    private CandidateManageServlet servlet;

    @Mock
    private CandidateService mockCandidateService;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private Part photoPart;

    @Before
    public void setUp() throws Exception {
        // 1. 首先，手动初始化Servlet。
        // 这会调用它自己的 init() 方法，该方法会创建真实的 Service 对象。
        servlet.init(servletConfig);

        // 2. 现在，关键的一步：在它初始化之后，我们再调用setter方法，
        // 用我们的模拟对象覆盖掉它刚刚创建的真实对象。
        servlet.setCandidateService(mockCandidateService);

        // 3. 配置Servlet运行时依赖的其他模拟对象
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn("D:\\fake\\path\\for\\testing\\img");

        // 4. 设置通用的 request/session 模拟行为
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/vote-system");
    }

    /**
     * 辅助方法：通过反射调用 protected 的 doPost 方法
     */
    private void invokeDoPost() throws Exception {
        Method doPost = CandidateManageServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true); // 允许访问 protected 方法
        doPost.invoke(servlet, request, response);
    }

    @Test
    public void testAddCandidateWithFileUpload_Success() throws Exception {
        // --- Arrange (准备) ---
        User adminUser = new User();
        adminUser.setUserRole("admin");
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getParameter("action")).thenReturn("addCandidate");
        when(request.getParameter("candidateName")).thenReturn("测试候选人");
        when(request.getParameter("candidateDescription")).thenReturn("这是一个测试描述");

        String fakeFileContent = "fake image data";
        InputStream fakeInputStream = new ByteArrayInputStream(fakeFileContent.getBytes());
        when(request.getPart("candidatePhoto")).thenReturn(photoPart);
        when(photoPart.getSize()).thenReturn((long) fakeFileContent.length());
        when(photoPart.getHeader("content-disposition")).thenReturn("form-data; name=\"candidatePhoto\"; filename=\"test.jpg\"");
        when(photoPart.getInputStream()).thenReturn(fakeInputStream);

        when(mockCandidateService.addCandidate(any(Candidate.class))).thenReturn(true);

        // --- Act (执行) ---
        invokeDoPost();

        // --- Assert (断言) ---
        verify(mockCandidateService, times(1)).addCandidate(any(Candidate.class));
        verify(session).setAttribute("msg", "候选人添加成功");
        verify(response).sendRedirect("/vote-system/admin/manage");
    }

    @Test
    public void testAddCandidate_ServiceFails() throws Exception {
        // --- Arrange (准备) ---
        User adminUser = new User();
        adminUser.setUserRole("admin");
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getParameter("action")).thenReturn("addCandidate");
        when(request.getParameter("candidateName")).thenReturn("失败的候选人");

        when(request.getPart("candidatePhoto")).thenReturn(null);

        when(mockCandidateService.addCandidate(any(Candidate.class))).thenReturn(false);

        // --- Act (执行) ---
        invokeDoPost();

        // --- Assert (断言) ---
        verify(session).setAttribute("msg", "候选人添加失败");
        verify(response).sendRedirect("/vote-system/admin/manage");
    }
}