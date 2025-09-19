package com.lnjoying.justice.cluster.manager.service.cert;

import com.lnjoying.justice.cluster.manager.config.ClusterServerRootCA;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;

public interface CertService
{
	int genClusterCerts(ClusterInnerInfo clusterInfo, boolean rotate);

	/**
	 * Generate a certificate for the cluster server
	 * @param certName
	 * @return
	 */
	X509CertificateInfo genClusterServerCerts(String certName);

	ClusterServerRootCA genClusterServerCACerts();

	int genK3sClusterCerts(ClusterInnerInfo clusterInfo, boolean rotate);
}
