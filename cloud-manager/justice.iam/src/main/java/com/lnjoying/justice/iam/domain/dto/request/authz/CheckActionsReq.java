package com.lnjoying.justice.iam.domain.dto.request.authz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/28 11:10
 */

@Data
@ApiModel(value = "CheckActionsReq")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckActionsReq
{
    @Size(min = 1, message = "actions cannot be empty")
    List<String> actions;
}
