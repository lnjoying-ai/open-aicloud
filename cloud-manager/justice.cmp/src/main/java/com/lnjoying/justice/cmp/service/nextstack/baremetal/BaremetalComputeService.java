package com.lnjoying.justice.cmp.service.nextstack.baremetal;

import com.lnjoying.justice.cmp.common.BaremetalInstanceStatus;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.BaremetalDeviceSearchCritical;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.BaremetalInstanceSearchCritical;
import com.lnjoying.justice.cmp.db.repo.BaremetalComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.BaremetalDeviceAddReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.BaremetalInstanceCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.NetworkDetailInfoReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.RpcImage;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcNetworkDetailInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.*;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.rpc.CombRpcService;
import com.lnjoying.justice.cmp.service.rpc.RpcImageServiceImpl;
import com.lnjoying.justice.cmp.service.rpc.RpcNetworkServiceImpl;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class BaremetalComputeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    @Autowired
    private BaremetalComputeRepository baremetalComputeRepository;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private RpcImageServiceImpl rpcImageService;

    @Autowired
    private RpcNetworkServiceImpl rpcNetworkService;

    public ResponseEntity addBaremetalDevice(String cloudId, BaremetalDeviceAddReq reqParam, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add baremetal device error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String deviceId = (String) resultMap.get("deviceId");
                    if (StringUtils.isEmpty(deviceId))
                    {
                        LOGGER.error("add baremetal device error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
                        tblCmpBaremetalDevice.setDeviceId(deviceId);
                        tblCmpBaremetalDevice.setCloudId(cloudId);
                        tblCmpBaremetalDevice.setEeBp(bpId);
                        tblCmpBaremetalDevice.setEeUser(userId);
                        tblCmpBaremetalDevice.setEeStatus(SYNCING);

                        tblCmpBaremetalDevice.setName(reqParam.getName());
                        tblCmpBaremetalDevice.setIpmiIp(reqParam.getIpmiIp());
                        tblCmpBaremetalDevice.setIpmiPort(reqParam.getIpmiPort());
                        tblCmpBaremetalDevice.setIpmiMac(reqParam.getMac());
                        tblCmpBaremetalDevice.setBmctype(reqParam.getBmcType());
                        tblCmpBaremetalDevice.setDescription(reqParam.getDescription());
                        try
                        {
                            baremetalComputeRepository.insertBaremetalDevice(tblCmpBaremetalDevice);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpBaremetalDevice updateTblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, deviceId);
                            updateTblCmpBaremetalDevice.setEeBp(bpId);
                            updateTblCmpBaremetalDevice.setEeUser(userId);
                            updateTblCmpBaremetalDevice.setEeStatus(SYNCING);
                            baremetalComputeRepository.updateBaremetalDevice(updateTblCmpBaremetalDevice);
                        }

                        cloudService.syncSingleData(cloudId, deviceId, SyncMsg.NS_BAREMETAL_DEVICE);
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
            LOGGER.error("add baremetal device failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public BaremetalDevicesConfigsRsp getBaremetalDevicesConfigsRsp(String cloudId) throws WebSystemException
    {
        BaremetalDevicesConfigsRsp getBaremetalDevicesConfigsRsp = new BaremetalDevicesConfigsRsp();
        getBaremetalDevicesConfigsRsp.setCpuNums(baremetalComputeRepository.getCpuNum(cloudId));
        getBaremetalDevicesConfigsRsp.setMemTotals(baremetalComputeRepository.getMemTotal(cloudId));
        return getBaremetalDevicesConfigsRsp;
    }


    public BaremetalDevicesRsp getBaremetalDevices(String cloudId, BaremetalDeviceSearchCritical critical) throws WebSystemException
    {
        try
        {
            BaremetalDevicesRsp getBaremetalDevicesRsp = new BaremetalDevicesRsp();
            long totalNum = baremetalComputeRepository.countBaremetalPhasesAndUserId(cloudId, critical.getBaremetalDevicePhaseStatus(),
                    critical.getNicSpecPhaseStatus(), critical.getUserId(), critical.getCpuNum(), critical.getMemTotal());
            getBaremetalDevicesRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getBaremetalDevicesRsp;
            }

            int begin = ((critical.getPageNum() - 1) * critical.getPageSize());
            String orderByClause = "create_time desc, device_id desc";
            List<BaremetalDeviceInfo> baremetalDeviceInfos = baremetalComputeRepository.getBaremetalDeviceInfos(cloudId, critical.getBaremetalDevicePhaseStatus(),
                    critical.getNicSpecPhaseStatus(), critical.getUserId(), critical.getCpuNum(), critical.getMemTotal(), orderByClause, critical.getPageSize(), begin);
            getBaremetalDevicesRsp.setDevices(baremetalDeviceInfos);

            baremetalDeviceInfos.forEach(baremetalDeviceInfo -> cloudService.syncSingleData(cloudId, baremetalDeviceInfo.getDeviceId(), SyncMsg.NS_BAREMETAL_DEVICE));

            return getBaremetalDevicesRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal devices failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public BaremetalDeviceDetailInfoRsp getBaremetalDevice(String cloudId, String deviceId) throws WebSystemException
    {
        TblCmpBaremetalDevice tblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, deviceId);
        if (null == tblCmpBaremetalDevice || tblCmpBaremetalDevice.getEeStatus() == REMOVED)
        {
            LOGGER.error("get baremetal device null: device id {}", deviceId);
            throw new WebSystemException(ErrorCode.BAREMETAL_DEVICE_NOT_EXIST, ErrorLevel.INFO);
        }
        TblCmpBaremetalDeviceSpec tblCmpBaremetalDeviceSpec = baremetalComputeRepository.getBaremetalDeviceSpecById(cloudId, tblCmpBaremetalDevice.getDeviceSpecId());
        BaremetalDeviceDetailInfoRsp getBaremetalDeviceDetailInfoRsp = new BaremetalDeviceDetailInfoRsp();

        getBaremetalDeviceDetailInfoRsp.setBaremetalDeviceDetailInfo(tblCmpBaremetalDevice, tblCmpBaremetalDeviceSpec);

        getBaremetalDeviceDetailInfoRsp.setClusterName("");

        TblCmpBaremetalInstanceExample example = new TblCmpBaremetalInstanceExample();
        TblCmpBaremetalInstanceExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andDeviceIdEqualTo(deviceId);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (baremetalComputeRepository.countBaremetalInstanceByExample(example)>0)
        {
            TblCmpBaremetalInstance tblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstances(example).get(0);
            getBaremetalDeviceDetailInfoRsp.setInstanceId(tblCmpBaremetalInstance.getInstanceId());
            getBaremetalDeviceDetailInfoRsp.setInstanceName(tblCmpBaremetalInstance.getName());
        }

        String userId = getBaremetalDeviceDetailInfoRsp.getUserId();
        if (null == userId || userId.isEmpty())
        {
            getBaremetalDeviceDetailInfoRsp.setUserName("");
        }
        else
        {
            String user = combRpcService.getUmsService().getUserNameByUserId(userId);
            if (null != user)
            {
                getBaremetalDeviceDetailInfoRsp.setUserName(user);
            }
        }

        cloudService.syncSingleData(cloudId, deviceId, SyncMsg.NS_BAREMETAL_DEVICE);

        return getBaremetalDeviceDetailInfoRsp;
    }

    public ResponseEntity removeBaremetalDevice(String cloudId, String deviceId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpBaremetalDevice tblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, deviceId);

            if (tblCmpBaremetalDevice == null)
            {
                LOGGER.error("get baremetal device null: device id {}", deviceId);
                throw new WebSystemException(ErrorCode.BAREMETAL_DEVICE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpBaremetalDevice.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpBaremetalDevice.setEeStatus(REMOVED);
                tblCmpBaremetalDevice.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove baremetal device failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity addBaremetalInstance(String cloudId, BaremetalInstanceCreateReq reqParam, String bpId, String userId)
    {
        try
        {
            LOGGER.info("add baremetal instance");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add baremetal instance error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String instanceId = (String) resultMap.get("instanceId");
                    if (StringUtils.isEmpty(instanceId))
                    {
                        LOGGER.error("add baremetal instance error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
                        tblCmpBaremetalInstance.setInstanceId(instanceId);
                        tblCmpBaremetalInstance.setCloudId(cloudId);
                        tblCmpBaremetalInstance.setEeBp(bpId);
                        tblCmpBaremetalInstance.setEeUser(userId);
                        tblCmpBaremetalInstance.setEeStatus(SYNCING);

                        tblCmpBaremetalInstance.setName(reqParam.getName());
                        tblCmpBaremetalInstance.setDeviceId(reqParam.getDeviceId());
                        tblCmpBaremetalInstance.setPhaseStatus(BaremetalInstanceStatus.INSTANCE_INIT);
                        tblCmpBaremetalInstance.setImageId(reqParam.getImageId());
                        tblCmpBaremetalInstance.setVpcId(reqParam.getVpcId());
                        tblCmpBaremetalInstance.setSubnetId(reqParam.getSubnetId());
                        tblCmpBaremetalInstance.setHostName(reqParam.getHostname());
                        tblCmpBaremetalInstance.setSysUsername(reqParam.getSysUsername());
                        tblCmpBaremetalInstance.setDescription(reqParam.getDescription());
                        try
                        {
                            baremetalComputeRepository.insertBaremetalInstance(tblCmpBaremetalInstance);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpBaremetalInstance updateTblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, instanceId);
                            updateTblCmpBaremetalInstance.setEeBp(bpId);
                            updateTblCmpBaremetalInstance.setEeUser(userId);
                            updateTblCmpBaremetalInstance.setEeStatus(SYNCING);
                            baremetalComputeRepository.updateBaremetalInstance(updateTblCmpBaremetalInstance);
                        }

                        cloudService.syncSingleData(cloudId, instanceId, SyncMsg.NS_BAREMETAL_INSTANCE);
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
            LOGGER.error("add baremetal instance failed: instance name {}, {}", reqParam.getImageId(), e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public BaremetalInstancesRsp getBaremetalInstances(String cloudId, BaremetalInstanceSearchCritical critical, String userId)
    {
        try
        {
            TblCmpBaremetalInstanceExample example = new TblCmpBaremetalInstanceExample();
            TblCmpBaremetalInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            if (userId != null)
            {
                criteria.andEeUserEqualTo(userId);
            }
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            List<String> eipMapPorts ;
            if (!StringUtils.isBlank(critical.getName()))
            {
                criteria.andNameLike("%" + critical.getName() + "%");
            }

            if (null != critical.getPortIdIsNull() && critical.getPortIdIsNull())
            {
                criteria.andPortIdFromAgentIsNull();
            }
            else if (null != critical.getPortIdIsNull() && !critical.getPortIdIsNull())
            {
                criteria.andPortIdFromAgentIsNotNull();
            }

            if (!StringUtils.isBlank(critical.getSubnetId()))
            {
                criteria.andSubnetIdEqualTo(critical.getSubnetId());
            }
            if (null != critical.getEipMapIsUsing() && !critical.getEipMapIsUsing())
            {
                eipMapPorts = rpcNetworkService.getPorts(cloudId, critical.getSubnetId());
                eipMapPorts.forEach(criteria::andPortIdFromAgentNotEqualTo);
            }

            BaremetalInstancesRsp getBaremetalInstancesRsp = new BaremetalInstancesRsp();

            long totalNum = baremetalComputeRepository.countBaremetalInstanceByExample(example);
            getBaremetalInstancesRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getBaremetalInstancesRsp;
            }

            int begin = ((critical.getPageNum() - 1) * critical.getPageSize());
            example.setOrderByClause("create_time desc,instance_id desc");

            //to do
            example.setStartRow(begin);
            example.setPageSize(critical.getPageSize());

            List<TblCmpBaremetalInstance> tblCmpBaremetalInstanceList = baremetalComputeRepository.getBaremetalInstances(example);
            if (null == tblCmpBaremetalInstanceList)
            {
                return getBaremetalInstancesRsp;
            }

            List<String> imageIdList = new ArrayList<>();
            List<NetworkDetailInfoReq> networkInfoList = new ArrayList<>();
            for (TblCmpBaremetalInstance tblCmpBaremetalInstance : tblCmpBaremetalInstanceList)
            {
                imageIdList.add(tblCmpBaremetalInstance.getImageId());

                NetworkDetailInfoReq req = new NetworkDetailInfoReq();
                req.setVpcId(tblCmpBaremetalInstance.getVpcId());
                req.setSubnetId(tblCmpBaremetalInstance.getSubnetId());
                req.setPortId(tblCmpBaremetalInstance.getPortIdFromAgent());
                networkInfoList.add(req);
            }

            List<RpcImage> imageList = rpcImageService.getBatchImages(cloudId, imageIdList);
            if (null == imageList || imageList.size() != imageIdList.size())
            {
                LOGGER.error("get baremetal instances failed, get image service error");
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }

            List<RpcNetworkDetailInfo> networkInfoDetailList = rpcNetworkService.getBatchNetworkInfos(cloudId, networkInfoList);
            if (null == networkInfoDetailList || networkInfoDetailList.size() != networkInfoList.size())
            {
                LOGGER.error("get baremetal instances failed, get network service error");
                throw new WebSystemException(ErrorCode.SystemError,ErrorLevel.INFO);
            }

            List<BaremetalInstanceInfo> baremetalInstanceInfoList = new ArrayList<BaremetalInstanceInfo>();
            for (int i = 0; i < tblCmpBaremetalInstanceList.size(); i++)
            {
                TblCmpBaremetalInstance tblCmpBaremetalInstance = tblCmpBaremetalInstanceList.get(i);

                BaremetalInstanceInfo baremetalInstanceInfo = new BaremetalInstanceInfo();
                baremetalInstanceInfo.setInstanceInfo(tblCmpBaremetalInstance);

                TblCmpBaremetalDevice tblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, tblCmpBaremetalInstance.getDeviceId());
                if (null != tblCmpBaremetalDevice)
                {
                    BaremetalDeviceAbbrInfo deviceInfo = new BaremetalDeviceAbbrInfo();
                    deviceInfo.setDeviceId(tblCmpBaremetalDevice.getDeviceId());
                    deviceInfo.setName(tblCmpBaremetalDevice.getName());

                    BaremetalDeviceSpecAbbrInfo device_spec_info = new BaremetalDeviceSpecAbbrInfo();
                    device_spec_info.setDeviceSpecId(tblCmpBaremetalDevice.getDeviceSpecId());
                    device_spec_info.setName("");       //to do later
                    deviceInfo.setDeviceSpecInfo(device_spec_info);

                    baremetalInstanceInfo.setDeviceInfo(deviceInfo);
                }

                if (imageList.size() == tblCmpBaremetalInstanceList.size())
                {
                    RpcImage image = imageList.get(i);

                    ImageAbbrInfo imageAbbrInfo = new ImageAbbrInfo();
                    imageAbbrInfo.setImageId(tblCmpBaremetalInstance.getImageId());
                    imageAbbrInfo.setName(image.getName());
                    baremetalInstanceInfo.setImageInfo(imageAbbrInfo);
                }

                if (networkInfoDetailList.size() == tblCmpBaremetalInstanceList.size())
                {
                    RpcNetworkDetailInfo networkDetailInfo = networkInfoDetailList.get(i);

                    VpcAbbrInfo vpcAbbrInfo = new VpcAbbrInfo();
                    vpcAbbrInfo.setVpcId(networkDetailInfo.getVpcId());
                    vpcAbbrInfo.setName(networkDetailInfo.getVpcName());
                    vpcAbbrInfo.setCidr(networkDetailInfo.getVpcCidr());
                    baremetalInstanceInfo.setVpcInfo(vpcAbbrInfo);

                    SubnetAbbrInfo subnetAbbrInfo = new SubnetAbbrInfo();
                    subnetAbbrInfo.setSubnetId(networkDetailInfo.getSubnetId());
                    subnetAbbrInfo.setName(networkDetailInfo.getSubnetName());
                    subnetAbbrInfo.setCidr(networkDetailInfo.getSubnetCidr());
                    baremetalInstanceInfo.setSubnetInfo(subnetAbbrInfo);

                    PortAbbrInfo portAbbrInfo = new PortAbbrInfo();
                    portAbbrInfo.setPortId(networkDetailInfo.getPortId());
                    portAbbrInfo.setIpAddress(networkDetailInfo.getIpAddress());
                    baremetalInstanceInfo.setPortInfo(portAbbrInfo);
                }

                baremetalInstanceInfoList.add(baremetalInstanceInfo);
            }

            getBaremetalInstancesRsp.setInstances(baremetalInstanceInfoList);

            baremetalInstanceInfoList.forEach(baremetalInstance -> cloudService.syncSingleData(cloudId, baremetalInstance.getInstanceId(), SyncMsg.NS_BAREMETAL_INSTANCE));

            return getBaremetalInstancesRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get baremetal instances failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public BaremetalInstanceDetailInfoRsp getBaremetalInstance(String cloudId, String instanceId, String userId)
    {
        TblCmpBaremetalInstance tblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, instanceId);
        if (null == tblCmpBaremetalInstance || tblCmpBaremetalInstance.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.BAREMETAL_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpBaremetalInstance.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        BaremetalInstanceDetailInfoRsp getBaremetalInstanceDetailInfoRsp = new BaremetalInstanceDetailInfoRsp();
        getBaremetalInstanceDetailInfoRsp.setInstanceDetailInfo(tblCmpBaremetalInstance);

        NetworkDetailInfoReq networkDetailInfoReq = new NetworkDetailInfoReq();
        networkDetailInfoReq.setVpcId(tblCmpBaremetalInstance.getVpcId());
        networkDetailInfoReq.setSubnetId(tblCmpBaremetalInstance.getSubnetId());
        networkDetailInfoReq.setPortId(tblCmpBaremetalInstance.getPortIdFromAgent());
        RpcNetworkDetailInfo networkDetailInfo = rpcNetworkService.getNetworkDetailInfo(cloudId, networkDetailInfoReq);
        if (null != networkDetailInfo)
        {
            getBaremetalInstanceDetailInfoRsp.setSubnetName(networkDetailInfo.getSubnetName());
            getBaremetalInstanceDetailInfoRsp.setVpcName(networkDetailInfo.getVpcName());
            getBaremetalInstanceDetailInfoRsp.setSubnetCidr(networkDetailInfo.getSubnetCidr());
            getBaremetalInstanceDetailInfoRsp.setIp(networkDetailInfo.getIpAddress());
        }

        List<NicInfo> nicInfoList = new ArrayList<>();

        String deviceId = tblCmpBaremetalInstance.getDeviceId();
        List<TblCmpNicInfo> tblCmpNicInfoList = baremetalComputeRepository.getNicsByDeviceId(cloudId, deviceId);
        for (TblCmpNicInfo tblCmpNicInfo : tblCmpNicInfoList)
        {
            NicInfo nicInfo = new NicInfo();
            nicInfo.setNicInfo(tblCmpNicInfo);

            nicInfoList.add(nicInfo);
        }
        getBaremetalInstanceDetailInfoRsp.setNicInfos(nicInfoList);

        RpcImage image = rpcImageService.getImage(cloudId, tblCmpBaremetalInstance.getImageId());
        if (null != image)
        {
            getBaremetalInstanceDetailInfoRsp.setImageName(image.getName());
            getBaremetalInstanceDetailInfoRsp.setImageOsType(image.getImageOsType());
        }

        cloudService.syncSingleData(cloudId, instanceId, SyncMsg.NS_BAREMETAL_INSTANCE);

        return getBaremetalInstanceDetailInfoRsp;
    }

    public ResponseEntity removeBaremetalInstance(String cloudId, String instanceId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpBaremetalInstance tblUpdateBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, instanceId);

            if (tblUpdateBaremetalInstance == null)
            {
                LOGGER.error("get baremetal instance null: instance id {}", instanceId);
                throw new WebSystemException(ErrorCode.BAREMETAL_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblUpdateBaremetalInstance.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblUpdateBaremetalInstance.setEeStatus(REMOVED);
                tblUpdateBaremetalInstance.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                baremetalComputeRepository.updateBaremetalInstance(tblUpdateBaremetalInstance);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove baremetal instance failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public InstanceStatsRsp getInstanceStats(String cloudId, String userId)
    {
        InstanceStatsRsp getInstanceStatsRsp = new InstanceStatsRsp();
        int countTotal = (int)baremetalComputeRepository.countInstanceByPhasesUserId(cloudId, userId, null);
        int countRunning = (int)baremetalComputeRepository.countInstanceByPhasesUserId(cloudId, userId, BaremetalInstanceStatus.INSTANCE_RUNNING);
        int countPoweroff = (int)baremetalComputeRepository.countInstanceByPhasesUserId(cloudId, userId, BaremetalInstanceStatus.INSTANCE_POWEROFF);
        int countCreateFailed = (int)baremetalComputeRepository.countInstanceByPhasesUserId(cloudId, userId, BaremetalInstanceStatus.INSTANCE_CREATE_FAILED);
        getInstanceStatsRsp.setInstanceTotal(countTotal);
        getInstanceStatsRsp.setInstanceRunning(countRunning);
        getInstanceStatsRsp.setInstancePowerOff(countPoweroff);
        getInstanceStatsRsp.setInstanceCreateFailed(countCreateFailed);
        getInstanceStatsRsp.setInstanceCreating(countTotal-countRunning-countPoweroff-countCreateFailed);

        return getInstanceStatsRsp;
    }

    public void checkBaremetalInstanceUser(String cloudId, String instanceId, String userId)
    {
        TblCmpBaremetalInstance tblUpdateBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, instanceId);
        if (tblUpdateBaremetalInstance == null)
        {
            LOGGER.error("get baremetal instance null: instance id {}", instanceId);
            throw new WebSystemException(ErrorCode.BAREMETAL_INSTANCE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblUpdateBaremetalInstance.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
