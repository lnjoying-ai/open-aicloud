package com.lnjoying.justice.iam.domain.dto.response.authz;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.domain.model.IamPolicyAttachment;
import com.lnjoying.justice.iam.domain.model.IamRoleAttachment;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 19:25
 */

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "AttachmentsRsp")
public class RoleAttachmentsRsp
{
    private long totalNum;

    private List<IamRoleAttachment> attachments;
}
