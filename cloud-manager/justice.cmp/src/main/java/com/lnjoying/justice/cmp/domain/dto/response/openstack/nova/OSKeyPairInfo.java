package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cmp.db.model.TblCmpOsKeyPairs;
import lombok.Data;

import java.util.Date;

@Data
public class OSKeyPairInfo
{
    @JsonProperty("created_at")
    @SerializedName("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;
    private boolean deleted;
    @JsonProperty("deleted_at")
    @SerializedName("deleted_at")
    private String deletedAt;
    private String fingerprint;
    private int id;
    private String name;
    @JsonProperty("public_key")
    @SerializedName("public_key")
    private String publicKey;
    @JsonProperty("updated_at")
    @SerializedName("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
    @JsonProperty("user_id")
    @SerializedName("user_id")
    private String userId;
    private String type;
    @JsonProperty("ee_name")
    @SerializedName("ee_name")
    private String eeName;

    public void setKeyPairInfo(TblCmpOsKeyPairs tblCmpOsKeyPair)
    {
        this.createdAt = tblCmpOsKeyPair.getCreatedAt();
        this.deleted = false;
        this.deletedAt = null;
        this.fingerprint = tblCmpOsKeyPair.getFingerprint();
        this.id = tblCmpOsKeyPair.getId();
        this.name = tblCmpOsKeyPair.getEeName();
        this.publicKey = tblCmpOsKeyPair.getPublicKey();
        this.updatedAt = tblCmpOsKeyPair.getUpdatedAt();
        this.userId = tblCmpOsKeyPair.getUserId();
        this.type = tblCmpOsKeyPair.getType();
        this.eeName = tblCmpOsKeyPair.getName();
    }
}
