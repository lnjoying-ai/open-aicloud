package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.ViewCompleteContainerInstInfo;
import com.lnjoying.justice.scheduler.db.model.ViewCompleteContainerInstInfoExample;

import java.util.List;

public interface ViewCompleteContainerInstInfoMapper
{
    long countByExample(ViewCompleteContainerInstInfoExample example);

    List<ViewCompleteContainerInstInfo> selectByExample(ViewCompleteContainerInstInfoExample example);

    ViewCompleteContainerInstInfo selectByPrimaryKey(String instId);
}