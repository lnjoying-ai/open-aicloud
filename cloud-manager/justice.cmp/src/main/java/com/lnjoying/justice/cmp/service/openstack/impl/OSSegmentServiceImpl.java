package com.lnjoying.justice.cmp.service.openstack.impl;

import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.repo.OSNeutronRepository;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSSegmentSearchCritical;
import com.lnjoying.justice.cmp.service.openstack.SegmentService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSSegmentServiceImpl implements SegmentService
{
    @Autowired
    private OSNeutronRepository osNeutronRepository;

    public OSSegmentsWithDetailsRsp getSegments(String cloudId, OSSegmentSearchCritical segmentSearchCritical, String userId) throws WebSystemException
    {
        OSSegmentsWithDetailsRsp segmentsWithDetails = new OSSegmentsWithDetailsRsp();
        List<TblCmpOsNetworksegments> networksegments = getTblCmpOsNetworkSegments(cloudId, segmentSearchCritical, userId);
        if (null == networksegments) {
            return segmentsWithDetails;
        }

        List<OSSegmentInfo> segmentInfos = networksegments.stream().map(tblCmpOsSegments -> {
            OSSegmentInfo segmentInfo = new OSSegmentInfo();
            segmentInfo.setNetworkSegmentInfo(tblCmpOsSegments);
            return segmentInfo;
        }).collect(Collectors.toList());

        segmentsWithDetails.setSegments(segmentInfos);
        return segmentsWithDetails;
    }

    private List<TblCmpOsNetworksegments> getTblCmpOsNetworkSegments(String cloudId, OSSegmentSearchCritical segmentSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsNetworksegmentsExample example = new TblCmpOsNetworksegmentsExample();
        TblCmpOsNetworksegmentsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        if (StringUtils.isNotEmpty(segmentSearchCritical.getId())) criteria.andIdEqualTo(segmentSearchCritical.getId());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getNetworkId())) criteria.andNetworkIdEqualTo(segmentSearchCritical.getNetworkId());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getPhysicalNetwork())) criteria.andPhysicalNetworkEqualTo(segmentSearchCritical.getPhysicalNetwork());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getNetworkType())) criteria.andNetworkTypeEqualTo(segmentSearchCritical.getNetworkType());
        if (segmentSearchCritical.getRevisionNumber() != null) criteria.andRevisionNumberEqualTo(segmentSearchCritical.getRevisionNumber().longValue());
        if (segmentSearchCritical.getSegmentationId() != null) criteria.andSegmentationIdEqualTo(segmentSearchCritical.getSegmentationId());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getName())) criteria.andNameEqualTo(segmentSearchCritical.getName());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getDescription())) criteria.andDescriptionEqualTo(segmentSearchCritical.getDescription());
        if (StringUtils.isNotEmpty(segmentSearchCritical.getSortKey()) && StringUtils.isNotEmpty(segmentSearchCritical.getSortDir()))
        {
            example.setOrderByClause(String.format("%s %s", segmentSearchCritical.getSortKey(), segmentSearchCritical.getSortDir()));
        }

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        List<TblCmpOsNetworksegments> networksegments = osNeutronRepository.getNetworksegments(example);
        return networksegments;
    }
}
