package com.lnjoying.justice.servicemanager.db.repo;

import com.lnjoying.justice.servicemanager.common.TargetServiceStatus;
import com.lnjoying.justice.servicemanager.db.mapper.TblServicePortInfoMapper;
import com.lnjoying.justice.servicemanager.db.mapper.TblServicePortTargetServiceInfoMapper;
import com.lnjoying.justice.servicemanager.db.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("servicePortRepository")
@Transactional(rollbackFor = {Exception.class})
public class ServicePortRepository
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	private TblServicePortInfoMapper tblServicePortInfoMapper;

	@Autowired
	private TblServicePortTargetServiceInfoMapper tblServicePortTargetServiceInfoMapper;

	public int insertServicePort(TblServicePortInfo tblServicePortInfo)
	{
		return tblServicePortInfoMapper.insert(tblServicePortInfo);
	}

	public int updateServicePort(TblServicePortInfo tblServicePortInfo)
	{
		return tblServicePortInfoMapper.updateByPrimaryKey(tblServicePortInfo);
	}

	public int updateServicePortSelective(TblServicePortInfo tblServicePortInfo)
	{
		return tblServicePortInfoMapper.updateByPrimaryKeySelective(tblServicePortInfo);
	}

	public TblServicePortInfo getServicePort(String servicePortId)
	{
		return tblServicePortInfoMapper.selectByPrimaryKey(servicePortId);
	}

	public List<TblServicePortInfo> getServicePortsByExample(TblServicePortInfoExample example)
	{
		return tblServicePortInfoMapper.selectByExample(example);
	}

	public int insertServicePortTargetService(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
	{
		return tblServicePortTargetServiceInfoMapper.insert(tblServicePortTargetServiceInfo);
	}

	public int updateServicePortTargetService(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
	{
		return tblServicePortTargetServiceInfoMapper.updateByPrimaryKey(tblServicePortTargetServiceInfo);
	}

	public int updateServicePortTargetServiceSelective(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
	{
		return tblServicePortTargetServiceInfoMapper.updateByPrimaryKeySelective(tblServicePortTargetServiceInfo);
	}

	public TblServicePortTargetServiceInfo getServicePortTargetService(String targetServiceId)
	{
		return tblServicePortTargetServiceInfoMapper.selectByPrimaryKey(targetServiceId);
	}

	public List<TblServicePortTargetServiceInfo> getServicePortTargetServicesByExample(TblServicePortTargetServiceInfoExample example)
	{
		return tblServicePortTargetServiceInfoMapper.selectByExample(example);
	}

	public List<TblServicePortTargetServiceInfo> getServicePortTargetServicesByServicePort(String servicePortId)
	{
		TblServicePortTargetServiceInfoExample example = new TblServicePortTargetServiceInfoExample();
		TblServicePortTargetServiceInfoExample.Criteria criteria = example.createCriteria();
		criteria.andServicePortIdEqualTo(servicePortId);
		criteria.andStatusNotEqualTo(TargetServiceStatus.REMOVED.getCode());
		example.setOrderByClause("create_time desc");
		return tblServicePortTargetServiceInfoMapper.selectByExample(example);
	}
}
