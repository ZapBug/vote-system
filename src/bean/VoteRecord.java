package bean;

/**
 * 投票记录实体类
 * 用于表示用户对候选人的投票记录
 */
public class VoteRecord {
    /**
     * 投票记录ID
     */
    private Integer voteId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 候选人ID
     */
    private Integer candidateId;

    /**
     * 投票时间
     */
    private java.sql.Timestamp voteTime;

    /**
     * 无参构造函数
     */
    public VoteRecord() {
        super();
    }

    /**
     * 有参构造函数
     *
     * @param voteId      投票记录ID
     * @param userId      用户ID
     * @param candidateId 候选人ID
     * @param voteTime    投票时间
     */
    public VoteRecord(Integer voteId, Integer userId, Integer candidateId, java.sql.Timestamp voteTime) {
        super();
        this.voteId = voteId;
        this.userId = userId;
        this.candidateId = candidateId;
        this.voteTime = voteTime;
    }

    /**
     * 获取投票记录ID
     *
     * @return 投票记录ID
     */
    public Integer getVoteId() {
        return voteId;
    }

    /**
     * 设置投票记录ID
     *
     * @param voteId 投票记录ID
     */
    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取候选人ID
     *
     * @return 候选人ID
     */
    public Integer getCandidateId() {
        return candidateId;
    }

    /**
     * 设置候选人ID
     *
     * @param candidateId 候选人ID
     */
    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    /**
     * 获取投票时间
     *
     * @return 投票时间
     */
    public java.sql.Timestamp getVoteTime() {
        return voteTime;
    }

    /**
     * 设置投票时间
     *
     * @param voteTime 投票时间
     */
    public void setVoteTime(java.sql.Timestamp voteTime) {
        this.voteTime = voteTime;
    }
}