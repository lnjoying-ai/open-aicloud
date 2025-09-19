package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPortforwardings;
import lombok.Data;

@Data
public class OSPortForwardingInfo
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;
    @JsonProperty("internal_port_id")
    @SerializedName("internal_port_id")
    private String internalPortId;
    @JsonProperty("internal_ip_address")
    @SerializedName("internal_ip_address")
    private String internalIpAddress;
    @JsonProperty("internal_port")
    @SerializedName("internal_port")
    private Integer internalPort;
    @JsonProperty("internal_port_range")
    @SerializedName("internal_port_range")
    private String internalPortRange;
    @JsonProperty("external_port")
    @SerializedName("external_port")
    private Integer externalPort;
    @JsonProperty("external_port_range")
    @SerializedName("external_port_range")
    private String externalPortRange;
    @JsonProperty("protocol")
    private String protocol;
    private String description;

    public void setForwardingInfo(TblCmpOsPortforwardings tblCmpOsPortforwardings)
    {
        this.id = tblCmpOsPortforwardings.getId();
        this.internalPortId = tblCmpOsPortforwardings.getInternalNeutronPortId();
        this.protocol = tblCmpOsPortforwardings.getProtocol();
        this.externalPort = tblCmpOsPortforwardings.getExternalPort();
    }
}
