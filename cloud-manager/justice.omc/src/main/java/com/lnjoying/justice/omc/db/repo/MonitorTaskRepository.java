package com.lnjoying.justice.omc.db.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.db.mapper.TblOmcMonitorInstanceMapper;
import com.lnjoying.justice.omc.db.mapper.TblOmcMonitorTaskMapper;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorTask;
import com.lnjoying.justice.omc.domain.model.ConfigVariable;
import com.lnjoying.justice.omc.domain.model.ExporterStackDeploymentStatusEnum;
import com.lnjoying.justice.omc.domain.model.MonitorInstanceEndpointInfo;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/28 15:12
 */

@Repository
public class MonitorTaskRepository
{

    @Autowired
    private TblOmcMonitorTaskMapper monitorTaskMapper;

    @Autowired
    private TblOmcMonitorInstanceMapper monitorInstanceMapper;


    public int insert(TblOmcMonitorTask record)
    {
        return monitorTaskMapper.insert(record);
    }

    public List<TblOmcMonitorTask> selectAllByDeploymentStatus(Integer deploymentStatus)
    {
        return monitorTaskMapper.selectAllByDeploymentStatus(deploymentStatus);
    }

    public List<String> selectIdByDeploymentStatus(Integer deploymentStatus)
    {
        return monitorTaskMapper.selectIdByDeploymentStatus(deploymentStatus);
    }

    public TblOmcMonitorTask selectByPrimaryKey(String taskId)
    {
        return monitorTaskMapper.selectByPrimaryKey(taskId);
    }


    public int updateByPrimaryKeySelective(TblOmcMonitorTask record)
    {
        return monitorTaskMapper.updateByPrimaryKeySelective(record);
    }

    public int batchInsertMonitorInstances(List<TblOmcMonitorInstance> list)
    {
        return monitorInstanceMapper.batchInsert(list);
    }


    public List<TblOmcMonitorInstance> selectAll(String queryBpId, String queryUserId, Integer status, Integer taskType)
    {
        return monitorInstanceMapper.selectAll(queryBpId, queryUserId, null, status, taskType);
    }

    public List<String> selectIdByStatusAndTaskType(Integer status, Integer taskType)
    {
        return monitorInstanceMapper.selectIdByStatusAndTaskType(status, taskType);
    }


    public int updateMonitorInstance(TblOmcMonitorInstance record)
    {
        return monitorInstanceMapper.updateByPrimaryKeySelective(record);
    }

    public TblOmcMonitorInstance selectMonitorInstance(String monitorInstanceId)
    {
        return monitorInstanceMapper.selectByPrimaryKey(monitorInstanceId);
    }

    public TblOmcMonitorInstance selectMonitorInstanceForUpdate(String monitorInstanceId)
    {
        return monitorInstanceMapper.selectForUpdate(monitorInstanceId);
    }

    public int insertMonitorInstanceSelective(TblOmcMonitorInstance record)
    {
        return monitorInstanceMapper.insertSelective(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public CombRetPacket processMonitorPortMappingResult(String uniqueId, String endpoint)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        combRetPacket.setStatus(ErrorCode.SUCCESS.getCode());

        String monitorId = "";
        String port = "";
        if (uniqueId.contains("-"))
        {
            String[] split = uniqueId.split("-");
            if (split != null && split.length > 0)
            {
                monitorId = split[0];
                if (split.length > 1)
                {
                    port = split[1];
                }
            }
        }

        TblOmcMonitorInstance tblOmcMonitorInstance = selectMonitorInstanceForUpdate(monitorId);
        if (Objects.nonNull(tblOmcMonitorInstance))
        {
            if (ExporterStackDeploymentStatusEnum.RUNNING.value() != tblOmcMonitorInstance.getStatus())
            {
                return combRetPacket;
            }
            TblOmcMonitorInstance updateTblOmcMonitorInstance = new TblOmcMonitorInstance();
            String endpointInDb = tblOmcMonitorInstance.getEndpoint();

            List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = fillEndpointInfo(endpoint, port, endpointInDb);
            if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
            {
                // verify that all ports are mapped
                boolean notMappedAll = monitorInstanceEndpointInfos.stream().anyMatch(monitorInstanceEndpointInfo ->
                {
                    if (StringUtils.isEmpty(monitorInstanceEndpointInfo.getMappedEndpoint()))
                    {
                        return true;
                    }
                    return false;
                });

                if (!notMappedAll)
                {
                    updateTblOmcMonitorInstance.setEndpoint(JacksonUtils.objToStrDefault(monitorInstanceEndpointInfos));
                    updateTblOmcMonitorInstance.setId(monitorId);
                    updateTblOmcMonitorInstance.setStatus(ExporterStackDeploymentStatusEnum.PORT_MAPPING_SUCCEEDED.value());
                    updateTblOmcMonitorInstance.setUpdateTime(new Date());
                    updateMonitorInstance(updateTblOmcMonitorInstance);
                }
                else
                {
                    updateTblOmcMonitorInstance.setEndpoint(JacksonUtils.objToStrDefault(monitorInstanceEndpointInfos));
                    updateTblOmcMonitorInstance.setId(monitorId);
                    updateTblOmcMonitorInstance.setUpdateTime(new Date());
                    updateMonitorInstance(updateTblOmcMonitorInstance);
                }
            }


            return combRetPacket;
        }
        return null;
    }

    private static List<MonitorInstanceEndpointInfo> fillEndpointInfo(String endpoint, String port, String endpointInDb)
    {
        List<MonitorInstanceEndpointInfo> monitorInstanceEndpointInfos = JacksonUtils.strToObjTypeDefault(endpointInDb, new TypeReference<List<MonitorInstanceEndpointInfo>>()
        {
        });
        if (!CollectionUtils.isEmpty(monitorInstanceEndpointInfos))
        {
            if (StringUtils.hasText(port))
            {
                for (MonitorInstanceEndpointInfo monitorInstanceEndpointInfo : monitorInstanceEndpointInfos)
                {
                    if (monitorInstanceEndpointInfo.getOriginalPort().equals(Integer.parseInt(port)))
                    {
                        monitorInstanceEndpointInfo.setMappedEndpoint(endpoint);
                    }
                }
            }

        }
        return monitorInstanceEndpointInfos;
    }
}

