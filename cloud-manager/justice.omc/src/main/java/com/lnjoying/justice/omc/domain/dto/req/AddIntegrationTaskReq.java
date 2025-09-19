package com.lnjoying.justice.omc.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/25 15:16
 */

@Data
@ApiModel(value = "AddIntegrationTaskReq")
@JsonIgnoreProperties({ "bp_id", "user_id", "user_name", "bp_name", "create_time", "update_time"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddIntegrationTaskReq extends BaseReq
{

    private String dataSourceId;

    private int integrationType;

    private int targetType;

    //@Size(min = 1, message = "targets cannot be empty")
    private List<String> targets = new ArrayList<>();

    private String stackTemplateVersionId;

    private List<Map<String, String>> stackParams;

    private List<Map<String, String>> customParams;

    private String name;

    private String description;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public final static class LightweightNodeParam
    {
        private NodeExporterParam nodeExporter;

        private CAdvisorParam cAdvisor;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public final static class NodeExporterParam
    {
        private String listenAddress;

        private String telemetryPath;

        private String scrapeInterval;

        private String scrapeTimeout;
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
