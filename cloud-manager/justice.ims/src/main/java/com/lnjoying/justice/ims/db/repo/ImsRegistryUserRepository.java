package com.lnjoying.justice.ims.db.repo;

import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryUserMapper;
import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryUserReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryUsersRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryUser;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lnjoying.justice.ims.db.model.TblImsRegistryUser.RegistryUserStatus.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * registry user repository
 *
 * @author merak
 **/

@Repository
public class ImsRegistryUserRepository
{

    @Autowired
    private TblImsRegistryUserMapper imsRegistryUserMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertRegistryUser(AddRegistryUserReq req)
    {
        checkIfTheUserExists(req.getRegistryUserName());

        checkIfTheRegistyUserHasBeenCreated(req.getUserId());

        TblImsRegistryUser registryUser = TblImsRegistryUser.builder().registryUserName(req.getRegistryUserName())
                .registryUserPassword(AesCryptoUtils.cbcEncryptHex(req.getRegistryPassword()))
                .registryUserId(Utils.assignUUId())
                .status(CREATING.value())
                .userId(req.getUserId())
                .bpId(req.getBpId())
                .userName(req.getUserName())
                .bpName(req.getBpName())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).build();

        imsRegistryUserMapper.insert(registryUser);
    }

    public TblImsRegistryUser selectByRegistryUser(String registryUserName)
    {
        TblImsRegistryUser tblImsRegistryUser = imsRegistryUserMapper.selectByRegistryUserName(registryUserName);

        if (Objects.isNull(tblImsRegistryUser))
        {
            throw new ImsWebSystemException(REGISTRY_USER_NOT_EXIST, INFO);
        }

        if (DELETED.value() == tblImsRegistryUser.getStatus())
        {
            throw new ImsWebSystemException(REGISTRY_USER_DROPPED, INFO);
        }
        return tblImsRegistryUser;
    }

    public List<TblImsRegistryUser> selectByBpId(String bpId)
    {
        return imsRegistryUserMapper.selectByBpId(bpId);
    }

    public List<TblImsRegistryUser> selectByUserId(String userId)
    {
        Assert.hasText(userId, "userId can not be empty");
        return imsRegistryUserMapper.selectByUserId(userId);
    }

    public TblImsRegistryUser selectOneByUserId(String userId)
    {
        Assert.hasText(userId, "userId can not be empty");
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            if (tblImsRegistryUsers.size() > 1)
            {
                throw new ImsWebSystemException(ONLY_ONE_REGISTRY_USER, INFO);
            }
            return tblImsRegistryUsers.get(0);
        }
        return null;
    }

   // @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(TblImsRegistryUser record)
    {
        return imsRegistryUserMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TblImsRegistryUser record)
    {
        return  imsRegistryUserMapper.updateByPrimaryKey(record);
    }


    public RegistryUsersRsp getRegistryUserList(String registryUserName, String bpId, String userId, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserMapper.selectAll(registryUserName, bpId, userId);
        PageInfo<TblImsRegistryUser> pageInfo = new PageInfo<>(tblImsRegistryUsers);
        return RegistryUsersRsp.builder().totalNum(pageInfo.getTotal())
                .registryUsers(tblImsRegistryUsers.stream().map(ImsRegistryUser::of).collect(Collectors.toList())).build();
    }

    public List<TblImsRegistryUser> selectAllByStatus(List<Integer> statuslist)
    {
        return imsRegistryUserMapper.selectAllByStatus(statuslist);
    }

    public void updateCompletedStatusToPartiallyCompleted()
    {
        List<Integer> statusList = Lists.newArrayList(COMPLETED.value());
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserMapper.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            tblImsRegistryUsers.stream().forEach(tblImsRegistryUser ->
            {
                TblImsRegistryUser build = TblImsRegistryUser.builder().registryUserId(tblImsRegistryUser.getRegistryUserId())
                        .status(PARTIALLY_COMPLETED.value())
                        .updateTime(LocalDateTime.now()).build();
                imsRegistryUserMapper.updateByPrimaryKeySelective(build);
            });
        }
    }

    private void checkIfTheUserExists(String registryUserName)
    {
        // Determine whether the user exists
        TblImsRegistryUser tblImsRegistryUser = imsRegistryUserMapper.selectByRegistryUserName(registryUserName);
        if (null != tblImsRegistryUser)
        {
            throw new ImsWebSystemException(REGISTRY_USER_EXIST, INFO);
        }

    }

    /**
     * Now only supports one user, create a registry user
     *
     * @param userId
     */
    private void checkIfTheRegistyUserHasBeenCreated(String userId)
    {
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserMapper.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers) && tblImsRegistryUsers.size() >= 1)
        {
            throw new ImsWebSystemException(THE_USER_HAS_CREATED_A_REGISTRY_USER, INFO);
        }
    }

    public List<TblImsRegistryUser> selectByUserName(String userName)
    {
        return imsRegistryUserMapper.selectByUserName(userName);
    }
}
