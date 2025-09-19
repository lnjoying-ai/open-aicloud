package com.lnjoying.justice.cmp.service.cloud;

import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.config.CloudManagerConfig;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.domain.dto.request.AddCloudReq;
import com.lnjoying.justice.cmp.domain.dto.request.UpdateCloudReq;
import com.lnjoying.justice.cmp.domain.dto.request.ActionCloudReq;
import com.lnjoying.justice.cmp.domain.dto.response.CloudBasicInfoListRsp;
import com.lnjoying.justice.cmp.domain.dto.response.CloudInfoListRsp;
import com.lnjoying.justice.cmp.domain.model.Authorization;
import com.lnjoying.justice.cmp.domain.model.CloudSearchCritical;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.domain.model.SyncResourceInfo;
import com.lnjoying.justice.cmp.service.process.CloudMsgProcessStrategy;
import com.lnjoying.justice.cmp.service.process.CloudProcessStrategy;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.service.syncdata.NSKSyncDataService;
import com.lnjoying.justice.cmp.utils.CustomErrorHandler;
import com.lnjoying.justice.cmp.utils.osclient.OSClientUtil;
import com.lnjoying.justice.cmp.utils.osclient.OSClientV3;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.vertx.core.http.HttpServerRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.foundation.vertx.http.VertxServerRequestToHttpServletRequest;
import org.apache.servicecomb.swagger.invocation.context.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.util.*;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;

@Service
public class CloudService
{
	private static Logger LOGGER = LogManager.getLogger();

	private static Map<String, HealthStatus> cloudHealthMap = new HashMap<>();

	@Autowired
	private CloudRepository cloudRepository;

	@Autowired
	private CloudMsgProcessStrategy cloudMsgProcessStrategy;

	@Autowired
	private CloudManagerConfig cloudManagerConfig;

	@Autowired
	private CloudProcessStrategy cloudProcessStrategy;

	@Autowired
	private ESKSyncDataService eskSyncDataService;

	@Autowired
	private NSKSyncDataService nskSyncDataService;

	public String addCloud(AddCloudReq addCloudReq, String bpId, String userId)
	{
		try
		{
			if (addCloudReq.getTargetNodes().size() == 0)
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
			}
			if (((addCloudReq.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) ||
					addCloudReq.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName())) &&
					(addCloudReq.getAuthorization() == null || addCloudReq.getAuthorization().getPassword() == null ||
							StringUtils.isEmpty(addCloudReq.getAuthorization().getPassword().getUserid()) ||
							StringUtils.isEmpty(addCloudReq.getAuthorization().getPassword().getPassword())))
					|| (addCloudReq.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()) &&
					(addCloudReq.getAuthorization() == null || addCloudReq.getAuthorization().getAccessKey() == null ||
							StringUtils.isEmpty(addCloudReq.getAuthorization().getAccessKey().getId()) ||
					StringUtils.isEmpty(addCloudReq.getAuthorization().getAccessKey().getSecret()))))
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
			}
			TargetNode targetNode = addCloudReq.getTargetNodes().get(0);
			CloudProduct cloudProduct = CloudProduct.fromName(addCloudReq.getProduct());
			TblCloudInfo tblCloudInfo = new TblCloudInfo();
			if (StringUtils.isNotEmpty(addCloudReq.getCloudId()))
			{
				if (addCloudReq.getCloudId().toLowerCase(Locale.ROOT).startsWith(cloudProduct.getShortName().toLowerCase(Locale.ROOT) + "-"))
				{
					tblCloudInfo.setCloudId(addCloudReq.getCloudId());
				}
				else
				{
					tblCloudInfo.setCloudId(cloudProduct.getShortName().toLowerCase(Locale.ROOT) + "-" + addCloudReq.getCloudId());
				}
			}
			else
			{
				tblCloudInfo.setCloudId(Utils.buildStr(cloudProduct.getShortName().toLowerCase(Locale.ROOT), "-", Utils.assignUUId()));
			}
			tblCloudInfo.setName(addCloudReq.getName());
			tblCloudInfo.setProduct(addCloudReq.getProduct());
			tblCloudInfo.setStatus(CloudStatus.CREATED.getCode());
			tblCloudInfo.setMode(addCloudReq.getMode());
			tblCloudInfo.setUrl(addCloudReq.getUrl());
			tblCloudInfo.setCertificate(addCloudReq.getCertificate());
			tblCloudInfo.setRegionId(targetNode.getDstRegionId());
			tblCloudInfo.setSiteId(targetNode.getDstSiteId());
			tblCloudInfo.setNodeId(targetNode.getDstNodeId());
			tblCloudInfo.setHealthCheck(JsonUtils.toJson(addCloudReq.getHealthCheck()));
			tblCloudInfo.setAuthorization(JsonUtils.toJson(addCloudReq.getAuthorization()));
			tblCloudInfo.setOsServiceEndpoints(JsonUtils.toJson(addCloudReq.getOsServiceEndpoints()));

			if (isAdmin() && (StringUtils.isNotEmpty(addCloudReq.getUserId()) || StringUtils.isNotEmpty(addCloudReq.getBpId())))
			{
				tblCloudInfo.setOwner(addCloudReq.getUserId());
				tblCloudInfo.setBp(addCloudReq.getBpId());
			}
			else
			{
				tblCloudInfo.setOwner(userId);
				tblCloudInfo.setBp(bpId);
			}

			tblCloudInfo.setLabels(JsonUtils.toJson(addCloudReq.getLabels()));
			tblCloudInfo.setCreateTime(new Date());
			tblCloudInfo.setUpdateTime(tblCloudInfo.getCreateTime());
			cloudRepository.insertCloud(tblCloudInfo);

			if (tblCloudInfo.getMode().equals(Mode.PROXY))
			{
				MessagePack messagePack = new MessagePack();
				messagePack.setMsgType(CloudMsg.IMPORT);
				messagePack.setMessageObj(tblCloudInfo.getCloudId());
				cloudMsgProcessStrategy.sendMessage(messagePack);
			}
			else if (tblCloudInfo.getMode().equals(Mode.DIRECT))
			{
				TblCloudInfo newTblCloudInfo = new TblCloudInfo();
				newTblCloudInfo.setCloudId(tblCloudInfo.getCloudId());
				newTblCloudInfo.setStatus(CloudStatus.INTERNAL_TEST.getCode());
				newTblCloudInfo.setUpdateTime(new Date());
				cloudRepository.updateCloudSelective(newTblCloudInfo);

				RedisUtil.zadd(RedisCache.CLOUD_HEALTH_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
				RedisUtil.zadd(RedisCache.CLOUD_SYNC_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
			}

			return tblCloudInfo.getCloudId();
		}
		catch (DuplicateKeyException e)
		{
			e.printStackTrace();
			LOGGER.error("add cloud error. {}. for {}", e.getMessage(), addCloudReq.getName());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
		catch (Exception e)
		{
			if (e instanceof WebSystemException || e instanceof ConstraintViolationException)
			{
				throw e;
			}
			e.printStackTrace();
			LOGGER.error("add cloud error. {}. for {}", e.getMessage(), addCloudReq.getName());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void updateCloud(String cloudId, UpdateCloudReq updateCloudReq, String userId)
	{
		try
		{
			TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

			if (tblCloudInfo == null || tblCloudInfo.getStatus() == CloudStatus.REMOVED.getCode())
			{
				throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
			}
			if (userId != null && !userId.equals(tblCloudInfo.getOwner()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
			if (tblCloudInfo.getStatus() < CloudStatus.INTERNAL_TEST.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode()
					|| tblCloudInfo.getStatus() == CloudStatus.IMPORT_FILED.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAIN_FILED.getCode())
			{
				boolean updateCloudAgent = false;

				if (! updateCloudReq.getTargetNodes().isEmpty())
				{
					TargetNode targetNode = updateCloudReq.getTargetNodes().get(0);
					if (!StringUtils.equals(tblCloudInfo.getRegionId(), targetNode.getDstRegionId()) ||
							!StringUtils.equals(tblCloudInfo.getSiteId(), targetNode.getDstSiteId()) ||
							!StringUtils.equals(targetNode.getDstNodeId(), tblCloudInfo.getNodeId()))
					{
						tblCloudInfo.setRegionId(targetNode.getDstRegionId());
						tblCloudInfo.setSiteId(targetNode.getDstSiteId());
						tblCloudInfo.setNodeId(targetNode.getDstNodeId());
						if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAIN_FILED.getCode())
						{
							tblCloudInfo.setStatus(CloudStatus.MAINTAINING.getCode());
						}
						else
						{
							tblCloudInfo.setStatus(CloudStatus.UPDATING.getCode());
						}
						updateCloudAgent = true;
					}
				}

				if (StringUtils.isNotEmpty(updateCloudReq.getProduct()) && !updateCloudReq.getProduct().equalsIgnoreCase(tblCloudInfo.getProduct()))
				{
					tblCloudInfo.setProduct(updateCloudReq.getProduct());
					if (((updateCloudReq.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) ||
							updateCloudReq.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName())) &&
							(updateCloudReq.getAuthorization() == null || updateCloudReq.getAuthorization().getPassword() == null ||
									StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getUserid()) ||
									StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getPassword())))
							|| (updateCloudReq.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()) &&
							(updateCloudReq.getAuthorization() == null || updateCloudReq.getAuthorization().getAccessKey() == null ||
									StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getId()) ||
									StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getSecret()))))
					{
						throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
					}
					tblCloudInfo.setAuthorization(JsonUtils.toJson(updateCloudReq.getAuthorization()));
				}
				else if (updateCloudReq.getAuthorization() != null)
				{
					Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);
					boolean updateAuthorization = false;

					if (authorization == null)
					{
						if (((tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) ||
								tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName())) &&
								(updateCloudReq.getAuthorization() == null || updateCloudReq.getAuthorization().getPassword() == null ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getUserid()) ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getPassword())))
								|| (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()) &&
								(updateCloudReq.getAuthorization() == null || updateCloudReq.getAuthorization().getAccessKey() == null ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getId()) ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getSecret()))))
						{
							throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
						}
						authorization = updateCloudReq.getAuthorization();
						updateAuthorization = true;
					}
					else
					{
						if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
						{
							if (authorization.getPassword() == null)
							{
								if (StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getUserid()) ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getPassword().getPassword()))
								{
									throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
								}
								authorization.setPassword(updateCloudReq.getAuthorization().getPassword());
								updateAuthorization = true;
							}
							else
							{
								if (StringUtils.isNotEmpty(updateCloudReq.getAuthorization().getPassword().getUserid()) && !updateCloudReq.getAuthorization().getPassword().getUserid().equals(authorization.getPassword().getUserid()))
								{
									authorization.getPassword().setPassword(updateCloudReq.getAuthorization().getPassword().getUserid());
									updateAuthorization = true;
								}
								if (StringUtils.isNotEmpty(updateCloudReq.getAuthorization().getPassword().getUsername()) && !updateCloudReq.getAuthorization().getPassword().getUsername().equals(authorization.getPassword().getUsername()))
								{
									authorization.getPassword().setPassword(updateCloudReq.getAuthorization().getPassword().getUsername());
									updateAuthorization = true;
								}
								if (StringUtils.isNotEmpty(updateCloudReq.getAuthorization().getPassword().getPassword()) && !updateCloudReq.getAuthorization().getPassword().getPassword().equals(authorization.getPassword().getPassword()))
								{
									authorization.getPassword().setPassword(updateCloudReq.getAuthorization().getPassword().getPassword());
									updateAuthorization = true;
								}
							}
						}
						else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
						{
							if (authorization.getAccessKey() == null)
							{
								if (StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getId()) ||
										StringUtils.isEmpty(updateCloudReq.getAuthorization().getAccessKey().getSecret()))
								{
									throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
								}
								authorization.setAccessKey(updateCloudReq.getAuthorization().getAccessKey());
								updateAuthorization = true;
							}
							else
							{
								if (StringUtils.isNotEmpty(updateCloudReq.getAuthorization().getAccessKey().getId()) && !updateCloudReq.getAuthorization().getAccessKey().getId().equals(authorization.getAccessKey().getId()))
								{
									authorization.getAccessKey().setId(updateCloudReq.getAuthorization().getAccessKey().getId());
									updateAuthorization = true;
								}
								if (StringUtils.isNotEmpty(updateCloudReq.getAuthorization().getAccessKey().getSecret()) && !updateCloudReq.getAuthorization().getAccessKey().getSecret().equals(authorization.getAccessKey().getSecret()))
								{
									authorization.getAccessKey().setSecret(updateCloudReq.getAuthorization().getAccessKey().getSecret());
									updateAuthorization = true;
								}
							}
						}

					}

					if (updateAuthorization)
					{
						tblCloudInfo.setAuthorization(JsonUtils.toJson(authorization));
					}
				}

				if (StringUtils.isNotEmpty(updateCloudReq.getName()))
				{
					tblCloudInfo.setName(updateCloudReq.getName());
				}
				if (StringUtils.isNotEmpty(updateCloudReq.getMode()))
				{
					tblCloudInfo.setMode(updateCloudReq.getMode());
				}
				if (StringUtils.isNotEmpty(updateCloudReq.getUrl()))
				{
					tblCloudInfo.setUrl(updateCloudReq.getUrl());
				}
				if (StringUtils.isNotEmpty(updateCloudReq.getCertificate()))
				{
					tblCloudInfo.setCertificate(updateCloudReq.getCertificate());
				}
				if (updateCloudReq.getHealthCheck() != null)
				{
					tblCloudInfo.setHealthCheck(JsonUtils.toJson(updateCloudReq.getHealthCheck()));
				}
				if (updateCloudReq.getOsServiceEndpoints() != null)
				{
					tblCloudInfo.setOsServiceEndpoints(JsonUtils.toJson(updateCloudReq.getOsServiceEndpoints()));
				}
				tblCloudInfo.setLabels(JsonUtils.toJson(updateCloudReq.getLabels()));

				tblCloudInfo.setUpdateTime(new Date());
				cloudRepository.updateCloudSelective(tblCloudInfo);

				if (updateCloudAgent)
				{
					if (tblCloudInfo.getMode().equals(Mode.PROXY))
					{
						MessagePack messagePack = new MessagePack();
						messagePack.setMsgType(CloudMsg.UPDATE);
						messagePack.setMessageObj(tblCloudInfo.getCloudId());
						cloudMsgProcessStrategy.sendMessage(messagePack);
					}
					else if (tblCloudInfo.getMode().equals(Mode.DIRECT))
					{
						TblCloudInfo newTblCloudInfo = new TblCloudInfo();
						newTblCloudInfo.setCloudId(tblCloudInfo.getCloudId());
						newTblCloudInfo.setStatus(CloudStatus.INTERNAL_TEST.getCode());
						newTblCloudInfo.setUpdateTime(new Date());
						cloudRepository.updateCloudSelective(newTblCloudInfo);

						RedisUtil.zadd(RedisCache.CLOUD_HEALTH_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
						RedisUtil.zadd(RedisCache.CLOUD_SYNC_IDS, tblCloudInfo.getCloudId(), new Date().getTime());
					}
				}

				OSClientUtil.removeOSClientV3(cloudId);
			}
			else if ((tblCloudInfo.getStatus() >= CloudStatus.PUBLIC_TEST.getCode() && tblCloudInfo.getStatus() <= CloudStatus.PRE_OFF_SHELF.getCode())
					|| tblCloudInfo.getStatus() == CloudStatus.OFF_SHELF.getCode())
			{
				if (StringUtils.isNotEmpty(updateCloudReq.getName()))
				{
					tblCloudInfo.setName(updateCloudReq.getName());
				}
				tblCloudInfo.setLabels(JsonUtils.toJson(updateCloudReq.getLabels()));
				tblCloudInfo.setUpdateTime(new Date());
				cloudRepository.updateCloudSelective(tblCloudInfo);
			}
			else
			{
				throw new WebSystemException(ErrorCode.PROHIBIT_UPDATE_CLOUD, ErrorLevel.INFO);
			}
		}
		catch (DuplicateKeyException e)
		{
			e.printStackTrace();
			LOGGER.error("update cloud error. {}. for {}", e.getMessage(), updateCloudReq.getName());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
		catch (Exception e)
		{
			if (e instanceof WebSystemException)
			{
				throw e;
			}
			e.printStackTrace();
			LOGGER.error("update cloud error. {}. for {}", e.getMessage(), updateCloudReq.getName());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void actionCloud(String cloudId, ActionCloudReq actionCloudReq, String userId)
	{
		try
		{
			TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

			if (tblCloudInfo == null)
			{
				throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
			}
			if (userId != null && !userId.equals(tblCloudInfo.getOwner()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
			if (actionCloudReq == null || StringUtils.isEmpty(actionCloudReq.getAction()))
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}

			switch (actionCloudReq.getAction())
			{
				case CloudAction.OPEN_TEST:
					if (tblCloudInfo.getStatus() == CloudStatus.INTERNAL_TEST.getCode())
					{
						tblCloudInfo.setStatus(CloudStatus.PUBLIC_TEST.getCode());
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				case CloudAction.MAINTAIN:
					if (tblCloudInfo.getStatus() == CloudStatus.PUBLIC_TEST.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.ON_SHELF.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.PRE_OFF_SHELF.getCode())
					{
						RedisUtil.set(RedisCache.CLOUD_STATUS + cloudId, tblCloudInfo.getStatus().toString());

						tblCloudInfo.setStatus(CloudStatus.MAINTAINING.getCode());
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				case CloudAction.COMPLETE_MAINTAIN:
					if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode())
					{
						String status = RedisUtil.get(RedisCache.CLOUD_STATUS + cloudId);

						if (StringUtils.isNumeric(status))
						{
							Integer oldStatus = Integer.parseInt(status);
							tblCloudInfo.setStatus(oldStatus);
						}
						else
						{
							tblCloudInfo.setStatus(CloudStatus.ON_SHELF.getCode());
						}

						RedisUtil.delete(RedisCache.CLOUD_STATUS + cloudId);
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				case CloudAction.SHELVE:
					if (tblCloudInfo.getStatus() == CloudStatus.INTERNAL_TEST.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.PUBLIC_TEST.getCode())
					{
						tblCloudInfo.setStatus(CloudStatus.ON_SHELF.getCode());
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				case CloudAction.PRE_OFF_SHELVE:
					if (tblCloudInfo.getStatus() == CloudStatus.PUBLIC_TEST.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.ON_SHELF.getCode())
					{
						tblCloudInfo.setStatus(CloudStatus.PRE_OFF_SHELF.getCode());
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				case CloudAction.OFF_SHELVE:
					if (tblCloudInfo.getStatus() == CloudStatus.PUBLIC_TEST.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.ON_SHELF.getCode() ||
							tblCloudInfo.getStatus() == CloudStatus.PRE_OFF_SHELF.getCode())
					{
						tblCloudInfo.setStatus(CloudStatus.OFF_SHELF.getCode());
					}
					else
					{
						throw new WebSystemException(ErrorCode.FORBIDDEN_ACTION, ErrorLevel.INFO);
					}
					break;
				default:
					throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}

			tblCloudInfo.setUpdateTime(new Date());
			cloudRepository.updateCloudSelective(tblCloudInfo);
		}
		catch (WebSystemException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("action cloud error. {}. for {}", e.getMessage(), cloudId);
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public void deleteCloud(String cloudId, boolean force, String userId)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

		if (userId != null && !userId.equals(tblCloudInfo.getOwner()))
		{
			throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
		}

		if (force)
		{
			tblCloudInfo.setStatus(CloudStatus.REMOVED.getCode());
		}
		else
		{
			tblCloudInfo.setStatus(CloudStatus.REMOVING.getCode());
		}

		cloudRepository.updateCloudSelective(tblCloudInfo);

		CloudProduct cloudProduct = CloudProduct.fromName(tblCloudInfo.getProduct());
		if (cloudProduct == CloudProduct.NEXTSTACK || cloudProduct == CloudProduct.EASYSTACK || cloudProduct == CloudProduct.OPENSTACK)
		{
			MessagePack messagePack = new MessagePack();
			messagePack.setMsgType(CloudMsg.DELETE);
			messagePack.setMessageObj(cloudId);
			cloudMsgProcessStrategy.sendMessage(messagePack);
		}
		RedisUtil.odel(RedisCache.CLOUD_HEALTH_STATUS, cloudId);
	}

	public CloudInfoListRsp getClouds(CloudSearchCritical cloudSearchCritical)
	{
		return cloudRepository.getClouds(cloudSearchCritical);
	}

	public CloudBasicInfoListRsp getCloudsBasicInfo(String userId)
	{
		return cloudRepository.getCloudsBasicInfo(userId);
	}

	public Map<String,Object> pingCloudUrl(String url)
	{
		Map<String, Object> pingResult = new HashMap<>();
		try
		{
			URL cloudUrl = new URL(url);
			boolean status = InetAddress.getByName(cloudUrl.getHost()).isReachable(3000);
			pingResult.put("reachable", status);
			return  pingResult;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			LOGGER.error("ping cloud url {} error. {}.", url, e.getMessage());
			throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
		}
	}

	public Map<String,String> syncCloudData(String cloudId)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()) || tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.OPENSTACK.getName()))
		{
			eskSyncDataService.syncData(cloudId);
			return null;
		}
		else
		{
			nskSyncDataService.syncData(cloudId);
			return null;
		}
	}

	public boolean isOwner(String cloudId, String uerId)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);

		if (tblCloudInfo == null || tblCloudInfo.getOwner() == null)
		{
			return false;
		}
		return tblCloudInfo.getOwner().equals(uerId);
	}

	public ResponseEntity sendHttpRequestToCloud(String vendor, String cloudId)
	{
		if (vendor.equalsIgnoreCase(CloudProduct.EASYSTACK.getShortName()) || vendor.equalsIgnoreCase(CloudProduct.OPENSTACK.getShortName()))
		{
			TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
			OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
			return osClientV3.sendHttpRequestToCloud();
		}
		else
		{
			return sendHttpRequestToCloud(cloudId);
		}
	}

	public ResponseEntity sendHttpRequestToCloud(String vendor, String cloudId, Object body)
	{
		if (vendor.equalsIgnoreCase(CloudProduct.EASYSTACK.getShortName()) || vendor.equalsIgnoreCase(CloudProduct.OPENSTACK.getShortName()))
		{
			TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
			OSClientV3 osClientV3 = OSClientUtil.getOSClientV3(tblCloudInfo);
			return osClientV3.sendHttpRequestToCloud(body);
		}
		else
		{
			return sendHttpRequestToCloud(cloudId, body);
		}
	}

	public ResponseEntity sendHttpRequestToCloud(String cloudId)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo == null)
		{
			throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
		}
		if (tblCloudInfo.getMode().equals(Mode.DIRECT))
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate;
			String url;
			try
			{
				String cloudUrl = tblCloudInfo.getUrl().toLowerCase(Locale.ROOT);
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

				if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
				{
					requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
					requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
				}

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);
				HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

				if (cloudUrl.startsWith("https://"))
				{
					SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
							SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
							NoopHostnameVerifier.INSTANCE);
					CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
					HttpComponentsClientHttpRequestFactory requestFactory =
							new HttpComponentsClientHttpRequestFactory();
					requestFactory.setHttpClient(httpClient);
					restTemplate = new RestTemplate(requestFactory);
					url = Utils.buildStr(tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}
				else if (cloudUrl.startsWith("http://"))
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr(tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}
				else
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr("http://", tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}

				HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());

				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate = new RestTemplate();
			try {
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
				requestHeaders.set("Connection", "close");

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);
				HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

				String url = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1]);

				HttpEntity<String> requestEntity = new HttpEntity<>(request.getContext().getBody().toString(), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());
				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public ResponseEntity sendHttpRequestToCloud(String cloudId, Object body)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo == null)
		{
			throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
		}
		if (tblCloudInfo.getMode().equals(Mode.DIRECT))
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate;
			String url;
			try
			{
				String cloudUrl = tblCloudInfo.getUrl().toLowerCase(Locale.ROOT);
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

				if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
				{
					requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
					requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
				}

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);
				HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

				if (cloudUrl.startsWith("https://"))
				{
					SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
							SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
							NoopHostnameVerifier.INSTANCE);
					CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
					HttpComponentsClientHttpRequestFactory requestFactory =
							new HttpComponentsClientHttpRequestFactory();
					requestFactory.setHttpClient(httpClient);
					restTemplate = new RestTemplate(requestFactory);
					url = Utils.buildStr(tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}
				else if (cloudUrl.startsWith("http://"))
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr(tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}
				else
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr("http://", tblCloudInfo.getUrl(), "/api/", httpServerRequest.uri().split("/api/")[1]);
				}

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());

				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate = new RestTemplate();
			try {
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
				requestHeaders.set("Connection", "close");

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);
				HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

				String url = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/", httpServerRequest.uri().split("/cmp/v1/")[1]);

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());
				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public ResponseEntity sendHttpRequestToCloud(String cloudId, String url, Object body, Map<String, String> headers)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo == null)
		{
			throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
		}
		if (tblCloudInfo.getMode().equals(Mode.DIRECT))
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate;
			try
			{
				String cloudUrl = tblCloudInfo.getUrl().toLowerCase(Locale.ROOT);
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

				if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
				{
					requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
					requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
				}

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);

				if (cloudUrl.startsWith("https://"))
				{
					SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
							SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
							NoopHostnameVerifier.INSTANCE);
					CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
					HttpComponentsClientHttpRequestFactory requestFactory =
							new HttpComponentsClientHttpRequestFactory();
					requestFactory.setHttpClient(httpClient);
					restTemplate = new RestTemplate(requestFactory);
					url = Utils.buildStr(tblCloudInfo.getUrl(), url);
				}
				else if (cloudUrl.startsWith("http://"))
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr(tblCloudInfo.getUrl(), url);
				}
				else
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr("http://", tblCloudInfo.getUrl(), url);
				}

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());

				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			VertxServerRequestToHttpServletRequest request = (VertxServerRequestToHttpServletRequest) ((Invocation) ContextUtils.getInvocationContext()).getRequestEx();

			RestTemplate restTemplate = new RestTemplate();
			try {
				HttpHeaders requestHeaders = new HttpHeaders();

				Enumeration headerNames = request.getHeaderNames();
				while (headerNames.hasMoreElements())
				{
					String headerName = (String) headerNames.nextElement();
					requestHeaders.add(headerName, request.getHeader(headerName));
				}

				removeGenHeader(requestHeaders);

				requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
				requestHeaders.set("Connection", "close");

				Field vertxRequestField = request.getClass().getDeclaredField("vertxRequest");
				vertxRequestField.setAccessible(true);
				HttpServerRequest httpServerRequest = (HttpServerRequest)vertxRequestField.get(request);

				url = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/NSK/clouds/", cloudId, url);

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());
				ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), requestEntity, Object.class);

				if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
				{
					try
					{
						Map resultMap = (Map) response.getBody();
						ErrorCode errorCode = ErrorCode.CUSTOM_ERROR;
						errorCode.setCode((int)resultMap.get("code") + 10000);
						errorCode.setMessage((String) resultMap.get("message"));
						throw new WebSystemException(errorCode, ErrorLevel.INFO);
					}
					catch (WebSystemException e)
					{
						throw e;
					}
					catch (Exception e)
					{

					}
				}

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public ResponseEntity sendHttpRequestToCloud(String cloudId, String url, HttpMethod method, Object body, Map<String, String> headers)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo == null)
		{
			throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
		}
		if (tblCloudInfo.getMode().equals(Mode.DIRECT))
		{
			RestTemplate restTemplate;
			try
			{
				String cloudUrl = tblCloudInfo.getUrl().toLowerCase(Locale.ROOT);
				HttpHeaders requestHeaders = new HttpHeaders();

				if (headers != null) headers.forEach(requestHeaders::add);

				Authorization authorization = JsonUtils.fromJson(tblCloudInfo.getAuthorization(), Authorization.class);

				if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
				{
					requestHeaders.add("X-Access-Key", authorization.getAccessKey().getId());
					requestHeaders.add("X-Access-Secret", authorization.getAccessKey().getSecret());
				}

				if (cloudUrl.startsWith("https://"))
				{
					SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
							SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
							NoopHostnameVerifier.INSTANCE);
					CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(scsf).build();
					HttpComponentsClientHttpRequestFactory requestFactory =
							new HttpComponentsClientHttpRequestFactory();
					requestFactory.setHttpClient(httpClient);
					restTemplate = new RestTemplate(requestFactory);
					url = Utils.buildStr(tblCloudInfo.getUrl(), url);
				}
				else if (cloudUrl.startsWith("http://"))
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr(tblCloudInfo.getUrl(), url);
				}
				else
				{
					restTemplate = new RestTemplate();
					url = Utils.buildStr("http://", tblCloudInfo.getUrl(), url);
				}

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());

				ResponseEntity<Object> response = restTemplate.exchange(url, method, requestEntity, Object.class);

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			RestTemplate restTemplate = new RestTemplate();
			try {
				HttpHeaders requestHeaders = new HttpHeaders();

				if (headers != null) headers.forEach(requestHeaders::add);

				requestHeaders.set("X-Access-Token", Utils.buildStr("systemadmin", cloudManagerConfig.getCloudManagerToken()));
				requestHeaders.set("Connection", "close");

				url = Utils.buildStr("http://", cloudManagerConfig.getServiceGwUrl(), "/proxy/NSK/clouds/", cloudId, url);

				HttpEntity<String> requestEntity = new HttpEntity<>(JsonUtils.toJson(body), requestHeaders);
				restTemplate.setErrorHandler(new CustomErrorHandler());
				ResponseEntity<Object> response = restTemplate.exchange(url, method, requestEntity, Object.class);

				return response;
			}
			catch (WebSystemException e)
			{
				throw e;
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public void syncSingleData(String cloudId, String resourceId, String msgType)
	{
		SyncResourceInfo syncResourceInfo = new SyncResourceInfo(cloudId, resourceId, msgType, null);
		cloudProcessStrategy.syncSingleData(syncResourceInfo);
	}

	public void syncSingleData(String cloudId, String resourceId, String msgType, DataSyncLevel dataSyncLevel)
	{
		SyncResourceInfo syncResourceInfo = new SyncResourceInfo(cloudId, resourceId, msgType, dataSyncLevel);
		cloudProcessStrategy.syncSingleData(syncResourceInfo, false);
	}

	private void removeGenHeader(HttpHeaders requestHeaders)
	{
		requestHeaders.remove(HttpHeaders.COOKIE);
		requestHeaders.remove(UserHeadInfo.USERID);
		requestHeaders.remove(UserHeadInfo.USERNAME);
		requestHeaders.remove(UserHeadInfo.BPID);
		requestHeaders.remove(UserHeadInfo.BpName);
		requestHeaders.remove(UserHeadInfo.AUTIORITIES);
		requestHeaders.remove(UserHeadInfo.USERKIND);
		requestHeaders.remove(UserHeadInfo.PROJECT);
		requestHeaders.remove(UserHeadInfo.PROJECT_CHAIN);
		requestHeaders.remove(UserHeadInfo.USER_AGENT);
	}

	public void checkCloudStatus(String cloudId, boolean isGetOperation)
	{
		TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
		if (tblCloudInfo == null || tblCloudInfo.getStatus() >= CloudStatus.REMOVING.getCode())
		{
			throw new WebSystemException(ErrorCode.CLOUD_NOT_EXIST, ErrorLevel.INFO);
		}
		else if (tblCloudInfo.getStatus() == CloudStatus.MAINTAINING.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAIN_FILED.getCode())
		{
			if (! isGetOperation)
			{
				throw new WebSystemException(ErrorCode.CLOUD_MAINTAINING, ErrorLevel.INFO);
			}
		}
		else if (tblCloudInfo.getStatus() == CloudStatus.OFF_SHELF.getCode())
		{
			throw new WebSystemException(ErrorCode.CLOUD_OFF_SHELF, ErrorLevel.INFO);
		}
	}

	public void createResource(String cloudId, String eeId, String bpId, String userId, String createResourceMsg, Object object)
	{
		CreateResourceInfo syncResourceInfo = new CreateResourceInfo(cloudId, eeId, bpId, userId, object);
		cloudProcessStrategy.createResource(syncResourceInfo, createResourceMsg);
	}

	public static HealthStatus getCloudHealthStatus(String cloudId)
	{
		try
		{
			if (cloudHealthMap.containsKey(cloudId))
			{
				return cloudHealthMap.get(cloudId);
			}
			else
			{
				String status = RedisUtil.get(RedisCache.CLOUD_HEALTH_STATUS + cloudId);
				if (!StringUtils.isNumeric(status))
				{
					return HealthStatus.UNKNOWN;
				}
				else
				{
					return HealthStatus.fromCode(Integer.parseInt(status));
				}
			}
		}
		catch (Exception e)
		{
			return HealthStatus.UNKNOWN;
		}
	}

	public static void updateCloudHealthStatus(String cloudId, HealthStatus healthStatus)
	{
		cloudHealthMap.put(cloudId, healthStatus);
	}
}
