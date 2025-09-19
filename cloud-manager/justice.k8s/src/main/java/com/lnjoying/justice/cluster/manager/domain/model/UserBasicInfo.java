package com.lnjoying.justice.cluster.manager.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 12/28/21 1:49 PM
 * @Description:
 */
@Data
@Builder
public class UserBasicInfo
{
    private String userId;
    private String bpId;
    private String userKind;
}
