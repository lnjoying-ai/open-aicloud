package com.lnjoying.justice.cis.controller.dto.response;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CreateContainerInstRsp
{
    private List<String> ids = new LinkedList<>();
}
