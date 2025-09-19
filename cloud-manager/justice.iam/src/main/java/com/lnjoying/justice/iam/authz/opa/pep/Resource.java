package com.lnjoying.justice.iam.authz.opa.pep;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 10:23
 */

@Data
public class Resource
{
    private String id;

    private String name;

    /**
     * type example ecrm:node
     */
    private String type;

    /**
     * bp id
     */
    private String bp;

    private String project;

    private Map<String, Object> attributes;

    private Map<String, Object> context;
}
