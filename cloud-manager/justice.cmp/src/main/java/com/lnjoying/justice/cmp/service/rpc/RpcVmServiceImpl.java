package com.lnjoying.justice.cmp.service.rpc;

import com.lnjoying.justice.cmp.db.model.TblCmpInstanceNetworkRef;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceNetworkRefExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstance;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceExample;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstance;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcFlavorInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.RpcGpuFlavorInfo;
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
import static com.lnjoying.justice.cmp.common.VmInstanceStatus.VM;

@Service
public class RpcVmServiceImpl
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceBiz.class);

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private RpcFlavorServiceImpl rpcFlavorService;

    public List<RpcGpuFlavorInfo> getGpuFlavorInfos(String cloudId)
    {
        List<RpcGpuFlavorInfo> gpuFlavorInfos = vmComputeRepository.selectGpuFlavorInfo(cloudId);
        if (0 == gpuFlavorInfos.size())
        {
            RpcGpuFlavorInfo gpuFlavorInfo = new RpcGpuFlavorInfo();
            gpuFlavorInfo.setGpuCount(0);
            gpuFlavorInfo.setGpuName("");
            gpuFlavorInfos.add(gpuFlavorInfo);
        }
        return gpuFlavorInfos;
    }

    public RpcInstance getVmInstanceFromPortId(String cloudId, String portId)
    {
        TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
        TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andPortIdEqualTo(portId);
        List<TblCmpVmInstance> tblVmInstanceList = vmComputeRepository.getVmInstances(example);
        TblCmpVmInstance tblVmInstance ;
        if(null == tblVmInstanceList || 0 == tblVmInstanceList.size())
        {
            TblCmpInstanceNetworkRefExample instanceNetworkRefExample = new TblCmpInstanceNetworkRefExample();
            TblCmpInstanceNetworkRefExample.Criteria  instanceNetworkRefExampleCriteria = instanceNetworkRefExample.createCriteria();
            instanceNetworkRefExampleCriteria.andPortIdEqualTo(portId);
            instanceNetworkRefExampleCriteria.andPhaseStatusNotEqualTo(REMOVED);
            instanceNetworkRefExampleCriteria.andCloudIdEqualTo(cloudId);
            instanceNetworkRefExampleCriteria.andEeStatusNotEqualTo(REMOVED);
            if (vmComputeRepository.countNetworkInfoAndInstanceIdByExample(instanceNetworkRefExample) == 0)
            {
                LOGGER.info("get instance: null, portId: {}", portId);
                return null;
            }
            String instanceId = vmComputeRepository.getNetworkInfoAndInstanceIds(instanceNetworkRefExample).get(0).getInstanceId();
            tblVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
        }
        else
        {
            tblVmInstance = tblVmInstanceList.get(0);
        }
        RpcInstance instance = new RpcInstance();
        instance.setInstanceId(tblVmInstance.getVmInstanceId());
        instance.setInstanceName(tblVmInstance.getName());
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
        TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
        TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpVmInstance> tblVmInstances = vmComputeRepository.getVmInstances(example);

        TblCmpInstanceNetworkRefExample instanceNetworkRefExample = new TblCmpInstanceNetworkRefExample();
        TblCmpInstanceNetworkRefExample.Criteria instanceNetworkRefCriteria = instanceNetworkRefExample.createCriteria();
        instanceNetworkRefCriteria.andSubnetIdEqualTo(subnetId);
        instanceNetworkRefCriteria.andPhaseStatusNotEqualTo(REMOVED);
        instanceNetworkRefCriteria.andCloudIdEqualTo(cloudId);
        instanceNetworkRefCriteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpInstanceNetworkRef> tblInstanceNetworkRefs = vmComputeRepository.getNetworkInfoAndInstanceIds(instanceNetworkRefExample);
        List<RpcInstanceInfo> vmInstances = new ArrayList<>();
        if (tblInstanceNetworkRefs.size() > 0 )
        {
            vmInstances.addAll(tblInstanceNetworkRefs.stream().map(
                    tblInstanceNetworkRef ->
                    {
                        RpcInstanceInfo instanceInfo = new RpcInstanceInfo();
                        instanceInfo.setInstanceId(tblInstanceNetworkRef.getInstanceId());
                        instanceInfo.setPortId(tblInstanceNetworkRef.getPortId());
                        instanceInfo.setVm(tblInstanceNetworkRef.getInstanceType() == VM);
                        instanceInfo.setName(vmComputeRepository.getVmInstanceById(cloudId, tblInstanceNetworkRef.getInstanceId()).getName());
                        return instanceInfo;
                    }).collect(Collectors.toList()));

        }
        if (tblVmInstances.size() > 0)
        {
            vmInstances.addAll(tblVmInstances.stream().map(
                    tblVmInstance ->
                    {
                        RpcInstanceInfo instanceInfo = new RpcInstanceInfo();
                        instanceInfo.setInstanceId(tblVmInstance.getVmInstanceId());
                        instanceInfo.setName(tblVmInstance.getName());
                        instanceInfo.setPortId(tblVmInstance.getPortId());
                        instanceInfo.setVm(true);
                        return instanceInfo;
                    }
            ).collect(Collectors.toList()));
        }
        if(0 == vmInstances.size() ){
            LOGGER.info("get instance: null, subnetId: {}",subnetId);
            return new ArrayList<>();
        }
        return vmInstances;
    }

    public List<RpcInstanceDetailInfo> getVmInstanceDetailInfos(String cloudId, List<String> instanceIdList)
    {
        return instanceIdList.stream().map(instanceId -> getVmInstanceDetailInfo(cloudId, instanceId)).collect(Collectors.toList());
    }

    public RpcInstanceDetailInfo getVmInstanceDetailInfo(String cloudId, String instanceId)
    {
        TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
        TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andVmInstanceIdEqualTo(instanceId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpVmInstance> tblCmpVmInstances = vmComputeRepository.getVmInstances(example);

        if (null == tblCmpVmInstances || 0 == tblCmpVmInstances.size())
        {
            LOGGER.info("get vm instance: null, instanceId: {}",instanceId);
            return null;
        }
        TblCmpVmInstance tblCmpVmInstance = tblCmpVmInstances.get(0);
        RpcInstanceDetailInfo instanceDetailInfo = new RpcInstanceDetailInfo();
        instanceDetailInfo.setInstanceId(instanceId);
        instanceDetailInfo.setName(tblCmpVmInstance.getName());
        instanceDetailInfo.setPortId(tblCmpVmInstance.getPortId());
        instanceDetailInfo.setPhaseStatus(tblCmpVmInstance.getPhaseStatus());
        instanceDetailInfo.setFlavorId(tblCmpVmInstance.getFlavorId());
        RpcFlavorInfo flavorInfo = rpcFlavorService.getFlavorInfo(cloudId, tblCmpVmInstance.getFlavorId());
        instanceDetailInfo.setFlavorName(flavorInfo.getName());
        return instanceDetailInfo;
    }
}
