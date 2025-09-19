package com.lnjoying.justice.api.service;

import com.lnjoying.justice.api.config.ApiConfig;
import com.lnjoying.justice.api.config.NotifyTemplate;
import com.lnjoying.justice.api.config.SmsConfig;
import com.lnjoying.justice.api.config.SmsSender;
import com.lnjoying.justice.api.entity.BatchSmsRsp;
import com.lnjoying.justice.api.entity.CopoteSmsRsp;
import com.lnjoying.justice.api.utils.MD5Utils;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import io.vertx.core.json.Json;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("copoteSmsSender")
public class CopoteSmsSenderImpl implements SmsSender
{
	private static Logger LOGGER = LogManager.getLogger();
	@Autowired
	SmsConfig smsConfig;
	@Autowired
	ApiConfig apiConfig;

	public BatchSmsRsp sendBatchSms(List<TipMessageService.NotifyParam> notifyParams, List<String> mobiles, String template)
	{
		if (CollectionUtils.isEmpty(notifyParams) || CollectionUtils.isEmpty(mobiles))
		{
			return null;
		}
		String url = "/tmpsubmit";
		List<String> strs = new ArrayList<>();
		notifyParams.forEach(smsParam -> strs.add(smsParam.getValue()));
		String content = JsonUtils.toJson(strs);
		Map<String, Object> params = setSmsParams(template, content, mobiles);

		return submitBatchMessage(params, url);
	}


	public BatchSmsRsp submitBatchMessage(Map<String, Object> sendParams, String url)
	{
		try {
			SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
					SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
					NoopHostnameVerifier.INSTANCE);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
			HttpComponentsClientHttpRequestFactory requestFactory =
					new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
			HttpEntity<String> requestEntity = new HttpEntity<>(Base64Utils.encode(JsonUtils.toJson(sendParams)), requestHeaders);

			ResponseEntity<String> response = restTemplate.exchange(smsConfig.getCopote().getUrl() + url, HttpMethod.POST, requestEntity, String.class);

			String result = response.getBody();
			CopoteSmsRsp smsRsp = Json.decodeValue(result, CopoteSmsRsp.class);
			LOGGER.info("SMS response result: {}" + response);
			return smsRsp.toBatchSmsRsp();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, Object>  setSmsParams(String template, String content, List<String> mobiles)
	{
		Map<String, Object> params = new HashMap<>();
		NotifyTemplate notifyTemplate = apiConfig.getNotifyTemplateCode().get(template);

		if (null == notifyTemplate)
		{
			LOGGER.error("template not exist, pls check {}", template);
			return null;
		}
		String templateId = notifyTemplate.getSms();

		String mobilesStr = String.join(",", mobiles);

		params.put("ecName", smsConfig.getCopote().getMsgEcName());
		params.put("apId", smsConfig.getCopote().getMsgApId());
		params.put("templateId", templateId);
		params.put("mobiles", mobilesStr);
		params.put("params", content);
		params.put("sign", smsConfig.getCopote().getSign());
		params.put("addSerial", smsConfig.getCopote().getAddSerial());
		String test = smsConfig.getCopote().getMsgEcName() + smsConfig.getCopote().getMsgApId() + smsConfig.getCopote().getMsgSecretKey() +
				templateId + mobilesStr + content + smsConfig.getCopote().getSign() + smsConfig.getCopote().getAddSerial();
		params.put("mac", MD5Utils.getPWD(test));

		return params;
	}
}
