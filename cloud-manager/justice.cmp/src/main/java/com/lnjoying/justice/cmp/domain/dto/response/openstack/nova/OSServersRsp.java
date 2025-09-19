package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import lombok.Data;

import java.util.List;

@Data
public class OSServersRsp
{
    private List<OSServerBasicInfo> servers;

    @JsonProperty("servers_links")
    @SerializedName("servers_links")
    private List<OSLink> serversLinks;
}
