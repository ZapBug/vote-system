package test;

import bean.VoteRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.VoteRecordService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * VoteRecordService测试类
 * 用于测试投票记录相关的业务逻辑
 * 采用"先增后删"的方式，确保不污染数据库
 */
public class VoteRecordServiceTest {

    private static final int TEST_USER_ID = 1;
    private static final int TEST_CANDIDATE_ID = 1;
    private VoteRecordService voteRecordService;

    @Before
    public void setUp() {
        voteRecordService = new VoteRecordService();
    }

    @After
    public void tearDown() {
        // 清理测试数据
        try {
            // 删除测试投票记录（如果存在）
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
        } catch (Exception e) {
            // 忽略清理过程中的异常
        }
        voteRecordService = null;
    }

    @Test
    public void testAddAndDeleteVoteRecord() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 测试添加投票记录
            boolean result = voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);
            assertTrue("添加投票记录应该成功", result);

            // 验证投票记录是否真的添加成功
            boolean hasVoted = voteRecordService.hasVoted(TEST_USER_ID, TEST_CANDIDATE_ID);
            assertTrue("应该能够查询到刚添加的投票记录", hasVoted);

            // 清理测试数据
            boolean deleteResult = voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
            assertTrue("应该能够删除测试投票记录", deleteResult);
        } catch (IOException e) {
            fail("添加投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testHasVoted() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加一条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);

            // 测试检查用户是否已投票
            boolean result = voteRecordService.hasVoted(TEST_USER_ID, TEST_CANDIDATE_ID);
            assertTrue("用户应该已经投过票", result);

            // 测试检查用户对其他候选人是否已投票
            boolean result2 = voteRecordService.hasVoted(TEST_USER_ID, TEST_CANDIDATE_ID + 1);
            assertFalse("用户对其他候选人应该没有投过票", result2);

            // 清理测试数据
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("检查投票状态不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetVoteRecordsByUser() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加几条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID + 1);

            // 测试根据用户ID获取投票记录
            List<VoteRecord> voteRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertNotNull("投票记录列表不应该为null", voteRecords);
            assertEquals("应该获取到2条投票记录", 2, voteRecords.size());

            // 验证获取到的投票记录
            for (VoteRecord record : voteRecords) {
                assertEquals("投票记录的用户ID应该匹配", Integer.valueOf(TEST_USER_ID), record.getUserId());
            }

            // 清理测试数据
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("获取投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllVoteRecords() {
        try {
            // 测试获取所有投票记录
            List<VoteRecord> voteRecords = voteRecordService.getAllVoteRecords();
            assertNotNull("投票记录列表不应该为null", voteRecords);
            // 不对列表是否为空做硬性断言，因为数据库可能确实没有数据
        } catch (IOException e) {
            fail("获取所有投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetVoteRecordById() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加一条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);

            // 获取刚添加的投票记录
            List<VoteRecord> voteRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertFalse("应该至少有一条投票记录", voteRecords.isEmpty());

            // 测试根据ID获取投票记录
            VoteRecord record = voteRecordService.getVoteRecordById(voteRecords.get(0).getVoteId());
            assertNotNull("应该能够根据ID获取投票记录", record);
            assertEquals("投票记录的用户ID应该匹配", Integer.valueOf(TEST_USER_ID), record.getUserId());
            assertEquals("投票记录的候选人ID应该匹配", Integer.valueOf(TEST_CANDIDATE_ID), record.getCandidateId());

            // 清理测试数据
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("根据ID获取投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteVoteRecord() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加一条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);

            // 获取刚添加的投票记录
            List<VoteRecord> voteRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertFalse("应该至少有一条投票记录", voteRecords.isEmpty());

            // 测试删除投票记录
            boolean result = voteRecordService.deleteVoteRecord(voteRecords.get(0).getVoteId());
            assertTrue("删除投票记录应该成功", result);

            // 验证投票记录是否真的被删除
            List<VoteRecord> remainingRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertTrue("投票记录应该已被删除", remainingRecords.isEmpty());
        } catch (IOException e) {
            fail("删除投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteVoteRecordsByUser() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加几条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID + 1);

            // 验证记录确实存在
            List<VoteRecord> voteRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertEquals("应该有2条投票记录", 2, voteRecords.size());

            // 测试根据用户ID删除投票记录
            boolean result = voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
            assertTrue("根据用户ID删除投票记录应该成功", result);

            // 验证投票记录是否真的被删除
            List<VoteRecord> remainingRecords = voteRecordService.getVoteRecordsByUser(TEST_USER_ID);
            assertTrue("投票记录应该已被删除", remainingRecords.isEmpty());
        } catch (IOException e) {
            fail("根据用户ID删除投票记录不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteVoteRecordsByCandidate() {
        try {
            // 确保没有测试数据存在
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);

            // 先添加几条投票记录
            voteRecordService.addVoteRecord(TEST_USER_ID, TEST_CANDIDATE_ID);
            voteRecordService.addVoteRecord(2, TEST_CANDIDATE_ID);

            // 验证记录确实存在
            List<VoteRecord> allRecords = voteRecordService.getAllVoteRecords();
            int originalCount = allRecords.size();

            // 测试根据候选人ID删除投票记录
            boolean result = voteRecordService.deleteVoteRecordsByCandidate(TEST_CANDIDATE_ID);
            assertTrue("根据候选人ID删除投票记录应该成功", result);

            // 验证投票记录是否真的被删除
            List<VoteRecord> remainingRecords = voteRecordService.getAllVoteRecords();
            assertTrue("应该删除了至少2条投票记录", originalCount - remainingRecords.size() >= 2);

            // 清理剩余测试数据
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID);
            voteRecordService.deleteVoteRecordsByUser(TEST_USER_ID + 1);
        } catch (IOException e) {
            fail("根据候选人ID删除投票记录不应该抛出异常: " + e.getMessage());
        }
    }
}