package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_repo_app_category_bind_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblRepoAppCategoryBindInfo
{
    /**
     * category id
     */
    @ApiModelProperty(value = "category id")
    private Integer categoryId;

    /**
     * app id
     */
    @ApiModelProperty(value = "app id")
    private String appId;

    /**
     * repo id
     */
    @ApiModelProperty(value = "repo id")
    private Integer repoId;

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