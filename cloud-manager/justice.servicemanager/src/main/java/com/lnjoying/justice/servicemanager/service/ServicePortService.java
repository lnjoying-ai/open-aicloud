package com.lnjoying.justice.servicemanager.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.lnjoying.justice.schema.entity.servicemanager.RpcPortMappingResult;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.servicemanager.common.*;
import com.lnjoying.justice.servicemanager.db.model.TblServiceGatewayInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfoExample;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import com.lnjoying.justice.servicemanager.db.repo.ServiceGatewayRepository;
import com.lnjoying.justice.servicemanager.domain.model.AllocPortResult;
import com.lnjoying.justice.servicemanager.domain.model.ServicePort;
import com.lnjoying.justice.servicemanager.domain.model.TargetServiceTestResult;
import com.lnjoying.justice.servicemanager.service.process.ServiceManagerMsgProcessStrategy;
import com.lnjoying.justice.servicemanager.service.rpc.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfo;
import com.lnjoying.justice.servicemanager.db.repo.ServicePortRepository;
import com.lnjoying.justice.servicemanager.domain.dto.request.AddServicePortReq;
import com.lnjoying.justice.servicemanager.domain.dto.request.UpdateServicePortReq;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServicePortRsp;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServicePortsRsp;
import com.lnjoying.justice.servicemanager.entity.search.ServicePortSearchCritical;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicePortService
{
	private static Logger LOGGER = LogManager.getLogger();

	private static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder().setConnectTimeout(5 * 1000).setReadTimeout(5 * 1000).build();

	@Autowired
	private ServicePortRepository servicePortRepository;

	@Autowired
	private ServiceGatewayRepository serviceGatewayRepository;

	@Autowired
	private ServiceGatewayService serviceGatewayService;

	@Autowired
	private CombRpcService combRpcService;

	@Autowired
	private ServiceManagerMsgProcessStrategy serviceManagerMsgProcessStrategy;

	private static Gson gson = new Gson();

	public String addServicePort(AddServicePortReq addServicePortReq, String bpId, String userId)
	{
		try
		{
			TblServicePortInfo tblServicePortInfo = new TblServicePortInfo();
			tblServicePortInfo.setId(Utils.buildStr("JST_SP_", Utils.assignUUId()));
			tblServicePortInfo.setName(addServicePortReq.getName());
			tblServicePortInfo.setPurpose(addServicePortReq.getPurpose());
			tblServicePortInfo.setTargetType(addServicePortReq.getTargetType());
			tblServicePortInfo.setDeployment(addServicePortReq.getDeployment());
			tblServicePortInfo.setStatus(ServicePortStatus.CREATING.getCode());
			tblServicePortInfo.setDescription(addServicePortReq.getDescription());
			tblServicePortInfo.setBpId(bpId);
			tblServicePortInfo.setUserId(userId);
			tblServicePortInfo.setTags(gson.toJson(addServicePortReq.getTags()));
			tblServicePortInfo.setCreateTime(new Date());
			tblServicePortInfo.setUpdateTime(tblServicePortInfo.getCreateTime());
			servicePortRepository.insertServicePort(tblServicePortInfo);

			if (CollectionUtils.isNotEmpty(addServicePortReq.getAddTargetServiceReqs()))
			{
				addServicePortReq.getAddTargetServiceReqs().forEach(addTargetServiceReq -> {
					TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = new TblServicePortTargetServiceInfo();
					tblServicePortTargetServiceInfo.setServicePortId(tblServicePortInfo.getId());
					tblServicePortTargetServiceInfo.setId(Utils.buildStr("JST_SP_TS_", Utils.assignUUId()));
					tblServicePortTargetServiceInfo.setDescription(addTargetServiceReq.getDescription());
					tblServicePortTargetServiceInfo.setTargetSvcNode(addTargetServiceReq.getSvcNode());
					tblServicePortTargetServiceInfo.setTargetSvcPort(addTargetServiceReq.getSvcPort());
					tblServicePortTargetServiceInfo.setSvcNode(null);
					tblServicePortTargetServiceInfo.setSvcIp(null);
					tblServicePortTargetServiceInfo.setSvcPort(null);
					tblServicePortTargetServiceInfo.setProtocol(addTargetServiceReq.getProtocol());
					tblServicePortTargetServiceInfo.setSite(addTargetServiceReq.getSiteId());
					tblServicePortTargetServiceInfo.setService(addTargetServiceReq.getService());
					tblServicePortTargetServiceInfo.setTargetIp(addTargetServiceReq.getTargetIp());
					tblServicePortTargetServiceInfo.setTargetPort(addTargetServiceReq.getTargetPort());
					tblServicePortTargetServiceInfo.setCert(addTargetServiceReq.getCert());
					tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.CREATING.getCode());
					tblServicePortTargetServiceInfo.setCreateTime(tblServicePortInfo.getCreateTime());
					tblServicePortTargetServiceInfo.setUpdateTime(tblServicePortInfo.getCreateTime());
					servicePortRepository.insertServicePortTargetService(tblServicePortTargetServiceInfo);
				});
			}

			return tblServicePortInfo.getId();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("add service port error. {}. for {}", e.getMessage(), addServicePortReq.getName());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public GetServicePortsRsp getServicePorts(ServicePortSearchCritical servicePortSearchCritical)
	{
		GetServicePortsRsp getServicePortsRsp = new GetServicePortsRsp();

		TblServicePortInfoExample example = new TblServicePortInfoExample();
		TblServicePortInfoExample.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getServicePortId())) criteria.andIdEqualTo(servicePortSearchCritical.getServicePortId());
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getName())) criteria.andNameLike("%" + servicePortSearchCritical.getName() + "%");
		if (servicePortSearchCritical.getStatus() != null)
		{
			criteria.andStatusEqualTo(servicePortSearchCritical.getStatus());
		}
		else
		{
			criteria.andStatusNotEqualTo(ServicePortStatus.REMOVED.getCode());
		}
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getTargetType())) criteria.andTargetTypeEqualTo(servicePortSearchCritical.getTargetType());
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getDeployment())) criteria.andDeploymentEqualTo(servicePortSearchCritical.getDeployment());
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getPurpose())) criteria.andPurposeEqualTo(servicePortSearchCritical.getPurpose());
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getBp())) criteria.andBpIdEqualTo(servicePortSearchCritical.getBp());
		if (StringUtils.isNotEmpty(servicePortSearchCritical.getUser())) criteria.andUserIdEqualTo(servicePortSearchCritical.getUser());
		example.setOrderByClause("create_time desc");

		PageHelper.startPage(servicePortSearchCritical.getPageNum(), servicePortSearchCritical.getPageSize());
		List<TblServicePortInfo> servicePortInfos = servicePortRepository.getServicePortsByExample(example);
		PageInfo<TblServicePortInfo> pageInfo = new PageInfo<>(servicePortInfos);

		List<ServicePort> servicePorts = servicePortInfos.stream().map(tblServicePortInfo -> {
			ServicePort servicePort = ServicePort.of(tblServicePortInfo);
			servicePort.setPortMap(servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId()));
			assembleName(servicePort);
			return servicePort;
		}).collect(Collectors.toList());

		getServicePortsRsp.setServicePorts(servicePorts);
		getServicePortsRsp.setTotalNum(pageInfo.getTotal());

		return getServicePortsRsp;
	}

	public GetServicePortRsp getServicePort(String servicePortId)
	{
		GetServicePortRsp getServicePortRsp = new GetServicePortRsp();

		TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);

		if (tblServicePortInfo == null || tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode())
		{
			throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
		}

		ServicePort servicePort = ServicePort.of(tblServicePortInfo);
		servicePort.setPortMap(servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId()));
		assembleName(servicePort);

		getServicePortRsp.setServicePort(servicePort);
		return getServicePortRsp;
	}

	public void updateServicePort(String servicePortId, UpdateServicePortReq updateServicePortReq)
	{
		try
		{
			TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);

			if (tblServicePortInfo == null)
			{
				throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
			}

			tblServicePortInfo.setName(updateServicePortReq.getName());
			tblServicePortInfo.setDescription(updateServicePortReq.getDescription());
			tblServicePortInfo.setTags(gson.toJson(updateServicePortReq.getTags()));
			tblServicePortInfo.setStatus(ServicePortStatus.UPDATING.getCode());
			servicePortRepository.updateServicePort(tblServicePortInfo);

			List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId());

			List<TblServicePortTargetServiceInfo> newTblServicePortTargetServiceInfos = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(updateServicePortReq.getAddTargetServiceReqs()))
			{
				updateServicePortReq.getAddTargetServiceReqs().forEach(addTargetServiceReq -> {
					TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = new TblServicePortTargetServiceInfo();
					tblServicePortTargetServiceInfo.setServicePortId(tblServicePortInfo.getId());
					tblServicePortTargetServiceInfo.setDescription(addTargetServiceReq.getDescription());
					tblServicePortTargetServiceInfo.setTargetSvcNode(addTargetServiceReq.getSvcNode());
					tblServicePortTargetServiceInfo.setTargetSvcPort(addTargetServiceReq.getSvcPort());
					tblServicePortTargetServiceInfo.setSvcNode(null);
					tblServicePortTargetServiceInfo.setSvcIp(null);
					tblServicePortTargetServiceInfo.setSvcPort(null);
					tblServicePortTargetServiceInfo.setProtocol(addTargetServiceReq.getProtocol());
					tblServicePortTargetServiceInfo.setSite(addTargetServiceReq.getSiteId());
					tblServicePortTargetServiceInfo.setService(addTargetServiceReq.getService());
					tblServicePortTargetServiceInfo.setTargetIp(addTargetServiceReq.getTargetIp());
					tblServicePortTargetServiceInfo.setTargetPort(addTargetServiceReq.getTargetPort());
					tblServicePortTargetServiceInfo.setCert(addTargetServiceReq.getCert());
					newTblServicePortTargetServiceInfos.add(tblServicePortTargetServiceInfo);
				});
			}
			Set<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfoSet = new HashSet<>(newTblServicePortTargetServiceInfos);

			for (TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo : tblServicePortTargetServiceInfos)
			{
				boolean contain = false;
				for (TblServicePortTargetServiceInfo newTblServicePortTargetServiceInfo : tblServicePortTargetServiceInfoSet)
				{
					if (tblServicePortTargetServiceInfo.equals(newTblServicePortTargetServiceInfo))
					{
						tblServicePortTargetServiceInfoSet.remove(newTblServicePortTargetServiceInfo);

						if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.NO_RESOURCE.getCode()
								|| tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.NOT_SATISFIED.getCode()
								|| tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.CREATE_FILED.getCode())
						{
							tblServicePortTargetServiceInfo.setTargetSvcNode(newTblServicePortTargetServiceInfo.getTargetSvcNode());
							tblServicePortTargetServiceInfo.setTargetSvcPort(newTblServicePortTargetServiceInfo.getTargetSvcPort());
							tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.UPDATING.getCode());
							tblServicePortTargetServiceInfo.setUpdateTime(new Date());
							servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
						}

						contain = true;
						break;
					}
				}

				if (!contain)
				{
					tblServicePortTargetServiceInfo.setStatus(ServicePortStatus.REMOVING.getCode());
					tblServicePortTargetServiceInfo.setUpdateTime(new Date());
					servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
				}
			}

			for (TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo : tblServicePortTargetServiceInfoSet)
			{
				tblServicePortTargetServiceInfo.setId(Utils.buildStr("JST_SP_TS_", Utils.assignUUId()));
				tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.CREATING.getCode());
				tblServicePortTargetServiceInfo.setCreateTime(tblServicePortInfo.getCreateTime());
				tblServicePortTargetServiceInfo.setUpdateTime(tblServicePortInfo.getCreateTime());
				servicePortRepository.insertServicePortTargetService(tblServicePortTargetServiceInfo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update service port error. {}. for {}", e.getMessage(), servicePortId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void deleteServicePort(String servicePortId)
	{
		TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);

		if (tblServicePortInfo == null)
		{
			throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
		}

		tblServicePortInfo.setStatus(ServicePortStatus.REMOVING.getCode());
		servicePortRepository.updateServicePort(tblServicePortInfo);
	}

	public void reallocateServicePort(String servicePortId)
	{
		try
		{
			TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);

			if (tblServicePortInfo == null || tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode())
			{
				throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
			}

			tblServicePortInfo.setStatus(ServicePortStatus.UPDATING.getCode());
			servicePortRepository.updateServicePort(tblServicePortInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("reallocate service port error. {}. for {}", e.getMessage(), servicePortId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void allocPort(String targetServiceId)
	{
		try
		{
			TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(targetServiceId);

			if (tblServicePortTargetServiceInfo == null ||
					(tblServicePortTargetServiceInfo.getStatus() != TargetServiceStatus.CREATING.getCode()
							&& tblServicePortTargetServiceInfo.getStatus() != TargetServiceStatus.UPDATING.getCode()))
			{
				return;
			}

			TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(tblServicePortTargetServiceInfo.getServicePortId());
			if (tblServicePortInfo == null ||
					(tblServicePortInfo.getStatus() != ServicePortStatus.CREATING.getCode()
							&& tblServicePortInfo.getStatus() != ServicePortStatus.UPDATING.getCode()))
			{
				return;
			}

			AllocPortResult allocPortResult = serviceGatewayService.allocPort(tblServicePortInfo, tblServicePortTargetServiceInfo);
			if (allocPortResult == null)
			{
				return;
			}
			else if (allocPortResult.getStatus() == TargetServiceStatus.READY.getCode())
			{
				tblServicePortTargetServiceInfo.setSvcNode(allocPortResult.getSvcNode());
				tblServicePortTargetServiceInfo.setPortAllocationId(allocPortResult.getPortAllocationId());
				if (tblServicePortInfo.getPurpose().startsWith(PurposePrefix.SERVICE.getName()))
				{
					tblServicePortTargetServiceInfo.setSvcIp(allocPortResult.getExternalIp());
					if (tblServicePortTargetServiceInfo.getSvcIp() == null)
					{
						tblServicePortTargetServiceInfo.setSvcIp(allocPortResult.getInternalIp());
					}
				}
				else if (tblServicePortInfo.getPurpose().startsWith(PurposePrefix.INNER.getName()))
				{
					tblServicePortTargetServiceInfo.setSvcIp(allocPortResult.getInternalIp());
				}
				else
				{
					tblServicePortTargetServiceInfo.setSvcIp(allocPortResult.getExternalIp());
				}
				tblServicePortTargetServiceInfo.setSvcPort(allocPortResult.getSvcPort());
			}
			tblServicePortTargetServiceInfo.setStatus(allocPortResult.getStatus());
			tblServicePortTargetServiceInfo.setUpdateTime(new Date());
			servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);

			if (tblServicePortInfo.getTags().contains("microServiceName:") && tblServicePortInfo.getTags().contains("reqId:"))
			{
				MessagePack messagePack = new MessagePack();
				messagePack.setMessageObj(tblServicePortInfo.getId());
				messagePack.setMsgType(ServiceManagerMsg.ADD_SERVICE_PORT_CALLBACK);
				serviceManagerMsgProcessStrategy.sendMessageIfNotInQueue(messagePack, ProcessorName.SERVICE_MANAGER_MSG);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("alloc service port error. {}. for {}", e.getMessage(), targetServiceId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void releasePort(String targetServiceId)
	{
		try
		{
			TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(targetServiceId);

			if (tblServicePortTargetServiceInfo == null)
			{
				return;
			}

			serviceGatewayService.releasePort(tblServicePortTargetServiceInfo);
			tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.REMOVED.getCode());
			tblServicePortTargetServiceInfo.setUpdateTime(new Date());
			servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("release port error. {}. for {}", e.getMessage(), targetServiceId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	private void assembleName(ServicePort servicePort)
	{
		servicePort.getTargetServices().forEach(targetService -> {
			if (StringUtils.isNotEmpty(targetService.getSiteId()))
			{
				targetService.setSiteName(combRpcService.getRegionResourceService().getSiteNameById(targetService.getSiteId()));
			}
			if (StringUtils.isNotEmpty(targetService.getService()))
			{
				if (servicePort.getTargetType().equals(TargetType.CONTAINER))
				{
					targetService.setServiceName(combRpcService.getContainerInstService().getInstName(targetService.getService()));
				}
				else if (servicePort.getTargetType().equals(TargetType.COMPOSE))
				{
					targetService.setServiceName(combRpcService.getAosService().getStackName(targetService.getService()));
				}
			}
		});
		if (StringUtils.isNotEmpty(servicePort.getDeployment()))
		{
			switch (servicePort.getTargetType())
			{
				case TargetType.CONTAINER:
				{
					servicePort.setDeploymentName(combRpcService.getContainerInstService().getSpecName(servicePort.getDeployment()));
					break;
				}
				case TargetType.COMPOSE:
				{
					servicePort.setDeploymentName(combRpcService.getAosService().getSpecName(servicePort.getDeployment()));
					break;
				}
				case TargetType.CLOUD:
				{
					servicePort.setDeploymentName(combRpcService.getCloudManagerService().getCloudName(servicePort.getDeployment()));
					break;
				}
				case TargetType.K8S_CLUSTER:
				{
					servicePort.setDeploymentName(combRpcService.getClusterManagerService().getClusterName(servicePort.getDeployment()));
					break;
				}
				case TargetType.EDGE_NODE:
				{
					servicePort.setDeploymentName(combRpcService.getRegionResourceService().getNodeNameById(servicePort.getDeployment()));
					break;
				}
				case TargetType.OTHER:
				{
					break;
				}
			}
		}
		if (StringUtils.isNotEmpty(servicePort.getUserId()))
		{
			servicePort.setUserName(combRpcService.getUmsService().getUserNameByUserId(servicePort.getUserId()));
		}
		if (StringUtils.isNotEmpty(servicePort.getBpId()))
		{
			servicePort.setBpName(combRpcService.getUmsService().getBpNameByBpId(servicePort.getBpId()));
		}
		servicePort.getTargetServices().forEach(targetService -> {
			if (StringUtils.isNotEmpty(targetService.getTargetSvcNode()))
			{
				TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetService.getTargetSvcNode());
				if (tblServiceGatewayInfo != null) targetService.setTargetSvcNodeName(tblServiceGatewayInfo.getName());
			}
			if (StringUtils.isNotEmpty(targetService.getSvcNode()))
			{
				TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetService.getSvcNode());
				if (tblServiceGatewayInfo != null) targetService.setSvcNodeName(tblServiceGatewayInfo.getName());
			}
		});
	}

	public List<TargetServiceTestResult> testServicePort(String servicePortId)
	{
		List<TargetServiceTestResult> servicePortTestResult = new ArrayList<>();
		List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(servicePortId);

		if (CollectionUtils.isNotEmpty(tblServicePortTargetServiceInfos))
		{
			tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
				TargetServiceTestResult targetServiceTestResult = TargetServiceTestResult.of(tblServicePortTargetServiceInfo);

				targetServiceTestResult.setServiceGatewayId(tblServicePortTargetServiceInfo.getSvcNode());

				if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.READY.getCode())
				{
					try
					{
						HttpHeaders requestHeaders = new HttpHeaders();
						requestHeaders.set("Content-Type", "application/json");
						requestHeaders.set("LnjoyingNetworkTest", "true");
						HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
						ResponseEntity<String> response = REST_TEMPLATE.exchange(String.format("http://%s:%s/", tblServicePortTargetServiceInfo.getSvcIp(), tblServicePortTargetServiceInfo.getSvcPort()), HttpMethod.GET, requestEntity, String.class);
						if (response != null && response.getStatusCode() == HttpStatus.OK)
						{
							String result = response.getBody();
							if (result == null)
							{
								targetServiceTestResult.setNetworkStatus(1);
							}
							else if (result.contains("Network connect success"))
							{
								targetServiceTestResult.setNetworkStatus(0);
							}
							else if (result.contains("Network connect error"))
							{
								targetServiceTestResult.setNetworkStatus(3);
							}
						}
						else
						{
							targetServiceTestResult.setNetworkStatus(1);
						}
					}
					catch (Exception e)
					{
						targetServiceTestResult.setNetworkStatus(1);
					}
				}

				if (StringUtils.isNotEmpty(targetServiceTestResult.getTargetSvcNode()))
				{
					TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetServiceTestResult.getTargetSvcNode());
					if (tblServiceGatewayInfo != null) targetServiceTestResult.setTargetSvcNodeName(tblServiceGatewayInfo.getName());
				}
				if (StringUtils.isNotEmpty(targetServiceTestResult.getSvcNode()))
				{
					TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetServiceTestResult.getSvcNode());
					if (tblServiceGatewayInfo != null) targetServiceTestResult.setSvcNodeName(tblServiceGatewayInfo.getName());
				}

				servicePortTestResult.add(targetServiceTestResult);
			});
		}
		return servicePortTestResult;
	}

	public List<TargetServiceTestResult> testTargetServicePort(String servicePortId, String targetServiceId)
	{
		List<TargetServiceTestResult> servicePortTestResult = new ArrayList<>();
		TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(targetServiceId);

		if (tblServicePortTargetServiceInfo != null)
		{
			TargetServiceTestResult targetServiceTestResult = TargetServiceTestResult.of(tblServicePortTargetServiceInfo);

			targetServiceTestResult.setServiceGatewayId(tblServicePortTargetServiceInfo.getSvcNode());

			if (tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.READY.getCode())
			{
				try
				{
					HttpHeaders requestHeaders = new HttpHeaders();
					requestHeaders.set("Content-Type", "application/json");
					requestHeaders.set("LnjoyingNetworkTest", "true");
					HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
					ResponseEntity<String> response = REST_TEMPLATE.exchange(String.format("http://%s:%s/", tblServicePortTargetServiceInfo.getSvcIp(), tblServicePortTargetServiceInfo.getSvcPort()), HttpMethod.GET, requestEntity, String.class);
					if (response != null && response.getStatusCode() == HttpStatus.OK)
					{
						String result = response.getBody();
						if (result == null)
						{
							targetServiceTestResult.setNetworkStatus(ServicePortNetwortStatus.SERVICE_GATEWAY_UNREACHABLE.getCode());
						}
						else if (result.contains("Network connect success"))
						{
							targetServiceTestResult.setNetworkStatus(ServicePortNetwortStatus.CONNECT_SUCCESS.getCode());
						}
						else if (result.contains("Network connect error"))
						{
							targetServiceTestResult.setNetworkStatus(ServicePortNetwortStatus.TARGET_SERVICE_UNREACHABLE.getCode());
						}
					}
					else
					{
						targetServiceTestResult.setNetworkStatus(ServicePortNetwortStatus.SERVICE_GATEWAY_UNREACHABLE.getCode());
					}
				}
				catch (Exception e)
				{
					targetServiceTestResult.setNetworkStatus(ServicePortNetwortStatus.SERVICE_GATEWAY_UNREACHABLE.getCode());
				}
			}

			if (StringUtils.isNotEmpty(targetServiceTestResult.getTargetSvcNode()))
			{
				TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetServiceTestResult.getTargetSvcNode());
				if (tblServiceGatewayInfo != null) targetServiceTestResult.setTargetSvcNodeName(tblServiceGatewayInfo.getName());
			}
			if (StringUtils.isNotEmpty(targetServiceTestResult.getSvcNode()))
			{
				TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(targetServiceTestResult.getSvcNode());
				if (tblServiceGatewayInfo != null) targetServiceTestResult.setSvcNodeName(tblServiceGatewayInfo.getName());
			}

			servicePortTestResult.add(targetServiceTestResult);
		}
		return servicePortTestResult;
	}

	public void addServicePortCallback(String servicePortId)
	{
		try
		{
			TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);
			if (tblServicePortInfo == null || tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode()
					|| tblServicePortInfo.getStatus() == ServicePortStatus.REMOVING.getCode())
			{
				return;
			}
			List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(servicePortId);
			if (CollectionUtils.isEmpty(tblServicePortTargetServiceInfos))
			{
				return;
			}
			List<String> tags = gson.fromJson(tblServicePortInfo.getTags(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType());

			String microServiceName = null;
			String reqId = null;
			for (String tag : tags)
			{
				if (tag.contains("microServiceName:"))
				{
					microServiceName = tag.substring(tag.indexOf(":") + 1);
				}
				if (tag.contains("reqId:"))
				{
					reqId = tag.substring(tag.indexOf(":") + 1);
				}
			}

			if (StringUtils.isEmpty(microServiceName) || StringUtils.isEmpty(reqId))
			{
				return;
			}

			RpcPortMappingResult rpcPortMappingResult = new RpcPortMappingResult();
			rpcPortMappingResult.setReqId(reqId);
			rpcPortMappingResult.setServicePortId(tblServicePortInfo.getId());
			for (TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo : tblServicePortTargetServiceInfos)
			{
				if (StringUtils.isNotEmpty(tblServicePortTargetServiceInfo.getSvcIp()) && tblServicePortTargetServiceInfo.getSvcPort() != null)
				{
					RpcPortMappingResult.TargetService targetService = new RpcPortMappingResult.TargetService();
					targetService.setTargetServiceId(tblServicePortTargetServiceInfo.getId());
					targetService.setProtocol(tblServicePortTargetServiceInfo.getProtocol());
					targetService.setService(tblServicePortTargetServiceInfo.getService());
					targetService.setTargetIp(tblServicePortTargetServiceInfo.getTargetIp());
					targetService.setTargetPort(tblServicePortTargetServiceInfo.getTargetPort());
					targetService.setEndpoint(String.format("%s:%s", tblServicePortTargetServiceInfo.getSvcIp(), tblServicePortTargetServiceInfo.getSvcPort()));
					rpcPortMappingResult.getTargetServices().add(targetService);
				}
				else
				{
					continue;
				}
			}
			if (rpcPortMappingResult.getTargetServices().size() != 0) combRpcService.getServicePortCallback(microServiceName).addServicePortCallback(rpcPortMappingResult);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("add service port callback error. {}. for {}", e.getMessage(), servicePortId);
		}
	}

	public GetServicePortRsp getTargetService(String servicePortId, String targetServiceId)
	{
		GetServicePortRsp getServicePortRsp = new GetServicePortRsp();

		TblServicePortInfo tblServicePortInfo = servicePortRepository.getServicePort(servicePortId);

		if (tblServicePortInfo == null || tblServicePortInfo.getStatus() == ServicePortStatus.REMOVED.getCode())
		{
			throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
		}

		ServicePort servicePort = ServicePort.of(tblServicePortInfo);
		TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo = servicePortRepository.getServicePortTargetService(targetServiceId);
		if (tblServicePortTargetServiceInfo == null || tblServicePortTargetServiceInfo.getStatus() == TargetServiceStatus.REMOVED.getCode())
		{
			throw new WebSystemException(ErrorCode.SERVICE_PORT_NOT_EXIST, ErrorLevel.ERROR);
		}
		List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = new ArrayList<>();
		tblServicePortTargetServiceInfos.add(tblServicePortTargetServiceInfo);
		servicePort.setPortMap(tblServicePortTargetServiceInfos);
		assembleName(servicePort);

		getServicePortRsp.setServicePort(servicePort);
		return getServicePortRsp;
	}
}
