package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.*;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class OSKeystoneRepository
{
	@Autowired
	private TblCmpOsProjectMapper tblCmpOsProjectMapper;

	public TblCmpOsProject getProjectById(String cloudId, String projectId)
	{
		return tblCmpOsProjectMapper.selectByPrimaryKey(new TblCmpOsProjectKey(cloudId, projectId));
	}

	public int insertProject(TblCmpOsProject tblCmpOsProject)
	{
		return tblCmpOsProjectMapper.insert(tblCmpOsProject);
	}

	public List<TblCmpOsProject> getProjects(TblCmpOsProjectExample example)
	{
		return tblCmpOsProjectMapper.selectByExample(example);
	}

	public long countProjectsByExample(TblCmpOsProjectExample example)
	{
		return tblCmpOsProjectMapper.countByExample(example);
	}

	public int updateProject(TblCmpOsProject tblCmpOsProject)
	{
		return tblCmpOsProjectMapper.updateByPrimaryKeySelective(tblCmpOsProject);
	}

	public Set<String> getProjectIds(String cloudId, Short isDomain)
	{
		return tblCmpOsProjectMapper.getProjectIds(cloudId, isDomain);
	}
}
