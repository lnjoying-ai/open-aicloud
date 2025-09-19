package com.lnjoying.justice.commonweb.swagger;

import io.swagger.annotations.ApiModel;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/21 11:49
 */
//@Component
public class ApiModelScanner
{
    private final List<String> scannerPackageNames = new ArrayList<>();

    private final AtomicBoolean scanned = new AtomicBoolean();

    private Set<Class<?>> cachedTypes;

    public ApiModelScanner()
    {
       this(Collections.singletonList("com.lnjoying.justice.*.db.model"));
    }

    public ApiModelScanner(List<String> scannerPackageNames)
    {
        if (!CollectionUtils.isEmpty(scannerPackageNames))
        {
            this.scannerPackageNames.addAll(scannerPackageNames);
        }
    }

    public Set<Class<?>> findTypes()
    {
        if (scanned.compareAndSet(false, true))
        {
            try
            {
                AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner(ApiModel.class);
                this.cachedTypes = annotatedTypeScanner.findTypes(scannerPackageNames);
            }
            catch (Exception e)
            {
                scanned.set(false);
            }
        }

        return this.cachedTypes;
    }
}
