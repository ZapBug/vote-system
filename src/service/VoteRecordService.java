package service;

import bean.VoteRecord;
import dao.VoteRecordDaoIm;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 投票记录业务逻辑处理类
 * 提供投票记录相关的业务操作方法
 */
public class VoteRecordService {

    /**
     * 添加投票记录
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @return 是否添加成功
     * @throws IOException 配置文件读取异常
     */
    public boolean addVoteRecord(int userId, int candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).addVoteRecord(userId, candidateId, null);
    }

    /**
     * 检查用户是否已经对某个候选人投过票
     *
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @return 是否已经投过票
     * @throws IOException 配置文件读取异常
     */
    public boolean hasVoted(int userId, int candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).hasVoted(userId, candidateId);
    }

    /**
     * 根据用户ID获取投票记录列表
     *
     * @param userId 用户ID
     * @return 投票记录列表
     * @throws IOException 配置文件读取异常
     */
    public List<VoteRecord> getVoteRecordsByUser(int userId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).getVoteRecordsByUser(userId);
    }

    /**
     * 获取所有投票记录
     *
     * @return 投票记录列表
     * @throws IOException 配置文件读取异常
     */
    public List<VoteRecord> getAllVoteRecords() throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).getAllVoteRecords();
    }

    /**
     * 根据投票记录ID获取投票记录
     *
     * @param voteId 投票记录ID
     * @return 投票记录对象
     * @throws IOException 配置文件读取异常
     */
    public VoteRecord getVoteRecordById(Integer voteId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).getVoteRecordById(voteId);
    }

    /**
     * 删除投票记录
     *
     * @param voteId 投票记录ID
     * @return 是否删除成功
     * @throws IOException 配置文件读取异常
     */
    public boolean deleteVoteRecord(Integer voteId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).deleteVoteRecord(voteId);
    }

    /**
     * 根据用户ID删除投票记录
     *
     * @param userId 用户ID
     * @return 是否删除成功
     * @throws IOException 配置文件读取异常
     */
    public boolean deleteVoteRecordsByUser(Integer userId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).deleteVoteRecordsByUser(userId);
    }

    /**
     * 根据候选人ID删除投票记录
     *
     * @param candidateId 候选人ID
     * @return 是否删除成功
     * @throws IOException 配置文件读取异常
     */
    public boolean deleteVoteRecordsByCandidate(Integer candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).deleteVoteRecordsByCandidate(candidateId);
    }

    /**
     * 获取候选人投票总数
     *
     * @param candidateId 候选人ID
     * @return 候选人获得的投票总数
     * @throws IOException 配置文件读取异常
     */
    public int getVoteCountForCandidate(int candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).getVoteCountForCandidate(candidateId);
    }

    /**
     * 获取所有候选人的投票总数
     *
     * @return 包含候选人ID和投票数的Map
     * @throws IOException 配置文件读取异常
     */
    public Map<Integer, Integer> getAllVoteCounts() throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new VoteRecordDaoIm(ssf).getAllVoteCounts();
    }
}