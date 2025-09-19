package com.lnjoying.justice.cluster.manager.service.client;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.openapi.models.V1Status;

import java.io.IOException;
import java.io.InputStream;

public interface K8sClientService
{
    AppsV1Api getApiInstance(InputStream stream) throws IOException;

    CoreV1Api getCoreApiInstance(InputStream stream) throws IOException;

    V1Deployment createK8sDeployment(AppsV1Api apiInstance, V1Deployment deployment, String namespace);

    V1Status deleteK8sDeployment(AppsV1Api apiInstance, String name, String namespace) throws ApiException;

    V1Deployment getK8sDeployment(AppsV1Api apiInstance, String name, String namespace) throws ApiException;

    V1DeploymentList listK8sDeployment(AppsV1Api apiInstance, String namespace);

    V1PodList listK8sPod(CoreV1Api apiCore, String name, String namespace) throws ApiException;

    V1PodList getK8sPods(CoreV1Api apiCore, String namespace) throws ApiException;
}
