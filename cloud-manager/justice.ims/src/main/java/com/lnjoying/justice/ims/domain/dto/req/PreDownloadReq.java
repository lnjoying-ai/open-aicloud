package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * pre download req
 *
 * @author merak
 **/

@Data
@ApiModel(value = "PreDownloadReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "userName", "bpName"})
public class PreDownloadReq extends BaseReq
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")

    private String registryId;

    @Size(min = 1, message = "nodes cannot be empty")
    private List<String> nodes;

    @Size(min = 1, message = "repos cannot be empty")
    private List<String> repos;
}
