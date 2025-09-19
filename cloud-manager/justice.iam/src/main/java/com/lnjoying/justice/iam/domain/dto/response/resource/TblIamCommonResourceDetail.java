package com.lnjoying.justice.iam.domain.dto.response.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamAction;
import com.lnjoying.justice.iam.db.model.TblIamCommonResource;
import com.lnjoying.justice.iam.db.model.TblIamConditionKey;
import com.lnjoying.justice.iam.db.model.TblIamResourceAttr;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/3 10:40
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TblIamCommonResourceDetail extends TblIamCommonResourceSummary
{

    List<TblIamAction> actions;

    List<TblIamResourceAttr> attrs;

    List<TblIamConditionKey> conditions;
}
