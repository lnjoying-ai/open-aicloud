package com.lnjoying.justice.iam.handler;

import org.apache.servicecomb.core.Handler;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.swagger.invocation.AsyncResponse;
import org.apache.servicecomb.swagger.invocation.exception.InvocationException;

public class ExceptionConvertHandler implements Handler {
  @Override
  public void handle(Invocation invocation, AsyncResponse asyncResp) throws Exception {
    invocation.next(response -> {
      if (response.isFailed()) {
        Throwable e = response.getResult();
        if (e instanceof InvocationException && ((InvocationException)e).getStatusCode() == 408) {
        }
      }
      asyncResp.complete(response);
    });
  }
}
