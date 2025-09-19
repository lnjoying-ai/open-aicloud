package com.lnjoying.justice.schema.entity.cluster;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 3/1/22 3:16 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClusterAuthParams
{
    private String clusterId;
    private String token;
    private String userId;
    private String bpId;
    private String roles;
    private String userKind;
    private String resources;
}
