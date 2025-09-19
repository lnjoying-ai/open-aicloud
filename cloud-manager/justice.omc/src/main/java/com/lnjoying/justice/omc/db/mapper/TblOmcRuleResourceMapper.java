package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcRuleResource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblOmcRuleResourceMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblOmcRuleResource record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcRuleResource record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TblOmcRuleResource selectByPrimaryKey(String id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcRuleResource record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcRuleResource record);
}