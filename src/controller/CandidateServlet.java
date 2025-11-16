package controller;

import bean.User;
import service.CandidateService;
import service.VoteRecordService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/candidate_vote")
public class CandidateServlet extends HttpServlet {

    // 将Service对象提升为成员变量，只在Servlet初始化时创建一次
    private CandidateService candidateService;
    private VoteRecordService voteRecordService;

    @Override
    public void init() throws ServletException {
        candidateService = new CandidateService();
        voteRecordService = new VoteRecordService();
    }

    // 用于测试的setter方法
    public void setCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    public void setVoteRecordService(VoteRecordService voteRecordService) {
        this.voteRecordService = voteRecordService;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 对于POST请求，最好使用重定向以防止用户刷新时重复提交
            session.setAttribute("msg", "<font color='red'>请先登录</font>");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String vote = request.getParameter("vote_num");

        if (vote == null || vote.trim().isEmpty()) {
            request.setAttribute("msg", "<font color='red'>请选择要投票的候选人</font>");
            request.setAttribute("candidates", this.candidateService.getAllCandidates());
            request.getRequestDispatcher("/vote_do").forward(request, response);
            return;
        }

        int candidateId;
        try {
            candidateId = Integer.parseInt(vote.trim());
            if (candidateId <= 0) {
                throw new NumberFormatException(); // 将负数也视为格式错误
            }
        } catch (NumberFormatException e) {
            request.setAttribute("msg", "<font color='red'>候选人ID格式不正确</font>");
            request.setAttribute("candidates", this.candidateService.getAllCandidates());
            request.getRequestDispatcher("/vote_do").forward(request, response);
            return;
        }

        // 检查用户是否已经对该候选人投过票
        if (this.voteRecordService.hasVoted(user.getUserId(), candidateId)) {
            request.setAttribute("msg", "<font color='red'>您已经投过票了</font>");
            request.setAttribute("candidates", this.candidateService.getAllCandidates());
            request.getRequestDispatcher("/vote_do").forward(request, response);
            return;
        }

        try {
            // 记录投票记录
            this.voteRecordService.addVoteRecord(user.getUserId(), candidateId);

            if (this.candidateService.addVoteNum(candidateId)) {
                // 成功后使用重定向（PRG模式），避免刷新重复投票
                session.setAttribute("msg", "<font color='green'>投票成功</font>");
                response.sendRedirect(request.getContextPath() + "/vote_do");
            } else {
                request.setAttribute("msg", "<font color='red'>投票失败，请稍后再试</font>");
                request.setAttribute("candidates", this.candidateService.getAllCandidates());
                request.getRequestDispatcher("/vote_do").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msg", "<font color='red'>系统错误，请联系管理员</font>");
            request.setAttribute("candidates", this.candidateService.getAllCandidates());
            request.getRequestDispatcher("/vote_do").forward(request, response);
        }
    }

    /**
     * 处理GET请求，重定向到首页
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // GET请求重定向到首页，防止直接访问投票提交端点
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}