package com.lnjoying.justice.cluster.manager.service.client;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class K8sClientServiceImpl implements K8sClientService
{

    private static Logger LOGGER = LogManager.getLogger();

    public AppsV1Api getApiInstance(InputStream stream) throws IOException
    {
        ApiClient client = Config.fromConfig(stream);
        Configuration.setDefaultApiClient(client);
        return new AppsV1Api(client);
    }

    public CoreV1Api getCoreApiInstance(InputStream stream) throws IOException
    {
        ApiClient client = Config.fromConfig(stream);
        Configuration.setDefaultApiClient(client);
        return new CoreV1Api(client);
    }

    public V1Deployment createK8sDeployment(AppsV1Api apiInstance, V1Deployment deployment, String namespace)
    {
        V1Deployment result = null;

        try
        {
            result = apiInstance.createNamespacedDeployment(
                    namespace,
                    deployment,
                    "true",
                    null,
                    null,
                    null);

        }
        catch (ApiException e)
        {
            LOGGER.error("create k8s deployment error, msg: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public V1Status deleteK8sDeployment(AppsV1Api apiInstance, String name, String namespace) throws ApiException
    {
        V1Status result;
        result = apiInstance.deleteNamespacedDeployment(name, namespace, "true", null, null, null, null, null);
        System.out.println(result);

        return result;
    }

    public V1Deployment getK8sDeployment(AppsV1Api apiInstance, String name, String namespace) throws ApiException
    {
        V1Deployment result = null;
        result = apiInstance.readNamespacedDeployment(name, namespace, "true");
        //System.out.println(result);

        return result;
    }

    public V1DeploymentList listK8sDeployment(AppsV1Api apiInstance, String namespace)
    {
        V1DeploymentList result;

        try
        {
            result = apiInstance.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null);
        }
        catch (ApiException e)
        {
            return null;
        }

        return result;
    }

    public V1PodList listK8sPod(CoreV1Api apiCore, String name, String namespace) throws ApiException
    {
        V1PodList list = null;

        list = apiCore.listNamespacedPod(namespace, "true", null, null, null, "app="+name, null, null, null, null, null);

        return list;
    }

    public V1PodList getK8sPods(CoreV1Api apiCore, String namespace) throws ApiException
    {
        V1PodList result = null;
        result = apiCore.listNamespacedPod(namespace,"true", null, null, null, null, null, null, null, null, null);

        return result;
    }
}
