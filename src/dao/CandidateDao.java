package dao;

import bean.Candidate;

import java.util.List;

/**
 * 候选人数据访问接口
 * 定义了对候选人表的各种操作方法
 */
public interface CandidateDao {

    /**
     * 增加候选人票数
     *
     * @param candidateId 候选人ID
     * @return 是否增加成功
     */
    boolean addVoteNum(int candidateId);

    /**
     * 获取候选人票数
     *
     * @param candidateId 候选人ID
     * @return 候选人票数
     */
    int getVoteNum(int candidateId);

    /**
     * 获取所有候选人
     *
     * @return 候选人列表
     */
    List<Candidate> getAllCandidates();

    /**
     * 根据候选人ID获取候选人信息
     *
     * @param candidateId 候选人ID
     * @return 候选人对象
     */
    Candidate getCandidateById(Integer candidateId);

    /**
     * 添加候选人
     *
     * @param candidate 候选人对象
     * @return 是否添加成功
     */
    boolean addCandidate(Candidate candidate);

    /**
     * 更新候选人信息
     *
     * @param candidate 候选人对象
     * @return 是否更新成功
     */
    boolean updateCandidate(Candidate candidate);

    /**
     * 删除候选人
     *
     * @param candidateId 候选人ID
     * @return 是否删除成功
     */
    boolean deleteCandidate(Integer candidateId);

    /**
     * 重置候选人票数
     *
     * @param candidateId 候选人ID
     * @return 是否重置成功
     */
    boolean resetVoteCount(Integer candidateId);
}