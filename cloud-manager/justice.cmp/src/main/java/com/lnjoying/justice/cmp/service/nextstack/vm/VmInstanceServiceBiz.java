package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.common.constant.ImageOsType;
import com.lnjoying.justice.cmp.common.nextstack.NSInstanceStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcFlavorInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcVolumeInfo;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.domain.model.UserResourceMetrics;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.VmInstanceSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.NetworkDetailInfoReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcImage;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcNetworkDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcSgInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.PciDeviceInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmInstanceDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmInstanceInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.VmInstancesRsp;
import com.lnjoying.justice.cmp.domain.info.baremetal.ImageAbbrInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.PortAbbrInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.SubnetAbbrInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.VpcAbbrInfo;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.nextstack.network.NetworkService;
import com.lnjoying.justice.cmp.service.nextstack.network.SgService;
import com.lnjoying.justice.cmp.service.rpc.*;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.*;

@Service
public class VmInstanceServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VmSnapServiceBiz.class);

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private RpcNetworkServiceImpl rpcNetworkService;

    @Autowired
    private RpcImageServiceImpl rpcImageService;

    @Autowired
    private RpcFlavorServiceImpl rpcFlavorService;

    @Autowired
    private RpcVolumeServiceImpl rpcVolumeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private SgService sgService;

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity addVmInstance(String cloudId, VmInstanceCreateReq addVmInstanceReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            checkAddVmInstanceReq(addVmInstanceReq);
            TblCmpEip tblCmpEip = networkService.preAllocateEip(cloudId, addVmInstanceReq, bpId, userId);

            addVmInstanceReq.setCmpTenantId(bpId);
            addVmInstanceReq.setCmpUserId(userId);

            TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
            tblCmpVmInstance.setVmInstanceId(Utils.buildStr("EE_", Utils.assignUUId()));
            tblCmpVmInstance.setEeId(tblCmpVmInstance.getVmInstanceId());
            tblCmpVmInstance.setCloudId(cloudId);
            tblCmpVmInstance.setEeBp(bpId);
            tblCmpVmInstance.setEeUser(userId);
            tblCmpVmInstance.setEeStatus(WAIT_CREATE);
            tblCmpVmInstance.setName(addVmInstanceReq.getName());
            tblCmpVmInstance.setDescription(addVmInstanceReq.getDescription());
            tblCmpVmInstance.setImageId(addVmInstanceReq.getImageId());
            tblCmpVmInstance.setFlavorId(addVmInstanceReq.getFlavorId());
            tblCmpVmInstance.setPubkeyId(addVmInstanceReq.getPubkeyId());
            tblCmpVmInstance.setStoragePoolId(addVmInstanceReq.getStoragePoolId());
            tblCmpVmInstance.setNodeId(addVmInstanceReq.getNodeId());
            tblCmpVmInstance.setPhaseStatus(NSInstanceStatus.INSTANCE_INIT.getCode());
            tblCmpVmInstance.setRootDisk(addVmInstanceReq.getRootDisk());

            vmComputeRepository.insertVmInstance(tblCmpVmInstance);

            networkService.updateEipBoundEeId(tblCmpEip, tblCmpVmInstance.getEeId());

            cloudService.createResource(cloudId, tblCmpVmInstance.getEeId(), bpId, userId, CreateResourceMsg.NS_VM_INSTANCE, addVmInstanceReq);

            return ResponseEntity.status(HttpStatus.CREATED).body(tblCmpVmInstance.getEeId());
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add vm instance failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void addVmInstance(CreateResourceInfo createResourceInfo)
    {
        try
        {
            VmInstanceCreateReq addVmInstanceReq = (VmInstanceCreateReq) createResourceInfo.getObject();
            String cloudId = createResourceInfo.getCloudId();
            String bpId = createResourceInfo.getBpId();
            String userId = createResourceInfo.getUserId();
            String eeId = createResourceInfo.getEeId();

            checkAddVmInstanceNetworkParams(cloudId, addVmInstanceReq, bpId, userId);

            JsonObject jsonObject = JsonParser.parseString(JsonUtils.toJson(addVmInstanceReq)).getAsJsonObject();
            jsonObject.remove("chargeType");
            jsonObject.remove("priceUnit");
            jsonObject.remove("period");
            jsonObject.remove("applyEip");

            String url = "/api/vm/v1/instances";
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, url, HttpMethod.POST, jsonObject, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add vm instance error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String instanceId = (String) resultMap.get("instanceId");
                    if (StringUtils.isEmpty(instanceId))
                    {
                        LOGGER.error("add vm instance error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                        tblCmpVmInstance.setVmInstanceId(instanceId);
                        tblCmpVmInstance.setCloudId(cloudId);
                        tblCmpVmInstance.setEeStatus(SYNCING);
                        tblCmpVmInstance.setEeId(eeId);

                        try
                        {
                            vmComputeRepository.updateVmInstanceByEeIdSelective(tblCmpVmInstance);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVmInstance oldTblCmpVmInstance = vmComputeRepository.getVmInstanceByEeId(cloudId, eeId);
                            TblCmpVmInstance updateTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);

                            updateTblCmpVmInstance.setEeBp(bpId);
                            updateTblCmpVmInstance.setEeUser(userId);
                            updateTblCmpVmInstance.setEeStatus(SYNCING);
                            updateTblCmpVmInstance.setEeId(eeId);
                            vmComputeRepository.updateVmInstanceByPrimaryKeySelective(updateTblCmpVmInstance);

                            oldTblCmpVmInstance.setEeStatus(REMOVED);
                            vmComputeRepository.updateVmInstanceByPrimaryKeySelective(oldTblCmpVmInstance);
                        }

                        cloudService.syncSingleData(cloudId, instanceId, SyncMsg.NS_VM_INSTANCE, DataSyncLevel.LEVEL_6);
                    }
                }
            }
            else
            {
                TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceByEeId(cloudId, eeId);
                tblCmpVmInstance.setPhaseStatus(NSInstanceStatus.INSTANCE_CREATE_FAILED.getCode());
                vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add vm instance failed, message:{}", e.getMessage());
        }
    }

    public ResponseEntity addVmInstances(String cloudId, VmInstancesCreateReq addVmInstancesReq, String bpId, String userId) throws WebSystemException
    {
        try
        {
            VmInstanceCreateReq addVmInstanceReq = addVmInstancesReq.getVmInstanceCreateReq();
            checkAddVmInstanceReq(addVmInstanceReq);
            List<TblCmpEip> tblCmpEips = networkService.preAllocateEips(cloudId, addVmInstancesReq, bpId, userId);

            addVmInstanceReq.setCmpTenantId(bpId);
            addVmInstanceReq.setCmpUserId(userId);

            if (addVmInstancesReq.getCount() == 1)
            {
                TblCmpEip tblCmpEip = CollectionUtils.isEmpty(tblCmpEips) ? null : tblCmpEips.get(0);

                TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                tblCmpVmInstance.setVmInstanceId(Utils.buildStr("EE_", Utils.assignUUId()));
                tblCmpVmInstance.setEeId(tblCmpVmInstance.getVmInstanceId());
                tblCmpVmInstance.setCloudId(cloudId);
                tblCmpVmInstance.setEeBp(bpId);
                tblCmpVmInstance.setEeUser(userId);
                tblCmpVmInstance.setEeStatus(WAIT_CREATE);
                tblCmpVmInstance.setName(addVmInstanceReq.getName());
                tblCmpVmInstance.setDescription(addVmInstanceReq.getDescription());
                tblCmpVmInstance.setImageId(addVmInstanceReq.getImageId());
                tblCmpVmInstance.setFlavorId(addVmInstanceReq.getFlavorId());
                tblCmpVmInstance.setPubkeyId(addVmInstanceReq.getPubkeyId());
                tblCmpVmInstance.setStoragePoolId(addVmInstanceReq.getStoragePoolId());
                tblCmpVmInstance.setNodeId(addVmInstanceReq.getNodeId());
                tblCmpVmInstance.setPhaseStatus(NSInstanceStatus.INSTANCE_INIT.getCode());
                tblCmpVmInstance.setRootDisk(addVmInstanceReq.getRootDisk());

                vmComputeRepository.insertVmInstance(tblCmpVmInstance);

                if (tblCmpEip != null)
                {
                    networkService.updateEipBoundEeId(tblCmpEip, tblCmpVmInstance.getEeId());
                    addVmInstanceReq.setEipId(tblCmpEip.getEipId());
                }

                cloudService.createResource(cloudId, tblCmpVmInstance.getEeId(), bpId, userId, CreateResourceMsg.NS_VM_INSTANCE, addVmInstanceReq);

                return ResponseEntity.status(HttpStatus.CREATED).body(tblCmpVmInstance.getEeId());
            }
            else
            {
                String addVmInstanceReqStr = JsonUtils.toJson(addVmInstanceReq);
                String name = addVmInstanceReq.getName();
                String hostname = StringUtils.isEmpty(addVmInstanceReq.getHostname()) ? null : addVmInstanceReq.getHostname();
                for (int i = 0; i < addVmInstancesReq.getCount(); i++)
                {
                    VmInstanceCreateReq tempAddVmInstanceReq = JsonUtils.fromJson(addVmInstanceReqStr, VmInstanceCreateReq.class);
                    int index = i + 1;
                    tempAddVmInstanceReq.setName(name + "-" + index);
                    if (! StringUtils.isEmpty(hostname))
                    {
                        tempAddVmInstanceReq.setHostname(hostname + "-" + index);
                    }

                    TblCmpEip tblCmpEip = CollectionUtils.isEmpty(tblCmpEips) ? null : tblCmpEips.get(i);

                    TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                    tblCmpVmInstance.setVmInstanceId(Utils.buildStr("EE_", Utils.assignUUId()));
                    tblCmpVmInstance.setEeId(tblCmpVmInstance.getVmInstanceId());
                    tblCmpVmInstance.setCloudId(cloudId);
                    tblCmpVmInstance.setEeBp(bpId);
                    tblCmpVmInstance.setEeUser(userId);
                    tblCmpVmInstance.setEeStatus(WAIT_CREATE);
                    tblCmpVmInstance.setName(tempAddVmInstanceReq.getName());
                    tblCmpVmInstance.setDescription(tempAddVmInstanceReq.getDescription());
                    tblCmpVmInstance.setImageId(tempAddVmInstanceReq.getImageId());
                    tblCmpVmInstance.setFlavorId(tempAddVmInstanceReq.getFlavorId());
                    tblCmpVmInstance.setPubkeyId(tempAddVmInstanceReq.getPubkeyId());
                    tblCmpVmInstance.setStoragePoolId(tempAddVmInstanceReq.getStoragePoolId());
                    tblCmpVmInstance.setNodeId(tempAddVmInstanceReq.getNodeId());
                    tblCmpVmInstance.setPhaseStatus(NSInstanceStatus.INSTANCE_INIT.getCode());
                    tblCmpVmInstance.setRootDisk(tempAddVmInstanceReq.getRootDisk());

                    vmComputeRepository.insertVmInstance(tblCmpVmInstance);

                    if (tblCmpEip != null)
                    {
                        networkService.updateEipBoundEeId(tblCmpEip, tblCmpVmInstance.getEeId());
                        tempAddVmInstanceReq.setEipId(tblCmpEip.getEipId());
                    }

                    cloudService.createResource(cloudId, tblCmpVmInstance.getEeId(), bpId, userId, CreateResourceMsg.NS_VM_INSTANCE, tempAddVmInstanceReq);
                }

                return ResponseEntity.status(HttpStatus.CREATED).body(null);
            }
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add vm instances failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkAddVmInstanceReq(VmInstanceCreateReq addVmInstanceReq)
    {
        if (StringUtils.isBlank(addVmInstanceReq.getFlavorId()))
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
        }
        if (null == addVmInstanceReq.getDiskInfos() || addVmInstanceReq.getDiskInfos().size() == 0) return;
        addVmInstanceReq.getDiskInfos().forEach(diskInfo -> {
            if (StringUtils.isBlank(diskInfo.getVolumeId()) && diskInfo.getSize() < 0)
            {
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
            }
        });
    }

    public ResponseEntity addVmInstance(String cloudId, VmInstanceRenewReq vmInstanceRenewReq, String bpId, String userId) throws WebSystemException
    {
        try
        {

            JsonObject jsonObject = JsonParser.parseString(JsonUtils.toJson(vmInstanceRenewReq)).getAsJsonObject();
            jsonObject.remove("chargeType");
            jsonObject.remove("priceUnit");
            jsonObject.remove("period");

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, jsonObject);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add vm instance error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String instanceId = (String) resultMap.get("instanceId");
                    if (StringUtils.isEmpty(instanceId))
                    {
                        LOGGER.error("add vm instance error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                        tblCmpVmInstance.setVmInstanceId(instanceId);
                        tblCmpVmInstance.setCloudId(cloudId);
                        tblCmpVmInstance.setEeBp(bpId);
                        tblCmpVmInstance.setEeUser(userId);
                        tblCmpVmInstance.setEeStatus(SYNCING);
                        try
                        {
                            vmComputeRepository.insertVmInstance(tblCmpVmInstance);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVmInstance updateTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
                            updateTblCmpVmInstance.setEeBp(bpId);
                            updateTblCmpVmInstance.setEeUser(userId);
                            updateTblCmpVmInstance.setEeStatus(SYNCING);
                            vmComputeRepository.updateVmInstanceByPrimaryKeySelective(updateTblCmpVmInstance);
                        }

                        cloudService.syncSingleData(cloudId, instanceId, SyncMsg.NS_VM_INSTANCE);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add vm instance failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public VmInstancesRsp getVmInfos(String cloudId, VmInstanceSearchCritical vmInstanceSearchCritical, String userId) throws WebSystemException
    {
        try
        {
            List<String> eipMapPorts ;
            List<TblCmpVmInstance> vmInstancesFromNetworkRef = new ArrayList<>();

            TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
            TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andPhaseStatusNotEqualTo(REMOVED);

            if (!StringUtils.isBlank(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (!StringUtils.isBlank(vmInstanceSearchCritical.getVmInstanceId()))
            {
                criteria.andVmInstanceIdEqualTo(vmInstanceSearchCritical.getVmInstanceId());
            }
            else if (!StringUtils.isBlank(vmInstanceSearchCritical.getName()))
            {
                criteria.andNameLike("%" + vmInstanceSearchCritical.getName() + "%");
            }
            if (null != vmInstanceSearchCritical.getPortIdIsNull() && vmInstanceSearchCritical.getPortIdIsNull())
            {
                criteria.andPortIdIsNull();
            }
            else if ( null != vmInstanceSearchCritical.getPortIdIsNull() && !vmInstanceSearchCritical.getPortIdIsNull())
            {
                criteria.andPortIdIsNotNull();
            }
            if (null != vmInstanceSearchCritical.getSubnetId() && !vmInstanceSearchCritical.getSubnetId().isEmpty())
            {
                criteria.andSubnetIdEqualTo(vmInstanceSearchCritical.getSubnetId());
                vmInstancesFromNetworkRef = getVmInstancesBySubnetId(cloudId, vmInstanceSearchCritical.getSubnetId());
            }
            if (!StringUtils.isBlank(vmInstanceSearchCritical.getInstanceGroupId()))
            {
                criteria.andInstanceGroupIdEqualTo(vmInstanceSearchCritical.getInstanceGroupId());
            }
            else if (null != vmInstanceSearchCritical.getInstanceGroupIdIsNull() && vmInstanceSearchCritical.getInstanceGroupIdIsNull())
            {
                criteria.andInstanceGroupIdIsNullOrIsEmpty();
            }
            else if (null != vmInstanceSearchCritical.getInstanceGroupIdIsNull() && !vmInstanceSearchCritical.getInstanceGroupIdIsNull())
            {
                criteria.andInstanceGroupIdIsNotNull();
            }
            if (null != vmInstanceSearchCritical.getEipIdIsNull() && vmInstanceSearchCritical.getEipIdIsNull())
            {
                criteria.andEipIdIsNull();
            }
            else if (null != vmInstanceSearchCritical.getEipIdIsNull() && !vmInstanceSearchCritical.getEipIdIsNull())
            {
                criteria.andEipIdIsNotNull();
            }

            if (StringUtils.isNotBlank(vmInstanceSearchCritical.getEipId()))
            {
                criteria.andEipIdEqualTo(vmInstanceSearchCritical.getEipId());
            }
            if (null != vmInstanceSearchCritical.getEipMapIsUsing() && !vmInstanceSearchCritical.getEipMapIsUsing())
            {
                eipMapPorts = rpcNetworkService.getPorts(cloudId, vmInstanceSearchCritical.getSubnetId());
                eipMapPorts.forEach(criteria::andPortIdNotEqualTo);

                if (vmInstancesFromNetworkRef.size() > 0 )
                {
                    Set<String> eipMapPortSet = new HashSet<>(eipMapPorts);
                    vmInstancesFromNetworkRef = vmInstancesFromNetworkRef.stream().filter(
                            tblVmInstance -> !eipMapPortSet.contains(tblVmInstance.getPortId())
                    ).collect(Collectors.toList());
                }
            }
            if(!StringUtils.isBlank(vmInstanceSearchCritical.getNodeId()))
            {
                criteria.andNodeIdEqualTo(vmInstanceSearchCritical.getNodeId());
            }

            long totalNum = vmComputeRepository.countVmInstancesByExample(example);
            VmInstancesRsp getVmInstancesRsp = new VmInstancesRsp();
            getVmInstancesRsp.setTotalNum(totalNum);
            if (totalNum < 1 && 0 == vmInstancesFromNetworkRef.size())
            {
                return getVmInstancesRsp;
            }

            int begin = ((vmInstanceSearchCritical.getPageNum() - 1) * vmInstanceSearchCritical.getPageSize());
            example.setOrderByClause("create_time desc, name desc");
            example.setStartRow(begin);
            example.setPageSize(vmInstanceSearchCritical.getPageSize());

            List<TblCmpVmInstance> tblCmpVmInstances = vmComputeRepository.getVmInstances(example);

            if (tblCmpVmInstances.isEmpty() && vmInstancesFromNetworkRef.isEmpty())
            {
                return getVmInstancesRsp;
            }
            else if (tblCmpVmInstances.isEmpty())
            {
                tblCmpVmInstances = vmInstancesFromNetworkRef;
            }
            else if (vmInstancesFromNetworkRef.size() > 0)
            {
                getVmInstancesRsp.setTotalNum(totalNum + vmInstancesFromNetworkRef.size());
                tblCmpVmInstances.addAll(vmInstancesFromNetworkRef);
            }

            List<String> imageIdList = new ArrayList<>();
            List<NetworkDetailInfoReq> networkInfoList = new ArrayList<>();

            for (TblCmpVmInstance tblCmpVmInstance : tblCmpVmInstances)
            {
                imageIdList.add(tblCmpVmInstance.getImageId());

                NetworkDetailInfoReq req = new NetworkDetailInfoReq();
                req.setVpcId(tblCmpVmInstance.getVpcId());
                req.setSubnetId(tblCmpVmInstance.getSubnetId());
                req.setPortId(tblCmpVmInstance.getPortId());
                networkInfoList.add(req);
            }
            List<RpcImage> imageList = rpcImageService.getBatchImages(cloudId, imageIdList);
            if (null == imageList || imageList.size() != imageIdList.size())
            {
                LOGGER.error("get vm instances failed, get image service error");
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }

            List<RpcNetworkDetailInfo> networkInfoDetailList = rpcNetworkService.getBatchNetworkInfos(cloudId, networkInfoList);
            if (null == networkInfoDetailList || networkInfoDetailList.size() != networkInfoList.size())
            {
                LOGGER.error("get vm instances failed, get network service error");
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }

            List<VmInstanceInfo> vmInstanceInfoList = new ArrayList<>();

            for (int i = 0; i < tblCmpVmInstances.size(); i++)
            {
                TblCmpVmInstance tblCmpVmInstance = tblCmpVmInstances.get(i);

                VmInstanceInfo vmInstanceInfo = new VmInstanceInfo();
                vmInstanceInfo.setInstanceInfo(tblCmpVmInstance);

                if (imageList.size() == tblCmpVmInstances.size())
                {
                    RpcImage image = imageList.get(i);

                    if (null != image)
                    {
                        ImageAbbrInfo imageAbbrInfo = new ImageAbbrInfo();
                        imageAbbrInfo.setImageId(tblCmpVmInstance.getImageId());
                        imageAbbrInfo.setName(image.getName());
                        vmInstanceInfo.setImageInfo(imageAbbrInfo);
                    }
                }

                if (networkInfoDetailList.size() == tblCmpVmInstances.size())
                {
                    RpcNetworkDetailInfo networkDetailInfo = networkInfoDetailList.get(i);

                    vmInstanceInfo.setEipId(networkDetailInfo.getEipId());
                    vmInstanceInfo.setEip(networkDetailInfo.getEip());
                    vmInstanceInfo.setBoundPhaseStatus(networkDetailInfo.getBoundPhaseStatus());
                    vmInstanceInfo.setBoundType(networkDetailInfo.getBoundType());
                    vmInstanceInfo.setPublicIp(networkDetailInfo.getPublicIp());
                    vmInstanceInfoList.add(vmInstanceInfo);

                    VpcAbbrInfo vpcAbbrInfo = new VpcAbbrInfo();
                    vpcAbbrInfo.setVpcId(networkDetailInfo.getVpcId());
                    vpcAbbrInfo.setName(networkDetailInfo.getVpcName());
                    vpcAbbrInfo.setCidr(networkDetailInfo.getVpcCidr());
                    vmInstanceInfo.setVpcInfo(vpcAbbrInfo);

                    SubnetAbbrInfo subnetAbbrInfo = new SubnetAbbrInfo();
                    subnetAbbrInfo.setSubnetId(networkDetailInfo.getSubnetId());
                    subnetAbbrInfo.setName(networkDetailInfo.getSubnetName());
                    subnetAbbrInfo.setCidr(networkDetailInfo.getSubnetCidr());
                    vmInstanceInfo.setSubnetInfo(subnetAbbrInfo);

                    PortAbbrInfo portAbbrInfo = new PortAbbrInfo();
                    portAbbrInfo.setPortId(networkDetailInfo.getPortId());
                    portAbbrInfo.setIpAddress(networkDetailInfo.getIpAddress());
                    vmInstanceInfo.setPortInfo(portAbbrInfo);
                }
            }

            getVmInstancesRsp.setVmInstancesInfo(vmInstanceInfoList);

            vmInstanceInfoList.forEach(vmInstanceInfo -> {
                vmInstanceInfo.setEeBpName(userService.getBpName(vmInstanceInfo.getEeBp()));
                vmInstanceInfo.setEeUserName(userService.getUserName(vmInstanceInfo.getEeUser()));
            });

            vmInstanceInfoList.forEach(vmInstance -> cloudService.syncSingleData(cloudId, vmInstance.getInstanceId(), SyncMsg.NS_VM_INSTANCE));

            return getVmInstancesRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get vm instances failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    private List<TblCmpInstanceNetworkRef> getInstanceNetworkRefs(String cloudId, String subnetId)
    {
        TblCmpInstanceNetworkRefExample instanceNetworkRefExample = new TblCmpInstanceNetworkRefExample();
        TblCmpInstanceNetworkRefExample.Criteria instanceNetworkRefExampleCriteria = instanceNetworkRefExample.createCriteria();
        instanceNetworkRefExampleCriteria.andPhaseStatusNotEqualTo(REMOVED);
        instanceNetworkRefExampleCriteria.andSubnetIdEqualTo(subnetId);
        instanceNetworkRefExampleCriteria.andEeStatusNotEqualTo(REMOVED);
        instanceNetworkRefExampleCriteria.andCloudIdEqualTo(cloudId);
        if (vmComputeRepository.countNetworkInfoAndInstanceIdByExample(instanceNetworkRefExample) > 0)
        {
            return vmComputeRepository.getNetworkInfoAndInstanceIds(instanceNetworkRefExample);
        }

        return new ArrayList<>();
    }

    public List<TblCmpVmInstance> getVmInstancesBySubnetId(String cloudId, String subnetId)
    {
        return getInstanceNetworkRefs(cloudId, subnetId).stream().map(
                tblInstanceNetworkRef ->
                {
                    TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, tblInstanceNetworkRef.getInstanceId());
                    tblCmpVmInstance.setPortId(tblInstanceNetworkRef.getPortId());
                    tblCmpVmInstance.setVpcId(tblInstanceNetworkRef.getVpcId());
                    return tblCmpVmInstance;
                }
        ).collect(Collectors.toList());
    }

    public VmInstanceDetailInfoRsp getVmInstance(String cloudId, String vmInstanceId, String userId) throws WebSystemException
    {
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
        if (null == tblCmpVmInstance || REMOVED == tblCmpVmInstance.getPhaseStatus() || REMOVED == tblCmpVmInstance.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVmInstance.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        RpcFlavorInfo flavor = rpcFlavorService.getFlavorInfo(cloudId, tblCmpVmInstance.getFlavorId());
        if (null == flavor)
        {
            throw new WebSystemException(ErrorCode.FLAVOR_NOT_EXIST, ErrorLevel.INFO);
        }

        VmInstanceDetailInfoRsp getVmInstanceDetailInfoRsp = new VmInstanceDetailInfoRsp();
        getVmInstanceDetailInfoRsp.setInstanceDetailInfo(tblCmpVmInstance, flavor);
        getVmInstanceDetailInfoRsp.setEeBpName(userService.getBpName(tblCmpVmInstance.getEeBp()));
        getVmInstanceDetailInfoRsp.setEeUserName(userService.getUserName(tblCmpVmInstance.getEeUser()));

        TblCmpInstanceNetworkRefExample example = new TblCmpInstanceNetworkRefExample();
        TblCmpInstanceNetworkRefExample.Criteria criteria = example.createCriteria();
        criteria.andInstanceIdEqualTo(tblCmpVmInstance.getVmInstanceId());
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        List<NetworkDetailInfoReq> networkDetailInfoReqs = new ArrayList<>();
        NetworkDetailInfoReq networkDetailInfoReq = new NetworkDetailInfoReq();
        networkDetailInfoReq.setVpcId(tblCmpVmInstance.getVpcId());
        networkDetailInfoReq.setSubnetId(tblCmpVmInstance.getSubnetId());
        networkDetailInfoReq.setPortId(tblCmpVmInstance.getPortId());
        networkDetailInfoReq.setInstanceId(tblCmpVmInstance.getVmInstanceId());
        networkDetailInfoReqs.add(networkDetailInfoReq);

        if (vmComputeRepository.countNetworkInfoAndInstanceIdByExample(example) > 0)
        {
            List<NetworkDetailInfoReq> reqs = vmComputeRepository.getNetworkInfoAndInstanceIds(example).stream().map(
                    tblInstanceNetworkRef -> {
                        NetworkDetailInfoReq req = new NetworkDetailInfoReq();
                        req.setInstanceId(tblInstanceNetworkRef.getInstanceId());
                        req.setVpcId(tblInstanceNetworkRef.getVpcId());
                        req.setSubnetId(tblInstanceNetworkRef.getSubnetId());
                        req.setPortId(tblInstanceNetworkRef.getPortId());
                        return req;
                    }
            ).collect(Collectors.toList());
            networkDetailInfoReqs.addAll(reqs);
        }
        List<RpcNetworkDetailInfo> networkDetailInfos = rpcNetworkService.getBatchNetworkInfos(cloudId, networkDetailInfoReqs);
        if (null != networkDetailInfos)
        {
            getVmInstanceDetailInfoRsp.setNetworkDetailInfos(networkDetailInfos);
        }
        List<RpcSgInfo> sgInfos = rpcNetworkService.getSgInfos(cloudId, vmInstanceId);
        if (null != sgInfos)
        {
            getVmInstanceDetailInfoRsp.setSgInfos(sgInfos);
        }
        if (StringUtils.isNotBlank(tblCmpVmInstance.getNodeId()))
        {
            getVmInstanceDetailInfoRsp.setHypervisorNodeName(vmComputeRepository.getHypervisorNodeById(cloudId, tblCmpVmInstance.getNodeId()).getName());
        }
        TblCmpVmSnapExample tblCmpVmSnapExample = new TblCmpVmSnapExample();
        TblCmpVmSnapExample.Criteria snapCriteria = tblCmpVmSnapExample.createCriteria();
        snapCriteria.andVmInstanceIdEqualTo(vmInstanceId);
        snapCriteria.andPhaseStatusNotEqualTo(REMOVED);
        snapCriteria.andCloudIdEqualTo(cloudId);
        snapCriteria.andEeStatusNotEqualTo(REMOVED);

        List<TblCmpVmSnap> tblCmpVmSnaps = vmComputeRepository.getVmSnaps(tblCmpVmSnapExample);
        if (tblCmpVmSnaps.size() > 0)
        {
            getVmInstanceDetailInfoRsp.setSnapInfos(tblCmpVmSnaps.stream().map(tblVmSnap -> {
                VmInstanceDetailInfoRsp.SnapInfo snapInfo = new VmInstanceDetailInfoRsp.SnapInfo();
                snapInfo.setSnapId(tblVmSnap.getSnapId());
                snapInfo.setSnapName(tblVmSnap.getName());
                snapInfo.setPhaseStatus(tblVmSnap.getPhaseStatus());
                snapInfo.setCreateTime(Utils.formatDate(tblCmpVmInstance.getCreateTime()));
                snapInfo.setUpdateTime(Utils.formatDate(tblCmpVmInstance.getUpdateTime()));
                return snapInfo;
            }).collect(Collectors.toList()));
        }
        List<RpcVolumeInfo> volumeInfos = rpcVolumeService.getVolumeInfosByVmId(cloudId, vmInstanceId);

        getVmInstanceDetailInfoRsp.setDiskInfos(volumeInfos);

        RpcImage image = rpcImageService.getImage(cloudId, tblCmpVmInstance.getImageId());
        if (null != image)
        {
            getVmInstanceDetailInfoRsp.setImageName(image.getName());
            getVmInstanceDetailInfoRsp.setImageOsType(image.getImageOsType());
        }
        else
        {
            Integer osType = "linux".equals(tblCmpVmInstance.getOsType()) ? ImageOsType.LINUX : ImageOsType.WINDOWS;
            getVmInstanceDetailInfoRsp.setImageOsType(osType);
        }
        List<PciDeviceInfo> pciDeviceInfos = new ArrayList<>();

        TblCmpPciDeviceExample pciDeviceExample = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria pciDeviceCriteria = pciDeviceExample.createCriteria();
        pciDeviceCriteria.andVmInstanceIdEqualTo(vmInstanceId);
        pciDeviceCriteria.andCloudIdEqualTo(cloudId);
        pciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
        pciDeviceCriteria.andPhaseStatusNotEqualTo(REMOVED);
        pciDeviceExample.setStartRow(0);
        pciDeviceExample.setPageSize(8);
        pciDeviceExample.setOrderByClause("create_time desc, device_id desc");

        List<TblCmpPciDevice> tblCmpPciDevices = vmComputeRepository.getPciDevices(pciDeviceExample);
        pciDeviceInfos = tblCmpPciDevices.stream().map(tblCmpPciDevice ->
        {
            PciDeviceInfo pciDeviceInfo = new PciDeviceInfo();
            pciDeviceInfo.setPciDevice(tblCmpPciDevice);
            return pciDeviceInfo;
        }).collect(Collectors.toList());

        getVmInstanceDetailInfoRsp.setPciInfos(pciDeviceInfos);

        TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, getVmInstanceDetailInfoRsp.getVolumeId());
        if (tblCmpVolume != null) getVmInstanceDetailInfoRsp.setStoragePoolId(tblCmpVolume.getStoragePoolId());

        cloudService.syncSingleData(cloudId, vmInstanceId, SyncMsg.NS_VM_INSTANCE);

        return getVmInstanceDetailInfoRsp;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeVmInstance(String cloudId, String instanceId, String userId, Boolean removeRootDisk) throws WebSystemException
    {
        try
        {
            TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
            if (tblCmpVmInstance == null)
            {
                LOGGER.error("get vm instance null: instance id {}", instanceId);
                throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVmInstance.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpVmInstance.getVmInstanceId().equals(tblCmpVmInstance.getEeId()) && tblCmpVmInstance.getPhaseStatus() == NSInstanceStatus.INSTANCE_CREATE_FAILED.getCode())
            {
                tblCmpVmInstance.setEeStatus(REMOVED);
                vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            checkVmInstanceStatus(tblCmpVmInstance);

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpVmInstance.setEeStatus(REMOVED);
                vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);

                refreashPciDevices(cloudId, instanceId);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove vm instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    private void refreashPciDevices(String cloudId, String vmInstanceId)
    {
        TblCmpPciDeviceExample pciDeviceExample = new TblCmpPciDeviceExample();
        TblCmpPciDeviceExample.Criteria pciDeviceCriteria = pciDeviceExample.createCriteria();
        pciDeviceCriteria.andVmInstanceIdEqualTo(vmInstanceId);
        pciDeviceCriteria.andCloudIdEqualTo(cloudId);
        pciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
        pciDeviceCriteria.andPhaseStatusNotEqualTo(REMOVED);
        pciDeviceExample.setStartRow(0);
        pciDeviceExample.setPageSize(8);
        pciDeviceExample.setOrderByClause("create_time desc, device_id desc");

        List<TblCmpPciDevice> tblCmpPciDevices = vmComputeRepository.getPciDevices(pciDeviceExample);

        if (!tblCmpPciDevices.isEmpty())
        {
            tblCmpPciDevices.forEach(tblCmpPciDevice -> cloudService.syncSingleData(cloudId, tblCmpPciDevice.getDeviceId(), SyncMsg.NS_PCI_DEVICE, DataSyncLevel.LEVEL_6));
        }
    }

    public Map<String,String> getVncInfo(String cloudId, String vmInstanceId, String userId) throws WebSystemException
    {
        LOGGER.info("get vnc info : vmInstanceId: {}, userId: {}", vmInstanceId, userId);
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
        if (null == tblCmpVmInstance)
        {
            LOGGER.info("the vm does not exist, vmInstanceId: {}",vmInstanceId);
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (!Objects.equals(userId, tblCmpVmInstance.getEeUser()))
        {
            LOGGER.info("The VM is not owned by the user, vmInstanceId:{} userId:{}",vmInstanceId,userId);
            throw new WebSystemException(ErrorCode.PARAM_ERROR,ErrorLevel.INFO);
        }

        checkVmInstanceStatus(tblCmpVmInstance);

        TblCmpHypervisorNode tblCmpHypervisorNode = vmComputeRepository.getHypervisorNodeById(cloudId, tblCmpVmInstance.getNodeId());
        if (null == tblCmpHypervisorNode)
        {
            throw new WebSystemException(ErrorCode.HYPERVISOR_NODE_NOT_EXIST, ErrorLevel.INFO);
        }
        Map<String,String> vncInfo = new HashMap<>();
        vncInfo.put("agent",tblCmpHypervisorNode.getManageIp());
        vncInfo.put("token", tblCmpVmInstance.getInstanceIdFromAgent());
        return vncInfo;
    }

    public ResponseEntity updateVmInstance(String cloudId, VmInstanceUpdateReq req, String instanceId, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
            if (tblCmpVmInstance == null)
            {
                LOGGER.error("get vm instance null: instance id {}", instanceId);
                throw new WebSystemException(ErrorCode.INSTANCE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVmInstance.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            checkVmInstanceStatus(tblCmpVmInstance);

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("update vm instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity poweroffInstance(String cloudId, String instanceId, Boolean detachment)
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("power off instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity poweronInstance(String cloudId, String instanceId)
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("power on instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity restartInstance(String cloudId, String instanceId)
    {
        try
        {

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("restart instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkVmInstanceStatus(String cloudId, String vmInstanceId)
    {
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
        checkVmInstanceStatus(tblCmpVmInstance);
    }

    public void checkVmInstanceStatus(TblCmpVmInstance tblCmpVmInstance)
    {
        if (null == tblCmpVmInstance || REMOVED == tblCmpVmInstance.getPhaseStatus() || REMOVED == tblCmpVmInstance.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        else if (NSInstanceStatus.CREATING_STATUS.contains(tblCmpVmInstance.getPhaseStatus()) || SYNCING != tblCmpVmInstance.getEeStatus())
        {
            throw new WebSystemException(ErrorCode.RESOURCE_IS_CREATING, ErrorLevel.INFO);
        }
    }

    public void checkVmInstanceUser(String cloudId, String instanceId, String userId)
    {
        TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
        if (null == tblCmpVmInstance)
        {
            throw new WebSystemException(ErrorCode.VM_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVmInstance.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public UserResourceMetrics getUserResourceMetrics(String cloudId, String userId)
    {
        try
        {
            UserResourceMetrics userResourceMetrics = new UserResourceMetrics();
            TblCmpVmInstanceExample tblCmpVmInstanceExample = new TblCmpVmInstanceExample();
            TblCmpVmInstanceExample.Criteria criteria = tblCmpVmInstanceExample.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            criteria.andEeUserEqualTo(userId);
            long vmNum = vmComputeRepository.countVmInstancesByExample(tblCmpVmInstanceExample);
            userResourceMetrics.setVmNum((int)vmNum);
            return userResourceMetrics;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("get user resources metrics error, {}", e.getMessage());
            return null;
        }
    }

    private void checkAddVmInstanceNetworkParams(String cloudId, VmInstanceCreateReq addVmInstanceReq, String bpId, String userId)
    {
        if (addVmInstanceReq.getNetworkInfos() == null) addVmInstanceReq.setNetworkInfos(new ArrayList<>());
        if (addVmInstanceReq.getNetworkInfos().isEmpty()) addVmInstanceReq.getNetworkInfos().add(new VmInstanceCreateReq.NetworkInfo());
        if (StringUtils.isEmpty(addVmInstanceReq.getNetworkInfos().get(0).getVpcId())
                || StringUtils.isEmpty(addVmInstanceReq.getNetworkInfos().get(0).getSubnetId()))
        {
            TblCmpSubnet tblCmpSubnet = networkService.checkUserAvailableSubnet(cloudId, bpId, userId);

            if (tblCmpSubnet == null)
            {
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }
            else
            {
                addVmInstanceReq.getNetworkInfos().get(0).setVpcId(tblCmpSubnet.getVpcId());
                addVmInstanceReq.getNetworkInfos().get(0).setSubnetId(tblCmpSubnet.getSubnetId());
            }
        }

        if (addVmInstanceReq.getSgIds() == null) addVmInstanceReq.setSgIds(new ArrayList<>());
        if (addVmInstanceReq.getSgIds().isEmpty())
        {
            String defaultSgId = sgService.checkUserDefaultSecurityGroup(cloudId, bpId, userId);

            if (StringUtils.isEmpty(defaultSgId))
            {
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }
            else
            {
                addVmInstanceReq.getSgIds().add(defaultSgId);
            }
        }
    }
}
