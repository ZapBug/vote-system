package dao;

import bean.Candidate;
import bean.CandidateExample;
import mapper.CandidateMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;


public class CandidateDaoIm implements CandidateDao {

    private final SqlSessionFactory ssf;

    public CandidateDaoIm(SqlSessionFactory ssf) {
        this.ssf = ssf;
    }

    public boolean addVoteNum(int candidateId) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        CandidateExample example = new CandidateExample();

        int voteCount = getVoteNum(candidateId);
        Candidate candidate = new Candidate();
        candidate.setCandidateId(candidateId);
        candidate.setVoteCount(voteCount + 1);

        example.createCriteria().andCandidateIdEqualTo(candidateId);
        mapper.updateByExampleSelective(candidate, example);
        session.commit();
        session.close();
        return true;
    }


    public int getVoteNum(int candidateId) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        CandidateExample example = new CandidateExample();
        example.createCriteria().andCandidateIdEqualTo(candidateId);
        List<Candidate> candidateList = mapper.selectByExample(example);
        session.close();
        if (candidateList != null && !candidateList.isEmpty()) {
            return candidateList.get(0).getVoteCount();
        }
        return 0;
    }

    public List<Candidate> getAllCandidates() {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        CandidateExample example = new CandidateExample();
        List<Candidate> candidateList = mapper.selectByExample(example);
        session.close();
        return candidateList;
    }

    public Candidate getCandidateById(Integer candidateId) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        Candidate candidate = mapper.selectByPrimaryKey(candidateId);
        session.close();
        return candidate;
    }

    public boolean addCandidate(Candidate candidate) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        int result = mapper.insert(candidate);
        session.commit();
        session.close();
        return result > 0;
    }

    public boolean updateCandidate(Candidate candidate) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        int result = mapper.updateByPrimaryKey(candidate);
        session.commit();
        session.close();
        return result > 0;
    }

    public boolean deleteCandidate(Integer candidateId) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        int result = mapper.deleteByPrimaryKey(candidateId);
        session.commit();
        session.close();
        return result > 0;
    }

    public boolean resetVoteCount(Integer candidateId) {
        SqlSession session = ssf.openSession();
        CandidateMapper mapper = session.getMapper(CandidateMapper.class);
        Candidate candidate = new Candidate();
        candidate.setCandidateId(candidateId);
        candidate.setVoteCount(0);

        CandidateExample example = new CandidateExample();
        example.createCriteria().andCandidateIdEqualTo(candidateId);
        int result = mapper.updateByExampleSelective(candidate, example);
        session.commit();
        session.close();
        return result > 0;
    }
}