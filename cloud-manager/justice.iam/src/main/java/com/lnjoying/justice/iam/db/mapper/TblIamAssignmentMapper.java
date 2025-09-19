package com.lnjoying.justice.iam.db.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.iam.db.model.TblIamAssignment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblIamAssignmentMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblIamAssignment record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamAssignment record);

    int batchInsert(@Param("list")List<TblIamAssignment> list);

    int deleteByActorIdAndTargetIdAndType(@Param("actorId")String actorId,@Param("targetId")String targetId,@Param("type")Integer type);

    List<TblIamAssignment> selectByTargetIdAndType(@Param("targetId")String targetId,@Param("type")Integer type);

    Integer countByTargetIdAndType(@Param("targetId")String targetId,@Param("type")Integer type);


}