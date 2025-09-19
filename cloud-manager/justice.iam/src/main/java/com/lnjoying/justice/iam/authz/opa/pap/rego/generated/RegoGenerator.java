package com.lnjoying.justice.iam.authz.opa.pap.rego.generated;

import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.iam.utils.IamTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/15 11:24
 */


public interface RegoGenerator
{
    Logger LOGGER = LogManager.getLogger();

    String UNDERSCORE = "_";

    String DOUBLE_UNDERSCORE = "__";

    String COLON = ":";

    String generate(Map<String, String> params);

    default String toGenerate(String path, Map<String, String> params)
    {
        try
        {
            String regoContent = FileUtils.getRegoContentFromNacosConfigPathResource(path);
            String format = IamTemplateUtil.format(path, regoContent, params);
            return format;
        }
        catch (Exception e)
        {
            LOGGER.error("get rego template content and parse from path:{} failed:{}", path,  e);
        }

        return "";
    }
}
