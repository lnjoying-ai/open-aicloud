package com.lnjoying.justice.api.service;

import com.lnjoying.justice.api.config.ApiConfig;
import com.lnjoying.justice.api.config.NotifyTemplate;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.config.SmsSender;
import com.lnjoying.justice.api.entity.BatchSmsRsp;
import com.lnjoying.justice.api.utils.MD5Utils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import com.micro.core.common.Utils;
import io.vertx.core.json.Json;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ucppassSmsSender")
public class UcPaasSmsSenderImpl implements SmsSender
{
	private static Logger LOGGER = LogManager.getLogger();
	@Autowired
    SmsConfig smsConfig;
	@Autowired
    ApiConfig apiConfig;

	public BatchSmsRsp sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template)
	{
		String url = "/templatesms";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < notifyParams.size(); i++) {
			sb.append(notifyParams.get(i).getValue()).append(";");
		}
		Map<String, Object> params = setSmsParams(template, sb.substring(0, sb.length() - 1), mobiles, Utils.assignUUId());
		if (null == params)
		{
			return null;
		}
		return submitBatchMessage(params, url);
	}


	public BatchSmsRsp submitBatchMessage(Map<String, Object> sendParams, String url) {
		try {

			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(smsConfig.getUcpaas().getUrl() + url);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(JsonUtils.toJson(sendParams),"UTF-8"));
			HttpEntity entity = httpClient.execute(httpPost).getEntity();
			String result = EntityUtils.toString(entity);
			BatchSmsRsp smsRsp = Json.decodeValue(result, BatchSmsRsp.class);
			LOGGER.info("SMS response result:" + smsRsp.toString());
			return smsRsp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, Object>  setSmsParams(String template, String content, List<String> mobiles, String uid)
	{
		Map<String, Object> params = new HashMap<>();
		NotifyTemplate notifyTemplate = apiConfig.getNotifyTemplateCode().get(template);

		if (null == notifyTemplate)
		{
			LOGGER.error("template not exist, pls check {}", template);
			return null;
		}
		String templateId = notifyTemplate.getSms();

		setCommonParams(params);
		params.put("templateid", templateId);
		params.put("param", content);
		params.put("mobile", String.join(",", mobiles));
		params.put("uid", uid);
		return params;
	}

	void setCommonParams(Map<String, Object> params)
	{
		params.put("clientid", smsConfig.getUcpaas().getClientid());
		params.put("password", MD5Utils.getPWD(smsConfig.getUcpaas().getPassword()));
	}
}
