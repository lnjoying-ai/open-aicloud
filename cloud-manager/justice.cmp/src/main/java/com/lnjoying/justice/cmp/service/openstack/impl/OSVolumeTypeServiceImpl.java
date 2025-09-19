package com.lnjoying.justice.cmp.service.openstack.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypes;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeTypesExample;
import com.lnjoying.justice.cmp.db.repo.OSCinderRepository;
import com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder.*;
import com.lnjoying.justice.cmp.entity.search.openstack.OSVolumeTypeSearchCritical;
import com.lnjoying.justice.cmp.service.nextstack.baremetal.BaremetalComputeService;
import com.lnjoying.justice.cmp.service.openstack.VolumeTypeService;
import com.lnjoying.justice.cmp.utils.BoolUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class OSVolumeTypeServiceImpl implements VolumeTypeService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    @Autowired
    private OSCinderRepository osCinderRepository;

    public OSVolumeTypesWithDetailsRsp getVolumeTypesWithDetails(String cloudId, OSVolumeTypeSearchCritical volumeTypeSearchCritical, String userId) throws WebSystemException
    {
        OSVolumeTypesWithDetailsRsp volumeTypesWithDetailsRsp = new OSVolumeTypesWithDetailsRsp();
        List<TblCmpOsVolumeTypes> types = osCinderRepository.getVolumeTypes(getTblCmpOsVolumeTypesExample(cloudId, volumeTypeSearchCritical, userId));
        if (null == types) {
            return volumeTypesWithDetailsRsp;
        }

        List<OSVolumeTypeInfo> volumeTypeInfos = types.stream().map(tblCmpOsVolumeType -> {
            OSVolumeTypeInfo volumeTypeInfo = new OSVolumeTypeInfo();
            volumeTypeInfo.setVolumeTypeInfo(tblCmpOsVolumeType);
            return volumeTypeInfo;
        }).collect(Collectors.toList());

        volumeTypesWithDetailsRsp.setVolumeTypes(volumeTypeInfos);
        return volumeTypesWithDetailsRsp;
    }

    private TblCmpOsVolumeTypesExample getTblCmpOsVolumeTypesExample(String cloudId, OSVolumeTypeSearchCritical volumeTypeSearchCritical, String userId) throws WebSystemException
    {
        TblCmpOsVolumeTypesExample example = new TblCmpOsVolumeTypesExample();
        TblCmpOsVolumeTypesExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        example.setOrderByClause("created_at desc");

        if (volumeTypeSearchCritical.getIsPublic() != null) criteria.andIsPublicEqualTo(BoolUtil.bool2Short(volumeTypeSearchCritical.getIsPublic()));

        if (StringUtils.isNotEmpty(userId)) criteria.andEeUserEqualTo(userId);

        return example;
    }

    public OSExtVolumeTypesWithDetailsRsp getVolumeTypesWithDetailsPage(String cloudId, OSVolumeTypeSearchCritical volumeTypeSearchCritical, String userId) throws WebSystemException
    {
        OSExtVolumeTypesWithDetailsRsp volumeTypesWithDetailsRsp = new OSExtVolumeTypesWithDetailsRsp();

        PageHelper.startPage(volumeTypeSearchCritical.getPageNum(), volumeTypeSearchCritical.getPageSize());
        List<TblCmpOsVolumeTypes> types = osCinderRepository.getVolumeTypes(getTblCmpOsVolumeTypesExample(cloudId, volumeTypeSearchCritical, userId));
        PageInfo<TblCmpOsVolumeTypes> pageInfo = new PageInfo<>(types);

        if (null == types) {
            return volumeTypesWithDetailsRsp;
        }

        List<OSVolumeTypeInfo> volumeTypeInfos = types.stream().map(tblCmpOsVolumeType -> {
            OSVolumeTypeInfo volumeTypeInfo = new OSVolumeTypeInfo();
            volumeTypeInfo.setVolumeTypeInfo(tblCmpOsVolumeType);
            return volumeTypeInfo;
        }).collect(Collectors.toList());

        volumeTypesWithDetailsRsp.setVolumeTypes(volumeTypeInfos);
        volumeTypesWithDetailsRsp.setTotalNum(pageInfo.getTotal());
        return volumeTypesWithDetailsRsp;
    }
}
