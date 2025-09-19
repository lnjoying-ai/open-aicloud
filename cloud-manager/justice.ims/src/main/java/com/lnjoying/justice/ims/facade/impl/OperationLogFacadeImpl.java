package com.lnjoying.justice.ims.facade.impl;

import com.lnjoying.justice.ims.db.mapper.TblOperationLogMapper;
import com.lnjoying.justice.ims.db.model.TblOperationLog;
import com.lnjoying.justice.ims.facade.OperationLogFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class
 *
 * @author merak
 **/

@Service
public class OperationLogFacadeImpl implements OperationLogFacade
{
    @Autowired
    private TblOperationLogMapper tblOperationLogMapper;

    @Override
    public int insert(TblOperationLog record)
    {
        return tblOperationLogMapper.insertSelective(record);
    }
}
