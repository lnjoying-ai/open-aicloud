package com.lnjoying.justice.iam.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.domain.dto.response.service.ServicesRsp;
import com.lnjoying.justice.iam.domain.model.IamService;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.LJ_Function;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.iam.common.constant.UMStatus;
import com.lnjoying.justice.iam.common.constant.UserLevel;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpContactsInfo;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpRawReq;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpUniqueReq;
import com.lnjoying.justice.iam.domain.dto.response.bp.BpRsp;
import com.lnjoying.justice.iam.domain.dto.response.bp.BpUniquenessRsp;
import com.lnjoying.justice.iam.domain.dto.response.bp.QueryBpsRsp;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleDto;
import com.lnjoying.justice.iam.domain.model.search.BpSearchCritical;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;

@Service("bpManagerService")
public class BpManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpManagerService.class);

    @Autowired
    private BpRepository bpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Pbkdf2PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectService projectService;

    @Transactional(rollbackFor = Exception.class)
    public Object addBp(BpRawReq bpRawReq)
    {
        try
        {
            LOGGER.info("add bp {}", bpRawReq);
            if (StringUtils.isEmpty(bpRawReq.getName()))
            {
                throw new WebSystemException(ErrorCode.BP_NAME_INVALID, ErrorLevel.INFO);
            }

            TblBpInfo tblBpInfo = new TblBpInfo();
            //tblBpInfo.setBpId(bpRawReq.getLicense_id());
            tblBpInfo.setBpId(Utils.assignUUId());
            tblBpInfo.setBpName(bpRawReq.getName());
            tblBpInfo.setDescription(bpRawReq.getDescription());
            tblBpInfo.setLicenseId(bpRawReq.getLicense_id());
            tblBpInfo.setStatus(bpRawReq.getStatus());

            TblUserInfo tblUserInfo = null;
            LJ_Function<TblUserInfo> lj_function = null;
            if (! StringUtils.isEmpty(bpRawReq.getMaster_user()))
            {
                tblUserInfo = userRepository.getUserByUserName(bpRawReq.getMaster_user());

                if (tblUserInfo == null)
                {
                    tblUserInfo = new TblUserInfo();
                    tblUserInfo.setUserId(Utils.assignUUId());
                    tblUserInfo.setUserName(bpRawReq.getMaster_user());
                    tblUserInfo.setPassword(passwordEncoder.encode(bpRawReq.getLicense_id()));
                    tblUserInfo.setStatus(UMStatus.ACTIVE);
                    tblUserInfo.setIsAllowed(true);
                    tblUserInfo.setKind(UserKindEnum.BP_ADMIN.getCode());
                    tblUserInfo.setLevel(UserLevel.LEVEL1);
                    setUserContactInfo(bpRawReq, tblUserInfo);
                    tblUserInfo.setCreateTime(new Date());

                    lj_function = tbl -> {
                        userRepository.insertUser(tbl);
                        // Initialize bp role
                        initBpRole(tbl);
                    };
                }
                else
                {
                    lj_function = tbl -> userRepository.updateUserInfo(tbl);
                }

                if (tblUserInfo != null)
                {
                    tblBpInfo.setMasterUser(tblUserInfo.getUserName());
                    tblUserInfo.setBpId(tblBpInfo.getBpId());
                    tblUserInfo.setUpdateTime(new Date());
                }
            }


            tblBpInfo.setStatus(bpRawReq.getStatus());
            tblBpInfo.setWebsite(bpRawReq.getWebsite());
            tblBpInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
            tblBpInfo.setUpdateTime(tblBpInfo.getCreateTime());
            if (bpRawReq.getContact_info() != null)
            {
                tblBpInfo.setContactInfo(JsonUtils.toJson(bpRawReq.getContact_info()));
            }

            int result = bpRepository.insertBp(tblBpInfo);


            if (result > 0 && tblUserInfo != null && lj_function != null)
            {
                lj_function.operator(tblUserInfo);
            }

            if (result > 0)
            {
                projectService.insertDefaultProject(tblBpInfo.getBpId(), tblBpInfo.getBpName());
                Map<String, String> retValue = new HashMap<>();
                retValue.put("id", tblBpInfo.getBpId());
                return retValue;
            }
            else
            {
                LOGGER.error("User register failed");
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
            }


        }
        catch (DuplicateKeyException e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.BP_KEY_Occupied, ErrorLevel.INFO, e.getMessage());
        }
        catch (WebSystemException e)
        {
            e.printStackTrace();
            throw new WebSystemException(e.getCode(), ErrorLevel.INFO, e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public long deleteBp(String bpId)
    {
        LOGGER.info("del bp {}", bpId);
        TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);
        if (tblBpInfo == null)
        {
            throw new WebSystemException(ErrorCode.BP_NOT_EXIST, ErrorLevel.INFO);
        }

        tblBpInfo.setStatus(UMStatus.REMOVED);
        return bpRepository.updateBp(tblBpInfo);
    }

    public Object getBpDtoInfo(String bpId)
    {
        LOGGER.info("get bp {}", bpId);

        TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);

        if (tblBpInfo == null)
        {
            throw new WebSystemException(ErrorCode.BP_NOT_EXIST, ErrorLevel.INFO);
        }

        BpRsp bpRsp = new BpRsp();
        bpRsp.setBpInfo(tblBpInfo);
        if (! StringUtils.isEmpty(tblBpInfo.getContactInfo()))
        {
            bpRsp.setContact_info(JsonUtils.fromJson(tblBpInfo.getContactInfo(), BpContactsInfo.class));
        }
        int total = userRepository.countByBpId(tblBpInfo.getBpId());
        bpRsp.setUser_total(total);
        return bpRsp;
    }

    public TblBpInfo getBpInfo(String bpId) {
       return  bpRepository.getBpInfo(bpId);
    }

    @Transactional(rollbackFor = Exception.class)
    public long updateBpInfo(String bpId, BpRawReq bpRawReq)
    {
        LOGGER.info("update bp {} raw info: {}", bpId, bpRawReq);
        TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);
        if (tblBpInfo == null)
        {
            throw new WebSystemException(ErrorCode.BP_NOT_EXIST, ErrorLevel.INFO);
        }

        // bp admin is not allowed to modify license id, only admin can
        if (isBpAdmin())
        {
            if (StringUtils.isNotBlank(tblBpInfo.getLicenseId()))
            {
                bpRawReq.setLicense_id(tblBpInfo.getLicenseId());
            }
        }
        tblBpInfo.setBpName(bpRawReq.getName());
        tblBpInfo.setDescription(bpRawReq.getDescription());
        tblBpInfo.setLicenseId(bpRawReq.getLicense_id());
        tblBpInfo.setMasterUser(bpRawReq.getMaster_user());
        tblBpInfo.setStatus(bpRawReq.getStatus());
        tblBpInfo.setWebsite(bpRawReq.getWebsite());
        tblBpInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
        if (bpRawReq.getContact_info() != null)
        {
            tblBpInfo.setContactInfo(JsonUtils.toJson(bpRawReq.getContact_info()));
        }

        TblUserInfo tblUserInfo = null;
        LJ_Function<TblUserInfo> lj_function = null;
        if (! StringUtils.isEmpty(bpRawReq.getMaster_user()))
        {
            tblUserInfo = userRepository.getUserByUserName(bpRawReq.getMaster_user());

            if (tblUserInfo == null)
            {
                tblUserInfo = new TblUserInfo();
                tblUserInfo.setUserId(Utils.assignUUId());
                tblUserInfo.setPassword(passwordEncoder.encode(bpRawReq.getLicense_id()));
                tblUserInfo.setStatus(UMStatus.ACTIVE);
                tblUserInfo.setLevel(UserLevel.LEVEL1);
                tblUserInfo.setIsAllowed(true);
                tblUserInfo.setKind(UserKindEnum.BP_ADMIN.getCode());
                setUserContactInfo(bpRawReq, tblUserInfo);
                tblUserInfo.setCreateTime(new Date());

                lj_function = tbl -> {
                    userRepository.insertUser(tbl);
                    // Initialize bp role
                    initBpRole(tbl);
                };
            }
            else
            {
                lj_function = tbl -> userRepository.updateUserInfo(tbl);
            }

            if (tblUserInfo != null)
            {
                tblUserInfo.setKind(UserKindEnum.BP_ADMIN.getCode());
                tblUserInfo.setBpId(tblBpInfo.getBpId());
                tblUserInfo.setUserName(bpRawReq.getMaster_user());
                tblBpInfo.setMasterUser(bpRawReq.getMaster_user());
                tblUserInfo.setUpdateTime(new Date());
            }
        }

        int ret =  bpRepository.updateBp(tblBpInfo);
        if (ret > 0)
        {
            if (Objects.nonNull(tblUserInfo))
            {
                lj_function.operator(tblUserInfo);
            }
        }

        return ret;
    }

    TblBpInfoExample setBpInfoExample(BpSearchCritical critical)
    {
        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        if (!isAdmin())
        {
            criteria.andBpIdEqualTo(queryBpId());
        }
        if (critical.getName() != null) criteria.andBpNameLike("%" + critical.getName() + "%");
        return example;
    }

    public Object getBpDtoInfos(BpSearchCritical critical)
    {
        try
        {
            TblBpInfoExample example = setBpInfoExample(critical);
            QueryBpsRsp queryBpsRsp = new QueryBpsRsp();

            Long total_num = bpRepository.countBpsByExample(example);
            queryBpsRsp.setTotal_num(total_num.intValue());
            if (total_num < 1)
            {
                return queryBpsRsp;
            }

            int begin = ((critical.getPageNum()-1) * critical.getPageSize());
            example.setOrderByClause("update_time desc");
            example.setStartRow(begin);
            example.setPageSize(critical.getPageSize());


            List<TblBpInfo> bpInfoList = bpRepository.getBpsByExample(example);
            if (bpInfoList == null)
            {
                return queryBpsRsp;
            }

            List<BpRsp> bpRspList = new ArrayList<>();
            bpInfoList.parallelStream().sequential().forEach(tblBpInfo ->
            {
                BpRsp bpRsp = new BpRsp();
                bpRsp.setBpInfo(tblBpInfo);
                if (! StringUtils.isEmpty(tblBpInfo.getContactInfo()))
                {
                    bpRsp.setContact_info(JsonUtils.fromJson(tblBpInfo.getContactInfo(), BpContactsInfo.class));
                }

                String masterUser = tblBpInfo.getMasterUser();
                if (StringUtils.isNotBlank(masterUser))
                {
                    TblUserInfo userInfo = userRepository.getUserByUserName(masterUser);
                    if (Objects.nonNull(userInfo))
                    {
                        bpRsp.setMaster_user_id(userInfo.getUserId());
                    }
                }

                int total = userRepository.countByBpId(tblBpInfo.getBpId());
                bpRsp.setUser_total(total);
                bpRspList.add(bpRsp);
            });
            queryBpsRsp.setBps(bpRspList);
            return queryBpsRsp;
        }
        catch (Exception e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }


    public Object uniqueness(BpUniqueReq request)
    {

        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        criteria.andBpNameEqualTo(request.getName());
        long count = bpRepository.countBpsByExample(example);
        boolean exist = count > 0;
        return BpUniquenessRsp.builder().name(exist).build();
    }

    public List<TblBpInfo> selectAllByCreateTime(Date createTime, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblBpInfo> tblBpInfos = bpRepository.selectAllByCreateTime(createTime);
        if (CollectionUtils.isNotEmpty(tblBpInfos))
        {
            return tblBpInfos;
        }
        return Collections.emptyList();
    }

    private void initBpRole(TblUserInfo tbl) {
        List<TblIamRole> wholeRoles = userRepository.getWholeRoles();
        if (CollectionUtils.isNotEmpty(wholeRoles)) {
            List<TblIamRole> tenantAdminRoles = wholeRoles.stream().filter(role -> role.getRole().equalsIgnoreCase("TENANT_ADMIN"))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tenantAdminRoles)) {
                List<RoleDto> collect = tenantAdminRoles.stream().map(x -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setRole(x.getRole());
                    roleDto.setPlatform(x.getPlatform());
                    return roleDto;
                }).collect(Collectors.toList());
                try {
                    userRepository.addRolesByUserId(tbl.getUserId(), collect);
                } catch (WebSystemException e) {
                    // Initial authorization failed, manual authorization
                   LOGGER.error("init bp role failed, need manual authorization");
                }
            }
        }
    }

    private void setUserContactInfo(BpRawReq bpRawReq, TblUserInfo tblUserInfo)
    {
        BpContactsInfo contactInfo = bpRawReq.getContact_info();
        if (Objects.nonNull(contactInfo))
        {
            String address = contactInfo.getAddress();
            if (StringUtils.isNotBlank(address))
            {
                tblUserInfo.setAddress(address);
            }

            String email = contactInfo.getEmail();
            if (StringUtils.isNotBlank(email))
            {
                TblUserInfo userByEmail = userRepository.getUserByEmail(email);
                if (Objects.nonNull(userByEmail))
                {
                    throw new WebSystemException(ErrorCode.EmailOccupied, ErrorLevel.ERROR);
                }
                tblUserInfo.setEmail(email);
            }

            String phone = contactInfo.getPhone();
            if (StringUtils.isNotBlank(phone))
            {
                TblUserInfo userByPhone = userRepository.getUserByPhone(phone);
                if (Objects.nonNull(userByPhone))
                {
                    throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.ERROR);
                }
                tblUserInfo.setPhone(phone);
            }
        }

    }
}
