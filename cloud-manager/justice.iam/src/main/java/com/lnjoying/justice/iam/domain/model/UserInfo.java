package com.lnjoying.justice.iam.domain.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 20:02
 */

@Data
public class UserInfo
{
    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;
}
