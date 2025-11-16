package test;

import bean.Candidate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CandidateService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * CandidateService测试类
 * 用于测试候选人相关的业务逻辑
 * 采用"先增后删"的方式，确保不污染数据库
 */
public class CandidateServiceTest {

    private static final int TEST_CANDIDATE_ID = 999999;
    private static final String TEST_CANDIDATE_NAME = "Test Candidate";
    private static final String TEST_CANDIDATE_DESC = "Test Candidate Description";
    private CandidateService candidateService;

    @Before
    public void setUp() {
        candidateService = new CandidateService();
    }

    @After
    public void tearDown() {
        // 清理测试数据
        try {
            // 删除测试候选人（如果存在）
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (Exception e) {
            // 忽略清理过程中的异常
        }
        candidateService = null;
    }

    @Test
    public void testGetAllCandidates() {
        try {
            // 测试获取所有候选人
            List<Candidate> candidates = candidateService.getAllCandidates();
            assertNotNull("候选人列表不应该为null", candidates);

            // 输出候选人信息用于调试
            System.out.println("候选人数量: " + candidates.size());
            for (Candidate candidate : candidates) {
                System.out.println("候选人: " + candidate.getCandidateId() + " - " + candidate.getCandidateName() +
                        " - 票数: " + candidate.getVoteCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("获取候选人列表不应该抛出异常: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail("获取候选人列表时发生未知异常: " + e.getMessage());
        }
    }

    @Test
    public void testSelectVoteNum() {
        try {
            // 测试查询候选人票数（使用系统中已存在的候选人）
            int voteNum = candidateService.selectVoteNum(1);
            // 候选人票数应该大于等于0
            assertTrue("候选人票数应该大于等于0", voteNum >= 0);
        } catch (IOException e) {
            fail("查询候选人票数不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testAddVoteNum() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 先添加一个测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 先查询当前票数
            int originalVoteNum = candidateService.selectVoteNum(TEST_CANDIDATE_ID);

            // 测试增加候选人票数
            boolean result = candidateService.addVoteNum(TEST_CANDIDATE_ID);
            assertTrue("增加候选人票数应该成功", result);

            // 验证票数确实增加了
            int newVoteNum = candidateService.selectVoteNum(TEST_CANDIDATE_ID);
            assertEquals("候选人票数应该增加1", originalVoteNum + 1, newVoteNum);

            // 清理测试数据
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (IOException e) {
            fail("增加候选人票数不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetCandidateById() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 先添加一个测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 测试根据ID获取候选人
            Candidate retrievedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertNotNull("应该能够根据ID获取候选人", retrievedCandidate);
            assertEquals("候选人ID应该匹配", Integer.valueOf(TEST_CANDIDATE_ID), retrievedCandidate.getCandidateId());
            assertEquals("候选人姓名应该匹配", TEST_CANDIDATE_NAME, retrievedCandidate.getCandidateName());
            assertEquals("候选人描述应该匹配", TEST_CANDIDATE_DESC, retrievedCandidate.getCandidateDescription());

            // 清理测试数据
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (IOException e) {
            fail("根据ID获取候选人不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testAddCandidate() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 创建候选人对象
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);

            // 测试添加候选人
            boolean result = candidateService.addCandidate(candidate);
            assertTrue("添加候选人应该成功", result);

            // 验证候选人是否真的添加成功
            Candidate retrievedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertNotNull("应该能够获取刚添加的候选人", retrievedCandidate);
            assertEquals("候选人ID应该匹配", Integer.valueOf(TEST_CANDIDATE_ID), retrievedCandidate.getCandidateId());
            assertEquals("候选人姓名应该匹配", TEST_CANDIDATE_NAME, retrievedCandidate.getCandidateName());

            // 清理测试数据
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (IOException e) {
            fail("添加候选人不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateCandidate() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 先添加一个测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 更新候选人信息
            candidate.setCandidateName("Updated Candidate");
            candidate.setCandidateDescription("Updated Description");

            // 测试更新候选人
            boolean result = candidateService.updateCandidate(candidate);
            assertTrue("更新候选人应该成功", result);

            // 验证更新是否成功
            Candidate updatedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertEquals("候选人姓名应该已更新", "Updated Candidate", updatedCandidate.getCandidateName());
            assertEquals("候选人描述应该已更新", "Updated Description", updatedCandidate.getCandidateDescription());

            // 清理测试数据
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (IOException e) {
            fail("更新候选人不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCandidate() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 先添加一个测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 验证候选人确实存在
            Candidate retrievedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertNotNull("候选人应该存在", retrievedCandidate);

            // 测试删除候选人
            boolean result = candidateService.deleteCandidate(TEST_CANDIDATE_ID);
            assertTrue("删除候选人应该成功", result);

            // 验证候选人是否真的被删除
            Candidate deletedCandidate = candidateService.getCandidateById(TEST_CANDIDATE_ID);
            assertNull("候选人应该已被删除", deletedCandidate);
        } catch (IOException e) {
            fail("删除候选人不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testResetVoteCount() {
        try {
            // 确保测试候选人不存在
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);

            // 先添加一个测试候选人
            Candidate candidate = new Candidate();
            candidate.setCandidateId(TEST_CANDIDATE_ID);
            candidate.setCandidateName(TEST_CANDIDATE_NAME);
            candidate.setCandidateDescription(TEST_CANDIDATE_DESC);
            candidate.setVoteCount(0);
            candidateService.addCandidate(candidate);

            // 增加一些票数
            candidateService.addVoteNum(TEST_CANDIDATE_ID);
            candidateService.addVoteNum(TEST_CANDIDATE_ID);

            // 验证票数确实增加了
            int voteNum = candidateService.selectVoteNum(TEST_CANDIDATE_ID);
            assertEquals("候选人票数应该为2", 2, voteNum);

            // 测试重置票数
            boolean result = candidateService.resetVoteCount(TEST_CANDIDATE_ID);
            assertTrue("重置票数应该成功", result);

            // 验证票数是否真的重置了
            int resetVoteNum = candidateService.selectVoteNum(TEST_CANDIDATE_ID);
            assertEquals("候选人票数应该重置为0", 0, resetVoteNum);

            // 清理测试数据
            candidateService.deleteCandidate(TEST_CANDIDATE_ID);
        } catch (IOException e) {
            fail("重置票数不应该抛出异常: " + e.getMessage());
        }
    }
}