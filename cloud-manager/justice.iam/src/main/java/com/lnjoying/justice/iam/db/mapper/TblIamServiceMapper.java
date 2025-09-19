package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamService;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
@MapperModel(IamResources.SERVICE)
public interface TblIamServiceMapper
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
    int insert(TblIamService record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamService record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamService selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamService record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamService record);

    Integer countByName(@Param("name") String name);

    Integer countByIamCode(@Param("iamCode")String iamCode);


    Integer countById(@Param("id") String id);

    List<TblIamService> selectAll(@Param("name") String name, @Param("enable") Integer enable);

    String selectNameById(@Param("id")String id);

	String selectDisplayNameById(@Param("id")String id);

    String selectIdByName(@Param("name")String name);

    String selectIamCodeById(@Param("id")String id);

    TblIamService selectByIamCode(@Param("iamCode")String iamCode);


}