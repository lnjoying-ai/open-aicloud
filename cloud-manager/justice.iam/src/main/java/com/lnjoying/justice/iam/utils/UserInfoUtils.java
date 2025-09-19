package com.lnjoying.justice.iam.utils;

import com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.iam.service.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.isSystemUser;


/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/8/18 10:59
 */

@Slf4j
@Component
public class UserInfoUtils
{

    private static UserRepository userRepository;

    private static UserManagerService userManagerService;

    public static boolean isAdmin(String userId)
    {
        try
        {
            if (StringUtils.isBlank(userId))
            {
                return false;
            }

            TblUserInfo userInfo = userRepository.getUserById(userId);
            if (Objects.isNull(userInfo))
            {
                return false;
            }

            String kind = String.valueOf(userInfo.getKind());
            if (!isSystemUser(kind))
            {
                return false;
            }
            if (ServiceCombRequestUtils.isAdmin(kind))
            {
                return true;
            }

            return false;
        }
        catch (Exception e)
        {
            log.error("is admin by userId:{} error:{}", userId, e);
        }

        return false;

    }

    public static boolean checkRole(String role)
    {

        List<String> roles = userManagerService.getUserRolesByUserId(getUserId());
        if (!CollectionUtils.isEmpty(roles))
        {
            if (roles.contains(role))
            {
                return true;
            }
        }

        return false;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository)
    {
        UserInfoUtils.userRepository = userRepository;
    }

    @Autowired
    public void setUserManagerService(UserManagerService userManagerService)
    {
        UserInfoUtils.userManagerService = userManagerService;
    }
}
