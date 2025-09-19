package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairs;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class OSKeyPairBasicInfo
{
    private String name;
    @JsonProperty("public_key")
    @SerializedName("public_key")
    private String publicKey;
    private String fingerprint;
    private String type;
    @JsonProperty("ee_name")
    @SerializedName("ee_name")
    private String eeName;

    public void setKeyPairBasicInfo(TblCmpOsKeyPairs tblCmpOsKeyPair)
    {
        this.name = tblCmpOsKeyPair.getEeName();
        if (StringUtils.isEmpty(this.name))
        {
            this.name = tblCmpOsKeyPair.getName();
        }
        this.publicKey = tblCmpOsKeyPair.getPublicKey();
        this.fingerprint = tblCmpOsKeyPair.getFingerprint();
        this.type = tblCmpOsKeyPair.getType();
        this.eeName = tblCmpOsKeyPair.getName();
    }
}
