package controller;

import bean.Candidate;
import bean.User;
import bean.VoteRecord;
import service.CandidateService;
import service.UserService;
import service.VoteRecordService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 后台管理Servlet
 * 处理管理员查看投票结果的请求
 */
@WebServlet("/admin/manage")

public class AdminManageServlet extends HttpServlet {


    // 将Service对象提升为成员变量，只在Servlet初始化时创建一次

    private CandidateService candidateService;

    private UserService userService;

    private VoteRecordService voteRecordService;


    @Override

    public void init() throws ServletException {

        candidateService = new CandidateService();

        userService = new UserService();

        voteRecordService = new VoteRecordService();

    }


    // 用于测试的setter方法

    public void setCandidateService(CandidateService candidateService) {

        this.candidateService = candidateService;

    }


    public void setUserService(UserService userService) {

        this.userService = userService;

    }


    public void setVoteRecordService(VoteRecordService voteRecordService) {

        this.voteRecordService = voteRecordService;

    }

    /**
     * 处理GET请求，显示投票结果管理页面
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("msg", "<font color='red'>请先登录</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (!"admin".equals(user.getUserRole())) {
            request.setAttribute("msg", "<font color='red'>您没有权限访问此页面</font>");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        try {
            // 使用成员变量，不再需要 new
            List<Candidate> candidates = this.candidateService.getAllCandidates();
            List<User> users = this.userService.getAllUsers();
            List<VoteRecord> voteRecords = this.voteRecordService.getAllVoteRecords();

            request.setAttribute("candidates", candidates);
            request.setAttribute("users", users);
            request.setAttribute("voteRecords", voteRecords);

            request.getRequestDispatcher("/admin/manage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "<font color='red'>系统错误，请稍后重试</font>");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    /**
     * POST请求直接调用GET处理
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}