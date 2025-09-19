package com.lnjoying.justice.ims.db.mapper;

import java.util.List;
import java.util.Set;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.db.model.TblImsRegistryRegion;
import com.lnjoying.justice.ims.domain.model.RegistryRegion;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;

public interface TblImsRegistryRegionMapper
{
    /**
     * delete by primary key
     *
     * @param registryId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@Param("registryId") String registryId, @Param("regionId") String regionId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblImsRegistryRegion record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblImsRegistryRegion record);

    /**
     * batch insert data
     *
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<TblImsRegistryRegion> list);

    /**
     * right fuzzy match
     *
     * @param likeRegionId
     * @return
     */
    List<TblImsRegistryRegion> selectByRegionIdLike(@Param("likeRegionId") String likeRegionId);

    /**
     * delete by registryId
     *
     * @param registryId
     * @return
     */
    int deleteByRegistryId(@Param("registryId") String registryId);

    /**
     * get registryRegions by registry id
     *
     * @param registryId
     * @return
     */
    List<TblImsRegistryRegion> selectByRegistryId(@Param("registryId") String registryId);

    /**
     * batch delete by regionIds
     *
     * @param regionIds
     * @return
     */
    int deleteByRegionIds(@Param("regionIds") Set<String> regionIds);

    /**
     * select all  regions which registry is enable
     * @return regionId list
     */
    List<TblImsRegistryRegion> selectAllEnableRegistryRegions();

}