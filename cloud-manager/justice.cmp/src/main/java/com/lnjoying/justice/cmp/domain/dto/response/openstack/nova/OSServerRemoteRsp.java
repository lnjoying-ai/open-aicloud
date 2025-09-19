package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSServerRemoteRsp
{
    @JsonProperty("remote_console")
    @SerializedName("remote_console")
    private OSServerRemote remoteConsole;

    @Data
    public static class OSServerRemote
    {
        private String protocol;
        private String type;
        private String url;
    }
}
