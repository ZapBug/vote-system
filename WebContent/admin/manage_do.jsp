<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="bean.User" %>
<%@ page import="bean.Candidate" %>
<%@ page import="service.UserService" %>
<%@ page import="service.CandidateService" %>
<%@ page import="service.VoteRecordService" %>
<%@ page import="util.FileUploadUtil" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.nio.file.Paths" %>
<%
    // 权限检查
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || !"admin".equals(currentUser.getUserRole())) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    String action = request.getParameter("action");
    String msg = null; // 用于存储消息
    try {
        if ("deleteUser".equals(action)) {
            Integer userId = Integer.parseInt(request.getParameter("userId"));
            UserService userService = new UserService();
            if (userService.deleteUser(userId)) {
                msg = "用户删除成功";
            } else {
                msg = "用户删除失败";
            }
        } else if ("deleteCandidate".equals(action)) {
            Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
            CandidateService candidateService = new CandidateService();
            if (candidateService.deleteCandidate(candidateId)) {
                VoteRecordService voteRecordService = new VoteRecordService();
                voteRecordService.deleteVoteRecordsByCandidate(candidateId);
                msg = "候选人删除成功";
            } else {
                msg = "候选人删除失败";
            }
        } else if ("resetVoteCount".equals(action)) {
            Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
            CandidateService candidateService = new CandidateService();
            if (candidateService.resetVoteCount(candidateId)) {
                msg = "候选人票数重置成功";
            } else {
                msg = "候选人票数重置失败";
            }
        } else if ("resetAllVoteCounts".equals(action)) {
            CandidateService candidateService = new CandidateService();
            VoteRecordService voteRecordService = new VoteRecordService();
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
                msg = "所有候选人票数重置成功";
            } else {
                msg = "重置候选人票数失败";
            }
        } else if ("deleteVoteRecord".equals(action)) {
            Integer voteId = Integer.parseInt(request.getParameter("voteId"));
            VoteRecordService voteRecordService = new VoteRecordService();
            if (voteRecordService.deleteVoteRecord(voteId)) {
                msg = "投票记录删除成功";
            } else {
                msg = "投票记录删除失败";
            }
        } else if ("addCandidate".equals(action)) {
            String candidateName = request.getParameter("candidateName");
            String candidateDescription = request.getParameter("candidateDescription");
            String candidatePhoto = request.getParameter("candidatePhoto");

            // 处理文件上传
            String photoPath = "";
            try {
                Part photoPart = request.getPart("candidatePhoto");
                if (photoPart != null && photoPart.getSize() > 0) {
                    String uploadDir = application.getRealPath("/img");
                    // 获取文件名
                    String fileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
                    // 读取文件内容为字节数组
                    byte[] fileContent = new byte[(int) photoPart.getSize()];
                    photoPart.getInputStream().read(fileContent);
                    // 保存文件
                    photoPath = FileUploadUtil.saveFile(fileName, fileContent, uploadDir);
                }
            } catch (IOException e) {
                msg = "文件上传失败：" + e.getMessage();
            } catch (Exception e) {
                // 忽略，可能不支持getPart方法
            }

            if (msg == null) { // 没有文件上传错误
                if (candidateName != null && !candidateName.trim().isEmpty()) {
                    Candidate candidate = new Candidate();
                    candidate.setCandidateName(candidateName.trim());
                    candidate.setCandidateDescription(candidateDescription != null ? candidateDescription.trim() : "");
                    candidate.setCandidatePhoto(photoPath != null ? photoPath : "");
                    candidate.setVoteCount(0);

                    CandidateService candidateService = new CandidateService();
                    if (candidateService.addCandidate(candidate)) {
                        msg = "候选人添加成功";
                    } else {
                        msg = "候选人添加失败";
                    }
                } else {
                    msg = "候选人姓名不能为空";
                }
            }
        } else if ("updateCandidate".equals(action)) {
            Integer candidateId = Integer.parseInt(request.getParameter("candidateId"));
            String candidateName = request.getParameter("candidateName");
            String candidateDescription = request.getParameter("candidateDescription");
            String candidatePhoto = request.getParameter("candidatePhoto");

            if (candidateName != null && !candidateName.trim().isEmpty()) {
                CandidateService candidateService = new CandidateService();
                Candidate candidate = candidateService.getCandidateById(candidateId);
                if (candidate != null) {
                    candidate.setCandidateName(candidateName.trim());
                    candidate.setCandidateDescription(candidateDescription != null ? candidateDescription.trim() : "");

                    // 处理文件上传
                    boolean photoUpdated = false;
                    try {
                        Part photoPart = request.getPart("candidatePhoto");
                        if (photoPart != null && photoPart.getSize() > 0) {
                            String uploadDir = application.getRealPath("/img");
                            // 获取文件名
                            String fileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
                            // 读取文件内容为字节数组
                            byte[] fileContent = new byte[(int) photoPart.getSize()];
                            photoPart.getInputStream().read(fileContent);
                            // 保存文件
                            String photoPath = FileUploadUtil.saveFile(fileName, fileContent, uploadDir);
                            if (photoPath != null) {
                                candidate.setCandidatePhoto(photoPath);
                                photoUpdated = true;
                            }
                        }
                    } catch (IOException e) {
                        msg = "文件上传失败：" + e.getMessage();
                    } catch (Exception e) {
                        // 忽略，可能不支持getPart方法
                    }

                    // 如果没有上传新文件，则保持原文件路径
                    if (!photoUpdated && candidatePhoto != null) {
                        candidate.setCandidatePhoto(candidatePhoto.trim());
                    }

                    if (msg == null) { // 没有文件上传错误
                        if (candidateService.updateCandidate(candidate)) {
                            msg = "候选人信息更新成功";
                        } else {
                            msg = "候选人信息更新失败";
                        }
                    }
                } else {
                    msg = "未找到指定的候选人";
                }
            } else {
                msg = "候选人姓名不能为空";
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        msg = "操作失败：" + e.getMessage();
    }
    //  将消息存入session，然后重定向
    if (msg != null) {
        session.setAttribute("msg", msg);
    }
    response.sendRedirect(request.getContextPath() + "/admin/manage");
%>