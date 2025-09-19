package com.lnjoying.justice.cmp.domain.dto.response.openstack.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSAuthRsp
{
    private OSToken token;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OSToken
    {
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @JsonProperty("issued_at")
        @SerializedName("issued_at")
        private Date issuedAt;
        @JsonProperty("audit_ids")
        @SerializedName("audit_ids")
        private List<String> auditIds;
        private List<String> methods;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @JsonProperty("expires_at")
        @SerializedName("expires_at")
        private Date expiresAt;
        private OSTokenUser user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class OSTokenUser
    {
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @JsonProperty("password_expires_at")
        @SerializedName("password_expires_at")
        private Date passwordExpiresAt;
        private Map<String, String> domain;
        private String id;
        private String name;
    }
}
