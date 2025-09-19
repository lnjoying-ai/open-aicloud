package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.BaremetalComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstance;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceInfo;
import com.lnjoying.justice.cmp.service.nextstack.repo.ImageServiceBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class RpcBmServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private BaremetalComputeRepository baremetalComputeRepository;

    public RpcInstance getBaremetalInstanceFromPortId(String cloudId, String portId) {
        TblCmpBaremetalInstanceExample example = new TblCmpBaremetalInstanceExample();
        TblCmpBaremetalInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andPortIdFromAgentEqualTo(portId);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpBaremetalInstance> tblBaremetalInstanceList = baremetalComputeRepository.getBaremetalInstances(example);
        if(null == tblBaremetalInstanceList || 0 == tblBaremetalInstanceList.size()){
            LOGGER.info("get instance: null, portId: {}",portId);
            return null;
        }
        TblCmpBaremetalInstance tblBaremetalInstance = tblBaremetalInstanceList.get(0);
        RpcInstance instance = new RpcInstance();
        instance.setInstanceId(tblBaremetalInstance.getInstanceId());
        instance.setInstanceName(tblBaremetalInstance.getName());
        instance.setPortId(portId);
        return instance;
    }

    public Map<String,List<RpcInstanceInfo>> getInstanceInfos(String cloudId, List<String> subnetIdList)
    {
        HashSet<String> set = new HashSet<>(subnetIdList);
        if(set.size() != subnetIdList.size()){
            LOGGER.info("there are duplicate elements in subnetIdList");
            return null;
        }
        return subnetIdList.stream().collect(Collectors.toMap(
                Function.identity(), subnetId -> getInstanceInfosFromSubnetId(cloudId, subnetId)
        ));
    }

    public List<RpcInstanceInfo> getInstanceInfosFromSubnetId(String cloudId, String subnetId)
    {
        TblCmpBaremetalInstanceExample example = new TblCmpBaremetalInstanceExample();
        TblCmpBaremetalInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(subnetId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdFromAgentIsNotNull();
        List<TblCmpBaremetalInstance> tblBaremetalInstances = baremetalComputeRepository.getBaremetalInstances(example);

        if(0 == tblBaremetalInstances.size()){
            LOGGER.info("get instance: null, subnetId: {}",subnetId);
            return new ArrayList<>();
        }
        return tblBaremetalInstances.stream().map(
                tblBaremetalInstance -> {
                    RpcInstanceInfo instanceInfo = new RpcInstanceInfo();
                    instanceInfo.setInstanceId(tblBaremetalInstance.getInstanceId());
                    instanceInfo.setName(tblBaremetalInstance.getName());
                    instanceInfo.setPortId(tblBaremetalInstance.getPortIdFromAgent());
                    instanceInfo.setVm(false);
                    return instanceInfo;
                }
        ).collect(Collectors.toList());
    }
}
