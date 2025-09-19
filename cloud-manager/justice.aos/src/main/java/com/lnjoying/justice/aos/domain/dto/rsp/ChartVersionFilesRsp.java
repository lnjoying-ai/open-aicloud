package com.lnjoying.justice.aos.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/17 9:50
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "chartVersionFilesRsp")
public class ChartVersionFilesRsp
{
    /**
     * key is the file name value is the file content
     */
    private Map<String , String> files;

    private String version;

    private String appId;

}
