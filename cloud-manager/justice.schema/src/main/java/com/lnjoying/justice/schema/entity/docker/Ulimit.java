package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:43 PM
 * @Description:
 */
@Data
public class Ulimit
{
    @SerializedName("Name")
    private String name;
    @SerializedName("Soft")
    private Long soft;
    @SerializedName("Hard")
    private Long hard;
}
