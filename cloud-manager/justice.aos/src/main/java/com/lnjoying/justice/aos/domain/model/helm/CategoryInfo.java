package com.lnjoying.justice.aos.domain.model.helm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblHelmStackInfo;
import com.lnjoying.justice.aos.db.model.TblRepoCategoryInfo;
import com.lnjoying.justice.aos.db.repo.HelmRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/9 10:49
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryInfo
{
    /**
     * category
     */
    @ApiModelProperty(value = "category")
    @JsonProperty("name")
    private String category;

    /**
     * category name displayed on the front end
     */
    @ApiModelProperty(value = "category name displayed on the front end")
    @JsonProperty("show_name")
    private String name;

    /**
     * category id
     */
    @ApiModelProperty(value = "category id")
    private Integer categoryId;

    @ApiModelProperty(value = "description")
    private String description;

    public static CategoryInfo of(TblRepoCategoryInfo info)
    {
        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtils.copyProperties(info, categoryInfo);
        return categoryInfo;
    }
}
