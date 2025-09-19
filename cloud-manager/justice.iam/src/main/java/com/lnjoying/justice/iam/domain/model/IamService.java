package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/16 16:35
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamService
{
    @ApiModelProperty(value="")
    private String id;

    @ApiModelProperty(value="")
    private String name;

    @ApiModelProperty(value="")
    private String displayName;

    @ApiModelProperty(value="")
    private String iamCode;

    @ApiModelProperty(value="")
    private String parentId;

    @ApiModelProperty(value="")
    private String lrnFormat;

    @ApiModelProperty(value="")
    private String lrnRegex;


    /**
     * enable(1:enable;-1:disable)
     */
    @ApiModelProperty(value="enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value="")
    private String description;

    /**
     * create time
     */
    @ApiModelProperty(value="create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value="update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public static IamService of (TblIamService tblIamService)
    {
        IamService iamService = new IamService();
        BeanUtils.copyProperties(tblIamService, iamService);
        return iamService;
    }
}
