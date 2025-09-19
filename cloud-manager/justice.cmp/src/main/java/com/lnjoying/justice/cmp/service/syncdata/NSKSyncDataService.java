package com.lnjoying.justice.cmp.service.syncdata;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.common.constant.ImageOsType;
import com.lnjoying.justice.cmp.common.nextstack.AgentConstant;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.EipPortMapCreateReqVo;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.PciDeviceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.EipInfoVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.*;
import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalDeviceInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalInstanceInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.NicInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.PubkeyDetailInfo;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import com.lnjoying.justice.cmp.domain.model.SyncResourceInfo;
import com.lnjoying.justice.cmp.service.process.CloudProcessStrategy;
import com.lnjoying.justice.cmp.utils.CustomErrorHandler;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.*;

@Service
public class NSKSyncDataService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NSKSyncDataService.class);

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private CloudManagerConfig cloudManagerConfig;

    @Autowired
    private BaremetalComputeRepository baremetalComputeRepository;

    @Autowired
    private FlavorRepository flavorRepository;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private CloudProcessStrategy cloudProcessStrategy;

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public void syncData(String cloudId)
    {
        syncBaremetalDevices(cloudId);
        syncBaremetalInstances(cloudId);
        syncPubkeys(cloudId);
        syncFlavors(cloudId);
        syncImages(cloudId);
        syncStoragePools(cloudId);
        syncVolumes(cloudId);
        syncVolumeSnaps(cloudId);
        syncHypervisorNodes(cloudId);
        syncInstanceGroups(cloudId);
        syncPciDevices(cloudId);
        syncResourceStats(cloudId);
        syncVmInstances(cloudId);
        syncSnaps(cloudId);
        syncPciDeviceAvailableNodes(cloudId);
        syncNfsServers(cloudId);
        syncVpcs(cloudId);
        syncSubnets(cloudId);
        syncEips(cloudId);
        syncPortMaps(cloudId);
        syncEipPools(cloudId);
        syncSecurityGroups(cloudId);
    }

    public void syncBaremetalDevices(String cloudId)
    {
        String urlPattern = "/api/bm/v1/baremetal_devices?page_num=%s&page_size=100";
        try
        {
            Set<String> deviceIds = baremetalComputeRepository.getBaremetalDeviceIds(cloudId);
            Set<String> deviceIdSet = new HashSet<>();
            if (deviceIds == null)
            {
                deviceIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    BaremetalDevicesRsp getBaremetalDevicesRsp = gson.fromJson(result, BaremetalDevicesRsp.class);
                    if (getBaremetalDevicesRsp == null || CollectionUtils.isEmpty(getBaremetalDevicesRsp.getDevices()))
                    {
                        return;
                    }

                    for (BaremetalDeviceInfo baremetalDeviceInfo : getBaremetalDevicesRsp.getDevices())
                    {
                        TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
                        tblCmpBaremetalDevice.setCloudId(cloudId);
                        tblCmpBaremetalDevice.setDeviceId(baremetalDeviceInfo.getDeviceId());
                        tblCmpBaremetalDevice.setName(baremetalDeviceInfo.getName());
                        tblCmpBaremetalDevice.setPhaseStatus(baremetalDeviceInfo.getPhaseStatus());
                        tblCmpBaremetalDevice.setClusterId(baremetalDeviceInfo.getClusterId());
                        tblCmpBaremetalDevice.setDeviceSpecId(null);
                        tblCmpBaremetalDevice.setUserId(baremetalDeviceInfo.getUserId());
                        tblCmpBaremetalDevice.setAddressType(null);
                        tblCmpBaremetalDevice.setIpmiIp(baremetalDeviceInfo.getIpmiIp());
                        tblCmpBaremetalDevice.setIpmiPort(baremetalDeviceInfo.getIpmiPort() == null ? null : baremetalDeviceInfo.getIpmiPort().shortValue());
                        tblCmpBaremetalDevice.setIpmiUsername(null);
                        tblCmpBaremetalDevice.setIpmiPassword(null);
                        tblCmpBaremetalDevice.setBmctype(null);
                        tblCmpBaremetalDevice.setIpmiMac(null);
                        tblCmpBaremetalDevice.setDeviceIdFromAgent(null);
                        tblCmpBaremetalDevice.setDescription(null);
                        tblCmpBaremetalDevice.setCreateTime(baremetalDeviceInfo.getCreateTime());
                        tblCmpBaremetalDevice.setUpdateTime(baremetalDeviceInfo.getUpdateTime());
                        tblCmpBaremetalDevice.setEeStatus(SYNCING);
                        try
                        {
                            baremetalComputeRepository.insertBaremetalDevice(tblCmpBaremetalDevice);
                        }
                        catch (DuplicateKeyException e)
                        {
                            baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);
                        }

                        deviceIds.remove(baremetalDeviceInfo.getDeviceId());
                        deviceIdSet.add(baremetalDeviceInfo.getDeviceId());
                    }
                    total = getBaremetalDevicesRsp.getTotalNum();
                }
            }

            if (deviceIds.size() > 0)
            {
                for (String deviceId : deviceIds)
                {
                    TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
                    tblCmpBaremetalDevice.setCloudId(cloudId);
                    tblCmpBaremetalDevice.setDeviceId(deviceId);
                    tblCmpBaremetalDevice.setEeStatus(REMOVED);

                    baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);
                }
            }

            if (deviceIdSet.size() > 0)
            {
                for (String deviceId : deviceIdSet)
                {
                    syncSingleData(cloudId, deviceId, SyncMsg.NS_BAREMETAL_DEVICE);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} baremetal devices error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncBaremetalDevice(String cloudId, String deviceId)
    {
        String url = "/api/bm/v1/baremetal_devices/" + deviceId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("baremetal device does not exists"))
                {
                    TblCmpBaremetalDevice tblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, deviceId);
                    if (tblCmpBaremetalDevice != null)
                    {
                        tblCmpBaremetalDevice.setEeStatus(REMOVED);
                        baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);
                    }
                    return;
                }

                BaremetalDeviceDetailInfoRsp baremetalDeviceDetailInfoRsp = gson.fromJson(result, BaremetalDeviceDetailInfoRsp.class);
                if (baremetalDeviceDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
                tblCmpBaremetalDevice.setCloudId(cloudId);
                tblCmpBaremetalDevice.setDeviceId(baremetalDeviceDetailInfoRsp.getDeviceId());
                tblCmpBaremetalDevice.setName(baremetalDeviceDetailInfoRsp.getName());
                tblCmpBaremetalDevice.setPhaseStatus(baremetalDeviceDetailInfoRsp.getPhaseStatus());
                tblCmpBaremetalDevice.setClusterId(baremetalDeviceDetailInfoRsp.getClusterId());
                tblCmpBaremetalDevice.setDeviceSpecId(null);
                tblCmpBaremetalDevice.setUserId(baremetalDeviceDetailInfoRsp.getUserId());
                tblCmpBaremetalDevice.setAddressType(null);
                tblCmpBaremetalDevice.setIpmiIp(baremetalDeviceDetailInfoRsp.getIpmiIp());
                tblCmpBaremetalDevice.setIpmiPort((short)baremetalDeviceDetailInfoRsp.getIpmiPort());
                tblCmpBaremetalDevice.setIpmiUsername(baremetalDeviceDetailInfoRsp.getIpmiUsername());
                tblCmpBaremetalDevice.setIpmiPassword(null);
                tblCmpBaremetalDevice.setBmctype(null);
                tblCmpBaremetalDevice.setIpmiMac(baremetalDeviceDetailInfoRsp.getMac());
                tblCmpBaremetalDevice.setDeviceIdFromAgent(null);
                tblCmpBaremetalDevice.setDescription(baremetalDeviceDetailInfoRsp.getDescription());
                tblCmpBaremetalDevice.setCreateTime(null);
                tblCmpBaremetalDevice.setUpdateTime(null);
                tblCmpBaremetalDevice.setEeStatus(SYNCING);

                try
                {
                    baremetalComputeRepository.insertBaremetalDevice(tblCmpBaremetalDevice);
                }
                catch (DuplicateKeyException e)
                {
                    baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);

                    TblCmpBaremetalDevice updateTblCmpBaremetalDevice = baremetalComputeRepository.getBaremetalDeviceById(cloudId, tblCmpBaremetalDevice.getDeviceId());
                    if (StringUtils.isNotEmpty(updateTblCmpBaremetalDevice.getDeviceSpecId()))
                    {
                        TblCmpBaremetalDeviceSpec tblCmpBaremetalDeviceSpec = new TblCmpBaremetalDeviceSpec();
                        tblCmpBaremetalDeviceSpec.setCloudId(cloudId);
                        tblCmpBaremetalDeviceSpec.setDeviceSpecId(updateTblCmpBaremetalDevice.getDeviceSpecId());
                        tblCmpBaremetalDeviceSpec.setName(null);
                        tblCmpBaremetalDeviceSpec.setProductName(null);
                        tblCmpBaremetalDeviceSpec.setSerialNumber(null);
                        tblCmpBaremetalDeviceSpec.setManufacturer(null);
                        tblCmpBaremetalDeviceSpec.setArchitecture(baremetalDeviceDetailInfoRsp.getArchitecture());
                        tblCmpBaremetalDeviceSpec.setProcessorCount(null);
                        tblCmpBaremetalDeviceSpec.setCpuLogicNum(null);
                        tblCmpBaremetalDeviceSpec.setCpuPhysicalNum(null);
                        tblCmpBaremetalDeviceSpec.setCpuModelName(baremetalDeviceDetailInfoRsp.getCpuModelName());
                        tblCmpBaremetalDeviceSpec.setCpuFrequency(baremetalDeviceDetailInfoRsp.getCpuFrequency());
                        tblCmpBaremetalDeviceSpec.setCpuNum(baremetalDeviceDetailInfoRsp.getCpuNum());
                        tblCmpBaremetalDeviceSpec.setMemTotal(baremetalDeviceDetailInfoRsp.getMemTotal());
                        tblCmpBaremetalDeviceSpec.setDiskTotal(baremetalDeviceDetailInfoRsp.getDiskTotal());
                        tblCmpBaremetalDeviceSpec.setDiskType(null);
                        tblCmpBaremetalDeviceSpec.setDiskDetail(null);
                        tblCmpBaremetalDeviceSpec.setCreateTime(null);
                        tblCmpBaremetalDeviceSpec.setUpdateTime(null);
                        tblCmpBaremetalDeviceSpec.setEeBp(null);
                        tblCmpBaremetalDeviceSpec.setEeUser(null);
                        tblCmpBaremetalDeviceSpec.setEeStatus(SYNCING);

                        try
                        {
                            baremetalComputeRepository.insertBaremetalDeviceSpec(tblCmpBaremetalDeviceSpec);
                        }
                        catch (DuplicateKeyException e1)
                        {
                            baremetalComputeRepository.updateBaremetalDeviceSpec(tblCmpBaremetalDeviceSpec);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} baremetal device {} error msg: {}", cloudId, deviceId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncBaremetalInstances(String cloudId)
    {
        String urlPattern = "/api/bm/v1/baremetal_instances?page_num=%s&page_size=100";
        try
        {
            Set<String> instanceIds = baremetalComputeRepository.getBaremetalInstanceIds(cloudId);
            if (instanceIds == null)
            {
                instanceIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    BaremetalInstancesRsp getBaremetalInstancesRsp = gson.fromJson(result, BaremetalInstancesRsp.class);
                    if (getBaremetalInstancesRsp == null || CollectionUtils.isEmpty(getBaremetalInstancesRsp.getInstances()))
                    {
                        return;
                    }

                    for (BaremetalInstanceInfo baremetalInstanceInfo : getBaremetalInstancesRsp.getInstances())
                    {
                        TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
                        tblCmpBaremetalInstance.setCloudId(cloudId);
                        tblCmpBaremetalInstance.setInstanceId(baremetalInstanceInfo.getInstanceId());
                        tblCmpBaremetalInstance.setName(baremetalInstanceInfo.getName());
                        tblCmpBaremetalInstance.setDeviceId(baremetalInstanceInfo.getDeviceInfo() == null ? null : baremetalInstanceInfo.getDeviceInfo().getDeviceId());
                        tblCmpBaremetalInstance.setPhaseStatus(baremetalInstanceInfo.getPhaseStatus());
                        tblCmpBaremetalInstance.setImageId(baremetalInstanceInfo.getImageInfo() == null ? null : baremetalInstanceInfo.getImageInfo().getImageId());
                        tblCmpBaremetalInstance.setVpcId(baremetalInstanceInfo.getVpcInfo() == null ? null : baremetalInstanceInfo.getVpcInfo().getVpcId());
                        tblCmpBaremetalInstance.setSubnetId(baremetalInstanceInfo.getSubnetInfo() == null ? null : baremetalInstanceInfo.getSubnetInfo().getSubnetId());
                        tblCmpBaremetalInstance.setStaticIp(null);
                        tblCmpBaremetalInstance.setHostName(baremetalInstanceInfo.getHostname());
                        tblCmpBaremetalInstance.setSysUsername(null);
                        tblCmpBaremetalInstance.setSysPassword(null);
                        tblCmpBaremetalInstance.setPubkeyId(null);
                        tblCmpBaremetalInstance.setIscsiTarget(null);
                        tblCmpBaremetalInstance.setIscsiInitiator(null);
                        tblCmpBaremetalInstance.setIscsiIpport(null);
                        tblCmpBaremetalInstance.setShareIdFromAgent(null);
                        tblCmpBaremetalInstance.setNicIdFromAgent(null);
                        tblCmpBaremetalInstance.setPortIdFromAgent(baremetalInstanceInfo.getPortInfo() == null ? null : baremetalInstanceInfo.getPortInfo().getPortId());
                        tblCmpBaremetalInstance.setInstanceIdFromAgent(null);
                        tblCmpBaremetalInstance.setDescription(baremetalInstanceInfo.getDescription());
                        tblCmpBaremetalInstance.setCreateTime(baremetalInstanceInfo.getCreateTime() == null ? null : Utils.parseStTime(baremetalInstanceInfo.getCreateTime()));
                        tblCmpBaremetalInstance.setUpdateTime(baremetalInstanceInfo.getUpdateTime() == null ? null : Utils.parseStTime(baremetalInstanceInfo.getUpdateTime()));
                        tblCmpBaremetalInstance.setEeStatus(SYNCING);
                        try
                        {
                            baremetalComputeRepository.insertBaremetalInstance(tblCmpBaremetalInstance);
                        }
                        catch (DuplicateKeyException e)
                        {
                            baremetalComputeRepository.updateBaremetalInstance(tblCmpBaremetalInstance);
                        }

                        instanceIds.remove(baremetalInstanceInfo.getInstanceId());

                        if (baremetalInstanceInfo.getDeviceInfo() != null && baremetalInstanceInfo.getDeviceInfo().getDeviceId() != null)
                        {
                            TblCmpBaremetalDevice tblCmpBaremetalDevice = new TblCmpBaremetalDevice();
                            tblCmpBaremetalDevice.setCloudId(cloudId);
                            tblCmpBaremetalDevice.setDeviceId(baremetalInstanceInfo.getDeviceInfo() == null ? null : baremetalInstanceInfo.getDeviceInfo().getDeviceId());
                            tblCmpBaremetalDevice.setDeviceSpecId(baremetalInstanceInfo.getDeviceInfo() == null ? null : baremetalInstanceInfo.getDeviceInfo().getDeviceSpecInfo() == null ? null : baremetalInstanceInfo.getDeviceInfo().getDeviceSpecInfo().getDeviceSpecId());
                            tblCmpBaremetalDevice.setEeStatus(SYNCING);
                            try
                            {
                                baremetalComputeRepository.insertBaremetalDevice(tblCmpBaremetalDevice);
                            }
                            catch (DuplicateKeyException e)
                            {
                                baremetalComputeRepository.updateBaremetalDevice(tblCmpBaremetalDevice);
                            }
                        }

                        if (baremetalInstanceInfo.getPortInfo() != null && baremetalInstanceInfo.getPortInfo().getPortId() != null)
                        {
                            TblCmpPort tblCmpPort = new TblCmpPort();
                            tblCmpPort.setCloudId(cloudId);
                            tblCmpPort.setPortId(baremetalInstanceInfo.getPortInfo() == null ? null : baremetalInstanceInfo.getPortInfo().getPortId());
                            tblCmpPort.setIpAddress(baremetalInstanceInfo.getPortInfo() == null ? null : baremetalInstanceInfo.getPortInfo().getIpAddress());
                            tblCmpPort.setEeStatus(SYNCING);
                            tblCmpPort.setInstanceId(baremetalInstanceInfo.getInstanceId());
                            try
                            {
                                networkRepository.createPort(tblCmpPort);
                            }
                            catch (DuplicateKeyException e)
                            {
                                networkRepository.updatePortSelective(tblCmpPort);
                            }
                        }
                    }
                    total = getBaremetalInstancesRsp.getTotalNum();
                }
            }

            if (instanceIds.size() > 0)
            {
                for (String instanceId : instanceIds)
                {
                    TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
                    tblCmpBaremetalInstance.setCloudId(cloudId);
                    tblCmpBaremetalInstance.setInstanceId(instanceId);
                    tblCmpBaremetalInstance.setEeStatus(REMOVED);

                    baremetalComputeRepository.updateBaremetalInstance(tblCmpBaremetalInstance);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} baremetal instances error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncBaremetalInstance(String cloudId, String instanceId)
    {
        String url = "/api/bm/v1/baremetal_instances/" + instanceId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("baremetal instance does not exists"))
                {
                    TblCmpBaremetalInstance tblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, instanceId);
                    if (tblCmpBaremetalInstance != null)
                    {
                        tblCmpBaremetalInstance.setEeStatus(REMOVED);
                        baremetalComputeRepository.updateBaremetalInstance(tblCmpBaremetalInstance);
                    }
                    return;
                }

                BaremetalInstanceDetailInfoRsp baremetalDeviceDetailInfoRsp = gson.fromJson(result, BaremetalInstanceDetailInfoRsp.class);
                if (baremetalDeviceDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpBaremetalInstance tblCmpBaremetalInstance = new TblCmpBaremetalInstance();
                tblCmpBaremetalInstance.setCloudId(cloudId);
                tblCmpBaremetalInstance.setInstanceId(baremetalDeviceDetailInfoRsp.getInstanceId());
                tblCmpBaremetalInstance.setName(baremetalDeviceDetailInfoRsp.getName());
                tblCmpBaremetalInstance.setDeviceId(baremetalDeviceDetailInfoRsp.getDeviceId());
                tblCmpBaremetalInstance.setPhaseStatus((int)baremetalDeviceDetailInfoRsp.getPhaseStatus());
                tblCmpBaremetalInstance.setImageId(baremetalDeviceDetailInfoRsp.getImageId());
                tblCmpBaremetalInstance.setVpcId(baremetalDeviceDetailInfoRsp.getVpcId());
                tblCmpBaremetalInstance.setSubnetId(baremetalDeviceDetailInfoRsp.getSubnetId());
                tblCmpBaremetalInstance.setStaticIp(null);
                tblCmpBaremetalInstance.setHostName(baremetalDeviceDetailInfoRsp.getHostname());
                tblCmpBaremetalInstance.setSysUsername(baremetalDeviceDetailInfoRsp.getSysUsername());
                tblCmpBaremetalInstance.setSysPassword(null);
                tblCmpBaremetalInstance.setPubkeyId(baremetalDeviceDetailInfoRsp.getPubkeyId());
                tblCmpBaremetalInstance.setIscsiTarget(null);
                tblCmpBaremetalInstance.setIscsiInitiator(null);
                tblCmpBaremetalInstance.setIscsiIpport(null);
                tblCmpBaremetalInstance.setShareIdFromAgent(null);
                tblCmpBaremetalInstance.setNicIdFromAgent(null);
                tblCmpBaremetalInstance.setPortIdFromAgent(null);
                tblCmpBaremetalInstance.setInstanceIdFromAgent(null);
                tblCmpBaremetalInstance.setDescription(baremetalDeviceDetailInfoRsp.getDescription());
                tblCmpBaremetalInstance.setCreateTime(null);
                tblCmpBaremetalInstance.setUpdateTime(null);
                tblCmpBaremetalInstance.setEeStatus(SYNCING);
                try
                {
                    baremetalComputeRepository.insertBaremetalInstance(tblCmpBaremetalInstance);
                }
                catch (DuplicateKeyException e)
                {
                    baremetalComputeRepository.updateBaremetalInstance(tblCmpBaremetalInstance);
                }

                if (! CollectionUtils.isEmpty(baremetalDeviceDetailInfoRsp.getNicInfos()))
                {
                    for (NicInfo nicInfo : baremetalDeviceDetailInfoRsp.getNicInfos())
                    {
                        TblCmpNicInfo tblCmpNicInfo = new TblCmpNicInfo();
                        tblCmpNicInfo.setCloudId(cloudId);
                        tblCmpNicInfo.setNicId(nicInfo.getNicId());
                        tblCmpNicInfo.setDeviceId(baremetalDeviceDetailInfoRsp.getDeviceId());
                        tblCmpNicInfo.setNicName(null);
                        tblCmpNicInfo.setLinkState(null);
                        tblCmpNicInfo.setNetworkType(nicInfo.getNetworkType() == null ? null : nicInfo.getNetworkType().shortValue());
                        tblCmpNicInfo.setIpmiMac(null);
                        tblCmpNicInfo.setIp(nicInfo.getIp());
                        tblCmpNicInfo.setAddressType(nicInfo.getAddressType() == null ? null : nicInfo.getAddressType().shortValue());
                        tblCmpNicInfo.setEeBp(null);
                        tblCmpNicInfo.setEeUser(null);
                        tblCmpNicInfo.setEeStatus(SYNCING);
                        try
                        {
                            baremetalComputeRepository.insertNicInfo(tblCmpNicInfo);
                        }
                        catch (DuplicateKeyException e)
                        {
                            baremetalComputeRepository.updateNicInfo(tblCmpNicInfo);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} baremetal instance {} error msg: {}", cloudId, instanceId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPubkeys(String cloudId)
    {
        String urlPattern = "/api/bm/v1/pubkeys?page_num=%s&page_size=100";
        try
        {
            Set<String> pubkeyIds = baremetalComputeRepository.getPubkeyIds(cloudId);
            if (pubkeyIds == null)
            {
                pubkeyIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    PubkeysRsp getPubkeysRsp = gson.fromJson(result, PubkeysRsp.class);
                    if (getPubkeysRsp == null || CollectionUtils.isEmpty(getPubkeysRsp.getPubkeys()))
                    {
                        return;
                    }

                    for (PubkeyDetailInfo getPubkeyDetailInfo : getPubkeysRsp.getPubkeys())
                    {
                        TblCmpPubkey tblCmpPubkey = new TblCmpPubkey();
                        tblCmpPubkey.setCloudId(cloudId);
                        tblCmpPubkey.setPubkeyId(getPubkeyDetailInfo.getPubkeyId());
                        tblCmpPubkey.setUserId(null);
                        tblCmpPubkey.setName(getPubkeyDetailInfo.getName());
                        tblCmpPubkey.setPubkey(getPubkeyDetailInfo.getPubkey());
                        tblCmpPubkey.setDescription(getPubkeyDetailInfo.getDescription());
                        tblCmpPubkey.setCreateTime(getPubkeyDetailInfo.getCreateTime() == null ? null : Utils.parseStTime(getPubkeyDetailInfo.getCreateTime()));
                        tblCmpPubkey.setUpdateTime(getPubkeyDetailInfo.getUpdateTime() == null ? null : Utils.parseStTime(getPubkeyDetailInfo.getUpdateTime()));
                        tblCmpPubkey.setEeStatus(SYNCING);

                        try
                        {
                            baremetalComputeRepository.insertPubkey(tblCmpPubkey);
                        } catch (DuplicateKeyException e)
                        {
                            baremetalComputeRepository.updatePubkey(tblCmpPubkey);
                        }

                        pubkeyIds.remove(getPubkeyDetailInfo.getPubkeyId());
                    }
                    total = getPubkeysRsp.getTotalNum();
                }
            }

            if (pubkeyIds.size() > 0)
            {
                for (String pubkeyId : pubkeyIds)
                {
                    TblCmpPubkey tblCmpPubkey = new TblCmpPubkey();
                    tblCmpPubkey.setCloudId(cloudId);
                    tblCmpPubkey.setPubkeyId(pubkeyId);
                    tblCmpPubkey.setEeStatus(REMOVED);

                    baremetalComputeRepository.updatePubkey(tblCmpPubkey);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} pubkeys error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPubkey(String cloudId, String pubkeyId)
    {
        String url = "/api/bm/v1/pubkeys/" + pubkeyId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("pubkey does not exists"))
                {
                    TblCmpPubkey tblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, pubkeyId);
                    if (tblCmpPubkey != null)
                    {
                        tblCmpPubkey.setEeStatus(REMOVED);
                        baremetalComputeRepository.updatePubkey(tblCmpPubkey);
                    }
                    return;
                }

                PubkeyDetailInfo pubkeyDetailInfo = gson.fromJson(result, PubkeyDetailInfo.class);

                if (pubkeyDetailInfo == null)
                {
                    return;
                }

                TblCmpPubkey tblCmpPubkey = new TblCmpPubkey();
                tblCmpPubkey.setCloudId(cloudId);
                tblCmpPubkey.setPubkeyId(pubkeyDetailInfo.getPubkeyId());
                tblCmpPubkey.setUserId(null);
                tblCmpPubkey.setName(pubkeyDetailInfo.getName());
                tblCmpPubkey.setPubkey(pubkeyDetailInfo.getPubkey());
                tblCmpPubkey.setDescription(pubkeyDetailInfo.getDescription());
                tblCmpPubkey.setCreateTime(pubkeyDetailInfo.getCreateTime() == null ? null : Utils.parseStTime(pubkeyDetailInfo.getCreateTime()));
                tblCmpPubkey.setUpdateTime(pubkeyDetailInfo.getUpdateTime() == null ? null : Utils.parseStTime(pubkeyDetailInfo.getUpdateTime()));
                tblCmpPubkey.setEeStatus(SYNCING);

                try
                {
                    baremetalComputeRepository.insertPubkey(tblCmpPubkey);
                } catch (DuplicateKeyException e)
                {
                    baremetalComputeRepository.updatePubkey(tblCmpPubkey);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} pubkey {} error msg: {}", cloudId, pubkeyId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFlavors(String cloudId)
    {
        String urlPattern = "/api/repo/v1/flavors?type=0&page_num=%s&page_size=100";
        try
        {
            Set<String> flavorIds = flavorRepository.getFlavorIds(cloudId);
            if (flavorIds == null)
            {
                flavorIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    FlavorsRsp getFlavorsRsp = gson.fromJson(result, FlavorsRsp.class);
                    if (getFlavorsRsp == null || CollectionUtils.isEmpty(getFlavorsRsp.getFlavors()))
                    {
                        return;
                    }

                    for (FlavorDetailInfoRsp flavorDetailInfoRsp : getFlavorsRsp.getFlavors())
                    {

                        TblCmpFlavor tblCmpFlavor = new TblCmpFlavor();
                        tblCmpFlavor.setCloudId(cloudId);
                        tblCmpFlavor.setEeStatus(SYNCING);
                        tblCmpFlavor.setFlavorId(flavorDetailInfoRsp.getFlavorId());
                        tblCmpFlavor.setName(flavorDetailInfoRsp.getName());
                        tblCmpFlavor.setType(flavorDetailInfoRsp.getType() == null ? null : flavorDetailInfoRsp.getType().shortValue());
                        tblCmpFlavor.setCpu(flavorDetailInfoRsp.getCpu());
                        tblCmpFlavor.setMem(flavorDetailInfoRsp.getMem());
                        tblCmpFlavor.setCreateTime(flavorDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(flavorDetailInfoRsp.getCreateTime()));
                        tblCmpFlavor.setUpdateTime(flavorDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(flavorDetailInfoRsp.getUpdateTime()));
                        tblCmpFlavor.setGpuCount(flavorDetailInfoRsp.getGpuCount());
                        tblCmpFlavor.setGpuName(flavorDetailInfoRsp.getGpuName());
                        tblCmpFlavor.setPhaseStatus(flavorDetailInfoRsp.getPhaseStatus());
                        tblCmpFlavor.setNeedIb(flavorDetailInfoRsp.getNeedIb());

                        try
                        {
                            flavorRepository.insertFlavor(tblCmpFlavor);
                        } catch (DuplicateKeyException e)
                        {
                            flavorRepository.updateFlavor(tblCmpFlavor);
                        }

                        flavorIds.remove(tblCmpFlavor.getFlavorId());
                    }
                    total = getFlavorsRsp.getTotalNum();
                }
            }

            if (flavorIds.size() > 0)
            {
                for (String flavorId : flavorIds)
                {
                    TblCmpFlavor tblCmpFlavor = new TblCmpFlavor();
                    tblCmpFlavor.setCloudId(cloudId);
                    tblCmpFlavor.setFlavorId(flavorId);
                    tblCmpFlavor.setEeStatus(REMOVED);

                    flavorRepository.updateFlavor(tblCmpFlavor);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} flavors error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFlavor(String cloudId, String flavorId)
    {
        String url = "/api/repo/v1/flavors/" + flavorId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("image does not exists") || result.contains("flavor does not exists"))
                {
                    TblCmpFlavor tblCmpFlavor = flavorRepository.getFlavorById(cloudId, flavorId);
                    if (tblCmpFlavor != null)
                    {
                        tblCmpFlavor.setEeStatus(REMOVED);
                        flavorRepository.updateFlavor(tblCmpFlavor);
                    }
                    return;
                }

                FlavorDetailInfoRsp flavorDetailInfoRsp = gson.fromJson(result, FlavorDetailInfoRsp.class);

                if (flavorDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpFlavor tblCmpFlavor = new TblCmpFlavor();
                tblCmpFlavor.setCloudId(cloudId);
                tblCmpFlavor.setEeStatus(SYNCING);
                tblCmpFlavor.setFlavorId(flavorDetailInfoRsp.getFlavorId());
                tblCmpFlavor.setName(flavorDetailInfoRsp.getName());
                tblCmpFlavor.setType(flavorDetailInfoRsp.getType() == null ? null : flavorDetailInfoRsp.getType().shortValue());
                tblCmpFlavor.setCpu(flavorDetailInfoRsp.getCpu());
                tblCmpFlavor.setMem(flavorDetailInfoRsp.getMem());
                tblCmpFlavor.setCreateTime(flavorDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(flavorDetailInfoRsp.getCreateTime()));
                tblCmpFlavor.setUpdateTime(flavorDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(flavorDetailInfoRsp.getUpdateTime()));
                tblCmpFlavor.setGpuCount(flavorDetailInfoRsp.getGpuCount());
                tblCmpFlavor.setGpuName(flavorDetailInfoRsp.getGpuName());
                tblCmpFlavor.setPhaseStatus(flavorDetailInfoRsp.getPhaseStatus());
                tblCmpFlavor.setNeedIb(flavorDetailInfoRsp.getNeedIb());

                try
                {
                    flavorRepository.insertFlavor(tblCmpFlavor);
                } catch (DuplicateKeyException e)
                {
                    flavorRepository.updateFlavor(tblCmpFlavor);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} flavor {} error msg: {}", cloudId, flavorId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncImages(String cloudId)
    {
        List<Boolean> isVms = Lists.newArrayList(true, false);
        String urlPattern = "/api/repo/v1/images?is_vm=%s&page_num=%s&page_size=100";
        ImagesRsp getImagesRsp = null;
        try
        {
            Set<String> imageIds = imageRepository.getImageIds(cloudId);
            if (imageIds == null)
            {
                imageIds = new HashSet<>();
            }

            for (Boolean isVm : isVms)
            {
                int pageNum = 0;
                long total = 100;
                while ((pageNum * 100L) < total)
                {
                    pageNum = pageNum + 1;
                    String result = getDataFromCloud(cloudId, String.format(urlPattern, isVm, pageNum));
                    if (StringUtils.isNotEmpty(result))
                    {
                        getImagesRsp = gson.fromJson(result, ImagesRsp.class);
                        if (getImagesRsp == null || CollectionUtils.isEmpty(getImagesRsp.getImages()))
                        {
                            break;
                        }

                        for (ImageDetailInfoRsp getImageDetailInfoRsp : getImagesRsp.getImages())
                        {
                            TblCmpImage tblCmpImage = new TblCmpImage();
                            tblCmpImage.setCloudId(cloudId);
                            tblCmpImage.setImageId(getImageDetailInfoRsp.getImageId());
                            if (isVm)
                            {
                                tblCmpImage.setFileIdFromAgent(null);
                            } else
                            {
                                tblCmpImage.setFileIdFromAgent("EE_CMP_PADDING");
                            }
                            tblCmpImage.setUserId(null);
                            tblCmpImage.setImageOsType(getImageDetailInfoRsp.getImageOsType() == null ? null : getImageDetailInfoRsp.getImageOsType().shortValue());
                            tblCmpImage.setImageOsVendor(null);
                            tblCmpImage.setImageOsVersion(null);
                            tblCmpImage.setImageName(getImageDetailInfoRsp.getImageName());
                            tblCmpImage.setImageFormat(getImageDetailInfoRsp.getImageFormat() == null ? null : getImageDetailInfoRsp.getImageFormat().shortValue());
                            tblCmpImage.setAgentIp(null);
                            tblCmpImage.setPhaseStatus(getImageDetailInfoRsp.getPhaseStatus() == null ? null : getImageDetailInfoRsp.getPhaseStatus().shortValue());
                            tblCmpImage.setPhaseInfo(null);
                            tblCmpImage.setIsPublic(getImageDetailInfoRsp.getIsPublic());
                            tblCmpImage.setCreateTime(getImageDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(getImageDetailInfoRsp.getCreateTime()));
                            tblCmpImage.setUpdateTime(getImageDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(getImageDetailInfoRsp.getUpdateTime()));
                            tblCmpImage.setEeStatus(SYNCING);

                            try
                            {
                                imageRepository.createImage(tblCmpImage);
                            } catch (DuplicateKeyException e)
                            {
                                imageRepository.updateImage(tblCmpImage);
                            }

                            imageIds.remove(getImageDetailInfoRsp.getImageId());

                            syncSingleData(cloudId, getImageDetailInfoRsp.getImageId(), SyncMsg.NS_IMAGE);
                        }
                        total = getImagesRsp.getTotalNum();
                    }
                }
            }

            if (imageIds.size() > 0)
            {
                for (String imageId : imageIds)
                {
                    TblCmpImage tblCmpImage = new TblCmpImage();
                    tblCmpImage.setCloudId(cloudId);
                    tblCmpImage.setImageId(imageId);
                    tblCmpImage.setEeStatus(REMOVED);

                    imageRepository.updateImage(tblCmpImage);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} images error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncImage(String cloudId, String imageId)
    {
        String url = "/api/repo/v1/images/" + imageId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("image does not exists"))
                {
                    TblCmpImage tblCmpImage = imageRepository.getImageById(cloudId, imageId);
                    if (tblCmpImage != null)
                    {
                        tblCmpImage.setEeStatus(REMOVED);
                        imageRepository.updateImage(tblCmpImage);
                    }
                    return;
                }

                ImageDetailInfoRsp imageDetailInfoRsp = gson.fromJson(result, ImageDetailInfoRsp.class);

                if (imageDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpImage tblCmpImage = new TblCmpImage();
                tblCmpImage.setCloudId(cloudId);
                tblCmpImage.setImageId(imageDetailInfoRsp.getImageId());
                tblCmpImage.setUserId(null);
                tblCmpImage.setImageOsType(imageDetailInfoRsp.getImageOsType() == null ? null : imageDetailInfoRsp.getImageOsType().shortValue());
                tblCmpImage.setImageOsVendor(imageDetailInfoRsp.getImageOsVendor() == null ? null : imageDetailInfoRsp.getImageOsVendor().shortValue());
                tblCmpImage.setImageOsVersion(imageDetailInfoRsp.getImageOsVersion());
                tblCmpImage.setImageName(imageDetailInfoRsp.getImageName());
                tblCmpImage.setImageFormat(imageDetailInfoRsp.getImageFormat() == null ? null : imageDetailInfoRsp.getImageFormat().shortValue());
                tblCmpImage.setAgentIp(null);
                tblCmpImage.setPhaseStatus(imageDetailInfoRsp.getPhaseStatus() == null ? null : imageDetailInfoRsp.getPhaseStatus().shortValue());
                tblCmpImage.setPhaseInfo(null);
                tblCmpImage.setIsPublic(imageDetailInfoRsp.getIsPublic());
                tblCmpImage.setCreateTime(imageDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(imageDetailInfoRsp.getCreateTime()));
                tblCmpImage.setUpdateTime(imageDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(imageDetailInfoRsp.getUpdateTime()));
                tblCmpImage.setEeStatus(SYNCING);

                try
                {
                    imageRepository.createImage(tblCmpImage);
                } catch (DuplicateKeyException e)
                {
                    imageRepository.updateImage(tblCmpImage);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} image {} error msg: {}", cloudId, imageId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncStoragePools(String cloudId)
    {
        String urlPattern = "/api/repo/v1/storage_pools?page_num=%s&page_size=100";
        try
        {
            Set<String> storagePoolIds = repoRepository.getStoragePoolIds(cloudId);
            if (storagePoolIds == null)
            {
                storagePoolIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    StoragePoolsRsp storagePoolsRsp = gson.fromJson(result, StoragePoolsRsp.class);
                    if (storagePoolsRsp == null || CollectionUtils.isEmpty(storagePoolsRsp.getStoragePools()))
                    {
                        return;
                    }

                    for (StoragePoolDetailInfoRsp storagePoolDetailInfoRsp : storagePoolsRsp.getStoragePools())
                    {
                        TblCmpStoragePool tblCmpStoragePool = new TblCmpStoragePool();
                        tblCmpStoragePool.setCloudId(cloudId);
                        tblCmpStoragePool.setStoragePoolId(storagePoolDetailInfoRsp.getPoolId());
                        tblCmpStoragePool.setEeStatus(SYNCING);
                        tblCmpStoragePool.setStoragePoolIdFromAgent(null);
                        tblCmpStoragePool.setType(storagePoolDetailInfoRsp.getType());
                        tblCmpStoragePool.setSid(storagePoolDetailInfoRsp.getSid());
                        tblCmpStoragePool.setParas(storagePoolDetailInfoRsp.getParas());
                        tblCmpStoragePool.setPhaseStatus(null);
                        tblCmpStoragePool.setCreateTime(storagePoolDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(storagePoolDetailInfoRsp.getCreateTime()));
                        tblCmpStoragePool.setUpdateTime(storagePoolDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(storagePoolDetailInfoRsp.getUpdateTime()));
                        tblCmpStoragePool.setName(storagePoolDetailInfoRsp.getName());
                        tblCmpStoragePool.setDescription(storagePoolDetailInfoRsp.getDescription());

                        try
                        {
                            repoRepository.insertStoragePool(tblCmpStoragePool);
                        }
                        catch (DuplicateKeyException e)
                        {
                            repoRepository.updateStoragePool(tblCmpStoragePool);
                        }

                        storagePoolIds.remove(tblCmpStoragePool.getStoragePoolId());
                    }
                    total = storagePoolsRsp.getTotalNum();
                }
            }

            if (storagePoolIds.size() > 0)
            {
                for (String storagePoolId : storagePoolIds)
                {
                    TblCmpStoragePool tblCmpStoragePool = new TblCmpStoragePool();
                    tblCmpStoragePool.setCloudId(cloudId);
                    tblCmpStoragePool.setStoragePoolId(storagePoolId);
                    tblCmpStoragePool.setEeStatus(REMOVED);

                    repoRepository.updateStoragePool(tblCmpStoragePool);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} storage pools error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncStoragePool(String cloudId, String storagePoolId)
    {
        String url = "/api/repo/v1/storage_pools/" + storagePoolId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("image does not exists") || result.contains("storage pool does not exists"))
                {
                    TblCmpStoragePool tblCmpStoragePool = repoRepository.getStoragePoolById(cloudId, storagePoolId);
                    if (tblCmpStoragePool != null)
                    {
                        tblCmpStoragePool.setEeStatus(REMOVED);
                        repoRepository.updateStoragePool(tblCmpStoragePool);
                    }
                    return;
                }

                StoragePoolDetailInfoRsp storagePoolDetailInfoRsp = gson.fromJson(result, StoragePoolDetailInfoRsp.class);

                if (storagePoolDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpStoragePool tblCmpStoragePool = new TblCmpStoragePool();
                tblCmpStoragePool.setCloudId(cloudId);
                tblCmpStoragePool.setStoragePoolId(storagePoolDetailInfoRsp.getPoolId());
                tblCmpStoragePool.setEeStatus(SYNCING);
                tblCmpStoragePool.setStoragePoolIdFromAgent(null);
                tblCmpStoragePool.setType(storagePoolDetailInfoRsp.getType());
                tblCmpStoragePool.setSid(storagePoolDetailInfoRsp.getSid());
                tblCmpStoragePool.setParas(storagePoolDetailInfoRsp.getParas());
                tblCmpStoragePool.setPhaseStatus(null);
                tblCmpStoragePool.setCreateTime(storagePoolDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(storagePoolDetailInfoRsp.getCreateTime()));
                tblCmpStoragePool.setUpdateTime(storagePoolDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(storagePoolDetailInfoRsp.getUpdateTime()));
                tblCmpStoragePool.setName(storagePoolDetailInfoRsp.getName());
                tblCmpStoragePool.setDescription(storagePoolDetailInfoRsp.getDescription());

                try
                {
                    repoRepository.insertStoragePool(tblCmpStoragePool);
                }
                catch (DuplicateKeyException e)
                {
                    repoRepository.updateStoragePool(tblCmpStoragePool);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} storage pool {} error msg: {}", cloudId, storagePoolId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVolumes(String cloudId)
    {
        List<Boolean> detacheds = Lists.newArrayList(true, false);
        String getVolumesUrlPattern = "/api/repo/v1/volumes?detached=%s&page_num=%s&page_size=100";
        String recycleVolumesUrlPattern = "/api/repo/v1/volumes/recycle?page_num=%s&page_size=100";
        try
        {
            Set<String> volumeIds = repoRepository.getVolumeIds(cloudId);
            if (volumeIds == null)
            {
                volumeIds = new HashSet<>();
            }

            for (Boolean detached : detacheds)
            {
                int pageNum = 0;
                long total = 100;
                while ((pageNum * 100L) < total)
                {
                    pageNum = pageNum + 1;
                    String result = getDataFromCloud(cloudId, String.format(getVolumesUrlPattern, detached, pageNum));
                    if (StringUtils.isNotEmpty(result))
                    {
                        VolumesRsp volumesRsp = gson.fromJson(result, VolumesRsp.class);
                        if (volumesRsp == null || CollectionUtils.isEmpty(volumesRsp.getVolumes()))
                        {
                            break;
                        }

                        for (VolumeVo volumeVo : volumesRsp.getVolumes())
                        {
                            TblCmpVolume tblCmpVolume = new TblCmpVolume();
                            tblCmpVolume.setCloudId(cloudId);
                            tblCmpVolume.setEeStatus(SYNCING);
                            tblCmpVolume.setVolumeId(volumeVo.getVolumeId());
                            tblCmpVolume.setStoragePoolId(volumeVo.getStoragePoolId());
                            tblCmpVolume.setVolumeIdFromAgent(null);
                            tblCmpVolume.setImageId(volumeVo.getImageId());
                            tblCmpVolume.setName(volumeVo.getName());
                            tblCmpVolume.setCreateTime(volumeVo.getCreateTime());
                            tblCmpVolume.setUpdateTime(volumeVo.getUpdateTime());
                            tblCmpVolume.setUserId(volumeVo.getUserId());
                            tblCmpVolume.setPhaseStatus(volumeVo.getPhaseStatus());
                            tblCmpVolume.setType(volumeVo.getType());
                            tblCmpVolume.setDescription(null);
                            tblCmpVolume.setSize(volumeVo.getSize());
                            tblCmpVolume.setExportName(null);
                            tblCmpVolume.setVmId(volumeVo.getVmInstanceId());
                            tblCmpVolume.setNodeIp(null);
                            tblCmpVolume.setLastIp(null);
                            tblCmpVolume.setDestIp(null);

                            if (!detached && volumeVo.getVmInstanceId() != null)
                            {
                                TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, volumeVo.getVmInstanceId());
                                if (tblCmpVmInstance != null)
                                {
                                    tblCmpVolume.setEeBp(tblCmpVmInstance.getEeBp());
                                    tblCmpVolume.setEeUser(tblCmpVmInstance.getEeUser());
                                }
                            }

                            try
                            {
                                repoRepository.insertVolume(tblCmpVolume);
                            } catch (DuplicateKeyException e)
                            {
                                if (volumeVo.getVmInstanceId() == null)
                                {
                                    TblCmpVolume updateTblCmpVolume = repoRepository.getVolumeById(cloudId, volumeVo.getVolumeId());
                                    updateTblCmpVolume.setCloudId(cloudId);
                                    updateTblCmpVolume.setEeStatus(SYNCING);
                                    updateTblCmpVolume.setVolumeId(volumeVo.getVolumeId());
                                    updateTblCmpVolume.setStoragePoolId(volumeVo.getStoragePoolId());
                                    updateTblCmpVolume.setVolumeIdFromAgent(null);
                                    updateTblCmpVolume.setImageId(volumeVo.getImageId());
                                    updateTblCmpVolume.setName(volumeVo.getName());
                                    updateTblCmpVolume.setCreateTime(volumeVo.getCreateTime());
                                    updateTblCmpVolume.setUpdateTime(volumeVo.getUpdateTime());
                                    updateTblCmpVolume.setUserId(volumeVo.getUserId());
                                    updateTblCmpVolume.setPhaseStatus(volumeVo.getPhaseStatus());
                                    updateTblCmpVolume.setType(volumeVo.getType());
                                    updateTblCmpVolume.setDescription(null);
                                    updateTblCmpVolume.setSize(volumeVo.getSize());
                                    updateTblCmpVolume.setExportName(null);
                                    updateTblCmpVolume.setVmId(volumeVo.getVmInstanceId());
                                    updateTblCmpVolume.setNodeIp(null);
                                    updateTblCmpVolume.setLastIp(null);
                                    updateTblCmpVolume.setDestIp(null);

                                    repoRepository.updateVolumeForce(updateTblCmpVolume);
                                } else
                                {
                                    repoRepository.updateVolume(tblCmpVolume);
                                }
                            }

                            volumeIds.remove(volumeVo.getVolumeId());
                        }
                        total = volumesRsp.getTotalNum();
                    }
                }
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(recycleVolumesUrlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    RootVolumesRsp volumesRsp = gson.fromJson(result, RootVolumesRsp.class);
                    if (volumesRsp == null || CollectionUtils.isEmpty(volumesRsp.getVolumes()))
                    {
                        return;
                    }

                    for (RootVolumeVo volumeVo : volumesRsp.getVolumes())
                    {
                        TblCmpVolume tblCmpVolume = new TblCmpVolume();
                        tblCmpVolume.setCloudId(cloudId);
                        tblCmpVolume.setEeStatus(SYNCING);
                        tblCmpVolume.setVolumeId(volumeVo.getVolumeId());
                        tblCmpVolume.setStoragePoolId(volumeVo.getStoragePoolId());
                        tblCmpVolume.setVolumeIdFromAgent(null);
                        tblCmpVolume.setImageId(volumeVo.getImageId());
                        tblCmpVolume.setName(volumeVo.getName());
                        tblCmpVolume.setCreateTime(volumeVo.getCreateTime());
                        tblCmpVolume.setUpdateTime(volumeVo.getUpdateTime());
                        tblCmpVolume.setUserId(volumeVo.getUserId());
                        tblCmpVolume.setPhaseStatus(volumeVo.getPhaseStatus());
                        tblCmpVolume.setType(volumeVo.getType());
                        tblCmpVolume.setDescription(null);
                        tblCmpVolume.setSize(volumeVo.getSize());
                        tblCmpVolume.setExportName(null);
                        tblCmpVolume.setVmId(null);
                        tblCmpVolume.setNodeIp(null);
                        tblCmpVolume.setLastIp(null);
                        tblCmpVolume.setDestIp(null);

                        try
                        {
                            repoRepository.insertVolume(tblCmpVolume);
                        } catch (DuplicateKeyException e)
                        {
                            repoRepository.updateVolume(tblCmpVolume);
                        }

                        volumeIds.remove(volumeVo.getVolumeId());
                    }
                    total = volumesRsp.getTotalNum();
                }
            }

            if (volumeIds.size() > 0)
            {
                for (String volumeId : volumeIds)
                {
                    TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);
                    if (tblCmpVolume == null || tblCmpVolume.getType() == 1)
                    {
                        syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME);
                        continue;
                    }
                    tblCmpVolume.setEeStatus(REMOVED);

                    repoRepository.updateVolume(tblCmpVolume);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVolume(String cloudId, String volumeId)
    {
        String url = "/api/repo/v1/volumes/" + volumeId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("volume does not exists"))
                {
                    TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);
                    if (tblCmpVolume != null && tblCmpVolume.getEeStatus() == SYNCING)
                    {
                        tblCmpVolume.setEeStatus(REMOVED);
                        repoRepository.updateVolume(tblCmpVolume);
                    }
                    return;
                }

                VolumeDetailInfoRsp volumeDetailInfoRsp = gson.fromJson(result, VolumeDetailInfoRsp.class);

                if (volumeDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpVolume tblCmpVolume = new TblCmpVolume();
                tblCmpVolume.setCloudId(cloudId);
                tblCmpVolume.setEeStatus(SYNCING);
                tblCmpVolume.setVolumeId(volumeDetailInfoRsp.getVolumeId());
                tblCmpVolume.setStoragePoolId(volumeDetailInfoRsp.getStoragePoolId());
                tblCmpVolume.setVolumeIdFromAgent(null);
                tblCmpVolume.setImageId(volumeDetailInfoRsp.getImageId());
                tblCmpVolume.setName(volumeDetailInfoRsp.getName());
                tblCmpVolume.setCreateTime(volumeDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(volumeDetailInfoRsp.getCreateTime()));
                tblCmpVolume.setUpdateTime(volumeDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(volumeDetailInfoRsp.getUpdateTime()));
                tblCmpVolume.setUserId(null);
                tblCmpVolume.setPhaseStatus(volumeDetailInfoRsp.getPhaseStatus());
                tblCmpVolume.setType(volumeDetailInfoRsp.getType());
                tblCmpVolume.setDescription(volumeDetailInfoRsp.getDescription());
                tblCmpVolume.setSize(volumeDetailInfoRsp.getSize());
                tblCmpVolume.setExportName(null);
                tblCmpVolume.setVmId(null);
                tblCmpVolume.setNodeIp(null);
                tblCmpVolume.setLastIp(null);
                tblCmpVolume.setDestIp(null);

                try
                {
                    repoRepository.insertVolume(tblCmpVolume);
                } catch (DuplicateKeyException e)
                {
                    repoRepository.updateVolume(tblCmpVolume);
                }

                if (StringUtils.isNotEmpty(volumeDetailInfoRsp.getImageId()) && volumeDetailInfoRsp.getImageOsVendor() != null)
                {
                    TblCmpImage tblCmpImage = new TblCmpImage();
                    tblCmpImage.setCloudId(cloudId);
                    tblCmpImage.setImageId(volumeDetailInfoRsp.getImageId());
                    tblCmpImage.setImageOsVendor(volumeDetailInfoRsp.getImageOsVendor() == null ? null : volumeDetailInfoRsp.getImageOsVendor().shortValue());

                    imageRepository.updateImage(tblCmpImage);
                }

                if (volumeDetailInfoRsp.getImageOsType() != null)
                {
                    TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                    tblCmpVmInstance.setCloudId(cloudId);
                    tblCmpVmInstance.setVolumeId(volumeId);
                    tblCmpVmInstance.setOsType(ImageOsType.LINUX == volumeDetailInfoRsp.getImageOsType() ? "linux" : "windows");

                    TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
                    TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
                    criteria.andCloudIdEqualTo(cloudId);
                    criteria.andEeStatusNotEqualTo(REMOVED);
                    criteria.andVolumeIdEqualTo(volumeId);
                    vmComputeRepository.updateVmInstanceByExample(tblCmpVmInstance, example);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume {} error msg: {}", cloudId, volumeId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVolumeSnaps(String cloudId)
    {
        String urlPattern = "/api/repo/v1/volume_snaps?page_num=%s&page_size=100";
        try
        {
            Set<String> volumeSnapIds = repoRepository.getVolumeSnapIds(cloudId);
            if (volumeSnapIds == null)
            {
                volumeSnapIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    VolumeSnapsRsp volumeSnapsRsp = gson.fromJson(result, VolumeSnapsRsp.class);
                    if (volumeSnapsRsp == null || CollectionUtils.isEmpty(volumeSnapsRsp.getVolumeSnaps()))
                    {
                        return;
                    }

                    for (VolumeSnapDetailInfoRsp volumeSnapDetailInfoRsp : volumeSnapsRsp.getVolumeSnaps())
                    {
                        TblCmpVolumeSnap tblCmpVolumeSnap = new TblCmpVolumeSnap();
                        tblCmpVolumeSnap.setCloudId(cloudId);
                        tblCmpVolumeSnap.setVolumeSnapId(volumeSnapDetailInfoRsp.getVolumeSnapId());
                        tblCmpVolumeSnap.setEeStatus(SYNCING);
                        tblCmpVolumeSnap.setVolumeSnapIdFromAgent(null);
                        tblCmpVolumeSnap.setUserId(volumeSnapDetailInfoRsp.getUserId());
                        tblCmpVolumeSnap.setVolumeId(volumeSnapDetailInfoRsp.getVolumeId());
                        tblCmpVolumeSnap.setCreateTime(volumeSnapDetailInfoRsp.getCreateTime());
                        tblCmpVolumeSnap.setUpdateTime(volumeSnapDetailInfoRsp.getUpdateTime());
                        tblCmpVolumeSnap.setPhaseStatus(volumeSnapDetailInfoRsp.getPhaseStatus());
                        tblCmpVolumeSnap.setDescription(volumeSnapDetailInfoRsp.getDescription());
                        tblCmpVolumeSnap.setName(volumeSnapDetailInfoRsp.getName());
                        tblCmpVolumeSnap.setIsCurrent(volumeSnapDetailInfoRsp.getIsCurrent());
                        tblCmpVolumeSnap.setParentId(volumeSnapDetailInfoRsp.getParentId());

                        try
                        {
                            repoRepository.insertVolumeSnap(tblCmpVolumeSnap);
                        } catch (DuplicateKeyException e)
                        {
                            repoRepository.updateVolumeSnap(tblCmpVolumeSnap);
                        }

                        volumeSnapIds.remove(volumeSnapDetailInfoRsp.getVolumeSnapId());
                    }
                    total = volumeSnapsRsp.getTotalNum();
                }
            }

            if (volumeSnapIds.size() > 0)
            {
                for (String volumeSnapId : volumeSnapIds)
                {
                    TblCmpVolumeSnap tblCmpVolumeSnap = new TblCmpVolumeSnap();
                    tblCmpVolumeSnap.setCloudId(cloudId);
                    tblCmpVolumeSnap.setVolumeSnapId(volumeSnapId);
                    tblCmpVolumeSnap.setEeStatus(REMOVED);

                    repoRepository.updateVolumeSnap(tblCmpVolumeSnap);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume snaps error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVolumeSnap(String cloudId, String volumeSnapId)
    {
        String url = "/api/repo/v1/volume_snaps/" + volumeSnapId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("volume snap does not exists"))
                {
                    TblCmpVolumeSnap tblCmpVolumeSnap = repoRepository.getVolumeSnapById(cloudId, volumeSnapId);
                    if (tblCmpVolumeSnap != null)
                    {
                        tblCmpVolumeSnap.setEeStatus(REMOVED);
                        repoRepository.updateVolumeSnap(tblCmpVolumeSnap);
                    }
                    return;
                }

                VolumeSnapDetailInfoRsp volumeSnapDetailInfoRsp = gson.fromJson(result, VolumeSnapDetailInfoRsp.class);

                if (volumeSnapDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpVolumeSnap tblCmpVolumeSnap = new TblCmpVolumeSnap();
                tblCmpVolumeSnap.setCloudId(cloudId);
                tblCmpVolumeSnap.setVolumeSnapId(volumeSnapDetailInfoRsp.getVolumeSnapId());
                tblCmpVolumeSnap.setEeStatus(SYNCING);
                tblCmpVolumeSnap.setVolumeSnapIdFromAgent(null);
                tblCmpVolumeSnap.setUserId(volumeSnapDetailInfoRsp.getUserId());
                tblCmpVolumeSnap.setVolumeId(volumeSnapDetailInfoRsp.getVolumeId());
                tblCmpVolumeSnap.setCreateTime(volumeSnapDetailInfoRsp.getCreateTime());
                tblCmpVolumeSnap.setUpdateTime(volumeSnapDetailInfoRsp.getUpdateTime());
                tblCmpVolumeSnap.setPhaseStatus(volumeSnapDetailInfoRsp.getPhaseStatus());
                tblCmpVolumeSnap.setDescription(volumeSnapDetailInfoRsp.getDescription());
                tblCmpVolumeSnap.setName(volumeSnapDetailInfoRsp.getName());
                tblCmpVolumeSnap.setIsCurrent(volumeSnapDetailInfoRsp.getIsCurrent());
                tblCmpVolumeSnap.setParentId(volumeSnapDetailInfoRsp.getParentId());

                try
                {
                    repoRepository.insertVolumeSnap(tblCmpVolumeSnap);
                } catch (DuplicateKeyException e)
                {
                    repoRepository.updateVolumeSnap(tblCmpVolumeSnap);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume snap {} error msg: {}", cloudId, volumeSnapId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncHypervisorNodes(String cloudId)
    {
        String urlPattern = "/api/vm/v1/hypervisor_nodes?page_num=%s&page_size=100";
        try
        {
            Set<String> nodeIds = vmComputeRepository.getNodeIds(cloudId);
            Set<String> nodeIdSet = new HashSet<>();
            if (nodeIds == null)
            {
                nodeIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    HypervisorNodesRsp getHypervisorNodesRsp = gson.fromJson(result, HypervisorNodesRsp.class);
                    if (getHypervisorNodesRsp == null || CollectionUtils.isEmpty(getHypervisorNodesRsp.getHypervisorNodes()))
                    {
                        NodeAllocationInfosRsp nodeAllocationInfosRsp = gson.fromJson(result, NodeAllocationInfosRsp.class);
                        if (nodeAllocationInfosRsp == null || CollectionUtils.isEmpty(nodeAllocationInfosRsp.getNodeAllocationInfos()))
                        {
                            return;
                        }

                        for (HypervisorNodeAllocationInfo nodeAllocationInfo : nodeAllocationInfosRsp.getNodeAllocationInfos())
                        {
                            TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                            tblCmpHypervisorNode.setCloudId(cloudId);
                            tblCmpHypervisorNode.setNodeId(nodeAllocationInfo.getNodeId());
                            tblCmpHypervisorNode.setInstanceId(null);
                            tblCmpHypervisorNode.setName(nodeAllocationInfo.getName());
                            tblCmpHypervisorNode.setPhaseStatus(nodeAllocationInfo.getNodePhaseStatus());
                            tblCmpHypervisorNode.setManageIp(nodeAllocationInfo.getManageIp());
                            tblCmpHypervisorNode.setHostName(null);
                            tblCmpHypervisorNode.setSysUsername(null);
                            tblCmpHypervisorNode.setSysPassword(null);
                            tblCmpHypervisorNode.setPubkeyId(null);
                            tblCmpHypervisorNode.setCpuModel(nodeAllocationInfo.getCpuModel());
                            tblCmpHypervisorNode.setCpuLogCount(nodeAllocationInfo.getCpuLogCount());
                            tblCmpHypervisorNode.setMemTotal(nodeAllocationInfo.getMemTotal());
                            tblCmpHypervisorNode.setDescription(null);
                            tblCmpHypervisorNode.setCreateTime(nodeAllocationInfo.getCreateTime());
                            tblCmpHypervisorNode.setEeStatus(SYNCING);
                            tblCmpHypervisorNode.setErrorCount(nodeAllocationInfo.getErrorCount());
                            tblCmpHypervisorNode.setIbCount(nodeAllocationInfo.getIbTotal());


                            try
                            {
                                vmComputeRepository.insertHypervisorNode(tblCmpHypervisorNode);
                            } catch (DuplicateKeyException e)
                            {
                                vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                            }

                            nodeIds.remove(nodeAllocationInfo.getNodeId());
                            nodeIdSet.add(nodeAllocationInfo.getNodeId());
                        }

                        return;
                    }

                    for (HypervisorNodesRsp.HypervisorNodeRsp hypervisorNode : getHypervisorNodesRsp.getHypervisorNodes())
                    {
                        TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                        tblCmpHypervisorNode.setCloudId(cloudId);
                        tblCmpHypervisorNode.setNodeId(hypervisorNode.getNodeId());
                        tblCmpHypervisorNode.setInstanceId(null);
                        tblCmpHypervisorNode.setName(hypervisorNode.getName());
                        tblCmpHypervisorNode.setPhaseStatus(hypervisorNode.getPhaseStatus());
                        tblCmpHypervisorNode.setManageIp(hypervisorNode.getManageIp());
                        tblCmpHypervisorNode.setHostName(hypervisorNode.getHostname());
                        tblCmpHypervisorNode.setSysUsername(null);
                        tblCmpHypervisorNode.setSysPassword(null);
                        tblCmpHypervisorNode.setPubkeyId(null);
                        tblCmpHypervisorNode.setCpuModel(null);
                        tblCmpHypervisorNode.setCpuLogCount(hypervisorNode.getCpuLogCount());
                        tblCmpHypervisorNode.setMemTotal(hypervisorNode.getMemTotal());
                        tblCmpHypervisorNode.setDescription(hypervisorNode.getDescription());
                        tblCmpHypervisorNode.setCreateTime(hypervisorNode.getCreateTime() == null ? null : Utils.parseStTime(hypervisorNode.getCreateTime()));
                        tblCmpHypervisorNode.setUpdateTime(hypervisorNode.getUpdateTime() == null ? null : Utils.parseStTime(hypervisorNode.getUpdateTime()));
                        tblCmpHypervisorNode.setEeStatus(SYNCING);

                        try
                        {
                            vmComputeRepository.insertHypervisorNode(tblCmpHypervisorNode);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                        }

                        nodeIds.remove(hypervisorNode.getNodeId());
                        nodeIdSet.add(hypervisorNode.getNodeId());
                    }
                    total = getHypervisorNodesRsp.getTotalNum();
                }
            }

            if (nodeIds.size() > 0)
            {
                for (String nodeId : nodeIds)
                {
                    TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                    tblCmpHypervisorNode.setCloudId(cloudId);
                    tblCmpHypervisorNode.setNodeId(nodeId);
                    tblCmpHypervisorNode.setEeStatus(REMOVED);

                    vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                }
            }

            if (nodeIdSet.size() > 0)
            {
                for (String nodeId : nodeIdSet)
                {
                    syncSingleData(cloudId, nodeId, SyncMsg.NS_HYPERVISOR_NODE);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} hypervisor nodes error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncHypervisorNode(String cloudId, String nodeId)
    {
        String url = "/api/vm/v1/hypervisor_nodes/" + nodeId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("hypervisor node does not exists"))
                {
                    TblCmpHypervisorNode tblCmpHypervisorNode = vmComputeRepository.getHypervisorNodeById(cloudId, nodeId);
                    if (tblCmpHypervisorNode != null)
                    {
                        tblCmpHypervisorNode.setEeStatus(REMOVED);
                        vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                    }
                    return;
                }

                HypervisorNodeAllocationInfo getHypervisorNodeDetailInfoRsp = gson.fromJson(result, HypervisorNodeAllocationInfo.class);
                if (getHypervisorNodeDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                tblCmpHypervisorNode.setCloudId(cloudId);
                tblCmpHypervisorNode.setNodeId(nodeId);
                tblCmpHypervisorNode.setInstanceId(null);
                tblCmpHypervisorNode.setName(getHypervisorNodeDetailInfoRsp.getName());
                tblCmpHypervisorNode.setPhaseStatus(getHypervisorNodeDetailInfoRsp.getNodePhaseStatus());
                tblCmpHypervisorNode.setManageIp(getHypervisorNodeDetailInfoRsp.getManageIp());
                tblCmpHypervisorNode.setSysPassword(null);
                tblCmpHypervisorNode.setEeStatus(SYNCING);
                tblCmpHypervisorNode.setMemTotal(getHypervisorNodeDetailInfoRsp.getMemTotal());
                tblCmpHypervisorNode.setCpuLogCount(getHypervisorNodeDetailInfoRsp.getCpuLogCount());
                tblCmpHypervisorNode.setCpuModel(getHypervisorNodeDetailInfoRsp.getCpuModel());
                tblCmpHypervisorNode.setAvailableIbCount(getHypervisorNodeDetailInfoRsp.getAvailableIbCount() == null ? 0 : getHypervisorNodeDetailInfoRsp.getAvailableIbCount());
                tblCmpHypervisorNode.setIbCount(getHypervisorNodeDetailInfoRsp.getIbTotal());

                try
                {
                    vmComputeRepository.insertHypervisorNode(tblCmpHypervisorNode);
                }
                catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} hypervisor node {} error msg: {}", cloudId, nodeId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncInstanceGroups(String cloudId)
    {
        String urlPattern = "/api/vm/v1/instance-groups?page_num=%s&page_size=100";
        try
        {
            Set<String> groupIds = vmComputeRepository.getInstanceGroupIds(cloudId);
            Set<String> groupIdSet = new HashSet<>();
            if (groupIds == null)
            {
                groupIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    InstanceGroupsBaseRsp instanceGroupsBaseRsp = gson.fromJson(result, InstanceGroupsBaseRsp.class);
                    if (instanceGroupsBaseRsp == null || CollectionUtils.isEmpty(instanceGroupsBaseRsp.getInstanceGroupInfos()))
                    {
                        return;
                    }

                    for (InstanceGroupsBaseRsp.InstanceGroupInfo instanceGroupInfo : instanceGroupsBaseRsp.getInstanceGroupInfos())
                    {
                        TblCmpInstanceGroup tblCmpInstanceGroup = new TblCmpInstanceGroup();
                        tblCmpInstanceGroup.setCloudId(cloudId);
                        tblCmpInstanceGroup.setInstanceGroupId(instanceGroupInfo.getInstanceGroupId());
                        tblCmpInstanceGroup.setName(instanceGroupInfo.getName());
                        tblCmpInstanceGroup.setDescription(instanceGroupInfo.getDescription());
                        tblCmpInstanceGroup.setCreateTime(instanceGroupInfo.getCreateTime());
                        tblCmpInstanceGroup.setEeStatus(SYNCING);

                        try
                        {
                            vmComputeRepository.insertInstanceGroup(tblCmpInstanceGroup);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateInstanceGroup(tblCmpInstanceGroup);
                        }

                        groupIds.remove(tblCmpInstanceGroup.getInstanceGroupId());
                        groupIdSet.add(tblCmpInstanceGroup.getInstanceGroupId());
                    }
                    total = instanceGroupsBaseRsp.getTotalNum();
                }
            }

            if (groupIds.size() > 0)
            {
                for (String groupId : groupIds)
                {
                    TblCmpInstanceGroup tblCmpInstanceGroup = new TblCmpInstanceGroup();
                    tblCmpInstanceGroup.setCloudId(cloudId);
                    tblCmpInstanceGroup.setInstanceGroupId(groupId);
                    tblCmpInstanceGroup.setEeStatus(REMOVED);

                    vmComputeRepository.updateInstanceGroup(tblCmpInstanceGroup);
                }
            }

            if (groupIdSet.size() > 0)
            {
                for (String groupId : groupIdSet)
                {
                    syncSingleData(cloudId, groupId, SyncMsg.NS_INSTANCE_GROUP);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} instance groups error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncInstanceGroup(String cloudId, String groupId)
    {
        String url = "/api/vm/v1/instance-groups/" + groupId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("instance group does not exist"))
                {
                    TblCmpInstanceGroup tblCmpInstanceGroup = vmComputeRepository.getInstanceGroupById(cloudId, groupId);
                    if (tblCmpInstanceGroup != null)
                    {
                        tblCmpInstanceGroup.setEeStatus(REMOVED);
                        vmComputeRepository.updateInstanceGroup(tblCmpInstanceGroup);
                    }
                    return;
                }

                InstanceGroup instanceGroup = gson.fromJson(result, InstanceGroup.class);
                if (instanceGroup == null)
                {
                    return;
                }

                TblCmpInstanceGroup tblCmpInstanceGroup = new TblCmpInstanceGroup();
                tblCmpInstanceGroup.setCloudId(cloudId);
                tblCmpInstanceGroup.setInstanceGroupId(instanceGroup.getInstanceGroupId());
                tblCmpInstanceGroup.setName(instanceGroup.getName());
                tblCmpInstanceGroup.setDescription(instanceGroup.getDescription());
                tblCmpInstanceGroup.setPhaseStatus(instanceGroup.getPhaseStatus());
                tblCmpInstanceGroup.setCreateTime(instanceGroup.getCreateTime());
                tblCmpInstanceGroup.setUpdateTime(instanceGroup.getUpdateTime());
                tblCmpInstanceGroup.setEeStatus(SYNCING);

                try
                {
                    vmComputeRepository.insertInstanceGroup(tblCmpInstanceGroup);
                } catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateInstanceGroup(tblCmpInstanceGroup);
                }

                syncInstanceGroupInstance(cloudId, groupId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} instance group {} error msg: {}", cloudId, groupId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncInstanceGroupInstance(String cloudId, String groupId)
    {
        String urlPattern = "/api/vm/v1/instances?instance_group_id=%s";
        try
        {
            TblCmpVmInstanceExample example = new TblCmpVmInstanceExample();
            TblCmpVmInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andInstanceGroupIdEqualTo(groupId);
            List<TblCmpVmInstance> tblCmpVmInstances = vmComputeRepository.getVmInstances(example);

            Set<String> instanceIds = new HashSet<>();

            if (! CollectionUtils.isEmpty(tblCmpVmInstances))
            {
                for (TblCmpVmInstance tblCmpVmInstance : tblCmpVmInstances)
                {
                    instanceIds.add(tblCmpVmInstance.getVmInstanceId());
                }
            }

            String result = getDataFromCloud(cloudId, String.format(urlPattern, groupId));
            if (StringUtils.isNotEmpty(result))
            {
                VmInstancesRsp getVmInstancesRsp = gson.fromJson(result, VmInstancesRsp.class);
                if (getVmInstancesRsp != null && !CollectionUtils.isEmpty(getVmInstancesRsp.getVmInstancesInfo()))
                {
                    for (VmInstanceInfo vmInstanceInfo : getVmInstancesRsp.getVmInstancesInfo())
                    {
                        TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                        tblCmpVmInstance.setCloudId(cloudId);
                        tblCmpVmInstance.setVmInstanceId(vmInstanceInfo.getInstanceId());
                        tblCmpVmInstance.setInstanceIdFromAgent(null);
                        tblCmpVmInstance.setName(vmInstanceInfo.getName());
                        tblCmpVmInstance.setPhaseStatus(vmInstanceInfo.getPhaseStatus());
                        tblCmpVmInstance.setUserId(null);
                        tblCmpVmInstance.setNodeId(null);
                        tblCmpVmInstance.setFlavorId(null);
                        tblCmpVmInstance.setImageId(vmInstanceInfo.getImageInfo() == null ? null : vmInstanceInfo.getImageInfo().getImageId());
                        tblCmpVmInstance.setVpcId(vmInstanceInfo.getVpcInfo() == null ? null : vmInstanceInfo.getVpcInfo().getVpcId());
                        tblCmpVmInstance.setSubnetId(vmInstanceInfo.getSubnetInfo() == null ? null : vmInstanceInfo.getSubnetInfo().getSubnetId());
                        tblCmpVmInstance.setPortId(vmInstanceInfo.getPortInfo() == null ? null : vmInstanceInfo.getPortInfo().getPortId());
                        tblCmpVmInstance.setStaticIp(null);
                        tblCmpVmInstance.setHostName(vmInstanceInfo.getHostname());
                        tblCmpVmInstance.setSysUsername(null);
                        tblCmpVmInstance.setSysPassword(null);
                        tblCmpVmInstance.setPubkeyId(null);
                        tblCmpVmInstance.setDescription(vmInstanceInfo.getDescription());
                        tblCmpVmInstance.setCreateTime(vmInstanceInfo.getCreateTime() == null ? null : Utils.parseStTime(vmInstanceInfo.getCreateTime()));
                        tblCmpVmInstance.setUpdateTime(vmInstanceInfo.getUpdateTime() == null ? null : Utils.parseStTime(vmInstanceInfo.getUpdateTime()));
                        tblCmpVmInstance.setLastNodeId(null);
                        tblCmpVmInstance.setDestNodeId(null);
                        tblCmpVmInstance.setVolumeId(vmInstanceInfo.getVolumeId());
                        tblCmpVmInstance.setStoragePoolId(null);
                        tblCmpVmInstance.setEeStatus(SYNCING);
                        tblCmpVmInstance.setEipId(vmInstanceInfo.getEipId());
                        tblCmpVmInstance.setInstanceGroupId(groupId);

                        try
                        {
                            vmComputeRepository.insertVmInstance(tblCmpVmInstance);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                        }

                        instanceIds.remove(vmInstanceInfo.getInstanceId());
                    }
                }
            }

            if (instanceIds.size() > 0)
            {
                for (String instanceId : instanceIds)
                {
                    TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, instanceId);
                    tblCmpVmInstance.setInstanceGroupId(null);

                    vmComputeRepository.updateVmInstanceByPrimaryKey(tblCmpVmInstance);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} instacncegroup {} instances error msg: {}", cloudId, groupId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPciDevices(String cloudId)
    {
        Set<String> nodeIds = vmComputeRepository.getNodeIds(cloudId);
        String urlPattern = "/api/vm/v1/pci_devices?node_id=%s&page_num=%s&page_size=100";
        try
        {
            Set<String> pciDeviceIds = vmComputeRepository.getPciDeviceIds(cloudId);
            if (pciDeviceIds == null)
            {
                pciDeviceIds = new HashSet<>();
            }

            for (String nodeId : nodeIds)
            {
                int pageNum = 0;
                int total = 100;
                while (total == 100)
                {
                    pageNum = pageNum + 1;
                    String result = getDataFromCloud(cloudId, String.format(urlPattern, nodeId, pageNum));
                    if (StringUtils.isNotEmpty(result))
                    {
                        List<PciDeviceDetailInfo> pciDeviceDetailInfos = gson.fromJson(result, new TypeToken<List<PciDeviceDetailInfo>>(){}.getType());
                        if (pciDeviceDetailInfos == null || CollectionUtils.isEmpty(pciDeviceDetailInfos))
                        {
                            break;
                        }

                        for (PciDeviceDetailInfo pciDeviceDetailInfo : pciDeviceDetailInfos)
                        {
                            TblCmpPciDevice tblCmpPciDevice = new TblCmpPciDevice();
                            tblCmpPciDevice.setCloudId(cloudId);
                            tblCmpPciDevice.setEeStatus(SYNCING);
                            tblCmpPciDevice.setDeviceId(pciDeviceDetailInfo.getDeviceId());
                            tblCmpPciDevice.setType(pciDeviceDetailInfo.getPciDeviceType());
                            tblCmpPciDevice.setName(pciDeviceDetailInfo.getPciDeviceName());
                            tblCmpPciDevice.setPhaseStatus(pciDeviceDetailInfo.getPhaseStatus());
                            tblCmpPciDevice.setUserId(pciDeviceDetailInfo.getUserId());
                            tblCmpPciDevice.setCreateTime(pciDeviceDetailInfo.getCreateTime());
                            tblCmpPciDevice.setUpdateTime(pciDeviceDetailInfo.getUpdateTime());
                            tblCmpPciDevice.setDeviceIdFromAgent(null);
                            tblCmpPciDevice.setVersion(null);
                            tblCmpPciDevice.setVmInstanceId(pciDeviceDetailInfo.getVmInstanceId());
                            tblCmpPciDevice.setNodeId(pciDeviceDetailInfo.getNodeId());

                            try
                            {
                                vmComputeRepository.insertPciDevice(tblCmpPciDevice);
                            }
                            catch (DuplicateKeyException e)
                            {
                                vmComputeRepository.updatePciDevice(tblCmpPciDevice);
                            }

                            if(tblCmpPciDevice.getVmInstanceId() == null)
                            {
                                TblCmpPciDevice updateTblCmpPciDevice = vmComputeRepository.getPciDeviceById(cloudId, pciDeviceDetailInfo.getDeviceId());
                                if (updateTblCmpPciDevice != null && updateTblCmpPciDevice.getVmInstanceId() != null)
                                {
                                    updateTblCmpPciDevice.setVmInstanceId(null);
                                    vmComputeRepository.updatePciDeviceForce(updateTblCmpPciDevice);
                                }
                            }

                            pciDeviceIds.remove(pciDeviceDetailInfo.getDeviceId());

                        }
                        total = pciDeviceDetailInfos.size();
                    }
                }
            }

            if (pciDeviceIds.size() > 0)
            {
                for (String pciDeviceId : pciDeviceIds)
                {
                    TblCmpPciDevice tblCmpPciDevice = new TblCmpPciDevice();
                    tblCmpPciDevice.setCloudId(cloudId);
                    tblCmpPciDevice.setDeviceId(pciDeviceId);
                    tblCmpPciDevice.setEeStatus(REMOVED);

                    vmComputeRepository.updatePciDevice(tblCmpPciDevice);
                }
            }

        }
        catch (Exception e)
        {
            LOGGER.error("sync {} pci devices error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPciDevice(String cloudId, String deviceId)
    {
        String url = "/api/vm/v1/pci_devices/" + deviceId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("pci device does not exists"))
                {
                    TblCmpPciDevice tblCmpPciDevice = vmComputeRepository.getPciDeviceById(cloudId, deviceId);
                    if (tblCmpPciDevice != null)
                    {
                        tblCmpPciDevice.setEeStatus(REMOVED);
                        vmComputeRepository.updatePciDevice(tblCmpPciDevice);
                    }
                    return;
                }

                PciDeviceDetailInfo pciDeviceDetailInfo = gson.fromJson(result, PciDeviceDetailInfo.class);
                if (pciDeviceDetailInfo == null)
                {
                    return;
                }

                TblCmpPciDevice tblCmpPciDevice = new TblCmpPciDevice();
                tblCmpPciDevice.setCloudId(cloudId);
                tblCmpPciDevice.setEeStatus(SYNCING);
                tblCmpPciDevice.setDeviceId(pciDeviceDetailInfo.getDeviceId());
                tblCmpPciDevice.setType(pciDeviceDetailInfo.getPciDeviceType());
                tblCmpPciDevice.setName(pciDeviceDetailInfo.getPciDeviceName());
                tblCmpPciDevice.setPhaseStatus(pciDeviceDetailInfo.getPhaseStatus());
                tblCmpPciDevice.setUserId(pciDeviceDetailInfo.getUserId());
                tblCmpPciDevice.setCreateTime(pciDeviceDetailInfo.getCreateTime());
                tblCmpPciDevice.setUpdateTime(pciDeviceDetailInfo.getUpdateTime());
                tblCmpPciDevice.setDeviceIdFromAgent(null);
                tblCmpPciDevice.setVersion(null);
                tblCmpPciDevice.setVmInstanceId(pciDeviceDetailInfo.getVmInstanceId());
                tblCmpPciDevice.setNodeId(pciDeviceDetailInfo.getNodeId());

                try
                {
                    vmComputeRepository.insertPciDevice(tblCmpPciDevice);
                }
                catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updatePciDevice(tblCmpPciDevice);
                }

                if(tblCmpPciDevice.getVmInstanceId() == null)
                {
                    TblCmpPciDevice updateTblCmpPciDevice = vmComputeRepository.getPciDeviceById(cloudId, pciDeviceDetailInfo.getDeviceId());
                    if (updateTblCmpPciDevice != null && updateTblCmpPciDevice.getVmInstanceId() != null)
                    {
                        updateTblCmpPciDevice.setVmInstanceId(null);
                        vmComputeRepository.updatePciDeviceForce(updateTblCmpPciDevice);
                    }
                }
            }

        }
        catch (Exception e)
        {
            LOGGER.error("sync {} pci device {} error msg: {}", cloudId, deviceId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncResourceStats(String cloudId)
    {
        int days = 1;
        List<String> names = Lists.newArrayList("cpu", "mem", "vm", "nat", "storage");
        List<TblCmpResourceStats> tblCmpResourceStatsList = null;
        String urlPattern = "/api/vm/v1/resource_stats?name=%s&days=%s";
        try
        {

            for (String name : names)
            {
                String result = getDataFromCloud(cloudId, String.format(urlPattern, name, days));
                if (StringUtils.isNotEmpty(result))
                {
                    tblCmpResourceStatsList = gson.fromJson(result, new TypeToken<List<TblCmpResourceStats>>(){}.getType());
                    if (tblCmpResourceStatsList == null || CollectionUtils.isEmpty(tblCmpResourceStatsList))
                    {
                        break;
                    }

                    for (TblCmpResourceStats tblCmpResourceStats : tblCmpResourceStatsList)
                    {
                        tblCmpResourceStats.setCloudId(cloudId);
                        tblCmpResourceStats.setEeStatus(SYNCING);

                        try
                        {
                            vmComputeRepository.insertResourceStats(tblCmpResourceStats);
                        }
                        catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateResourceStats(tblCmpResourceStats);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} resource stats error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVmInstances(String cloudId)
    {
        String urlPattern = "/api/vm/v1/instances?page_num=%s&page_size=100";
        try
        {
            Set<String> instanceIds = vmComputeRepository.getVmInstanceIds(cloudId);
            Set<String> instanceIdSet = new HashSet<>();
            if (instanceIds == null)
            {
                instanceIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    VmInstancesRsp getVmInstancesRsp = gson.fromJson(result, VmInstancesRsp.class);
                    if (getVmInstancesRsp == null || CollectionUtils.isEmpty(getVmInstancesRsp.getVmInstancesInfo()))
                    {
                        return;
                    }

                    for (VmInstanceInfo vmInstanceInfo : getVmInstancesRsp.getVmInstancesInfo())
                    {
                        TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                        tblCmpVmInstance.setCloudId(cloudId);
                        tblCmpVmInstance.setVmInstanceId(vmInstanceInfo.getInstanceId());
                        tblCmpVmInstance.setInstanceIdFromAgent(null);
                        tblCmpVmInstance.setName(vmInstanceInfo.getName());
                        tblCmpVmInstance.setPhaseStatus(vmInstanceInfo.getPhaseStatus());
                        tblCmpVmInstance.setUserId(null);
                        tblCmpVmInstance.setNodeId(null);
                        tblCmpVmInstance.setFlavorId(null);
                        tblCmpVmInstance.setImageId(vmInstanceInfo.getImageInfo() == null ? null : vmInstanceInfo.getImageInfo().getImageId());
                        tblCmpVmInstance.setVpcId(vmInstanceInfo.getVpcInfo() == null ? null : vmInstanceInfo.getVpcInfo().getVpcId());
                        tblCmpVmInstance.setSubnetId(vmInstanceInfo.getSubnetInfo() == null ? null : vmInstanceInfo.getSubnetInfo().getSubnetId());
                        tblCmpVmInstance.setPortId(vmInstanceInfo.getPortInfo() == null ? null : vmInstanceInfo.getPortInfo().getPortId());
                        tblCmpVmInstance.setStaticIp(null);
                        tblCmpVmInstance.setHostName(vmInstanceInfo.getHostname());
                        tblCmpVmInstance.setSysUsername(null);
                        tblCmpVmInstance.setSysPassword(null);
                        tblCmpVmInstance.setPubkeyId(null);
                        tblCmpVmInstance.setDescription(vmInstanceInfo.getDescription());
                        tblCmpVmInstance.setCreateTime(vmInstanceInfo.getCreateTime() == null ? null : Utils.parseStTime(vmInstanceInfo.getCreateTime()));
                        tblCmpVmInstance.setUpdateTime(vmInstanceInfo.getUpdateTime() == null ? null : Utils.parseStTime(vmInstanceInfo.getUpdateTime()));
                        tblCmpVmInstance.setLastNodeId(null);
                        tblCmpVmInstance.setDestNodeId(null);
                        tblCmpVmInstance.setVolumeId(vmInstanceInfo.getVolumeId());
                        tblCmpVmInstance.setStoragePoolId(null);
                        tblCmpVmInstance.setEeStatus(SYNCING);
                        tblCmpVmInstance.setEipId(vmInstanceInfo.getEipId());

                        try
                        {
                            vmComputeRepository.insertVmInstance(tblCmpVmInstance);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                        }

                        if (tblCmpVmInstance.getEipId() == null)
                        {
                            TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceInfo.getInstanceId());
                            if (tempTblCmpVmInstance != null && tempTblCmpVmInstance.getEipId() != null)
                            {
                                tempTblCmpVmInstance.setEipId(null);
                                vmComputeRepository.updateVmInstanceByPrimaryKey(tempTblCmpVmInstance);
                            }
                        }

                        instanceIds.remove(vmInstanceInfo.getInstanceId());
                        instanceIdSet.add(vmInstanceInfo.getInstanceId());

                        if (vmInstanceInfo.getPortInfo() != null && vmInstanceInfo.getPortInfo().getPortId() != null)
                        {
                            TblCmpPort tblCmpPort = new TblCmpPort();
                            tblCmpPort.setCloudId(cloudId);
                            tblCmpPort.setPortId(vmInstanceInfo.getPortInfo().getPortId());
                            tblCmpPort.setIpAddress(vmInstanceInfo.getPortInfo().getIpAddress());
                            tblCmpPort.setEeStatus(SYNCING);
                            if (AgentConstant.PORT_BOUND.equals(vmInstanceInfo.getBoundType()))
                            {
                                tblCmpPort.setEipId(vmInstanceInfo.getEipId());
                            }
                            tblCmpPort.setEipPhaseStatus(vmInstanceInfo.getBoundPhaseStatus());
                            tblCmpPort.setInstanceId(vmInstanceInfo.getInstanceId());
                            if (tblCmpPort.getEipId() == null) tblCmpPort.setEipPhaseStatus(PhaseStatus.DETACH_EIP_DONE);
                            try
                            {
                                networkRepository.createPort(tblCmpPort);
                            } catch (DuplicateKeyException e)
                            {
                                networkRepository.updatePort(tblCmpPort);
                            }
                        }

                        if (tblCmpVmInstance.getVolumeId() != null)
                        {
                            TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceInfo.getInstanceId());
                            if (tempTblCmpVmInstance != null && (tempTblCmpVmInstance.getEeUser() !=null || tempTblCmpVmInstance.getEeBp() != null))
                            {
                                TblCmpVolume tblCmpVolume = new TblCmpVolume();
                                tblCmpVolume.setCloudId(cloudId);
                                tblCmpVolume.setEeStatus(SYNCING);
                                tblCmpVolume.setVolumeId(vmInstanceInfo.getVolumeId());
                                tblCmpVolume.setVmId(vmInstanceInfo.getInstanceId());
                                tblCmpVolume.setEeUser(tempTblCmpVmInstance.getEeUser());
                                tblCmpVolume.setEeBp(tempTblCmpVmInstance.getEeBp());

                                try
                                {
                                    repoRepository.insertVolume(tblCmpVolume);
                                } catch (DuplicateKeyException e)
                                {
                                    repoRepository.updateVolume(tblCmpVolume);
                                }
                            }
                            syncSingleData(cloudId, vmInstanceInfo.getVolumeId(), SyncMsg.NS_VOLUME);
                        }
                    }
                    total = getVmInstancesRsp.getTotalNum();
                }
            }

            if (instanceIds.size() > 0)
            {
                for (String instanceId : instanceIds)
                {
                    TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                    tblCmpVmInstance.setCloudId(cloudId);
                    tblCmpVmInstance.setVmInstanceId(instanceId);
                    tblCmpVmInstance.setEeStatus(REMOVED);

                    vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                }
            }

            if (instanceIdSet.size() > 0)
            {
                for (String vmInstanceId : instanceIdSet)
                {
                    syncSingleData(cloudId, vmInstanceId, SyncMsg.NS_VM_INSTANCE);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} vm instances error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVmInstance(String cloudId, String vmInstanceId)
    {
        String url = "/api/vm/v1/instances/" + vmInstanceId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("vm instance does not exists"))
                {
                    TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
                    if (tblCmpVmInstance != null && tblCmpVmInstance.getEeStatus() == SYNCING)
                    {
                        tblCmpVmInstance.setEeStatus(REMOVED);
                        vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                    }
                    return;
                }

                VmInstanceDetailInfoRsp getVmInstanceDetailInfoRsp = gson.fromJson(result, VmInstanceDetailInfoRsp.class);
                if (getVmInstanceDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpVmInstance tblCmpVmInstance = new TblCmpVmInstance();
                tblCmpVmInstance.setCloudId(cloudId);
                tblCmpVmInstance.setVmInstanceId(getVmInstanceDetailInfoRsp.getInstanceId());
                tblCmpVmInstance.setInstanceIdFromAgent(null);
                tblCmpVmInstance.setName(getVmInstanceDetailInfoRsp.getName());
                tblCmpVmInstance.setPhaseStatus(getVmInstanceDetailInfoRsp.getPhaseStatus());
                tblCmpVmInstance.setUserId(getVmInstanceDetailInfoRsp.getUserId());
                tblCmpVmInstance.setNodeId(getVmInstanceDetailInfoRsp.getHypervisorNodeId());
                tblCmpVmInstance.setFlavorId(getVmInstanceDetailInfoRsp.getFlavorId());
                tblCmpVmInstance.setImageId(getVmInstanceDetailInfoRsp.getImageId());
                tblCmpVmInstance.setVpcId(getVmInstanceDetailInfoRsp.getVpcId());
                tblCmpVmInstance.setSubnetId(getVmInstanceDetailInfoRsp.getSubnetId());
                tblCmpVmInstance.setPortId(null);
                tblCmpVmInstance.setStaticIp(null);
                tblCmpVmInstance.setHostName(getVmInstanceDetailInfoRsp.getHostname());
                tblCmpVmInstance.setSysUsername(getVmInstanceDetailInfoRsp.getSysUsername());
                tblCmpVmInstance.setSysPassword(null);
                tblCmpVmInstance.setPubkeyId(getVmInstanceDetailInfoRsp.getPubkeyId());
                tblCmpVmInstance.setDescription(getVmInstanceDetailInfoRsp.getDescription());
                tblCmpVmInstance.setCreateTime(getVmInstanceDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(getVmInstanceDetailInfoRsp.getCreateTime()));
                tblCmpVmInstance.setUpdateTime(getVmInstanceDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(getVmInstanceDetailInfoRsp.getUpdateTime()));
                tblCmpVmInstance.setLastNodeId(null);
                tblCmpVmInstance.setDestNodeId(null);
                tblCmpVmInstance.setVolumeId(getVmInstanceDetailInfoRsp.getVolumeId());
                tblCmpVmInstance.setBootDev(getVmInstanceDetailInfoRsp.getBootDev());
                tblCmpVmInstance.setStoragePoolId(null);
                tblCmpVmInstance.setEeStatus(SYNCING);
                tblCmpVmInstance.setRootDisk(getVmInstanceDetailInfoRsp.getRootDisk());

                try
                {
                    vmComputeRepository.insertVmInstance(tblCmpVmInstance);
                } catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateVmInstanceByPrimaryKeySelective(tblCmpVmInstance);
                }

                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getNetworkDetailInfos()))
                {
                    TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
                    if (tempTblCmpVmInstance != null)
                    {
                        boolean update = false;
                        if (tblCmpVmInstance.getVpcId() == null && getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getVpcId() != null)
                        {
                            tempTblCmpVmInstance.setVpcId(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getVpcId());
                            update = true;
                        }
                        if (tblCmpVmInstance.getSubnetId() == null && getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getSubnetId() != null)
                        {
                            tempTblCmpVmInstance.setSubnetId(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getSubnetId());
                            update = true;
                        }
                        if (tblCmpVmInstance.getPortId() == null && getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getPortId() != null)
                        {
                            tempTblCmpVmInstance.setPortId(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getPortId());
                            update = true;
                        }
                        if (tempTblCmpVmInstance.getEipId() != null)
                        {
                            if (!tempTblCmpVmInstance.getEipId().equals(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getEipId()))
                            {
                                tempTblCmpVmInstance.setEipId(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getEipId());
                                update = true;
                            }
                        }
                        else
                        {
                            if (getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getEipId() != null)
                            {
                                tempTblCmpVmInstance.setEipId(getVmInstanceDetailInfoRsp.getNetworkDetailInfos().get(0).getEipId());
                                update = true;
                            }
                        }
                        if (update)
                        {
                            vmComputeRepository.updateVmInstanceByPrimaryKey(tempTblCmpVmInstance);
                        }
                    }

                    syncInstanceNetworkRef(cloudId, vmInstanceId, getVmInstanceDetailInfoRsp.getNetworkDetailInfos().subList(1, getVmInstanceDetailInfoRsp.getNetworkDetailInfos().size()));
                }

                if (StringUtils.isNotEmpty(tblCmpVmInstance.getFlavorId()))
                {
                    TblCmpFlavor tblCmpFlavor = new TblCmpFlavor();
                    tblCmpFlavor.setFlavorId(getVmInstanceDetailInfoRsp.getFlavorId());
                    tblCmpFlavor.setName(getVmInstanceDetailInfoRsp.getFlavorName());
                    tblCmpFlavor.setCpu(getVmInstanceDetailInfoRsp.getCpu());
                    tblCmpFlavor.setMem(getVmInstanceDetailInfoRsp.getMem());
                    tblCmpFlavor.setCloudId(cloudId);
                    tblCmpFlavor.setEeStatus(SYNCING);

                    try
                    {
                        flavorRepository.insertFlavor(tblCmpFlavor);
                    } catch (DuplicateKeyException e)
                    {
                        flavorRepository.updateFlavor(tblCmpFlavor);
                    }
                }

                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getNetworkDetailInfos()))
                {
                    syncVmInstanceNetworks(cloudId, vmInstanceId, getVmInstanceDetailInfoRsp.getNetworkDetailInfos());
                }

                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getSgInfos()))
                {
                    TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
                    if (tempTblCmpVmInstance != null)
                    {
                        syncSgVmInstance(cloudId, vmInstanceId, tempTblCmpVmInstance.getEeBp(), tempTblCmpVmInstance.getEeUser(), getVmInstanceDetailInfoRsp.getSgInfos());
                    }
                }

                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getSnapInfos()))
                {
                    for (VmInstanceDetailInfoRsp.SnapInfo snapInfo : getVmInstanceDetailInfoRsp.getSnapInfos())
                    {
                        TblCmpVmSnap tblCmpVmSnap = new TblCmpVmSnap();
                        tblCmpVmSnap.setCloudId(cloudId);
                        tblCmpVmSnap.setEeStatus(SYNCING);
                        tblCmpVmSnap.setSnapId(snapInfo.getSnapId());
                        tblCmpVmSnap.setVmInstanceId(vmInstanceId);
                        tblCmpVmSnap.setName(snapInfo.getSnapName());
                        tblCmpVmSnap.setUserId(null);
                        tblCmpVmSnap.setSnapIdFromAgent(null);
                        tblCmpVmSnap.setPhaseStatus(snapInfo.getPhaseStatus());
                        tblCmpVmSnap.setDescription(null);
                        tblCmpVmSnap.setIsCurrent(snapInfo.getIsCurrent());

                        try
                        {
                            vmComputeRepository.insertVmSnap(tblCmpVmSnap);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateVmSnap(tblCmpVmSnap);
                        }
                    }
                }

                Set<String> volumeIds = getVolumeInfosByVmId(cloudId, vmInstanceId);

                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getDiskInfos()))
                {
                    for (RpcVolumeInfo rpcVolumeInfo : getVmInstanceDetailInfoRsp.getDiskInfos())
                    {
                        TblCmpVolume tblCmpVolume = new TblCmpVolume();
                        tblCmpVolume.setCloudId(cloudId);
                        tblCmpVolume.setEeStatus(SYNCING);
                        tblCmpVolume.setVolumeId(rpcVolumeInfo.getVolumeId());
                        tblCmpVolume.setName(rpcVolumeInfo.getVolumeName());
                        tblCmpVolume.setSize(rpcVolumeInfo.getSize());
                        tblCmpVolume.setPhaseStatus(rpcVolumeInfo.getPhaseStatus());
                        tblCmpVolume.setType(rpcVolumeInfo.getType());
                        tblCmpVolume.setVmId(vmInstanceId);

                        TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstanceId);
                        if (tempTblCmpVmInstance != null)
                        {
                            tblCmpVolume.setEeBp(tempTblCmpVmInstance.getEeBp());
                            tblCmpVolume.setEeUser(tempTblCmpVmInstance.getEeUser());
                        }

                        try
                        {
                            repoRepository.insertVolume(tblCmpVolume);
                        } catch (DuplicateKeyException e)
                        {
                            repoRepository.updateVolume(tblCmpVolume);
                        }

                        syncSingleData(cloudId, rpcVolumeInfo.getVolumeId(), SyncMsg.NS_VOLUME);
                        volumeIds.remove(rpcVolumeInfo.getVolumeId());
                    }
                }

                if (tblCmpVmInstance.getVolumeId() != null)
                {
                    TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, getVmInstanceDetailInfoRsp.getInstanceId());
                    if (tempTblCmpVmInstance != null && (tempTblCmpVmInstance.getEeUser() !=null || tempTblCmpVmInstance.getEeBp() != null))
                    {
                        TblCmpVolume tblCmpVolume = new TblCmpVolume();
                        tblCmpVolume.setCloudId(cloudId);
                        tblCmpVolume.setEeStatus(SYNCING);
                        tblCmpVolume.setVolumeId(getVmInstanceDetailInfoRsp.getVolumeId());
                        tblCmpVolume.setVmId(getVmInstanceDetailInfoRsp.getInstanceId());
                        tblCmpVolume.setEeUser(tempTblCmpVmInstance.getEeUser());
                        tblCmpVolume.setEeBp(tempTblCmpVmInstance.getEeBp());

                        try
                        {
                            repoRepository.insertVolume(tblCmpVolume);
                        } catch (DuplicateKeyException e)
                        {
                            repoRepository.updateVolume(tblCmpVolume);
                        }

                        volumeIds.remove(getVmInstanceDetailInfoRsp.getVolumeId());
                    }
                    syncSingleData(cloudId, getVmInstanceDetailInfoRsp.getVolumeId(), SyncMsg.NS_VOLUME);
                }

                if (!CollectionUtils.isEmpty(volumeIds))
                {
                    for (String volumeId : volumeIds)
                    {
                        try
                        {
                            TblCmpVolume tblCmpVolume = repoRepository.getVolumeById(cloudId, volumeId);
                            tblCmpVolume.setVmId(null);
                            repoRepository.updateVolumeForce(tblCmpVolume);

                            syncSingleData(cloudId, volumeId, SyncMsg.NS_VOLUME);
                        }
                        catch (Exception e)
                        {
                            LOGGER.error("update {} volume {} error msg: {}", cloudId, vmInstanceId, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }

                TblCmpPciDeviceExample pciDeviceExample = new TblCmpPciDeviceExample();
                TblCmpPciDeviceExample.Criteria pciDeviceCriteria = pciDeviceExample.createCriteria();
                pciDeviceCriteria.andVmInstanceIdEqualTo(vmInstanceId);
                pciDeviceCriteria.andCloudIdEqualTo(cloudId);
                pciDeviceCriteria.andEeStatusNotEqualTo(REMOVED);
                List<String> pciDeviceIds = vmComputeRepository.getPciDevices(pciDeviceExample).stream().map(TblCmpPciDevice::getDeviceId).collect(Collectors.toList());


                if (! CollectionUtils.isEmpty(getVmInstanceDetailInfoRsp.getPciInfos()))
                {
                    for (PciDeviceInfo pciDeviceInfo : getVmInstanceDetailInfoRsp.getPciInfos())
                    {
                        TblCmpPciDevice tblCmpPciDevice = new TblCmpPciDevice();
                        tblCmpPciDevice.setCloudId(cloudId);
                        tblCmpPciDevice.setEeStatus(SYNCING);
                        tblCmpPciDevice.setDeviceId(pciDeviceInfo.getDeviceId());
                        tblCmpPciDevice.setType(pciDeviceInfo.getPciDeviceType());
                        tblCmpPciDevice.setName(pciDeviceInfo.getPciDeviceName());
                        tblCmpPciDevice.setPhaseStatus(pciDeviceInfo.getPhaseStatus());
                        tblCmpPciDevice.setUserId(pciDeviceInfo.getUserId());
                        tblCmpPciDevice.setVmInstanceId(vmInstanceId);
                        tblCmpPciDevice.setCreateTime(pciDeviceInfo.getCreateTime() == null ? null : Utils.parseStTime(pciDeviceInfo.getCreateTime().substring(0, 19)));
                        tblCmpPciDevice.setUpdateTime(pciDeviceInfo.getUpdateTime() == null ? null : Utils.parseStTime(pciDeviceInfo.getUpdateTime().substring(0, 19)));

                        try
                        {
                            vmComputeRepository.insertPciDevice(tblCmpPciDevice);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updatePciDevice(tblCmpPciDevice);
                        }

                        pciDeviceIds.remove(tblCmpPciDevice.getDeviceId());
                    }
                }

                if (pciDeviceIds.size() > 0)
                {
                    for (String pciDeviceId : pciDeviceIds)
                    {
                        TblCmpPciDevice updateTblCmpPciDevice = vmComputeRepository.getPciDeviceById(cloudId, pciDeviceId);
                        updateTblCmpPciDevice.setVmInstanceId(null);
                        vmComputeRepository.updatePciDeviceForce(updateTblCmpPciDevice);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} vm instance {} error msg: {}", cloudId, vmInstanceId, e.getMessage());
            e.printStackTrace();
        }
    }

    private Set<String> getVolumeInfosByVmId(String cloudId, String vmInstanceId)
    {
        Set<String> volumeIds = new HashSet<>();
        try
        {
            TblCmpVolumeExample example = new TblCmpVolumeExample();
            TblCmpVolumeExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andVmIdEqualTo(vmInstanceId);
            criteria.andImageIdIsNull();
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            List<TblCmpVolume> volumes = repoRepository.getVolumes(example);

            if (!CollectionUtils.isEmpty(volumes))
            {
                volumes.forEach(volume -> volumeIds.add(volume.getVolumeId()));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get volume info by vm {} error msg: {}", vmInstanceId, e.getMessage());
            e.printStackTrace();
        }
        return volumeIds;
    }

    private void syncInstanceNetworkRef(String cloudId, String vmInstanceId, List<RpcNetworkDetailInfo> networkDetailInfos)
    {
        Set<String> instanceNetworkIds = vmComputeRepository.getInstanceNetworkRefIds(cloudId, vmInstanceId);
        for (RpcNetworkDetailInfo networkDetailInfo : networkDetailInfos)
        {
            TblCmpInstanceNetworkRefExample example = new TblCmpInstanceNetworkRefExample();
            TblCmpInstanceNetworkRefExample.Criteria criteria = example.createCriteria();
            criteria.andInstanceIdEqualTo(vmInstanceId);
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andVpcIdEqualTo(networkDetailInfo.getVpcId());
            criteria.andSubnetIdEqualTo(networkDetailInfo.getSubnetId());
            criteria.andPortIdEqualTo(networkDetailInfo.getPortId());
            List<TblCmpInstanceNetworkRef> tblCmpInstanceNetworkRefs = vmComputeRepository.getNetworkInfoAndInstanceIds(example);
            if (! CollectionUtils.isEmpty(tblCmpInstanceNetworkRefs))
            {
                for (TblCmpInstanceNetworkRef tblCmpInstanceNetworkRef : tblCmpInstanceNetworkRefs)
                {
                    instanceNetworkIds.remove(tblCmpInstanceNetworkRef.getInstanceNetworkId());
                }
            }
            else
            {
                TblCmpInstanceNetworkRef tblCmpInstanceNetworkRef = new TblCmpInstanceNetworkRef();
                tblCmpInstanceNetworkRef.setCloudId(cloudId);
                tblCmpInstanceNetworkRef.setInstanceId(vmInstanceId);
                tblCmpInstanceNetworkRef.setVpcId(networkDetailInfo.getVpcId());
                tblCmpInstanceNetworkRef.setSubnetId(networkDetailInfo.getSubnetId());
                tblCmpInstanceNetworkRef.setPortId(networkDetailInfo.getPortId());
                tblCmpInstanceNetworkRef.setEeStatus(SYNCING);
                tblCmpInstanceNetworkRef.setInstanceNetworkId(Utils.assignUUId());
                try
                {
                    vmComputeRepository.insertNetworkInfoAndInstanceId(tblCmpInstanceNetworkRef);
                }
                catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateNetworkInfoAndInstanceId(tblCmpInstanceNetworkRef);
                }
            }

        }

        if (! CollectionUtils.isEmpty(instanceNetworkIds))
        {
            for (String instanceNetworkId : instanceNetworkIds)
            {
                vmComputeRepository.deleteInstanceNetworkByPrimaryKey(new TblCmpInstanceNetworkRefKey(cloudId, instanceNetworkId));
            }
        }
    }

    private void syncSgVmInstance(String cloudId, String vmInstanceId, String eeBp, String eeUser, List<RpcSgInfo> sgInfos)
    {
        Set<String> sgVmInstanceIds = networkRepository.getSgVmInstanceIds(cloudId, vmInstanceId);
        long timeMillis = System.currentTimeMillis();
        for (RpcSgInfo sgInfo : sgInfos)
        {
            TblCmpSgVmInstanceExample example = new TblCmpSgVmInstanceExample();
            TblCmpSgVmInstanceExample.Criteria criteria = example.createCriteria();
            criteria.andInstanceIdEqualTo(vmInstanceId);
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andSgIdEqualTo(sgInfo.getSgId());
            List<TblCmpSgVmInstance> tblCmpSgVmInstances = networkRepository.getSgVmInstances(example);
            if (! CollectionUtils.isEmpty(tblCmpSgVmInstances))
            {
                for (TblCmpSgVmInstance tblCmpSgVmInstance : tblCmpSgVmInstances)
                {
                    if ((eeBp == null && tblCmpSgVmInstance.getEeBp() != null) || (eeBp != null && !eeBp.equals(tblCmpSgVmInstance.getEeBp())) ||
                            (eeUser == null && tblCmpSgVmInstance.getEeUser() != null) || (eeUser != null && !eeUser.equals(tblCmpSgVmInstance.getEeUser())))
                    {
                        tblCmpSgVmInstance.setEeBp(eeBp);
                        tblCmpSgVmInstance.setEeUser(eeUser);
                        tblCmpSgVmInstance.setCreateTime(Utils.buildDate(timeMillis));
                        networkRepository.updateSgVmInstance(tblCmpSgVmInstance);
                    }
                    else
                    {
                        tblCmpSgVmInstance.setCreateTime(Utils.buildDate(timeMillis));
                        networkRepository.updateSgVmInstanceSelective(tblCmpSgVmInstance);
                    }
                    sgVmInstanceIds.remove(tblCmpSgVmInstance.getSgVmId());
                }
            }
            else
            {
                TblCmpSgVmInstance tblCmpSgVmInstance = new TblCmpSgVmInstance();
                tblCmpSgVmInstance.setCloudId(cloudId);
                tblCmpSgVmInstance.setInstanceId(vmInstanceId);
                tblCmpSgVmInstance.setSgId(sgInfo.getSgId());
                tblCmpSgVmInstance.setEeStatus(SYNCING);
                tblCmpSgVmInstance.setSgVmId(Utils.assignUUId());
                tblCmpSgVmInstance.setEeBp(eeBp);
                tblCmpSgVmInstance.setEeUser(eeUser);
                tblCmpSgVmInstance.setCreateTime(Utils.buildDate( timeMillis));
                try
                {
                    networkRepository.createSgVmInstance(tblCmpSgVmInstance);
                }
                catch (DuplicateKeyException e)
                {
                    networkRepository.updateSgVmInstanceSelective(tblCmpSgVmInstance);
                }
            }

            timeMillis--;

            if (! CollectionUtils.isEmpty(sgInfo.getRules()))
            {
                for (RpcSecurityGroupRule rpcSecurityGroupRule : sgInfo.getRules())
                {
                    TblCmpSecurityGroupRule tblCmpSecurityGroupRule = new TblCmpSecurityGroupRule();
                    tblCmpSecurityGroupRule.setCloudId(cloudId);
                    tblCmpSecurityGroupRule.setEeStatus(SYNCING);
                    tblCmpSecurityGroupRule.setRuleId(rpcSecurityGroupRule.getRuleId());
                    tblCmpSecurityGroupRule.setSgId(sgInfo.getSgId());
                    tblCmpSecurityGroupRule.setPhaseStatus(null);
                    tblCmpSecurityGroupRule.setPriority(rpcSecurityGroupRule.getPriority() == null ? null : rpcSecurityGroupRule.getPriority().shortValue());
                    tblCmpSecurityGroupRule.setDirection(rpcSecurityGroupRule.getDirection() == null ? null : rpcSecurityGroupRule.getDirection().shortValue());
                    tblCmpSecurityGroupRule.setProtocol(rpcSecurityGroupRule.getProtocol() == null ? null : rpcSecurityGroupRule.getProtocol().shortValue());
                    tblCmpSecurityGroupRule.setPort(rpcSecurityGroupRule.getPort());
                    if (rpcSecurityGroupRule.getAddressRef() != null)
                    {
                        tblCmpSecurityGroupRule.setCidr(rpcSecurityGroupRule.getAddressRef() == null ? null : rpcSecurityGroupRule.getAddressRef().getCidr());
                        tblCmpSecurityGroupRule.setSgIdReference(rpcSecurityGroupRule.getAddressRef() == null ? null : rpcSecurityGroupRule.getAddressRef().getSgId());
                    }

                    tblCmpSecurityGroupRule.setPoolId(null);
                    tblCmpSecurityGroupRule.setDescription(rpcSecurityGroupRule.getDescription());
                    tblCmpSecurityGroupRule.setAddressType(rpcSecurityGroupRule.getAddressType() == null ? null : rpcSecurityGroupRule.getAddressType().shortValue());
                    tblCmpSecurityGroupRule.setAction(rpcSecurityGroupRule.getAction() == null ? null : rpcSecurityGroupRule.getAction().shortValue());

                    try
                    {
                        networkRepository.createSecurityGroupRule(tblCmpSecurityGroupRule);
                    }
                    catch (DuplicateKeyException e)
                    {
                        boolean forceUpdate = false;
                        if (tblCmpSecurityGroupRule.getCidr() == null)
                        {
                            forceUpdate = true;
                        }
                        if (tblCmpSecurityGroupRule.getSgIdReference() == null)
                        {
                            forceUpdate = true;
                        }
                        if (forceUpdate)
                        {
                            TblCmpSecurityGroupRule updateTblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, rpcSecurityGroupRule.getRuleId());
                            if (updateTblCmpSecurityGroupRule != null)
                            {
                                updateTblCmpSecurityGroupRule.setPriority(rpcSecurityGroupRule.getPriority() == null ? null : rpcSecurityGroupRule.getPriority().shortValue());
                                updateTblCmpSecurityGroupRule.setDirection(rpcSecurityGroupRule.getDirection() == null ? null : rpcSecurityGroupRule.getDirection().shortValue());
                                updateTblCmpSecurityGroupRule.setProtocol(rpcSecurityGroupRule.getProtocol() == null ? null : rpcSecurityGroupRule.getProtocol().shortValue());
                                updateTblCmpSecurityGroupRule.setPort(rpcSecurityGroupRule.getPort());
                                if (rpcSecurityGroupRule.getAddressRef() != null)
                                {
                                    updateTblCmpSecurityGroupRule.setCidr(rpcSecurityGroupRule.getAddressRef() == null ? null : rpcSecurityGroupRule.getAddressRef().getCidr());
                                    updateTblCmpSecurityGroupRule.setSgIdReference(rpcSecurityGroupRule.getAddressRef() == null ? null : rpcSecurityGroupRule.getAddressRef().getSgId());
                                }

                                updateTblCmpSecurityGroupRule.setPoolId(null);
                                updateTblCmpSecurityGroupRule.setDescription(rpcSecurityGroupRule.getDescription());
                                updateTblCmpSecurityGroupRule.setAddressType(rpcSecurityGroupRule.getAddressType() == null ? null : rpcSecurityGroupRule.getAddressType().shortValue());
                                updateTblCmpSecurityGroupRule.setAction(rpcSecurityGroupRule.getAction() == null ? null : rpcSecurityGroupRule.getAction().shortValue());
                                networkRepository.updateSecurityGroupRule(updateTblCmpSecurityGroupRule);
                            }
                            else
                            {
                                networkRepository.updateSecurityGroupRuleSelective(tblCmpSecurityGroupRule);
                            }
                        }
                        else
                        {
                            networkRepository.updateSecurityGroupRuleSelective(tblCmpSecurityGroupRule);
                        }
                    }


                }
            }
        }

        if (! CollectionUtils.isEmpty(sgVmInstanceIds))
        {
            for (String sgVmInstanceId : sgVmInstanceIds)
            {
                networkRepository.deleteSgVmInstanceByPrimaryKey(new TblCmpSgVmInstanceKey(cloudId, sgVmInstanceId));
            }
        }
    }

    private void syncVmInstanceNetworks(String cloudId, String vmInstanceId, List<RpcNetworkDetailInfo> getNetworkDetailInfos)
    {
        for (RpcNetworkDetailInfo rpcNetworkDetailInfo : getNetworkDetailInfos)
        {
            TblCmpVpc tblCmpVpc = new TblCmpVpc();
            tblCmpVpc.setCloudId(cloudId);
            tblCmpVpc.setEeStatus(SYNCING);
            tblCmpVpc.setVpcId(rpcNetworkDetailInfo.getVpcId());
            tblCmpVpc.setName(rpcNetworkDetailInfo.getVpcName());
            tblCmpVpc.setVpcCidr(rpcNetworkDetailInfo.getVpcCidr());
            try
            {
                networkRepository.createVpc(tblCmpVpc);
            }
            catch (DuplicateKeyException e)
            {
                networkRepository.updateVpc(tblCmpVpc);
            }

            TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
            tblCmpSubnet.setCloudId(cloudId);
            tblCmpSubnet.setEeStatus(SYNCING);
            tblCmpSubnet.setSubnetId(rpcNetworkDetailInfo.getSubnetId());
            tblCmpSubnet.setName(rpcNetworkDetailInfo.getSubnetName());
            tblCmpSubnet.setSubnetCidr(rpcNetworkDetailInfo.getSubnetCidr());
            try
            {
                networkRepository.createSubnet(tblCmpSubnet);
            }
            catch (DuplicateKeyException e)
            {
                networkRepository.updateSubnet(tblCmpSubnet);
            }

            if (rpcNetworkDetailInfo.getPortId() != null)
            {
                TblCmpPort tblCmpPort = new TblCmpPort();
                tblCmpPort.setCloudId(cloudId);
                tblCmpPort.setEeStatus(SYNCING);
                tblCmpPort.setPortId(rpcNetworkDetailInfo.getPortId());
                tblCmpPort.setIpAddress(rpcNetworkDetailInfo.getIpAddress());
                if (AgentConstant.PORT_BOUND.equals(rpcNetworkDetailInfo.getBoundType()))
                {
                    tblCmpPort.setEipId(rpcNetworkDetailInfo.getEipId());
                }
                tblCmpPort.setEipPhaseStatus(rpcNetworkDetailInfo.getBoundPhaseStatus());
                tblCmpPort.setInstanceId(vmInstanceId);
                if (tblCmpPort.getEipId() == null) tblCmpPort.setEipPhaseStatus(PhaseStatus.DETACH_EIP_DONE);
                if (rpcNetworkDetailInfo.getIsVip() != null && rpcNetworkDetailInfo.getIsVip()) tblCmpPort.setType(PortType.vip);
                try
                {
                    networkRepository.createPort(tblCmpPort);
                }
                catch (DuplicateKeyException e)
                {
                    networkRepository.updatePort(tblCmpPort);
                }
            }
        }
    }

    public void syncSnaps(String cloudId)
    {
        String urlPattern = "/api/vm/v1/snaps?page_num=%s&page_size=100";
        try
        {
            Set<String> snapIds = vmComputeRepository.getSnapIds(cloudId);
            if (snapIds == null)
            {
                snapIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    VmSnapsRsp getSnapsRsp = gson.fromJson(result, VmSnapsRsp.class);
                    if (getSnapsRsp == null || CollectionUtils.isEmpty(getSnapsRsp.getSnaps()))
                    {
                        return;
                    }

                    for (VmSnapInfo snapInfo : getSnapsRsp.getSnaps())
                    {
                        TblCmpVmSnap tblCmpVmSnap = new TblCmpVmSnap();
                        tblCmpVmSnap.setCloudId(cloudId);
                        tblCmpVmSnap.setSnapId(snapInfo.getSnapId());
                        tblCmpVmSnap.setVmInstanceId(snapInfo.getVmInstanceId());
                        tblCmpVmSnap.setName(snapInfo.getName());
                        tblCmpVmSnap.setUserId(null);
                        tblCmpVmSnap.setSnapIdFromAgent(null);
                        tblCmpVmSnap.setPhaseStatus(snapInfo.getPhaseStatus());
                        tblCmpVmSnap.setDescription(snapInfo.getDescription());
                        tblCmpVmSnap.setIsCurrent(snapInfo.isCurrent());
                        tblCmpVmSnap.setCreateTime(snapInfo.getCreateTime() == null ? null : Utils.parseStTime(snapInfo.getCreateTime()));
                        tblCmpVmSnap.setUpdateTime(snapInfo.getUpdateTime() == null ? null : Utils.parseStTime(snapInfo.getUpdateTime()));
                        tblCmpVmSnap.setEeStatus(SYNCING);
                        tblCmpVmSnap.setParentId(snapInfo.getParentId());

                        try
                        {
                            vmComputeRepository.insertVmSnap(tblCmpVmSnap);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateVmSnap(tblCmpVmSnap);
                        }

                        snapIds.remove(snapInfo.getSnapId());
                    }
                    total = getSnapsRsp.getTotalNum();
                }
            }

            if (snapIds.size() > 0)
            {
                for (String snapId : snapIds)
                {
                    TblCmpVmSnap tblCmpVmSnap = new TblCmpVmSnap();
                    tblCmpVmSnap.setCloudId(cloudId);
                    tblCmpVmSnap.setSnapId(snapId);
                    tblCmpVmSnap.setEeStatus(REMOVED);

                    vmComputeRepository.updateVmSnap(tblCmpVmSnap);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} snaps error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSnap(String cloudId, String vmSnapId)
    {
        String url = "/api/vm/v1/snaps/" + vmSnapId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("vm snap does not exists"))
                {
                    TblCmpVmSnap tblCmpVmSnap = vmComputeRepository.getVmSnapById(cloudId, vmSnapId);
                    if (tblCmpVmSnap != null)
                    {
                        tblCmpVmSnap.setEeStatus(REMOVED);
                        vmComputeRepository.updateVmSnap(tblCmpVmSnap);
                    }
                    return;
                }

                VmSnapDetailInfoRsp vmSnapDetailInfoRsp = gson.fromJson(result, VmSnapDetailInfoRsp.class);
                if (vmSnapDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpVmSnap tblCmpVmSnap = new TblCmpVmSnap();
                tblCmpVmSnap.setCloudId(cloudId);
                tblCmpVmSnap.setSnapId(vmSnapDetailInfoRsp.getSnapId());
                tblCmpVmSnap.setVmInstanceId(vmSnapDetailInfoRsp.getVmInstanceId());
                tblCmpVmSnap.setName(vmSnapDetailInfoRsp.getName());
                tblCmpVmSnap.setUserId(null);
                tblCmpVmSnap.setSnapIdFromAgent(null);
                tblCmpVmSnap.setPhaseStatus(vmSnapDetailInfoRsp.getPhaseStatus());
                tblCmpVmSnap.setDescription(vmSnapDetailInfoRsp.getDescription());
                tblCmpVmSnap.setIsCurrent(vmSnapDetailInfoRsp.isCurrent());
                tblCmpVmSnap.setCreateTime(vmSnapDetailInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(vmSnapDetailInfoRsp.getCreateTime()));
                tblCmpVmSnap.setUpdateTime(vmSnapDetailInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(vmSnapDetailInfoRsp.getUpdateTime()));
                tblCmpVmSnap.setEeStatus(SYNCING);
                tblCmpVmSnap.setParentId(vmSnapDetailInfoRsp.getParentId());

                try
                {
                    vmComputeRepository.insertVmSnap(tblCmpVmSnap);
                } catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateVmSnap(tblCmpVmSnap);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} snap {} error msg: {}", cloudId, vmSnapId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPciDeviceAvailableNodes(String cloudId)
    {
        List<Boolean> isGpus = Lists.newArrayList(true, false);
        String urlPattern = "/api/vm/v1/pci_devices/available_nodes?is_gpu=%s&page_num=%s&page_size=100";
        try
        {
            for (Boolean isGpu : isGpus)
            {
                int pageNum = 0;
                long total = 100;
                while ((pageNum * 100L) < total)
                {
                    pageNum = pageNum + 1;
                    String result = getDataFromCloud(cloudId, String.format(urlPattern, isGpu, pageNum));
                    if (StringUtils.isNotEmpty(result))
                    {
                        List<HypervisorNodeInfo> hypervisorNodeInfos = gson.fromJson(result, new com.google.gson.reflect.TypeToken<List<HypervisorNodeInfo>>(){}.getType());
                        if (hypervisorNodeInfos == null || CollectionUtils.isEmpty(hypervisorNodeInfos))
                        {
                            return;
                        }

                        for (HypervisorNodeInfo hypervisorNodeInfo : hypervisorNodeInfos)
                        {
                            if (hypervisorNodeInfo.getCpuModel() == null) continue;
                            TblCmpHypervisorNode tblCmpHypervisorNode = new TblCmpHypervisorNode();
                            tblCmpHypervisorNode.setCloudId(cloudId);
                            tblCmpHypervisorNode.setNodeId(hypervisorNodeInfo.getNodeId());
                            tblCmpHypervisorNode.setCpuModel(hypervisorNodeInfo.getCpuModel());

                            vmComputeRepository.updateHypervisorNode(tblCmpHypervisorNode);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} pci devices available nodes error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncNfsServers(String cloudId)
    {
        String urlPattern = "/api/vm/v1/nfs?page_num=%s&page_size=100";
        try
        {
            Set<String> nfsIds = vmComputeRepository.getNfsIds(cloudId);
            if (nfsIds == null)
            {
                nfsIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    NfsInfosRsp nfsInfosRsp = gson.fromJson(result, NfsInfosRsp.class);
                    if (nfsInfosRsp == null || CollectionUtils.isEmpty(nfsInfosRsp.getNfsInfos()))
                    {
                        return;
                    }

                    for (NfsInfoRsp nfsInfoRsp : nfsInfosRsp.getNfsInfos())
                    {
                        TblCmpNfs tblCmpNfs = new TblCmpNfs();
                        tblCmpNfs.setCloudId(cloudId);
                        tblCmpNfs.setNfsId(nfsInfoRsp.getNfsId());
                        tblCmpNfs.setName(nfsInfoRsp.getName());
                        tblCmpNfs.setCreateTime(nfsInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(nfsInfoRsp.getCreateTime()));
                        tblCmpNfs.setUpdateTime(nfsInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(nfsInfoRsp.getUpdateTime()));
                        tblCmpNfs.setVpcId(nfsInfoRsp.getVpcId());
                        tblCmpNfs.setSubnetId(nfsInfoRsp.getSubnetId());
                        tblCmpNfs.setPortId(nfsInfoRsp.getPortId());
                        tblCmpNfs.setSize(nfsInfoRsp.getSize());
                        tblCmpNfs.setPhaseStatus(nfsInfoRsp.getPhaseStatus());
                        tblCmpNfs.setDescription(nfsInfoRsp.getDescription());
                        tblCmpNfs.setEeStatus(SYNCING);

                        try
                        {
                            vmComputeRepository.insertNfs(tblCmpNfs);
                        } catch (DuplicateKeyException e)
                        {
                            vmComputeRepository.updateNfs(tblCmpNfs);
                        }

                        nfsIds.remove(nfsInfoRsp.getNfsId());
                    }
                    total = nfsInfosRsp.getTotalNum();
                }
            }

            if (nfsIds.size() > 0)
            {
                for (String nfsId : nfsIds)
                {
                    TblCmpNfs tblCmpNfs = new TblCmpNfs();
                    tblCmpNfs.setCloudId(cloudId);
                    tblCmpNfs.setNfsId(nfsId);
                    tblCmpNfs.setEeStatus(REMOVED);

                    vmComputeRepository.updateNfs(tblCmpNfs);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} nfs servers error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncNfsServer(String cloudId, String nfsId)
    {
        String url = "/api/vm/v1/nfs/" + nfsId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("nfs does not exist"))
                {
                    TblCmpNfs tblCmpNfs = vmComputeRepository.getNfsById(cloudId, nfsId);
                    if (tblCmpNfs != null)
                    {
                        tblCmpNfs.setEeStatus(REMOVED);
                        vmComputeRepository.updateNfs(tblCmpNfs);
                    }
                    return;
                }

                NfsInfoRsp nfsInfoRsp = gson.fromJson(result, NfsInfoRsp.class);
                if (nfsInfoRsp == null)
                {
                    return;
                }

                TblCmpNfs tblCmpNfs = new TblCmpNfs();
                tblCmpNfs.setCloudId(cloudId);
                tblCmpNfs.setNfsId(nfsInfoRsp.getNfsId());
                tblCmpNfs.setName(nfsInfoRsp.getName());
                tblCmpNfs.setCreateTime(nfsInfoRsp.getCreateTime() == null ? null : Utils.parseStTime(nfsInfoRsp.getCreateTime()));
                tblCmpNfs.setUpdateTime(nfsInfoRsp.getUpdateTime() == null ? null : Utils.parseStTime(nfsInfoRsp.getUpdateTime()));
                tblCmpNfs.setVpcId(nfsInfoRsp.getVpcId());
                tblCmpNfs.setSubnetId(nfsInfoRsp.getSubnetId());
                tblCmpNfs.setPortId(nfsInfoRsp.getPortId());
                tblCmpNfs.setSize(nfsInfoRsp.getSize());
                tblCmpNfs.setPhaseStatus(nfsInfoRsp.getPhaseStatus());
                tblCmpNfs.setDescription(nfsInfoRsp.getDescription());
                tblCmpNfs.setEeStatus(SYNCING);

                try
                {
                    vmComputeRepository.insertNfs(tblCmpNfs);
                } catch (DuplicateKeyException e)
                {
                    vmComputeRepository.updateNfs(tblCmpNfs);
                }

                if (tblCmpNfs.getPortId() != null)
                {
                    TblCmpPort tblCmpPort = new TblCmpPort();
                    tblCmpPort.setCloudId(cloudId);
                    tblCmpPort.setPortId(tblCmpNfs.getPortId());
                    tblCmpPort.setIpAddress(nfsInfoRsp.getServicePath().replace(":/", ""));
                    tblCmpPort.setEeStatus(SYNCING);
                    tblCmpPort.setSubnetId(tblCmpNfs.getSubnetId());
                    try
                    {
                        networkRepository.createPort(tblCmpPort);
                    } catch (DuplicateKeyException e)
                    {
                        networkRepository.updatePort(tblCmpPort);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} nfs {} error msg: {}", cloudId, nfsId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVpcs(String cloudId)
    {
        String urlPattern = "/api/network/v1/vpcs?page_num=%s&page_size=100";
        try
        {
            Set<String> vpcIds = networkRepository.getVpcIds(cloudId);
            if (vpcIds == null)
            {
                vpcIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    VpcsRspVo getVpcsRsp = gson.fromJson(result, VpcsRspVo.class);
                    if (getVpcsRsp == null || CollectionUtils.isEmpty(getVpcsRsp.getVpcs()))
                    {
                        return;
                    }

                    for (VpcDetailInfoRspVo vpcDetailInfoRspVo : getVpcsRsp.getVpcs())
                    {
                        TblCmpVpc tblCmpVpc = new TblCmpVpc();
                        tblCmpVpc.setCloudId(cloudId);
                        tblCmpVpc.setVpcId(vpcDetailInfoRspVo.getVpcId());
                        tblCmpVpc.setVpcIdFromAgent(null);
                        tblCmpVpc.setName(vpcDetailInfoRspVo.getName());
                        tblCmpVpc.setUserId(vpcDetailInfoRspVo.getUserId());
                        tblCmpVpc.setVlanId(null);
                        tblCmpVpc.setPhaseStatus(vpcDetailInfoRspVo.getPhaseStatus() == null ? null : vpcDetailInfoRspVo.getPhaseStatus().shortValue());
                        tblCmpVpc.setPhaseInfo(vpcDetailInfoRspVo.getPhaseInfo());
                        tblCmpVpc.setAddressType(null);
                        tblCmpVpc.setVpcCidr(vpcDetailInfoRspVo.getCidr());
                        tblCmpVpc.setCreateTime(vpcDetailInfoRspVo.getCreateTime() == null ? null : Utils.parseStTime(vpcDetailInfoRspVo.getCreateTime()));
                        tblCmpVpc.setUpdateTime(vpcDetailInfoRspVo.getUpdateTime() == null ? null : Utils.parseStTime(vpcDetailInfoRspVo.getUpdateTime()));
                        tblCmpVpc.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createVpc(tblCmpVpc);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateVpc(tblCmpVpc);
                        }

                        vpcIds.remove(vpcDetailInfoRspVo.getVpcId());
                    }
                    total = getVpcsRsp.getTotalNum();
                }
            }

            if (vpcIds.size() > 0)
            {
                for (String vpcId : vpcIds)
                {
                    TblCmpVpc tblCmpVpc = new TblCmpVpc();
                    tblCmpVpc.setCloudId(cloudId);
                    tblCmpVpc.setVpcId(vpcId);
                    tblCmpVpc.setEeStatus(REMOVED);

                    networkRepository.updateVpc(tblCmpVpc);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} vpcs error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVpc(String cloudId, String vpcId)
    {
        String url = "/api/network/v1/vpcs/" + vpcId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("vpc not exist"))
                {
                    TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);
                    if (tblCmpVpc != null && tblCmpVpc.getEeStatus() == SYNCING)
                    {
                        tblCmpVpc.setEeStatus(REMOVED);
                        networkRepository.updateVpc(tblCmpVpc);
                    }
                    return;
                }

                VpcDetailInfoRspVo vpcDetailInfoRspVo = gson.fromJson(result, VpcDetailInfoRspVo.class);
                if (vpcDetailInfoRspVo == null)
                {
                    return;
                }

                TblCmpVpc tblCmpVpc = new TblCmpVpc();
                tblCmpVpc.setCloudId(cloudId);
                tblCmpVpc.setVpcId(vpcDetailInfoRspVo.getVpcId());
                tblCmpVpc.setVpcIdFromAgent(null);
                tblCmpVpc.setName(vpcDetailInfoRspVo.getName());
                tblCmpVpc.setUserId(vpcDetailInfoRspVo.getUserId());
                tblCmpVpc.setVlanId(null);
                tblCmpVpc.setPhaseStatus(vpcDetailInfoRspVo.getPhaseStatus() == null ? null : vpcDetailInfoRspVo.getPhaseStatus().shortValue());
                tblCmpVpc.setPhaseInfo(vpcDetailInfoRspVo.getPhaseInfo());
                tblCmpVpc.setAddressType(null);
                tblCmpVpc.setVpcCidr(vpcDetailInfoRspVo.getCidr());
                tblCmpVpc.setCreateTime(vpcDetailInfoRspVo.getCreateTime() == null ? null : Utils.parseStTime(vpcDetailInfoRspVo.getCreateTime()));
                tblCmpVpc.setUpdateTime(vpcDetailInfoRspVo.getUpdateTime() == null ? null : Utils.parseStTime(vpcDetailInfoRspVo.getUpdateTime()));
                tblCmpVpc.setEeStatus(SYNCING);

                try
                {
                    networkRepository.createVpc(tblCmpVpc);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateVpc(tblCmpVpc);
                }

                syncVpcVlanId(cloudId, vpcId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} vpc {} error msg: {}", cloudId, vpcId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVpcVlanId(String cloudId, String vpcId)
    {
        String urlPattern = "/api/network/v1/eips?vpc_id=%s&page_num=1&page_size=10";
        try
        {
            String result = getDataFromCloud(cloudId, String.format(urlPattern, vpcId));
            if (StringUtils.isNotEmpty(result))
            {
                GetEipsRsp getEipsRsp = gson.fromJson(result, GetEipsRsp.class);
                if (getEipsRsp == null || CollectionUtils.isEmpty(getEipsRsp.getEips()))
                {
                    return;
                }

                EipInfoVo eipInfo = getEipsRsp.getEips().get(0);
                TblCmpEipPoolVpcRefExample example = new TblCmpEipPoolVpcRefExample();
                TblCmpEipPoolVpcRefExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andEeStatusNotEqualTo(REMOVED);
                criteria.andPoolIdEqualTo(eipInfo.getEipPoolId());
                List<TblCmpEipPoolVpcRef> tblCmpEipPoolVpcRefs = networkRepository.getEipPoolAndVpcRefs(example);
                if (CollectionUtils.isEmpty(tblCmpEipPoolVpcRefs))
                {
                    return;
                }

                TblCmpVpc tblCmpVpc = new TblCmpVpc();
                tblCmpVpc.setCloudId(cloudId);
                tblCmpVpc.setVpcId(vpcId);
                tblCmpVpc.setVlanId(tblCmpEipPoolVpcRefs.get(0).getVlanId());
                networkRepository.updateVpc(tblCmpVpc);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} vpc vlanId {} error msg: {}", cloudId, vpcId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSubnets(String cloudId)
    {
        String urlPattern = "/api/network/v1/subnets?page_num=%s&page_size=100";
        try
        {
            Set<String> subnetIds = networkRepository.getSubnetIds(cloudId);
            if (subnetIds == null)
            {
                subnetIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    SubnetsRspVo getSubnetsRsp = gson.fromJson(result, SubnetsRspVo.class);
                    if (getSubnetsRsp == null || CollectionUtils.isEmpty(getSubnetsRsp.getSubnets()))
                    {
                        return;
                    }

                    for (SubnetDetailInfoRspVo subnetDetailInfoRspVo : getSubnetsRsp.getSubnets())
                    {
                        TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
                        tblCmpSubnet.setCloudId(cloudId);
                        tblCmpSubnet.setSubnetId(subnetDetailInfoRspVo.getSubnetId());
                        tblCmpSubnet.setSubnetIdFromAgent(null);
                        tblCmpSubnet.setName(subnetDetailInfoRspVo.getName());
                        tblCmpSubnet.setUserId(subnetDetailInfoRspVo.getUserId());
                        tblCmpSubnet.setVpcId(subnetDetailInfoRspVo.getVpcId());
                        tblCmpSubnet.setPhaseStatus(subnetDetailInfoRspVo.getPhaseStatus() == null ? null : subnetDetailInfoRspVo.getPhaseStatus().shortValue());
                        tblCmpSubnet.setPhaseInfo(subnetDetailInfoRspVo.getPhaseInfo());
                        tblCmpSubnet.setAddressType(Short.valueOf(String.valueOf(subnetDetailInfoRspVo.getAddressType())));
                        tblCmpSubnet.setSubnetCidr(subnetDetailInfoRspVo.getCidr());
                        tblCmpSubnet.setGatewayIp(subnetDetailInfoRspVo.getGatewayIp());
                        tblCmpSubnet.setCreateTime(subnetDetailInfoRspVo.getCreateTime() == null ? null : Utils.parseStTime(subnetDetailInfoRspVo.getCreateTime()));
                        tblCmpSubnet.setUpdateTime(subnetDetailInfoRspVo.getUpdateTime() == null ? null : Utils.parseStTime(subnetDetailInfoRspVo.getUpdateTime()));
                        tblCmpSubnet.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createSubnet(tblCmpSubnet);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateSubnet(tblCmpSubnet);
                        }

                        subnetIds.remove(subnetDetailInfoRspVo.getSubnetId());
                    }
                    total = getSubnetsRsp.getTotalNum();
                }
            }

            if (subnetIds.size() > 0)
            {
                for (String subnetId : subnetIds)
                {
                    TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
                    tblCmpSubnet.setCloudId(cloudId);
                    tblCmpSubnet.setSubnetId(subnetId);
                    tblCmpSubnet.setEeStatus(REMOVED);

                    networkRepository.updateSubnet(tblCmpSubnet);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} subnets error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSubnet(String cloudId, String subnetId)
    {
        String url = "/api/network/v1/subnets/" + subnetId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("subnet not exist"))
                {
                    TblCmpSubnet tblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);
                    if (tblCmpSubnet != null && tblCmpSubnet.getEeStatus() == SYNCING)
                    {
                        tblCmpSubnet.setEeStatus(REMOVED);
                        networkRepository.updateSubnet(tblCmpSubnet);
                    }
                    return;
                }

                SubnetDetailInfoRspVo subnetDetailInfoRspVo = gson.fromJson(result, SubnetDetailInfoRspVo.class);
                if (subnetDetailInfoRspVo == null)
                {
                    return;
                }

                TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
                tblCmpSubnet.setCloudId(cloudId);
                tblCmpSubnet.setSubnetId(subnetDetailInfoRspVo.getSubnetId());
                tblCmpSubnet.setSubnetIdFromAgent(null);
                tblCmpSubnet.setName(subnetDetailInfoRspVo.getName());
                tblCmpSubnet.setUserId(subnetDetailInfoRspVo.getUserId());
                tblCmpSubnet.setVpcId(subnetDetailInfoRspVo.getVpcId());
                tblCmpSubnet.setPhaseStatus(subnetDetailInfoRspVo.getPhaseStatus() == null ? null : subnetDetailInfoRspVo.getPhaseStatus().shortValue());
                tblCmpSubnet.setPhaseInfo(subnetDetailInfoRspVo.getPhaseInfo());
                tblCmpSubnet.setAddressType(Short.valueOf(String.valueOf(subnetDetailInfoRspVo.getAddressType())));
                tblCmpSubnet.setSubnetCidr(subnetDetailInfoRspVo.getCidr());
                tblCmpSubnet.setGatewayIp(subnetDetailInfoRspVo.getGatewayIp());
                tblCmpSubnet.setCreateTime(subnetDetailInfoRspVo.getCreateTime() == null ? null : Utils.parseStTime(subnetDetailInfoRspVo.getCreateTime()));
                tblCmpSubnet.setUpdateTime(subnetDetailInfoRspVo.getUpdateTime() == null ? null : Utils.parseStTime(subnetDetailInfoRspVo.getUpdateTime()));
                tblCmpSubnet.setEeStatus(SYNCING);

                try
                {
                    networkRepository.createSubnet(tblCmpSubnet);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateSubnet(tblCmpSubnet);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} subnet {} error msg: {}", cloudId, subnetId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncEips(String cloudId)
    {
        String urlPattern = "/api/network/v1/eips?page_num=%s&page_size=1000";
        try
        {
            Set<String> eipIds = networkRepository.getEipIds(cloudId);
            if (eipIds == null)
            {
                eipIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 1000;
            while ((pageNum * 1000L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    GetEipsRsp getEipsRsp = gson.fromJson(result, GetEipsRsp.class);
                    if (getEipsRsp == null || CollectionUtils.isEmpty(getEipsRsp.getEips()))
                    {
                        return;
                    }

                    for (EipInfoVo eipInfo : getEipsRsp.getEips())
                    {
                        TblCmpEip tblCmpEip = new TblCmpEip();
                        tblCmpEip.setCloudId(cloudId);
                        tblCmpEip.setEipId(eipInfo.getEipId());
                        tblCmpEip.setPoolId(eipInfo.getEipPoolId());
                        tblCmpEip.setUserId(null);
                        tblCmpEip.setAddressType(eipInfo.getAddressType() == null ? null : eipInfo.getAddressType().shortValue());
                        tblCmpEip.setIpaddr(eipInfo.getIpAddress());
                        tblCmpEip.setPrefixLen(null);
                        tblCmpEip.setStatus(eipInfo.getPhaseStatus() == null ? null : eipInfo.getPhaseStatus().shortValue());
                        tblCmpEip.setBandwidth(null);
                        tblCmpEip.setCreateTime(eipInfo.getCreateTime() == null ? null : Utils.parseStTime(eipInfo.getCreateTime()));
                        tblCmpEip.setUpdateTime(eipInfo.getUpdateTime() == null ? null : Utils.parseStTime(eipInfo.getUpdateTime()));
                        tblCmpEip.setEeStatus(SYNCING);
                        tblCmpEip.setBoundType(eipInfo.getBoundType());
                        tblCmpEip.setBoundId(eipInfo.getBoundId());
                        tblCmpEip.setPublicIp(eipInfo.getPublicIp());
                        if (AgentConstant.PORT_BOUND.equals(tblCmpEip.getBoundType()))
                        {
                            TblCmpPort tblCmpPort = networkRepository.getPortByVmInstanceId(cloudId, tblCmpEip.getBoundId());
                            if (tblCmpPort != null)
                            {
                                tblCmpEip.setBoundId(tblCmpPort.getPortId());
                            }
                        }

                        try
                        {
                            networkRepository.createEip(tblCmpEip);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateEipByPrimaryKeySelective(tblCmpEip);
                        }

                        if(tblCmpEip.getBoundId() == null)
                        {
                            TblCmpEip tempTblCmpEip = networkRepository.getEipById(cloudId, eipInfo.getEipId());
                            if (tempTblCmpEip != null && tempTblCmpEip.getBoundId() != null)
                            {
                                tempTblCmpEip.setBoundId(null);
                                networkRepository.updateEipByPrimaryKey(tempTblCmpEip);
                            }
                        }

                        eipIds.remove(eipInfo.getEipId());
                    }
                    total = getEipsRsp.getTotalNum();
                }
            }

            if (eipIds.size() > 0)
            {
                for (String eipId : eipIds)
                {
                    TblCmpEip tblCmpEip = new TblCmpEip();
                    tblCmpEip.setCloudId(cloudId);
                    tblCmpEip.setEipId(eipId);
                    tblCmpEip.setEeStatus(REMOVED);

                    networkRepository.updateEipByPrimaryKeySelective(tblCmpEip);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} eips error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncEip(String cloudId, String eipId)
    {
        String url = "/api/network/v1/eips/" + eipId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("eip does not exists"))
                {
                    TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);
                    if (tblCmpEip != null)
                    {
                        tblCmpEip.setEeStatus(REMOVED);
                        networkRepository.updateEipByPrimaryKeySelective(tblCmpEip);
                    }
                    return;
                }

                EipInfoVo eipInfoVo = gson.fromJson(result, EipInfoVo.class);
                if (eipInfoVo == null)
                {
                    return;
                }

                TblCmpEip tblCmpEip = new TblCmpEip();
                tblCmpEip.setCloudId(cloudId);
                tblCmpEip.setEipId(eipInfoVo.getEipId());
                tblCmpEip.setPoolId(eipInfoVo.getEipPoolId());
                tblCmpEip.setUserId(null);
                tblCmpEip.setAddressType(eipInfoVo.getAddressType() == null ? null : eipInfoVo.getAddressType().shortValue());
                tblCmpEip.setIpaddr(eipInfoVo.getIpAddress());
                tblCmpEip.setPrefixLen(null);
                tblCmpEip.setStatus(eipInfoVo.getPhaseStatus() == null ? null : eipInfoVo.getPhaseStatus().shortValue());
                tblCmpEip.setBandwidth(null);
                tblCmpEip.setCreateTime(eipInfoVo.getCreateTime() == null ? null : Utils.parseStTime(eipInfoVo.getCreateTime()));
                tblCmpEip.setUpdateTime(eipInfoVo.getUpdateTime() == null ? null : Utils.parseStTime(eipInfoVo.getUpdateTime()));
                tblCmpEip.setEeStatus(SYNCING);
                tblCmpEip.setBoundType(eipInfoVo.getBoundType());
                tblCmpEip.setBoundId(eipInfoVo.getBoundId());
                tblCmpEip.setPublicIp(eipInfoVo.getPublicIp());
                if (AgentConstant.PORT_BOUND.equals(tblCmpEip.getBoundType()))
                {
                    TblCmpPort tblCmpPort = networkRepository.getPortByVmInstanceId(cloudId, tblCmpEip.getBoundId());
                    if (tblCmpPort != null)
                    {
                        tblCmpEip.setBoundId(tblCmpPort.getPortId());
                    }
                }

                try
                {
                    networkRepository.createEip(tblCmpEip);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateEipByPrimaryKeySelective(tblCmpEip);
                }

                if(tblCmpEip.getBoundId() == null || tblCmpEip.getBoundType() == null)
                {
                    TblCmpEip tempTblCmpEip = networkRepository.getEipById(cloudId, eipId);
                    if (tempTblCmpEip != null && (tempTblCmpEip.getBoundId() != null || tempTblCmpEip.getBoundType() != null))
                    {
                        tempTblCmpEip.setBoundId(tblCmpEip.getBoundId());
                        tempTblCmpEip.setBoundType(tblCmpEip.getBoundType());
                        networkRepository.updateEipByPrimaryKey(tempTblCmpEip);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} eip {} error msg: {}", cloudId, eipId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPortMaps(String cloudId)
    {
        String urlPattern = "/api/network/v1/portMaps?page_num=%s&page_size=100";
        try
        {
            Set<String> eipMapIds = networkRepository.getEipMapIds(cloudId);
            if (eipMapIds == null)
            {
                eipMapIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    GetEipPortMapsRsp getEipPortMapsRsp = gson.fromJson(result, GetEipPortMapsRsp.class);
                    if (getEipPortMapsRsp == null || CollectionUtils.isEmpty(getEipPortMapsRsp.getEipPortMaps()))
                    {
                        return;
                    }

                    for (EipPortMapRspVo eipPortMapRspVo : getEipPortMapsRsp.getEipPortMaps())
                    {
                        TblCmpEipMap tblCmpEipMap = new TblCmpEipMap();
                        tblCmpEipMap.setCloudId(cloudId);
                        tblCmpEipMap.setEipMapId(eipPortMapRspVo.getEipMapId());
                        tblCmpEipMap.setMapName(eipPortMapRspVo.getMapName());
                        tblCmpEipMap.setEipId(eipPortMapRspVo.getEipId());
                        tblCmpEipMap.setSubnetId(eipPortMapRspVo.getSubnetId());
                        tblCmpEipMap.setUserId(eipPortMapRspVo.getUserId());

                        TblCmpPortExample tblCmpPortExample = new TblCmpPortExample();
                        TblCmpPortExample.Criteria criteria = tblCmpPortExample.createCriteria();
                        criteria.andCloudIdEqualTo(cloudId);
                        criteria.andEeStatusNotEqualTo(REMOVED);
                        criteria.andInstanceIdEqualTo(eipPortMapRspVo.getInstanceId());
                        criteria.andSubnetIdEqualTo(eipPortMapRspVo.getSubnetId());
                        criteria.andEipIdEqualTo(eipPortMapRspVo.getEipId());
                        List<TblCmpPort> tblCmpPorts = networkRepository.getPorts(tblCmpPortExample);
                        if (! CollectionUtils.isEmpty(tblCmpPorts))
                        {
                            tblCmpEipMap.setPortId(tblCmpPorts.get(0).getPortId());
                        }
                        else
                        {
                            tblCmpEipMap.setPortId(null);
                        }

                        if (StringUtils.isEmpty(tblCmpEipMap.getPortId()))
                        {
                            if (StringUtils.isNotEmpty(eipPortMapRspVo.getInstanceId()))
                            {
                                if (eipPortMapRspVo.getVm())
                                {
                                    TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, eipPortMapRspVo.getInstanceId());
                                    if (tblCmpVmInstance != null)
                                    {
                                        tblCmpEipMap.setPortId(tblCmpVmInstance.getPortId());
                                    }
                                }
                                else
                                {
                                    TblCmpBaremetalInstance tblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, eipPortMapRspVo.getInstanceId());
                                    if (tblCmpBaremetalInstance != null)
                                    {
                                        tblCmpEipMap.setPortId(tblCmpBaremetalInstance.getPortIdFromAgent());
                                    }
                                }
                            }
                        }

                        tblCmpEipMap.setIsStaticIp(null);
                        tblCmpEipMap.setInsideIp(eipPortMapRspVo.getInsideIp());
                        tblCmpEipMap.setStatus(eipPortMapRspVo.getPhaseStatus() == null ? null : eipPortMapRspVo.getPhaseStatus().shortValue());
                        tblCmpEipMap.setBandwidth(eipPortMapRspVo.getBandwidth());
                        tblCmpEipMap.setIsOneToOne(eipPortMapRspVo.getOneToOne());
                        tblCmpEipMap.setCreateTime(eipPortMapRspVo.getCreateTime() == null ? null : Utils.parseStTime(eipPortMapRspVo.getCreateTime()));
                        tblCmpEipMap.setUpdateTime(null);
                        tblCmpEipMap.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createEipMap(tblCmpEipMap);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateEipMap(tblCmpEipMap);
                        }

                        eipMapIds.remove(eipPortMapRspVo.getEipMapId());

                        Set<String> portMapIds = networkRepository.getPortMapIds(cloudId, eipPortMapRspVo.getEipMapId());
                        if (portMapIds == null)
                        {
                            portMapIds = new HashSet<>();
                        }

                        if (! CollectionUtils.isEmpty(eipPortMapRspVo.getPortMaps()))
                        {
                            for (EipPortMapCreateReqVo.portMap portMap : eipPortMapRspVo.getPortMaps())
                            {
                                TblCmpPortMap tblCmpPortMap = new TblCmpPortMap();
                                tblCmpPortMap.setCloudId(cloudId);
                                tblCmpPortMap.setPortMapId(portMap.getPortMapId());
                                tblCmpPortMap.setGlobalPort(portMap.getGlobalPort());
                                tblCmpPortMap.setLocalPort(portMap.getLocalPort());
                                tblCmpPortMap.setProtocol(portMap.getProtocol() == null ? null : portMap.getProtocol().shortValue());
                                tblCmpPortMap.setCreateTime(portMap.getCreateTime() == null ? null : Utils.parseStTime(portMap.getCreateTime()));
                                tblCmpPortMap.setUpdateTime(portMap.getUpdateTime() == null ? null : Utils.parseStTime(portMap.getUpdateTime()));
                                tblCmpPortMap.setEipMapId(eipPortMapRspVo.getEipMapId());
                                tblCmpPortMap.setEeStatus(SYNCING);
                                try
                                {
                                    networkRepository.createPortMap(tblCmpPortMap);
                                } catch (DuplicateKeyException e)
                                {
                                    networkRepository.updatePortMap(tblCmpPortMap);
                                }

                                portMapIds.remove(portMap.getPortMapId());
                            }
                        }

                        if (portMapIds.size() > 0)
                        {
                            for (String portMapId : portMapIds)
                            {
                                TblCmpPortMap tblCmpPortMap = new TblCmpPortMap();
                                tblCmpPortMap.setCloudId(cloudId);
                                tblCmpPortMap.setPortMapId(portMapId);
                                tblCmpPortMap.setEeStatus(REMOVED);

                                networkRepository.updatePortMap(tblCmpPortMap);
                            }
                        }
                    }
                    total = getEipPortMapsRsp.getTotalNum();
                }
            }

            if (eipMapIds.size() > 0)
            {
                for (String eipMapId : eipMapIds)
                {
                    TblCmpEipMap tblCmpEipMap = new TblCmpEipMap();
                    tblCmpEipMap.setCloudId(cloudId);
                    tblCmpEipMap.setEipMapId(eipMapId);
                    tblCmpEipMap.setEeStatus(REMOVED);

                    networkRepository.updateEipMap(tblCmpEipMap);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} port maps error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPortMap(String cloudId, String eipMapId)
    {
        String url = "/api/network/v1/portMaps/" + eipMapId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("eipMap does not exists"))
                {
                    TblCmpEipMap tblCmpEipMap = networkRepository.getEipMapById(cloudId, eipMapId);
                    if (tblCmpEipMap != null)
                    {
                        tblCmpEipMap.setEeStatus(REMOVED);
                        networkRepository.updateEipMap(tblCmpEipMap);
                    }
                    return;
                }

                EipPortMapRspVo eipPortMapRspVo = gson.fromJson(result, EipPortMapRspVo.class);
                if (eipPortMapRspVo == null)
                {
                    return;
                }

                TblCmpEipMap tblCmpEipMap = new TblCmpEipMap();
                tblCmpEipMap.setCloudId(cloudId);
                tblCmpEipMap.setEipMapId(eipPortMapRspVo.getEipMapId());
                tblCmpEipMap.setMapName(eipPortMapRspVo.getMapName());
                tblCmpEipMap.setEipId(eipPortMapRspVo.getEipId());
                tblCmpEipMap.setSubnetId(eipPortMapRspVo.getSubnetId());
                tblCmpEipMap.setUserId(eipPortMapRspVo.getUserId());

                TblCmpPortExample tblCmpPortExample = new TblCmpPortExample();
                TblCmpPortExample.Criteria criteria = tblCmpPortExample.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andEeStatusNotEqualTo(REMOVED);
                criteria.andInstanceIdEqualTo(eipPortMapRspVo.getInstanceId());
                criteria.andSubnetIdEqualTo(eipPortMapRspVo.getSubnetId());
                criteria.andEipIdEqualTo(eipPortMapRspVo.getEipId());
                List<TblCmpPort> tblCmpPorts = networkRepository.getPorts(tblCmpPortExample);
                if (! CollectionUtils.isEmpty(tblCmpPorts))
                {
                    tblCmpEipMap.setPortId(tblCmpPorts.get(0).getPortId());
                }
                else
                {
                    tblCmpEipMap.setPortId(null);
                }

                if (StringUtils.isEmpty(tblCmpEipMap.getPortId()))
                {
                    if (StringUtils.isNotEmpty(eipPortMapRspVo.getInstanceId()))
                    {
                        if (eipPortMapRspVo.getVm())
                        {
                            TblCmpVmInstance tblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, eipPortMapRspVo.getInstanceId());
                            if (tblCmpVmInstance != null)
                            {
                                tblCmpEipMap.setPortId(tblCmpVmInstance.getPortId());
                            }
                        }
                        else
                        {
                            TblCmpBaremetalInstance tblCmpBaremetalInstance = baremetalComputeRepository.getBaremetalInstanceById(cloudId, eipPortMapRspVo.getInstanceId());
                            if (tblCmpBaremetalInstance != null)
                            {
                                tblCmpEipMap.setPortId(tblCmpBaremetalInstance.getPortIdFromAgent());
                            }
                        }
                    }
                }

                tblCmpEipMap.setIsStaticIp(null);
                tblCmpEipMap.setInsideIp(eipPortMapRspVo.getInsideIp());
                tblCmpEipMap.setStatus(eipPortMapRspVo.getPhaseStatus() == null ? null : eipPortMapRspVo.getPhaseStatus().shortValue());
                tblCmpEipMap.setBandwidth(eipPortMapRspVo.getBandwidth());
                tblCmpEipMap.setIsOneToOne(eipPortMapRspVo.getOneToOne());
                tblCmpEipMap.setCreateTime(eipPortMapRspVo.getCreateTime() == null ? null : Utils.parseStTime(eipPortMapRspVo.getCreateTime()));
                tblCmpEipMap.setUpdateTime(null);
                tblCmpEipMap.setEeStatus(SYNCING);

                try
                {
                    networkRepository.createEipMap(tblCmpEipMap);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateEipMap(tblCmpEipMap);
                }

                Set<String> portMapIds = networkRepository.getPortMapIds(cloudId, eipPortMapRspVo.getEipMapId());
                if (portMapIds == null)
                {
                    portMapIds = new HashSet<>();
                }

                if (! CollectionUtils.isEmpty(eipPortMapRspVo.getPortMaps()))
                {
                    for (EipPortMapCreateReqVo.portMap portMap : eipPortMapRspVo.getPortMaps())
                    {
                        TblCmpPortMap tblCmpPortMap = new TblCmpPortMap();
                        tblCmpPortMap.setCloudId(cloudId);
                        tblCmpPortMap.setPortMapId(portMap.getPortMapId());
                        tblCmpPortMap.setGlobalPort(portMap.getGlobalPort());
                        tblCmpPortMap.setLocalPort(portMap.getLocalPort());
                        tblCmpPortMap.setProtocol(portMap.getProtocol() == null ? null : portMap.getProtocol().shortValue());
                        tblCmpPortMap.setCreateTime(portMap.getCreateTime() == null ? null : Utils.parseStTime(portMap.getCreateTime()));
                        tblCmpPortMap.setUpdateTime(portMap.getUpdateTime() == null ? null : Utils.parseStTime(portMap.getUpdateTime()));
                        tblCmpPortMap.setEeStatus(SYNCING);
                        tblCmpPortMap.setEipMapId(eipMapId);
                        try
                        {
                            networkRepository.createPortMap(tblCmpPortMap);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updatePortMap(tblCmpPortMap);
                        }

                        portMapIds.remove(portMap.getPortMapId());
                    }
                }

                if (portMapIds.size() > 0)
                {
                    for (String portMapId : portMapIds)
                    {
                        TblCmpPortMap tblCmpPortMap = new TblCmpPortMap();
                        tblCmpPortMap.setCloudId(cloudId);
                        tblCmpPortMap.setPortMapId(portMapId);
                        tblCmpPortMap.setEeStatus(REMOVED);

                        networkRepository.updatePortMap(tblCmpPortMap);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} port map {} error msg: {}", cloudId, eipMapId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncEipPools(String cloudId)
    {
        String urlPattern = "/api/network/v1/eip_pools?page_num=%s&page_size=100";
        try
        {
            Set<String> poolIds = networkRepository.getPoolIds(cloudId);
            if (poolIds == null)
            {
                poolIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    EipPoolsRspVo getEipPoolsRsp = gson.fromJson(result, EipPoolsRspVo.class);
                    if (getEipPoolsRsp == null || CollectionUtils.isEmpty(getEipPoolsRsp.getEipPools()))
                    {
                        return;
                    }

                    for (TblCmpEipPool tblCmpEipPool : getEipPoolsRsp.getEipPools())
                    {
                        tblCmpEipPool.setCloudId(cloudId);
                        tblCmpEipPool.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createEipPool(tblCmpEipPool);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateEipPool(tblCmpEipPool);
                        }

                        poolIds.remove(tblCmpEipPool.getPoolId());
                    }
                    total = getEipPoolsRsp.getTotalNum();
                }
            }

            if (poolIds.size() > 0)
            {
                for (String poolId : poolIds)
                {
                    TblCmpEipPool tblCmpEipPool = new TblCmpEipPool();
                    tblCmpEipPool.setCloudId(cloudId);
                    tblCmpEipPool.setPoolId(poolId);
                    tblCmpEipPool.setEeStatus(REMOVED);

                    networkRepository.updateEipPool(tblCmpEipPool);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} eip pools error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncEipPool(String cloudId, String eipPoolId)
    {
        String url = "/api/network/v1/eip_pools/" + eipPoolId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("eip pool does not exists"))
                {
                    TblCmpEipPool tblCmpEipPool = networkRepository.getEipPoolById(cloudId, eipPoolId);
                    if (tblCmpEipPool != null)
                    {
                        tblCmpEipPool.setEeStatus(REMOVED);
                        networkRepository.updateEipPool(tblCmpEipPool);
                    }
                    return;
                }

                EipPoolDetailInfoRspVo eipPoolDetailInfoRspVo = gson.fromJson(result, EipPoolDetailInfoRspVo.class);
                if (eipPoolDetailInfoRspVo == null)
                {
                    return;
                }

                TblCmpEipPool tblCmpEipPool = new TblCmpEipPool();
                tblCmpEipPool.setCloudId(cloudId);
                tblCmpEipPool.setEeStatus(SYNCING);
                tblCmpEipPool.setPoolId(eipPoolId);
                tblCmpEipPool.setName(eipPoolDetailInfoRspVo.getName());
                tblCmpEipPool.setDescription(eipPoolDetailInfoRspVo.getDescription());
                tblCmpEipPool.setPhaseStatus(eipPoolDetailInfoRspVo.getPhaseStatus());
                tblCmpEipPool.setCreateTime(eipPoolDetailInfoRspVo.getCreateTime() == null ? null : Utils.parseStTime(eipPoolDetailInfoRspVo.getCreateTime()));
                tblCmpEipPool.setUpdateTime(eipPoolDetailInfoRspVo.getUpdateTime() == null ? null : Utils.parseStTime(eipPoolDetailInfoRspVo.getUpdateTime()));

                try
                {
                    networkRepository.createEipPool(tblCmpEipPool);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateEipPool(tblCmpEipPool);
                }

                TblCmpEipPoolVpcRefExample example = new TblCmpEipPoolVpcRefExample();
                TblCmpEipPoolVpcRefExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(cloudId);
                criteria.andEeStatusNotEqualTo(REMOVED);
                criteria.andPoolIdEqualTo(eipPoolId);
                List<TblCmpEipPoolVpcRef> tblCmpEipPoolVpcRefs = networkRepository.getEipPoolAndVpcRefs(example);
                boolean contain = false;
                for (TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef : tblCmpEipPoolVpcRefs)
                {
                    if (eipPoolDetailInfoRspVo.getVlanId() != null && Objects.equals(eipPoolDetailInfoRspVo.getVlanId(), tblCmpEipPoolVpcRef.getVlanId()))
                    {
                        contain = true;
                        continue;
                    }
                    tblCmpEipPoolVpcRef.setEeStatus(REMOVED);
                    networkRepository.updateEipPoolAndVpcRef(tblCmpEipPoolVpcRef);
                }
                if (eipPoolDetailInfoRspVo.getVlanId() != null && eipPoolDetailInfoRspVo.getVlanId() != 0 && !contain)
                {
                    TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef = new TblCmpEipPoolVpcRef();
                    tblCmpEipPoolVpcRef.setCloudId(cloudId);
                    tblCmpEipPoolVpcRef.setEeStatus(SYNCING);
                    tblCmpEipPoolVpcRef.setPoolVpcId(Utils.assignUUId());
                    tblCmpEipPoolVpcRef.setVlanId(eipPoolDetailInfoRspVo.getVlanId());
                    tblCmpEipPoolVpcRef.setPoolId(eipPoolId);
                    try
                    {
                        networkRepository.createEipPoolAndVpcRef(tblCmpEipPoolVpcRef);
                    } catch (DuplicateKeyException e)
                    {
                        networkRepository.updateEipPoolAndVpcRef(tblCmpEipPoolVpcRef);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} eip pool {} error msg: {}", cloudId, eipPoolId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSecurityGroups(String cloudId)
    {
        String urlPattern = "/api/network/v1/sgs?page_num=%s&page_size=100";
        try
        {
            Set<String> sgIds = networkRepository.getSgIds(cloudId);
            Set<String> sgIdSet = new HashSet<>();
            if (sgIds == null)
            {
                sgIds = new HashSet<>();
            }

            int pageNum = 0;
            long total = 100;
            while ((pageNum * 100L) < total)
            {
                pageNum = pageNum + 1;
                String result = getDataFromCloud(cloudId, String.format(urlPattern, pageNum));
                if (StringUtils.isNotEmpty(result))
                {
                    SecurityGroupsRspVo getSecurityGroupsRsp = gson.fromJson(result, SecurityGroupsRspVo.class);
                    if (getSecurityGroupsRsp == null || CollectionUtils.isEmpty(getSecurityGroupsRsp.getSecurityGroups()))
                    {
                        return;
                    }

                    for (SecurityGroupRspVo securityGroup : getSecurityGroupsRsp.getSecurityGroups())
                    {
                        TblCmpSecurityGroup tblCmpSecurityGroup = new TblCmpSecurityGroup();
                        tblCmpSecurityGroup.setCloudId(cloudId);
                        tblCmpSecurityGroup.setSgId(securityGroup.getSgId());
                        tblCmpSecurityGroup.setName(securityGroup.getName());
                        tblCmpSecurityGroup.setPhaseStatus(securityGroup.getPhaseStatus());
                        tblCmpSecurityGroup.setDescription(securityGroup.getDescription());
                        tblCmpSecurityGroup.setUserId(null);
                        tblCmpSecurityGroup.setCreateTime(null);
                        tblCmpSecurityGroup.setUpdateTime(null);
                        tblCmpSecurityGroup.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createSecurityGroup(tblCmpSecurityGroup);
                        } catch (DuplicateKeyException e)
                        {
                            networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
                        }

                        sgIds.remove(securityGroup.getSgId());
                        sgIdSet.add(securityGroup.getSgId());
                    }
                    total = getSecurityGroupsRsp.getTotalNum();
                }
            }

            if (sgIds.size() > 0)
            {
                for (String sgId : sgIds)
                {
                    TblCmpSecurityGroup tblCmpSecurityGroup = new TblCmpSecurityGroup();
                    tblCmpSecurityGroup.setCloudId(cloudId);
                    tblCmpSecurityGroup.setSgId(sgId);
                    tblCmpSecurityGroup.setEeStatus(REMOVED);

                    networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
                }
            }

            if (sgIdSet.size() > 0)
            {
                for (String sgId : sgIdSet)
                {
                    syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} security groups error msg: {}", cloudId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSecurityGroup(String cloudId, String sgId)
    {
        String url = "/api/network/v1/sgs/" + sgId;
        try
        {
            String result = getDataFromCloud(cloudId, url);
            if (StringUtils.isNotEmpty(result))
            {
                if (result.contains("security group does not exists"))
                {
                    TblCmpSecurityGroup tblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);
                    if (tblCmpSecurityGroup != null && tblCmpSecurityGroup.getEeStatus() == SYNCING)
                    {
                        tblCmpSecurityGroup.setEeStatus(REMOVED);
                        networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
                    }
                    return;
                }

                GetSecurityGroupDetailInfoRsp getSecurityGroupDetailInfoRsp = gson.fromJson(result, GetSecurityGroupDetailInfoRsp.class);
                if (getSecurityGroupDetailInfoRsp == null)
                {
                    return;
                }

                TblCmpSecurityGroup tblCmpSecurityGroup = new TblCmpSecurityGroup();
                tblCmpSecurityGroup.setCloudId(cloudId);
                tblCmpSecurityGroup.setSgId(sgId);
                tblCmpSecurityGroup.setName(getSecurityGroupDetailInfoRsp.getName());
                tblCmpSecurityGroup.setPhaseStatus(getSecurityGroupDetailInfoRsp.getPhaseStatus());
                tblCmpSecurityGroup.setDescription(getSecurityGroupDetailInfoRsp.getDescription());
                tblCmpSecurityGroup.setUserId(null);
                tblCmpSecurityGroup.setCreateTime(null);
                tblCmpSecurityGroup.setUpdateTime(null);
                tblCmpSecurityGroup.setEeStatus(SYNCING);

                try
                {
                    networkRepository.createSecurityGroup(tblCmpSecurityGroup);
                } catch (DuplicateKeyException e)
                {
                    networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
                }

                Set<String> ruleIds = getSgRuleIdsBySgId(cloudId, sgId);

                if (! CollectionUtils.isEmpty(getSecurityGroupDetailInfoRsp.getRules()))
                {
                    for (RpcSecurityGroupRule securityGroupRule : getSecurityGroupDetailInfoRsp.getRules())
                    {
                        TblCmpSecurityGroupRule tblCmpSecurityGroupRule = new TblCmpSecurityGroupRule();
                        tblCmpSecurityGroupRule.setCloudId(cloudId);
                        tblCmpSecurityGroupRule.setRuleId(securityGroupRule.getRuleId());
                        tblCmpSecurityGroupRule.setSgId(sgId);
                        tblCmpSecurityGroupRule.setPhaseStatus(null);
                        tblCmpSecurityGroupRule.setPriority(securityGroupRule.getPriority() == null ? null : securityGroupRule.getPriority().shortValue());
                        tblCmpSecurityGroupRule.setDirection(securityGroupRule.getDirection() == null ? null : securityGroupRule.getDirection().shortValue());
                        tblCmpSecurityGroupRule.setProtocol(securityGroupRule.getProtocol() == null ? null : securityGroupRule.getProtocol().shortValue());
                        tblCmpSecurityGroupRule.setPort(securityGroupRule.getPort());
                        tblCmpSecurityGroupRule.setCidr(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getCidr());
                        tblCmpSecurityGroupRule.setSgIdReference(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getSgId());
                        tblCmpSecurityGroupRule.setPoolId(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getIpPoolId());
                        tblCmpSecurityGroupRule.setDescription(securityGroupRule.getDescription());
                        tblCmpSecurityGroupRule.setAddressType(securityGroupRule.getAddressType() == null ? null : securityGroupRule.getAddressType().shortValue());
                        tblCmpSecurityGroupRule.setAction(securityGroupRule.getAction() == null ? null : securityGroupRule.getAction().shortValue());
                        tblCmpSecurityGroupRule.setCreateTime(securityGroupRule.getCreateTime() == null ? null : Utils.parseStTime(securityGroupRule.getCreateTime()));
                        tblCmpSecurityGroupRule.setUpdateTime(securityGroupRule.getUpdateTime() == null ? null : Utils.parseStTime(securityGroupRule.getUpdateTime()));
                        tblCmpSecurityGroupRule.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.createSecurityGroupRule(tblCmpSecurityGroupRule);
                        }
                        catch (DuplicateKeyException e)
                        {
                            boolean forceUpdate = false;
                            if (tblCmpSecurityGroupRule.getCidr() == null)
                            {
                                forceUpdate = true;
                            }
                            if (tblCmpSecurityGroupRule.getSgIdReference() == null)
                            {
                                forceUpdate = true;
                            }
                            if (forceUpdate)
                            {
                                TblCmpSecurityGroupRule updateTblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, securityGroupRule.getRuleId());
                                if (updateTblCmpSecurityGroupRule != null)
                                {
                                    updateTblCmpSecurityGroupRule.setSgId(tblCmpSecurityGroupRule.getSgId());
                                    updateTblCmpSecurityGroupRule.setPriority(securityGroupRule.getPriority() == null ? null : securityGroupRule.getPriority().shortValue());
                                    updateTblCmpSecurityGroupRule.setDirection(securityGroupRule.getDirection() == null ? null : securityGroupRule.getDirection().shortValue());
                                    updateTblCmpSecurityGroupRule.setProtocol(securityGroupRule.getProtocol() == null ? null : securityGroupRule.getProtocol().shortValue());
                                    updateTblCmpSecurityGroupRule.setPort(securityGroupRule.getPort());
                                    updateTblCmpSecurityGroupRule.setCidr(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getCidr());
                                    updateTblCmpSecurityGroupRule.setSgIdReference(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getSgId());
                                    updateTblCmpSecurityGroupRule.setPoolId(securityGroupRule.getAddressRef() == null ? null : securityGroupRule.getAddressRef().getIpPoolId());
                                    updateTblCmpSecurityGroupRule.setDescription(securityGroupRule.getDescription());
                                    updateTblCmpSecurityGroupRule.setAddressType(securityGroupRule.getAddressType() == null ? null : securityGroupRule.getAddressType().shortValue());
                                    updateTblCmpSecurityGroupRule.setAction(securityGroupRule.getAction() == null ? null : securityGroupRule.getAction().shortValue());

                                    networkRepository.updateSecurityGroupRule(updateTblCmpSecurityGroupRule);
                                }
                                else
                                {
                                    networkRepository.updateSecurityGroupRuleSelective(tblCmpSecurityGroupRule);
                                }
                            }
                            else
                            {
                                networkRepository.updateSecurityGroupRuleSelective(tblCmpSecurityGroupRule);
                            }
                        }

                        ruleIds.remove(securityGroupRule.getRuleId());
                    }
                }

                Set<String> sgVmInstanceIds = networkRepository.getSgVmInstanceIdsBySgIds(cloudId, sgId);

                for (GetSecurityGroupDetailInfoRsp.VmInstance vmInstance : getSecurityGroupDetailInfoRsp.getVmInstances())
                {
                    TblCmpVmInstance tempTblCmpVmInstance = vmComputeRepository.getVmInstanceById(cloudId, vmInstance.getInstanceId());

                    TblCmpSgVmInstanceExample example = new TblCmpSgVmInstanceExample();
                    TblCmpSgVmInstanceExample.Criteria criteria = example.createCriteria();
                    criteria.andInstanceIdEqualTo(vmInstance.getInstanceId());
                    criteria.andCloudIdEqualTo(cloudId);
                    criteria.andEeStatusNotEqualTo(REMOVED);
                    criteria.andSgIdEqualTo(sgId);
                    List<TblCmpSgVmInstance> tblCmpSgVmInstances = networkRepository.getSgVmInstances(example);
                    if (! CollectionUtils.isEmpty(tblCmpSgVmInstances))
                    {
                        for (TblCmpSgVmInstance tblCmpSgVmInstance : tblCmpSgVmInstances)
                        {
                            if ((tempTblCmpVmInstance == null && (tblCmpSgVmInstance.getEeBp() != null || tblCmpSgVmInstance.getEeUser() != null)) ||
                                    (tempTblCmpVmInstance != null && ((tempTblCmpVmInstance.getEeBp() == null && tblCmpSgVmInstance.getEeBp() != null) ||
                                            (tempTblCmpVmInstance.getEeBp() != null && !tempTblCmpVmInstance.getEeBp().equals(tblCmpSgVmInstance.getEeBp()))) ||
                                            (tempTblCmpVmInstance.getEeUser() == null && tblCmpSgVmInstance.getEeUser() != null) ||
                                            (tempTblCmpVmInstance.getEeUser() != null && !tempTblCmpVmInstance.getEeUser().equals(tblCmpSgVmInstance.getEeUser()))))
                            {
                                tblCmpSgVmInstance.setEeBp(tempTblCmpVmInstance.getEeBp());
                                tblCmpSgVmInstance.setEeUser(tempTblCmpVmInstance.getEeUser());
                                networkRepository.updateSgVmInstance(tblCmpSgVmInstance);
                            }
                            sgVmInstanceIds.remove(tblCmpSgVmInstance.getSgVmId());
                        }
                    }
                    else
                    {
                        TblCmpSgVmInstance tblCmpSgVmInstance = new TblCmpSgVmInstance();
                        tblCmpSgVmInstance.setCloudId(cloudId);
                        tblCmpSgVmInstance.setInstanceId(vmInstance.getInstanceId());
                        tblCmpSgVmInstance.setSgId(sgId);
                        tblCmpSgVmInstance.setEeStatus(SYNCING);
                        tblCmpSgVmInstance.setSgVmId(Utils.assignUUId());
                        if (tempTblCmpVmInstance != null)
                        {
                            tblCmpSgVmInstance.setEeBp(tempTblCmpVmInstance.getEeBp());
                            tblCmpSgVmInstance.setEeUser(tempTblCmpVmInstance.getEeUser());
                        }

                        try
                        {
                            networkRepository.createSgVmInstance(tblCmpSgVmInstance);
                        }
                        catch (DuplicateKeyException e)
                        {
                            networkRepository.updateSgVmInstanceSelective(tblCmpSgVmInstance);
                        }
                    }
                }

                if (! CollectionUtils.isEmpty(ruleIds))
                {
                    for (String ruleId : ruleIds)
                    {
                        TblCmpSecurityGroupRule tblCmpSecurityGroupRule = new TblCmpSecurityGroupRule();
                        tblCmpSecurityGroupRule.setCloudId(cloudId);
                        tblCmpSecurityGroupRule.setRuleId(ruleId);
                        tblCmpSecurityGroupRule.setEeStatus(REMOVED);
                        networkRepository.updateSecurityGroupRule(tblCmpSecurityGroupRule);
                    }
                }

                if (! CollectionUtils.isEmpty(sgVmInstanceIds))
                {
                    for (String sgVmInstanceId : sgVmInstanceIds)
                    {
                        networkRepository.deleteSgVmInstanceByPrimaryKey(new TblCmpSgVmInstanceKey(cloudId, sgVmInstanceId));
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} security group {} error msg: {}", cloudId, sgId, e.getMessage());
            e.printStackTrace();
        }
    }

    private Set<String> getSgRuleIdsBySgId(String cloudId, String sgId)
    {
        Set<String> ruleIds = new HashSet<>();
        try
        {
            TblCmpSecurityGroupRuleExample example = new TblCmpSecurityGroupRuleExample();
            TblCmpSecurityGroupRuleExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andSgIdEqualTo(sgId);
            List<TblCmpSecurityGroupRule> rules = networkRepository.getSecurityGroupRules(example);

            if (!CollectionUtils.isEmpty(rules))
            {
                rules.forEach(rule -> ruleIds.add(rule.getRuleId()));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get security group rule ids by security group id {} error msg: {}", sgId, e.getMessage());
            e.printStackTrace();
        }
        return ruleIds;
    }

    public String getDataFromCloud(String cloudId, String path)
    {
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
        if (tblCloudInfo == null)
        {
            throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
        }
        if (tblCloudInfo.getMode().equals(Mode.DIRECT))
        {
            RestTemplate restTemplate;
            String url;
            try {
                String cloudUrl = tblCloudInfo.getUrl().toLowerCase(Locale.ROOT);
                if (cloudUrl.startsWith("https://"))
                {
                    SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
                            SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
                            NoopHostnameVerifier.INSTANCE);
                    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
                    HttpComponentsClientHttpRequestFactory requestFactory =
                            new HttpComponentsClientHttpRequestFactory();
                    requestFactory.setHttpClient(httpClient);
                    restTemplate = new RestTemplate(requestFactory);
                    url = Utils.buildStr(tblCloudInfo.getUrl(), path);
                }
                else if (cloudUrl.startsWith("http://"))
                {
                    restTemplate = new RestTemplate();
                    url = Utils.buildStr(tblCloudInfo.getUrl(), path);
                }
                else
                {
                    restTemplate = new RestTemplate();
                    url = Utils.buildStr("http://", tblCloudInfo.getUrl(), path);
                }

                HttpHeaders requestHeaders = new HttpHeaders();

                Authorization authorization = gson.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

                if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
                {
                    requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
                    requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
                }
                requestHeaders.set("Connection", "close");

                HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
                restTemplate.setErrorHandler(new CustomErrorHandler());

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            RestTemplate restTemplate = new RestTemplate();
            try {
                HttpHeaders requestHeaders = new HttpHeaders();

                path = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/",
                        CloudProduct.fromName(tblCloudInfo.getProduct()).getShortName(), "/clouds/", cloudId, path);

                requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
                requestHeaders.set("Connection", "close");

                HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
                restTemplate.setErrorHandler(new CustomErrorHandler());
                ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, requestEntity, String.class);
                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void syncSingleData(String cloudId, String resourceId, String msgType)
    {
        SyncResourceInfo syncResourceInfo = new SyncResourceInfo(cloudId, resourceId, msgType, null);
        cloudProcessStrategy.syncSingleData(syncResourceInfo);
    }
}
