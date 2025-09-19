package com.lnjoying.justice.sys.facade.impl;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.client.config.NacosConfigService;
import com.alibaba.nacos.client.config.http.ServerHttpAgent;
import com.alibaba.nacos.common.http.HttpRestResult;
import com.alibaba.nacos.spring.context.event.config.EventPublishingConfigService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.service.sys.SysService;
import com.lnjoying.justice.sys.domain.dto.req.AddConfigReq;
import com.lnjoying.justice.sys.facade.NacosService;
import com.lnjoying.justice.sys.service.CombRpcService;
import com.lnjoying.justice.sys.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.alibaba.nacos.api.common.Constants.CONFIG_CONTROLLER_PATH;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.sys.constant.NacosConstants.NACOS_NAMESPACE_URL;
import static org.springframework.http.HttpStatus.OK;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/21 17:20
 */

@Service
@Slf4j
public class NacosServiceImpl implements NacosService
{
    @NacosInjected
    private ConfigService configService;

    @Autowired
    @Qualifier("globalNacosProperties$config")
    private Properties configGlobalNacosPropertiesBeanName;

    private ServerHttpAgent agent;

    private long timeout = 30000L;

    @Autowired
    private CombRpcService combRpcService;

    @Override
    public Boolean createNamespace(String namespaceId)
    {
        try
        {
            HttpRestResult<String> result = agent.httpPost(NACOS_NAMESPACE_URL, null, buildCreateNamespaceRequestParams(namespaceId), agent.getEncode(), timeout);
            return parseResult(result, Boolean.class);
        }
        catch (Exception e)
        {
            log.error("create namespace error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }


    @Override
    public Boolean checkNamespaceIdExist(String namespaceId)
    {
        try
        {
            HttpRestResult<String> result = agent.httpGet(NACOS_NAMESPACE_URL, null, buildCheckNamespaceIdExistParams(namespaceId), agent.getEncode(), timeout);
            return parseResult(result, Boolean.class);
        }
        catch (Exception e)
        {
            log.error("check namespace id exist error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @Override
    public Boolean publishConfig(AddConfigReq addConfig)
    {
        try
        {
            HttpRestResult<String> result = agent.httpPost(CONFIG_CONTROLLER_PATH, null, buildPublishConfigParams(addConfig), agent.getEncode(), timeout);
            return parseResult(result, Boolean.class);
        }
        catch (Exception e)
        {
            log.error("check namespace id exist error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @Override
    public Object getConfig(String namespace, String group, String dataId)
    {
        String userId = getUserId();
        if (isAdmin())
        {
            userId = StringUtils.isNotBlank(namespace) ? namespace : "";
        }

        try
        {
            HttpRestResult<String> result = agent.httpGet(CONFIG_CONTROLLER_PATH, null, buildGetConfigParams(userId, group, dataId), agent.getEncode(), timeout);
            JsonNode jsonNode = parseResult(result, JsonNode.class);
            return assembleUserInfo(jsonNode);
        }
        catch (Exception e)
        {
            log.error("check namespace id exist error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @Override
    public Object getConfigList(String namespace, String group, String dataId, boolean system, Integer pageNum, Integer pageSize)
    {
        try
        {
            if (system)
            {
                if (isAdmin())
                {
                    HttpRestResult<String> result = agent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, buildGetSystemConfigListParams(), agent.getEncode(), timeout);
                    return parseResult(result, JsonNode.class);
                }
                else
                {
                    return null;
                }

            }
            else
            {
                if(!isAdmin())
                {
                    namespace = getUserId();
                }
                HttpRestResult<String> result = agent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null,
                        buildGetTenantConfigListParams(namespace, group, dataId, pageNum, pageSize), agent.getEncode(), timeout);
                JsonNode jsonNode = parseResult(result, JsonNode.class);
                return assembleUserInfo(jsonNode);
            }

        }
        catch (Exception e)
        {
            log.error("get config list error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }


    @Override
    public void deleteConfig(String namespace, String dataId, String group)
    {
        String userId = getUserId();
        if (isAdmin())
        {
            userId = StringUtils.isNotBlank(namespace) ? namespace : "";
        }
        try
        {

            HttpRestResult<String> result = agent.httpDelete(Constants.CONFIG_CONTROLLER_PATH, null, buildDeleteConfigParams(userId, group, dataId), agent.getEncode(), timeout);
            Boolean delete = parseResult(result, Boolean.class);
            if (delete)
            {
                log.info("delete config success, namespace: {}, group:{}, dataId:{}", userId, group, dataId);
            }
            else
            {
                log.error("delete config fail, namespace: {}, group:{}, dataId:{}", userId, group, dataId);
                throw new WebSystemException(DELETE_CONFIG_ERROR, ErrorLevel.ERROR);
            }
        }
        catch (Exception e)
        {
            log.error("delete config list error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @Override
    public Boolean exists(String namespace, String group, String dataId)
    {
        try
        {
            HttpRestResult<String> result = agent.httpGet(CONFIG_CONTROLLER_PATH, null, buildGetConfigParams(namespace, group, dataId), agent.getEncode(), timeout);
            if (OK.value() == (result.getCode()) && StringUtils.isNotBlank(result.getData()))
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            log.error("check namespace id exist error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @Override
    public SysService.ConfigInfoBase getConfigInfoBase(String namespace, String group, String dataId)
    {
        try
        {
            HttpRestResult<String> result = agent.httpGet(CONFIG_CONTROLLER_PATH, null, buildGetConfigParams(namespace, group, dataId), agent.getEncode(), timeout);
            JsonNode jsonNode = parseResult(result, JsonNode.class);
            SysService.ConfigInfoBase configInfoBase = new SysService.ConfigInfoBase();
            configInfoBase.setUserId(jsonNode.get("tenant").asText());
            configInfoBase.setGroup(jsonNode.get("group").asText());
            configInfoBase.setDataId(jsonNode.get("dataId").asText());
            configInfoBase.setContent(jsonNode.get("content").asText());
            configInfoBase.setMd5(jsonNode.get("md5").asText());
            return configInfoBase;
        }
        catch (Exception e)
        {
            log.error("get config info base error: {}", e);
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    @PostConstruct
    public void getServerHttpAgent()
    {
        try {
            Field configServiceField = ReflectionUtils.findField(EventPublishingConfigService.class, "configService");
            configServiceField.setAccessible(true);
            NacosConfigService innerConfigService = (NacosConfigService)ReflectionUtils.getField(configServiceField, configService);
            Field agentField = ReflectionUtils.findField(NacosConfigService.class, "agent");
            agentField.setAccessible(true);
            this.agent = (ServerHttpAgent)ReflectionUtils.getField(agentField, innerConfigService);
        }
        catch(Exception e)
        {
            log.error("get server http agent failed: {}", e);
            throw new WebSystemException(GET_CONFIG_ERROR, ErrorLevel.ERROR);
        }
    }

    @PostConstruct
    public void getNacosTimeout()
    {
        String timeoutStr = (String) configGlobalNacosPropertiesBeanName.get(NacosProperties.CONFIG_LONG_POLL_TIMEOUT);
        if (StringUtils.isNotBlank(timeoutStr))
        {
            this.timeout = Long.parseLong(timeoutStr);

        }
    }

    public <T>T parseResult(HttpRestResult<String> result, Class<T> clazz)
    {
        if (OK.value() == result.getCode())
        {
            String data = result.getData();
            if (StringUtils.isNotBlank(data))
            {
                try
                {
                    return (T) JacksonUtils.strToObj(data, clazz);
                }
                catch (IOException e)
                {
                    log.error("result  parse error:{}", e);
                }
            }
            return (T)new Object();
        }
        else
        {
            log.error("get config fail, code:{}, message:{}", result.getCode(), result.getMessage());
            throw new WebSystemException(NACOS_OPERATION_ERROR, ErrorLevel.ERROR);
        }
    }

    private Map<String, String> buildCreateNamespaceRequestParams(String namespaceId)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("customNamespaceId", namespaceId);
        params.put("namespaceName", namespaceId);
        params.put("namespaceDesc", namespaceId);
        return params;
    }

    private Map<String, String> buildCheckNamespaceIdExistParams(String namespaceId)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("checkNamespaceIdExist", "true");
        params.put("customNamespaceId", namespaceId);
        return params;
    }

    private Map<String, String> buildPublishConfigParams(AddConfigReq addConfig)
    {
        // String dataId, String group, String content, String configTags, String type, String appName, String desc, String tenant)
        Map<String, String> params = new HashMap<String, String>();
        params.put("dataId", addConfig.getDataId());
        params.put("group", addConfig.getGroup());
        params.put("content", addConfig.getContent());
        params.put("configTags", addConfig.getConfigTags());
        params.put("type", addConfig.getType());
        params.put("appName", addConfig.getAppName());
        params.put("desc", addConfig.getDesc());
        params.put("tenant", addConfig.getNamespace());
        return params;
    }


    private Map<String, String> buildGetConfigParams(String userId, String group, String dataId)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("dataId", dataId);
        params.put("group", group);
        params.put("tenant", userId);
        params.put("show", "all");
        return params;
    }

    private Map<String, String> buildGetSystemConfigListParams()
    {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", "1");
        params.put("pageSize", "100");
        params.put("search", "accurate");
        params.put("dataId", "");
        params.put("group", "");
        params.put("tenant", "");
        return params;
    }

    private Map<String, String> buildGetTenantConfigListParams(String namespace, String group, String dataId, Integer pageNum, Integer pageSize)
    {

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageNo", String.valueOf(pageNum));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("search", "blur");
        params.put("dataId", StringUtils.isEmpty(dataId) ? "" :dataId);
        params.put("group", StringUtils.isEmpty(group) ? "" :group);
        params.put("tenant", StringUtils.isEmpty(namespace) ? "___%" :namespace);
        return params;
    }

    private Map<String, String> buildDeleteConfigParams(String userId, String group, String dataId)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("dataId", dataId);
        params.put("group", group);
        params.put("tenant", userId);
        return params;
    }

    private Object assembleUserInfo(JsonNode jsonNode)
    {
        if (Objects.nonNull(jsonNode))
        {
            JsonNode pageItems = jsonNode.get("pageItems");
            if (pageItems.isArray())
            {
                ArrayNode array = (ArrayNode) pageItems;

                array.forEach(value -> {
                    String tenant = value.get("tenant").asText();
                    ((ObjectNode)value).put("tenant_name", getUserName(tenant));
                });
            }
        }

        return jsonNode;
    }

    private String getUserName(String userId)
    {
        String userName = "";
        if (StringUtils.isNotBlank(userId))
        {
            try
            {
                userName = combRpcService.getUmsService().getUserNameByUserId(userId);
            }
            catch (Exception e)
            {
                log.error("get user name failed");
            }
        }
        return userName;
    }
}

