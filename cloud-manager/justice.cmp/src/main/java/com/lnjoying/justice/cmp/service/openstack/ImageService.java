package com.lnjoying.justice.cmp.service.openstack;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.glance.OSImageCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSExtImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImageInfo;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.glance.OSImagesWithDetailsRsp;
import com.lnjoying.justice.cmp.entity.search.openstack.OSImageSearchCritical;
import org.springframework.http.ResponseEntity;

public interface ImageService
{
    ResponseEntity<Object> addImage(String cloudId, OSImageCreateReq imageCreateReq, String bpId, String userId);

    OSImageInfo getImage(String cloudId, String imageId);

    OSImagesWithDetailsRsp getImages(String cloudId, OSImageSearchCritical imageSearchCritical, String userId);

    ResponseEntity<Object> removeImage(String cloudId, String imageId, String userId);

    OSExtImagesWithDetailsRsp getImagesPage(String cloudId, OSImageSearchCritical imageSearchCritical, String userId);
}
