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
public class OSPortForwardingSearchCritical extends CmpSearchCritical
{
    private String id;
    private String internalPortId;
    private Integer externalPort;
    private String externalPortRange;
    private String protocol;
    private String sortKey;
    private String sortDir;
    private String fields;

    public OSPortForwardingSearchCritical(String id, String internalPortId, Integer externalPort, String externalPortRange,
                                          String protocol, String sortDir, String sortKey, String fields)
    {
        this.id = id;
        this.internalPortId = internalPortId;
        this.externalPort = externalPort;
        this.externalPortRange = externalPortRange;
        this.protocol = protocol;
        this.sortDir = sortDir;
        this.sortKey = sortKey;
        this.fields = fields;
    }
}
