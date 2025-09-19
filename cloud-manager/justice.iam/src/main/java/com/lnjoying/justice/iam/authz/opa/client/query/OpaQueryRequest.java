package com.lnjoying.justice.iam.authz.opa.client.query;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 15:16
 */

@Data
public class OpaQueryRequest
{
    private final Object input;

    private final String path;

    public  Map<String, Object> buildInput()
    {
        Map<String, Object> input = new HashMap<>();
        input.put("input", this.input);
        return input;
    }
}
