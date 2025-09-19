package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.db.model.TblOperationLog;

/**
 * operation log facade
 *
 * @author merak
 **/

public interface OperationLogFacade
{
    /**
     * Insert log record
     *
     * @param record
     * @return
     */
    int insert(TblOperationLog record);
}
