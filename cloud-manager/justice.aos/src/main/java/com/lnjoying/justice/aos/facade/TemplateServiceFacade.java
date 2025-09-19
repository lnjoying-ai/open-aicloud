package com.lnjoying.justice.aos.facade;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.aos.common.TemplateSearchCritical;
import com.lnjoying.justice.aos.db.mapper.AosOperator;
import com.lnjoying.justice.aos.db.model.*;
import com.lnjoying.justice.aos.db.repo.TemplateRepository;
import com.lnjoying.justice.aos.domain.dto.req.AddTemplateReq;
import com.lnjoying.justice.aos.domain.dto.req.UpdateTemplateReq;
import com.lnjoying.justice.aos.domain.dto.rsp.GetTemplateListRsp;
import com.lnjoying.justice.aos.domain.model.JusticeCompose;
import com.lnjoying.justice.aos.domain.model.TemplateInfo;
import com.lnjoying.justice.aos.domain.model.TemplateInputParams;
import com.lnjoying.justice.aos.domain.model.TemplateVerbose;
import com.lnjoying.justice.aos.handler.resourcesupervisor.TemplateResourceSupervisor;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;

import javax.ws.rs.HttpMethod;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.lnjoying.justice.commonweb.util.HttpContextUtils.getHttpServletRequest;
import static com.lnjoying.justice.schema.common.ErrorCode.TEMPLATE_PARSE_FAILED;


/**
 * service facade for template, provider add,update,delete, get function.
 */
@Service
public class TemplateServiceFacade
{
	Yaml yaml = new Yaml();

	@Autowired
	TemplateRepository templateRepo;

	@Autowired
	AosOperator aosOperator;

	@Autowired
	private CombRpcService combRpcService;

	@Autowired
	private TemplateResourceSupervisor templateResourceSupervisor;

	/**
	 * add new template to db
	 * @param req
	 * @param bpId
	 * @param bpName
	 * @param userId
	 * @return
	 * @throws WebSystemException
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> addTemplate(AddTemplateReq req, String bpId, String bpName, String userId)
	{
		try
		{
			if (StringUtils.isEmpty(req.getName()) || StringUtils.isEmpty(req.getVersion()))
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}

			// Check if it already exists template base info
			TemplateSearchCritical searchCritical = new TemplateSearchCritical();
			searchCritical.setName(req.getName());
			searchCritical.setContainsPublic(true);
			searchCritical.setBpId(bpId);
			searchCritical.setUserId(userId);
			List<TblStackTemplateBaseInfo> templateNameBasics = templateRepo.selectAllByNameAndBpIdAndUserId(searchCritical);
			// insert basic info
			TblStackTemplateBaseInfo tblStackTemplateBaseInfo = new TblStackTemplateBaseInfo();
			if (CollectionUtils.isEmpty(templateNameBasics))
			{
				tblStackTemplateBaseInfo.setId(Utils.assignUUId());
				tblStackTemplateBaseInfo.setName(req.getName());
				tblStackTemplateBaseInfo.setUserId(userId);
				tblStackTemplateBaseInfo.setBpId(bpId);
				tblStackTemplateBaseInfo.setLogoUrl(req.getLogo_url());
				tblStackTemplateBaseInfo.setStatus(RecordStatus.NORMAL.value());
				if (! StringUtils.isEmpty(req.getVendor()))
				{
					tblStackTemplateBaseInfo.setVendor(req.getVendor());
				}
				else
				{
					tblStackTemplateBaseInfo.setVendor(bpName);
				}
				tblStackTemplateBaseInfo.setDescription(req.getDescription());
				if (req.getLabels() != null && ! req.getLabels().isEmpty())
				{
					tblStackTemplateBaseInfo.setLabels(JsonUtils.toJson(req.getLabels()));
				}
				tblStackTemplateBaseInfo.setCreateTime(new Date());
				tblStackTemplateBaseInfo.setUpdateTime(tblStackTemplateBaseInfo.getCreateTime());
				templateRepo.insertTemplateBasicInfo(tblStackTemplateBaseInfo);
			}
			else {
				tblStackTemplateBaseInfo = templateNameBasics.get(0);
			}

			// insert template info
			TblStackTemplateInfo tblStackTemplateInfo = new TblStackTemplateInfo();
			tblStackTemplateInfo.setTemplateId(Utils.assignUUId());
			tblStackTemplateInfo.setRootId(tblStackTemplateBaseInfo.getId());
			tblStackTemplateInfo.setVersion(req.getVersion());
			tblStackTemplateInfo.setAosType(req.getAos_type());
			if (StringUtils.isEmpty(tblStackTemplateInfo.getAosType()))
			{
				tblStackTemplateInfo.setAosType("docker-compose");
			}
			tblStackTemplateInfo.setContentType(req.getContent_type());
			tblStackTemplateInfo.setIsUsed(false);
			if (! StringUtils.isEmpty(req.getJustice_compose()))
			{
				tblStackTemplateInfo.setJusticeCompose(req.getJustice_compose());
				JusticeCompose justiceCompose  = yaml.loadAs(req.getJustice_compose(), JusticeCompose.class);
				if (justiceCompose.getBasic() != null && justiceCompose.getBasic().getInput_params() != null)
				{
					tblStackTemplateInfo.setShowInputs(JsonUtils.toJson(justiceCompose.getBasic().getInput_params()));
				}
			}

			tblStackTemplateInfo.setStackCompose(req.getStack_compose());
			tblStackTemplateInfo.setCreateTime(new Date());
			tblStackTemplateInfo.setUpdateTime(tblStackTemplateInfo.getCreateTime());
			tblStackTemplateInfo.setStatus(RecordStatus.NORMAL.value());
			tblStackTemplateInfo.setDescription(req.getDescription());

			tblStackTemplateInfo.setLabels(tblStackTemplateBaseInfo.getLabels());

			templateRepo.insertTemplate(tblStackTemplateInfo);
			//记录资源创建事件
			String resourceInstanceName = String.format("%s:%s", tblStackTemplateBaseInfo.getName(),
					tblStackTemplateInfo.getVersion());
			templateResourceSupervisor.publishCreateEvent(tblStackTemplateInfo, resourceInstanceName,null, "addTemplate");

			Map<String,String> ret = new HashMap<>();
			ret.put("id", tblStackTemplateInfo.getTemplateId());
			return ret;
		}
		catch (DuplicateKeyException e)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DUP, ErrorLevel.INFO);
		}
		catch (ConstructorException e)
		{
			throw new WebSystemException(TEMPLATE_PARSE_FAILED, ErrorLevel.ERROR);
		}
	}

	/**
	 * update template in db
	 * @param templateId
	 * @param req
	 * @throws WebSystemException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateTemplate(String templateId, UpdateTemplateReq req)
	{
		TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplate(templateId);
		//记录原始状态
		TblStackTemplateInfo beforeUpdateTemplate = DeepCopyUtils.deepCopy(tblStackTemplateInfo);
		if (tblStackTemplateInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblStackTemplateInfo.getStatus() == RecordStatus.DELETED.value())
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DROPPED, ErrorLevel.INFO);
		}

		tblStackTemplateInfo.setAosType(req.getAos_type());
		tblStackTemplateInfo.setContentType(req.getContent_type());


		tblStackTemplateInfo.setIsUsed(false);
		tblStackTemplateInfo.setJusticeCompose(req.getJustice_compose());
		if (! StringUtils.isEmpty(req.getJustice_compose()))
		{
			tblStackTemplateInfo.setJusticeCompose(req.getJustice_compose());
			JusticeCompose justiceCompose  = yaml.loadAs(req.getJustice_compose(), JusticeCompose.class);
			if (Objects.nonNull(justiceCompose))
			{
				if (justiceCompose.getBasic() != null && justiceCompose.getBasic().getInput_params() != null)
				{
					tblStackTemplateInfo.setShowInputs(JsonUtils.toJson(justiceCompose.getBasic().getInput_params()));
				}
				else
				{
					tblStackTemplateInfo.setShowInputs(null);
				}
			}
			else
			{
				tblStackTemplateInfo.setShowInputs(null);
			}

		}

		if (req.getLabels() != null && ! req.getLabels().isEmpty())
		{
			tblStackTemplateInfo.setLabels(JsonUtils.toJson(req.getLabels()));
		}

		tblStackTemplateInfo.setStackCompose(req.getStack_compose());
		tblStackTemplateInfo.setDescription(req.getDescription());
		tblStackTemplateInfo.setUpdateTime(new Date());
		templateRepo.updateTemplate(tblStackTemplateInfo);

		// update basic info
		String rootId = tblStackTemplateInfo.getRootId();
		if (StringUtils.isNotBlank(rootId))
		{
			TblStackTemplateBaseInfo baseInfo = new TblStackTemplateBaseInfo();
			baseInfo.setId(rootId);
			if (! StringUtils.isEmpty(req.getVendor()))
			{
				baseInfo.setVendor(req.getVendor());
			}
			if (req.getLabels() != null && ! req.getLabels().isEmpty())
			{
				baseInfo.setLabels(JsonUtils.toJson(req.getLabels()));
			}
			baseInfo.setDescription(req.getDescription());
			baseInfo.setLogoUrl(req.getLogo_url());
			baseInfo.setUpdateTime(new Date());
			templateRepo.updateBasicInfoByPrimaryKeySelective(baseInfo);
		}
	}

	/**
	 * delete template in db
	 * @param templateId
	 * @throws WebSystemException
	 */
	public void deleteTemplate(String templateId)
	{
		TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplate(templateId);
		if (tblStackTemplateInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblStackTemplateInfo.getStatus() == RecordStatus.DELETED.value())
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DROPPED, ErrorLevel.INFO);
		}

		String rootId = tblStackTemplateInfo.getRootId();
		modifyStackTemplateBaseInfo(rootId);

		tblStackTemplateInfo.setStatus(RecordStatus.DELETED.value());
		templateRepo.updateTemplate(tblStackTemplateInfo);

	}

	public void deleteTemplate(String templateId, String name,String operUserId)
	{
		TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplate(templateId);
		if (tblStackTemplateInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		TblStackTemplateBaseInfo tblStackTemplateBaseInfo = templateRepo.selectBasicInfoByPrimaryKey(tblStackTemplateInfo.getRootId());
		if (tblStackTemplateBaseInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(name))
		{
			if (! tblStackTemplateBaseInfo.getName().equals(name))
			{
				throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
			}
		}

		if (tblStackTemplateInfo.getStatus() == RecordStatus.DELETED.value())
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId))
		{
			if (! operUserId.equals(tblStackTemplateBaseInfo.getUserId()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}
		modifyStackTemplateBaseInfo(tblStackTemplateBaseInfo.getId());
		tblStackTemplateInfo.setStatus(RecordStatus.DELETED.value());
		templateRepo.updateTemplate(tblStackTemplateInfo);
	}

	public void deleteTemplateByName(String name, String operUserId)
	{
		TemplateSearchCritical searchCritical = new TemplateSearchCritical();
		TblStackTemplateInfoExample example =  new TblStackTemplateInfoExample();
		TblStackTemplateInfoExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		if (! StringUtils.isEmpty(operUserId))
		{
			criteria.andUserIdEqualTo(operUserId);
			searchCritical.setUserId(operUserId);
		}
		criteria.andStatusNotEqualTo(RecordStatus.DELETED.value());

		searchCritical.setName(name);
		List<TblStackTemplateBaseInfo> tblStackTemplateBaseInfos = templateRepo.selectAllByNameAndBpIdAndUserId(searchCritical);
		if (!CollectionUtils.isEmpty(tblStackTemplateBaseInfos))
		{
			tblStackTemplateBaseInfos.stream().forEach(tblStackTemplateBaseInfo -> {
				modifyStackTemplateBaseInfo(tblStackTemplateBaseInfo.getId());
			});
		}

		TblStackTemplateInfo tblStackTemplateInfo = new TblStackTemplateInfo();
		tblStackTemplateInfo.setStatus(RecordStatus.DELETED.value());
		tblStackTemplateInfo.setUpdateTime(new Date());

		templateRepo.updateTemplate(tblStackTemplateInfo, example);
	}

	/**
	 * get template by templateId
	 * @param templateId
	 * @param operUserId
	 * @return
	 * @throws WebSystemException
	 */
	public TemplateInfo getTemplate(String templateId, String operUserId)
	{
		TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplate(templateId);
		if (tblStackTemplateInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}
		TblStackTemplateBaseInfo tblStackTemplateBaseInfo = templateRepo.selectBasicInfoByPrimaryKey(tblStackTemplateInfo.getRootId());
		if (tblStackTemplateBaseInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblStackTemplateInfo.getStatus() == RecordStatus.DELETED.value())
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId))
		{
			if (! operUserId.equals(tblStackTemplateBaseInfo.getUserId()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		return assembleTemplateInfo(tblStackTemplateInfo, tblStackTemplateBaseInfo);
	}

	/**
	 * download template file by templateId
	 * @param templateId
	 * @param operUserId
	 * @return
	 * @throws IOException
	 */
	public ByteArrayOutputStream downTemplate(String templateId, String operUserId) throws IOException
	{
		TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplate(templateId);
		if (tblStackTemplateInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}
		TblStackTemplateBaseInfo tblStackTemplateBaseInfo = templateRepo.selectBasicInfoByPrimaryKey(tblStackTemplateInfo.getRootId());
		if (tblStackTemplateBaseInfo == null)
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_NOT_EXIST, ErrorLevel.INFO);
		}

		if (tblStackTemplateInfo.getStatus() == RecordStatus.DELETED.value())
		{
			throw new WebSystemException(ErrorCode.TEMPLATE_DROPPED, ErrorLevel.INFO);
		}

		if (! StringUtils.isEmpty(operUserId))
		{
			if (! operUserId.equals(tblStackTemplateBaseInfo.getUserId()))
			{
				throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		try
		{
			if (! StringUtils.isEmpty(tblStackTemplateInfo.getStackCompose()))
			{
				Utils.zipFile(tblStackTemplateInfo.getStackCompose(), zos, "docker-compose.yml");
			}

			if (! StringUtils.isEmpty(tblStackTemplateInfo.getJusticeCompose()))
			{
				Utils.zipFile(tblStackTemplateInfo.getJusticeCompose(), zos, "justice-compose.yml");
			}
		}
		finally
		{
			zos.flush();
			baos.flush();

			zos.close();
			baos.close();
		}

		return baos;
	}

	/**
	 *
	 * assemble template info by record in db
	 * @param tblStackTemplateInfo
	 * @return
	 */
	private TemplateInfo assembleTemplateInfo(TblStackTemplateInfo tblStackTemplateInfo, TblStackTemplateBaseInfo tblStackTemplateBaseInfo)
	{

		TemplateInfo templateInfo = new TemplateInfo();

		// change name to template name + username
		templateInfo.setName(tblStackTemplateBaseInfo.getName());
		templateInfo.setVersion(tblStackTemplateInfo.getVersion());
		templateInfo.setIs_used(tblStackTemplateInfo.getIsUsed());
		templateInfo.setAos_type(tblStackTemplateInfo.getAosType());
		templateInfo.setContent_type(tblStackTemplateInfo.getContentType());
		templateInfo.setJustice_compose(tblStackTemplateInfo.getJusticeCompose());
		templateInfo.setStack_compose(tblStackTemplateInfo.getStackCompose());
		templateInfo.setVendor(tblStackTemplateBaseInfo.getVendor());
		templateInfo.setLogo_url(tblStackTemplateBaseInfo.getLogoUrl());
		templateInfo.setName(tblStackTemplateBaseInfo.getName());
		templateInfo.setId(tblStackTemplateInfo.getTemplateId());

		if (! StringUtils.isEmpty(tblStackTemplateInfo.getShowInputs()))
		{
			List<TemplateInputParams> inputs = JsonUtils.fromJson(tblStackTemplateInfo.getShowInputs(), new com.google.gson.reflect.TypeToken<List<TemplateInputParams>>(){}.getType());
			templateInfo.setShow_inputs(inputs);
		}

		templateInfo.setDescription(tblStackTemplateInfo.getDescription());
		templateInfo.setCreate_time(Utils.formatDate(tblStackTemplateInfo.getCreateTime()));
		templateInfo.setUpdate_time(Utils.formatDate(tblStackTemplateInfo.getUpdateTime()));

		if (tblStackTemplateInfo.getLabels() != null)
		{
			List<String> labels = JsonUtils.fromJson(tblStackTemplateInfo.getLabels(), new com.google.gson.reflect.TypeToken<List<String>>(){}.getType());
			templateInfo.setLabels(labels);
		}

		return templateInfo;
	}

	/**
	 * get template list
	 * @param searchCritical
	 * @return
	 */
	public GetTemplateListRsp getTemplateList(TemplateSearchCritical searchCritical)
	{

		TblStackTemplateBaseInfoExample example = new TblStackTemplateBaseInfoExample();
		TblStackTemplateBaseInfoExample.Criteria criteria = example.createCriteria();
		if (CollectionUtils.hasContent(searchCritical.getName()))
		{
			criteria.andNameLike("%"+searchCritical.getName()+"%");
		}

		if (CollectionUtils.hasContent(searchCritical.getBpId()))
		{
			criteria.andBpIdEqualToOrPublic(searchCritical.getBpId());
		}

		if (CollectionUtils.hasContent(searchCritical.getUserId()))
		{
			criteria.andUserIdEqualTo(searchCritical.getUserId());
		}

		if (CollectionUtils.hasContent(searchCritical.getRootId()))
		{
			criteria.andIdEqualTo(searchCritical.getRootId());
		}

		if (CollectionUtils.hasContent(searchCritical.getLabels()))
		{
			String [] labelArray = searchCritical.getLabels().split(" ");
			for (String label : labelArray)
			{
				criteria.andLabelsLike("%" + label + "%");
			}
		}
		criteria.andStatusNotEqualTo(-1);
		PageHelper.startPage(searchCritical.getPageNum(), searchCritical.getPageSize());
		List<TblStackTemplateBaseInfo> templateNameBasics = templateRepo.selecByExample(example);
		PageInfo<TblStackTemplateBaseInfo> pageInfo = new PageInfo<>(templateNameBasics);


		GetTemplateListRsp getTemplateListRsp = new GetTemplateListRsp();
		getTemplateListRsp.setTotal_num(pageInfo.getTotal());

		if (! templateNameBasics.isEmpty())
		{
			List<TemplateVerbose> templates = new ArrayList<>();

			for (TblStackTemplateBaseInfo templateNameBasic : templateNameBasics)
			{
				TemplateVerbose templateVerbose = doGetTemplateVerbose(templateNameBasic);
				if (templateVerbose == null) continue;
				templates.add(templateVerbose);

			}

			getTemplateListRsp.setTemplates(templates);

		}

		return getTemplateListRsp;
	}

	public boolean isOwnerOfTemplate(String templateId, String bpId, String userId)
	{
		if (StringUtils.isEmpty(templateId))
		{
			return false;
		}
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
		{
			return false;
		}

		TblStackTemplateInfoExample example = new TblStackTemplateInfoExample();
		TblStackTemplateInfoExample.Criteria criteria = example.createCriteria();
		criteria.andTemplateIdEqualTo(templateId);
		List<TblStackTemplateInfo> tblStackInfos = templateRepo.getTemplates(example);
		if (CollectionUtils.isEmpty(tblStackInfos))
		{
			return false;
		}

		TblStackTemplateInfo tblStackTemplateInfo = tblStackInfos.get(0);
		TemplateSearchCritical searchCritical = new TemplateSearchCritical();
		searchCritical.setRootId(tblStackTemplateInfo.getRootId());
		searchCritical.setUserId(userId);
		searchCritical.setBpId(bpId);
		if (HttpMethod.GET.equalsIgnoreCase(getHttpServletRequest().getMethod()))
		{
			searchCritical.setContainsPublic(true);
		}
		List<TblStackTemplateBaseInfo> templateNameBasics = templateRepo.selectAllByNameAndBpIdAndUserId(searchCritical);
		return null != templateNameBasics && templateNameBasics.size() != 0;
	}

	public List<TblStackTemplateInfo> selectBasicInfoByName(String name)
	{
		List<TblStackTemplateBaseInfo> tblStackTemplateBaseInfos = templateRepo.selectBasicInfoByName(name);
		if (!CollectionUtils.isEmpty(tblStackTemplateBaseInfos))
		{
			TblStackTemplateBaseInfo tblStackTemplateBaseInfo = tblStackTemplateBaseInfos.get(0);
			return templateRepo.selectByRootId(tblStackTemplateBaseInfo.getId());
		}

		return Collections.EMPTY_LIST;
	}


	public TemplateVerbose getTemplateVersionList(TemplateSearchCritical searchCritical)
	{
		TblStackTemplateBaseInfo tblStackTemplateBaseInfo = templateRepo.selectBasicInfoByPrimaryKey(searchCritical.getRootId());
		if (Objects.nonNull(tblStackTemplateBaseInfo))
		{
			TemplateVerbose templateVerbose = doGetTemplateVerbose(tblStackTemplateBaseInfo);
			return templateVerbose;
		}
		return null;
	}

	/**
	 * If all template info is deleted, set template basic info to delete state
	 * @param rootId
	 * @return true to modify, false do not modify
	 */
	private boolean checkModifyStackTemplateBaseInfo(String rootId)
	{
		List<TblStackTemplateInfo> tblStackTemplateInfos = templateRepo.selectByRootId(rootId);
		if (!CollectionUtils.isEmpty(tblStackTemplateInfos) && tblStackTemplateInfos.size() > 1)
		{
			return false;
		}

		return true;
	}

	private void modifyStackTemplateBaseInfo(String rootId)
	{
		boolean modify = checkModifyStackTemplateBaseInfo(rootId);
		if (modify)
		{
			TblStackTemplateBaseInfo baseInfo = new TblStackTemplateBaseInfo();
			baseInfo.setId(rootId);
			baseInfo.setUpdateTime(new Date());
			baseInfo.setStatus(RecordStatus.DELETED.value());
			templateRepo.updateBasicInfoByPrimaryKeySelective(baseInfo);
		}
	}

	private TemplateVerbose doGetTemplateVerbose(TblStackTemplateBaseInfo templateNameBasic)
	{
		List<TblStackTemplateInfo> templateInfoList = templateRepo.selectByRootId(templateNameBasic.getId());
		if (templateInfoList == null || templateInfoList.isEmpty())
		{
			return null;
		}
		List<TemplateInfo> templateInfos = new ArrayList<>();
		int usedNum = 0;

		for (TblStackTemplateInfo tblStackTemplateInfo : templateInfoList)
		{
			TemplateInfo templateInfo = assembleTemplateInfo(tblStackTemplateInfo, templateNameBasic);
			templateInfos.add(templateInfo);
			if (tblStackTemplateInfo.getIsUsed() == true)
			{
				usedNum++;
			}
		}

		TemplateInfo latest = templateInfos.get(0);
		TemplateVerbose templateVerbose = new TemplateVerbose();
		templateVerbose.setId(templateNameBasic.getId());
		templateVerbose.setVersions(templateInfos);
		templateVerbose.setVersion(latest.getVersion());
		templateVerbose.setName(latest.getName());
		templateVerbose.setLogo_url(latest.getLogo_url());
		templateVerbose.setNum(templateInfos.size());
		templateVerbose.setVendor(latest.getVendor());
		String userId = templateNameBasic.getUserId();
		if (StringUtils.isNotBlank(userId))
		{
			templateVerbose.setUserId(userId);
			templateVerbose.setUserName(combRpcService.getUmsService().getUserNameByUserId(userId));
		}

		String bpId = templateNameBasic.getBpId();
		if (StringUtils.isNotBlank(bpId))
		{
			templateVerbose.setBpId(bpId);
			templateVerbose.setBpName(combRpcService.getUmsService().getBpNameByBpId(bpId));
		}


		templateVerbose.setUsed_num(usedNum);
		return templateVerbose;
	}

}
