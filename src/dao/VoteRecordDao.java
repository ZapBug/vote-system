package dao;

import bean.User;
import bean.VoteRecord;

import java.util.List;
import java.util.Map;

/**
 * 投票记录数据访问接口
 * 定义了对投票记录表的各种操作方法
 */
public interface VoteRecordDao {

    /**
     * 添加投票记录
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @param u           当前用户对象
     * @return 是否添加成功
     */
    boolean addVoteRecord(int userId, int candidateId, User u);

    /**
     * 检查用户是否已经对某个候选人投过票
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @return 是否已经投过票
     */
    boolean hasVoted(int userId, int candidateId);

    /**
     * 根据用户ID获取投票记录列表
     *
     * @param userId 用户ID
     * @return 投票记录列表
     */
    List<VoteRecord> getVoteRecordsByUser(int userId);

    /**
     * 获取所有投票记录
     *
     * @return 投票记录列表
     */
    List<VoteRecord> getAllVoteRecords();

    /**
     * 根据投票记录ID获取投票记录
     *
     * @param voteId 投票记录ID
     * @return 投票记录对象
     */
    VoteRecord getVoteRecordById(Integer voteId);

    /**
     * 删除投票记录
     *
     * @param voteId 投票记录ID
     * @return 是否删除成功
     */
    boolean deleteVoteRecord(Integer voteId);

    /**
     * 根据用户ID删除投票记录
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteVoteRecordsByUser(Integer userId);

    /**
     * 根据候选人ID删除投票记录
     *
     * @param candidateId 候选人ID
     * @return 是否删除成功
     */
    boolean deleteVoteRecordsByCandidate(Integer candidateId);

    /**
     * 获取候选人投票总数
     *
     * @param candidateId 候选人ID
     * @return 候选人获得的投票总数
     */
    int getVoteCountForCandidate(int candidateId);

    /**
     * 获取所有候选人的投票总数
     *
     * @return 包含候选人ID和投票数的Map
     */
    Map<Integer, Integer> getAllVoteCounts();
}