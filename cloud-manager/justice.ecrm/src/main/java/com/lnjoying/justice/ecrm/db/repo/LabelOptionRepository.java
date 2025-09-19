package com.lnjoying.justice.ecrm.db.repo;

import com.google.gson.Gson;
import com.lnjoying.justice.ecrm.db.mapper.*;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.domain.dto.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("labelOptionRepository")
@Transactional(rollbackFor = {Exception.class})
public class LabelOptionRepository
{
	@Autowired
	TblLabelOptionInfoMapper tblLabelOptionInfoMapper;

	Gson gson =  new Gson();

	public List<LabelOption> selectLabelOption(String labelType, String type)
	{
		List<LabelOption> labelOptions = new ArrayList<>();
		TblLabelOptionInfoExample example = new TblLabelOptionInfoExample();
		TblLabelOptionInfoExample.Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(type);
		criteria.andLabelTypeEqualTo(labelType);
		example.setOrderByClause("order_num");
		List<TblLabelOptionInfo> tblLabelOptionInfos = tblLabelOptionInfoMapper.selectByExample(example);
		for (TblLabelOptionInfo tblLabelOptionInfo : tblLabelOptionInfos)
		{
			labelOptions.add(gson.fromJson(tblLabelOptionInfo.getLabelOption(), LabelOption.class));
		}
		return labelOptions;
	}
}
