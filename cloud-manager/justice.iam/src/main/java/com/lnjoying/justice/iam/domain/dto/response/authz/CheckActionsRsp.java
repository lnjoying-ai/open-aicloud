package com.lnjoying.justice.iam.domain.dto.response.authz;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/28 11:12
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "CheckActionsRsp")
public class CheckActionsRsp
{
    List<ActionDecision> actions;

    @Data
    public final static class ActionDecision
    {
        private String decision;

        private String action;
    }
}
