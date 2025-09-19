package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFloatingips;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSExtIP
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("ip_address")
    @SerializedName("ip_address")
    private String ipAddress;
    @JsonProperty("subnet_id")
    @SerializedName("subnet_id")
    private String subnetId;


    @JsonProperty("subnet_name")
    @SerializedName("subnet_name")
    private String subnetName;
    @JsonProperty("ip_version")
    @SerializedName("ip_version")
    private Integer ipVersion;
    @JsonProperty("router_external")
    @SerializedName("router_external")
    private Boolean routerExternal;
    @JsonProperty("unreachable")
    @SerializedName("unreachable")
    private Boolean unreachable;
    @JsonProperty("floating_ip_address")
    @SerializedName("floating_ip_address")
    private String floatingIpAddress;

    public void setSubnet(TblCmpOsSubnets tblCmpOsSubnet)
    {
        if (tblCmpOsSubnet != null)
        {
            this.subnetName = tblCmpOsSubnet.getName();
        }
    }

    public void setFloatingIp(List<TblCmpOsFloatingips> tblCmpOsFloatingips)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsFloatingips))
        {
            this.floatingIpAddress = tblCmpOsFloatingips.get(0).getFloatingIpAddress();
        }
    }
}
