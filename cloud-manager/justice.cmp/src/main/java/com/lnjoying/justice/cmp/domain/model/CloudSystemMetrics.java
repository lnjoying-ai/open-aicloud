package com.lnjoying.justice.cmp.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

 @Data
public class CloudSystemMetrics
{
    @SerializedName("baremetal_device_num")
    @JsonProperty("baremetal_device_num")
    private int baremetalDeviceNum;

    @SerializedName("baremetal_instance_num")
    @JsonProperty("baremetal_instance_num")
    private int baremetalInstanceNum;

    @SerializedName("hypervisor_node_num")
    @JsonProperty("hypervisor_node_num")
    private int hypervisorNodeNum;

    @SerializedName("vm_instance_num")
    @JsonProperty("vm_instance_num")
    private int vmInstanceNum;
}
