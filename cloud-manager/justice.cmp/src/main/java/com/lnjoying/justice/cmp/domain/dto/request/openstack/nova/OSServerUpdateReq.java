package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.openstack4j.model.compute.Server;

@Data
public class OSServerUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSServerUpdate server;

    @Data
    public static class OSServerUpdate
    {
        private String accessIPv4;
        private String accessIPv6;
        private String name;
        private String hostname;
        @JsonProperty("OS-DCF:diskConfig")
        @SerializedName("OS-DCF:diskConfig")
        private Server.DiskConfig diskConfig;
        private String description;
    }
}
