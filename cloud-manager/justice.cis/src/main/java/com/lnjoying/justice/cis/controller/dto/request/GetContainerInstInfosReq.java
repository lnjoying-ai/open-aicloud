package com.lnjoying.justice.cis.controller.dto.request;

import lombok.Data;

@Data
public class GetContainerInstInfosReq
{
    private String userId;

    private Integer status;

    private String region;

    private String site;

    private String nodeId;

    private Integer pageSize;

    private Integer pageNum;
}
