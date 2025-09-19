package com.lnjoying.justice.ecrm.facade;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.db.repo.LabelOptionRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.LabelOption;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelOptionServiceFacade
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	LabelOptionRepository labelOptionRepository;

	public List<LabelOption> getLabelOption(String labelType)
	{
		List<LabelOption> labelOptions;
		String labelOptionCache = RedisUtil.get(RedisCache.LABEL_OPTION, labelType);
		if (StringUtils.isEmpty(labelOptionCache))
		{
			labelOptions = labelOptionRepository.selectLabelOption(labelType, "label");

			//set all region cache
			RedisUtil.set(RedisCache.LABEL_OPTION, labelType, JsonUtils.toJson(labelOptions), RedisCache.LABEL_OPTION_DURATION);
		}
		else
		{
			labelOptions = JsonUtils.fromJson(labelOptionCache, new TypeToken<List<LabelOption>>(){}.getType());
		}
		return labelOptions;
	}

	public List<LabelOption> getTaintOption(String taintType)
	{
		List<LabelOption> labelOptions;
		String labelOptionCache = RedisUtil.get(RedisCache.TAINT_OPTION, taintType);
		if (StringUtils.isEmpty(labelOptionCache))
		{
			labelOptions = labelOptionRepository.selectLabelOption(taintType, "taint");

			//set all region cache
			RedisUtil.set(RedisCache.TAINT_OPTION, taintType, JsonUtils.toJson(labelOptions), RedisCache.LABEL_OPTION_DURATION);
		}
		else
		{
			labelOptions = JsonUtils.fromJson(labelOptionCache, new TypeToken<List<LabelOption>>(){}.getType());
		}
		return labelOptions;
	}
}
