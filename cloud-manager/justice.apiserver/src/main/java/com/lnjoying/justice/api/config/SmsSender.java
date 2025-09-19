/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com.lnjoying.justice.api.config;

import com.lnjoying.justice.api.entity.BatchSmsRsp;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;

import java.util.List;

public interface SmsSender
{
	BatchSmsRsp sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template);
}
