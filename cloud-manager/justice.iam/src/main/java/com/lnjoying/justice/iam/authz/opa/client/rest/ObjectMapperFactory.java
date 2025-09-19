package com.lnjoying.justice.iam.authz.opa.client.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/23 17:26
 */

public class ObjectMapperFactory
{
    private static final ObjectMapperFactory instance = new ObjectMapperFactory();

    private ObjectMapper objectMapper = createConfiguredObjectMapper();

    public ObjectMapper create() {
        return objectMapper;
    }

    private static ObjectMapper createConfiguredObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapperFactory getInstance() {
        return instance;
    }
}
