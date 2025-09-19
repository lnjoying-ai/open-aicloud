package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypes;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSVolumeTypeInfo
{
    private static final long serialVersionUID = 1L;
    @JsonProperty("extra_specs")
    @SerializedName("extra_specs")
    private Map<String, Object> extraSpecs;
    @JsonProperty("name")
    @SerializedName("name")
    private String name;
    @JsonProperty("is_public")
    @SerializedName("is_public")
    private Boolean isPublic;
    private String description;
    private String id;
    @JsonProperty("os-volume-type-access:is_public")
    @SerializedName("os-volume-type-access:is_public")
    private Boolean accessIsPublic;
    @JsonProperty("qos_specs_id")
    @SerializedName("qos_specs_id")
    private String qosSpecsId;

    public void setVolumeTypeInfo(TblCmpOsVolumeTypes tblCmpOsVolumeType)
    {
        this.name = tblCmpOsVolumeType.getName();
        this.isPublic = BoolUtil.short2Bool(tblCmpOsVolumeType.getIsPublic());
        this.description = tblCmpOsVolumeType.getDescription();
        this.id = tblCmpOsVolumeType.getId();
        this.qosSpecsId = tblCmpOsVolumeType.getQosSpecsId();
    }
}
