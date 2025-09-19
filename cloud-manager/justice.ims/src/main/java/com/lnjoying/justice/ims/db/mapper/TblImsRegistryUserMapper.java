package com.lnjoying.justice.ims.db.mapper;

import java.util.Collection;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MapperModel(ImsResources.USER)
public interface TblImsRegistryUserMapper
{
    /**
     * delete by primary key
     *
     * @param registryUserId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String registryUserId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblImsRegistryUser record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistryUser record);

    /**
     * select by primary key
     *
     * @param registryUserId primary key
     * @return object by primary key
     */
    TblImsRegistryUser selectByPrimaryKey(String registryUserId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblImsRegistryUser record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblImsRegistryUser record);

    /**
     * select by user name
     *
     * @param registryUserName
     * @return
     */
    TblImsRegistryUser selectByRegistryUserName(@Param("registryUserName") String registryUserName);

    /**
     * Get on condition
     *
     * @param registryUserName
     * @param bpId
     * @param userId
     * @return
     */
    List<TblImsRegistryUser> selectAll(@Param("registryUserName") String registryUserName, @Param("bpId") String bpId, @Param("userId") String userId);

    /**
     * select by bpId
     *
     * @param bpId
     * @return
     */
    List<TblImsRegistryUser> selectByBpId(@Param("bpId") String bpId);

    /**
     * select by userId
     *
     * @param userId
     * @return
     */
    List<TblImsRegistryUser> selectByUserId(@Param("userId") String userId);

    /**
     * Find by status
     *
     * @param list
     * @return
     */
    List<TblImsRegistryUser> selectAllByStatus(@Param("list") List<Integer> list);


    List<TblImsRegistryUser> selectByUserName(@Param("userName")String userName);


}