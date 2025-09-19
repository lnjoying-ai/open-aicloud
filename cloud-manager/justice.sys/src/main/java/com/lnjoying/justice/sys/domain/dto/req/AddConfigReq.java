package com.lnjoying.justice.sys.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/6/5 23:15
 */

@Data
@ApiModel(value = "AddConfigReq")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddConfigReq
{
    private String id;

    @NotBlank(message = "data id can not be empty")
    @ApiModelProperty(value = "data id")
    @JsonProperty(value = "data_id")
    @Length(max = 255, min = 1, message = "Length must be greater than or equal to 1 or less than or equal to 255")
    private String dataId;

    @Length(max = 128, min = 1, message = "Length must be greater than or equal to 1 or less than or equal to 128")
    private String group;

    @NotBlank(message = "content can not be empty")
    private String content;

    private String configTags;

    private String type;

    private String appName;

    private String desc;

    private String namespace;
}
