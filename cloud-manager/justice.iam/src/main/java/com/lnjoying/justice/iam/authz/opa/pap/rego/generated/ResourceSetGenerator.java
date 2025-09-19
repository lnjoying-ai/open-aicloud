package com.lnjoying.justice.iam.authz.opa.pap.rego.generated;

import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.iam.authz.opa.topic.EventConverter;
import com.lnjoying.justice.iam.config.freemarker.IamFreemarkerConfig;
import com.lnjoying.justice.iam.utils.IamTemplateUtil;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/15 11:23
 */

public class ResourceSetGenerator implements RegoGenerator
{
    public static ResourceSetGenerator INSTANCE = new ResourceSetGenerator();

    public static final String FTL_DATA_RESOURCE = "resource";

    public static final String FTL_DATA_RESOURCE_NAME = "resource_name";

    public static final String OPA_POLICY_ID_PREFIX = "lnjoying/generated/conditionset/__autogen__";

    private ResourceSetGenerator(){}

    private static final String PATH = "resourceset.rego.ftl";

    @Override
    public String generate(Map<String, String> params)
    {
        return toGenerate(PATH, params);
    }

    public static String colonResourceName(String serviceName, String resourceName)
    {
        return serviceName + COLON + resourceName;
    }

    public static String doubleUnderscoreResourceName(String serviceName, String resourceName)
    {
        return serviceName + DOUBLE_UNDERSCORE + resourceName;
    }
}
