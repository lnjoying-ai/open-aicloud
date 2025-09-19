package com.lnjoying.justice.servicemanager.domain.model.servicecomb;

import lombok.Data;

import java.util.List;

@Data
public class ServiceCombService
{
    private List<ServicesDetail> allServicesDetail;

    @Data
    public class ServicesDetail
    {
        private MicroService microService;
        private List<Instance> instances;
    }
}
