package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/14 16:31
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfigVariable
{
    private String variable;

    private String description;

    private String label;

    private String type;

    private boolean required;

    private String defaultValue;

    private boolean immutable;
}
