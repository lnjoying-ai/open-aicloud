package com.lnjoying.justice.cmp.domain.dto.request.openstack.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OSAuthReq
{
    private OSAuth auth;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OSAuth
    {
        private OSIdentity identity;
        private OSScope scope;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OSIdentity
    {
        private List<String> methods;
        private OSPassword password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OSPassword
    {
        private OSUser user;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OSUser
    {
        private String name;
        private String id;
        private Map<String, String> domain;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OSScope
    {
        private Map<String, Boolean> system;
        private Map<String, String> project;
    }
}
