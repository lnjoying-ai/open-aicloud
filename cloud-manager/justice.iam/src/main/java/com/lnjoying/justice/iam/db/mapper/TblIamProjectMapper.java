package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.Collection;import java.util.List;

@Mapper
@MapperModel(IamResources.PROJECT)
public interface TblIamProjectMapper
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
    int insert(TblIamProject record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamProject record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamProject selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamProject record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamProject record);

    List<TblIamProject> selectByNameAndBpId(@Param("name") String name, @Param("bpId") String bpId, @Param("type")Integer type);


    Integer countByNameAndBpId(@Param("name") String name, @Param("bpId") String bpId);

    Integer countByIdAndBpId(@Param("id") String id, @Param("bpId") String bpId);

    List<TblIamProjectDetail> selectAll(@Param("name") String name, @Param("enable") Integer enable, @Param("bpId") String bpId, @Param("nullParent") Boolean nullParent);

    TblIamProjectDetail selectByIdAndBpId(@Param("id") String id, @Param("bpId") String bpId);

    String selectNameById(@Param("id") String id);

    String selectDisplayNameById(@Param("id") String id);

    String selectLatestNameByName(@Param("name") String name);

    int batchInsert(@Param("list") List<TblIamProject> list);

    TblIamProject selectDefaultProject(@Param("name") String name, @Param("userId") String userId, @Param("defaultAdminProjectName") String defaultAdminProjectName, @Param("type")Integer type);

    List<TblIamProjectDetail> selectAllChildren(@Param("idCollection") Collection<String> idCollection);

    List<TblIamProject> selectByName(@Param("name")String name);


}