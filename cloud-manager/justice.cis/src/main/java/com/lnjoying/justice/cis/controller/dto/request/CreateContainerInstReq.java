package com.lnjoying.justice.cis.controller.dto.request;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lnjoying.justice.schema.entity.dev.DevNeedInfo;
import com.lnjoying.justice.schema.entity.dev.FailoverPolicy;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "createContainerInstReq")
public class CreateContainerInstReq
{
    @NotBlank(message = "name can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$",
            message = "name: 'a'-'z', 'A'-'Z', '0'-'9', '-'")
    private String name;

    private String description;

    private DevNeedInfo dev_need_info;

    @Range(min = 0, max = 3, message = "from 0 to 3")
    private Integer on_one_node;

    private List<TargetNode> target_nodes;

    private Integer replica_num;

    private ObjectNode inst_params;

    private boolean auto_run;

    private String bp_id;

    private String user_id;

    private String registry_id;

    private SchedulingStrategy scheduling_strategy;

    private Map<String, String> extra_params = new HashMap<>();

    @Pattern(regexp = "no|always|unless-stopped|on-failure(:\\d+)*",
            message = "restart policy error")
    private String restart_policy;

    private FailoverPolicy failover_policy;

    /**
     * check basic data by trim.
     */
    public void stringTrim() {
        this.name = StringUtils.trimWhitespace(this.name);
        this.description =  StringUtils.trimWhitespace(this.description);
        this.bp_id =  StringUtils.trimWhitespace(this.bp_id);
        this.user_id =  StringUtils.trimWhitespace(this.user_id);
    }
}
