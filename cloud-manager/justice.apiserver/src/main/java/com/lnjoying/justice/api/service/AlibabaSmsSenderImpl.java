package com.lnjoying.justice.api.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.lnjoying.justice.api.config.ApiConfig;
import com.lnjoying.justice.api.config.NotifyTemplate;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.config.SmsSender;
import com.lnjoying.justice.api.config.sms.ParamValueCalculation;
import com.lnjoying.justice.api.entity.BatchSmsRsp;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("alibabaSmsSender")
public class AlibabaSmsSenderImpl implements SmsSender
{
	private static Logger LOGGER = LogManager.getLogger();
	private static Client client;

	@Autowired
	SmsConfig smsConfig;
	@Autowired
	ApiConfig apiConfig;

	public static Client getClient(SmsConfig smsConfig) throws Exception {

		if (client != null)
		{
			return client;
		}

		Config config = new Config().setAccessKeyId(smsConfig.getAlibaba().getAccessKeyId())
				.setAccessKeySecret(smsConfig.getAlibaba().getAccessKeySecret())
				.setEndpoint(smsConfig.getAlibaba().getEndpoint());

		return new Client(config);
	}

	public BatchSmsRsp sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template)
	{
		if (CollectionUtils.isEmpty(notifyParams) || CollectionUtils.isEmpty(mobiles))
		{
			return null;
		}

		NotifyTemplate notifyTemplate = apiConfig.getNotifyTemplateCode().get(template);
		if (null == notifyTemplate)
		{
			LOGGER.error("template not exist, pls check {}", template);
			return null;
		}
		String templateId = notifyTemplate.getSms();
		Map<String, String> templateParams = smsConfig.getTemplateParamsReplacement().get(templateId);
		Map<String, ParamValueCalculation> paramValueCalculations = smsConfig.getParamValueCalculation().get(templateId);

		Map<String, String> contentMap = new HashMap();
		notifyParams.forEach(smsParam -> {
			String key;
			String value = smsParam.getValue();
			if (templateParams.containsKey(smsParam.getKey()))
			{
				key = templateParams.get(smsParam.getKey());
			}
			else
			{
				key = smsParam.getKey();
			}
			if (paramValueCalculations.containsKey(key))
			{
				ParamValueCalculation paramValueCalculation = paramValueCalculations.get(key);

				switch (paramValueCalculation.getOperator())
				{
					case "+":
						value = String.valueOf(Integer.parseInt(value) + paramValueCalculation.getValue());
						break;
					case "-":
						value = String.valueOf(Integer.parseInt(value) - paramValueCalculation.getValue());
						break;
					case "*":
						value = String.valueOf(Integer.parseInt(value) * paramValueCalculation.getValue());
						break;
					case "/":
						value = String.valueOf(Integer.parseInt(value) / paramValueCalculation.getValue());
						break;
				}
			}

			contentMap.put(key, value);
		});

		SendSmsRequest sendSmsRequest = setSmsParams(templateId, JsonUtils.toJson(contentMap), mobiles);

		return submitBatchMessage(sendSmsRequest);
	}


	public BatchSmsRsp submitBatchMessage(SendSmsRequest sendSmsRequest)
	{
		try {
			Client client = getClient(smsConfig);

			SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

			LOGGER.info("SMS response result: {}", sendSmsResponse);
			return BatchSmsRsp.of(sendSmsResponse);
		} catch (Exception e) {
			LOGGER.error("send message error: {}", e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private SendSmsRequest setSmsParams(String templateCode, String templateParam, List<String> mobiles)
	{
		SendSmsRequest sendSmsRequest = new SendSmsRequest()
				.setPhoneNumbers(String.join(",", mobiles))
				.setSignName(smsConfig.getAlibaba().getSignName())
				.setTemplateCode(templateCode)
				.setTemplateParam(templateParam);

		return sendSmsRequest;
	}
}
