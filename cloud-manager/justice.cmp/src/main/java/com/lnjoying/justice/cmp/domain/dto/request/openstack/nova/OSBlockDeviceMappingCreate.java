package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OSBlockDeviceMappingCreate
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("boot_index")
    @SerializedName("boot_index")
    public Integer bootIndex;
    @JsonProperty("delete_on_termination")
    @SerializedName("delete_on_termination")
    public boolean deleteOnTermination;
    @JsonProperty("destination_type")
    @SerializedName("destination_type")
    public String destinationType;
    @JsonProperty("device_name")
    @SerializedName("device_name")
    public String deviceName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("device_type")
    @SerializedName("device_type")
    public String deviceType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("disk_bus")
    @SerializedName("disk_bus")
    public String diskBus;
    @JsonProperty("guest_format")
    @SerializedName("guest_format")
    public String guestFormat;
    @JsonProperty("no_device")
    @SerializedName("no_device")
    public boolean noDevice;
    @JsonProperty("source_type")
    @SerializedName("source_type")
    public String sourceType;
    public String uuid;
    @JsonProperty("volume_size")
    @SerializedName("volume_size")
    public Integer volumeSize;
    public String tag;
    @JsonProperty("volume_type")
    @SerializedName("volume_type")
    public String volumeType;
}
