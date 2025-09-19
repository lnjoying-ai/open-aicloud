package com.lnjoying.justice.servicemanager.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.entity.servicemanager.PortStatus;
import com.lnjoying.justice.schema.entity.servicemanager.RpcCreatePortReq;
import com.lnjoying.justice.schema.entity.servicemanager.TargetPort;
import com.lnjoying.justice.servicemanager.common.*;
import com.lnjoying.justice.servicemanager.config.ServiceManagerConfig;
import com.lnjoying.justice.servicemanager.db.model.*;
import com.lnjoying.justice.servicemanager.db.repo.ServiceGatewayRepository;
import com.lnjoying.justice.servicemanager.domain.dto.request.UpdateServiceGatewayReq;
import com.lnjoying.justice.servicemanager.domain.dto.response.GetServiceGatewaysRsp;
import com.lnjoying.justice.servicemanager.domain.model.AllocPortResult;
import com.lnjoying.justice.servicemanager.domain.model.ServiceGateway;
import com.lnjoying.justice.servicemanager.domain.model.ServiceGatewayPortRange;
import com.lnjoying.justice.servicemanager.domain.model.servicecomb.Instance;
import com.lnjoying.justice.servicemanager.domain.model.servicecomb.ServiceCombService;
import com.lnjoying.justice.servicemanager.entity.search.ServiceGatewaySearchCritical;
import com.lnjoying.justice.servicemanager.utils.CustomErrorHandler;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceGatewayService
{
	private static Logger LOGGER = LogManager.getLogger();

	private static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder().build();

	@Autowired
	private ServiceGatewayRepository serviceGatewayRepository;

	@Autowired
	private ServiceManagerConfig serviceManagerConfig;

	private static Map<String, ServiceGateway> serviceGatewayMap = new HashMap<>();

	public GetServiceGatewaysRsp getServiceGateways(ServiceGatewaySearchCritical serviceGatewaySearchCritical)
	{
		try
		{
			GetServiceGatewaysRsp getServiceGatewaysRsp = new GetServiceGatewaysRsp();

			Map<String, Instance> serviceGatewayInstanceMap = getServiceGatewaysFromServiceComb();

			TblServiceGatewayInfoExample example = new TblServiceGatewayInfoExample();
			TblServiceGatewayInfoExample.Criteria criteria = example.createCriteria();
			if (StringUtils.isNotEmpty(serviceGatewaySearchCritical.getServiceGatewayId())) criteria.andIdEqualTo(serviceGatewaySearchCritical.getServiceGatewayId());
			if (StringUtils.isNotEmpty(serviceGatewaySearchCritical.getEndpoint())) criteria.andEndpointLike("%" + serviceGatewaySearchCritical.getServiceGatewayId() + "%");
			example.setOrderByClause("create_time desc");

			PageHelper.startPage(serviceGatewaySearchCritical.getPageNum(), serviceGatewaySearchCritical.getPageSize());
			List<TblServiceGatewayInfo> tblServiceGatewayInfos = serviceGatewayRepository.getServiceGatewaysByExample(example);
			PageInfo<TblServiceGatewayInfo> pageInfo = new PageInfo<>(tblServiceGatewayInfos);
			List<ServiceGateway> serviceGateways = tblServiceGatewayInfos.stream().map(tblServiceGatewayInfo -> {
				ServiceGateway serviceGateway = ServiceGateway.of(tblServiceGatewayInfo);
				serviceGateway.setStatusByServiceComb(serviceGatewayInstanceMap);
				return serviceGateway;
			}).collect(Collectors.toList());

			getServiceGatewaysRsp.setTotalNum(pageInfo.getTotal());
			getServiceGatewaysRsp.setServiceGateways(serviceGateways);

			return getServiceGatewaysRsp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service gateways error. {}.", e.getMessage());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public GetServiceGatewaysRsp getServiceGatewaysWithDetails(ServiceGatewaySearchCritical serviceGatewaySearchCritical)
	{
		try
		{
			GetServiceGatewaysRsp getServiceGatewaysWithDetailsRsp = new GetServiceGatewaysRsp();

			Map<String, Instance> serviceGatewayInstanceMap = getServiceGatewaysFromServiceComb();

			TblServiceGatewayInfoExample example = new TblServiceGatewayInfoExample();
			TblServiceGatewayInfoExample.Criteria criteria = example.createCriteria();
			if (StringUtils.isNotEmpty(serviceGatewaySearchCritical.getServiceGatewayId())) criteria.andIdEqualTo(serviceGatewaySearchCritical.getServiceGatewayId());
			if (StringUtils.isNotEmpty(serviceGatewaySearchCritical.getEndpoint())) criteria.andEndpointLike("%" + serviceGatewaySearchCritical.getServiceGatewayId() + "%");
			example.setOrderByClause("create_time desc");

			PageHelper.startPage(serviceGatewaySearchCritical.getPageNum(), serviceGatewaySearchCritical.getPageSize());
			List<TblServiceGatewayInfo> tblServiceGatewayInfos = serviceGatewayRepository.getServiceGatewaysByExample(example);
			PageInfo<TblServiceGatewayInfo> pageInfo = new PageInfo<>(tblServiceGatewayInfos);
			List<ServiceGateway> serviceGateways = tblServiceGatewayInfos.stream().map(tblServiceGatewayInfo -> {
				ServiceGateway serviceGateway = ServiceGateway.of(tblServiceGatewayInfo);
				serviceGateway.setPortRanges(serviceGatewayRepository.getServiceGatewayPortsByServiceGateway(tblServiceGatewayInfo.getId()));
				serviceGateway.setStatusByServiceComb(serviceGatewayInstanceMap);
				return serviceGateway;
			}).collect(Collectors.toList());

			getServiceGatewaysWithDetailsRsp.setTotalNum(pageInfo.getTotal());
			getServiceGatewaysWithDetailsRsp.setServiceGateways(serviceGateways);

			return getServiceGatewaysWithDetailsRsp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service gateways with details error. {}.", e.getMessage());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public ServiceGateway getServiceGateway(String serviceGatewayId)
	{
		try
		{
			Map<String, Instance> serviceGatewayInstanceMap = getServiceGatewaysFromServiceComb();
			TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(serviceGatewayId);
			ServiceGateway serviceGateway = ServiceGateway.of(tblServiceGatewayInfo);
			serviceGateway.setPortRanges(serviceGatewayRepository.getServiceGatewayPortsByServiceGateway(tblServiceGatewayInfo.getId()));
			serviceGateway.setStatusByServiceComb(serviceGatewayInstanceMap);

			return serviceGateway;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service gateways with details error. {}.", e.getMessage());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void updateServiceGateway(String serviceGatewayId, UpdateServiceGatewayReq updateServiceGatewayReq)
	{
		try
		{
			TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(serviceGatewayId);
			if (tblServiceGatewayInfo == null)
			{
				throw new WebSystemException(ErrorCode.SERVICE_GATEWAY_NOT_EXIST, ErrorLevel.ERROR);
			}

			checkPortRanges(updateServiceGatewayReq.getPortRanges());

			tblServiceGatewayInfo.setName(updateServiceGatewayReq.getName());
			tblServiceGatewayInfo.setPurpose(updateServiceGatewayReq.getPurpose());
			tblServiceGatewayInfo.setDescription(updateServiceGatewayReq.getDescription());
			serviceGatewayRepository.updateServiceGateway(tblServiceGatewayInfo);

			List<TblServiceGatewayPortInfo> tblServiceGatewayPortInfos = serviceGatewayRepository.getServiceGatewayPortsByServiceGateway(serviceGatewayId);

			List<TblServiceGatewayPortInfo> newTblServiceGatewayPortInfos = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(updateServiceGatewayReq.getPortRanges()))
			{
				updateServiceGatewayReq.getPortRanges().forEach(serviceGatewayPortRange ->
				{
					TblServiceGatewayPortInfo tblServiceGatewayPortInfo = new TblServiceGatewayPortInfo();
					tblServiceGatewayPortInfo.setInternalIp(serviceGatewayPortRange.getInternalIp());
					tblServiceGatewayPortInfo.setExternalIp(serviceGatewayPortRange.getExternalIp());
					tblServiceGatewayPortInfo.setListenPortRangeMin(serviceGatewayPortRange.getListenPortRangeMin());
					tblServiceGatewayPortInfo.setListenPortRangeMax(serviceGatewayPortRange.getListenPortRangeMax());
					tblServiceGatewayPortInfo.setPortRangeMin(serviceGatewayPortRange.getPortRangeMin());
					tblServiceGatewayPortInfo.setPortRangeMax(serviceGatewayPortRange.getPortRangeMax());
					tblServiceGatewayPortInfo.setDescription(serviceGatewayPortRange.getDescription());
					newTblServiceGatewayPortInfos.add(tblServiceGatewayPortInfo);
				});
			}
			Set<TblServiceGatewayPortInfo> newTblServiceGatewayPortInfoSet = new HashSet<>(newTblServiceGatewayPortInfos);

			for (TblServiceGatewayPortInfo tblServiceGatewayPortInfo : tblServiceGatewayPortInfos)
			{
				boolean contain = false;
				for (TblServiceGatewayPortInfo newTblServiceGatewayPortInfo : newTblServiceGatewayPortInfoSet)
				{
					if (Objects.equals(newTblServiceGatewayPortInfo.getInternalIp(), tblServiceGatewayPortInfo.getInternalIp()) &&
							Objects.equals(newTblServiceGatewayPortInfo.getListenPortRangeMin(), tblServiceGatewayPortInfo.getListenPortRangeMin()) &&
							Objects.equals(newTblServiceGatewayPortInfo.getListenPortRangeMax(), tblServiceGatewayPortInfo.getListenPortRangeMax()))
					{
						contain = true;
						newTblServiceGatewayPortInfoSet.remove(newTblServiceGatewayPortInfo);

						tblServiceGatewayPortInfo.setExternalIp(newTblServiceGatewayPortInfo.getExternalIp());
						tblServiceGatewayPortInfo.setPortRangeMin(newTblServiceGatewayPortInfo.getPortRangeMin());
						tblServiceGatewayPortInfo.setPortRangeMax(newTblServiceGatewayPortInfo.getPortRangeMax());
						tblServiceGatewayPortInfo.setDescription(newTblServiceGatewayPortInfo.getDescription());
						tblServiceGatewayPortInfo.setUpdateTime(new Date());
						serviceGatewayRepository.updateServiceGatewayPort(tblServiceGatewayPortInfo);
						break;
					}
				}

				if (!contain)
				{
					tblServiceGatewayPortInfo.setStatus(CommonPhaseStatus.REMOVED);
					tblServiceGatewayPortInfo.setUpdateTime(new Date());
					serviceGatewayRepository.updateServiceGatewayPort(tblServiceGatewayPortInfo);
				}
			}

			newTblServiceGatewayPortInfoSet.forEach(tblServiceGatewayPortInfo -> {
				tblServiceGatewayPortInfo.setServiceGatewayId(serviceGatewayId);
				tblServiceGatewayPortInfo.setId(Utils.buildStr("JST_SGW_PR_", Utils.assignUUId()));
				tblServiceGatewayPortInfo.setTotal(tblServiceGatewayPortInfo.getPortRangeMax() - tblServiceGatewayPortInfo.getPortRangeMin() + 1);
				tblServiceGatewayPortInfo.setStatus(CommonPhaseStatus.DEFAULT);

				serviceGatewayRepository.insertServiceGatewayPort(tblServiceGatewayPortInfo);
			});

			refreshServiceGateway(serviceGatewayId);
		}
		catch (WebSystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("update service gateway error. {}. for {}", e.getMessage(), serviceGatewayId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	private Map<String, Instance> getServiceGatewaysFromServiceComb()
	{
		Map<String, Instance> serviceGatewayMap = null;
		try
		{
			String serviceGatewayJson = RedisUtil.get(RedisCache.SERVICEGW_MAP);

			if (StringUtils.isEmpty(serviceGatewayJson))
			{
				String url = serviceManagerConfig.getServicecombUrl().trim() + "/v4/default/govern/microservices?options=instances&withShared=true&serviceName=servicegw";
				HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
				factory.setConnectTimeout(60 * 1000);
				factory.setReadTimeout(5 * 60 * 1000);
				RestTemplate restTemplate = new RestTemplate(factory);
				ResponseEntity<String> response = null;
				try {
					HttpHeaders requestHeaders = new HttpHeaders();
					requestHeaders.set("Content-Type", "application/json");
					requestHeaders.set("Connection", "false");
					HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
					restTemplate.setErrorHandler(new CustomErrorHandler());
					response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
				}
				catch (Exception e)
				{
					return null;
				}

				if (response != null && response.getStatusCode() == HttpStatus.OK && response.getBody() != null)
				{
					ServiceCombService serviceCombService = JsonUtils.fromJson(response.getBody(), ServiceCombService.class);
					serviceGatewayMap = new HashMap<>();
					if (serviceCombService != null && CollectionUtils.isNotEmpty(serviceCombService.getAllServicesDetail())
							&& CollectionUtils.isNotEmpty(serviceCombService.getAllServicesDetail().get(0).getInstances()))
					{
						for (Instance instance : serviceCombService.getAllServicesDetail().get(0).getInstances())
						{
							serviceGatewayMap.put(instance.getEndpoints().get(0), instance);
						}
						RedisUtil.set(RedisCache.SERVICEGW_MAP, JsonUtils.toJson(serviceGatewayMap), 60 * 60);
					}
				}
			}
			else
			{
				serviceGatewayMap = JsonUtils.fromJson(serviceGatewayJson, new TypeToken<Map<String, Instance>>(){}.getType());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get service gateway from servicecomb error. {}", e.getMessage());
		}
		return serviceGatewayMap;
	}

	public AllocPortResult allocPort(TblServicePortInfo tblServicePortInfo, TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
	{
		AllocPortResult result = null;

		result = new AllocPortResult(null, null, null, null, null, TargetServiceStatus.NO_RESOURCE.getCode());

		List<ServiceGateway> satisfiedServiceGateways = new ArrayList<>();

		if (StringUtils.isNotEmpty(tblServicePortTargetServiceInfo.getTargetSvcNode()))
		{
			ServiceGateway serviceGateway = getServiceGatewayMap().get(tblServicePortTargetServiceInfo.getTargetSvcNode());
			if (serviceGateway != null) satisfiedServiceGateways.add(serviceGateway);
		}
		else
		{
			if (StringUtils.isNotEmpty(tblServicePortInfo.getPurpose()))
			{
				List<ServiceGateway> singlePurposeServiceGateways = new ArrayList<>();
				List<ServiceGateway> multiPurposeServiceGateways = new ArrayList<>();
				List<ServiceGateway> noPurposeServiceGateways = new ArrayList<>();
				getServiceGatewayMap().forEach((k,v) -> {
					if (StringUtils.isEmpty(v.getPurpose()))
					{
						noPurposeServiceGateways.add(v);
					}
					else if (v.getPurpose().equalsIgnoreCase(tblServicePortInfo.getPurpose()))
					{
						singlePurposeServiceGateways.add(v);
					}
					else if (v.getPurpose().toLowerCase(Locale.ROOT).startsWith(tblServicePortInfo.getPurpose().toLowerCase(Locale.ROOT) + ",")
							|| v.getPurpose().toLowerCase(Locale.ROOT).endsWith("," + tblServicePortInfo.getPurpose().toLowerCase(Locale.ROOT))
							|| v.getPurpose().toLowerCase(Locale.ROOT).contains("," + tblServicePortInfo.getPurpose().toLowerCase(Locale.ROOT) + ","))
					{
						multiPurposeServiceGateways.add(v);
					}
				});

				satisfiedServiceGateways.addAll(singlePurposeServiceGateways);
				satisfiedServiceGateways.addAll(multiPurposeServiceGateways);
				satisfiedServiceGateways.addAll(noPurposeServiceGateways);
			}
			else
			{
				getServiceGatewayMap().forEach((k,v) -> {
					satisfiedServiceGateways.add(v);
				});
			}
		}

		if (CollectionUtils.isEmpty(satisfiedServiceGateways))
		{
			return result;
		}

		Map<String, Instance> serviceGatewayInstanceMap = getServiceGatewaysFromServiceComb();

		if (tblServicePortTargetServiceInfo.getTargetSvcPort() != null)
		{
			for (ServiceGateway serviceGateway : satisfiedServiceGateways)
			{
				Instance instance = serviceGatewayInstanceMap.get(serviceGateway.getEndpoint());
				if (! instance.getStatus().equals(ServiceGatewayStatus.UP.name()))
				{
					continue;
				}

				for (ServiceGatewayPortRange serviceGatewayPortRange : serviceGateway.getPortRanges())
				{
					if (serviceGatewayPortRange.getPortRangeMin() <= tblServicePortTargetServiceInfo.getTargetSvcPort() &&
							serviceGatewayPortRange.getPortRangeMax() >= tblServicePortTargetServiceInfo.getTargetSvcPort())
					{
						int listenPort = tblServicePortTargetServiceInfo.getTargetSvcPort() + serviceGatewayPortRange.getListenPortRangeMin() - serviceGatewayPortRange.getPortRangeMin();
						if (serviceGatewayPortRange.getAvailablePorts().contains(listenPort))
						{
							serviceGatewayPortRange.getAvailablePorts().remove(tblServicePortTargetServiceInfo.getTargetSvcPort());

							TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation = insertPortAllocation(serviceGatewayPortRange.getPortRangeId(),
									serviceGatewayPortRange.getInternalIp(), serviceGatewayPortRange.getExternalIp(), tblServicePortTargetServiceInfo.getTargetSvcPort(),
									listenPort, tblServicePortTargetServiceInfo.getId());

							RpcCreatePortReq rpcCreatePortReq = genRpcCreatePortReq(tblServicePortInfo, tblServicePortTargetServiceInfo, tblServiceGatewayPortAllocation);

							Integer status = TargetServiceStatus.CREATE_FILED.getCode();
							try
							{
								HttpHeaders requestHeaders = new HttpHeaders();
								requestHeaders.set("Content-Type", "application/json");
								HttpEntity<RpcCreatePortReq> requestEntity = new HttpEntity<RpcCreatePortReq>(rpcCreatePortReq, requestHeaders);
								Integer rpcResult = REST_TEMPLATE.postForObject(serviceGateway.getEndpoint().replace("rest", "http") + "/ServiceGatewayServiceImpl/createPort", requestEntity, Integer.class);
								if (rpcResult != null && rpcResult == ErrorCode.SUCCESS.getCode()) status = TargetServiceStatus.READY.getCode();
							}
							catch (Exception e)
							{
								e.printStackTrace();
								LOGGER.error("rpc create port error, {}", e.getMessage());
							}
							if (status == TargetServiceStatus.CREATE_FILED.getCode())
							{
								tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
								tblServiceGatewayPortAllocation.setUpdateTime(new Date());
								serviceGatewayRepository.updateServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
								if (tblServicePortTargetServiceInfo.getTargetSvcPort() != null)
								{
									serviceGatewayPortRange.getAvailablePorts().add(tblServicePortTargetServiceInfo.getTargetSvcPort());
								}
							}
							result.setStatus(status);
							result.setPortAllocationId(tblServiceGatewayPortAllocation.getId());
							result.setExternalIp(serviceGatewayPortRange.getExternalIp());
							result.setInternalIp(serviceGatewayPortRange.getInternalIp());
							if (result.getInternalIp() == null)
							{
								String endpoint = serviceGateway.getEndpoint().replace("rest://", "");
								result.setInternalIp(endpoint.substring(0, endpoint.indexOf(":")));
							}
							result.setSvcPort(tblServicePortTargetServiceInfo.getTargetSvcPort());
							result.setSvcNode(serviceGateway.getServiceGatewayId());
							return result;
						}
					}
				}
			}
		}
		else
		{
			for (ServiceGateway serviceGateway : satisfiedServiceGateways)
			{
				Instance instance = serviceGatewayInstanceMap.get(serviceGateway.getEndpoint());
				if (! instance.getStatus().equals(ServiceGatewayStatus.UP.name()))
				{
					continue;
				}

				for (ServiceGatewayPortRange serviceGatewayPortRange : serviceGateway.getPortRanges())
				{
					if (serviceGatewayPortRange.getAvailablePorts().size() > 0)
					{
						for (Integer listenPort : serviceGatewayPortRange.getAvailablePorts())
						{
							serviceGatewayPortRange.getAvailablePorts().remove(listenPort);

							Integer port = listenPort - serviceGatewayPortRange.getListenPortRangeMin() + serviceGatewayPortRange.getPortRangeMin();

							TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation = insertPortAllocation(serviceGatewayPortRange.getPortRangeId(),
									serviceGatewayPortRange.getInternalIp(), serviceGatewayPortRange.getExternalIp(), port, listenPort, tblServicePortTargetServiceInfo.getId());

							RpcCreatePortReq rpcCreatePortReq = genRpcCreatePortReq(tblServicePortInfo, tblServicePortTargetServiceInfo, tblServiceGatewayPortAllocation);

							Integer status = TargetServiceStatus.CREATE_FILED.getCode();
							try
							{
								HttpHeaders requestHeaders = new HttpHeaders();
								requestHeaders.set("Content-Type", "application/json");
								HttpEntity<RpcCreatePortReq> requestEntity = new HttpEntity<RpcCreatePortReq>(rpcCreatePortReq, requestHeaders);
								Integer rpcResult = REST_TEMPLATE.postForObject(serviceGateway.getEndpoint().replace("rest", "http") + "/ServiceGatewayServiceImpl/createPort", requestEntity, Integer.class);
								if (rpcResult != null && rpcResult == ErrorCode.SUCCESS.getCode()) status = TargetServiceStatus.READY.getCode();
							}
							catch (Exception e)
							{
								e.printStackTrace();
								LOGGER.error("rpc create port error, {}", e.getMessage());
							}
							if (status == TargetServiceStatus.CREATE_FILED.getCode())
							{
								tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
								tblServiceGatewayPortAllocation.setUpdateTime(new Date());
								serviceGatewayRepository.updateServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
								if (tblServicePortTargetServiceInfo.getTargetSvcPort() != null)
								{
									serviceGatewayPortRange.getAvailablePorts().add(tblServicePortTargetServiceInfo.getTargetSvcPort());
								}
							}
							result.setStatus(status);
							result.setPortAllocationId(tblServiceGatewayPortAllocation.getId());
							result.setExternalIp(serviceGatewayPortRange.getExternalIp());
							result.setInternalIp(serviceGatewayPortRange.getInternalIp());
							if (result.getInternalIp() == null)
							{
								String endpoint = serviceGateway.getEndpoint().replace("rest://", "");
								result.setInternalIp(endpoint.substring(0, endpoint.indexOf(":")));
							}
							result.setSvcPort(port);
							result.setSvcNode(serviceGateway.getServiceGatewayId());
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	public void releasePort(TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo)
	{
		if (StringUtils.isEmpty(tblServicePortTargetServiceInfo.getSvcIp()) || tblServicePortTargetServiceInfo.getSvcPort() == null)
		{
			return;
		}

		TblServiceGatewayPortAllocationExample example = new TblServiceGatewayPortAllocationExample();
		TblServiceGatewayPortAllocationExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(PortAllocationStatus.RELEASED.getCode());
		criteria.andServicePortTargetServiceIdEqualTo(tblServicePortTargetServiceInfo.getId());

		List<TblServiceGatewayPortAllocation> tblServiceGatewayPortAllocations = serviceGatewayRepository.getServiceGatewayPortAllocationsByExample(example);

		if (CollectionUtils.isNotEmpty(tblServiceGatewayPortAllocations))
		{
			tblServiceGatewayPortAllocations.forEach(tblServiceGatewayPortAllocation -> {
				TblServiceGatewayPortInfo tblServiceGatewayPortInfo = serviceGatewayRepository.getServiceGatewayPort(tblServiceGatewayPortAllocation.getPortRangeId());
				ServiceGateway serviceGateway = getServiceGatewayMap().get(tblServiceGatewayPortInfo.getServiceGatewayId());
				try
				{
					String reqBody = String.format("{\"targetPortId\":\"%s\",\"internalIp\":\"%s\",\"listenPort\":%s}}", tblServiceGatewayPortAllocation.getServicePortTargetServiceId(),
							tblServiceGatewayPortAllocation.getInternalIp(), tblServiceGatewayPortAllocation.getListenPort());
					HttpHeaders requestHeaders = new HttpHeaders();
					requestHeaders.set("Content-Type", "application/json");
					HttpEntity<String> requestEntity = new HttpEntity<String>(reqBody, requestHeaders);
					Integer rpcResult = REST_TEMPLATE.postForObject(serviceGateway.getEndpoint().replace("rest", "http") + "/ServiceGatewayServiceImpl/releasePort", requestEntity, Integer.class);
					if (rpcResult != null && rpcResult == ErrorCode.SUCCESS.getCode()) tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
				}
				catch (Exception e)
				{
					tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.RELEASED.getCode());
					e.printStackTrace();
					LOGGER.error("release port error, {}", e.getMessage());
				}
				tblServiceGatewayPortAllocation.setUpdateTime(new Date());
				serviceGatewayRepository.updateServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
				refreshServiceGateway(tblServiceGatewayPortInfo.getServiceGatewayId());
			});
		}
	}

	public Map<String, ServiceGateway> getServiceGatewayMap()
	{
		if (serviceGatewayMap.keySet().size() == 0)
		{
			initServiceGatewayMap();
		}

		return serviceGatewayMap;
	}

	public void initServiceGatewayMap()
	{
		TblServiceGatewayInfoExample example = new TblServiceGatewayInfoExample();
		TblServiceGatewayInfoExample.Criteria criteria = example.createCriteria();
		criteria.andStatusNotEqualTo(ServiceGatewayStatus.REMOVED.getCode());
		List<TblServiceGatewayInfo> tblServiceGatewayInfos = serviceGatewayRepository.getServiceGatewaysByExample(example);
		tblServiceGatewayInfos.forEach(tblServiceGatewayInfo -> {
			ServiceGateway serviceGateway = ServiceGateway.of(tblServiceGatewayInfo);
			serviceGateway.setPortRanges(serviceGatewayRepository.getServiceGatewayPortsByServiceGateway(tblServiceGatewayInfo.getId()));
			serviceGateway.getPortRanges().forEach(serviceGatewayPortRange -> {
				TblServiceGatewayPortAllocationExample allocationExample = new TblServiceGatewayPortAllocationExample();
				TblServiceGatewayPortAllocationExample.Criteria allocationCriteria = allocationExample.createCriteria();
				allocationCriteria.andStatusNotEqualTo(PortAllocationStatus.RELEASED.getCode());
				allocationCriteria.andPortBetween(serviceGatewayPortRange.getListenPortRangeMin(), serviceGatewayPortRange.getListenPortRangeMax());
				if (serviceGatewayPortRange.getInternalIp() == null)
				{
					allocationCriteria.andServiceGatewayIdEqualTo(tblServiceGatewayInfo.getId());
					serviceGatewayPortRange.setAvailablePorts(serviceGatewayRepository.getServiceGatewayPortAllocationsByExampleLeftJoinPortRange(allocationExample));
				}
				else
				{
					allocationCriteria.andInternalIpEqualToOrIsNull(serviceGatewayPortRange.getInternalIp());
					serviceGatewayPortRange.setAvailablePorts(serviceGatewayRepository.getServiceGatewayPortAllocationsByExample(allocationExample));
				}
			});
			serviceGatewayMap.put(serviceGateway.getServiceGatewayId(), serviceGateway);
		});
	}

	public void refreshServiceGateway(String serviceGatewayId)
	{
		TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(serviceGatewayId);
		ServiceGateway serviceGateway = ServiceGateway.of(tblServiceGatewayInfo);
		List<TblServiceGatewayPortInfo> tblServiceGatewayPortInfos = serviceGatewayRepository.getServiceGatewayPortsByServiceGateway(tblServiceGatewayInfo.getId());
		Map<String, TblServiceGatewayPortInfo> portInfoMap = tblServiceGatewayPortInfos.stream().collect(Collectors.toMap(TblServiceGatewayPortInfo::getId, (p) -> p));
		serviceGateway.setPortRanges(tblServiceGatewayPortInfos);
		serviceGateway.getPortRanges().forEach(serviceGatewayPortRange -> {
			TblServiceGatewayPortAllocationExample allocationExample = new TblServiceGatewayPortAllocationExample();
			TblServiceGatewayPortAllocationExample.Criteria allocationCriteria = allocationExample.createCriteria();
			allocationCriteria.andStatusNotEqualTo(PortAllocationStatus.RELEASED.getCode());
			allocationCriteria.andPortBetween(serviceGatewayPortRange.getPortRangeMin(), serviceGatewayPortRange.getPortRangeMax());
			if (serviceGatewayPortRange.getInternalIp() == null)
			{
				allocationCriteria.andServiceGatewayIdEqualTo(tblServiceGatewayInfo.getId());
				serviceGatewayPortRange.setAvailablePorts(serviceGatewayRepository.getServiceGatewayPortAllocationsByExampleLeftJoinPortRange(allocationExample));
			}
			else
			{
				allocationCriteria.andInternalIpEqualToOrIsNull(serviceGatewayPortRange.getInternalIp());
				serviceGatewayPortRange.setAvailablePorts(serviceGatewayRepository.getServiceGatewayPortAllocationsByExample(allocationExample));
			}

			TblServiceGatewayPortInfo tblServiceGatewayPortInfo = portInfoMap.get(serviceGatewayPortRange.getPortRangeId());
			if (serviceGatewayPortRange.getLeft() != tblServiceGatewayPortInfo.getLeft())
			{
				tblServiceGatewayPortInfo.setLeft(serviceGatewayPortRange.getLeft());
				serviceGatewayRepository.updateServiceGatewayPort(tblServiceGatewayPortInfo);
			}
		});
		serviceGatewayMap.put(serviceGateway.getServiceGatewayId(), serviceGateway);
	}

	public TblServiceGatewayPortAllocation insertPortAllocation(String portRangeId, String internalIp, String externalIp,
																Integer port, Integer listenPort, String targetServiceId)
	{
		TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation = new TblServiceGatewayPortAllocation();
		tblServiceGatewayPortAllocation.setId(Utils.buildStr("JST_SGW_P_", Utils.assignUUId()));
		tblServiceGatewayPortAllocation.setPortRangeId(portRangeId);
		tblServiceGatewayPortAllocation.setInternalIp(internalIp);
		tblServiceGatewayPortAllocation.setExternalIp(externalIp);
		tblServiceGatewayPortAllocation.setPort(port);
		tblServiceGatewayPortAllocation.setListenPort(listenPort);
		tblServiceGatewayPortAllocation.setServicePortTargetServiceId(targetServiceId);
		tblServiceGatewayPortAllocation.setStatus(PortAllocationStatus.CREATING.getCode());
		tblServiceGatewayPortAllocation.setCreateTime(new Date());
		tblServiceGatewayPortAllocation.setUpdateTime(tblServiceGatewayPortAllocation.getCreateTime());
		serviceGatewayRepository.insertServiceGatewayPortAllocation(tblServiceGatewayPortAllocation);
		return tblServiceGatewayPortAllocation;
	}

	public RpcCreatePortReq genRpcCreatePortReq(TblServicePortInfo tblServicePortInfo, TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo, TblServiceGatewayPortAllocation tblServiceGatewayPortAllocation)
	{
		RpcCreatePortReq rpcCreatePortReq = new RpcCreatePortReq();
		rpcCreatePortReq.setServicePortId(tblServicePortInfo.getId());
		rpcCreatePortReq.setTargetType(tblServicePortInfo.getTargetType());
		rpcCreatePortReq.setDeployment(tblServicePortInfo.getDeployment());

		rpcCreatePortReq.setTargetServiceId(tblServicePortTargetServiceInfo.getId());
		rpcCreatePortReq.setService(tblServicePortTargetServiceInfo.getService());

		rpcCreatePortReq.setTargetPort(tblServicePortTargetServiceInfo.getTargetPort());

		rpcCreatePortReq.setInternalIp(tblServiceGatewayPortAllocation.getInternalIp());
		rpcCreatePortReq.setExternalIp(tblServiceGatewayPortAllocation.getExternalIp());
		rpcCreatePortReq.setPort(tblServiceGatewayPortAllocation.getPort());
		rpcCreatePortReq.setListenPort(tblServiceGatewayPortAllocation.getListenPort());

		rpcCreatePortReq.setCert(tblServicePortTargetServiceInfo.getCert());

		rpcCreatePortReq.setProtocol(tblServicePortTargetServiceInfo.getProtocol());

		rpcCreatePortReq.setTargets(new ArrayList<>());

		switch (tblServicePortInfo.getTargetType())
		{
			case TargetType.CONTAINER:
			case TargetType.COMPOSE:
			{
				break;
			}
			case TargetType.K8S_CLUSTER:
			case TargetType.CLOUD:
			{
				rpcCreatePortReq.getTargets().add(new TargetPort(null, null, null, null,
						tblServicePortTargetServiceInfo.getTargetIp(), tblServicePortTargetServiceInfo.getTargetPort()));
				break;
			}
			case TargetType.EDGE_NODE:
			{
				rpcCreatePortReq.getTargets().add(new TargetPort(null, null, tblServicePortInfo.getDeployment(),
						null, null, tblServicePortTargetServiceInfo.getTargetPort()));
				break;
			}
			case TargetType.OTHER:
			{
				rpcCreatePortReq.getTargets().add(new TargetPort(null, tblServicePortTargetServiceInfo.getSite(), null,
						null, tblServicePortTargetServiceInfo.getTargetIp(), tblServicePortTargetServiceInfo.getTargetPort()));
				break;
			}
		}
		return rpcCreatePortReq;
	}

	private void checkPortRanges(List<ServiceGatewayPortRange> portRanges)
	{
		for (int i = 0; i < portRanges.size(); i++)
		{
			ServiceGatewayPortRange portRange = portRanges.get(i);

			if (portRange.getListenPortRangeMin() == null || portRange.getListenPortRangeMax() == null ||
					portRange.getPortRangeMin() == null || portRange.getPortRangeMax() == null ||
					portRange.getPortRangeMax() < portRange.getPortRangeMin() ||
					portRange.getListenPortRangeMax() < portRange.getListenPortRangeMin() ||
					(portRange.getListenPortRangeMax()-portRange.getListenPortRangeMin()) != (portRange.getPortRangeMax() - portRange.getPortRangeMin()))
			{
				throw new WebSystemException(ErrorCode.SERVICE_GATEWAY_PORT_RANGE_ERROR, ErrorLevel.INFO);
			}

			for (int j = 0; j < i; j++)
			{
				ServiceGatewayPortRange tempPortRange = portRanges.get(j);
				if ((tempPortRange.getExternalIp() == null && portRange.getExternalIp() == null) ||
						(tempPortRange.getExternalIp() != null && tempPortRange.getExternalIp().equals(portRange.getExternalIp())))
				{
					if (inRange(tempPortRange.getPortRangeMin(), tempPortRange.getPortRangeMax(), portRange.getPortRangeMin()) ||
							inRange(tempPortRange.getPortRangeMin(), tempPortRange.getPortRangeMax(), portRange.getPortRangeMax()))
					{
						throw new WebSystemException(ErrorCode.SERVICE_GATEWAY_PORT_RANGE_ERROR, ErrorLevel.INFO);
					}
				}

				if (tempPortRange.getInternalIp() == null || portRange.getInternalIp() == null || tempPortRange.getInternalIp().equals(portRange.getInternalIp()))
				{
					if (inRange(tempPortRange.getListenPortRangeMin(), tempPortRange.getListenPortRangeMax(), portRange.getListenPortRangeMin()) ||
							inRange(tempPortRange.getListenPortRangeMin(), tempPortRange.getListenPortRangeMax(), portRange.getListenPortRangeMax()))
					{
						throw new WebSystemException(ErrorCode.SERVICE_GATEWAY_PORT_RANGE_ERROR, ErrorLevel.INFO);
					}
				}
			}
		}
	}

	private boolean inRange(Integer rangeMin, Integer rangeMax, Integer position)
	{
		if (position == null || rangeMin == null || rangeMax == null) return false;
		return position >= rangeMin && position <= rangeMax;
	}

	public List<PortStatus> portStatus(String serviceGatewayId)
	{
		TblServiceGatewayInfo tblServiceGatewayInfo = serviceGatewayRepository.getServiceGateway(serviceGatewayId);
		try
		{
			List<PortStatus> rpcResult = REST_TEMPLATE.postForObject(tblServiceGatewayInfo.getEndpoint().replace("rest", "http") + "/ServiceGatewayServiceImpl/getPortStatus", null, List.class);
			if (rpcResult != null )
			return rpcResult;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("get port status error, {}", e.getMessage());
		}
		return null;
	}
}
