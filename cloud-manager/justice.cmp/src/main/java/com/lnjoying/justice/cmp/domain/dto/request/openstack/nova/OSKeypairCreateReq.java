package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSKeypairCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSKeypairCreate keypair;

    @Data
    public static class OSKeypairCreate
    {
        private String name;
        @JsonProperty("public_key")
        @SerializedName("public_key")
        private String publicKey;
        private String type;
        @JsonProperty("userId")
        @SerializedName("userId")
        private String userId;
    }
}
