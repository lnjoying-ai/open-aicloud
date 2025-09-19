package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import lombok.Data;

import java.util.Date;

@Data
public class OSFault
{
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private String details;
    private Date created;
}
