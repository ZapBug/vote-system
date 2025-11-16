package controller;

import bean.Candidate;
import bean.User;
import service.CandidateService;
import service.UserService;
import service.VoteRecordService;
import util.FileUploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 候选人管理Servlet
 * 处理候选人的添加、更新等操作，支持文件上传
 */
@WebServlet("/admin/candidateManage")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 5,        // 5MB
        maxRequestSize = 1024 * 1024 * 20     // 20MB
)
public class CandidateManageServlet extends HttpServlet {

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 权限检查
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String msg = null;

        try {
            if ("addCandidate".equals(action)) {
                msg = addCandidate(request);
            } else if ("updateCandidate".equals(action)) {
                msg = updateCandidate(request);
            } else if ("deleteCandidate".equals(action)) {
                msg = deleteCandidate(request);
            } else if ("resetVoteCount".equals(action)) {
                msg = resetVoteCount(request);
            } else if ("resetAllVoteCounts".equals(action)) {
                msg = resetAllVoteCounts(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "操作失败：" + e.getMessage();
        }

        // 将消息存入session，然后重定向
        if (msg != null) {
            request.getSession().setAttribute("msg", msg);
        }
        response.sendRedirect(request.getContextPath() + "/admin/manage");
    }

    /**
     * 添加候选人
     */
    private String addCandidate(HttpServletRequest request) throws IOException, ServletException {
        String candidateName = request.getParameter("candidateName");
        String candidateDescription = request.getParameter("candidateDescription");

        if (candidateName == null || candidateName.trim().isEmpty()) {
            return "候选人姓名不能为空";
        }

        // 处理文件上传
        String photoPath = "";
        Part photoPart = request.getPart("candidatePhoto");
        if (photoPart != null && photoPart.getSize() > 0) {
            String fileName = getFileName(photoPart);
            byte[] fileContent = getFileContent(photoPart);
            String uploadDir = getServletContext().getRealPath("/img");
            // 检查uploadDir是否为null，避免在测试环境中出现空指针异常
            if (uploadDir != null) {
                photoPath = FileUploadUtil.saveFile(fileName, fileContent, uploadDir);
            }
        }

        Candidate candidate = new Candidate();
        candidate.setCandidateName(candidateName.trim());
        candidate.setCandidateDescription(candidateDescription != null ? candidateDescription.trim() : "");
        candidate.setCandidatePhoto(photoPath != null ? photoPath : "");
        candidate.setVoteCount(0);

        if (candidateService.addCandidate(candidate)) {
            return "候选人添加成功";
        } else {
            return "候选人添加失败";
        }
    }

    /**
     * 更新候选人
     */
    private String updateCandidate(HttpServletRequest request) throws IOException, ServletException {
        Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
        String candidateName = request.getParameter("candidateName");
        String candidateDescription = request.getParameter("candidateDescription");
        String currentPhoto = request.getParameter("currentPhoto");

        if (candidateName == null || candidateName.trim().isEmpty()) {
            return "候选人姓名不能为空";
        }

        Candidate candidate = candidateService.getCandidateById(candidateId);
        if (candidate == null) {
            return "未找到指定的候选人";
        }

        candidate.setCandidateName(candidateName.trim());
        candidate.setCandidateDescription(candidateDescription != null ? candidateDescription.trim() : "");

        // 处理文件上传
        Part photoPart = request.getPart("candidatePhoto");
        if (photoPart != null && photoPart.getSize() > 0) {
            String fileName = getFileName(photoPart);
            byte[] fileContent = getFileContent(photoPart);
            String uploadDir = getServletContext().getRealPath("/img");
            String photoPath = FileUploadUtil.saveFile(fileName, fileContent, uploadDir);
            if (photoPath != null) {
                candidate.setCandidatePhoto(photoPath);
            }
        } else {
            // 没有上传新文件，保持原文件路径
            candidate.setCandidatePhoto(currentPhoto != null ? currentPhoto.trim() : "");
        }

        if (candidateService.updateCandidate(candidate)) {
            return "候选人信息更新成功";
        } else {
            return "候选人信息更新失败";
        }
    }

    /**
     * 删除候选人
     */
    private String deleteCandidate(HttpServletRequest request) throws IOException {
        Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
        if (candidateService.deleteCandidate(candidateId)) {
            voteRecordService.deleteVoteRecordsByCandidate(candidateId);
            return "候选人删除成功";
        } else {
            return "候选人删除失败";
        }
    }

    /**
     * 重置候选人票数
     */
    private String resetVoteCount(HttpServletRequest request) throws IOException {
        Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
        if (candidateService.resetVoteCount(candidateId)) {
            return "候选人票数重置成功";
        } else {
            return "候选人票数重置失败";
        }
    }

    /**
     * 重置所有候选人票数
     */
    private String resetAllVoteCounts(HttpServletRequest request) throws IOException {
        // 获取所有候选人
        java.util.List<Candidate> candidates = candidateService.getAllCandidates();
        boolean success = true;
        for (Candidate candidate : candidates) {
            if (!candidateService.resetVoteCount(candidate.getCandidateId())) {
                success = false;
                break;
            }
        }
        // 删除所有投票记录
        if (success) {
            java.util.List<bean.VoteRecord> records = voteRecordService.getAllVoteRecords();
            for (bean.VoteRecord record : records) {
                voteRecordService.deleteVoteRecord(record.getVoteId());
            }
            return "所有候选人票数重置成功";
        } else {
            return "重置候选人票数失败";
        }
    }

    /**
     * 从Part中提取文件名
     *
     * @param part 上传的文件部分
     * @return 文件名
     */
    private String getFileName(Part part) {
        if (part == null) {
            return null;
        }
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String content : contentDisposition.split(";")) {
                if (content != null && content.trim().startsWith("filename")) {
                    int index = content.indexOf('=');
                    if (index >= 0) {
                        return content.substring(index + 1).trim().replace("\"", "");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从Part中提取文件内容
     *
     * @param part 上传的文件部分
     * @return 文件内容
     */
    private byte[] getFileContent(Part part) throws IOException {
        try (InputStream inputStream = part.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}