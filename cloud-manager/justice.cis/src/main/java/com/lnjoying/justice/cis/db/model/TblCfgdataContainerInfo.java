package com.lnjoying.justice.cis.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_cfgdata_container_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblCfgdataContainerInfo {
    @ApiModelProperty(value="")
    private String cfgVolumeId;

    @ApiModelProperty(value="")
    private String dataId;

    @ApiModelProperty(value="")
    private String dataGroup;

    @ApiModelProperty(value="")
    private String dataHash;

    @ApiModelProperty(value="")
    private String userId;

    @ApiModelProperty(value="")
    private String nodeId;

    @ApiModelProperty(value="")
    private String containerId;

    @ApiModelProperty(value="")
    private Integer syncState;

    @ApiModelProperty(value="")
    private Date createTime;

    @ApiModelProperty(value="")
    private Date updateTime;
}