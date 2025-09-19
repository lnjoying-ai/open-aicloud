package com.lnjoying.justice.schema.service.ims;

import com.lnjoying.justice.schema.msg.EdgeMessage;
import io.swagger.annotations.ApiParam;

/**
 * ims service
 *
 * @author merak
 **/
public interface ImsService {
    /**
     * Process the edge message
     * @param edgeMessage
     * @return
     */
    int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);
}
