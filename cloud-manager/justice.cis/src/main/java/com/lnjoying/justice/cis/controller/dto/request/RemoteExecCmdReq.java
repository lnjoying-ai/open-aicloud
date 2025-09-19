package com.lnjoying.justice.cis.controller.dto.request;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "remoteExecCmdReq")
public class RemoteExecCmdReq
{
    private List<String> cmds;

    private boolean tty;

    private boolean stdin;

    private boolean stdout;

    private String instId;

}
