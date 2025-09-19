package com.lnjoying.justice.cmp.domain.dto.request.openstack.glance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OSImageCreateReq
{
    private static final long serialVersionUID = 1L;

    @JsonProperty("container_format")
    @SerializedName("container_format")
    private String containerFormat;
    @JsonProperty("disk_format")
    @SerializedName("disk_format")
    private String diskFormat;
    private String id;
    @JsonProperty("min_disk")
    @SerializedName("min_disk")
    private Integer minDisk;
    @JsonProperty("min_ram")
    @SerializedName("min_ram")
    private Integer minRam;
    private String name;
    @JsonProperty("protected")
    @SerializedName("protected")
    private Boolean protect;
    private List<String> tags;
    @JsonProperty("visibility")
    @SerializedName("visibility")
    private String visibility;
}
