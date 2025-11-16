package test;

import bean.Candidate;
import bean.User;
import bean.VoteRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CandidateService;
import service.UserService;
import service.VoteRecordService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 后台管理服务层测试类
 * 用于测试后台管理相关的服务层功能
 */
public class AdminManageServiceTest {

    private static final int TEST_USER_ID = 777777;
    private static final String TEST_USERNAME = "adminuser";
    private static final String TEST_PASSWORD = "adminpass123";
    private static final String TEST_AGE = "30";
    private static final String TEST_SEX = "M";
    private static final int TEST_CANDIDATE_ID = 888888;
    private static final String TEST_CANDIDATE_NAME = "Admin Test Candidate";
    private static final String TEST_CANDIDATE_DESC = "Admin Test Candidate Description";
    private UserService userService;
    private CandidateService candidateService;
    private VoteRecordService voteRecordService;

    @Before
    public void setUp() {
        userService = new UserService();
        candidateService = new CandidateService();
        voteRecordService = new VoteRecordService();
    }

    @After
    public void tearDown() {
        // 清理测试数据
        try {
            // 删除测试用户（如果存在）
            userService.deleteUser(TEST_USER_ID);
            // 删除测试候选人（如果存在）
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (Exception e) {
            // 忽略清理过程中的异常
        }
        userService = null;
        candidateService = null;
        voteRecordService = null;
    }

    @Test
    public void testAdminUserManagement() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 测试添加用户
            boolean registerResult = userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);
            assertTrue("应该能够成功注册用户", registerResult);

            // 测试获取所有用户
            List<User> users = userService.getAllUsers();
            assertNotNull("用户列表不应该为null", users);
            assertFalse("用户列表不应该为空", users.isEmpty());

            boolean found = false;
            for (User user : users) {
                if (TEST_USER_ID == user.getUserId()) {
                    found = true;
                    assertEquals("用户名应该匹配", TEST_USERNAME, user.getUserName());
                    break;
                }
            }
            assertTrue("应该能在用户列表中找到刚添加的用户", found);

            // 测试删除用户
            boolean deleteResult = userService.deleteUser(TEST_USER_ID);
            assertTrue("应该能够成功删除用户", deleteResult);

            // 验证用户确实被删除
            User deletedUser = userService.getUserById(TEST_USER_ID);
            assertNull("用户应该已被删除", deletedUser);

        } catch (IOException e) {
            fail("用户管理测试不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testAdminCandidateManagement() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 测试添加候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);

            boolean addResult = candidateService.addCandidate(candidate);
            assertTrue("应该能够成功添加候选人", addResult);

            // 测试获取所有候选人
            List<Candidate> candidates = candidateService.getAllCandidates();
            assertNotNull("候选人列表不应该为null", candidates);
            assertFalse("候选人列表不应该为空", candidates.isEmpty());

            boolean found = false;
            for (Candidate c : candidates) {
                if (TEST_CANDIDATE_ID == c.getCandidateId()) {
                    found = true;
                    assertEquals("候选人姓名应该匹配", TEST_CANDIDATE_NAME, c.getCandidateName());
                    assertEquals("候选人描述应该匹配", TEST_CANDIDATE_DESC, c.getCandidateDescription());
                    break;
                }
            }
            assertTrue("应该能在候选人列表中找到刚添加的候选人", found);

            // 测试删除候选人
            boolean deleteResult = candidateService.deleteCandidate(TEST_CANDIDATE_ID);
            assertTrue("应该能够成功删除候选人", deleteResult);

            // 验证候选人确实被删除
            Candidate deletedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertNull("候选人应该已被删除", deletedCandidate);

        } catch (IOException e) {
            fail("候选人管理测试不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testAdminVoteRecordManagement() {
        try {
            // 确保测试数据不存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先确保用户和候选人都存在
            userService.deleteUser(TEST_USER_ID);
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 添加测试用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 添加测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 测试添加投票记录
            boolean addVoteResult = voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);
            assertTrue("应该能够成功添加投票记录", addVoteResult);

            // 测试获取所有投票记录
            List<VoteRecord> voteRecords = voteRecordService.getAllVoteRecords();
            assertNotNull("投票记录列表不应该为null", voteRecords);

            boolean found = false;
            for (VoteRecord record : voteRecords) {
                if (TEST_USER_ID == record.getUserId() && TEST_CANDIDATE_ID == record.getCandidateId()) {
                    found = true;
                    break;
                }
            }
            assertTrue("应该能在投票记录列表中找到刚添加的记录", found);

            // 测试重置候选人票数
            boolean resetResult = candidateService.resetVoteCount(TEST_CANDIDATE_ID);
            assertTrue("应该能够成功重置候选人票数", resetResult);

            // 验证票数确实被重置
            int voteCount = candidateService.selectVoteNum(TEST_CANDIDATE_ID);
            assertEquals("候选人票数应该被重置为0", 0, voteCount);

            // 清理测试数据
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
            userService.deleteUser(TEST_USER_ID);
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

        } catch (IOException e) {
            fail("投票记录管理测试不应该抛出异常: " + e.getMessage());
        }
    }
}