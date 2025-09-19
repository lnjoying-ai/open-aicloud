package com.lnjoying.justice.iam.authz.opa.pep;

import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/24 10:20
 */

@Data
public class User
{
    private String key;

    private String bp;

    private String kind;

    private Map<String, String> attributes;
}
