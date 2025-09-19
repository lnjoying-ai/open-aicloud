package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_repo_app_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblRepoAppInfo
{
    /**
     * repo_name+”-”+”app_name”
     */
    @ApiModelProperty(value = "repo_name+”-”+”app_name”")
    private String appId;

    /**
     * repo name
     */
    @ApiModelProperty(value = "repo name")
    private String repoName;

    /**
     * app name
     */
    @ApiModelProperty(value = "app name")
    private String appName;

    /**
     * versions
     */
    @ApiModelProperty(value = "versions")
    private Object versions;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date updateTime;
}