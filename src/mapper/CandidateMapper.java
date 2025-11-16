package mapper;

import bean.Candidate;
import bean.CandidateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CandidateMapper {
    long countByExample(CandidateExample example);

    int deleteByExample(CandidateExample example);

    int deleteByPrimaryKey(Integer candidateId);

    int insert(Candidate record);

    int insertSelective(Candidate record);

    List<Candidate> selectByExample(CandidateExample example);

    Candidate selectByPrimaryKey(Integer candidateId);

    int updateByExampleSelective(@Param("record") Candidate record, @Param("example") CandidateExample example);

    int updateByExample(@Param("record") Candidate record, @Param("example") CandidateExample example);

    int updateByPrimaryKeySelective(Candidate record);

    int updateByPrimaryKey(Candidate record);
}