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
 * @date: 2023年11月01日 15:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEventSearchCritical extends PageSearchCritical
{
    private String eventName;
    private String serviceName;
    private String resourceName;
    private String resourceId;
    private String resourceInstName;
    private String type;
    private String bpId;
    private String userId;
    private String logLevel;
    private Date startTriggerTime;
    private Date endTriggerTime;
}
