package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.neutron.OSNetworkCreate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.openstack4j.model.compute.Personality;
import org.openstack4j.model.compute.Server;

import java.util.List;
import java.util.Map;

@Data
public class OSServerCreateReq
{
    private static final long serialVersionUID = 1L;
    private OSServerCreate server;

    @Data
    public static class OSServerCreate
    {
        private String flavorRef;
        private String name;
        @JsonProperty("networks")
        @SerializedName("networks")
        private List<OSNetworkCreate> networks;
        private String accessIPv4;
        private String accessIPv6;
        private String adminPass;
        @JsonProperty("availability_zone")
        @SerializedName("availability_zone")
        private String availabilityZone;
        @JsonProperty("block_device_mapping_v2")
        @SerializedName("block_device_mapping_v2")
        private List<OSBlockDeviceMappingCreate> blockDeviceMapping;
        @JsonProperty("config_drive")
        @SerializedName("config_drive")
        private Boolean configDrive;
        private String imageRef;
        @JsonProperty("key_name")
        @SerializedName("key_name")
        private String keyName;
        @JsonProperty("metadata")
        @SerializedName("metadata")
        private Map<String, String> metadata;
        @JsonProperty("OS-DCF:diskConfig")
        @SerializedName("OS-DCF:diskConfig")
        private Server.DiskConfig diskConfig;
        private List<Personality> personality;
        @JsonProperty("security_groups")
        @SerializedName("security_groups")
        private List<OSSecurityGroup> securityGroups;
        @JsonProperty("user_data")
        @SerializedName("user_data")
        private String userData;
        private String description;
        private String hostname;
        private List<String> tags;
        @JsonProperty("trusted_image_certificates")
        @SerializedName("trusted_image_certificates")
        private List<String> trustedImageCertificates;
        private String host;
        @JsonProperty("hypervisor_hostname")
        @SerializedName("hypervisor_hostname")
        private String hypervisorHostname;
        @JsonProperty("os:scheduler_hints")
        @SerializedName("os:scheduler_hints")
        private transient Map<String, Object> schedulerHints;
    }

    @Data
    public static class OSSecurityGroup
    {
        private String name;
    }
}
