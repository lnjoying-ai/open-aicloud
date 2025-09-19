package com.lnjoying.justice.cmp.db.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface ManualMapper
{
    int getVmNum(@Param("status") int status, @Param("userId") String userId, @Param("bpId") String bpId);

    int getBaremetalNum(@Param("status") int status, @Param("userId") String userId, @Param("bpId") String bpId);

    Set<String> getSubnetConnectToRouter(@Param("cloudId") String cloudId);
}