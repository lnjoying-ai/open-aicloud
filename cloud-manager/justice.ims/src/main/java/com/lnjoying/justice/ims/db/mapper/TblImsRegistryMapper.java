package com.lnjoying.justice.ims.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import org.apache.ibatis.annotations.Param;

import com.lnjoying.justice.ims.db.model.TblImsRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@MapperModel(ImsResources.REGISTRY)
public interface TblImsRegistryMapper
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
    int insert(TblImsRegistry record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistry record);

    /**
     * select by primary key
     *
     * @param registryId primary key
     * @return object by primary key
     */
    TblImsRegistry selectByPrimaryKey(String registryId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblImsRegistry record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblImsRegistry record);

    /**
     * Select by condition
     *
     * @param registryIds
     * @param likeRegistryName
     * @param userId
     * @param statusCollection
     * @return
     */
    List<TblImsRegistry> selectAll(@Param("registryIds") Set<String> registryIds, @Param("likeRegistryName") String likeRegistryName, @Param("userId") String userId,
                                   @Param("statusCollection") Collection<Integer> statusCollection);

    /**
     * Select a registry in the enable status
     *
     * @return
     */
    TblImsRegistry selectOne();

    /**
     * Query through the status list
     *
     * @param list
     * @return
     */
    List<TblImsRegistry> selectAllByStatus(@Param("list") List<Integer> list);

    /**
     * query by regionId
     *
     * @param regionId
     * @return
     */
    List<TblImsRegistry> selectByRegionId(@Param("regionId") String regionId);

    /**
     * query by registry name
     *
     * @param registryName
     * @return
     */
    List<TblImsRegistry> selectByRegistryName(@Param("registryName") String registryName);


    TblImsRegistry selectByUrl(@Param("url")String url);


}