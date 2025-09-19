package com.lnjoying.justice.omc.service.impl;

import com.lnjoying.justice.omc.service.AosStackTemplateService;
import com.lnjoying.justice.omc.service.CombRpcService;
import com.lnjoying.justice.schema.service.aos.AosTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/27 20:32
 */

@Slf4j
@Service
public class AosTemplateServiceImpl implements AosStackTemplateService
{
    @Autowired
    private CombRpcService combRpcService;


    @Override
    public int countStackByName(String stackTemplateName)
    {
        return combRpcService.getAosTemplateService().countStackByName(stackTemplateName);
    }

    @Override
    public int addStackTemplate(AosTemplateService.StackTemplateReq addTemplateReq)
    {
        return combRpcService.getAosTemplateService().addStackTemplate(addTemplateReq);
    }
}
