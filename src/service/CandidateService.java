package service;

import bean.Candidate;
import dao.CandidateDaoIm;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 候选人业务逻辑处理类
 * 提供候选人相关的业务操作方法
 */
public class CandidateService {

    // 避免重复创建服务实例
    private final VoteRecordService voteRecordService;

    public CandidateService() {
        this.voteRecordService = new VoteRecordService();
    }

    /**
     * 查询候选人的票数
     *
     * @param candidateId 候选人ID
     * @return 候选人的票数
     * @throws IOException 配置文件读取异常
     */
    public int selectVoteNum(int candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).getVoteNum(candidateId);
    }

    /**
     * 增加候选人的票数
     *
     * @param candidateId 候选人ID
     * @return 是否增加成功
     * @throws IOException 配置文件读取异常
     */
    public boolean addVoteNum(int candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).addVoteNum(candidateId);
    }

    /**
     * 获取所有候选人信息
     * 从vote_record表获取准确的票数以确保数据一致性
     *
     * @return 候选人列表
     * @throws IOException 配置文件读取异常
     */
    public List<Candidate> getAllCandidates() throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        List<Candidate> candidates = new CandidateDaoIm(ssf).getAllCandidates();

        // 根据vote_record表更新票数以确保准确性
        // 一次性获取所有票数以提高效率
        java.util.Map<Integer, Integer> voteCounts = this.voteRecordService.getAllVoteCounts();
        for (Candidate candidate : candidates) {
            Integer count = voteCounts.get(candidate.getCandidateId());
            candidate.setVoteCount(count != null ? count : 0);
        }

        return candidates;
    }

    /**
     * 从vote_record表计算实际票数
     *
     * @param candidateId 候选人ID
     * @return 来自投票记录的实际票数
     * @throws IOException 配置文件读取异常
     */
    public int getActualVoteCount(int candidateId) throws IOException {
        return this.voteRecordService.getVoteCountForCandidate(candidateId);
    }

    /**
     * 根据候选人ID获取候选人信息
     *
     * @param candidateId 候选人ID
     * @return 候选人对象
     * @throws IOException 配置文件读取异常
     */
    public Candidate getCandidateById(Integer candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).getCandidateById(candidateId);
    }

    /**
     * 添加候选人
     *
     * @param candidate 候选人对象
     * @return 是否添加成功
     * @throws IOException 配置文件读取异常
     */
    public boolean addCandidate(Candidate candidate) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).addCandidate(candidate);
    }

    /**
     * 更新候选人信息
     *
     * @param candidate 候选人对象
     * @return 是否更新成功
     * @throws IOException 配置文件读取异常
     */
    public boolean updateCandidate(Candidate candidate) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).updateCandidate(candidate);
    }

    /**
     * 删除候选人
     *
     * @param candidateId 候选人ID
     * @return 是否删除成功
     * @throws IOException 配置文件读取异常
     */
    public boolean deleteCandidate(Integer candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).deleteCandidate(candidateId);
    }

    /**
     * 重置候选人票数
     *
     * @param candidateId 候选人ID
     * @return 是否重置成功
     * @throws IOException 配置文件读取异常
     */
    public boolean resetVoteCount(Integer candidateId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);
        return new CandidateDaoIm(ssf).resetVoteCount(candidateId);
    }
}