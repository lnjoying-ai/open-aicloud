package com.lnjoying.justice.iam.rpcserviceimpl;

import com.lnjoying.justice.iam.config.CommonProperties;
import com.lnjoying.justice.iam.service.BpManagerService;
import com.lnjoying.justice.schema.common.UserMetaDTO;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.schema.entity.iam.UserMetasRsp;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.lnjoying.justice.iam.common.constant.UMStatus;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblBpInfoExample;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfoExample;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleDto;
import com.lnjoying.justice.iam.service.UserManagerService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.bouncycastle.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.constant.UserKindEnum.fromCode;

/**
 *  Rpc implementation class
 *
 * @author merak
 **/
@RpcSchema(schemaId = "umsService")
@Slf4j
public class UmsServiceImpl implements UmsService {

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BpRepository bpRepository;

    @Autowired
    private BpManagerService bpManagerService;

    @Autowired
    private CommonProperties commonProperties;

    private Date startDate = new Date();
    private Date endDate = new Date();


    @Override
    public List<String> getRolesByUserId(@ApiParam(name = "userId")String userId) {
        List<RoleDto> rolesByUserId = userManagerService.getRolesByUserId(userId);
        if (! CollectionUtils.isEmpty(rolesByUserId)) {
            List<String> roles = rolesByUserId.stream().map(roleDto ->
                    Strings.toUpperCase("role" + "_" + roleDto.getPlatform() + "_" + roleDto.getRole()))
                    .collect(Collectors.toList());
            return roles;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isBpUser(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        if (null == userId || userId.isEmpty())
        {
            return false;
        }
        if (null == bpId || bpId.isEmpty())
        {
            return false;
        }
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        return null != tblUserInfo && null != tblUserInfo.getBpId() && !tblUserInfo.getBpId().isEmpty() && tblUserInfo.getBpId().equals(bpId);
    }

    @Override
    public String getBpNameByBpId(@ApiParam(name = "bpId")String bpId)
    {
        TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);
        if (Objects.nonNull(tblBpInfo))
        {
            return StringUtils.isNotBlank(tblBpInfo.getBpName()) ? tblBpInfo.getBpName() : "";
        }
        return "";
    }

    @Override
    public String getUserNameByUserId(@ApiParam(name = "userId")String userId)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        if (Objects.nonNull(tblUserInfo))
        {
            return StringUtils.isNotBlank(tblUserInfo.getUserName()) ? tblUserInfo.getUserName() : "";
        }
        return "";
    }

    @Override
    public UserInfo getUserInfoByUseId(@ApiParam(name = "userId")String userId)
    {
        try
        {
            TblUserInfo tblUserInfo = userRepository.getUserById(userId);
            UserInfo userInfo = toUserInfo(tblUserInfo);
            return userInfo;
        }
        catch (Exception e)
        {
            log.error("get user info by userId failed, userId:{}", userId, e);
        }

        return null;
    }

    @Override
    public UserInfo getMasterUserInfoByBpId(@ApiParam(name = "bpId")String bpId)
    {
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andBpIdEqualTo(bpId);
        criteria.andKindEqualTo(UserKindEnum.BP_ADMIN.getCode());
        TblUserInfo tblUserInfo = userRepository.getUserByExample(example);
        UserInfo userInfo = toUserInfo(tblUserInfo);

        return userInfo;
    }

    @Override
    public BpInfo getAllBps()
    {
        return null;
    }

    @Override
    public List<String> getRolesByUserIdAndServiceCode(@ApiParam(name = "userId")String userId, @ApiParam(name = "serviceCode")String serviceCode)
    {
        List<String> rolesByUserId = userManagerService.getUserRolesByUserIdAndServiceCode(userId, serviceCode);
        if (! CollectionUtils.isEmpty(rolesByUserId)) {
            return rolesByUserId;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserInfo> getUserInfosByRole(@ApiParam(name = "role")String role, @ApiParam(name = "serviceCode")String serviceCode)
    {
        if (StringUtils.isNotBlank(role))
        {
            if (role.startsWith("role"))
            {
                role = role.replaceAll("role_", "");
            }

            if (role.startsWith("ROLE_"))
            {
                role = role.replaceAll("ROLE_", "");
            }

            if (role.contains(serviceCode))
            {
                role =  role.replaceAll(serviceCode+"_", "");
            }

            if (role.contains(StringUtils.upperCase(serviceCode)))
            {
                role =  role.replaceAll(StringUtils.upperCase(serviceCode)+"_", "");
            }

        }
        List<TblUserInfo> tblUserInfos = userManagerService.getUserInfosByRoleAndServiceCode(role, serviceCode);
        if (! CollectionUtils.isEmpty(tblUserInfos))
        {
            return tblUserInfos.stream().map(user -> toUserInfo(user)).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public UserMetasRsp getUserMetaGroupByBpId(@ApiParam(name = "bpIds")List<String> bpIds, @ApiParam(name = "startDate")Date startDate,
                                               @ApiParam(name = "endDate")Date endDate,
                                               @ApiParam(name = "pageSize")Integer pageSize, @ApiParam(name = "pageNum")Integer pageNum,
                                               @ApiParam(name = "sortField")String sortField, @ApiParam(name = "sortDirection")String sortDirection,
                                               @ApiParam(name = "userId") String userId, @ApiParam(name = "userName")String userName)
    {
        return userRepository.getUserMetaGroupByBpId(bpIds, startDate, endDate, pageSize, pageNum, sortDirection, userId, userName);
    }

    @Override
    public UserMetaDTO getUserMetaByBpId(@ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        return userRepository.getUserMetaByBpId(bpId, userId);
    }

    private UserInfo toUserInfo(TblUserInfo tblUserInfo)
    {
        if (Objects.nonNull(tblUserInfo))
        {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(tblUserInfo.getUserName());
            userInfo.setUserId(tblUserInfo.getUserId());
            userInfo.setBpId(tblUserInfo.getBpId());
            userInfo.setKind(fromCode(tblUserInfo.getKind()).getMessage());
            userInfo.setEmail(tblUserInfo.getEmail());
            return userInfo;
        }
        return null;
    }

    @Override
    public int getBpNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }
        return (int)bpRepository.countBpsByExample(example);
    }

    @Override
    public List<String> getBpIds()
    {
        List<String> bpIds = new ArrayList<>();
        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        List<TblBpInfo> tblBpInfos = bpRepository.getBpsByExample(example);
        if (! CollectionUtils.isEmpty(tblBpInfos))
        {
            tblBpInfos.forEach(tblBpInfo -> {bpIds.add(tblBpInfo.getBpId());});
        }
        return bpIds;
    }

    @Override
    public long countBpIds()
    {
        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        return bpRepository.countBpsByExample(example);
    }

    @Override
    public List<String> getAcBpIds()
    {
        List<String> bpIds = new ArrayList<>();
        TblBpInfoExample example = new TblBpInfoExample();
        TblBpInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        criteria.andCreateTimeBetween(startDate,endDate);
        example.setOrderByClause("create_time");
        List<TblBpInfo> tblBpInfos = bpRepository.getBpsByExample(example);
        startDate = endDate;
        // 计算后 10分钟时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MINUTE, 10);
        endDate = calendar.getTime();
        if (! CollectionUtils.isEmpty(tblBpInfos))
        {
            tblBpInfos.forEach(tblBpInfo -> {bpIds.add(tblBpInfo.getBpId());});
        }
        return bpIds;
    }

    @Override
    public List<String> getUserIds()
    {
        List<String> userIds = new ArrayList<>();
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        List<TblUserInfo> tblUserInfos = userRepository.getUsersByExample(example);
        if (! CollectionUtils.isEmpty(tblUserInfos))
        {
            tblUserInfos.forEach(tblUserInfo -> {userIds.add(tblUserInfo.getBpId());});
        }
        return userIds;
    }

    @Override
    public List<BpInfo> selectAllBps(@ApiParam(name = "createTime")Date createTime,@ApiParam(name = "pageNum") Integer pageNum,@ApiParam(name = "pageSize") Integer pageSize)
    {
        List<TblBpInfo> tblBpInfos = bpManagerService.selectAllByCreateTime(createTime, pageNum, pageSize);
        if (! CollectionUtils.isEmpty(tblBpInfos))
        {
            return tblBpInfos.stream().map(bp -> {
                BpInfo bpInfo = new BpInfo();
                BeanUtils.copyProperties(bp, bpInfo);
                return bpInfo;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public String getRegisterMode()
    {
        return commonProperties.getRegisterMode();
    }
}
