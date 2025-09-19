package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamResourceAction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblIamResourceActionMapper {
    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblIamResourceAction record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamResourceAction record);

}