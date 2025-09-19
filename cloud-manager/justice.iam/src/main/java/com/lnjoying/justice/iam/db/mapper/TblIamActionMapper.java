package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamAction;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblIamActionMapper
{
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblIamAction record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamAction record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamAction selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamAction record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamAction record);

    List<TblIamAction> selectAll();

    //List<TblIamAction> selectAllByResourceId(@Param("resourceId") String resourceId);
    List<TblIamAction> selectAllByResourceId(@Param("resourceId")String resourceId);

    Integer countByResourceId(@Param("resourceId")String resourceId);

    TblIamAction selectByResourceIdAndActionName(@Param("resourceId")String resourceId,@Param("actionName")String actionName);



    TblIamAction selectByActionName(@Param("actionName") String actionName);

    List<ActionsCache.ServiceAction> selectAllServiceActions();


}