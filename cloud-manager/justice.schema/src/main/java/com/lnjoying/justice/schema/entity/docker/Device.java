package com.lnjoying.justice.schema.entity.docker;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:42 PM
 * @Description:
 */
@Data
public class Device
{
    @SerializedName("CgroupPermissions")
    private String cGroupPermissions = "";
    @SerializedName("PathOnHost")
    private String pathOnHost = null;
    @SerializedName("PathInContainer")
    private String pathInContainer = null;
}
