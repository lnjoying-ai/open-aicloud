package com.lnjoying.justice.cis.controller.dto.request;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "logContainerInstReq")
public class LogContainerInstReq
{
    private boolean flow;

    private int lines;

    private String head_or_tail;

    private String instId;
}
