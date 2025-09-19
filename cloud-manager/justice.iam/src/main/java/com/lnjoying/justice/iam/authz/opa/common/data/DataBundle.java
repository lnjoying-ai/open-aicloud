package com.lnjoying.justice.iam.authz.opa.common.data;

import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.client.data.OpaDataRequest;
import com.lnjoying.justice.iam.authz.opa.common.EventType;
import lombok.Data;

import static com.lnjoying.justice.iam.authz.opa.data.DataEvent.DATA_TYPE;
import static com.lnjoying.justice.iam.authz.opa.policy.PolicyEvent.POLICY_TYPE;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/17 15:03
 */

@EventType(DATA_TYPE)
@Data
public class DataBundle implements DataProcessor
{
    private String opaDataId;

    private String data;

    @Override
    public void process(OpaClient opaClient)
    {
        OpaDataRequest opaRequest = new OpaDataRequest();
        opaRequest.setPath(this.opaDataId);
        opaRequest.setContent(data);
        opaClient.createOrOverride(opaRequest);
    }
}
