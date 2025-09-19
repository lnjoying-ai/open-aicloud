package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.iam.authz.opa.pap.rego.util.FormatterUtils;
import com.lnjoying.justice.iam.authz.opa.pep.pathMatcher.SwaggerRequestMapping;
import com.lnjoying.justice.iam.db.model.TblIamAction;
import com.lnjoying.justice.iam.db.model.TblIamCommonResource;
import com.lnjoying.justice.iam.db.model.TblIamResourceAttr;
import com.lnjoying.justice.iam.db.model.TblIamService;
import com.lnjoying.justice.iam.db.repo.ResourceRepository;
import com.lnjoying.justice.iam.db.repo.ServiceRepository;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.domain.model.ServiceResourceEnCnNameCache;
import com.lnjoying.justice.iam.swagger.SwaggerOperation;
import com.lnjoying.justice.schema.service.iam.CommonResourceService;
import com.micro.core.common.Utils;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import lombok.extern.slf4j.Slf4j;
import org.apache.servicecomb.provider.pojo.Invoker;
import org.apache.servicecomb.registry.RegistrationManager;
import org.apache.servicecomb.swagger.SwaggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.iam.common.constant.IamConstants.IAM_SERVICE_CODE;
import static com.lnjoying.justice.iam.db.model.TblIamAction.ActionType.ActionEnableStatus.ENABLE;
import static com.lnjoying.justice.iam.db.model.TblIamAction.ActionType.getActionType;
import static com.lnjoying.justice.schema.service.iam.CommonResourceService.DEFAULT_SCHEMA_ID;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 10:11
 */

@Slf4j
@Service
public class ResourceService
{
    @Autowired
    private ResourceRepository resourceRepository;


    @Autowired
    private SwaggerRequestMapping swaggerRequestMapping;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ActionsCache actionsCache;

    @Autowired
    private ServiceResourceEnCnNameCache serviceResourceEnCnNameCache;

    private AtomicBoolean updateCommonResource = new AtomicBoolean(false);
    
    public void registerRequestMapping()
    {
        try
        {
            List<TblIamAction> tblIamActions = resourceRepository.selectAllActions();
            if (!CollectionUtils.isEmpty(tblIamActions))
            {
                // get all api documents
                List<String> allApis = tblIamActions.stream().map(tblIamAction ->
                {
                    String associatedApis = tblIamAction.getAssociatedApis();
                    return StringUtils.hasText(associatedApis) ? associatedApis : null;
                }).filter(Objects::nonNull).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(allApis))
                {
                    log.info("total number of api documents is {}", allApis.size());
                    allApis.stream().forEach(api -> {
                        swaggerRequestMapping.getSwaggerLoader().loadSwagger(api);
                    });

                    swaggerRequestMapping.detectMappingInfo();
                }
            }
        }
        catch (Exception e)
        {
            log.error("register request mapping failed, error:{}", e);
        }


    }

    public void registerCommonResourceInfos(boolean overwrite)
    {
        try
        {
            Map<String, String> schemaMap = getSchemaMap();
            registerCommonResourceInfos(schemaMap, overwrite);
        }
        catch (Exception e)
        {
            log.error("register basic resources error:{}", e);
        }
    }

    public void registerCommonResourceInfos(String iamCode, Map<String, String> schemaMap)
    {
        Map<String, String> schemaMapResult = new HashMap<>();
        // fix schemaMapName
        schemaMap.forEach((schemaName, schemaContent) -> {
            if (StringUtils.startsWithIgnoreCase(schemaName, iamCode + ":"))
            {
                schemaMapResult.put(schemaName, schemaContent);
            }
            else
            {
                schemaMapResult.put(iamCode + ":" + schemaName, schemaContent);
            }

        });

        registerCommonResourceInfos(schemaMapResult, true);
    }

    public void registerCommonResourceInfos(Map<String, String> schemaMap, boolean overwrite)
    {
        try
        {
            if (!CollectionUtils.isEmpty(schemaMap))
            {
                schemaMap.entrySet().stream().forEach(schema ->
                {
                    String value = schema.getValue();
                    String iamCode = schema.getKey().split(":")[0];
                    Swagger swagger = SwaggerUtils.parseSwagger(value);
                    doRegisterCommonResources(iamCode, swagger, overwrite);
                    doRegisterActions(iamCode, swagger, value, overwrite);
                    doRegisterCommonResourceAttr(iamCode, swagger, overwrite);
                });

            }
        }
        catch (Exception e)
        {
            log.error("register common resource failed, error:{}", e);
        }

    }

    private void doRegisterCommonResourceAttr(String iamCode, Swagger swagger, boolean overwrite)
    {
        Map<String, Object> vendorExtensions = swagger.getVendorExtensions();

        try
        {
            if (!CollectionUtils.isEmpty(vendorExtensions))
            {
                LinkedHashMap resourceMap = (LinkedHashMap) vendorExtensions.get(SWAGGER_X_RESOURCE);
                if (!CollectionUtils.isEmpty(resourceMap))
                {
                    String resourceModel = (String) resourceMap.get(SWAGGER_RESOURCE_MODEL);
                    String resourceName = FormatterUtils.lowerHyphen2LowerUnderscore((String)resourceMap.get(SWAGGER_RESOURCE_SINGULAR_NAME));
                    String serviceId = serviceRepository.selectServiceIdByIamCode(iamCode);
                    TblIamCommonResource tblIamCommonResource = resourceRepository.selectByServiceIdAndName(serviceId, resourceName);
                    if (StringUtils.hasText(resourceModel) && Objects.nonNull(tblIamCommonResource))
                    {
                        insertOrUpdateResourceAttrs(overwrite, swagger, resourceModel, tblIamCommonResource);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("register common resource attr failed, error:{}", e);
        }
    }


    public void loadServiceActions()
    {
        try
        {
            List<ActionsCache.ServiceAction> serviceActions = resourceRepository.selectAllServiceActions();
            if (!CollectionUtils.isEmpty(serviceActions))
            {
                Map<String, ActionsCache.ServiceAction> serviceActionMap = serviceActions.stream().collect(Collectors.toMap(serviceAction -> serviceAction.getActionName(), serviceAction -> serviceAction));
                actionsCache.create(serviceActionMap);
                serviceResourceEnCnNameCache.create(serviceActionMap);
            }
        }
        catch (Exception e)
        {
            log.error("load service action failed, error:{}", e);
        }
    }


    private void insertOrUpdateResourceAttrs(boolean overwrite, Swagger swagger, String resourceModel, TblIamCommonResource tblIamCommonResource)
    {
        String resourceId = tblIamCommonResource.getId();
        Map<String, Model> definitions = swagger.getDefinitions();
        if (!CollectionUtils.isEmpty(definitions))
        {
            List<TblIamResourceAttr> resourceAttrs = new ArrayList<>();
            String[] modelArray = org.apache.commons.lang.StringUtils.split(resourceModel, ",");
            Arrays.stream(modelArray).forEach(m -> {
                Model model = definitions.get(m);
                if (Objects.nonNull(model))
                {
                    //models.add(model);
                    Map<String, Property> properties = model.getProperties();
                    if (!CollectionUtils.isEmpty(properties))
                    {
                        List<TblIamResourceAttr> resourceAttrList = getResourceAttrs(properties, resourceId, m);
                        if (!CollectionUtils.isEmpty(resourceAttrList))
                        {
                            resourceAttrs.addAll(resourceAttrList);
                        }
                    }
                }
            });

            if (!CollectionUtils.isEmpty(resourceAttrs))
            {
                // No need to consider whether to overwrite, delete directly, and then rebuild in batches
                // delete All
                resourceRepository.deleteByResourceId(resourceId);
                // batch Insert
                resourceRepository.batchInsertResourceAttrs(resourceAttrs);

            }

        }
    }

    private List<TblIamResourceAttr> getResourceAttrs(Map<String, Property> properties, String resourceId, String model)
    {
        List<TblIamResourceAttr> tblIamResourceAttrs = properties.entrySet().stream().map(entry ->
        {
            String key = entry.getKey();
            Property property = entry.getValue();
            TblIamResourceAttr tblIamResourceAttr = new TblIamResourceAttr();
            tblIamResourceAttr.setId(Utils.assignUUId());
            tblIamResourceAttr.setResourceId(resourceId);
            tblIamResourceAttr.setModel(model);
            tblIamResourceAttr.setAttrName(key);
            tblIamResourceAttr.setAttrType(property.getType());
            tblIamResourceAttr.setDescription(property.getDescription());
            tblIamResourceAttr.setCreateTime(new Date());
            tblIamResourceAttr.setUpdateTime(new Date());
            return tblIamResourceAttr;
        }).collect(Collectors.toList());
        return tblIamResourceAttrs;
    }

    private void createService(String resourceName, String resourceDescription, String resourceLrn, String serviceId)
    {
        TblIamCommonResource iamCommonResource = new TblIamCommonResource();
        iamCommonResource.setId(Utils.assignUUId());
        iamCommonResource.setServiceId(serviceId);
        iamCommonResource.setName(resourceName);
        iamCommonResource.setDescription(resourceDescription);
        iamCommonResource.setLrn(resourceLrn);
        iamCommonResource.setCreateTime(new Date());
        iamCommonResource.setUpdateTime(iamCommonResource.getCreateTime());
        resourceRepository.insertSelective(iamCommonResource);
    }

    private void updateService(String resourceDescription, String resourceLrn, TblIamCommonResource tblIamCommonResource)
    {
        tblIamCommonResource.setDescription(resourceDescription);
        tblIamCommonResource.setLrn(resourceLrn);
        tblIamCommonResource.setUpdateTime(new Date());
        resourceRepository.updateByPrimaryKeySelective(tblIamCommonResource);
    }

    private void updateAction(String apis, TblIamAction tblIamAction, String operationId, String resourceId)
    {
        tblIamAction.setResourceId(resourceId);
        tblIamAction.setActionType(getActionType(operationId));
        tblIamAction.setAssociatedApis(apis);
        tblIamAction.setUpdateTime(new Date());
        resourceRepository.updateAction(tblIamAction);
    }

    private void createAction(String apis, String operationId, String resourceId)
    {
        TblIamAction newIamAction = new TblIamAction();
        newIamAction.setId(Utils.assignUUId());
        newIamAction.setResourceId(resourceId);
        newIamAction.setActionName(operationId);
        newIamAction.setActionType(getActionType(operationId));
        newIamAction.setEnable(ENABLE.value());
        newIamAction.setDescription("");
        newIamAction.setAssociatedApis(apis);
        newIamAction.setCreateTime(new Date());
        newIamAction.setUpdateTime(new Date());
        resourceRepository.insertActionsSelective(newIamAction);
    }

    private  Map<String, String> getSchemaMap()
    {
        Map<String, String> schemaMap = new HashMap<>();
        List<TblIamService> tblIamServices = serviceRepository.selectAll("");
        if (!CollectionUtils.isEmpty(tblIamServices))
        {
            tblIamServices.removeIf(x -> x.getIamCode().equalsIgnoreCase(IAM_SERVICE_CODE));
            tblIamServices.parallelStream().forEach(tblIamService -> {
                String iamCode = tblIamService.getIamCode();
                try
                {
                    CommonResourceService proxy = Invoker.createProxy(iamCode, DEFAULT_SCHEMA_ID, CommonResourceService.class);
                    Map<String, String> map = proxy.getSchemaMap();
                    if (!CollectionUtils.isEmpty(map))
                    {
                        map.forEach((schemaName, schemaContent) -> {
                            schemaMap.put(iamCode + ":" + schemaName, schemaContent);
                        });

                    }
                }
                catch (Exception e)
                {
                    log.error("get service:{} schema map error:{}", iamCode, e);
                }
            });

            Map<String, String> iamSchemaMap = RegistrationManager.INSTANCE.getMicroservice().getSchemaMap();
            if (!CollectionUtils.isEmpty(iamSchemaMap))
            {
                iamSchemaMap.forEach((schemaName, schemaContent) -> {
                    schemaMap.put(IAM_SERVICE_CODE + ":" + schemaName, schemaContent);
                });
            }
        }

        return schemaMap;
    }

    private void doRegisterActions(String iamCode, Swagger swagger, String swaggerDoc, boolean overwrite)
    {
        SwaggerOperation swaggerOperation = new SwaggerOperation();
        Map<String, Object> vendorExtensions = swagger.getVendorExtensions();

        try
        {
            // get resource id
            TblIamCommonResource tblIamCommonResource = null;
            if (!CollectionUtils.isEmpty(vendorExtensions))
            {
                LinkedHashMap resourceMap = (LinkedHashMap)vendorExtensions.get(SWAGGER_X_RESOURCE);
                if (!CollectionUtils.isEmpty(resourceMap))
                {

                    String resourceName = FormatterUtils.lowerHyphen2LowerUnderscore((String)resourceMap.get(SWAGGER_RESOURCE_SINGULAR_NAME));
                    String serviceId = serviceRepository.selectServiceIdByIamCode(iamCode);
                    tblIamCommonResource = resourceRepository.selectByServiceIdAndName(serviceId, resourceName);
                }
            }

            if (Objects.nonNull(tblIamCommonResource))
            {
                String resourceId = tblIamCommonResource.getId();
                String resourceName = tblIamCommonResource.getName();
                Map<String, String> operationApis = swaggerOperation.getOperationApis(iamCode, swaggerDoc);
                if (!CollectionUtils.isEmpty(operationApis))
                {
                    int total = operationApis.size();
                    AtomicInteger failCount = new AtomicInteger(0);
                    AtomicInteger successCount = new AtomicInteger(0);
                    operationApis.entrySet().parallelStream().forEach(entry ->{
                        try
                        {
                            String operationId = entry.getKey();
                            // Need to deal with the problem of duplicate names of multiple resource actions in a service
                            TblIamAction tblIamActionInService = resourceRepository.selectActionByName(operationId);
                            if (Objects.nonNull(tblIamActionInService))
                            {
                                if (tblIamActionInService.getResourceId().equals(resourceId))
                                {
                                    successCount.incrementAndGet();
                                    if (!overwrite)
                                    {
                                        return;
                                    }

                                    updateAction(entry.getValue(), tblIamActionInService, operationId, resourceId);
                                }

                                else
                                {
                                    // handle actions with the same name
                                    TblIamAction tblIamActionInResource = resourceRepository.selectByResourceIdAndActionName(resourceId, operationId);
                                    String rewriteOperationId = String.format("%s(%s)", operationId, resourceName);
                                    if (Objects.isNull(tblIamActionInResource))
                                    {
                                        tblIamActionInResource = resourceRepository.selectByResourceIdAndActionName(resourceId, rewriteOperationId);
                                    }

                                    if (Objects.nonNull(tblIamActionInResource))
                                    {
                                        successCount.incrementAndGet();
                                        if (!overwrite)
                                        {
                                            return;
                                        }

                                        updateAction(entry.getValue(), tblIamActionInResource, rewriteOperationId, resourceId);
                                    }
                                    else
                                    {
                                        createAction(entry.getValue(), rewriteOperationId, resourceId);
                                    }
                                }

                            }
                            else
                            {
                                createAction(entry.getValue(), operationId, resourceId);
                            }
                        }
                        catch(Exception e)
                        {
                            failCount.incrementAndGet();
                        }
                    });
                    log.info("written to db, iam code: {}, total:{}, success:{}, fail:{}", iamCode, total, successCount.get(), failCount.get());
                }
            }
        }
        catch (Exception e)
        {
            log.error("register action failed, error:{}", e);
        }
    }

    private void doRegisterCommonResources(String iamCode, Swagger swagger, boolean overwrite)
    {
        try
        {
            Map<String, Object> vendorExtensions = swagger.getVendorExtensions();
            if (!CollectionUtils.isEmpty(vendorExtensions))
            {
                LinkedHashMap resourceMap = (LinkedHashMap)vendorExtensions.get(SWAGGER_X_RESOURCE);
                if (!CollectionUtils.isEmpty(resourceMap))
                {
                    String resourceName = FormatterUtils.lowerHyphen2LowerUnderscore((String)resourceMap.get(SWAGGER_RESOURCE_SINGULAR_NAME));
                    String resourceDescription = (String)resourceMap.get(SWAGGER_RESOURCE_DESCRIPTION);
                    String resourceLrn = (String)resourceMap.get(SWAGGER_RESOURCE_LRN);

                    String serviceId = serviceRepository.selectServiceIdByIamCode(iamCode);
                    TblIamCommonResource tblIamCommonResource = resourceRepository.selectByServiceIdAndName(serviceId, resourceName);
                    if (Objects.nonNull(tblIamCommonResource))
                    {
                        if (!overwrite)
                        {
                            return;
                        }
                        updateService(resourceDescription, resourceLrn, tblIamCommonResource);
                    }
                    else
                    {
                        createService(resourceName, resourceDescription, resourceLrn, serviceId);
                    }

                }
            }
        }
        catch (Exception e)
        {
            log.error("register common resources failed, error:{}", e);
        }

    }
}
