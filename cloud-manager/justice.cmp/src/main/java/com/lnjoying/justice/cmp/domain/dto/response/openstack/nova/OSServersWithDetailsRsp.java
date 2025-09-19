package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import lombok.Data;

import java.util.List;

@Data
public class OSServersWithDetailsRsp
{
    private List<OSServerInfo> servers;
}
