package com.lnjoying.justice.cmp.entity.search.openstack;

import com.lnjoying.justice.cmp.entity.search.CmpSearchCritical;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OSNetworkSearchCritical extends CmpSearchCritical
{
    private Boolean adminStateUp;
    private String id;
    private Integer mtu;
    private String name;
    private String projectId;
    private String networkType;
    private String physicalNetwork;
    private Integer segmentationId;
    private Integer revisionNumber;
    private Boolean external;
    private Boolean shared;
    private String status;
    private String tenantId;
    private Boolean vlanTransparent;
    private String description;
    private Boolean isDefault;
    private String tags;
    private String tagsAny;
    private String notTags;
    private String notTagsAny;
    private String sortDir;
    private String sortKey;
    private String fields;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSNetworkSearchCritical(Boolean adminStateUp, String id, Integer mtu, String name, String projectId, String networkType,
                                   String physicalNetwork, Integer segmentationId, Integer revisionNumber, Boolean external,
                                   Boolean shared, String status, String tenantId, Boolean vlanTransparent, String description,
                                   Boolean isDefault, String tags, String tagsAny, String notTags, String notTagsAny, String sortDir,
                                   String sortKey, String fields, Integer pageNum, Integer pageSize)
    {
        this.adminStateUp = adminStateUp;
        this.id = id;
        this.mtu = mtu;
        this.name = name;
        this.projectId = projectId;
        this.networkType = networkType;
        this.physicalNetwork = physicalNetwork;
        this.segmentationId = segmentationId;
        this.revisionNumber = revisionNumber;
        this.external = external;
        this.shared = shared;
        this.status = status;
        this.tenantId = tenantId;
        this.vlanTransparent = vlanTransparent;
        this.description = description;
        this.isDefault = isDefault;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.fields = fields;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
