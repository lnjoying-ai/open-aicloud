package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_repo_app_version_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblRepoAppVersionInfo
{
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * app id
     */
    @ApiModelProperty(value = "app id")
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
     * version
     */
    @ApiModelProperty(value = "version")
    private String version;

    /**
     * app info
     */
    @ApiModelProperty(value = "app info")
    private Object appInfo;

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