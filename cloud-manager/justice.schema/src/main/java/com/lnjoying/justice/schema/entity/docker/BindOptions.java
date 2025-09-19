package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 7:53 PM
 * @Description:
 */
@Data
public class BindOptions
{
    @SerializedName("Propagation")
    String propagation;
}
