package com.lnjoying.justice.cmp.db.repo;

import com.lnjoying.justice.cmp.db.mapper.TblCmpFlavorMapper;
import com.lnjoying.justice.cmp.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class FlavorRepository
{
	@Autowired
	private TblCmpFlavorMapper tblCmpFlavorMapper;

	public TblCmpFlavor getFlavorById(String cloudId, String flavorId)
	{
		return tblCmpFlavorMapper.selectByPrimaryKey(new TblCmpFlavorKey(cloudId, flavorId));
	}

	public int insertFlavor(TblCmpFlavor tblCmpFlavor)
	{
		return tblCmpFlavorMapper.insert(tblCmpFlavor);
	}

	public List<TblCmpFlavor> getFlavors(TblCmpFlavorExample example)
	{
		return tblCmpFlavorMapper.selectByExample(example);
	}

	public long countFlavorsByExample(TblCmpFlavorExample example)
	{
		return tblCmpFlavorMapper.countByExample(example);
	}

	public int updateFlavor(TblCmpFlavor tblCmpFlavor)
	{
		return tblCmpFlavorMapper.updateByPrimaryKeySelective(tblCmpFlavor);
	}

	public Set<String> getFlavorIds(String cloudId)
	{
		return tblCmpFlavorMapper.getFlavorIds(cloudId);
	}
}
