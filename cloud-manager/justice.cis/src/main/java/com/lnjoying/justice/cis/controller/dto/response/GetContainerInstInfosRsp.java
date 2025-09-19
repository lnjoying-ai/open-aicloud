package com.lnjoying.justice.cis.controller.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetContainerInstInfosRsp
{
    int total_num;

    List<DockerContainerInfo> containers;
}
