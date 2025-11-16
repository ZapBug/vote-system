package bean;

/**
 * 候选人实体类
 * 用于表示投票系统中的候选人信息
 */
public class Candidate {
    /**
     * 候选人ID
     */
    private Integer candidateId;

    /**
     * 候选人姓名
     */
    private String candidateName;

    /**
     * 候选人描述
     */
    private String candidateDescription;

    /**
     * 候选人照片路径
     */
    private String candidatePhoto;

    /**
     * 候选人获得的票数
     */
    private Integer voteCount;

    /**
     * 无参构造函数
     */
    public Candidate() {
        super();
    }

    /**
     * 有参构造函数
     *
     * @param candidateId          候选人ID
     * @param candidateName        候选人姓名
     * @param candidateDescription 候选人描述
     * @param candidatePhoto       候选人照片路径
     * @param voteCount            候选人获得的票数
     */
    public Candidate(Integer candidateId, String candidateName, String candidateDescription, String candidatePhoto, Integer voteCount) {
        super();
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.candidateDescription = candidateDescription;
        this.candidatePhoto = candidatePhoto;
        this.voteCount = voteCount;
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
     * 获取候选人姓名
     *
     * @return 候选人姓名
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * 设置候选人姓名
     *
     * @param candidateName 候选人姓名
     */
    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName == null ? null : candidateName.trim();
    }

    /**
     * 获取候选人描述
     *
     * @return 候选人描述
     */
    public String getCandidateDescription() {
        return candidateDescription;
    }

    /**
     * 设置候选人描述
     *
     * @param candidateDescription 候选人描述
     */
    public void setCandidateDescription(String candidateDescription) {
        this.candidateDescription = candidateDescription == null ? null : candidateDescription.trim();
    }

    /**
     * 获取候选人照片路径
     *
     * @return 候选人照片路径
     */
    public String getCandidatePhoto() {
        return candidatePhoto;
    }

    /**
     * 设置候选人照片路径
     *
     * @param candidatePhoto 候选人照片路径
     */
    public void setCandidatePhoto(String candidatePhoto) {
        this.candidatePhoto = candidatePhoto == null ? null : candidatePhoto.trim();
    }

    /**
     * 获取候选人获得的票数
     *
     * @return 候选人获得的票数
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * 设置候选人获得的票数
     *
     * @param voteCount 候选人获得的票数
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}