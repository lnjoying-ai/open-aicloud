package com.lnjoying.justice.ims.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryMapper;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryRegionMapper;
import com.lnjoying.justice.ims.db.model.RecordStatus;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryRegion;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistriesRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistry;
import com.lnjoying.justice.ims.domain.model.RegistryRegion;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.DELETED;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.DISABLE;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.UNAVAILABLE;
import static com.lnjoying.justice.schema.common.ErrorCode.NO_AVAILABLE_REGISTRY;
import static com.lnjoying.justice.schema.common.ErrorCode.REGISTRY_UNAVAILABLE;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * registry repository
 *
 * @author merak
 **/

@Repository
public class ImsRegistryRepository
{

    @Autowired
    private TblImsRegistryMapper registryMapper;

    @Autowired
    private TblImsRegistryRegionMapper registryRegionMapper;

    /**
     * insert record selective
     *
     * @param record
     * @return
     */
    public void insertSelective(TblImsRegistry record)
    {
        registryMapper.insertSelective(record);
    }

    public RegistriesRsp getRegistryList(String name, Set<String> registryIds, String userId, Collection<Integer> status, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblImsRegistry> imsRegionRegistries = registryMapper.selectAll(registryIds, name, userId, status);
        PageInfo<TblImsRegistry> pageInfo = new PageInfo<>(imsRegionRegistries);
        return RegistriesRsp.builder().totalNum(pageInfo.getTotal())
                .registries(imsRegionRegistries.stream().map(ImsRegistry::of).collect(Collectors.toList())).build();
    }

    public TblImsRegistry getRegistry(String registryId, String operUserId)
    {
        Assert.hasText(registryId, "The given registryId must not be null!");
        TblImsRegistry imsRegistry = registryMapper.selectByPrimaryKey(registryId);
        if (Objects.isNull(imsRegistry))
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_NOT_EXIST, INFO);
        }

        if (DELETED.value() == imsRegistry.getStatus().intValue())
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_DROPPED, INFO);
        }

        if (DISABLE.value() == imsRegistry.getStatus().intValue())
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_DISABLE, INFO);
        }

        if (UNAVAILABLE >= imsRegistry.getStatus().intValue())
        {
            throw new ImsWebSystemException(REGISTRY_UNAVAILABLE, INFO);
        }

        if (StringUtils.isNotBlank(operUserId))
        {
            if (!operUserId.equals(imsRegistry.getUserId()))
            {
                throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        return imsRegistry;
    }

    public TblImsRegistry getRegistryDetail(String registryId, String operUserId)
    {
        Assert.hasText(registryId, "The given registryId must not be null!");
        TblImsRegistry imsRegistry = registryMapper.selectByPrimaryKey(registryId);
        if (Objects.isNull(imsRegistry))
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_NOT_EXIST, INFO);
        }

        if (DELETED.value() == imsRegistry.getStatus().intValue())
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_DROPPED, INFO);
        }

        if (StringUtils.isNotBlank(operUserId))
        {
            if (!operUserId.equals(imsRegistry.getUserId()))
            {
                throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        return imsRegistry;
    }

    public TblImsRegistry getRegistryDoNotCheckStatus(String registryId, String operUserId)
    {
        Assert.hasText(registryId, "The given registryId must not be null!");
        TblImsRegistry imsRegistry = registryMapper.selectByPrimaryKey(registryId);
        if (Objects.isNull(imsRegistry))
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_NOT_EXIST, INFO);
        }

        if (StringUtils.isNotBlank(operUserId))
        {
            if (!operUserId.equals(imsRegistry.getUserId()))
            {
                throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        return imsRegistry;
    }

    public List<TblImsRegistry> getRegistryByName(String registryName)
    {
        Assert.hasText(registryName, "The given registryName must not be null!");
        List<TblImsRegistry> imsRegistry = registryMapper.selectByRegistryName(registryName);
        return imsRegistry;
    }

    public void updateRegistry(TblImsRegistry imsRegistry)
    {
        registryMapper.updateByPrimaryKeySelective(imsRegistry);
    }

    public int insertRegistryRegion(List<TblImsRegistryRegion> registryRegions)
    {
        return registryRegionMapper.batchInsert(registryRegions);
    }

    /**
     * get registryId list by regionId right fuzzy match
     *
     * @param regionId
     * @return registryId list
     */
    public Set<String> selectByRegionId(String regionId)
    {
        return registryRegionMapper.selectByRegionIdLike(regionId)
                .stream().map(record -> record.getRegistryId()).collect(Collectors.toSet());
    }

    public int deleteRegistryRegionsByRegistryId(@NotNull String registryId)
    {
        Assert.hasText(registryId, "The given registryId must not be null!");
        return registryRegionMapper.deleteByRegistryId(registryId);
    }

    /**
     * get regionIds by registry id
     *
     * @param registryId
     * @return regionIds
     */
    public Set<String> selectRegistryRegionsByRegistryId(String registryId)
    {
        return registryRegionMapper.selectByRegistryId(registryId)
                .stream().map(record -> record.getRegionId()).collect(Collectors.toSet());
    }

    /**
     * Select a registry in the enable status
     *
     * @return
     */
    public TblImsRegistry selectOne()
    {
        TblImsRegistry tblImsRegistry = registryMapper.selectOne();
        if (Objects.isNull(tblImsRegistry))
        {
            throw new ImsWebSystemException(NO_AVAILABLE_REGISTRY, INFO);
        }
        return tblImsRegistry;
    }

    /**
     * Query through the status list
     *
     * @param statusList
     * @return
     */
    public List<TblImsRegistry> selectAllByStatus(List<Integer> statusList)
    {
        Assert.notNull(statusList, "The given status must not be null!");
        return registryMapper.selectAllByStatus(statusList);
    }

    /**
     * Get a list of all registry except deleted
     *
     * @return
     */
    public List<TblImsRegistry> selectAll()
    {
        return registryMapper.selectAll(null, null, null, null);
    }

    public List<TblImsRegistry> getRegistryByRegionId(String regionId)
    {
        Assert.notNull(regionId, "The given regionId must not be null!");
        return registryMapper.selectByRegionId(regionId);
    }

    public void deleteByRegions(HashSet<String> regions)
    {
        registryRegionMapper.deleteByRegionIds(regions);
    }

    public int deleteByPrimaryKey(String registryId)
    {
        return registryMapper.deleteByPrimaryKey(registryId);
    }

    public List<TblImsRegistryRegion> selectAllEnableRegistryRegions()
    {
        return registryRegionMapper.selectAllEnableRegistryRegions();
    }


    public TblImsRegistry selectByUrl(String url)
    {
        return registryMapper.selectByUrl(url);
    }

    public TblImsRegistry selectByPrimaryKey(String registryId)
    {
        return registryMapper.selectByPrimaryKey(registryId);
    }
}
