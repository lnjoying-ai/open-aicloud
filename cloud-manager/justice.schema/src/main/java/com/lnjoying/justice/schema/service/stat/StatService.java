package com.lnjoying.justice.schema.service.stat;

import com.lnjoying.justice.schema.msg.EdgeMessage;
import io.swagger.annotations.ApiParam;

public interface StatService
{
	int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);

}
