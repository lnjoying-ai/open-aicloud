package com.lnjoying.justice.cmp.service.syncdata;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.*;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSBlockDeviceMappingCreate;
import com.lnjoying.justice.cmp.domain.dto.response.easystack.neturon.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImageInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.keystone.OSProjectsWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class ESKSyncDataService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NSKSyncDataService.class);

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").serializeNulls().create();
    private static Gson cinderGson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private OSNovaRepository osNovaRepository;

    @Autowired
    private OSNeutronRepository osNeutronRepository;

    @Autowired
    private OSCinderRepository osCinderRepository;

    @Autowired
    private OSKeystoneRepository osKeystoneRepository;

    @Autowired
    private OSGlanceRepository osGlanceRepository;

    @Autowired
    private ESNeutronRepository esNeutronRepository;

    @Autowired
    private CloudService cloudService;

    public void syncData(String cloudId)
    {
        TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

        syncDomains(tblCloudInfo);
        syncProjects(tblCloudInfo);

        syncImages(tblCloudInfo);

        syncFlavors(tblCloudInfo);
        syncKeypairs(tblCloudInfo);
        syncServers(tblCloudInfo);

        syncNetworks(tblCloudInfo);
        syncNetworksegments(tblCloudInfo, null);
        syncPorts(tblCloudInfo, null);
        syncSubnets(tblCloudInfo);
        syncRouters(tblCloudInfo);
        syncFloatingIps(tblCloudInfo);
        syncSecurityGroups(tblCloudInfo);
        syncSecurityGroupRules(tblCloudInfo, null);

        syncFirewalls(tblCloudInfo);
        syncFirewallRules(tblCloudInfo);
        syncFirewallPolicies(tblCloudInfo);

        syncVolumes(tblCloudInfo);
        syncSnapshots(tblCloudInfo);
        syncVolumeTypes(tblCloudInfo);
        syncBackups(tblCloudInfo);
    }

    public void syncDomains(TblCloudInfo tblCloudInfo)
    {
        String url = "/v3/domains";
        try
        {
            Set<String> domainIds = osKeystoneRepository.getProjectIds(tblCloudInfo.getCloudId(), (short)1);
            if (domainIds == null)
            {
                domainIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSProjectsWithDetailsRsp getDomainsRsp = gson.fromJson(result, OSProjectsWithDetailsRsp.class);
                if (getDomainsRsp != null && ! CollectionUtils.isEmpty(getDomainsRsp.getDomains()))
                {
                    for (OSProjectsWithDetailsRsp.OSDomainInfo osDomainInfo : getDomainsRsp.getDomains())
                    {
                        updateProjectToDB(tblCloudInfo.getCloudId(), osDomainInfo, null);
                        domainIds.remove(osDomainInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(domainIds))
            {
                for (String domainId : domainIds)
                {
                    TblCmpOsProject tblCmpOsProject = new TblCmpOsProject();
                    tblCmpOsProject.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsProject.setId(domainId);
                    tblCmpOsProject.setEeStatus(REMOVED);

                    osKeystoneRepository.updateProject(tblCmpOsProject);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} domains error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncProjects(TblCloudInfo tblCloudInfo)
    {
        String url = "/v3/projects";
        try
        {
            Set<String> projectIds = osKeystoneRepository.getProjectIds(tblCloudInfo.getCloudId(), (short)0);
            if (projectIds == null)
            {
                projectIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSProjectsWithDetailsRsp getProjectsRsp = gson.fromJson(result, OSProjectsWithDetailsRsp.class);
                if (getProjectsRsp != null && ! CollectionUtils.isEmpty(getProjectsRsp.getProjects()))
                {
                    for (OSProjectsWithDetailsRsp.OSProjectInfo projectInfo : getProjectsRsp.getProjects())
                    {
                        updateProjectToDB(tblCloudInfo.getCloudId(), null, projectInfo);
                        projectIds.remove(projectInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(projectIds))
            {
                for (String projectId : projectIds)
                {
                    TblCmpOsProject tblCmpOsProject = new TblCmpOsProject();
                    tblCmpOsProject.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsProject.setId(projectId);
                    tblCmpOsProject.setEeStatus(REMOVED);

                    osKeystoneRepository.updateProject(tblCmpOsProject);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} projects error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateProjectToDB(String cloudId, OSProjectsWithDetailsRsp.OSDomainInfo osDomainInfo, OSProjectsWithDetailsRsp.OSProjectInfo projectInfo)
    {
        TblCmpOsProject tblCmpOsProject = new TblCmpOsProject();
        tblCmpOsProject.setCloudId(cloudId);
        tblCmpOsProject.setEeStatus(SYNCING);
        if (osDomainInfo != null)
        {
            tblCmpOsProject.setId(osDomainInfo.getId());
            tblCmpOsProject.setName(osDomainInfo.getName());
            tblCmpOsProject.setDescription(osDomainInfo.getDescription());
            tblCmpOsProject.setEnabled(BoolUtil.bool2Short(osDomainInfo.getEnabled()));
            tblCmpOsProject.setIsDomain((short)1);
        }
        else if (projectInfo != null)
        {
            tblCmpOsProject.setId(projectInfo.getId());
            tblCmpOsProject.setName(projectInfo.getName());
            tblCmpOsProject.setDescription(projectInfo.getDescription());
            tblCmpOsProject.setEnabled(BoolUtil.bool2Short(projectInfo.getEnabled()));
            tblCmpOsProject.setDomainId(projectInfo.getDomainId());
            tblCmpOsProject.setParentId(projectInfo.getParentId());
            tblCmpOsProject.setIsDomain((short)0);
        }

        try
        {
            osKeystoneRepository.insertProject(tblCmpOsProject);
        }
        catch (DuplicateKeyException e)
        {
            osKeystoneRepository.updateProject(tblCmpOsProject);
        }
    }

    public void syncImages(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2/images?limit=100";
        String urlPattern = "/v2/images?marker=%s&limit=100";
        String marker = null;
        int pageSize = 100;
        try
        {
            Set<String> imageIds = osGlanceRepository.getImageIds(tblCloudInfo.getCloudId());
            if (imageIds == null)
            {
                imageIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            int total = pageSize;
            while (total == pageSize)
            {
                total = 0;
                if (marker != null ) url = String.format(urlPattern, marker);
                ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
                if (response.getStatusCode() != HttpStatus.OK) return;
                String result = response.getBody();
                if (StringUtils.isNotEmpty(result))
                {
                    OSImagesWithDetailsRsp osImagesWithDetailsRsp = gson.fromJson(result, OSImagesWithDetailsRsp.class);
                    if (osImagesWithDetailsRsp != null && ! CollectionUtils.isEmpty(osImagesWithDetailsRsp.getImages()))
                    {
                        total = osImagesWithDetailsRsp.getImages().size();
                        marker = osImagesWithDetailsRsp.getImages().get(total - 1).getId();
                        for (OSImageInfo imageInfo : osImagesWithDetailsRsp.getImages())
                        {
                            updateImageToDB(tblCloudInfo.getCloudId(), imageInfo, null, null);
                            imageIds.remove(imageInfo.getId());
                        }
                    }
                }
            }

            if (! CollectionUtils.isEmpty(imageIds))
            {
                for (String imageId : imageIds)
                {
                    TblCmpOsImages tblCmpOsImage = new TblCmpOsImages();
                    tblCmpOsImage.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsImage.setId(imageId);
                    tblCmpOsImage.setEeStatus(REMOVED);

                    osGlanceRepository.updateImages(tblCmpOsImage);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} images error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncImage(TblCloudInfo tblCloudInfo, String imageId)
    {
        String urlPattern = "/v2/images/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, imageId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osGlanceRepository.deleteImage(tblCloudInfo.getCloudId(), imageId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSImageInfo osImage = gson.fromJson(result, OSImageInfo.class);
                if (osImage == null)
                {
                    osGlanceRepository.deleteImage(tblCloudInfo.getCloudId(), imageId);
                    return;
                }

                updateImageToDB(tblCloudInfo.getCloudId(), osImage, null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} image {} error msg: {}", tblCloudInfo.getName(), imageId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateImageToDB(String cloudId, OSImageInfo imageInfo, String eeBp, String eeUser)
    {
        TblCmpOsImages tblCmpOsImage = new TblCmpOsImages();
        tblCmpOsImage.setCloudId(cloudId);
        tblCmpOsImage.setEeStatus(SYNCING);
        tblCmpOsImage.setEeBp(eeBp);
        tblCmpOsImage.setEeUser(eeUser);
        tblCmpOsImage.setChecksum(imageInfo.getChecksum());
        tblCmpOsImage.setContainerFormat(imageInfo.getContainerFormat());
        tblCmpOsImage.setCreatedAt(imageInfo.getCreatedAt());
        tblCmpOsImage.setDiskFormat(imageInfo.getDiskFormat());
        tblCmpOsImage.setId(imageInfo.getId());
        tblCmpOsImage.setMinDisk(imageInfo.getMinDisk());
        tblCmpOsImage.setMinRam(imageInfo.getMinRam());
        tblCmpOsImage.setName(imageInfo.getName());
        tblCmpOsImage.setOsHashAlgo(imageInfo.getOsHashAlgo());
        tblCmpOsImage.setOsHashValue(imageInfo.getOsHashValue());
        tblCmpOsImage.setOsHidden(BoolUtil.bool2Short(imageInfo.getOsHidden()));
        tblCmpOsImage.setOwner(imageInfo.getOwner());
        tblCmpOsImage.setProtected(BoolUtil.bool2Short(imageInfo.getProtect()));
        tblCmpOsImage.setSize(imageInfo.getSize());
        tblCmpOsImage.setStatus(imageInfo.getStatus());
        tblCmpOsImage.setUpdatedAt(imageInfo.getUpdatedAt());
        tblCmpOsImage.setVirualSize(imageInfo.getVirtualSize() == null ? null : imageInfo.getVirtualSize().longValue());
        tblCmpOsImage.setVisibility(imageInfo.getVisibility());

        Map<String, String> properties = new HashMap<>();
        if (StringUtils.isNotEmpty(imageInfo.getInstanceId())) properties.put("instance_id", imageInfo.getInstanceId());
        if (StringUtils.isNotEmpty(imageInfo.getBootRoles())) properties.put("boot_roles", imageInfo.getBootRoles());
        if (StringUtils.isNotEmpty(imageInfo.getOwnerUserName())) properties.put("owner_user_name", imageInfo.getOwnerUserName());
        if (StringUtils.isNotEmpty(imageInfo.getUserId())) properties.put("user_id", imageInfo.getUserId());
        if (StringUtils.isNotEmpty(imageInfo.getOwnerProjectName())) properties.put("owner_project_name", imageInfo.getOwnerProjectName());
        if (StringUtils.isNotEmpty(imageInfo.getImageType()))
        {
            properties.put("image_type", imageInfo.getImageType());
        }
        else
        {
            if (StringUtils.isNotEmpty(imageInfo.getBootRoles()) || StringUtils.isNotEmpty(imageInfo.getBlockDeviceMapping()))
            {
                properties.put("image_type", "snapshot");
            }
        }
        if (StringUtils.isNotEmpty(imageInfo.getBaseImageRef())) properties.put("base_image_ref", imageInfo.getBaseImageRef());
        if (StringUtils.isNotEmpty(imageInfo.getImageLocation())) properties.put("image_location", imageInfo.getImageLocation());
        if (StringUtils.isNotEmpty(imageInfo.getOwnerId())) properties.put("owner_id", imageInfo.getOwnerId());
        if (StringUtils.isNotEmpty(imageInfo.getBlockDeviceMapping())) properties.put("block_device_mapping", imageInfo.getBlockDeviceMapping());
        if (StringUtils.isNotEmpty(imageInfo.getBdmV2())) properties.put("bdm_v2", imageInfo.getBdmV2());
        if (StringUtils.isNotEmpty(imageInfo.getHwCdromBus())) properties.put("hw_cdrom_bus", imageInfo.getHwCdromBus());
        if (StringUtils.isNotEmpty(imageInfo.getHwDiskBus())) properties.put("hw_disk_bus", imageInfo.getHwDiskBus());
        if (StringUtils.isNotEmpty(imageInfo.getHwMachineType())) properties.put("hw_machine_type", imageInfo.getHwMachineType());
        if (StringUtils.isNotEmpty(imageInfo.getHwVideoModel())) properties.put("hw_video_model", imageInfo.getHwVideoModel());
        if (StringUtils.isNotEmpty(imageInfo.getHwVifModel())) properties.put("hw_vif_model", imageInfo.getHwVifModel());
        if (StringUtils.isNotEmpty(imageInfo.getRootDeviceName())) properties.put("root_device_name", imageInfo.getRootDeviceName());
        if (StringUtils.isNotEmpty(imageInfo.getUsageType())) properties.put("usage_type", imageInfo.getUsageType());
        if (StringUtils.isNotEmpty(imageInfo.getSelf())) properties.put("self", imageInfo.getSelf());
        if (StringUtils.isNotEmpty(imageInfo.getFile())) properties.put("file", imageInfo.getFile());
        if (StringUtils.isNotEmpty(imageInfo.getSchema())) properties.put("schema", imageInfo.getSchema());
        tblCmpOsImage.setProperties(gson.toJson(properties));

        try
        {
            osGlanceRepository.insertImages(tblCmpOsImage);
        }
        catch (DuplicateKeyException e)
        {
            osGlanceRepository.updateImages(tblCmpOsImage);
        }
    }

    public void syncFlavors(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.1/flavors/detail";
        try
        {
            Set<String> flavorIds = osNovaRepository.getFlavorIds(tblCloudInfo.getCloudId());
            if (flavorIds == null)
            {
                flavorIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSFlavorsWithDetailsRsp getFlavorsRsp = gson.fromJson(result, OSFlavorsWithDetailsRsp.class);
                if (getFlavorsRsp != null && ! CollectionUtils.isEmpty(getFlavorsRsp.getFlavors()))
                {
                    for (OSFlavorInfo flavorInfo : getFlavorsRsp.getFlavors())
                    {
                        updateFlavorToDB(tblCloudInfo.getCloudId(), flavorInfo, null, null);
                        flavorIds.remove(flavorInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(flavorIds))
            {
                for (String flavorId : flavorIds)
                {
                    osNovaRepository.deleteFlavor(tblCloudInfo.getCloudId(), flavorId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} flavors error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFlavor(TblCloudInfo tblCloudInfo, String flavorId)
    {
        String urlPattern = "/v2.1/flavors/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, flavorId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNovaRepository.deleteFlavor(tblCloudInfo.getCloudId(), flavorId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSFlavorWithDetailsRsp getFlavorRsp = gson.fromJson(result, OSFlavorWithDetailsRsp.class);
                if (getFlavorRsp == null || getFlavorRsp.getFlavor() == null)
                {
                    osNovaRepository.deleteFlavor(tblCloudInfo.getCloudId(), flavorId);
                    return;
                }

                updateFlavorToDB(tblCloudInfo.getCloudId(), getFlavorRsp.getFlavor(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} flavor {} error msg: {}", tblCloudInfo.getName(), flavorId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFlavorToDB(String cloudId, OSFlavorInfo flavorInfo, String eeBp, String eeUser)
    {
        TblCmpOsFlavors tblCmpOsFlavor = new TblCmpOsFlavors();
        tblCmpOsFlavor.setCloudId(cloudId);
        tblCmpOsFlavor.setEeStatus(SYNCING);
        tblCmpOsFlavor.setEeBp(eeBp);
        tblCmpOsFlavor.setEeUser(eeUser);
        tblCmpOsFlavor.setFlavorid(flavorInfo.getId());
        tblCmpOsFlavor.setName(flavorInfo.getName());
        tblCmpOsFlavor.setDescription(flavorInfo.getDescription());
        tblCmpOsFlavor.setMemoryMb(flavorInfo.getRam());
        tblCmpOsFlavor.setVcpus(flavorInfo.getVcpus());
        tblCmpOsFlavor.setRootGb(flavorInfo.getDisk());
        tblCmpOsFlavor.setEphemeralGb(flavorInfo.getEphemeral());
        tblCmpOsFlavor.setDisabled(BoolUtil.bool2Short(flavorInfo.getDisabled()));
        if (flavorInfo.getSwap() instanceof Integer)
        {
            tblCmpOsFlavor.setSwap((int) flavorInfo.getSwap());
        }
        else if (flavorInfo.getSwap() instanceof Double)
        {
            tblCmpOsFlavor.setSwap(((Double) flavorInfo.getSwap()).intValue());
        }
        else
        {
            tblCmpOsFlavor.setSwap(0);
        }
        tblCmpOsFlavor.setRxtxFactor(flavorInfo.getRxtxFactor());
        tblCmpOsFlavor.setIsPublic(BoolUtil.bool2Short(flavorInfo.getIsPublic()));

        try
        {
            osNovaRepository.insertFlavor(tblCmpOsFlavor);
        }
        catch (DuplicateKeyException e)
        {
            osNovaRepository.updateFlavor(tblCmpOsFlavor);
        }

        Set<String> flavorExtraSpecKeys = osNovaRepository.getFlavorExtraSpecKeys(cloudId, flavorInfo.getId());
        if (flavorExtraSpecKeys == null)
        {
            flavorExtraSpecKeys = new HashSet<>();
        }

        if (flavorInfo.getExtraSpecs() != null && !flavorInfo.getExtraSpecs().isEmpty())
        {
            updateFlavorExtraSpecsToDB(cloudId, flavorInfo.getId(), flavorInfo.getExtraSpecs(), flavorExtraSpecKeys, eeBp, eeUser);
        }

        if (! CollectionUtils.isEmpty(flavorExtraSpecKeys))
        {
            for (String key : flavorExtraSpecKeys)
            {
                TblCmpOsFlavorExtraSpecs tblCmpOsFlavorExtraSpecs = new TblCmpOsFlavorExtraSpecs();
                tblCmpOsFlavorExtraSpecs.setCloudId(cloudId);
                tblCmpOsFlavorExtraSpecs.setFlavorId(flavorInfo.getId());
                tblCmpOsFlavorExtraSpecs.setEeStatus(REMOVED);
                tblCmpOsFlavorExtraSpecs.setKey(key);

                osNovaRepository.updateFlavorExtraSpec(tblCmpOsFlavorExtraSpecs);
            }
        }
    }

    public void updateFlavorExtraSpecsToDB(String cloudId, String flavorId, Map<String, String> extraSpecs, Set<String> flavorExtraSpecKeys, String eeBp, String eeUser)
    {
        for(Map.Entry<String, String> entry : extraSpecs.entrySet())
        {
            TblCmpOsFlavorExtraSpecs tblCmpOsFlavorExtraSpecs = new TblCmpOsFlavorExtraSpecs();
            tblCmpOsFlavorExtraSpecs.setCloudId(cloudId);
            tblCmpOsFlavorExtraSpecs.setFlavorId(flavorId);
            tblCmpOsFlavorExtraSpecs.setKey(entry.getKey());
            tblCmpOsFlavorExtraSpecs.setValue(entry.getValue());
            tblCmpOsFlavorExtraSpecs.setEeStatus(SYNCING);
            tblCmpOsFlavorExtraSpecs.setEeBp(eeBp);
            tblCmpOsFlavorExtraSpecs.setEeUser(eeUser);

            try
            {
                osNovaRepository.insertFlavorExtraSpec(tblCmpOsFlavorExtraSpecs);
            }
            catch (DuplicateKeyException e)
            {
                osNovaRepository.updateFlavorExtraSpec(tblCmpOsFlavorExtraSpecs);
            }

            flavorExtraSpecKeys.remove(entry.getKey());
        }
    }

    public void syncKeypairs(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.1/os-keypairs";
        try
        {
            Set<String> keyPairNames = osNovaRepository.getKeyPairNames(tblCloudInfo.getCloudId());
            if (keyPairNames == null)
            {
                keyPairNames = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSKeyPairsRsp osKeyPairsRsp = gson.fromJson(result, OSKeyPairsRsp.class);
                if (osKeyPairsRsp != null && ! CollectionUtils.isEmpty(osKeyPairsRsp.getKeypairs()))
                {
                    for (OSKeyPairsRsp.OSKeyPairInfo keyPairInfo : osKeyPairsRsp.getKeypairs())
                    {
                        syncKeypair(tblCloudInfo, keyPairInfo.getKeypair().getName(), null, null, null);
                        keyPairNames.remove(keyPairInfo.getKeypair().getName());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(keyPairNames))
            {
                for (String keyPairName : keyPairNames)
                {
                    TblCmpOsKeyPairs tblCmpOsKeyPair = new TblCmpOsKeyPairs();
                    tblCmpOsKeyPair.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsKeyPair.setName(keyPairName);
                    tblCmpOsKeyPair.setEeStatus(REMOVED);

                    osNovaRepository.updateKeyPair(tblCmpOsKeyPair);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} keypairs error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncKeypair(TblCloudInfo tblCloudInfo, String keypairName, String eeBp, String eeUser, String eeName)
    {
        String urlPattern = "/v2.1/os-keypairs/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, keypairName));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNovaRepository.deleteKeyPair(tblCloudInfo.getCloudId(), keypairName);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSKeyPairWithDetailsRsp osKeyPairsRsp = gson.fromJson(result, OSKeyPairWithDetailsRsp.class);
                if (osKeyPairsRsp == null || osKeyPairsRsp.getKeypair() == null)
                {
                    osNovaRepository.deleteKeyPair(tblCloudInfo.getCloudId(), keypairName);
                    return;
                }

                TblCmpOsKeyPairs tblCmpOsKeyPair = new TblCmpOsKeyPairs();
                tblCmpOsKeyPair.setCloudId(tblCloudInfo.getCloudId());
                tblCmpOsKeyPair.setEeStatus(SYNCING);
                tblCmpOsKeyPair.setEeName(eeName);
                tblCmpOsKeyPair.setEeBp(eeBp);
                tblCmpOsKeyPair.setEeUser(eeUser);
                tblCmpOsKeyPair.setCreatedAt(osKeyPairsRsp.getKeypair().getCreatedAt());
                tblCmpOsKeyPair.setFingerprint(osKeyPairsRsp.getKeypair().getFingerprint());
                tblCmpOsKeyPair.setId(osKeyPairsRsp.getKeypair().getId());
                tblCmpOsKeyPair.setName(osKeyPairsRsp.getKeypair().getName());
                tblCmpOsKeyPair.setPublicKey(osKeyPairsRsp.getKeypair().getPublicKey());
                tblCmpOsKeyPair.setUpdatedAt(osKeyPairsRsp.getKeypair().getUpdatedAt());
                tblCmpOsKeyPair.setUserId(osKeyPairsRsp.getKeypair().getUserId());
                tblCmpOsKeyPair.setType(osKeyPairsRsp.getKeypair().getType());

                try
                {
                    osNovaRepository.insertKeyPair(tblCmpOsKeyPair);
                }
                catch (DuplicateKeyException e)
                {
                    osNovaRepository.updateKeyPair(tblCmpOsKeyPair);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} keypair {} error msg: {}", tblCloudInfo.getName(), keypairName, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncServers(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.1/servers/detail";
        String softDeletedServersUrl = "/v2.1/servers/detail?deleted=true";
        try
        {
            Set<String> instanceIds = osNovaRepository.getInstanceIds(tblCloudInfo.getCloudId());
            if (instanceIds == null)
            {
                instanceIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "compute 2.67");

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url, osHeaders);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSServersWithDetailsRsp osServersWithDetailsRsp = gson.fromJson(result, OSServersWithDetailsRsp.class);
                if (osServersWithDetailsRsp != null && ! CollectionUtils.isEmpty(osServersWithDetailsRsp.getServers()))
                {
                    for (OSServerInfo serverInfo : osServersWithDetailsRsp.getServers())
                    {
                        updateServerToDB(tblCloudInfo.getCloudId(), serverInfo, null, null);
                        instanceIds.remove(serverInfo.getId());
                    }
                }
            }

            ResponseEntity<String> softDeletedServersResponse = osClientV3.sendHttpGetRequestToCloud(softDeletedServersUrl, osHeaders);
            if (softDeletedServersResponse.getStatusCode() != HttpStatus.OK) return;
            String softDeletedServersResult = softDeletedServersResponse.getBody();
            if (StringUtils.isNotEmpty(softDeletedServersResult))
            {
                OSServersWithDetailsRsp osServersWithDetailsRsp = gson.fromJson(result, OSServersWithDetailsRsp.class);
                if (osServersWithDetailsRsp != null && ! CollectionUtils.isEmpty(osServersWithDetailsRsp.getServers()))
                {
                    for (OSServerInfo serverInfo : osServersWithDetailsRsp.getServers())
                    {
                        updateServerToDB(tblCloudInfo.getCloudId(), serverInfo, null, null);
                        instanceIds.remove(serverInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(instanceIds))
            {
                for (String instanceId : instanceIds)
                {
                    osNovaRepository.deleteInstance(tblCloudInfo.getCloudId(), instanceId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} servers error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncServer(TblCloudInfo tblCloudInfo, String serverId)
    {
        String urlPattern = "/v2.1/servers/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "compute 2.67");

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, serverId), osHeaders);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNovaRepository.deleteInstance(tblCloudInfo.getCloudId(), serverId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSServerWithDetailsRsp osServerWithDetailsRsp = gson.fromJson(result, OSServerWithDetailsRsp.class);
                if (osServerWithDetailsRsp == null || osServerWithDetailsRsp.getServer() == null)
                {
                    osNovaRepository.deleteInstance(tblCloudInfo.getCloudId(), serverId);
                    return;
                }

                updateServerToDB(tblCloudInfo.getCloudId(), osServerWithDetailsRsp.getServer(), null, null);

                cloudService.syncSingleData(tblCloudInfo.getCloudId(), serverId, SyncMsg.OS_VOLUME_ATTACHMENTS);

                setEeUserForRelatedResources(tblCloudInfo.getCloudId(), serverId);

                syncPorts(tblCloudInfo, serverId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} server {} error msg: {}", tblCloudInfo.getName(), serverId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateServerToDB(String cloudId, OSServerInfo serverInfo, String eeBp, String eeUser)
    {
        TblCmpOsInstances tblCmpOsInstance = new TblCmpOsInstances();
        tblCmpOsInstance.setCloudId(cloudId);
        tblCmpOsInstance.setEeStatus(SYNCING);
        tblCmpOsInstance.setEeBp(eeBp);
        tblCmpOsInstance.setEeUser(eeUser);
        tblCmpOsInstance.setAccessIpV4(serverInfo.getAccessIPv4());
        tblCmpOsInstance.setAccessIpV6(serverInfo.getAccessIPv6());
        tblCmpOsInstance.setConfigDrive(serverInfo.getConfigDrive());
        tblCmpOsInstance.setCreatedAt(serverInfo.getCreated());
        tblCmpOsInstance.setUuid(serverInfo.getId());
        tblCmpOsInstance.setKeyName(serverInfo.getKeyName());
        tblCmpOsInstance.setDisplayName(serverInfo.getName());
        tblCmpOsInstance.setAvailabilityZone(serverInfo.getAvailabilityZone());
        tblCmpOsInstance.setHost(serverInfo.getHost());
        tblCmpOsInstance.setHostname(serverInfo.getHostname());
        tblCmpOsInstance.setKernelId(serverInfo.getKernelid());
        tblCmpOsInstance.setLaunchIndex(serverInfo.getLaunchIndex());
        tblCmpOsInstance.setRamdiskId(serverInfo.getRamdiskId());
        tblCmpOsInstance.setReservationId(serverInfo.getReservationId());
        tblCmpOsInstance.setRootDeviceName(serverInfo.getRootDeviceName());
        tblCmpOsInstance.setUserData(serverInfo.getUserData());
        tblCmpOsInstance.setPowerState(serverInfo.getPowerState());
        tblCmpOsInstance.setTaskState(serverInfo.getTaskState());
        tblCmpOsInstance.setVmState(serverInfo.getVmState());
        tblCmpOsInstance.setLaunchedAt(serverInfo.getLaunchedAt());
        tblCmpOsInstance.setTerminatedAt(serverInfo.getTerminatedAt());
        tblCmpOsInstance.setStatus(serverInfo.getStatus());
        tblCmpOsInstance.setProjectId(serverInfo.getTenantId());
        tblCmpOsInstance.setUpdatedAt(serverInfo.getUpdated());
        tblCmpOsInstance.setUserId(serverInfo.getUserId());
        tblCmpOsInstance.setProgress(serverInfo.getProgress());
        tblCmpOsInstance.setLocked(BoolUtil.bool2Short(serverInfo.getLocked()));
        tblCmpOsInstance.setDisplayDescription(serverInfo.getDescription());
        tblCmpOsInstance.setLockedBy(serverInfo.getLockedReason());
        tblCmpOsInstance.setInstanceTypeId(serverInfo.getFlavor() == null ? null : serverInfo.getFlavor().getId());
        if (!(serverInfo.getImage() instanceof String))
        {
            OSServerInfo.OSImage image = gson.fromJson(gson.toJson(serverInfo.getImage()), OSServerInfo.OSImage.class);
            tblCmpOsInstance.setImageRef(image == null ? null : image.getId());
        }
        else
        {
            if (StringUtils.isNotEmpty((String) serverInfo.getImage()))
            {
                tblCmpOsInstance.setImageRef((String) serverInfo.getImage());
            }
        }
        tblCmpOsInstance.setNetworkInfo(gson.toJson(serverInfo.getAddresses()));

        try
        {
            osNovaRepository.insertInstance(tblCmpOsInstance);
        }
        catch (DuplicateKeyException e)
        {
            osNovaRepository.updateInstance(tblCmpOsInstance);
        }

        if (serverInfo.getFault() != null)
        {
            updateInstanceFaultToDB(cloudId, serverInfo.getId(), serverInfo.getFault(), eeBp, eeUser);
        }

        if (! CollectionUtils.isEmpty(serverInfo.getOsExtendedVolumesAttached()))
        {
            serverInfo.getOsExtendedVolumesAttached().forEach(osExtendedVolumesAttach -> {
                TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping = new TblCmpOsBlockDeviceMapping();
                tblCmpOsBlockDeviceMapping.setCloudId(cloudId);
                tblCmpOsBlockDeviceMapping.setVolumeId(osExtendedVolumesAttach.getId());
                tblCmpOsBlockDeviceMapping.setDeleteOnTermination(BoolUtil.bool2Short(osExtendedVolumesAttach.getDeleteOnTermination()));

                try
                {
                    osNovaRepository.updateByVolumeIdSelective(tblCmpOsBlockDeviceMapping);
                }
                catch (Exception e)
                {
                    LOGGER.error("update server block device mapping error, {}", e.getMessage());
                }
            });
        }
    }

    public void updateInstanceFaultToDB(String cloudId, String instanceId, OSFault fault, String eeBp, String eeUser)
    {
        TblCmpOsInstanceFaults tblCmpOsInstanceFault = new TblCmpOsInstanceFaults();
        tblCmpOsInstanceFault.setCloudId(cloudId);
        tblCmpOsInstanceFault.setEeStatus(SYNCING);
        tblCmpOsInstanceFault.setEeBp(eeBp);
        tblCmpOsInstanceFault.setEeUser(eeUser);
        tblCmpOsInstanceFault.setInstanceUuid(instanceId);
        tblCmpOsInstanceFault.setCode(fault.getCode());
        tblCmpOsInstanceFault.setCreatedAt(fault.getCreated());
        tblCmpOsInstanceFault.setMessage(fault.getMessage());
        tblCmpOsInstanceFault.setDetails(fault.getDetails());

        try
        {
            osNovaRepository.insertInstanceFault(tblCmpOsInstanceFault);
        }
        catch (DuplicateKeyException e)
        {
            osNovaRepository.updateInstanceFault(tblCmpOsInstanceFault);
        }
    }

    public void syncNetworks(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/networks";
        try
        {
            Set<String> networkIds = osNeutronRepository.getNetworkIds(tblCloudInfo.getCloudId());
            if (networkIds == null)
            {
                networkIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSNetworksWithDetailsRsp networksWithDetailsRsp = gson.fromJson(result, OSNetworksWithDetailsRsp.class);
                if (networksWithDetailsRsp != null && ! CollectionUtils.isEmpty(networksWithDetailsRsp.getNetworks()))
                {
                    for (OSNetworkInfo networkInfo : networksWithDetailsRsp.getNetworks())
                    {
                        updateNetworkToDB(tblCloudInfo.getCloudId(), networkInfo, null, null);
                        syncNetworksegments(tblCloudInfo, networkInfo.getId());

                        networkIds.remove(networkInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(networkIds))
            {
                for (String networkId : networkIds)
                {
                    osNeutronRepository.deleteNetwork(tblCloudInfo.getCloudId(), networkId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} networks error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncNetwork(TblCloudInfo tblCloudInfo, String networkId)
    {
        String urlPattern = "/v2.0/networks/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, networkId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteNetwork(tblCloudInfo.getCloudId(), networkId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSNetworkWithDetailsRsp networkWithDetailsRsp = gson.fromJson(result, OSNetworkWithDetailsRsp.class);
                if (networkWithDetailsRsp == null || networkWithDetailsRsp.getNetwork() == null)
                {
                    osNeutronRepository.deleteNetwork(tblCloudInfo.getCloudId(), networkId);
                    return;
                }

                updateNetworkToDB(tblCloudInfo.getCloudId(), networkWithDetailsRsp.getNetwork(), null, null);
                syncNetworksegments(tblCloudInfo, networkId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} network {} error msg: {}", tblCloudInfo.getName(), networkId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateNetworkToDB(String cloudId, OSNetworkInfo networkInfo, String eeBp, String eeUser)
    {
        TblCmpOsNetworks tblCmpOsNetwork = new TblCmpOsNetworks();
        tblCmpOsNetwork.setCloudId(cloudId);
        tblCmpOsNetwork.setEeStatus(SYNCING);
        tblCmpOsNetwork.setEeBp(eeBp);
        tblCmpOsNetwork.setEeUser(eeUser);
        tblCmpOsNetwork.setId(networkInfo.getId());
        tblCmpOsNetwork.setName(networkInfo.getName());
        tblCmpOsNetwork.setProjectId(networkInfo.getProjectId());
        tblCmpOsNetwork.setAdminStateUp(BoolUtil.bool2Short(networkInfo.getAdminStateUp()));
        tblCmpOsNetwork.setMtu(networkInfo.getMtu());
        tblCmpOsNetwork.setStatus(networkInfo.getStatus());
        tblCmpOsNetwork.setAvailabilityZoneHints(gson.toJson(networkInfo.getAvailabilityZoneHints()));
        tblCmpOsNetwork.setVlanTransparent(BoolUtil.bool2Short(networkInfo.getVlanTransparent()));
        tblCmpOsNetwork.setShared(BoolUtil.bool2Short(networkInfo.getShared()));
        tblCmpOsNetwork.setCreatedAt(networkInfo.getCreatedAt());
        tblCmpOsNetwork.setUpdatedAt(networkInfo.getUpdatedAt());
        tblCmpOsNetwork.setDescription(networkInfo.getDescription());
        tblCmpOsNetwork.setRevisionNumber(networkInfo.getRevisionNumber() == null ? null : networkInfo.getRevisionNumber().longValue());
        tblCmpOsNetwork.setIsDefault(BoolUtil.bool2Short(networkInfo.getIsDefault()));
        tblCmpOsNetwork.setDnsDomain(networkInfo.getDnsDomain());
        tblCmpOsNetwork.setPortSecurityEnabled(BoolUtil.bool2Short(networkInfo.getPortSecurityEnabled()));
        tblCmpOsNetwork.setPolicyId(networkInfo.getQosPolicyId());

        try
        {
            osNeutronRepository.insertNetwork(tblCmpOsNetwork);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateNetwork(tblCmpOsNetwork);
        }

        updateNetworksegmentToDB(cloudId, networkInfo, eeBp, eeUser);
    }

    public void updateNetworksegmentToDB(String cloudId, OSNetworkInfo networkInfo, String eeBp, String eeUser)
    {
        TblCmpOsNetworksegmentsExample example = new TblCmpOsNetworksegmentsExample();
        TblCmpOsNetworksegmentsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andNetworkIdEqualTo(networkInfo.getId());
        List<TblCmpOsNetworksegments> tblCmpOsNetworksegments = osNeutronRepository.getNetworksegments(example);
        Set<TblCmpOsNetworksegments> tblCmpOsNetworksegmentSet = new HashSet<>(tblCmpOsNetworksegments);

        if (StringUtils.isNotEmpty(networkInfo.getNetworkType()) || StringUtils.isNotEmpty(networkInfo.getProviderPhyNet()) || StringUtils.isNotEmpty(networkInfo.getProviderSegID()))
        {
            TblCmpOsNetworksegments tblCmpOsNetworksegment = new TblCmpOsNetworksegments();
            tblCmpOsNetworksegment.setCloudId(cloudId);
            tblCmpOsNetworksegment.setEeStatus(SYNCING);
            tblCmpOsNetworksegment.setId(Utils.assignUUId());
            tblCmpOsNetworksegment.setNetworkId(networkInfo.getId());
            tblCmpOsNetworksegment.setNetworkType(networkInfo.getNetworkType());
            tblCmpOsNetworksegment.setPhysicalNetwork(networkInfo.getProviderPhyNet());
            tblCmpOsNetworksegment.setSegmentationId(networkInfo.getProviderSegID() == null ? null : Integer.valueOf(networkInfo.getProviderSegID()));

            for (TblCmpOsNetworksegments item : tblCmpOsNetworksegmentSet)
            {
                if (StringUtils.equals(item.getNetworkType(), networkInfo.getNetworkType()) &&
                        StringUtils.equals(item.getPhysicalNetwork(), networkInfo.getProviderPhyNet()) &&
                        StringUtils.equals(item.getSegmentationId() == null ? null : item.getSegmentationId().toString(), networkInfo.getProviderSegID()))
                {
                    tblCmpOsNetworksegmentSet.remove(item);
                    tblCmpOsNetworksegment.setId(item.getId());
                    break;
                }
            }

            try
            {
                osNeutronRepository.insertNetworksegment(tblCmpOsNetworksegment);
            }
            catch (DuplicateKeyException e)
            {
                osNeutronRepository.updateNetworksegment(tblCmpOsNetworksegment);
            }
        }

        if (! CollectionUtils.isEmpty(networkInfo.getSegments()))
        {
            networkInfo.getSegments().forEach(osNetworkSegment -> {
                TblCmpOsNetworksegments tblCmpOsNetworksegment = new TblCmpOsNetworksegments();
                tblCmpOsNetworksegment.setCloudId(cloudId);
                tblCmpOsNetworksegment.setEeStatus(SYNCING);
                tblCmpOsNetworksegment.setId(Utils.assignUUId());
                tblCmpOsNetworksegment.setNetworkId(networkInfo.getId());
                tblCmpOsNetworksegment.setNetworkType(osNetworkSegment.getNetworkType());
                tblCmpOsNetworksegment.setPhysicalNetwork(osNetworkSegment.getProviderPhyNet());
                tblCmpOsNetworksegment.setSegmentationId(osNetworkSegment.getProviderSegID() == null ? null : Integer.valueOf(osNetworkSegment.getProviderSegID()));

                for (TblCmpOsNetworksegments item : tblCmpOsNetworksegmentSet)
                {
                    if (StringUtils.equals(item.getNetworkType(), networkInfo.getNetworkType()) &&
                            StringUtils.equals(item.getPhysicalNetwork(), networkInfo.getProviderPhyNet()) &&
                            StringUtils.equals(item.getSegmentationId() == null ? null : item.getSegmentationId().toString(), networkInfo.getProviderSegID()))
                    {
                        tblCmpOsNetworksegmentSet.remove(item);
                        tblCmpOsNetworksegment.setId(item.getId());
                        break;
                    }
                }

                try
                {
                    osNeutronRepository.insertNetworksegment(tblCmpOsNetworksegment);
                }
                catch (DuplicateKeyException e)
                {
                    osNeutronRepository.updateNetworksegment(tblCmpOsNetworksegment);
                }
            });
        }

        if (! CollectionUtils.isEmpty(tblCmpOsNetworksegmentSet))
        {
            tblCmpOsNetworksegmentSet.forEach(tblCmpOsNetworksegment -> {
                tblCmpOsNetworksegment.setEeStatus(REMOVED);
                osNeutronRepository.updateNetworksegment(tblCmpOsNetworksegment);
            });
        }
    }

    public void syncNetworksegments(TblCloudInfo tblCloudInfo, String networkId)
    {
        String urlPattern = "/v2.0/segments?network_id=%s";
        String url = "/v2.0/segments";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity<String> response;
            if (StringUtils.isNotEmpty(networkId))
            {
                response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, networkId));

            }
            else {
                response = osClientV3.sendHttpGetRequestToCloud(url);
            }
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSegmentsWithDetailsRsp segmentsWithDetailsRsp = gson.fromJson(result, OSSegmentsWithDetailsRsp.class);
                if (segmentsWithDetailsRsp == null || CollectionUtils.isEmpty(segmentsWithDetailsRsp.getSegments()))
                {
                    return;
                }

                for (OSSegmentInfo segmentInfo : segmentsWithDetailsRsp.getSegments())
                {

                    TblCmpOsNetworksegments tblCmpOsNetworksegment = new TblCmpOsNetworksegments();
                    tblCmpOsNetworksegment.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsNetworksegment.setEeStatus(SYNCING);
                    tblCmpOsNetworksegment.setId(segmentInfo.getId());
                    tblCmpOsNetworksegment.setNetworkId(segmentInfo.getNetworkId());
                    tblCmpOsNetworksegment.setPhysicalNetwork(segmentInfo.getPhysicalNetwork());
                    tblCmpOsNetworksegment.setNetworkType(segmentInfo.getNetworkType());
                    tblCmpOsNetworksegment.setRevisionNumber(segmentInfo.getRevisionNumber() == null ? null : segmentInfo.getRevisionNumber().longValue());
                    tblCmpOsNetworksegment.setSegmentationId(segmentInfo.getSegmentationId());
                    tblCmpOsNetworksegment.setName(segmentInfo.getName());
                    tblCmpOsNetworksegment.setDescription(segmentInfo.getDescription());
                    tblCmpOsNetworksegment.setCreatedAt(segmentInfo.getCreatedAt());
                    tblCmpOsNetworksegment.setUpdatedAt(segmentInfo.getUpdatedAt());

                    try
                    {
                        osNeutronRepository.insertNetworksegment(tblCmpOsNetworksegment);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osNeutronRepository.updateNetworksegment(tblCmpOsNetworksegment);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} networksegments error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPorts(TblCloudInfo tblCloudInfo, String deviceId)
    {
        String url = "/v2.0/ports";
        if (deviceId != null)
        {
            url = String.format("/v2.0/ports?device_id=%s", deviceId);
        }
        try
        {
            Set<String> portIds = osNeutronRepository.getPortIds(tblCloudInfo.getCloudId(), deviceId);
            if (portIds == null)
            {
                portIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSPortsWithDetailsRsp portsWithDetailsRsp = gson.fromJson(result, OSPortsWithDetailsRsp.class);
                if (portsWithDetailsRsp != null && ! CollectionUtils.isEmpty(portsWithDetailsRsp.getPorts()))
                {
                    for (OSPortInfo portInfo : portsWithDetailsRsp.getPorts())
                    {
                        updatePortToDB(tblCloudInfo.getCloudId(), portInfo, null, null);

                        portIds.remove(portInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(portIds))
            {
                for (String portId : portIds)
                {
                    osNeutronRepository.deletePort(tblCloudInfo.getCloudId(), portId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} {} ports error msg: {}", tblCloudInfo.getName(), deviceId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncPort(TblCloudInfo tblCloudInfo, String portId)
    {
        String urlPattern = "/v2.0/ports/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, portId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deletePort(tblCloudInfo.getCloudId(), portId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSPortWithDetailsRsp portWithDetailsRsp = gson.fromJson(result, OSPortWithDetailsRsp.class);
                if (portWithDetailsRsp == null || portWithDetailsRsp.getPort() == null)
                {
                    osNeutronRepository.deletePort(tblCloudInfo.getCloudId(), portId);
                    return;
                }

                updatePortToDB(tblCloudInfo.getCloudId(), portWithDetailsRsp.getPort(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} port {} error msg: {}", tblCloudInfo.getName(), portId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updatePortToDB(String cloudId, OSPortInfo portInfo, String eeBp, String eeUser)
    {
        TblCmpOsPorts tblCmpOsPort = new TblCmpOsPorts();
        tblCmpOsPort.setCloudId(cloudId);
        tblCmpOsPort.setEeStatus(SYNCING);
        tblCmpOsPort.setEeBp(eeBp);
        tblCmpOsPort.setEeUser(eeUser);
        tblCmpOsPort.setId(portInfo.getId());
        tblCmpOsPort.setProjectId(portInfo.getProjectId());
        tblCmpOsPort.setName(portInfo.getName());
        tblCmpOsPort.setNetworkId(portInfo.getNetworkId());
        tblCmpOsPort.setMacAddress(portInfo.getMacAddress());
        tblCmpOsPort.setAdminStateUp(BoolUtil.bool2Short(portInfo.isAdminStateUp()));
        tblCmpOsPort.setDeviceId(portInfo.getDeviceId());
        tblCmpOsPort.setDeviceOwner(portInfo.getDeviceOwner());
        tblCmpOsPort.setStatus(portInfo.getState());
        tblCmpOsPort.setIpAllocation(portInfo.getIpAllocation());

        tblCmpOsPort.setDataPlaneStatus(portInfo.getDataPlaneStatus());

        tblCmpOsPort.setDnsDomain(portInfo.getDnsDomain());
        tblCmpOsPort.setDnsName(portInfo.getDnsName());
        tblCmpOsPort.setPolicyId(portInfo.getQosPolicyId());

        tblCmpOsPort.setCreatedAt(portInfo.getCreatedAt());
        tblCmpOsPort.setUpdatedAt(portInfo.getUpdatedAt());
        tblCmpOsPort.setDescription(portInfo.getDescription());
        tblCmpOsPort.setRevisionNumber(portInfo.getRevisionNumber() == null ? null : portInfo.getRevisionNumber().longValue());

        tblCmpOsPort.setPropagateUplinkStatus(BoolUtil.bool2Short(portInfo.getPropagateUplinkStatus()));
        tblCmpOsPort.setMacLearningEnabled(BoolUtil.bool2Short(portInfo.getMacLearningEnabled()));
        tblCmpOsPort.setPortSecurityEnabled(BoolUtil.bool2Short(portInfo.getPortSecurityEnabled()));

        try
        {
            osNeutronRepository.insertPort(tblCmpOsPort);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updatePort(tblCmpOsPort);
        }

        updateAllowedaddresspairsToDB(cloudId, portInfo.getId(), portInfo.getAllowedAddressPairs());

        if (StringUtils.isNotEmpty(portInfo.getVifType()) || StringUtils.isNotEmpty(portInfo.getVNicType())
                || StringUtils.isNotEmpty(portInfo.getHostId()) || !portInfo.getProfile().isEmpty()
                || !portInfo.getVifDetails().isEmpty())
        {
            TblCmpOsMl2PortBindings tblCmpOsMl2PortBinding = new TblCmpOsMl2PortBindings();
            tblCmpOsMl2PortBinding.setCloudId(cloudId);
            tblCmpOsMl2PortBinding.setEeStatus(SYNCING);
            tblCmpOsMl2PortBinding.setPortId(portInfo.getId());
            tblCmpOsMl2PortBinding.setProfile(gson.toJson(portInfo.getProfile()));
            tblCmpOsMl2PortBinding.setVifDetails(gson.toJson(portInfo.getVifDetails()));
            tblCmpOsMl2PortBinding.setVifType(portInfo.getVifType());
            tblCmpOsMl2PortBinding.setVnicType(portInfo.getVNicType());
            tblCmpOsMl2PortBinding.setHost(portInfo.getHostId());

            try
            {
                osNeutronRepository.insertMl2PortBinding(tblCmpOsMl2PortBinding);
            }
            catch (DuplicateKeyException e)
            {
                osNeutronRepository.updateMl2PortBinding(tblCmpOsMl2PortBinding);
            }
        }

        updateExtraDhcpOptsToDB(cloudId, portInfo.getId(), portInfo.getExtraDhcpOptCreates());

        updateSecuritygroupportbindingsToDB(cloudId, portInfo.getId(), portInfo.getSecurityGroups());

        updateIpallocationsToDB(cloudId, portInfo.getId(), portInfo.getNetworkId(), portInfo.getFixedIps());
    }

    private void updateAllowedaddresspairsToDB(String cloudId, String portId, Set<OSAllowedAddressPair> allowedAddressPairs)
    {
        TblCmpOsAllowedaddresspairsExample example = new TblCmpOsAllowedaddresspairsExample();
        TblCmpOsAllowedaddresspairsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPortIdEqualTo(portId);
        List<TblCmpOsAllowedaddresspairs> tblCmpOsAllowedaddresspairs = osNeutronRepository.getAllowedaddresspairs(example);

        if (CollectionUtils.isEmpty(allowedAddressPairs))
        {
            if (! CollectionUtils.isEmpty(tblCmpOsAllowedaddresspairs))
            {
                tblCmpOsAllowedaddresspairs.forEach(tblCmpOsAllowedaddresspair ->
                {
                    tblCmpOsAllowedaddresspair.setEeStatus(REMOVED);
                    osNeutronRepository.updateAllowedaddresspair(tblCmpOsAllowedaddresspair);
                });
            }
        }
        else
        {
            if (! CollectionUtils.isEmpty(tblCmpOsAllowedaddresspairs))
            {
                tblCmpOsAllowedaddresspairs.forEach(tblCmpOsAllowedaddresspair -> {
                    OSAllowedAddressPair osAllowedAddressPair = new OSAllowedAddressPair(tblCmpOsAllowedaddresspair.getIpAddress(), tblCmpOsAllowedaddresspair.getMacAddress());
                    if (allowedAddressPairs.contains(osAllowedAddressPair))
                    {
                        allowedAddressPairs.remove(osAllowedAddressPair);
                    }
                    else
                    {
                        tblCmpOsAllowedaddresspair.setEeStatus(REMOVED);
                        osNeutronRepository.updateAllowedaddresspair(tblCmpOsAllowedaddresspair);
                    }
                });
            }

            if (! CollectionUtils.isEmpty(allowedAddressPairs))
            {
                allowedAddressPairs.forEach(allowedAddressPair -> {
                    TblCmpOsAllowedaddresspairs tblCmpOsAllowedaddresspair = new TblCmpOsAllowedaddresspairs();
                    tblCmpOsAllowedaddresspair.setCloudId(cloudId);
                    tblCmpOsAllowedaddresspair.setEeStatus(SYNCING);
                    tblCmpOsAllowedaddresspair.setPortId(portId);
                    tblCmpOsAllowedaddresspair.setMacAddress(allowedAddressPair.getMacAddress());
                    tblCmpOsAllowedaddresspair.setIpAddress(allowedAddressPair.getIpAddress());
                    try
                    {
                        osNeutronRepository.insertAllowedaddresspair(tblCmpOsAllowedaddresspair);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osNeutronRepository.updateAllowedaddresspair(tblCmpOsAllowedaddresspair);
                    }
                });
            }
        }
    }

    private void updateExtraDhcpOptsToDB(String cloudId, String portId, List<OSExtraDhcpOptCreate> extraDhcpOptCreates)
    {
        TblCmpOsExtradhcpoptsExample example = new TblCmpOsExtradhcpoptsExample();
        TblCmpOsExtradhcpoptsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andPortIdEqualTo(portId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsExtradhcpopts> tblCmpOsExtradhcpopts = osNeutronRepository.getExtradhcpopts(example);

        if (CollectionUtils.isEmpty(extraDhcpOptCreates))
        {
            if (! CollectionUtils.isEmpty(tblCmpOsExtradhcpopts))
            {
                tblCmpOsExtradhcpopts.forEach(tblCmpOsExtradhcpopt ->
                {
                    tblCmpOsExtradhcpopt.setEeStatus(REMOVED);
                    osNeutronRepository.updateExtradhcpopt(tblCmpOsExtradhcpopt);
                });
            }
        }
        else
        {
            Set<OSExtraDhcpOptCreate> extraDhcpOptCreateSet = new HashSet<>(extraDhcpOptCreates);

            if (! CollectionUtils.isEmpty(extraDhcpOptCreateSet))
            {
                tblCmpOsExtradhcpopts.forEach(tblCmpOsExtradhcpopt ->
                {
                    OSExtraDhcpOptCreate osExtraDhcpOptCreate = new OSExtraDhcpOptCreate(tblCmpOsExtradhcpopt.getOptValue(), tblCmpOsExtradhcpopt.getOptName(), tblCmpOsExtradhcpopt.getIpVersion());
                    if (extraDhcpOptCreateSet.contains(osExtraDhcpOptCreate))
                    {
                        extraDhcpOptCreateSet.remove(osExtraDhcpOptCreate);
                    }
                    else
                    {
                        tblCmpOsExtradhcpopt.setEeStatus(REMOVED);
                        osNeutronRepository.updateExtradhcpopt(tblCmpOsExtradhcpopt);
                    }
                });
            }

            if (! CollectionUtils.isEmpty(extraDhcpOptCreateSet))
            {
                for (OSExtraDhcpOptCreate extraDhcpOptCreate : extraDhcpOptCreates)
                {
                    TblCmpOsExtradhcpopts tblCmpOsExtradhcpopt = new TblCmpOsExtradhcpopts();
                    tblCmpOsExtradhcpopt.setCloudId(cloudId);
                    tblCmpOsExtradhcpopt.setEeStatus(SYNCING);
                    tblCmpOsExtradhcpopt.setPortId(portId);
                    tblCmpOsExtradhcpopt.setOptName(extraDhcpOptCreate.getOptName());
                    tblCmpOsExtradhcpopt.setOptValue(extraDhcpOptCreate.getOptName());
                    tblCmpOsExtradhcpopt.setIpVersion(extraDhcpOptCreate.getIpVersion());
                    try
                    {
                        osNeutronRepository.insertExtradhcpopt(tblCmpOsExtradhcpopt);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osNeutronRepository.updateExtradhcpopt(tblCmpOsExtradhcpopt);
                    }
                }
            }
        }
    }

    private void updateSecuritygroupportbindingsToDB(String cloudId, String portId, List<String> securityGroups)
    {
        TblCmpOsSecuritygroupportbindingsExample example = new TblCmpOsSecuritygroupportbindingsExample();
        TblCmpOsSecuritygroupportbindingsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andPortIdEqualTo(portId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsSecuritygroupportbindings> tblCmpOsSecuritygroupportbindings = osNeutronRepository.getSecuritygroupportbindings(example);

        if (CollectionUtils.isEmpty(securityGroups))
        {
            if (! CollectionUtils.isEmpty(tblCmpOsSecuritygroupportbindings))
            {
                tblCmpOsSecuritygroupportbindings.forEach(tblCmpOsSecuritygroupportbinding ->
                {
                    tblCmpOsSecuritygroupportbinding.setEeStatus(REMOVED);
                    osNeutronRepository.updateSecuritygroupportbinding(tblCmpOsSecuritygroupportbinding);
                });
            }
        }
        else
        {
            Set<String> securityGroupIdSet = new HashSet<>(securityGroups);

            if (! CollectionUtils.isEmpty(tblCmpOsSecuritygroupportbindings))
            {
                tblCmpOsSecuritygroupportbindings.forEach(tblCmpOsSecuritygroupportbinding ->
                {
                    if (securityGroupIdSet.contains(tblCmpOsSecuritygroupportbinding.getSecurityGroupId()))
                    {
                        securityGroupIdSet.remove(tblCmpOsSecuritygroupportbinding.getSecurityGroupId());
                    }
                    else
                    {
                        tblCmpOsSecuritygroupportbinding.setEeStatus(REMOVED);
                        osNeutronRepository.updateSecuritygroupportbinding(tblCmpOsSecuritygroupportbinding);
                    }
                });
            }

            if (! CollectionUtils.isEmpty(securityGroupIdSet))
            {
                for (String securityGroup : securityGroups)
                {
                    TblCmpOsSecuritygroupportbindings tblCmpOsSecuritygroupportbinding = new TblCmpOsSecuritygroupportbindings();
                    tblCmpOsSecuritygroupportbinding.setCloudId(cloudId);
                    tblCmpOsSecuritygroupportbinding.setEeStatus(SYNCING);
                    tblCmpOsSecuritygroupportbinding.setPortId(portId);
                    tblCmpOsSecuritygroupportbinding.setSecurityGroupId(securityGroup);
                    try
                    {
                        osNeutronRepository.insertSecuritygroupportbinding(tblCmpOsSecuritygroupportbinding);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osNeutronRepository.updateSecuritygroupportbinding(tblCmpOsSecuritygroupportbinding);
                    }
                }
            }
        }
    }

    private void updateIpallocationsToDB(String cloudId, String portId, String networkId, Set<OSIP> fixedIps)
    {
        TblCmpOsIpallocationsExample example = new TblCmpOsIpallocationsExample();
        TblCmpOsIpallocationsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andPortIdEqualTo(portId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsIpallocations> tblCmpOsIpallocations = osNeutronRepository.getIpallocations(example);

        if (CollectionUtils.isEmpty(fixedIps))
        {
            if (! CollectionUtils.isEmpty(tblCmpOsIpallocations))
            {
                tblCmpOsIpallocations.forEach(tblCmpOsIpallocation -> {
                    tblCmpOsIpallocation.setEeStatus(REMOVED);
                    osNeutronRepository.updateIpallocation(tblCmpOsIpallocation);
                });
            }
        }
        else
        {
            if (! CollectionUtils.isEmpty(tblCmpOsIpallocations))
            {
                tblCmpOsIpallocations.forEach(tblCmpOsIpallocation ->
                {
                    OSIP osip = new OSIP(tblCmpOsIpallocation.getIpAddress(), tblCmpOsIpallocation.getSubnetId());
                    if (fixedIps.contains(osip))
                    {
                        fixedIps.remove(osip);
                    }
                    else
                    {
                        tblCmpOsIpallocation.setEeStatus(REMOVED);
                        osNeutronRepository.updateIpallocation(tblCmpOsIpallocation);
                    }
                });
            }

            if (! CollectionUtils.isEmpty(fixedIps))
            {
                for (OSIP fixedIp : fixedIps)
                {
                    TblCmpOsIpallocations tblCmpOsIpallocation = new TblCmpOsIpallocations();
                    tblCmpOsIpallocation.setCloudId(cloudId);
                    tblCmpOsIpallocation.setEeStatus(SYNCING);
                    tblCmpOsIpallocation.setPortId(portId);
                    tblCmpOsIpallocation.setIpAddress(fixedIp.getIpAddress());
                    tblCmpOsIpallocation.setSubnetId(fixedIp.getSubnetId());
                    tblCmpOsIpallocation.setNetworkId(networkId);
                    try
                    {
                        osNeutronRepository.insertIpallocation(tblCmpOsIpallocation);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osNeutronRepository.updateIpallocation(tblCmpOsIpallocation);
                    }
                }
            }
        }
    }

    public void syncSubnets(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/subnets";
        try
        {
            Set<String> subnetIds = osNeutronRepository.getSubnetIds(tblCloudInfo.getCloudId());
            if (subnetIds == null)
            {
                subnetIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSubnetsWithDetailsRsp subnetsWithDetailsRsp = gson.fromJson(result, OSSubnetsWithDetailsRsp.class);
                if (subnetsWithDetailsRsp != null && ! CollectionUtils.isEmpty(subnetsWithDetailsRsp.getSubnets()))
                {
                    for (OSSubnetInfo subnetInfo : subnetsWithDetailsRsp.getSubnets())
                    {
                        updateSubnetToDB(tblCloudInfo.getCloudId(), subnetInfo, null, null);
                        subnetIds.remove(subnetInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(subnetIds))
            {
                for (String subnetId : subnetIds)
                {
                    osNeutronRepository.deleteSubnet(tblCloudInfo.getCloudId(), subnetId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} subnets error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSubnet(TblCloudInfo tblCloudInfo, String subnetId)
    {
        String urlPattern = "/v2.0/subnets/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, subnetId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteSubnet(tblCloudInfo.getCloudId(), subnetId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSubnetWithDetailsRsp subnetWithDetailsRsp = gson.fromJson(result, OSSubnetWithDetailsRsp.class);
                if (subnetWithDetailsRsp == null || subnetWithDetailsRsp.getSubnet() == null)
                {
                    osNeutronRepository.deleteSubnet(tblCloudInfo.getCloudId(), subnetId);
                    return;
                }

                updateSubnetToDB(tblCloudInfo.getCloudId(), subnetWithDetailsRsp.getSubnet(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} subnet {} error msg: {}", tblCloudInfo.getName(), subnetId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSubnetToDB(String cloudId, OSSubnetInfo subnetInfo, String eeBp, String eeUser)
    {
        TblCmpOsSubnets tblCmpOsSubnet = new TblCmpOsSubnets();
        tblCmpOsSubnet.setCloudId(cloudId);
        tblCmpOsSubnet.setEeStatus(SYNCING);
        tblCmpOsSubnet.setEeBp(eeBp);
        tblCmpOsSubnet.setEeUser(eeUser);
        tblCmpOsSubnet.setId(subnetInfo.getId());
        tblCmpOsSubnet.setName(subnetInfo.getName());
        tblCmpOsSubnet.setProjectId(subnetInfo.getProjectId());
        tblCmpOsSubnet.setNetworkId(subnetInfo.getNetworkId());
        tblCmpOsSubnet.setIpVersion(subnetInfo.getIpVersion());
        tblCmpOsSubnet.setSubnetpoolId(subnetInfo.getSubnetpoolId());
        tblCmpOsSubnet.setEnableDhcp(BoolUtil.bool2Short(subnetInfo.isEnableDHCP()));
        tblCmpOsSubnet.setIpv6RaMode(subnetInfo.getIpv6RaMode());
        tblCmpOsSubnet.setIpv6AddressMode(subnetInfo.getIpv6AddressMode());
        tblCmpOsSubnet.setGatewayIp(subnetInfo.getGateway());
        tblCmpOsSubnet.setCidr(subnetInfo.getCidr());
        tblCmpOsSubnet.setSegmentId(subnetInfo.getSegmentId());
        tblCmpOsSubnet.setCreatedAt(subnetInfo.getCreatedAt());
        tblCmpOsSubnet.setUpdatedAt(subnetInfo.getUpdatedAt());
        tblCmpOsSubnet.setDescription(subnetInfo.getDescription());
        tblCmpOsSubnet.setRevisionNumber(subnetInfo.getRevisionNumber() == null ? null : subnetInfo.getRevisionNumber().longValue());

        try
        {
            osNeutronRepository.insertSubnet(tblCmpOsSubnet);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateSubnet(tblCmpOsSubnet);
        }

        syncIpallocationpools(cloudId, subnetInfo.getId(), subnetInfo.getPools());

        syncSubnetroutes(cloudId, subnetInfo.getId(), subnetInfo.getHostRoutes());

        syncDnsnameservers(cloudId, subnetInfo.getId(), subnetInfo.getDnsNames());

        syncSubnetServiceTypes(cloudId, subnetInfo.getId(), subnetInfo.getServiceTypes());
    }

    private void syncIpallocationpools(String cloudId, String subnetId, List<OSPool> pools)
    {
        TblCmpOsIpallocationpoolsExample example = new TblCmpOsIpallocationpoolsExample();
        TblCmpOsIpallocationpoolsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsIpallocationpools> tblCmpOsIpallocationpools = osNeutronRepository.getIpallocationpools(example);
        Set<TblCmpOsIpallocationpools> tblCmpOsIpallocationpoolSet = new HashSet<>(tblCmpOsIpallocationpools);

        if (! CollectionUtils.isEmpty(pools))
        {
            for (OSPool pool : pools)
            {
                TblCmpOsIpallocationpools tblCmpOsIpallocationpool = new TblCmpOsIpallocationpools();
                tblCmpOsIpallocationpool.setCloudId(cloudId);
                tblCmpOsIpallocationpool.setEeStatus(SYNCING);
                tblCmpOsIpallocationpool.setSubnetId(subnetId);
                tblCmpOsIpallocationpool.setFirstIp(pool.getStart());
                tblCmpOsIpallocationpool.setLastIp(pool.getEnd());
                try
                {
                    osNeutronRepository.insertIpallocationpool(tblCmpOsIpallocationpool);
                }
                catch (DuplicateKeyException e)
                {
                    osNeutronRepository.updateIpallocationpool(tblCmpOsIpallocationpool);
                }

                for (TblCmpOsIpallocationpools item : tblCmpOsIpallocationpoolSet)
                {
                    if (item.getFirstIp().equals(pool.getStart()) && item.getLastIp().equals(pool.getEnd()))
                    {
                        tblCmpOsIpallocationpoolSet.remove(item);
                        break;
                    }
                }
            }
        }

        tblCmpOsIpallocationpoolSet.forEach(tblCmpOsIpallocationpool -> {
            tblCmpOsIpallocationpool.setEeStatus(REMOVED);
            osNeutronRepository.updateIpallocationpool(tblCmpOsIpallocationpool);
        });
    }

    private void syncSubnetroutes(String cloudId, String subnetId, List<OSHostRoute> hostRoutes)
    {
        TblCmpOsSubnetroutesExample example = new TblCmpOsSubnetroutesExample();
        TblCmpOsSubnetroutesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsSubnetroutes> tblCmpOsSubnetroutes = osNeutronRepository.getSubnetroutes(example);
        Set<TblCmpOsSubnetroutes> tblCmpOsSubnetrouteSet = new HashSet<>(tblCmpOsSubnetroutes);

        if (! CollectionUtils.isEmpty(hostRoutes))
        {
            for (OSHostRoute hostRoute : hostRoutes)
            {
                TblCmpOsSubnetroutes tblCmpOsSubnetroute = new TblCmpOsSubnetroutes();
                tblCmpOsSubnetroute.setCloudId(cloudId);
                tblCmpOsSubnetroute.setEeStatus(SYNCING);
                tblCmpOsSubnetroute.setSubnetId(subnetId);
                tblCmpOsSubnetroute.setDestination(hostRoute.getDestination());
                tblCmpOsSubnetroute.setNexthop(hostRoute.getNexthop());
                try
                {
                    osNeutronRepository.insertSubnetroute(tblCmpOsSubnetroute);
                }
                catch (DuplicateKeyException e)
                {
                    osNeutronRepository.updateSubnetroute(tblCmpOsSubnetroute);
                }

                for (TblCmpOsSubnetroutes item : tblCmpOsSubnetrouteSet)
                {
                    if (item.getDestination().equals(hostRoute.getDestination()) && item.getNexthop().equals(hostRoute.getNexthop()))
                    {
                        tblCmpOsSubnetrouteSet.remove(item);
                        break;
                    }
                }
            }
        }

        tblCmpOsSubnetrouteSet.forEach(tblCmpOsSubnetroute -> {
            tblCmpOsSubnetroute.setEeStatus(REMOVED);
            osNeutronRepository.updateSubnetroute(tblCmpOsSubnetroute);
        });
    }

    private void syncDnsnameservers(String cloudId, String subnetId, List<String> dnsNames)
    {
        TblCmpOsDnsnameserversExample example = new TblCmpOsDnsnameserversExample();
        TblCmpOsDnsnameserversExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsDnsnameservers> tblCmpOsDnsnameservers = osNeutronRepository.getDnsnameservers(example);
        Set<TblCmpOsDnsnameservers> tblCmpOsDnsnameserverSet = new HashSet<>(tblCmpOsDnsnameservers);

        if (! CollectionUtils.isEmpty(dnsNames))
        {
            for (String dnsName : dnsNames)
            {
                TblCmpOsDnsnameservers tblCmpOsDnsnameserver = new TblCmpOsDnsnameservers();
                tblCmpOsDnsnameserver.setCloudId(cloudId);
                tblCmpOsDnsnameserver.setEeStatus(SYNCING);
                tblCmpOsDnsnameserver.setSubnetId(subnetId);
                tblCmpOsDnsnameserver.setAddress(dnsName);
                try
                {
                    osNeutronRepository.insertDnsnameserver(tblCmpOsDnsnameserver);
                }
                catch (DuplicateKeyException e)
                {
                    osNeutronRepository.updateDnsnameserver(tblCmpOsDnsnameserver);
                }

                for (TblCmpOsDnsnameservers item : tblCmpOsDnsnameserverSet)
                {
                    if (item.getAddress().equals(dnsName))
                    {
                        tblCmpOsDnsnameserverSet.remove(item);
                        break;
                    }
                }
            }
        }

        tblCmpOsDnsnameserverSet.forEach(tblCmpOsDnsnameserver -> {
            tblCmpOsDnsnameserver.setEeStatus(REMOVED);
            osNeutronRepository.updateDnsnameserver(tblCmpOsDnsnameserver);
        });
    }

    private void syncSubnetServiceTypes(String cloudId, String subnetId, List<String> serviceTypes)
    {
        TblCmpOsSubnetServiceTypesExample example = new TblCmpOsSubnetServiceTypesExample();
        TblCmpOsSubnetServiceTypesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpOsSubnetServiceTypes> tblCmpOsSubnetServiceTypes = osNeutronRepository.getSubnetServiceTypes(example);
        Set<TblCmpOsSubnetServiceTypes> tblCmpOsSubnetServiceTypeSet = new HashSet<>(tblCmpOsSubnetServiceTypes);

        if (! CollectionUtils.isEmpty(serviceTypes))
        {
            for (String serviceType : serviceTypes)
            {
                TblCmpOsSubnetServiceTypes tblCmpOsSubnetServiceType = new TblCmpOsSubnetServiceTypes();
                tblCmpOsSubnetServiceType.setCloudId(cloudId);
                tblCmpOsSubnetServiceType.setEeStatus(SYNCING);
                tblCmpOsSubnetServiceType.setSubnetId(subnetId);
                tblCmpOsSubnetServiceType.setServiceType(serviceType);
                try
                {
                    osNeutronRepository.insertSubnetServiceType(tblCmpOsSubnetServiceType);
                }
                catch (DuplicateKeyException e)
                {
                    osNeutronRepository.updateSubnetServiceType(tblCmpOsSubnetServiceType);
                }

                for (TblCmpOsSubnetServiceTypes item : tblCmpOsSubnetServiceTypeSet)
                {
                    if (item.getServiceType().equals(serviceType))
                    {
                        tblCmpOsSubnetServiceTypeSet.remove(item);
                        break;
                    }
                }
            }
        }

        tblCmpOsSubnetServiceTypeSet.forEach(tblCmpOsSubnetServiceType -> {
            tblCmpOsSubnetServiceType.setEeStatus(REMOVED);
            osNeutronRepository.updateSubnetServiceType(tblCmpOsSubnetServiceType);
        });
    }

    public void syncRouters(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/routers";
        try
        {
            Set<String> routerIds = osNeutronRepository.getRouterIds(tblCloudInfo.getCloudId());
            if (routerIds == null)
            {
                routerIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSRoutersWithDetailsRsp routersWithDetailsRsp = gson.fromJson(result, OSRoutersWithDetailsRsp.class);
                if (routersWithDetailsRsp != null && ! CollectionUtils.isEmpty(routersWithDetailsRsp.getRouters()))
                {
                    for (OSRouterInfo routerInfo : routersWithDetailsRsp.getRouters())
                    {
                        updateRouterToDB(tblCloudInfo.getCloudId(), routerInfo, null, null);

                        deleteRouterportByRouterid(tblCloudInfo.getCloudId(), routerInfo.getId());

                        if (routerInfo.getExternalGatewayInfo() != null && ! CollectionUtils.isEmpty(routerInfo.getExternalGatewayInfo().getExternalFixedIps()))
                        {
                            for (OSIP fixedIp : routerInfo.getExternalGatewayInfo().getExternalFixedIps())
                            {
                                updateRouterportToDB(tblCloudInfo.getCloudId(), routerInfo.getId(), fixedIp);
                            }
                        }

                        deleteRouterrouteByRouterid(tblCloudInfo.getCloudId(), routerInfo.getId());

                        if (! CollectionUtils.isEmpty(routerInfo.getRoutes()))
                        {
                            for (OSHostRoute hostRoute : routerInfo.getRoutes())
                            {
                                updateRouterrouteToDB(tblCloudInfo.getCloudId(), routerInfo.getId(), hostRoute);
                            }
                        }

                        routerIds.remove(routerInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(routerIds))
            {
                for (String routerId : routerIds)
                {
                    osNeutronRepository.deleteRouter(tblCloudInfo.getCloudId(), routerId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} routers error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncRouter(TblCloudInfo tblCloudInfo, String routerId)
    {
        String urlPattern = "/v2.0/routers/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, routerId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteRouter(tblCloudInfo.getCloudId(), routerId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSRouterWithDetailsRsp routerWithDetailsRsp = gson.fromJson(result, OSRouterWithDetailsRsp.class);
                if (routerWithDetailsRsp == null || routerWithDetailsRsp.getRouter() == null)
                {
                    osNeutronRepository.deleteRouter(tblCloudInfo.getCloudId(), routerId);
                    return;
                }

                updateRouterToDB(tblCloudInfo.getCloudId(), routerWithDetailsRsp.getRouter(), null, null);

                deleteRouterportByRouterid(tblCloudInfo.getCloudId(), routerId);

                if (routerWithDetailsRsp.getRouter().getExternalGatewayInfo() != null && ! CollectionUtils.isEmpty(routerWithDetailsRsp.getRouter().getExternalGatewayInfo().getExternalFixedIps()))
                {
                    for (OSIP fixedIp : routerWithDetailsRsp.getRouter().getExternalGatewayInfo().getExternalFixedIps())
                    {
                        updateRouterportToDB(tblCloudInfo.getCloudId(), routerId, fixedIp);
                    }
                }

                deleteRouterrouteByRouterid(tblCloudInfo.getCloudId(), routerId);

                if (! CollectionUtils.isEmpty(routerWithDetailsRsp.getRouter().getRoutes()))
                {
                    for (OSHostRoute hostRoute : routerWithDetailsRsp.getRouter().getRoutes())
                    {
                        updateRouterrouteToDB(tblCloudInfo.getCloudId(), routerId, hostRoute);
                    }
                }

                syncPorts(tblCloudInfo, routerId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} router {} error msg: {}", tblCloudInfo.getName(), routerId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateRouterportToDB(String cloudId, String routerId, OSIP fixedIp)
    {
        TblCmpOsIpallocationsExample example = new TblCmpOsIpallocationsExample();
        TblCmpOsIpallocationsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andIpAddressEqualTo(fixedIp.getIpAddress());
        criteria.andSubnetIdEqualTo(fixedIp.getSubnetId());

        List<TblCmpOsIpallocations> ipallocations = osNeutronRepository.getIpallocations(example);

        if (! CollectionUtils.isEmpty(ipallocations))
        {
            TblCmpOsRouterports tblCmpOsRouterport = new TblCmpOsRouterports();
            tblCmpOsRouterport.setCloudId(cloudId);
            tblCmpOsRouterport.setEeStatus(SYNCING);
            tblCmpOsRouterport.setRouterId(routerId);
            tblCmpOsRouterport.setPortId(ipallocations.get(0).getPortId());
            tblCmpOsRouterport.setPortType("network:router_gateway");

            try
            {
                osNeutronRepository.insertRouterport(tblCmpOsRouterport);
            } catch (DuplicateKeyException e)
            {
                osNeutronRepository.updateRouterport(tblCmpOsRouterport);
            }
        }
    }

    public void deleteRouterportByRouterid(String cloudId, String routerId)
    {
        try
        {
            osNeutronRepository.updateRouterportEeStatusByRouterid(cloudId, routerId, REMOVED);
        } catch (DuplicateKeyException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteRouterrouteByRouterid(String cloudId, String routerId)
    {
        try
        {
            osNeutronRepository.updateRouterrouteEeStatusByRouterid(cloudId, routerId, REMOVED);
        } catch (DuplicateKeyException e)
        {
            e.printStackTrace();
        }
    }

    public void updateRouterrouteToDB(String cloudId, String routerId, OSHostRoute hostRoute)
    {
        TblCmpOsRouterroutes tblCmpOsRouterroute = new TblCmpOsRouterroutes();
        tblCmpOsRouterroute.setCloudId(cloudId);
        tblCmpOsRouterroute.setEeStatus(SYNCING);
        tblCmpOsRouterroute.setRouterId(routerId);
        tblCmpOsRouterroute.setDestination(hostRoute.getDestination());
        tblCmpOsRouterroute.setNexthop(hostRoute.getNexthop());

        try
        {
            osNeutronRepository.insertRouterroute(tblCmpOsRouterroute);
        } catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateRouterroute(tblCmpOsRouterroute);
        }
    }

    public void updateRouterToDB(String cloudId, OSRouterInfo routerInfo, String eeBp, String eeUser)
    {
        TblCmpOsRouters tblCmpOsRouter = new TblCmpOsRouters();
        tblCmpOsRouter.setCloudId(cloudId);
        tblCmpOsRouter.setEeStatus(SYNCING);
        tblCmpOsRouter.setEeBp(eeBp);
        tblCmpOsRouter.setEeUser(eeUser);
        tblCmpOsRouter.setId(routerInfo.getId());
        tblCmpOsRouter.setName(routerInfo.getName());
        tblCmpOsRouter.setProjectId(routerInfo.getProjectId());
        tblCmpOsRouter.setAdminStateUp(BoolUtil.bool2Short(routerInfo.getAdminStateUp()));
        tblCmpOsRouter.setStatus(routerInfo.getStatus());
        tblCmpOsRouter.setDistributed(BoolUtil.bool2Short(routerInfo.getDistributed()));
        tblCmpOsRouter.setHa(BoolUtil.bool2Short(routerInfo.getHa()));
        tblCmpOsRouter.setAvailabilityZoneHints(gson.toJson(routerInfo.getAvailabilityZoneHints()));

        tblCmpOsRouter.setCreatedAt(routerInfo.getCreatedAt());
        tblCmpOsRouter.setUpdatedAt(routerInfo.getUpdatedAt());
        tblCmpOsRouter.setDescription(routerInfo.getDescription());
        tblCmpOsRouter.setRevisionNumber(routerInfo.getRevisionNumber() == null ? null : routerInfo.getRevisionNumber().longValue());

        tblCmpOsRouter.setEnableSnat(routerInfo.getExternalGatewayInfo() == null ? null : BoolUtil.bool2Short(routerInfo.getExternalGatewayInfo().getEnableSnat()));
        try
        {
            osNeutronRepository.insertRouter(tblCmpOsRouter);
        } catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateRouterEeSelective(tblCmpOsRouter);
        }
    }

    public void syncFloatingIps(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/floatingips";
        try
        {
            Set<String> floatingipIds = osNeutronRepository.getFloatingipIds(tblCloudInfo.getCloudId());
            if (floatingipIds == null)
            {
                floatingipIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSFloatingIpsWithDetailsRsp floatingIpsWithDetailsRsp = gson.fromJson(result, OSFloatingIpsWithDetailsRsp.class);
                if (floatingIpsWithDetailsRsp != null && ! CollectionUtils.isEmpty(floatingIpsWithDetailsRsp.getFloatingips()))
                {
                    for (OSFloatingIpInfo floatingIpInfo : floatingIpsWithDetailsRsp.getFloatingips())
                    {

                        updateFloatingIpToDB(tblCloudInfo.getCloudId(), floatingIpInfo, null, null);

                        syncPortForwardings(tblCloudInfo, floatingIpInfo.getId());

                        floatingipIds.remove(floatingIpInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(floatingipIds))
            {
                for (String floatingipId : floatingipIds)
                {
                    osNeutronRepository.deleteFloatingIp(tblCloudInfo.getCloudId(), floatingipId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} floatingips error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFloatingIp(TblCloudInfo tblCloudInfo, String floatingIpId)
    {
        String urlPattern = "/v2.0/floatingips/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, floatingIpId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteFloatingIp(tblCloudInfo.getCloudId(), floatingIpId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSFloatingIpWithDetailsRsp floatingIpWithDetailsRsp = gson.fromJson(result, OSFloatingIpWithDetailsRsp.class);
                if (floatingIpWithDetailsRsp == null || floatingIpWithDetailsRsp.getFloatingip() == null)
                {
                    osNeutronRepository.deleteFloatingIp(tblCloudInfo.getCloudId(), floatingIpId);
                    return;
                }

                updateFloatingIpToDB(tblCloudInfo.getCloudId(), floatingIpWithDetailsRsp.getFloatingip(), null, null);

                syncPortForwardings(tblCloudInfo, floatingIpId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} floatingip {} error msg: {}", tblCloudInfo.getName(), floatingIpId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFloatingIpToDB(String cloudId, OSFloatingIpInfo floatingIpInfo, String eeBp, String eeUser)
    {
        TblCmpOsFloatingips tblCmpOsFloatingip = new TblCmpOsFloatingips();
        tblCmpOsFloatingip.setCloudId(cloudId);
        tblCmpOsFloatingip.setEeStatus(SYNCING);
        tblCmpOsFloatingip.setEeBp(eeBp);
        tblCmpOsFloatingip.setEeUser(eeUser);
        tblCmpOsFloatingip.setId(floatingIpInfo.getId());
        tblCmpOsFloatingip.setProjectId(floatingIpInfo.getProjectId());
        tblCmpOsFloatingip.setFloatingIpAddress(floatingIpInfo.getFloatingIpAddress());
        tblCmpOsFloatingip.setFloatingNetworkId(floatingIpInfo.getFloatingNetworkId());
        tblCmpOsFloatingip.setFixedIpAddress(floatingIpInfo.getFixedIpAddress());
        tblCmpOsFloatingip.setRouterId(floatingIpInfo.getRouterId());
        tblCmpOsFloatingip.setStatus(floatingIpInfo.getStatus());
        tblCmpOsFloatingip.setDnsName(floatingIpInfo.getDnsName());
        tblCmpOsFloatingip.setDnsDomain(floatingIpInfo.getDnsDomain());
        tblCmpOsFloatingip.setCreatedAt(floatingIpInfo.getCreatedAt());
        tblCmpOsFloatingip.setUpdatedAt(floatingIpInfo.getUpdatedAt());
        tblCmpOsFloatingip.setDescription(floatingIpInfo.getDescription());
        tblCmpOsFloatingip.setRevisionNumber(floatingIpInfo.getRevisionNumber() == null ? null : floatingIpInfo.getRevisionNumber().longValue());
        tblCmpOsFloatingip.setPolicyId(floatingIpInfo.getQosPolicyId());

        try
        {
            osNeutronRepository.insertFloatingip(tblCmpOsFloatingip);
        } catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateFloatingipEeSelective(tblCmpOsFloatingip);
        }
    }

    public void syncPortForwardings(TblCloudInfo tblCloudInfo, String floatingipId)
    {
        String urlPattern = "/v2.0/floatingips/%s/port_forwardings";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, floatingipId));
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSPortForwardingsWithDetailsRsp portForwardingsWithDetailsRsp = gson.fromJson(result, OSPortForwardingsWithDetailsRsp.class);
                if (portForwardingsWithDetailsRsp == null || CollectionUtils.isEmpty(portForwardingsWithDetailsRsp.getPortForwardings()))
                {
                    return;
                }

                for (OSPortForwardingInfo portForwardingInfo : portForwardingsWithDetailsRsp.getPortForwardings())
                {
                    updatePortforwardingToDB(tblCloudInfo.getCloudId(), portForwardingInfo, null, null);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} {} portforwardings error msg: {}", tblCloudInfo.getName(), floatingipId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updatePortforwardingToDB(String cloudId, OSPortForwardingInfo portForwardingInfo, String eeBp, String eeUser)
    {
        TblCmpOsPortforwardings tblCmpOsPortforwarding = new TblCmpOsPortforwardings();
        tblCmpOsPortforwarding.setCloudId(cloudId);
        tblCmpOsPortforwarding.setEeStatus(SYNCING);
        tblCmpOsPortforwarding.setEeBp(eeBp);
        tblCmpOsPortforwarding.setEeUser(eeUser);
        tblCmpOsPortforwarding.setId(portForwardingInfo.getId());
        tblCmpOsPortforwarding.setInternalNeutronPortId(portForwardingInfo.getInternalPortId());
        tblCmpOsPortforwarding.setProtocol(portForwardingInfo.getProtocol());
        tblCmpOsPortforwarding.setExternalPort(portForwardingInfo.getExternalPort());

        try
        {
            osNeutronRepository.insertPortforwarding(tblCmpOsPortforwarding);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updatePortforwarding(tblCmpOsPortforwarding);
        }
    }

    public void syncSecurityGroups(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/security-groups";
        try
        {
            Set<String> securitygroupIds = osNeutronRepository.getSecuritygroupIds(tblCloudInfo.getCloudId());
            if (securitygroupIds == null)
            {
                securitygroupIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSecurityGroupsWithDetailsRsp securityGroupsWithDetailsRsp = gson.fromJson(result, OSSecurityGroupsWithDetailsRsp.class);
                if (securityGroupsWithDetailsRsp != null && ! CollectionUtils.isEmpty(securityGroupsWithDetailsRsp.getSecurityGroups()))
                {
                    for (OSSecurityGroupInfo securityGroupInfo : securityGroupsWithDetailsRsp.getSecurityGroups())
                    {
                        updateSecurityGroupToDB(tblCloudInfo.getCloudId(), securityGroupInfo, null, null, null);

                        syncSecurityGroupRules(tblCloudInfo, securityGroupInfo.getId());

                        securitygroupIds.remove(securityGroupInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(securitygroupIds))
            {
                for (String securitygroupId : securitygroupIds)
                {
                    osNeutronRepository.deleteSecurityGroup(tblCloudInfo.getCloudId(), securitygroupId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} security groups error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSecurityGroup(TblCloudInfo tblCloudInfo, String securityGroupId)
    {
        String urlPattern = "/v2.0/security-groups/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, securityGroupId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteSecurityGroup(tblCloudInfo.getCloudId(), securityGroupId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSecurityGroupWithDetailsRsp securityGroupWithDetailsRsp = gson.fromJson(result, OSSecurityGroupWithDetailsRsp.class);
                if (securityGroupWithDetailsRsp == null || securityGroupWithDetailsRsp.getSecurityGroup() == null)
                {
                    osNeutronRepository.deleteSecurityGroup(tblCloudInfo.getCloudId(), securityGroupId);
                    return;
                }

                updateSecurityGroupToDB(tblCloudInfo.getCloudId(), securityGroupWithDetailsRsp.getSecurityGroup(), null, null, null);

                syncSecurityGroupRules(tblCloudInfo, securityGroupId);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} security group {} error msg: {}", tblCloudInfo.getName(), securityGroupId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSecurityGroupToDB(String cloudId, OSSecurityGroupInfo securityGroupInfo, String eeBp, String eeUser, String eeName)
    {
        TblCmpOsSecuritygroups tblCmpOsSecuritygroup = new TblCmpOsSecuritygroups();
        tblCmpOsSecuritygroup.setCloudId(cloudId);
        tblCmpOsSecuritygroup.setEeStatus(SYNCING);
        tblCmpOsSecuritygroup.setEeName(eeName);
        tblCmpOsSecuritygroup.setEeBp(eeBp);
        tblCmpOsSecuritygroup.setEeUser(eeUser);
        tblCmpOsSecuritygroup.setId(securityGroupInfo.getId());
        tblCmpOsSecuritygroup.setProjectId(securityGroupInfo.getTenantId());
        tblCmpOsSecuritygroup.setProjectId(securityGroupInfo.getProjectId());
        tblCmpOsSecuritygroup.setCreatedAt(securityGroupInfo.getCreatedAt());
        tblCmpOsSecuritygroup.setUpdatedAt(securityGroupInfo.getUpdatedAt());
        tblCmpOsSecuritygroup.setRevisionNumber(securityGroupInfo.getRevisionNumber() == null ? null : securityGroupInfo.getRevisionNumber().longValue());
        tblCmpOsSecuritygroup.setName(securityGroupInfo.getName());
        tblCmpOsSecuritygroup.setDescription(securityGroupInfo.getDescription());
        tblCmpOsSecuritygroup.setShared(BoolUtil.bool2Short(securityGroupInfo.getShared()));

        try
        {
            osNeutronRepository.insertSecuritygroup(tblCmpOsSecuritygroup);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateSecuritygroup(tblCmpOsSecuritygroup);
        }
    }

    public void syncSecurityGroupRules(TblCloudInfo tblCloudInfo, String securityGroupId)
    {
        String urlPattern = "/v2.0/security-group-rules?security_group_id=%s";
        String url = "/v2.0/security-group-rules";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
            ResponseEntity<String> response;
            Set<String> securitygroupruleIds = new HashSet<>();
            if (StringUtils.isNotEmpty(securityGroupId))
            {
                response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, securityGroupId));
                securitygroupruleIds = osNeutronRepository.getSecuritygroupruleIds(tblCloudInfo.getCloudId(), securityGroupId);
            }
            else {
                response = osClientV3.sendHttpGetRequestToCloud(url);
            }
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSecurityGroupRulesWithDetailsRsp securityGroupRulesWithDetailsRsp = gson.fromJson(result, OSSecurityGroupRulesWithDetailsRsp.class);
                if (securityGroupRulesWithDetailsRsp != null && ! CollectionUtils.isEmpty(securityGroupRulesWithDetailsRsp.getSecurityGroupRules()))
                {
                    for (OSSecurityGroupRuleInfo securityGroupRuleInfo : securityGroupRulesWithDetailsRsp.getSecurityGroupRules())
                    {
                        updateSecurityGroupRuleToDB(tblCloudInfo.getCloudId(), securityGroupRuleInfo, null, null);

                        securitygroupruleIds.remove(securityGroupRuleInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(securitygroupruleIds))
            {
                for (String securitygroupruleId : securitygroupruleIds)
                {
                    osNeutronRepository.deleteSecurityGroupRule(tblCloudInfo.getCloudId(), securitygroupruleId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} {} security group rules error msg: {}", tblCloudInfo.getName(), securityGroupId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSecurityGroupRule(TblCloudInfo tblCloudInfo, String securityGroupRuleId)
    {
        String urlPattern = "/v2.0/security-group-rules/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, securityGroupRuleId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osNeutronRepository.deleteSecurityGroupRule(tblCloudInfo.getCloudId(), securityGroupRuleId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSSecurityGroupRuleWithDetailsRsp securityGroupRuleWithDetailsRsp = gson.fromJson(result, OSSecurityGroupRuleWithDetailsRsp.class);
                if (securityGroupRuleWithDetailsRsp == null || securityGroupRuleWithDetailsRsp.getSecurityGroupRule() == null)
                {
                    osNeutronRepository.deleteSecurityGroupRule(tblCloudInfo.getCloudId(), securityGroupRuleId);
                    return;
                }

                updateSecurityGroupRuleToDB(tblCloudInfo.getCloudId(), securityGroupRuleWithDetailsRsp.getSecurityGroupRule(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} security group rule {} error msg: {}", tblCloudInfo.getName(), securityGroupRuleId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSecurityGroupRuleToDB(String cloudId, OSSecurityGroupRuleInfo securityGroupRuleInfo, String eeBp, String eeUser)
    {
        TblCmpOsSecuritygrouprules tblCmpOsSecuritygrouprule = new TblCmpOsSecuritygrouprules();
        tblCmpOsSecuritygrouprule.setCloudId(cloudId);
        tblCmpOsSecuritygrouprule.setEeStatus(SYNCING);
        tblCmpOsSecuritygrouprule.setEeBp(eeBp);
        tblCmpOsSecuritygrouprule.setEeUser(eeUser);
        tblCmpOsSecuritygrouprule.setRemoteGroupId(securityGroupRuleInfo.getRemoteGroupId());
        tblCmpOsSecuritygrouprule.setDirection(securityGroupRuleInfo.getDirection());
        tblCmpOsSecuritygrouprule.setProtocol(securityGroupRuleInfo.getProtocol());
        tblCmpOsSecuritygrouprule.setEthertype(securityGroupRuleInfo.getEthertype());
        tblCmpOsSecuritygrouprule.setPortRangeMax(securityGroupRuleInfo.getPortRangeMax());
        tblCmpOsSecuritygrouprule.setSecurityGroupId(securityGroupRuleInfo.getSecurityGroupId());
        tblCmpOsSecuritygrouprule.setProjectId(securityGroupRuleInfo.getTenantId());
        tblCmpOsSecuritygrouprule.setProjectId(securityGroupRuleInfo.getProjectId());
        tblCmpOsSecuritygrouprule.setPortRangeMin(securityGroupRuleInfo.getPortRangeMin());
        tblCmpOsSecuritygrouprule.setRemoteIpPrefix(securityGroupRuleInfo.getRemoteIpPrefix());
        tblCmpOsSecuritygrouprule.setCreatedAt(securityGroupRuleInfo.getCreatedAt());
        tblCmpOsSecuritygrouprule.setUpdatedAt(securityGroupRuleInfo.getUpdatedAt());
        tblCmpOsSecuritygrouprule.setRevisionNumber(securityGroupRuleInfo.getRevisionNumber() == null ? null : securityGroupRuleInfo.getRevisionNumber().longValue());
        tblCmpOsSecuritygrouprule.setId(securityGroupRuleInfo.getId());
        tblCmpOsSecuritygrouprule.setDescription(securityGroupRuleInfo.getDescription());

        try
        {
            osNeutronRepository.insertSecuritygrouprule(tblCmpOsSecuritygrouprule);
        }
        catch (DuplicateKeyException e)
        {
            osNeutronRepository.updateSecuritygrouprule(tblCmpOsSecuritygrouprule);
        }
    }

    public void syncFirewalls(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/fw/firewalls";
        try
        {
            Set<String> firewallIds = esNeutronRepository.getFirewallIds(tblCloudInfo.getCloudId());
            if (firewallIds == null)
            {
                firewallIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallsRsp firewallsRsp = gson.fromJson(result, ESFirewallsRsp.class);
                if (firewallsRsp != null && ! CollectionUtils.isEmpty(firewallsRsp.getFirewalls()))
                {
                    for (ESFirewallInfo firewallInfo : firewallsRsp.getFirewalls())
                    {
                        updateFirewallToDB(tblCloudInfo.getCloudId(), firewallInfo, null, null);

                        firewallIds.remove(firewallInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(firewallIds))
            {
                for (String firewallId : firewallIds)
                {
                    esNeutronRepository.deleteFirewall(tblCloudInfo.getCloudId(), firewallId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewalls error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFirewall(TblCloudInfo tblCloudInfo, String firewallId)
    {
        String urlPattern = "/v2.0/fw/firewalls/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, firewallId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                esNeutronRepository.deleteFirewall(tblCloudInfo.getCloudId(), firewallId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallRsp firewallRsp = gson.fromJson(result, ESFirewallRsp.class);
                if (firewallRsp == null || firewallRsp.getFirewall() == null)
                {
                    esNeutronRepository.deleteFirewall(tblCloudInfo.getCloudId(), firewallId);
                    return;
                }

                updateFirewallToDB(tblCloudInfo.getCloudId(), firewallRsp.getFirewall(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewall {} error msg: {}", tblCloudInfo.getName(), firewallId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFirewallToDB(String cloudId, ESFirewallInfo firewallInfo, String eeBp, String eeUser)
    {
        TblCmpEsFirewalls tblCmpEsFirewall = new TblCmpEsFirewalls();
        tblCmpEsFirewall.setCloudId(cloudId);
        tblCmpEsFirewall.setEeStatus(SYNCING);
        tblCmpEsFirewall.setEeBp(eeBp);
        tblCmpEsFirewall.setEeUser(eeUser);
        tblCmpEsFirewall.setId(firewallInfo.getId());
        tblCmpEsFirewall.setName(firewallInfo.getName());
        tblCmpEsFirewall.setAdminStateUp(BoolUtil.bool2Short(firewallInfo.getAdminStateUp()));
        tblCmpEsFirewall.setStatus(firewallInfo.getStatus());
        tblCmpEsFirewall.setTenantId(firewallInfo.getTenantId());
        tblCmpEsFirewall.setProjectId(firewallInfo.getProjectId());
        tblCmpEsFirewall.setDescription(firewallInfo.getDescription());
        tblCmpEsFirewall.setFirewallPolicyId(firewallInfo.getFirewallPolicyId());

        try
        {
            esNeutronRepository.insertFirewall(tblCmpEsFirewall);
        }
        catch (DuplicateKeyException e)
        {
            esNeutronRepository.updateFirewall(tblCmpEsFirewall);
        }

        syncFirewallBindings(cloudId, firewallInfo);
    }

    public void syncFirewallBindings(String cloudId, ESFirewallInfo firewallInfo)
    {
        Set<String> routerIds = esNeutronRepository.getFirewallBindingIds(cloudId, firewallInfo.getId());
        if (!CollectionUtils.isEmpty(firewallInfo.getRouterIds()))
        {
            for (String routerId : firewallInfo.getRouterIds())
            {
                if (routerIds.contains(routerId))
                {
                    routerIds.remove(routerId);
                }
                else
                {
                    TblCmpEsFirewallBindings tblCmpEsFirewallBinding = new TblCmpEsFirewallBindings();
                    tblCmpEsFirewallBinding.setCloudId(cloudId);
                    tblCmpEsFirewallBinding.setEeStatus(SYNCING);
                    tblCmpEsFirewallBinding.setFirewallId(firewallInfo.getId());
                    tblCmpEsFirewallBinding.setRouterId(routerId);

                    try
                    {
                        esNeutronRepository.insertFirewallBinding(tblCmpEsFirewallBinding);
                    }
                    catch (DuplicateKeyException e)
                    {
                        esNeutronRepository.updateFirewallBinding(tblCmpEsFirewallBinding);
                    }
                }
            }
        }

        if (! CollectionUtils.isEmpty(routerIds))
        {
            for (String routerId : routerIds)
            {
                esNeutronRepository.deleteFirewallBinding(cloudId, firewallInfo.getId(), routerId);
            }
        }
    }

    public void syncFirewallPolicies(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/fw/firewall_policies";
        try
        {
            Set<String> firewallPolicyIds = esNeutronRepository.getFirewallPolicyIds(tblCloudInfo.getCloudId());
            if (firewallPolicyIds == null)
            {
                firewallPolicyIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallPoliciesRsp firewallPoliciesRsp = gson.fromJson(result, ESFirewallPoliciesRsp.class);
                if (firewallPoliciesRsp != null && ! CollectionUtils.isEmpty(firewallPoliciesRsp.getFirewallPolicies()))
                {
                    for (ESFirewallPolicyInfo firewallPolicyInfo : firewallPoliciesRsp.getFirewallPolicies())
                    {
                        updateFirewallPolicyToDB(tblCloudInfo.getCloudId(), firewallPolicyInfo, null, null);

                        firewallPolicyIds.remove(firewallPolicyInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(firewallPolicyIds))
            {
                for (String firewallPolicyId : firewallPolicyIds)
                {
                    esNeutronRepository.deleteFirewallPolicy(tblCloudInfo.getCloudId(), firewallPolicyId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewall policies error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFirewallPolicy(TblCloudInfo tblCloudInfo, String firewallPolicyId)
    {
        String urlPattern = "/v2.0/fw/firewall_policies/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, firewallPolicyId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                esNeutronRepository.deleteFirewallPolicy(tblCloudInfo.getCloudId(), firewallPolicyId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallPolicyRsp firewallPolicyRsp = gson.fromJson(result, ESFirewallPolicyRsp.class);
                if (firewallPolicyRsp == null || firewallPolicyRsp.getFirewallPolicy() == null)
                {
                    esNeutronRepository.deleteFirewallPolicy(tblCloudInfo.getCloudId(), firewallPolicyId);
                    return;
                }

                updateFirewallPolicyToDB(tblCloudInfo.getCloudId(), firewallPolicyRsp.getFirewallPolicy(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewall policy {} error msg: {}", tblCloudInfo.getName(), firewallPolicyId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFirewallPolicyToDB(String cloudId, ESFirewallPolicyInfo firewallPolicyInfo, String eeBp, String eeUser)
    {
        TblCmpEsFirewallPolicies tblCmpEsFirewallPolicy = new TblCmpEsFirewallPolicies();
        tblCmpEsFirewallPolicy.setCloudId(cloudId);
        tblCmpEsFirewallPolicy.setEeStatus(SYNCING);
        tblCmpEsFirewallPolicy.setEeBp(eeBp);
        tblCmpEsFirewallPolicy.setEeUser(eeUser);
        tblCmpEsFirewallPolicy.setId(firewallPolicyInfo.getId());
        tblCmpEsFirewallPolicy.setName(firewallPolicyInfo.getName());
        tblCmpEsFirewallPolicy.setTenantId(firewallPolicyInfo.getTenantId());
        tblCmpEsFirewallPolicy.setProjectId(firewallPolicyInfo.getProjectId());
        tblCmpEsFirewallPolicy.setShared(BoolUtil.bool2Short(firewallPolicyInfo.getShared()));
        tblCmpEsFirewallPolicy.setDescription(firewallPolicyInfo.getDescription());

        try
        {
            esNeutronRepository.insertFirewallPolicy(tblCmpEsFirewallPolicy);
        }
        catch (DuplicateKeyException e)
        {
            esNeutronRepository.updateFirewallPolicy(tblCmpEsFirewallPolicy);
        }
    }

    public void syncFirewallRules(TblCloudInfo tblCloudInfo)
    {
        String url = "/v2.0/fw/firewall_rules";
        try
        {
            Set<String> firewallRuleIds = esNeutronRepository.getFirewallIds(tblCloudInfo.getCloudId());
            if (firewallRuleIds == null)
            {
                firewallRuleIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(url);
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallRulesRsp firewallRulesRsp = gson.fromJson(result, ESFirewallRulesRsp.class);
                if (firewallRulesRsp != null && ! CollectionUtils.isEmpty(firewallRulesRsp.getFirewallRules()))
                {
                    for (ESFirewallRuleInfo firewallRuleInfo : firewallRulesRsp.getFirewallRules())
                    {
                        updateFirewallRuleToDB(tblCloudInfo.getCloudId(), firewallRuleInfo, null, null);

                        firewallRuleIds.remove(firewallRuleInfo.getId());

                        updateFirewallRulePolicyId(tblCloudInfo.getCloudId(), firewallRuleInfo);
                    }
                }
            }

            if (! CollectionUtils.isEmpty(firewallRuleIds))
            {
                for (String firewallRuleId : firewallRuleIds)
                {
                    esNeutronRepository.deleteFirewallRule(tblCloudInfo.getCloudId(), firewallRuleId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewall rules error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncFirewallRule(TblCloudInfo tblCloudInfo, String firewallRuleId)
    {
        String urlPattern = "/v2.0/fw/firewall_rules/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, firewallRuleId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                esNeutronRepository.deleteFirewallRule(tblCloudInfo.getCloudId(), firewallRuleId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                ESFirewallRuleRsp firewallRuleRsp = gson.fromJson(result, ESFirewallRuleRsp.class);
                if (firewallRuleRsp == null || firewallRuleRsp.getFirewallRule() == null)
                {
                    esNeutronRepository.deleteFirewallRule(tblCloudInfo.getCloudId(), firewallRuleId);
                    return;
                }

                updateFirewallRuleToDB(tblCloudInfo.getCloudId(), firewallRuleRsp.getFirewallRule(), null, null);

                updateFirewallRulePolicyId(tblCloudInfo.getCloudId(), firewallRuleRsp.getFirewallRule());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} firewall rule {} error msg: {}", tblCloudInfo.getName(), firewallRuleId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFirewallRuleToDB(String cloudId, ESFirewallRuleInfo firewallRuleInfo, String eeBp, String eeUser)
    {
        TblCmpEsFirewallRules tblCmpEsFirewallRule = new TblCmpEsFirewallRules();
        tblCmpEsFirewallRule.setCloudId(cloudId);
        tblCmpEsFirewallRule.setEeStatus(SYNCING);
        tblCmpEsFirewallRule.setEeBp(eeBp);
        tblCmpEsFirewallRule.setEeUser(eeUser);
        tblCmpEsFirewallRule.setId(firewallRuleInfo.getId());
        tblCmpEsFirewallRule.setName(firewallRuleInfo.getName());
        tblCmpEsFirewallRule.setProtocol(firewallRuleInfo.getProtocol());
        tblCmpEsFirewallRule.setDescription(firewallRuleInfo.getDescription());
        tblCmpEsFirewallRule.setSourcePort(firewallRuleInfo.getSourcePort());
        tblCmpEsFirewallRule.setSourceIpAddress(firewallRuleInfo.getSourceIpAddress());
        tblCmpEsFirewallRule.setDestinationIpAddress(firewallRuleInfo.getDestinationIpAddress());
        tblCmpEsFirewallRule.setFirewallPolicyId(firewallRuleInfo.getFirewallPolicyId());
        tblCmpEsFirewallRule.setPosition(firewallRuleInfo.getPosition());
        tblCmpEsFirewallRule.setDestinationPort(firewallRuleInfo.getDestinationPort());
        tblCmpEsFirewallRule.setTenantId(firewallRuleInfo.getTenantId());
        tblCmpEsFirewallRule.setEnabled(BoolUtil.bool2Short(firewallRuleInfo.getEnabled()));
        tblCmpEsFirewallRule.setAction(firewallRuleInfo.getAction());
        tblCmpEsFirewallRule.setIpVersion(firewallRuleInfo.getIpVersion());
        tblCmpEsFirewallRule.setShared(BoolUtil.bool2Short(firewallRuleInfo.getShared()));
        tblCmpEsFirewallRule.setProjectId(firewallRuleInfo.getProjectId());

        try
        {
            esNeutronRepository.insertFirewallRule(tblCmpEsFirewallRule);
        }
        catch (DuplicateKeyException e)
        {
            esNeutronRepository.updateFirewallRuleSelective(tblCmpEsFirewallRule);
        }
    }

    public void updateFirewallRulePolicyId(String cloudId, ESFirewallRuleInfo firewallRuleInfo)
    {
        if (firewallRuleInfo.getFirewallPolicyId() == null)
        {
            TblCmpEsFirewallRules tblCmpEsFirewallRule = esNeutronRepository.getFirewallRuleById(cloudId, firewallRuleInfo.getId());
            if (tblCmpEsFirewallRule != null && tblCmpEsFirewallRule.getFirewallPolicyId() != null)
            {
                tblCmpEsFirewallRule.setFirewallPolicyId(null);
                esNeutronRepository.updateFirewallRule(tblCmpEsFirewallRule);
            }
        }
    }

    public void syncVolumes(TblCloudInfo tblCloudInfo)
    {
        String urlPattern = "/v3/%s/volumes/detail";
        try
        {
            Set<String> volumeIds = osCinderRepository.getVolumeIds(tblCloudInfo.getCloudId());
            if (volumeIds == null)
            {
                volumeIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3)));
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumesWithDetailsRsp volumesWithDetailsRsp = cinderGson.fromJson(result, OSVolumesWithDetailsRsp.class);
                if (volumesWithDetailsRsp != null && ! CollectionUtils.isEmpty(volumesWithDetailsRsp.getVolumes()))
                {
                    for (OSVolumeInfo volumeInfo : volumesWithDetailsRsp.getVolumes())
                    {
                        updateVolumeToDB(tblCloudInfo.getCloudId(), volumeInfo, null, null);

                        volumeIds.remove(volumeInfo.getId());

                        Set<String> volumeAttachmentIds = osCinderRepository.getVolumeAttachmentIds(tblCloudInfo.getCloudId(), volumeInfo.getId());
                        updateVolumeAttachmentToDB(tblCloudInfo.getCloudId(), volumeInfo.getAttachments(), volumeAttachmentIds);
                        if (! CollectionUtils.isEmpty(volumeAttachmentIds))
                        {
                            for (String volumeAttachmentId : volumeAttachmentIds)
                            {
                                TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment = new TblCmpOsVolumeAttachment();
                                tblCmpOsVolumeAttachment.setCloudId(tblCloudInfo.getCloudId());
                                tblCmpOsVolumeAttachment.setEeStatus(REMOVED);
                                tblCmpOsVolumeAttachment.setId(volumeAttachmentId);
                                try
                                {
                                    osCinderRepository.insertVolumeAttachment(tblCmpOsVolumeAttachment);
                                }
                                catch (DuplicateKeyException e)
                                {
                                    osCinderRepository.updateVolumeAttachment(tblCmpOsVolumeAttachment);
                                }
                            }
                        }
                    }
                }
            }

            if (! CollectionUtils.isEmpty(volumeIds))
            {
                for (String volumeId : volumeIds)
                {
                    osCinderRepository.deleteVolume(tblCloudInfo.getCloudId(), volumeId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volumes error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncVolume(TblCloudInfo tblCloudInfo, String volumeId)
    {
        String urlPattern = "/v3/%s/volumes/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3), volumeId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osCinderRepository.deleteVolume(tblCloudInfo.getCloudId(), volumeId);
                return;
            }else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumeWithDetailsRsp volumeWithDetailsRsp = cinderGson.fromJson(result, OSVolumeWithDetailsRsp.class);
                if (volumeWithDetailsRsp == null || volumeWithDetailsRsp.getVolume() == null)
                {
                    osCinderRepository.deleteVolume(tblCloudInfo.getCloudId(), volumeId);
                    return;
                }

                updateVolumeToDB(tblCloudInfo.getCloudId(), volumeWithDetailsRsp.getVolume(), null, null);

                Set<String> volumeAttachmentIds = osCinderRepository.getVolumeAttachmentIds(tblCloudInfo.getCloudId(), volumeId);
                updateVolumeAttachmentToDB(tblCloudInfo.getCloudId(), volumeWithDetailsRsp.getVolume().getAttachments(), volumeAttachmentIds);
                if (! CollectionUtils.isEmpty(volumeAttachmentIds))
                {
                    for (String volumeAttachmentId : volumeAttachmentIds)
                    {
                        TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment = new TblCmpOsVolumeAttachment();
                        tblCmpOsVolumeAttachment.setCloudId(tblCloudInfo.getCloudId());
                        tblCmpOsVolumeAttachment.setEeStatus(REMOVED);
                        tblCmpOsVolumeAttachment.setId(volumeAttachmentId);
                        try
                        {
                            osCinderRepository.insertVolumeAttachment(tblCmpOsVolumeAttachment);
                        }
                        catch (DuplicateKeyException e)
                        {
                            osCinderRepository.updateVolumeAttachment(tblCmpOsVolumeAttachment);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume {} error msg: {}", tblCloudInfo.getName(), volumeId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateVolumeToDB(String cloudId, OSVolumeInfo volumeInfo, String eeBp, String eeUser)
    {
        TblCmpOsVolumes tblCmpOsVolume = new TblCmpOsVolumes();
        tblCmpOsVolume.setCloudId(cloudId);
        tblCmpOsVolume.setEeStatus(SYNCING);
        tblCmpOsVolume.setEeBp(eeBp);
        tblCmpOsVolume.setEeUser(eeUser);
        tblCmpOsVolume.setMigrationStatus(volumeInfo.getMigrationStatus());
        tblCmpOsVolume.setAvailabilityZone(volumeInfo.getZone());
        tblCmpOsVolume.setHost(volumeInfo.getHost());
        tblCmpOsVolume.setEncryptionKeyId(volumeInfo.getEncryptionKeyId());
        tblCmpOsVolume.setUpdatedAt(volumeInfo.getUpdatedAt());
        tblCmpOsVolume.setReplicationStatus(volumeInfo.getReplicationStatus());
        tblCmpOsVolume.setSnapshotId(volumeInfo.getSnapshotId());
        tblCmpOsVolume.setId(volumeInfo.getId());
        tblCmpOsVolume.setSize(volumeInfo.getSize());
        tblCmpOsVolume.setUserId(volumeInfo.getUserId());
        tblCmpOsVolume.setProjectId(volumeInfo.getTenantId());
        tblCmpOsVolume.setStatus(volumeInfo.getStatus());
        tblCmpOsVolume.setDisplayDescription(volumeInfo.getDescription());
        tblCmpOsVolume.setMultiattch(BoolUtil.bool2Short(volumeInfo.getMultiattach()));
        tblCmpOsVolume.setSourceVolid(volumeInfo.getSourceVolid());
        tblCmpOsVolume.setConsistencygroupId(volumeInfo.getConsistencygroupId());
        tblCmpOsVolume.setNameId(volumeInfo.getNameId());
        tblCmpOsVolume.setDisplayName(volumeInfo.getName());
        tblCmpOsVolume.setBootable(BoolUtil.bool2Short(volumeInfo.getBootable()));
        tblCmpOsVolume.setCreatedAt(volumeInfo.getCreatedAt());
        tblCmpOsVolume.setVolumeTypeId(volumeInfo.getVolumeTypeId());
        tblCmpOsVolume.setGroupId(volumeInfo.getGroupId());
        tblCmpOsVolume.setProviderId(volumeInfo.getProviderId());
        tblCmpOsVolume.setServiceUuid(volumeInfo.getServiceUuid());
        tblCmpOsVolume.setSharedTargets(BoolUtil.bool2Short(volumeInfo.getSharedTargets()));
        tblCmpOsVolume.setClusterName(volumeInfo.getClusterName());
        tblCmpOsVolume.setVolumeGlanceMetadata(gson.toJson(volumeInfo.getImageMetadata()));

        if (StringUtils.isEmpty(volumeInfo.getVolumeTypeId()) && StringUtils.isNotEmpty(volumeInfo.getVolumeType()))
        {
            TblCmpOsVolumeTypesExample example = new TblCmpOsVolumeTypesExample();
            TblCmpOsVolumeTypesExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andNameEqualTo(volumeInfo.getVolumeType());
            List<TblCmpOsVolumeTypes> volumeTypes = osCinderRepository.getVolumeTypes(example);

            if (! CollectionUtils.isEmpty(volumeTypes))
            {
                tblCmpOsVolume.setVolumeTypeId(volumeTypes.get(0).getId());
            }
        }

        try
        {
            osCinderRepository.insertVolume(tblCmpOsVolume);
        }
        catch (DuplicateKeyException e)
        {
            osCinderRepository.updateVolume(tblCmpOsVolume);
        }
    }

    public void updateVolumeAttachmentToDB(String cloudId, List<OSVolumeAttachmentInfo> volumeAttachmentInfos, Set<String> volumeAttachmentIds)
    {
        if (! CollectionUtils.isEmpty(volumeAttachmentInfos))
        {
            for (OSVolumeAttachmentInfo volumeAttachmentInfo : volumeAttachmentInfos)
            {
                TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment = new TblCmpOsVolumeAttachment();
                tblCmpOsVolumeAttachment.setCloudId(cloudId);
                tblCmpOsVolumeAttachment.setEeStatus(SYNCING);
                tblCmpOsVolumeAttachment.setInstanceUuid(volumeAttachmentInfo.getServerId());
                tblCmpOsVolumeAttachment.setId(volumeAttachmentInfo.getId());
                tblCmpOsVolumeAttachment.setAttachTime(volumeAttachmentInfo.getAttachedAt());
                tblCmpOsVolumeAttachment.setAttachedHost(volumeAttachmentInfo.getHostName());
                tblCmpOsVolumeAttachment.setVolumeId(volumeAttachmentInfo.getVolumeId());
                tblCmpOsVolumeAttachment.setMountpoint(volumeAttachmentInfo.getDevice());
                try
                {
                    osCinderRepository.insertVolumeAttachment(tblCmpOsVolumeAttachment);
                }
                catch (DuplicateKeyException e)
                {
                    osCinderRepository.updateVolumeAttachment(tblCmpOsVolumeAttachment);
                }

                volumeAttachmentIds.remove(volumeAttachmentInfo.getId());
            }
        }
    }

    public void syncSnapshots(TblCloudInfo tblCloudInfo)
    {
        String urlPattern = "/v3/%s/snapshots/detail";
        try
        {
            Set<String> snapshotIds = osCinderRepository.getSnapshotIds(tblCloudInfo.getCloudId());
            if (snapshotIds == null)
            {
                snapshotIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3)));
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumeSnapshotsWithDetailsRsp volumeSnapshotsWithDetailsRsp = cinderGson.fromJson(result, OSVolumeSnapshotsWithDetailsRsp.class);
                if (volumeSnapshotsWithDetailsRsp != null && ! CollectionUtils.isEmpty(volumeSnapshotsWithDetailsRsp.getSnapshots()))
                {
                    for (OSVolumeSnapshotInfo volumeSnapshotInfo : volumeSnapshotsWithDetailsRsp.getSnapshots())
                    {
                        updateSnapshotToDB(tblCloudInfo.getCloudId(), volumeSnapshotInfo, null, null);

                        snapshotIds.remove(volumeSnapshotInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(snapshotIds))
            {
                for (String snapshotId : snapshotIds)
                {
                    osCinderRepository.deleteSnapshot(tblCloudInfo.getCloudId(), snapshotId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} snapshots error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncSnapshot(TblCloudInfo tblCloudInfo, String snapshotId)
    {
        String urlPattern = "/v3/%s/snapshots/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3), snapshotId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osCinderRepository.deleteSnapshot(tblCloudInfo.getCloudId(), snapshotId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumeSnapshotWithDetailsRsp volumeSnapshotWithDetailsRsp = cinderGson.fromJson(result, OSVolumeSnapshotWithDetailsRsp.class);
                if (volumeSnapshotWithDetailsRsp == null || volumeSnapshotWithDetailsRsp.getSnapshot() == null)
                {
                    osCinderRepository.deleteSnapshot(tblCloudInfo.getCloudId(), snapshotId);
                    return;
                }

                updateSnapshotToDB(tblCloudInfo.getCloudId(), volumeSnapshotWithDetailsRsp.getSnapshot(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} snapshot {} error msg: {}", tblCloudInfo.getName(), snapshotId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateSnapshotToDB(String cloudId, OSVolumeSnapshotInfo volumeSnapshotInfo, String eeBp, String eeUser)
    {
        TblCmpOsSnapshots tblCmpOsSnapshot = new TblCmpOsSnapshots();
        tblCmpOsSnapshot.setCloudId(cloudId);
        tblCmpOsSnapshot.setEeStatus(SYNCING);
        tblCmpOsSnapshot.setEeBp(eeBp);
        tblCmpOsSnapshot.setEeUser(eeUser);
        tblCmpOsSnapshot.setStatus(volumeSnapshotInfo.getStatus());
        tblCmpOsSnapshot.setProgress(volumeSnapshotInfo.getProgress());
        tblCmpOsSnapshot.setDisplayDescription(volumeSnapshotInfo.getDescription());
        tblCmpOsSnapshot.setCreatedAt(volumeSnapshotInfo.getCreatedAt());
        tblCmpOsSnapshot.setDisplayName(volumeSnapshotInfo.getName());
        tblCmpOsSnapshot.setUserId(volumeSnapshotInfo.getUserId());
        tblCmpOsSnapshot.setVolumeId(volumeSnapshotInfo.getVolumeId());
        tblCmpOsSnapshot.setProjectId(volumeSnapshotInfo.getProjectId());
        tblCmpOsSnapshot.setVolumeSize(volumeSnapshotInfo.getSize());
        tblCmpOsSnapshot.setId(volumeSnapshotInfo.getId());
        tblCmpOsSnapshot.setUpdatedAt(volumeSnapshotInfo.getUpdatedAt());
        tblCmpOsSnapshot.setGroupSnapshotId(volumeSnapshotInfo.getGroupSnapshotId());

        try
        {
            osCinderRepository.insertSnapshot(tblCmpOsSnapshot);
        }
        catch (DuplicateKeyException e)
        {
            osCinderRepository.updateSnapshot(tblCmpOsSnapshot);
        }
    }

    public void syncVolumeTypes(TblCloudInfo tblCloudInfo)
    {
        String urlPattern = "/v3/%s/types";
        try
        {
            Set<String> typeIds = osCinderRepository.getVolumeTypeIds(tblCloudInfo.getCloudId());
            if (typeIds == null)
            {
                typeIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3)));
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumeTypesWithDetailsRsp volumeTypesWithDetailsRsp = cinderGson.fromJson(result, OSVolumeTypesWithDetailsRsp.class);
                if (volumeTypesWithDetailsRsp != null && ! CollectionUtils.isEmpty(volumeTypesWithDetailsRsp.getVolumeTypes()))
                {
                    for (OSVolumeTypeInfo volumeTypeInfo : volumeTypesWithDetailsRsp.getVolumeTypes())
                    {
                        updateVolumeTypeToDB(tblCloudInfo.getCloudId(), volumeTypeInfo, null, null);

                        typeIds.remove(volumeTypeInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(typeIds))
            {
                for (String typeId : typeIds)
                {
                    TblCmpOsVolumeTypes tblCmpOsVolumeType = new TblCmpOsVolumeTypes();
                    tblCmpOsVolumeType.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsVolumeType.setId(typeId);
                    tblCmpOsVolumeType.setEeStatus(REMOVED);

                    osCinderRepository.updateVolumeType(tblCmpOsVolumeType);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} volume types error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateVolumeTypeToDB(String cloudId, OSVolumeTypeInfo volumeTypeInfo, String eeBp, String eeUser)
    {
        TblCmpOsVolumeTypes tblCmpOsVolumeType = new TblCmpOsVolumeTypes();
        tblCmpOsVolumeType.setCloudId(cloudId);
        tblCmpOsVolumeType.setEeStatus(SYNCING);
        tblCmpOsVolumeType.setEeBp(eeBp);
        tblCmpOsVolumeType.setEeUser(eeUser);
        tblCmpOsVolumeType.setName(volumeTypeInfo.getName());
        tblCmpOsVolumeType.setIsPublic(BoolUtil.bool2Short(volumeTypeInfo.getIsPublic()));
        tblCmpOsVolumeType.setDescription(volumeTypeInfo.getDescription());
        tblCmpOsVolumeType.setId(volumeTypeInfo.getId());
        tblCmpOsVolumeType.setQosSpecsId(volumeTypeInfo.getQosSpecsId());

        try
        {
            osCinderRepository.insertVolumeType(tblCmpOsVolumeType);
        }
        catch (DuplicateKeyException e)
        {
            osCinderRepository.updateVolumeType(tblCmpOsVolumeType);
        }
    }

    public void syncBackups(TblCloudInfo tblCloudInfo)
    {
        String urlPattern = "/v3/%s/backups/detail";
        try
        {
            Set<String> backupIds = osCinderRepository.getBackupIds(tblCloudInfo.getCloudId());
            if (backupIds == null)
            {
                backupIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3)));
            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSVolumeBackupsWithDetailsRsp volumeBackupsWithDetailsRsp = cinderGson.fromJson(result, OSVolumeBackupsWithDetailsRsp.class);
                if (volumeBackupsWithDetailsRsp != null && ! CollectionUtils.isEmpty(volumeBackupsWithDetailsRsp.getBackups()))
                {
                    for (OSVolumeBackupInfo volumeBackupInfo : volumeBackupsWithDetailsRsp.getBackups())
                    {
                        updateBackupToDB(tblCloudInfo.getCloudId(), volumeBackupInfo, null, null);

                        backupIds.remove(volumeBackupInfo.getId());
                    }
                }
            }

            if (! CollectionUtils.isEmpty(backupIds))
            {
                for (String backupId : backupIds)
                {
                    osCinderRepository.deleteBackup(tblCloudInfo.getCloudId(), backupId);
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} backups error msg: {}", tblCloudInfo.getName(), e.getMessage());
            e.printStackTrace();
        }
    }

    public void syncBackup(TblCloudInfo tblCloudInfo, String backupId)
    {
        String urlPattern = "/v3/%s/backups/%s";
        try
        {
            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, OSClientUtil.getOSProject(osClientV3), backupId));
            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                osCinderRepository.deleteBackup(tblCloudInfo.getCloudId(), backupId);
            }
            else if (response.getStatusCode() != HttpStatus.OK)
            {
                return;
            }
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSBackupWithDetailsRsp backupWithDetailsRsp = cinderGson.fromJson(result, OSBackupWithDetailsRsp.class);
                if (backupWithDetailsRsp == null || backupWithDetailsRsp.getBackup() == null)
                {
                    osCinderRepository.deleteBackup(tblCloudInfo.getCloudId(), backupId);
                    return;
                }

                updateBackupToDB(tblCloudInfo.getCloudId(), backupWithDetailsRsp.getBackup(), null, null);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} backup {} error msg: {}", tblCloudInfo.getName(), backupId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateBackupToDB(String cloudId, OSVolumeBackupInfo volumeBackupInfo, String eeBp, String eeUser)
    {
        TblCmpOsBackups tblCmpOsBackup = new TblCmpOsBackups();
        tblCmpOsBackup.setCloudId(cloudId);
        tblCmpOsBackup.setEeStatus(SYNCING);
        tblCmpOsBackup.setEeBp(eeBp);
        tblCmpOsBackup.setEeUser(eeUser);
        tblCmpOsBackup.setStatus(volumeBackupInfo.getStatus());
        tblCmpOsBackup.setObjectCount(volumeBackupInfo.getObjectCount());
        tblCmpOsBackup.setFailReason(volumeBackupInfo.getFailReason());
        tblCmpOsBackup.setDisplayDescription(volumeBackupInfo.getDescription());
        tblCmpOsBackup.setAvailabilityZone(volumeBackupInfo.getZone());
        tblCmpOsBackup.setCreatedAt(volumeBackupInfo.getCreatedAt());
        tblCmpOsBackup.setUpdatedAt(volumeBackupInfo.getUpdatedAt());
        tblCmpOsBackup.setDisplayName(volumeBackupInfo.getName());
        tblCmpOsBackup.setVolumeId(volumeBackupInfo.getVolumeId());
        tblCmpOsBackup.setContainer(volumeBackupInfo.getContainer());
        tblCmpOsBackup.setSize(volumeBackupInfo.getSize());
        tblCmpOsBackup.setId(volumeBackupInfo.getId());
        tblCmpOsBackup.setDataTimestamp(volumeBackupInfo.getDataTimestamp());
        tblCmpOsBackup.setSnapshotId(volumeBackupInfo.getSnapshotId());
        tblCmpOsBackup.setProjectId(volumeBackupInfo.getProjectId());
        tblCmpOsBackup.setUserId(volumeBackupInfo.getUserId());
        tblCmpOsBackup.setEncryptionKeyId(volumeBackupInfo.getEncryptionKeyId());

        try
        {
            osCinderRepository.insertBackup(tblCmpOsBackup);
        }
        catch (DuplicateKeyException e)
        {
            osCinderRepository.updateBackup(tblCmpOsBackup);
        }
    }

    public void syncServerVolumeAttachments(TblCloudInfo tblCloudInfo, String serverId)
    {
        String urlPattern = "/v2.1/servers/%s/os-volume_attachments";
        try
        {
            Set<String> volumeAttachmentIds = osCinderRepository.getServerVolumeAttachmentIds(tblCloudInfo.getCloudId(), serverId);
            if (volumeAttachmentIds == null)
            {
                volumeAttachmentIds = new HashSet<>();
            }

            OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);

            Map<String, String> osHeaders = new HashMap<>();
            osHeaders.put("Openstack-Api-Version", "compute 2.79");

            ResponseEntity<String> response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, serverId), osHeaders);

            if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            {
                response = osClientV3.sendHttpGetRequestToCloud(String.format(urlPattern, serverId));
            }

            if (response.getStatusCode() != HttpStatus.OK) return;
            String result = response.getBody();
            if (StringUtils.isNotEmpty(result))
            {
                OSServerVolumeAttachmentsRsp osServerVolumeAttachmentsRsp = gson.fromJson(result, OSServerVolumeAttachmentsRsp.class);
                if (osServerVolumeAttachmentsRsp != null && ! CollectionUtils.isEmpty(osServerVolumeAttachmentsRsp.getVolumeAttachments()))
                {
                    for (OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment attachment : osServerVolumeAttachmentsRsp.getVolumeAttachments())
                    {
                        updateVolumeAttachmentToDB(tblCloudInfo.getCloudId(), attachment, null, null);
                        volumeAttachmentIds.remove(attachment.getId());

                        cloudService.syncSingleData(tblCloudInfo.getCloudId(), attachment.getVolumeId(), SyncMsg.OS_VOLUME);
                    }

                    updateBlockDeviceMapping(tblCloudInfo, serverId, osServerVolumeAttachmentsRsp.getVolumeAttachments());
                }
            }

            if (! CollectionUtils.isEmpty(volumeAttachmentIds))
            {
                for (String volumeAttachmentId : volumeAttachmentIds)
                {
                    TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment = new TblCmpOsVolumeAttachment();
                    tblCmpOsVolumeAttachment.setCloudId(tblCloudInfo.getCloudId());
                    tblCmpOsVolumeAttachment.setEeStatus(REMOVED);
                    tblCmpOsVolumeAttachment.setId(volumeAttachmentId);
                    try
                    {
                        osCinderRepository.insertVolumeAttachment(tblCmpOsVolumeAttachment);
                    }
                    catch (DuplicateKeyException e)
                    {
                        osCinderRepository.updateVolumeAttachment(tblCmpOsVolumeAttachment);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("sync {} {} volume attachment error msg: {}", tblCloudInfo.getName(), serverId, e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateVolumeAttachmentToDB(String cloudId, OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment attachment, String eeBp, String eeUser)
    {
        TblCmpOsVolumeAttachment tblCmpOsVolumeAttachment = new TblCmpOsVolumeAttachment();
        tblCmpOsVolumeAttachment.setCloudId(cloudId);
        tblCmpOsVolumeAttachment.setEeStatus(SYNCING);
        tblCmpOsVolumeAttachment.setEeBp(eeBp);
        tblCmpOsVolumeAttachment.setEeUser(eeUser);
        tblCmpOsVolumeAttachment.setId(attachment.getId());
        tblCmpOsVolumeAttachment.setInstanceUuid(attachment.getServerId());
        tblCmpOsVolumeAttachment.setVolumeId(attachment.getVolumeId());
        tblCmpOsVolumeAttachment.setMountpoint(attachment.getDevice());

        try
        {
            osCinderRepository.insertVolumeAttachment(tblCmpOsVolumeAttachment);
        }
        catch (DuplicateKeyException e)
        {
            osCinderRepository.updateVolumeAttachment(tblCmpOsVolumeAttachment);
        }
    }

    public void setEeUserForRelatedResources(String cloudId, String serverId)
    {
        TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(cloudId, serverId);

        if (tblCmpOsInstances.getEeStatus().equals(REMOVED) || StringUtils.isEmpty(tblCmpOsInstances.getEeUser()))
        {
            return;
        }

        TblCmpOsVolumeAttachmentExample volumeAttachmentExample = new TblCmpOsVolumeAttachmentExample();
        TblCmpOsVolumeAttachmentExample.Criteria volumeAttachmentCriteria = volumeAttachmentExample.createCriteria();
        volumeAttachmentCriteria.andCloudIdEqualTo(cloudId);
        volumeAttachmentCriteria.andEeStatusNotEqualTo(REMOVED);
        volumeAttachmentCriteria.andInstanceUuidEqualTo(serverId);
        List<TblCmpOsVolumeAttachment> volumeAttachments = osCinderRepository.getVolumeAttachments(volumeAttachmentExample);

        if (! CollectionUtils.isEmpty(volumeAttachments))
        {
            TblCmpOsVolumesExample volumeExample = new TblCmpOsVolumesExample();
            TblCmpOsVolumesExample.Criteria volumeCriteria = volumeExample.createCriteria();
            volumeCriteria.andCloudIdEqualTo(cloudId);
            volumeCriteria.andEeStatusNotEqualTo(REMOVED);
            volumeCriteria.andEeUserIsNull();
            volumeCriteria.andIdIn(volumeAttachments.stream().map(TblCmpOsVolumeAttachment::getId).collect(Collectors.toList()));
            List<TblCmpOsVolumes> volumes = osCinderRepository.getVolumes(volumeExample);

            if (! CollectionUtils.isEmpty(volumes))
            {
                volumes.forEach(volume -> {
                    volume.setEeUser(tblCmpOsInstances.getEeUser());
                    try
                    {
                        osCinderRepository.updateVolume(volume);
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("set volume ee user error: {}", e.getMessage());
                    }
                });
            }
        }

        TblCmpOsPortsExample portExample = new TblCmpOsPortsExample();
        TblCmpOsPortsExample.Criteria portCriteria = portExample.createCriteria();
        portCriteria.andCloudIdEqualTo(cloudId);
        portCriteria.andEeStatusNotEqualTo(REMOVED);
        portCriteria.andEeUserIsNull();
        portCriteria.andDeviceIdEqualTo(serverId);
        List<TblCmpOsPorts> ports = osNeutronRepository.getPorts(portExample);

        if (! CollectionUtils.isEmpty(ports))
        {
            ports.forEach(port -> {
                port.setEeUser(tblCmpOsInstances.getEeUser());
                try
                {
                    osNeutronRepository.updatePort(port);
                }
                catch (Exception e)
                {
                    LOGGER.error("set port ee user error: {}", e.getMessage());
                }
            });
        }
    }

    public void updateBlockDeviceMappingToDB(String cloudId, OSBlockDeviceMappingCreate osBlockDeviceMapping, String instanceUuid, String eeBp, String eeUser)
    {
        try
        {
            TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping = new TblCmpOsBlockDeviceMapping();
            tblCmpOsBlockDeviceMapping.setCloudId(cloudId);
            tblCmpOsBlockDeviceMapping.setUuid(Utils.assignUUId());
            tblCmpOsBlockDeviceMapping.setDeviceName(osBlockDeviceMapping.getDeviceName());
            tblCmpOsBlockDeviceMapping.setDeleteOnTermination(BoolUtil.bool2Short(osBlockDeviceMapping.isDeleteOnTermination()));
            if (osBlockDeviceMapping.getSourceType().equalsIgnoreCase("image"))
            {
                TblCmpOsImages tblCmpOsImage = osGlanceRepository.getImagesById(cloudId, osBlockDeviceMapping.getUuid());

                if (tblCmpOsImage == null)
                {
                    TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
                    if (tblCloudInfo == null) return;
                    syncImage(tblCloudInfo, osBlockDeviceMapping.getUuid());
                    tblCmpOsImage = osGlanceRepository.getImagesById(cloudId, osBlockDeviceMapping.getUuid());
                }

                if (tblCmpOsImage.getProperties().contains("snapshot"))
                {
                    Map<String, String> properties = gson.fromJson(tblCmpOsImage.getProperties(), new TypeToken<Map<String, String>>(){}.getType());
                    if (properties.containsKey("block_device_mapping"))
                    {
                        List<OSBlockDeviceMappingCreate> osBlockDeviceMappingCreates = gson.fromJson(properties.get("block_device_mapping"), new com.google.gson.reflect.TypeToken<List<OSBlockDeviceMappingCreate>>(){}.getType());
                        if (! CollectionUtils.isEmpty(osBlockDeviceMappingCreates))
                        {
                            osBlockDeviceMappingCreates.forEach(osBlockDeviceMappingCreate -> {
                                updateBlockDeviceMappingToDB(cloudId, osBlockDeviceMappingCreate, instanceUuid, eeBp, eeUser);
                            });
                        }
                    }
                }
                else
                {
                    tblCmpOsBlockDeviceMapping.setImageId(osBlockDeviceMapping.getUuid());
                }
            }
            else if (osBlockDeviceMapping.getSourceType().equalsIgnoreCase("volume"))
            {
                tblCmpOsBlockDeviceMapping.setVolumeId(osBlockDeviceMapping.getUuid());
            }

            tblCmpOsBlockDeviceMapping.setVolumeSize(osBlockDeviceMapping.getVolumeSize());
            tblCmpOsBlockDeviceMapping.setInstanceUuid(instanceUuid);
            tblCmpOsBlockDeviceMapping.setSourceType(osBlockDeviceMapping.getSourceType());
            tblCmpOsBlockDeviceMapping.setDestinationType(osBlockDeviceMapping.getDestinationType());
            tblCmpOsBlockDeviceMapping.setGuestFormat(osBlockDeviceMapping.getGuestFormat());
            tblCmpOsBlockDeviceMapping.setDeviceType(osBlockDeviceMapping.getDeviceType());
            tblCmpOsBlockDeviceMapping.setDiskBus(osBlockDeviceMapping.getDiskBus());
            tblCmpOsBlockDeviceMapping.setBootIndex(osBlockDeviceMapping.getBootIndex());

            tblCmpOsBlockDeviceMapping.setTag(osBlockDeviceMapping.getTag());
            tblCmpOsBlockDeviceMapping.setVolumeType(osBlockDeviceMapping.getVolumeType());
            tblCmpOsBlockDeviceMapping.setEeBp(eeBp);
            tblCmpOsBlockDeviceMapping.setEeUser(eeUser);
            tblCmpOsBlockDeviceMapping.setEeStatus(SYNCING);

            try
            {
                osNovaRepository.insertBlockDeviceMapping(tblCmpOsBlockDeviceMapping);
            }
            catch (DuplicateKeyException e)
            {
                osNovaRepository.updateBlockDeviceMapping(tblCmpOsBlockDeviceMapping);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update image error");
        }
    }

    public void updateBlockDeviceMapping(TblCloudInfo tblCloudInfo, String serverId, List<OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment> attachments)
    {
        try
        {
            TblCmpOsBlockDeviceMappingExample volumeIdExistExample = new TblCmpOsBlockDeviceMappingExample();
            TblCmpOsBlockDeviceMappingExample.Criteria volumeIdExistCriteria = volumeIdExistExample.createCriteria();
            volumeIdExistCriteria.andCloudIdEqualTo(tblCloudInfo.getCloudId());
            volumeIdExistCriteria.andInstanceUuidEqualTo(serverId);
            volumeIdExistCriteria.andEeStatusNotEqualTo(REMOVED);
            volumeIdExistCriteria.andVolumeIdIsNotNull();

            List<TblCmpOsBlockDeviceMapping> tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(volumeIdExistExample);

            Map<String, OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment> volumeAttachmentMap =  attachments.stream().collect(Collectors.toMap(OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment::getId, each-> each, (value1, value2) -> value1));

            if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
            {
                for (TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping : tblCmpOsBlockDeviceMappings)
                {
                    OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment volumeAttachment = volumeAttachmentMap.get(tblCmpOsBlockDeviceMapping.getVolumeId());
                    if (tblCmpOsBlockDeviceMapping.getDeleteOnTermination() == null && volumeAttachment.getDeleteOnTermination() != null)
                    {
                        tblCmpOsBlockDeviceMapping.setDeleteOnTermination(BoolUtil.bool2Short(volumeAttachment.getDeleteOnTermination()));
                        try
                        {
                            osNovaRepository.updateBlockDeviceMapping(tblCmpOsBlockDeviceMapping);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            LOGGER.error("update block device mapping error");
                        }
                    }
                    volumeAttachmentMap.remove(tblCmpOsBlockDeviceMapping.getVolumeId());
                }
            }

            TblCmpOsBlockDeviceMappingExample volumeIdNullExample = new TblCmpOsBlockDeviceMappingExample();
            TblCmpOsBlockDeviceMappingExample.Criteria volumeIdNullCriteria = volumeIdNullExample.createCriteria();
            volumeIdNullCriteria.andCloudIdEqualTo(tblCloudInfo.getCloudId());
            volumeIdNullCriteria.andInstanceUuidEqualTo(serverId);
            volumeIdNullCriteria.andEeStatusNotEqualTo(REMOVED);
            volumeIdNullCriteria.andVolumeIdIsNull();

            tblCmpOsBlockDeviceMappings = osNovaRepository.getBlockDeviceMappings(volumeIdNullExample);

            if (! CollectionUtils.isEmpty(tblCmpOsBlockDeviceMappings))
            {
                List<String> volumeIds = new ArrayList<>(volumeAttachmentMap.keySet());
                TblCmpOsVolumesExample example = new TblCmpOsVolumesExample();
                TblCmpOsVolumesExample.Criteria criteria = example.createCriteria();
                criteria.andCloudIdEqualTo(tblCloudInfo.getCloudId());
                criteria.andIdIn(volumeIds);
                criteria.andEeStatusNotEqualTo(REMOVED);
                List<TblCmpOsVolumes> tblCmpOsVolumes = osCinderRepository.getVolumes(example);

                if (! CollectionUtils.isEmpty(tblCmpOsVolumes))
                {
                    Set<TblCmpOsVolumes> volumeSet =  new HashSet<>(tblCmpOsVolumes);
                    for (TblCmpOsBlockDeviceMapping tblCmpOsBlockDeviceMapping : tblCmpOsBlockDeviceMappings)
                    {
                        for (TblCmpOsVolumes tblCmpOsVolume : volumeSet)
                        {
                            if (tblCmpOsVolume.getSize().equals(tblCmpOsBlockDeviceMapping.getVolumeSize()) &&
                                    tblCmpOsVolume.getVolumeTypeId().equals(tblCmpOsBlockDeviceMapping.getVolumeType()))
                            {
                                tblCmpOsBlockDeviceMapping.setVolumeId(tblCmpOsVolume.getId());
                                OSServerVolumeAttachmentsRsp.OSServerVolumeAttachment volumeAttachment = volumeAttachmentMap.get(tblCmpOsBlockDeviceMapping.getVolumeId());
                                if (tblCmpOsBlockDeviceMapping.getDeleteOnTermination() == null && volumeAttachment.getDeleteOnTermination() != null)
                                {
                                    tblCmpOsBlockDeviceMapping.setDeleteOnTermination(BoolUtil.bool2Short(volumeAttachment.getDeleteOnTermination()));
                                }
                                try
                                {
                                    osNovaRepository.updateBlockDeviceMapping(tblCmpOsBlockDeviceMapping);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    LOGGER.error("update block device mapping error");
                                }
                                volumeSet.remove(tblCmpOsVolume);
                                continue;
                            }
                        }
                    }

                    TblCmpOsInstances tblCmpOsInstances = osNovaRepository.getInstanceById(tblCloudInfo.getCloudId(), serverId);
                    if (tblCmpOsInstances != null && tblCmpOsInstances.getChargeType() != null)
                    {
                        tblCmpOsVolumes.forEach(tblCmpOsVolume -> {
                            if (tblCmpOsVolume.getChargeType() == null)
                            {
                                tblCmpOsVolume.setChargeType(tblCmpOsInstances.getChargeType());
                                tblCmpOsVolume.setPeriod(tblCmpOsInstances.getPeriod());
                                tblCmpOsVolume.setPriceUnit(tblCmpOsInstances.getPriceUnit());
                                tblCmpOsVolume.setExpireTime(tblCmpOsInstances.getExpireTime());

                                try
                                {
                                    osCinderRepository.updateVolume(tblCmpOsVolume);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    LOGGER.error("update volume error");
                                }
                            }
                        });
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("update block device mapping error");
        }
    }

    public static class DateDeserializer implements JsonDeserializer<Date>
    {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").withZone(ZoneId.of("UTC"));
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                Instant instant = ZonedDateTime.ofInstant(Instant.from(FORMATTER.parse(json.getAsString())), ZoneId.systemDefault()).toInstant();
                return Date.from(instant);
            } catch (Exception e) {
                throw new JsonParseException(e);
            }
        }
    }
}
