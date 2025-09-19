package com.lnjoying.justice.cis.domain.dto.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/26 10:08
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "UpdateContainersCfgReq")
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContainersCfgReq
{
    @NotBlank(message = "cfg can not be empty")
    private String cfg;

    @NotEmpty(message = "at least one container is required")
    private List<String> containerIds;

    private boolean restart = false;
}
