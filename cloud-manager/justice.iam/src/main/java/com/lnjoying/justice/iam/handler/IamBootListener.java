package com.lnjoying.justice.iam.handler;

import com.lnjoying.justice.iam.config.opa.OpaProperties;
import com.lnjoying.justice.iam.service.*;
import org.apache.servicecomb.core.BootListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/1 15:04
 */

@Component
public class IamBootListener implements BootListener
{
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private BasePolicyService basePolicyService;

    @Autowired
    private UserManagerService userService;

    @Autowired
    private OpaProperties opaProperties;

    @Autowired
    private ProjectService projectService;

    @Override
    public void onBeforeProducerProvider(BootEvent event)
    {
        basePolicyService.checkBasePolicy();
        //userService.loadUserAttributesToOpa();
        projectService.checkProject();
    }

    @Override
    public void onAfterRegistry(BootEvent event)
    {
        if (Objects.nonNull(opaProperties.getResource()) && opaProperties.getResource().isUpdate())
        {
            resourceService.registerCommonResourceInfos(true);
        }

        resourceService.registerRequestMapping();
        policyService.loadPolicyBundleFromDb();
        policyService.loadAllResourceTypesFromDb();
        policyService.loadAuthzDatasFromDb();
        resourceService.loadServiceActions();
    }

    @Override
    public int getOrder()
    {
        return Short.MAX_VALUE + 1;
    }
}