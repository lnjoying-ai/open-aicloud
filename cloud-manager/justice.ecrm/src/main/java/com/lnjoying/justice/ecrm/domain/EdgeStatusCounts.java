package com.lnjoying.justice.ecrm.domain;

import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/16 11:08
 */

@Data
public class EdgeStatusCounts
{
    private String onlineStatus;

    private String onlineStatusCode;

    private String bpId;

    private String userId;

    private Integer total;
}
