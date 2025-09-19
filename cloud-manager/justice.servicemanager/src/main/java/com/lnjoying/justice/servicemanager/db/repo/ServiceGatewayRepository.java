package com.lnjoying.justice.servicemanager.db.repo;

import com.lnjoying.justice.servicemanager.common.CommonPhaseStatus;
import com.lnjoying.justice.servicemanager.db.mapper.TblServiceGatewayInfoMapper;
import com.lnjoying.justice.servicemanager.db.mapper.TblServiceGatewayPortAllocationMapper;
import com.lnjoying.justice.servicemanager.db.mapper.TblServiceGatewayPortInfoMapper;
import com.lnjoying.justice.servicemanager.db.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository("serviceGatewayRepository")
@Transactional(rollbackFor = {Exception.class})
public class ServiceGatewayRepository
{
	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	private TblServiceGatewayInfoMapper tblServiceGatewayInfoMapper;

	@Autowired
	private TblServiceGatewayPortInfoMapper tblServiceGatewayPortInfoMapper;

	@Autowired
	private TblServiceGatewayPortAllocationMapper tblServiceGatewayPortAllocationMapper;

	public int insertServiceGateway(TblServiceGatewayInfo tblServiceGatewayInfo)
	{
		return tblServiceGatewayInfoMapper.insert(tblServiceGatewayInfo);
	}

	public int updateServiceGateway(TblServiceGatewayInfo tblServiceGatewayInfo)
	{
		return tblServiceGatewayInfoMapper.updateByPrimaryKey(tblServiceGatewayInfo);
	}

	public int updateServiceGatewaySelective(TblServiceGatewayInfo tblServiceGatewayInfo)
	{
		return tblServiceGatewayInfoMapper.updateByPrimaryKeySelective(tblServiceGatewayInfo);
	}

	public TblServiceGatewayInfo getServiceGateway(String serviceGatewayId)
	{
		return tblServiceGatewayInfoMapper.selectByPrimaryKey(serviceGatewayId);
	}

	public List<TblServiceGatewayInfo> getServiceGatewaysByExample(TblServiceGatewayInfoExample example)
	{
		return tblServiceGatewayInfoMapper.selectByExample(example);
	}

	public int insertServiceGatewayPort(TblServiceGatewayPortInfo tblServiceGatewayPortInfo)
	{
		return tblServiceGatewayPortInfoMapper.insert(tblServiceGatewayPortInfo);
	}

	public int updateServiceGatewayPort(TblServiceGatewayPortInfo tblServiceGatewayPortInfo)
	{
		return tblServiceGatewayPortInfoMapper.updateByPrimaryKey(tblServiceGatewayPortInfo);
	}

	public int updateServiceGatewayPortSelective(TblServiceGatewayPortInfo tblServiceGatewayPortInfo)
	{
		return tblServiceGatewayPortInfoMapper.updateByPrimaryKeySelective(tblServiceGatewayPortInfo);
	}

	public TblServiceGatewayPortInfo getServiceGatewayPort(String portRangeId)
	{
		return tblServiceGatewayPortInfoMapper.selectByPrimaryKey(portRangeId);
	}

	public List<TblServiceGatewayPortInfo> getServiceGatewayPortsByExample(TblServiceGatewayPortInfoExample example)
	{
		return tblServiceGatewayPortInfoMapper.selectByExample(example);
	}

	public List<TblServiceGatewayPortInfo> getServiceGatewayPortsByServiceGateway(String serviceGatewayId)
	{
		TblServiceGatewayPortInfoExample example = new TblServiceGatewayPortInfoExample();
		TblServiceGatewayPortInfoExample.Criteria criteria = example.createCriteria();
		criteria.andServiceGatewayIdEqualTo(serviceGatewayId);
		criteria.andStatusNotEqualTo(CommonPhaseStatus.REMOVED);
		example.setOrderByClause("create_time desc");
		return tblServiceGatewayPortInfoMapper.selectByExample(example);
	}

	public int insertServiceGatewayPortAllocation(TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation)
	{
		return tblServiceGatewayPortAllocationMapper.insert(tblServiceGatewayPortAllocation);
	}

	public int updateServiceGatewayPortAllocation(TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation)
	{
		return tblServiceGatewayPortAllocationMapper.updateByPrimaryKey(tblServiceGatewayPortAllocation);
	}

	public int updateServiceGatewayPortAllocationSelective(TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation)
	{
		return tblServiceGatewayPortAllocationMapper.updateByPrimaryKeySelective(tblServiceGatewayPortAllocation);
	}

	public TblServiceGatewayPortAllocation getServiceGatewayPortAllocation(String portAllocationId)
	{
		return tblServiceGatewayPortAllocationMapper.selectByPrimaryKey(portAllocationId);
	}

	public List<TblServiceGatewayPortAllocation> getServiceGatewayPortAllocationsByExample(TblServiceGatewayPortAllocationExample example)
	{
		return tblServiceGatewayPortAllocationMapper.selectByExample(example);
	}

	public List<TblServiceGatewayPortAllocation> getServiceGatewayPortAllocationsByServiceGatewayId(String serviceGatewayId)
	{
		return tblServiceGatewayPortAllocationMapper.selectByServiceGatewayId(serviceGatewayId);
	}

	public List<TblServiceGatewayPortAllocation> getServiceGatewayPortAllocationsByExampleLeftJoinPortRange(TblServiceGatewayPortAllocationExample example)
	{
		return tblServiceGatewayPortAllocationMapper.selectByExampleLeftJoinPortRange(example);
	}
}
