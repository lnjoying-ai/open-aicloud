package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSPortSearchCritical extends CmpSearchCritical
{
    private Boolean adminStateUp;
    private String hostId;
    private String description;
    private String deviceId;
    private String deviceOwner;
    private List<String> fixedIps;
    private String id;
    private String ipAllocation;
    private String macAddress;
    private String name;
    private String networkId;
    private String projectId;
    private Integer revisionNumber;
    private String sortDir;
    private String sortKey;
    private String status;
    private String tenantId;
    private String tags;
    private String tagsAny;
    private String notTags;
    private String notTagsAny;
    private String fields;
    private Boolean macLearningEnabled;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSPortSearchCritical(Boolean adminStateUp, String hostId, String description, String deviceId, String deviceOwner,
                                List<String> fixedIps, String id, String ipAllocation, String macAddress, String name, String networkId,
                                String projectId, Integer revisionNumber, String sortDir, String sortKey, String status, String tenantId,
                                String tags, String tagsAny, String notTags, String notTagsAny, String fields, Boolean macLearningEnabled,
                                Integer pageNum, Integer pageSize)
    {
        this.adminStateUp = adminStateUp;
        this.hostId = hostId;
        this.description = description;
        this.deviceId = deviceId;
        this.deviceOwner = deviceOwner;
        this.fixedIps = fixedIps;
        this.id = id;
        this.ipAllocation = ipAllocation;
        this.macAddress = macAddress;
        this.name = name;
        this.networkId = networkId;
        this.projectId = projectId;
        this.revisionNumber = revisionNumber;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.status = status;
        this.tenantId = tenantId;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.fields = fields;
        this.macLearningEnabled = macLearningEnabled;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
