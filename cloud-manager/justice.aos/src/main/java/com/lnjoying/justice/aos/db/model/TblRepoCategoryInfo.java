package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_repo_category_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblRepoCategoryInfo
{
    /**
     * category id
     */
    @ApiModelProperty(value = "category id")
    private Integer categoryId;

    /**
     * category
     */
    @ApiModelProperty(value = "category")
    private String category;

    /**
     * category name displayed on the front end
     */
    @ApiModelProperty(value = "category name displayed on the front end")
    private String name;

    /**
     * As long as the keyword satisfies one of them, it can be considered to be classified as this category
     */
    @ApiModelProperty(value = "As long as the keyword satisfies one of them, it can be considered to be classified as this category")
    private String keyWord;

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

    /**
     * description
     */
    @ApiModelProperty(value = "description")
    private String description;
}