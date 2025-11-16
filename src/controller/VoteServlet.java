package controller;

import bean.Candidate;
import bean.User;
import service.CandidateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 投票页面Servlet
 * 处理投票页面的请求，显示候选人列表
 */
@WebServlet("/vote_do")
public class VoteServlet extends HttpServlet {

    // 将Service对象提升为成员变量
    private final CandidateService candidateService = new CandidateService();

    /**
     * GET和POST请求都执行相同的逻辑
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("msg", "<font color='red'>请先登录</font>");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            List<Candidate> candidates = this.candidateService.getAllCandidates();
            request.setAttribute("candidates", candidates);
            request.getRequestDispatcher("/vote.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "<font color='red'>系统错误</font>");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}