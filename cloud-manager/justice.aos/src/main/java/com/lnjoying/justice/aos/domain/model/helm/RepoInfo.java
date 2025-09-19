package com.lnjoying.justice.aos.domain.model.helm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 16:20
 */

@ApiModel(value = "repo_info")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RepoInfo extends TblRepoInfo
{

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

    /**
     * auth info
     */
    @JsonIgnore
    @ApiModelProperty(value = "auth info")
    private Object authInfo;


    public static RepoInfo of(TblRepoInfo info)
    {
        RepoInfo repoInfo = new RepoInfo();
        BeanUtils.copyProperties(info, repoInfo);
        return repoInfo;
    }
}
