package com.lnjoying.justice.ims.db.mapper;

import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import org.apache.ibatis.annotations.Param;

@MapperModel(ImsResources.PROJECT)
public interface TblImsRegistryProjectMapper
{
    /**
     * delete by primary key
     *
     * @param projectId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String projectId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblImsRegistryProject record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistryProject record);

    /**
     * select by primary key
     *
     * @param projectId primary key
     * @return object by primary key
     */
    TblImsRegistryProject selectByPrimaryKey(String projectId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblImsRegistryProject record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblImsRegistryProject record);

    /**
     * Query by projectName
     *
     * @param projectName
     * @return
     */
    TblImsRegistryProject selectByProjectName(@Param("projectName") String projectName);

    /**
     * query all
     *
     * @param registryId
     * @param likeProjectName
     * @param scope
     * @param userId
     * @param bpId
     * @param status
     * @return
     */
    List<TblImsRegistryProject> selectAll(@Param("registryId") String registryId, @Param("likeProjectName") String likeProjectName, @Param("scope") Integer scope, @Param("userId") String userId, @Param("bpId") String bpId, @Param("status") Integer status);

    /**
     * Ordinary tenants inquire about projects created by themselves,
     * projects of bp, and projects disclosed by the admin
     *
     * @param registryId
     * @param likeProjectName
     * @param scope
     * @param userId
     * @param bpId
     * @param status
     * @return
     */
    List<TblImsRegistryProject> selectAllOrdinaryTenant(@Param("registryId") String registryId, @Param("likeProjectName") String likeProjectName, @Param("scope") Integer scope, @Param("userId") String userId, @Param("bpId") String bpId, @Param("status") Integer status);


    /**
     * query by scope
     *
     * @param scope
     * @return
     */
    List<TblImsRegistryProject> selectByScope(@Param("scope") Integer scope);

    /**
     * query by registryId
     * @param registryId
     * @return
     */
    List<TblImsRegistryProject> selectByRegistryId(@Param("registryId")String registryId);

}