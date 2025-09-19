package com.lnjoying.justice.ims.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistry3rdMapper;
import com.lnjoying.justice.ims.db.model.RecordStatus;
import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import com.lnjoying.justice.ims.domain.dto.rsp.Registries3rdRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistry;
import com.lnjoying.justice.ims.domain.model.ImsRegistry3rd;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * third party registry repository
 *
 * @author merak
 **/

@Repository
public class ImsRegistry3rdRepository
{
    public static final String DEFAULT_REGISTRY_ID = "defaultdockerio";

    public static final String DOCKER_IO_URL = "docker.io";

    @Autowired
    private TblImsRegistry3rdMapper registry3rdMapper;

    public void insertSelective(TblImsRegistry3rd record)
    {
        registry3rdMapper.insertSelective(record);
    }

    public Registries3rdRsp getRegistry3rdList(String registryName, String bpId, String userId, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblImsRegistry3rd> tblImsRegistry3rds = registry3rdMapper.selectAll(registryName, RecordStatus.NORMAL.value(), bpId, userId);
        PageInfo<TblImsRegistry3rd> pageInfo = new PageInfo<>(tblImsRegistry3rds);
        return Registries3rdRsp.builder().totalNum(pageInfo.getTotal())
                .registries(tblImsRegistry3rds.stream().map(ImsRegistry3rd::of).collect(Collectors.toList())).build();
    }

    public TblImsRegistry3rd getRegistry3rd(String registryId, String bpId, String operUserId)
    {
        TblImsRegistry3rd tblImsRegistry3rd = registry3rdMapper.selectByPrimaryKey(registryId);
        if (Objects.isNull(tblImsRegistry3rd))
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_3RD_NOT_EXIST, ErrorLevel.INFO);
        }

        if (RecordStatus.DELETED.value() == tblImsRegistry3rd.getStatus())
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_DROPPED, ErrorLevel.INFO);
        }

        if (StringUtils.isNotBlank(bpId))
        {
            if (!bpId.equals(tblImsRegistry3rd.getBpId()))
            {
                throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        if (StringUtils.isNotBlank(operUserId))
        {
            if (!operUserId.equals(tblImsRegistry3rd.getUserId()))
            {
                throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
            }
        }

        return tblImsRegistry3rd;
    }

    public void checkOperUser(String registryId, String bpId, String operUserId)
    {
        getRegistry3rd(registryId, bpId, operUserId);
    }

    public void updateRegistry3rd(TblImsRegistry3rd record)
    {
        registry3rdMapper.updateByPrimaryKeySelective(record);
    }

    public List<TblImsRegistry3rd> selectByRegistryName(String registryName)
    {
        Assert.hasText(registryName, "The given registryName must not be null!");
        List<TblImsRegistry3rd> tblImsRegistry3rds = registry3rdMapper.selectByRegistryName(registryName);
        return tblImsRegistry3rds;
    }

    /**
     * add default docker hub registry
     */
    public void addDefaultRegistry3rd()
    {

        TblImsRegistry3rd tblImsRegistry3rd = registry3rdMapper.selectByPrimaryKey(DEFAULT_REGISTRY_ID);
        if (Objects.isNull(tblImsRegistry3rd))
        {
            // The default docker hub does not require an accessKey accessSecret,
            TblImsRegistry3rd defaultDockerHubRegistry = TblImsRegistry3rd.builder().registryId(DEFAULT_REGISTRY_ID)
                    .registryName("default-docker-hub").registryDesc("默认的docker hub 仓库")
                    .url(DOCKER_IO_URL)
                    .type(1)
                    .userId(UUID.randomUUID().toString())
                    .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .build();
            registry3rdMapper.insertSelective(defaultDockerHubRegistry);
        }

    }

    public int deleteByPrimaryKey(String registryId)
    {
        return registry3rdMapper.deleteByPrimaryKey(registryId);
    }

    public List<TblImsRegistry3rd> selectByUrlAndAccessKey(String url,String accessKey)
    {
        return registry3rdMapper.selectByUrlAndAccessKey(url, accessKey);
    }

    public TblImsRegistry3rd selectByPrimaryKey(String registryId)
    {
        return registry3rdMapper.selectByPrimaryKey(registryId);
    }
}
