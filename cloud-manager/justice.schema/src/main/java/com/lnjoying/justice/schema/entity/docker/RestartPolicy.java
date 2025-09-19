package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:43 PM
 * @Description:
 */
@Data
public class RestartPolicy implements Serializable
{
    @SerializedName("MaximumRetryCount")
    private Integer maximumRetryCount = 0;
    @SerializedName("Name")
    private String name = "";
}
