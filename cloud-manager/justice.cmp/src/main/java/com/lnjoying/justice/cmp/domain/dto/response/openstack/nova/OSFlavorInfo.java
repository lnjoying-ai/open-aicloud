package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavorExtraSpecs;
import com.lnjoying.justice.cmp.db.model.TblCmpOsFlavors;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.OSLink;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSFlavorInfo
{
    private String id;
    private String name;
    private String description;

    private Integer ram;
    private Integer vcpus;
    private Integer disk;

    private List<OSLink> links;

    @JsonProperty("OS-FLV-EXT-DATA:ephemeral")
    @SerializedName("OS-FLV-EXT-DATA:ephemeral")
    private int ephemeral;
    @JsonProperty("OS-FLV-DISABLED:disabled")
    @SerializedName("OS-FLV-DISABLED:disabled")
    private Boolean disabled;

    private Object swap;

    @JsonProperty("rxtx_factor")
    @SerializedName("rxtx_factor")
    private float rxtxFactor = 1.0F;

    @JsonProperty("os-flavor-access:is_public")
    @SerializedName("os-flavor-access:is_public")
    private Boolean isPublic = true;

    @JsonProperty("extra_specs")
    @SerializedName("extra_specs")
    private Map<String, String> extraSpecs;

    public void setFlavorInfo(TblCmpOsFlavors tblCmpOsFlavor)
    {
        this.id = tblCmpOsFlavor.getFlavorid();
        this.name = tblCmpOsFlavor.getName();
        this.description = tblCmpOsFlavor.getDescription();

        this.ram = tblCmpOsFlavor.getMemoryMb();
        this.vcpus = tblCmpOsFlavor.getVcpus();
        this.disk = tblCmpOsFlavor.getRootGb();

        this.ephemeral = tblCmpOsFlavor.getEphemeralGb();
        this.disabled = BoolUtil.short2Bool(tblCmpOsFlavor.getDisabled());

        this.swap = tblCmpOsFlavor.getSwap();

        this.rxtxFactor = tblCmpOsFlavor.getRxtxFactor();

        this.isPublic = BoolUtil.short2Bool(tblCmpOsFlavor.getIsPublic());
    }

    public void setFlavorInfoExtraSpecs(List<TblCmpOsFlavorExtraSpecs> tblCmpOsFlavorExtraSpecs)
    {
        if (! CollectionUtils.isEmpty(tblCmpOsFlavorExtraSpecs))
        {
            this.extraSpecs = new HashMap<>();
            for (TblCmpOsFlavorExtraSpecs tblCmpOsFlavorExtraSpec : tblCmpOsFlavorExtraSpecs)
            {
                this.extraSpecs.put(tblCmpOsFlavorExtraSpec.getKey(), tblCmpOsFlavorExtraSpec.getValue());
            }
        }
    }
}
