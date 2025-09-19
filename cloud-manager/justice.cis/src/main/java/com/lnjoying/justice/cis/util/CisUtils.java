package com.lnjoying.justice.cis.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.domain.model.InspectInfo;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 6/1/22 3:06 PM
 */
public class CisUtils
{
    private static Logger LOGGER = LogManager.getLogger();
    
    public static InstanceState getDockerInstState(String externInfo, int code)
    {
        try
        {
            JsonObject jsonObject = JsonParser.parseString(externInfo).getAsJsonObject();
            JsonObject stateObj = jsonObject.get("State").getAsJsonObject();
            String instStatus = stateObj.get("Status").getAsString();
            if (instStatus.equals("created"))
            {
                return InstanceState.CREATED;
            }
            else if (instStatus.equals("running"))
            {
                return InstanceState.RUNNING;
            }
    
            int exitCode = stateObj.get("ExitCode").getAsInt();
            if (exitCode == 0)
            {
                return InstanceState.SUCCESS_QUIT;
            }
            else
            {
                return InstanceState.ABNORMAL_QUIT;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("the extern info is not json, {}", externInfo);
            return InstanceState.fromCode(code);
        }
        
    }

    public static InspectInfo getDockerInstInspectInfo(String externInfo)
    {
        try
        {
            InspectInfo inspectInfo = JsonUtils.fromJson(externInfo, InspectInfo.class);
            return inspectInfo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("the extern info is not json, {}", externInfo);
            return null;
        }

    }
}
