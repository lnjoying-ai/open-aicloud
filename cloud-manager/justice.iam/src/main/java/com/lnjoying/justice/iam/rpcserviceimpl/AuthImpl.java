package com.lnjoying.justice.iam.rpcserviceimpl;

import io.swagger.annotations.ApiOperation;
import org.apache.servicecomb.provider.pojo.RpcSchema;

@RpcSchema(schemaId = "authrpc")
public class AuthImpl
{
	@ApiOperation(value = "islogout", nickname = "islogout")
	public Boolean islogout()
	{
		return false;
	}
}