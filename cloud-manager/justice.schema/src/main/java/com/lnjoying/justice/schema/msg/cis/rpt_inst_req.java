package com.lnjoying.justice.schema.msg.cis;

import lombok.Data;

@Data
public class rpt_inst_req
{
    private String instId;

    private Integer instStatus;

    private String refId;

    private String resultInfo;

    private String externInfo;
}
