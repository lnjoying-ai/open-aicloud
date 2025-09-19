package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsIpallocations;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsPorts;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.OSExtIP;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class OSExtServerOsInterfacesRsp
{
    private List<OSExtInterfaceAttachment> interfaceAttachments;

    @SerializedName("total_num")
    @JsonProperty("total_num")
    private long totalNum;

    @Data
    public static class OSExtInterfaceAttachment
    {
        @JsonProperty("port_state")
        @SerializedName("port_state")
        private String portState;
        @JsonProperty("fixed_ips")
        @SerializedName("fixed_ips")
        private List<OSExtIP> fixedIps;
        @JsonProperty("mac_addr")
        @SerializedName("mac_addr")
        private String macAddr;
        @JsonProperty("net_id")
        @SerializedName("net_id")
        private String netId;
        @JsonProperty("port_id")
        @SerializedName("port_id")
        private String portId;
        @JsonProperty("tag")
        @SerializedName("tag")
        private String tag;

        @JsonProperty("net_name")
        @SerializedName("net_name")
        private String netName;

        public void setPort(TblCmpOsPorts tblCmpOsPort)
        {
            this.portState = tblCmpOsPort.getStatus();
            this.macAddr = tblCmpOsPort.getMacAddress();
            this.netId = tblCmpOsPort.getNetworkId();
            this.portId = tblCmpOsPort.getId();
        }

        public void setFixedIps(List<TblCmpOsIpallocations> tblCmpOsIpallocations)
        {
            if (! CollectionUtils.isEmpty(tblCmpOsIpallocations))
            {
                this.fixedIps = new ArrayList<>();
                for (TblCmpOsIpallocations tblCmpOsIpallocation : tblCmpOsIpallocations)
                {
                    OSExtIP fixedIp = new OSExtIP();
                    fixedIp.setSubnetId(tblCmpOsIpallocation.getSubnetId());
                    fixedIp.setIpAddress(tblCmpOsIpallocation.getIpAddress());
                    if (tblCmpOsIpallocation.getIpAddress() != null)
                    {
                        if (tblCmpOsIpallocation.getIpAddress().contains(":"))
                        {
                            fixedIp.setIpVersion(6);
                        }
                        else if (tblCmpOsIpallocation.getIpAddress().contains("."))
                        {
                            fixedIp.setIpVersion(4);
                        }
                    }
                    this.fixedIps.add(fixedIp);
                }
            }
        }

        public void setNetwork(TblCmpOsNetworks tblCmpOsNetwork)
        {
            if (tblCmpOsNetwork != null)
            {
                this.netName = tblCmpOsNetwork.getName();
                if (tblCmpOsNetwork.getIsDefault() != null)
                {
                    this.fixedIps.forEach(fixedIp -> fixedIp.setRouterExternal(true));
                }
            }
        }
    }
}
