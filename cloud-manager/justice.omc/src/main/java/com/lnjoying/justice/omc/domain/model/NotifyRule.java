package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.domain.dto.req.AddAlarmReq;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/2 17:08
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotifyRule
{
    private boolean repeatNotify;

    private Date notifyStartTime;

    private Date notifyEndTime;

    private List<NotifyObject> notifyObjects;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public final static class NotifyObject
    {
        private String receiverId;

        private String notifyChannels;

    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public final static class CAdvisorParam
    {
        private String port;

        private String scrapeInterval;

        private String scrapeTimeout;
    }
}
