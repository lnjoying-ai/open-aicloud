package com.lnjoying.justice.aos.domain.model.helm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblHelmStackInfo;
import com.lnjoying.justice.aos.db.model.TblRepoCategoryInfo;
import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import com.lnjoying.justice.aos.db.repo.HelmRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/10 9:53
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HelmStackInfo extends TblHelmStackInfo
{

    /**
     * release name
     */
    @JsonProperty("name")
    @ApiModelProperty(value="release name")
    private String releaseName;

    /**
     * app id
     */
    @JsonProperty("chart_app_id")
    @ApiModelProperty(value="app id")
    private String appId;

    private List<CategoryInfo> categories;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public static HelmStackInfo of(TblHelmStackInfo info, HelmRepository helmRepository)
    {
        HelmStackInfo stackInfo = new HelmStackInfo();
        BeanUtils.copyProperties(info, stackInfo);
        List<TblRepoCategoryInfo> tblRepoCategoryInfos = helmRepository.selectAllCategoryInfosByAppId(info.getAppId());
        List<CategoryInfo> categoryInfos = tblRepoCategoryInfos.stream().map(CategoryInfo::of).collect(Collectors.toList());
        stackInfo.setCategories(categoryInfos);
        return stackInfo;
    }

}
