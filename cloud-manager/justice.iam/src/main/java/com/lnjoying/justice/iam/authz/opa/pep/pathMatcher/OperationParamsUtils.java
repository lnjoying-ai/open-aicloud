package com.lnjoying.justice.iam.authz.opa.pep.pathMatcher;

import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/13 20:29
 */

public class OperationParamsUtils
{

    public static String[] buildOperationParams(Operation operation)
    {
        List<Parameter> parameters = operation.getParameters();
        if (!CollectionUtils.isEmpty(parameters))
        {
            String[] params = (String[]) parameters.stream().map(parameter ->
            {
                return parameter.getName();
            }).toArray(String[]::new);

            return params;
        }
        return new String[0];
    }

    public enum ParamType
    {
        QUERY("query"),

        PATH("path"),

        BODY("body"),

        COOKIE("cookie"),

        FORM_DATA("formData");

        final String name;

        ParamType(String name)
        {
            this.name = name;
        }

        public static ParamType fromName(String name)
        {
            return Arrays.stream(ParamType.values()).filter(x -> x.name().equalsIgnoreCase(name)).findFirst().get();
        }

        public String getName()
        {
            return name;
        }
    }
}
