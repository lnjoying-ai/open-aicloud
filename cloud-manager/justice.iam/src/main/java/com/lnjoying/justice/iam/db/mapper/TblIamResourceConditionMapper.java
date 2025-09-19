package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamResourceCondition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblIamResourceConditionMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblIamResourceCondition record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamResourceCondition record);
}