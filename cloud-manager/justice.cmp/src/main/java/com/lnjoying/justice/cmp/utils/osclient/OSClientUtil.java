package com.lnjoying.justice.cmp.utils.osclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnjoying.justice.cmp.common.CloudProduct;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.model.TblCmpOsProject;
import com.lnjoying.justice.cmp.db.model.TblCmpOsProjectExample;
import com.lnjoying.justice.cmp.db.repo.OSKeystoneRepository;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.OSServiceEndpoints;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Component
public class OSClientUtil
{
    private static CloudManagerConfig cloudManagerConfig;

    @Autowired
    public void setCloudManagerConfig(CloudManagerConfig cloudManagerConfig)
    {
        OSClientUtil.cloudManagerConfig = cloudManagerConfig;
    }


    private static OSKeystoneRepository osKeystoneRepository;

    @Autowired
    public void setKeystoneRepository(OSKeystoneRepository osKeystoneRepository)
    {
        OSClientUtil.osKeystoneRepository = osKeystoneRepository;
    }

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").create();

    private static Logger LOGGER = LogManager.getLogger();

    public static CloudManagerConfig getCloudManagerConfig()
    {
        return cloudManagerConfig;
    }

    private static Map<String, OSClientV3> osClientV3Map = new ConcurrentHashMap<>();

    public static OSClientV3 getOSClientV3(TblCloudInfo tblCloudInfo)
    {
        try
        {
            OSClientV3 osClientV3 = osClientV3Map.get(tblCloudInfo.getCloudId());

            if (osClientV3 == null)
            {
                osClientV3 = new OSClientV3();
                osClientV3.setCloudId(tblCloudInfo.getCloudId());

                osClientV3.setCloudProductShort(CloudProduct.fromName(tblCloudInfo.getProduct()).getShortName());

                Authorization authorization = gson.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

                osClientV3.setUser(authorization.getPassword().getUserid());
                osClientV3.setPassword(authorization.getPassword().getPassword());

                osClientV3.setServiceEndpoints(new HashMap<>());
                OSServiceEndpoints osServiceEndpoints = gson.fromJson(tblCloudInfo.getOsServiceEndpoints(), OSServiceEndpoints.class);
                URL cloudUrl = new URL(tblCloudInfo.getUrl());

                if (osServiceEndpoints == null || osServiceEndpoints.getEndpoints() == null || osServiceEndpoints.getEndpoints().isEmpty())
                {
                    for (OSServiceEndpoints.OSService service : OSServiceEndpoints.OSService.values())
                    {
                        osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(cloudUrl.getHost(), service.getPort(), service.getUrlPrefix()));
                    }
                }
                else
                {
                    osClientV3.setProjectId(osServiceEndpoints.getProjectId());

                    for (OSServiceEndpoints.OSService service : OSServiceEndpoints.OSService.values())
                    {
                        OSServiceEndpoints.Endpoint endpoint = osServiceEndpoints.getEndpoints().getOrDefault(service.getName(), null);
                        if (endpoint == null || StringUtils.isEmpty(endpoint.getUrl()))
                        {
                            osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(cloudUrl.getHost(), service.getPort(), service.getUrlPrefix()));
                        }
                        else
                        {
                            if (endpoint.getUrl().startsWith("https://"))
                            {
                                URL serviceUrl = new URL(endpoint.getUrl());
                                if (serviceUrl.getPort() < 1)
                                {
                                    osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(serviceUrl.getHost(), 443, serviceUrl.getPath()));
                                }
                                else
                                {
                                    osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(serviceUrl.getHost(), serviceUrl.getPort(), serviceUrl.getPath()));
                                }
                            }
                            else
                            {
                                URL serviceUrl;
                                if (!endpoint.getUrl().startsWith("http://"))
                                {
                                    serviceUrl = new URL("http://" + endpoint.getUrl());
                                }
                                else
                                {
                                    serviceUrl = new URL(endpoint.getUrl());
                                }
                                if (serviceUrl.getPort() < 1)
                                {
                                    osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(serviceUrl.getHost(), 80, serviceUrl.getPath()));
                                }
                                else
                                {
                                    osClientV3.getServiceEndpoints().put(service.getName(), new OSClientV3.ServiceEndpoint(serviceUrl.getHost(), serviceUrl.getPort(), serviceUrl.getPath()));
                                }
                            }
                        }
                    }
                }

                OSClientUtil.setOSClientV3(osClientV3);
            }

            return osClientV3;
        }
        catch (Exception e)
        {
            LOGGER.error("auth cloud failed:{}", e);
        }
        return null;
    }

    public static String getOSProject(OSClientV3 osClientV3)
    {
        if (StringUtils.isEmpty(osClientV3.getProjectId()))
        {
            TblCmpOsProjectExample example = new TblCmpOsProjectExample();
            TblCmpOsProjectExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(osClientV3.getCloudId());
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andIsDomainEqualTo((short)0);
            criteria.andEnabledEqualTo((short)1);
            List<TblCmpOsProject> projects = osKeystoneRepository.getProjects(example);
            if (! CollectionUtils.isEmpty(projects))
            {
                osClientV3.setProjectId(projects.get(0).getId());
                setOSClientV3(osClientV3);
            }

            osClientV3.authenticate();
            return null;
        }
        return osClientV3.getProjectId();
    }

    public static String getOSProject(String cloudId)
    {
        OSClientV3 osClientV3 = osClientV3Map.get(cloudId);
        if (osClientV3 == null)
        {
            return null;
        }
        return getOSProject(osClientV3);
    }

    public static void setOSClientV3(OSClientV3 osClientV3)
    {
        osClientV3Map.put(osClientV3.getCloudId(), osClientV3);
    }

    public static void removeOSClientV3(String cloudId)
    {
        osClientV3Map.remove(cloudId);
    }
}
