package com.lnjoying.justice.iam.authz.opa.client.query;

/**
 * @Description:
 * <P>Execute a simple query</P>
 * @see <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#query-api>opa query api docs</a>
 * @Author: Merak
 * @Date: 2023/2/23 15:18
 */

public interface OpaQueryApi
{
    <R> R query(OpaQueryRequest request, Class<R> responseType);

    <R> R adHocQuery(OpaQueryRequest request, Class<R> responseType);
}
