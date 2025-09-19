package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSServerSearchCritical extends CmpSearchCritical
{
    private String accessIpV4;
    private String accessIpV6;
    private Boolean allTenants;
    private String autoDiskConfig;
    private String availabilityZone;
    private String changesSince;
    private String configDrive;
    private String createdAt;
    private Boolean deleted;
    private String description;
    private String flavor;
    private String host;
    private String hostname;
    private String image;
    private String ip;
    private String ip6;
    private String kernelId;
    private String keyName;
    private Integer launchIndex;
    private String launchedAt;
    private Integer limit;
    private String lockedBy;
    private String marker;
    private String name;
    private String node;
    private Integer powerState;
    private Integer progress;
    private String projectId;
    private String ramdiskId;
    private String reservationId;
    private String rootDeviceName;
    private Boolean softDeleted;
    private String sortDir;
    private String sortKey;
    private String status;
    private String taskState;
    private String terminatedAt;
    private String userId;
    private String uuid;
    private String vmState;
    private String notTags;
    private String notTagsAny;
    private String tags;
    private String tagsAny;
    private String changesBefore;
    private Boolean locked;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSServerSearchCritical(String accessIpV4, String accessIpV6, Boolean allTenants, String autoDiskConfig, String availabilityZone,
                                  String changesSince, String configDrive, String createdAt, Boolean deleted, String description,
                                  String flavor, String host, String hostname, String image, String ip,  String ip6, String kernelId,
                                  String keyName, Integer launchIndex, String launchedAt, Integer limit, String lockedBy,
                                  String marker, String name, String node, Integer powerState, Integer progress, String projectId,
                                  String ramdiskId, String reservationId, String rootDeviceName, Boolean softDeleted, String sortDir,
                                  String sortKey, String status, String taskState, String terminatedAt, String userId, String uuid,
                                  String vmState, String notTags, String notTagsAny, String tags, String tagsAny, String changesBefore,
                                  Boolean locked, Integer pageNum, Integer pageSize)
    {
        this.accessIpV4 = accessIpV4;
        this.accessIpV6 = accessIpV6;
        this.allTenants = allTenants;
        this.autoDiskConfig = autoDiskConfig;
        this.availabilityZone = availabilityZone;
        this.changesSince = changesSince;
        this.configDrive = configDrive;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.description = description;
        this.flavor = flavor;
        this.host = host;
        this.hostname = hostname;
        this.image = image;
        this.ip = ip;
        this.ip6 = ip6;
        this.kernelId = kernelId;
        this.keyName = keyName;
        this.launchIndex = launchIndex;
        this.launchedAt = launchedAt;
        this.limit = limit;
        this.lockedBy = lockedBy;
        this.marker = marker;
        this.name = name;
        this.node = node;
        this.powerState = powerState;
        this.progress = progress;
        this.projectId = projectId;
        this.ramdiskId = ramdiskId;
        this.reservationId = reservationId;
        this.rootDeviceName = rootDeviceName;
        this.softDeleted = softDeleted;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.status = status;
        this.taskState = taskState;
        this.terminatedAt = terminatedAt;
        this.userId = userId;
        this.uuid = uuid;
        this.vmState = vmState;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.changesBefore = changesBefore;
        this.locked = locked;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
