package com.lnjoying.justice.ims.domain.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request base class
 *
 * @author merak
 **/

@Data
public class BaseReq
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
