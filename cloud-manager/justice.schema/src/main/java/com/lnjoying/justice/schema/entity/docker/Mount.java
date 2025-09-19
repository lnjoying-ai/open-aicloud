package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:44 PM
 * @Description:
 */
@Data
public class Mount
{
    @SerializedName("Type")
    private String type;
    @SerializedName("Source")
    private String source;
    @SerializedName("Target")
    private String target;
    @SerializedName("ReadOnly")
    private Boolean readOnly;
    @SerializedName("BindOptions")
    private BindOptions bindOptions;
    @SerializedName("VolumeOptions")
    private VolumeOptions volumeOptions;
    @SerializedName("TmpfsOptions")
    private TmpfsOptions tmpfsOptions;
}
