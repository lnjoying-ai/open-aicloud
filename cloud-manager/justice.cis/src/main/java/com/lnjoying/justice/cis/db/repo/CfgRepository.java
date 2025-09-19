package com.lnjoying.justice.cis.db.repo;

import com.lnjoying.justice.cis.controller.dto.response.DockerContainerInfo;
import com.lnjoying.justice.cis.db.mapper.TblCfgdataContainerInfoMapper;
import com.lnjoying.justice.cis.db.model.TblCfgdataContainerInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/25 20:58
 */

@Repository("cfgRepository")
public class CfgRepository
{

    @Autowired
    private TblCfgdataContainerInfoMapper cfgdataContainerInfoMapper;

    public List<TblContainerInstInfo> getContainersByCfg(String userId, String group, String dataId, List<Integer> cloudRemoveStatusList)
    {
        return cfgdataContainerInfoMapper.selectContainersByUserIdAndDatagroupAndDataId(userId, group, dataId, cloudRemoveStatusList);
    }


    public int insertSelective(TblCfgdataContainerInfo record)
    {
        return cfgdataContainerInfoMapper.insertSelective(record);
    }


    public int updateByPrimaryKeySelective(TblCfgdataContainerInfo record)
    {
        return cfgdataContainerInfoMapper.updateByPrimaryKeySelective(record);
    }


    public List<TblCfgdataContainerInfo> selectAll(String userId, String dataGroup, String dataId, Collection<String> containerIds)
    {
        return cfgdataContainerInfoMapper.selectAll(userId, dataGroup, dataId, containerIds);
    }

    public TblCfgdataContainerInfo selectByPrimaryKey(String cfgVolumeId)
    {
      return cfgdataContainerInfoMapper.selectByPrimaryKey(cfgVolumeId);
    }

    public List<TblCfgdataContainerInfo> selectByContainerId(String containerId)
    {
        return cfgdataContainerInfoMapper.selectByContainerId(containerId);
    }


    public void deleteByContainerId(String containerId)
    {
        if (StringUtils.isNotBlank(containerId))
        {
            cfgdataContainerInfoMapper.deleteByContainerId(containerId);
        }
    }
}
