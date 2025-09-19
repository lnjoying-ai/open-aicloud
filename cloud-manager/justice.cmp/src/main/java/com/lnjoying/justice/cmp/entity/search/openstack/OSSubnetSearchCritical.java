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
public class OSSubnetSearchCritical extends CmpSearchCritical
{
    private String id;
    private String tenantId;
    private String projectId;
    private String name;
    private Boolean enableDhcp;
    private String networkId;
    private Integer ipVersion;
    private String gatewayIp;
    private String cidr;
    private String description;
    private String ipv6AddressMode;
    private String ipv6RaMode;
    private Integer revisionNumber;
    private String segmentId;
    private Boolean shared;
    private String sortDir;
    private String sortKey;
    private String subnetpoolId;
    private String tags;
    private String tagsAny;
    private String notTags;
    private String notTagsAny;
    private Boolean dnsPublishFixedIp;
    private String fields;

    private Integer pageNum = 1;
    private Integer pageSize = 100;

    public OSSubnetSearchCritical(String id, String tenantId, String projectId, String name, Boolean enableDhcp, String networkId,
                                  Integer ipVersion, String gatewayIp, String cidr, String description, String ipv6AddressMode,
                                  String ipv6RaMode, Integer revisionNumber, String segmentId, Boolean shared, String sortDir,
                                  String sortKey, String subnetpoolId, String tags, String tagsAny, String notTags, String notTagsAny,
                                  Boolean dnsPublishFixedIp, String fields, Integer pageNum, Integer pageSize)
    {
        this.id = id;
        this.tenantId = tenantId;
        this.projectId = projectId;
        this.name = name;
        this.enableDhcp = enableDhcp;
        this.networkId = networkId;
        this.ipVersion = ipVersion;
        this.gatewayIp = gatewayIp;
        this.cidr = cidr;
        this.description = description;
        this.ipv6AddressMode = ipv6AddressMode;
        this.ipv6RaMode = ipv6RaMode;
        this.revisionNumber = revisionNumber;
        this.segmentId = segmentId;
        this.shared = shared;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.subnetpoolId = subnetpoolId;
        this.tags = tags;
        this.tagsAny = tagsAny;
        this.notTags = notTags;
        this.notTagsAny = notTagsAny;
        this.dnsPublishFixedIp = dnsPublishFixedIp;
        this.fields = fields;

        if (pageNum != null) this.pageNum = pageNum;
        if (pageSize != null) this.pageSize = pageSize;
    }
}
