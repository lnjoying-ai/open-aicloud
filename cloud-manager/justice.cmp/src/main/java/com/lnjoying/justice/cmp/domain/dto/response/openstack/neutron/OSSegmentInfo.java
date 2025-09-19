package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworks;
import com.lnjoying.justice.cmp.db.model.TblCmpOsNetworksegments;
import com.lnjoying.justice.cmp.db.model.TblCmpOsSubnets;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OSSegmentInfo
{
    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("network_id")
    @SerializedName("network_id")
    private String networkId;
    @JsonProperty("physical_network")
    @SerializedName("physical_network")
    private String physicalNetwork;
    @JsonProperty("network_type")
    @SerializedName("network_type")
    private String networkType;
    @JsonProperty("revision_number")
    @SerializedName("revision_number")
    private Integer revisionNumber;
    @JsonProperty("segmentation_id")
    @SerializedName("segmentation_id")
    private Integer segmentationId;
    private String name;
    private String description;
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    public void setNetworkSegmentInfo(TblCmpOsNetworksegments tblCmpOsNetworksegments)
    {
        this.id = tblCmpOsNetworksegments.getId();
        this.networkId = tblCmpOsNetworksegments.getNetworkId();
        this.physicalNetwork = tblCmpOsNetworksegments.getPhysicalNetwork();
        this.networkType = tblCmpOsNetworksegments.getNetworkType();
        this.revisionNumber = tblCmpOsNetworksegments.getRevisionNumber() == null ? null : tblCmpOsNetworksegments.getRevisionNumber().intValue();
        this.segmentationId = tblCmpOsNetworksegments.getSegmentationId();
        this.name = tblCmpOsNetworksegments.getName();
        this.description = tblCmpOsNetworksegments.getDescription();
        this.createdAt = tblCmpOsNetworksegments.getCreatedAt();
        this.updatedAt = tblCmpOsNetworksegments.getUpdatedAt();
    }
}
