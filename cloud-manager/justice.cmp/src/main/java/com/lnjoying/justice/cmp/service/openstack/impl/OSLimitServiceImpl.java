package com.lnjoying.justice.cmp.service.openstack.impl;

import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSFlavorsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSFlavorSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.openstack.LimitService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OSLimitServiceImpl implements LimitService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    public OSFlavorsRsp getLimits(String cloudId, OSFlavorSearchCritical flavorSearchCritical, String userId) throws WebSystemException
    {
        OSFlavorsRsp getFlavorsRsp = new OSFlavorsRsp();
        return getFlavorsRsp;
    }
}
