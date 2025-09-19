package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.db.model.TblCmpOsInstances;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSExtActionServerReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSServerCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.openstack.nova.OSServerUpdateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.nova.*;
import com.lnjoying.justice.cmp.domain.model.UserResourceMetrics;
import com.lnjoying.justice.cmp.entity.search.openstack.OSServerSearchCritical;
import org.springframework.http.ResponseEntity;

public interface ServerService
{
    OSServersRsp getServers(String cloudId, OSServerSearchCritical serverSearchCritical, String userId);

    ResponseEntity<Object> addServer(String cloudId, OSServerCreateReq addServerReq, String bpId, String userId);

    OSServersWithDetailsRsp getServersDetailed(String cloudId, OSServerSearchCritical serverSearchCritical, String userId);

    OSServerWithDetailsRsp getServerDetails(String cloudId, String serverId, String userId);

    ResponseEntity<Object> updateServer(String cloudId, String serverId, OSServerUpdateReq osServerUpdateReq, String bpId, String userId);

    ResponseEntity<Object> removeServer(String cloudId, String serverId, String userId);

    ResponseEntity<Object> actionServer(String cloudId, String serverId, Object request, String bpId, String userId);

    OSServerRemoteRsp remoteServer(String cloudId, String serverId, Object request, String bpId, String userId);

    OSServerVolumeAttachmentsRsp getServerVolumeAttachments(String cloudId, String serverId, Integer limit, Integer offset, String userId);

    OSExtServersWithDetailsRsp getServersDetailedPage(String cloudId, OSServerSearchCritical serverSearchCritical, String userId);

    OSExtServerVolumeAttachmentsRsp getServerVolumeAttachmentsPage(String cloudId, String serverId, Integer limit, Integer offset, String userId, Integer pageSize, Integer pageNum);

    OSExtServerWithDetailsRsp getExtServerDetails(String cloudId, String serverId, String userId);

    ResponseEntity<Object> extEctionServer(String cloudId, String serverId, OSExtActionServerReq request, String bpId, String userId);

    OSExtServerOsInterfacesRsp getServerOsInterfacesPage(String cloudId, String serverId, String userId, Integer pageSize, Integer pageNum);

    OSExtImagesWithDetailsRsp getRebuildServerImages(String cloudId, String serverId, String userId);

    void checkServerStatus(TblCmpOsInstances tblCmpOsInstances);

    UserResourceMetrics getUserResourceMetrics(String cloudId, String userId);
}
