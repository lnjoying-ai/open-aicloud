package com.lnjoying.justice.sys.domain.dto.resp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/21 20:26
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfigResp
{
    private String dataId;

    private String group;

    private String content;

    private String appName;

    private String type;

    private String configTags;

    private String desc;

    //private String namespace;
}
