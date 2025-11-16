package dao;

import bean.User;
import bean.VoteRecord;
import bean.VoteRecordExample;
import mapper.VoteRecordMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票记录数据访问实现类
 * 实现了VoteRecordDao接口中定义的方法
 */
public class VoteRecordDaoIm implements VoteRecordDao {

    private final SqlSessionFactory ssf;

    /**
     * 构造函数
     *
     * @param ssf SqlSessionFactory对象
     */
    public VoteRecordDaoIm(SqlSessionFactory ssf) {
        this.ssf = ssf;
    }

    /**
     * 添加投票记录
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @param u           当前用户对象
     * @return 是否添加成功
     */
    public boolean addVoteRecord(int userId, int candidateId, User u) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);

        VoteRecord voteRecord = new VoteRecord();
        voteRecord.setUserId(userId);
        voteRecord.setCandidateId(candidateId);
        voteRecord.setVoteTime(new java.sql.Timestamp(System.currentTimeMillis()));

        mapper.insert(voteRecord);
        session.commit();
        session.close();
        return true;
    }

    /**
     * 检查用户是否已经对某个候选人投过票
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @return 是否已经投过票
     */
    public boolean hasVoted(int userId, int candidateId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        example.createCriteria().andUserIdEqualTo(userId).andCandidateIdEqualTo(candidateId);
        List<VoteRecord> voteRecordList = mapper.selectByExample(example);
        session.close();
        return voteRecordList != null && !voteRecordList.isEmpty();
    }

    /**
     * 根据用户ID获取投票记录列表
     *
     * @param userId 用户ID
     * @return 投票记录列表
     */
    public List<VoteRecord> getVoteRecordsByUser(int userId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<VoteRecord> voteRecordList = mapper.selectByExample(example);
        session.close();
        return voteRecordList;
    }

    /**
     * 获取所有投票记录
     *
     * @return 投票记录列表
     */
    public List<VoteRecord> getAllVoteRecords() {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        List<VoteRecord> voteRecordList = mapper.selectByExample(example);
        session.close();
        return voteRecordList;
    }

    /**
     * 根据投票记录ID获取投票记录
     *
     * @param voteId 投票记录ID
     * @return 投票记录对象
     */
    public VoteRecord getVoteRecordById(Integer voteId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecord voteRecord = mapper.selectByPrimaryKey(voteId);
        session.close();
        return voteRecord;
    }

    /**
     * 删除投票记录
     *
     * @param voteId 投票记录ID
     * @return 是否删除成功
     */
    public boolean deleteVoteRecord(Integer voteId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        int result = mapper.deleteByPrimaryKey(voteId);
        session.commit();
        session.close();
        return result > 0;
    }

    /**
     * 根据用户ID删除投票记录
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    public boolean deleteVoteRecordsByUser(Integer userId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        example.createCriteria().andUserIdEqualTo(userId);
        int result = mapper.deleteByExample(example);
        session.commit();
        session.close();
        return result > 0;
    }

    /**
     * 根据候选人ID删除投票记录
     *
     * @param candidateId 候选人ID
     * @return 是否删除成功
     */
    public boolean deleteVoteRecordsByCandidate(Integer candidateId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        example.createCriteria().andCandidateIdEqualTo(candidateId);
        int result = mapper.deleteByExample(example);
        session.commit();
        session.close();
        return result > 0;
    }

    /**
     * 获取候选人投票总数
     *
     * @param candidateId 候选人ID
     * @return 候选人获得的投票总数
     */
    public int getVoteCountForCandidate(int candidateId) {
        SqlSession session = ssf.openSession();
        VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
        VoteRecordExample example = new VoteRecordExample();
        example.createCriteria().andCandidateIdEqualTo(candidateId);
        long count = mapper.countByExample(example);
        session.close();
        return (int) count;
    }

    /**
     * 获取所有候选人的投票总数
     *
     * @return 包含候选人ID和投票数的Map
     */

    public Map<Integer, Integer> getAllVoteCounts() {
        try (SqlSession session = ssf.openSession()) {
            // Using a custom query via mapper XML would be better,
            // but for now, I'll use the available mapper methods
            VoteRecordMapper mapper = session.getMapper(VoteRecordMapper.class);
            VoteRecordExample example = new VoteRecordExample();
            List<VoteRecord> records = mapper.selectByExample(example);

            Map<Integer, Integer> voteCounts = new HashMap<>();
            for (VoteRecord record : records) {
                int candidateId = record.getCandidateId();
                voteCounts.put(candidateId, voteCounts.getOrDefault(candidateId, 0) + 1);
            }
            return voteCounts;
        }
    }
}