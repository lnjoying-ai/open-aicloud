package com.lnjoying.justice.servicemanager.entity.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicePortSearchCritical extends PageSearchCritical
{
    private String servicePortId;
    private String name;
    private Integer status;
    private String targetType;
    private String deployment;
    private String purpose;
    private String bp;
    private String user;
}
