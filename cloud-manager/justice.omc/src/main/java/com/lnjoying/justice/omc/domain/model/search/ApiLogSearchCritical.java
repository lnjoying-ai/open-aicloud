package com.lnjoying.justice.omc.domain.model.search;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 功能描述
 *
 * @author: Robin
 * @date: 2023年11月01日 15:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiLogSearchCritical extends PageSearchCritical
{
    private String serviceName;
    private String resourceName;
    private String resourceId;
    private String bpId;
    private String userId;
    private String logLevel;
    private String httpMethod;
    private Date startTriggerTime;
    private Date endTriggerTime;
}
