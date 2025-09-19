package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.db.mapper.*;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentEntitiesRsp;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleDto;
import com.lnjoying.justice.schema.common.UserMetaDTO;
import com.lnjoying.justice.schema.entity.iam.UserMetasRsp;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
@Transactional(rollbackFor = {Exception.class})
public class UserRepository
{
    @Autowired
    TblUserInfoMapper tblUserInfoMapper;

    @Autowired
    TblRoleInfoMapper tblRoleInfoMapper;

    @Autowired
    private TblIamRoleMapper iamRoleMapper;

    @Autowired
    TblUserRoleInfoMapper tblUserRoleInfoMapper;

    @Autowired
    UserOperator userOperator;

    public int insertUser(TblUserInfo tblUserInfo)
    {

        return tblUserInfoMapper.insert(tblUserInfo);
    }

    public TblUserInfo getUserById(String userId)
    {
        return tblUserInfoMapper.selectByPrimaryKey(userId);
    }


    public TblUserInfo getUserByPhone(String phone)
    {
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        return getUserByExample(example);
    }


    public TblUserInfo getUserByEmail(String email)
    {
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        return getUserByExample(example);
    }

    public TblUserInfo getUserByExample(TblUserInfoExample example)
    {
        List<TblUserInfo> tblUserInfos = tblUserInfoMapper.selectByExample(example);
        if (tblUserInfos != null && !tblUserInfos.isEmpty()) {
            return tblUserInfos.get(0);
        }

        return null;
    }

    public TblUserInfo getUserByKey(String key)
    {
        TblUserInfo tblUserInfo = getUserByUserName(key);
        if (tblUserInfo != null)
        {
            return tblUserInfo;
        }

        tblUserInfo = getUserByPhone(key);
        if (tblUserInfo != null)
        {
            return tblUserInfo;
        }

        tblUserInfo = getUserByEmail(key);
        if (tblUserInfo != null)
        {
            return tblUserInfo;
        }
        return tblUserInfo;
    }

    public TblUserInfo getUserByUserName(String userName)
    {
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        return getUserByExample(example);
    }

    public List<TblIamRole> getRolesByUserId(String userId)
    {
        return userOperator.getUserRolesByUserId(userId);
    }

    public List<TblIamRole> getUserRolesByUserIdAndServiceCode(String userId, String serviceCode)
    {
        return userOperator.getUserRolesByUserIdAndServiceCode(userId, serviceCode);
    }

    public List<TblUserInfo> getUserInfosByRoleAndServiceCode(String role, String serviceCode)
    {
        return userOperator.getUserInfosByRoleAndServiceCode(role, serviceCode);
    }

    public int updateUserInfo(TblUserInfo tblUserInfo)
    {
        return tblUserInfoMapper.updateByPrimaryKey(tblUserInfo);
    }

    public int deleteUserInfo(String userId)
    {
        return tblUserInfoMapper.deleteByPrimaryKey(userId);
    }

    public int updateUserStatus(String userId, boolean isAllowed)
    {
        TblUserInfo tblUserInfo = new TblUserInfo();
        tblUserInfo.setUserId(userId);
        tblUserInfo.setIsAllowed(isAllowed);
        return tblUserInfoMapper.updateByPrimaryKeySelective(tblUserInfo);
    }

    public List<TblUserInfo> getUsersByExample(TblUserInfoExample example)
    {
        return tblUserInfoMapper.selectByExample(example);
    }

    public int deleteUserRoleByUserId(String userId)
    {
        TblUserRoleInfoExample tblUserRoleInfoExample = new TblUserRoleInfoExample();
        TblUserRoleInfoExample.Criteria criteria = tblUserRoleInfoExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return tblUserRoleInfoMapper.deleteByExample(tblUserRoleInfoExample);
    }

    public long countUsersByExample(TblUserInfoExample example)
    {
        return tblUserInfoMapper.countByExample(example);
    }

    public List<TblIamRole> getWholeRoles()
    {
        return iamRoleMapper.selectByRoleType(TblIamRole.RoleType.SYSTEM.value());
    }

    public TblIamRole getRoleInfo(String platform, String role)
    {
        List<TblIamRoleDetail> tblIamRoleDetails = iamRoleMapper.selectAll(null, null, platform, role, null);
        if (CollectionUtils.isEmpty(tblIamRoleDetails))
        {
            return null;
        }
        return tblIamRoleDetails.get(0);
    }

    public int insertUserRoleInfo(TblUserRoleInfoKey tblUserRoleInfoKey)
    {
        return tblUserRoleInfoMapper.insert(tblUserRoleInfoKey);
    }

    public void addRolesByUserId(String userId, List<RoleDto> roleDtoList)
    {
        TblUserInfo tblUserInfo = getUserById(userId);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        for (RoleDto roleDto : roleDtoList)
        {
            try
            {
                TblIamRole tblRoleInfo = getRoleInfo(roleDto.getPlatform(), roleDto.getRole());
                if (tblRoleInfo == null) continue;
                TblUserRoleInfoKey tblUserRoleInfoKey = new TblUserRoleInfoKey();
                tblUserRoleInfoKey.setRoleId(tblRoleInfo.getRoleId());
                tblUserRoleInfoKey.setUserId(tblUserInfo.getUserId());
                insertUserRoleInfo(tblUserRoleInfoKey);
            }
            catch (DuplicateKeyException e)
            {
                continue;
            }
        }
    }

    public TblUserInfo selectByOpenId(String openId)
    {
        return tblUserInfoMapper.selectByWeixin(openId);
    }


    public List<AttachmentEntitiesRsp.User> selectAllByUserIdIn(Collection<String> userIds)
    {
        return tblUserInfoMapper.selectAllByUserIdIn(userIds);
    }

    public int updateByPrimaryKeySelective(TblUserInfo record)
    {
        return tblUserInfoMapper.updateByPrimaryKeySelective(record);
    }

    public List<TblUserInfo> selectByBpId(String bpId)
    {
        return tblUserInfoMapper.selectByBpId(bpId);
    }

    public int countByBpId(String bpId)
    {
        Integer count = tblUserInfoMapper.countByBpId(bpId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public UserMetasRsp getUserMetaGroupByBpId(List<String> bpIds, Date startDate, Date endDate, Integer pageSize, Integer pageNum, String sortDirection, String userId, String userName)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<UserMetaDTO> userMetaDTOS = tblUserInfoMapper.selectByBpIdAndCreateTimeBetween(bpIds, null, startDate, endDate, sortDirection, userId, userName);
        if (!CollectionUtils.isEmpty(userMetaDTOS))
        {
            return UserMetasRsp.builder().userMetas(userMetaDTOS).totalNum(new PageInfo<>(userMetaDTOS).getTotal()).build();
        }

        return UserMetasRsp.builder().userMetas(Collections.EMPTY_LIST).totalNum(0).build();
    }

    public UserMetaDTO getUserMetaByBpId(String bpId, String userId)
    {
        if (StringUtils.isEmpty(bpId) && StringUtils.isEmpty(userId))
        {
            return null;
        }

        List<String> bpIds = bpId != null ? Lists.newArrayList(bpId) : Lists.newArrayList();
        List<String> userIds = userId != null ? Lists.newArrayList(userId) : Lists.newArrayList();

        List<UserMetaDTO> userMetaDTOS = tblUserInfoMapper.selectByBpIdAndCreateTimeBetween(bpIds, userIds, null, null, null, null, null);
        if (!CollectionUtils.isEmpty(userMetaDTOS))
        {
            return userMetaDTOS.get(0);
        }

        return null;
    }
}
