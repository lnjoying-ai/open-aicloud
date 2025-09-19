package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import io.swagger.models.Swagger;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/28 19:44
 */

public interface SwaggerLoader
{
    Swagger loadSwagger(String swaggerContext);

    List<Swagger> getSwaggers();
}
