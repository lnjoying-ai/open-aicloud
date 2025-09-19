package com.lnjoying.justice.ims.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;

@MapperModel(ImsResources.THIRD_PARTY_REGISTRY)
public interface TblImsRegistry3rdMapper
{
    /**
     * delete by primary key
     *
     * @param registryId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String registryId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblImsRegistry3rd record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistry3rd record);

    /**
     * select by primary key
     *
     * @param registryId primary key
     * @return object by primary key
     */
    TblImsRegistry3rd selectByPrimaryKey(String registryId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblImsRegistry3rd record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblImsRegistry3rd record);

    /**
     * select by condition
     *
     * @param likeRegistryName
     * @param status
     * @param userId
     * @return
     */
    List<TblImsRegistry3rd> selectAll(@Param("likeRegistryName") String likeRegistryName, @Param("status") Integer status, @Param("bpId") String bpId, @Param("userId") String userId);

    /**
     * select by registryName
     *
     * @param registryName
     * @return
     */
    List<TblImsRegistry3rd> selectByRegistryName(@Param("registryName") String registryName);


    List<TblImsRegistry3rd> selectByUrlAndAccessKey(@Param("url")String url,@Param("accessKey")String accessKey);


}