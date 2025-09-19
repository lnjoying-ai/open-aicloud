package com.lnjoying.justice.iam.domain.dto.response.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamAction;
import com.lnjoying.justice.iam.db.model.TblIamCommonResource;
import com.lnjoying.justice.iam.db.model.TblIamConditionKey;
import com.lnjoying.justice.iam.db.model.TblIamResourceAttr;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/3 10:40
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TblIamCommonResourceSummary extends TblIamCommonResource
{

    private String projectName;

    private String projectDisplayName;

    private String serviceName;

    private String serviceDisplayName;

    private String serviceIamCode;

    private int actionTotal;

    private int  attrTotal;

    private int conditionTotal;
}
