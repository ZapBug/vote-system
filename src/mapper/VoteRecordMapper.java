package mapper;

import bean.VoteRecord;
import bean.VoteRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoteRecordMapper {
    long countByExample(VoteRecordExample example);

    int deleteByExample(VoteRecordExample example);

    int deleteByPrimaryKey(Integer voteId);

    int insert(VoteRecord record);

    int insertSelective(VoteRecord record);

    List<VoteRecord> selectByExample(VoteRecordExample example);

    VoteRecord selectByPrimaryKey(Integer voteId);

    int updateByExampleSelective(@Param("record") VoteRecord record, @Param("example") VoteRecordExample example);

    int updateByExample(@Param("record") VoteRecord record, @Param("example") VoteRecordExample example);

    int updateByPrimaryKeySelective(VoteRecord record);

    int updateByPrimaryKey(VoteRecord record);
}