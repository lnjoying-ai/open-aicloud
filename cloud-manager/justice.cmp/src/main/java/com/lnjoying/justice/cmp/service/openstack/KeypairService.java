package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSKeypairCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSExtKeyPairsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSKeyPairWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.OSKeyPairsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSKeyPairSearchCritical;
import org.springframework.http.ResponseEntity;

public interface KeypairService
{
    OSKeyPairsRsp getKeypairs(String cloudId, OSKeyPairSearchCritical keyPairSearchCritical, String userId);

    ResponseEntity<Object> addKeypair(String cloudId, OSKeypairCreateReq addKeypairReq, String bpId, String userId);

    OSKeyPairWithDetailsRsp getKeypairDetails(String cloudId, String keypairName, String userId);

    ResponseEntity<Object> removeKeypair(String cloudId, String keypairName, String userId);

    OSExtKeyPairsRsp getKeypairsPage(String cloudId, OSKeyPairSearchCritical keyPairSearchCritical, String userId);
}
