package com.lnjoying.justice.aos.db.repo;

import com.lnjoying.justice.aos.common.TemplateSearchCritical;
import com.lnjoying.justice.aos.db.mapper.TblStackTemplateBaseInfoMapper;
import com.lnjoying.justice.aos.db.mapper.TblStackTemplateInfoMapper;
import com.lnjoying.justice.aos.db.model.TblStackTemplateBaseInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateBaseInfoExample;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfoExample;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Repository("templateRepository")
@Transactional(rollbackFor = {Exception.class})
public class TemplateRepository
{
	@Autowired
	TblStackTemplateInfoMapper tblStackTemplateInfoMapper;

	@Autowired
	private TblStackTemplateBaseInfoMapper tblStackTemplateBaseInfoMapper;

	public int insertTemplate(TblStackTemplateInfo tblStackTemplateInfo)
	{
		return tblStackTemplateInfoMapper.insert(tblStackTemplateInfo);
	}

	public int updateTemplate(TblStackTemplateInfo tblStackTemplateInfo)
	{
		return tblStackTemplateInfoMapper.updateByPrimaryKeySelective(tblStackTemplateInfo);
	}

	public int updateTemplate(TblStackTemplateInfo tblStackTemplateInfo, TblStackTemplateInfoExample example)
	{
		return tblStackTemplateInfoMapper.updateByExampleSelective(tblStackTemplateInfo, example);
	}


	public int deleteTemplate(String templateId)
	{
		return tblStackTemplateInfoMapper.deleteByPrimaryKey(templateId);
	}

	public TblStackTemplateInfo getTemplate(String templateId)
	{
		return tblStackTemplateInfoMapper.selectByPrimaryKey(templateId);
	}

	public Long countTemplate(TblStackTemplateInfoExample example)
	{
		return tblStackTemplateInfoMapper.countByExample(example);
	}

	public List<TblStackTemplateInfo> getTemplates(TblStackTemplateInfoExample example)

	{
		return tblStackTemplateInfoMapper.selectByExample(example);
	}
	
	public List<TblStackTemplateInfo> selectByRootId(String rootId)
	{
		return tblStackTemplateInfoMapper.selectByRootId(rootId);
	}

	public int insertTemplateBasicInfo(TblStackTemplateBaseInfo baseInfo)
	{
		return tblStackTemplateBaseInfoMapper.insert(baseInfo);
	}

	public int countByNameAndBpIdAndUserId(String name, String bpId, String userId, List<String> tags)
	{

		return tblStackTemplateBaseInfoMapper.countByNameAndBpIdAndUserId(name, bpId, userId);
	}

	public List<TblStackTemplateBaseInfo> selectAllByNameAndBpIdAndUserId(TemplateSearchCritical searchCritical)
	{
		return tblStackTemplateBaseInfoMapper.selectAllByNameAndBpIdAndUserId(searchCritical.getRootId(), searchCritical.getName(), searchCritical.getBpId(), searchCritical.getUserId(),
				searchCritical.isContainsPublic(), searchCritical.getPageSize(), searchCritical.getStartRow());
	}
	
	public List<TblStackTemplateBaseInfo> selecByExample(TblStackTemplateBaseInfoExample example)
	{
		return tblStackTemplateBaseInfoMapper.selectByExample(example);
	}

	public TblStackTemplateBaseInfo selectBasicInfoByPrimaryKey(String id)
	{
		return tblStackTemplateBaseInfoMapper.selectByPrimaryKey(id);
	}

	public int updateBasicInfoByPrimaryKeySelective(TblStackTemplateBaseInfo record)
	{
		return tblStackTemplateBaseInfoMapper.updateByPrimaryKeySelective(record);
	}

	public TblStackTemplateInfo getTemplateByName(String name)
	{
		Assert.hasText(name, "name can not be empty");
		return tblStackTemplateInfoMapper.selectOneByTemplateName(name);
	}

	public List<TblStackTemplateBaseInfo> selectBasicInfoByName(String name)
	{
		return tblStackTemplateBaseInfoMapper.selectAllByName(name);
	}
}
