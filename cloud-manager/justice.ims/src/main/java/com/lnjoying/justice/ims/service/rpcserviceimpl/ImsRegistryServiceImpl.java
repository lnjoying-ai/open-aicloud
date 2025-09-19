package com.lnjoying.justice.ims.service.rpcserviceimpl;

import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import com.lnjoying.justice.ims.domain.model.ImsRegistry3rd;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistry3rdFacade;
import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.ims.facade.ImsRegistryUserFacade;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.Base64Utils;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.service.ims.ImsRegistryService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.ims.db.repo.ImsRegistry3rdRepository.DOCKER_IO_URL;
import static com.lnjoying.justice.schema.common.ErrorCode.REGISTRY_USER_NOT_EXIST;
import static com.lnjoying.justice.schema.common.ErrorCode.User_Not_Grant;
import static com.lnjoying.justice.schema.constant.RoleConstants.*;

/**
 * Rpc implementation class
 *
 * @author merak
 **/

@RpcSchema(schemaId = "imsRegistryService")
@Slf4j
public class ImsRegistryServiceImpl implements ImsRegistryService
{
    @Autowired
    private ImsRegistryFacade imsRegistryFacade;

    @Autowired
    private ImsRegistry3rdFacade imsRegistry3rdFacade;

    @Autowired
    private ImsRegistryUserFacade imsRegistryUserFacade;

    @Autowired
    private CombRpcService combRpcService;


    @Override
    public Registry getRegistry(@ApiParam(name = "registryId") String registryId, @ApiParam(name = "bpId") String bpId, @ApiParam(name = "userId") String userId)
    {
        Assert.hasText(registryId, "registry Id can not be empty");
        Assert.hasText(userId, "user Id can not be empty");
        // Verify user roles
        UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(userId);
        if (Objects.isNull(userInfo))
        {
            throw new ImsWebSystemException(User_Not_Grant, ErrorLevel.INFO);
        }

        String kind = String.valueOf(userInfo.getKind());
        if (!isSystemUser(kind))
        {
            throw new ImsWebSystemException(User_Not_Grant, ErrorLevel.INFO);
        }

        Registry registry = new Registry();
        try
        {
            TblImsRegistry imsRegistry = imsRegistryFacade.getImsRegistry(registryId, null);
            if (Objects.nonNull(imsRegistry))
            {
                registry.setRegistryId(imsRegistry.getRegistryId());
                registry.setRegistryUrl(imsRegistry.getUrl());
                String userName = "";
                String password = "";
                if (isAdmin(kind))
                {
                    userName = imsRegistry.getAdminName();
                    password = imsRegistry.getAdminPasswd();
                }
                else
                {
                    TblImsRegistryUser imsRegistryUser = imsRegistryUserFacade.selectByUserId(userId);
                    if (Objects.isNull(imsRegistryUser))
                    {
                        throw new ImsWebSystemException(REGISTRY_USER_NOT_EXIST, ErrorLevel.INFO);
                    }
                    userName = imsRegistryUser.getRegistryUserName();
                    password = imsRegistryUser.getRegistryUserPassword();
                }
                registry.setRegistryUserName(userName);
                String encodePassword = Base64Utils.encode(AesCryptoUtils.cbcDecryptStr(password));
                registry.setRegistryPassword(encodePassword);
                return registry;
            }
        }
        catch (Exception e)
        {
            log.error("registry exception", e);
        }

        // Third-party registry query
        ImsRegistry3rd registry3rd;
        if (isAdmin(kind))
        {
            registry3rd = imsRegistry3rdFacade.getRegistry3rd(registryId, null, null);
        }
        else if (isBpAdmin(kind))
        {
            registry3rd = imsRegistry3rdFacade.getRegistry3rd(registryId, bpId, null);
        }
        else
        {
            registry3rd = imsRegistry3rdFacade.getRegistry3rd(registryId, bpId, userId);
        }
        if (Objects.nonNull(registry3rd))
        {
            String url = registry3rd.getUrl();
            url = StringUtils.isBlank(url) ? "" : url;
            String accessKey = registry3rd.getAccessKey();
            accessKey = StringUtils.isBlank(accessKey) ? "" : accessKey;
            String accessSecret = registry3rd.getAccessSecret();
            String encodePassword = "";
            if (StringUtils.isNotBlank(accessSecret))
            {
                encodePassword = Base64Utils.encode(AesCryptoUtils.cbcDecryptStr(accessSecret));
            }
            // If the registry URL is docker.io and username password is an empty string, the URL is set to empty string
            if (StringUtils.isNotBlank(url) && DOCKER_IO_URL.equals(url))
            {
                if (StringUtils.isBlank(accessKey) && StringUtils.isBlank(accessSecret))
                {
                    url = "";
                }
            }
            registry.setRegistryId(registry3rd.getRegistryId());
            registry.setRegistryUrl(url);
            registry.setRegistryUserName(accessKey);
            registry.setRegistryPassword(encodePassword);
            return registry;
        }

        return null;
    }

    @Override
    public Registry getRegistryByUrlAndName(@ApiParam(name = "registryUrl")@NotNull String registryUrl, @ApiParam(name = "userName") String userName)
    {
        Assert.hasText(registryUrl, "registry url can not be empty");
        Assert.hasText(userName, "user anme can not be empty");
        Registry registry = new Registry();
        try
        {
            TblImsRegistry imsRegistry = imsRegistryFacade.selectRegistryByUrl(registryUrl);
            if (Objects.nonNull(imsRegistry))
            {
                registry.setRegistryId(imsRegistry.getRegistryId());
                registry.setRegistryUrl(imsRegistry.getUrl());
                String registryUserName = "";
                String registryPassword = "";
                if (userName.equals(imsRegistry.getAdminName()))
                {
                    registryUserName = imsRegistry.getAdminName();
                    registryPassword = imsRegistry.getAdminPasswd();
                }
                else
                {
                    TblImsRegistryUser imsRegistryUser = imsRegistryUserFacade.selectByUserName(userName);
                    if (Objects.nonNull(imsRegistryUser))
                    {
                        registryUserName = imsRegistryUser.getRegistryUserName();
                        registryPassword = imsRegistryUser.getRegistryUserPassword();
                    }

                }
                registry.setRegistryUserName(userName);
                String encodePassword = AesCryptoUtils.cbcDecryptStr(registryPassword);
                registry.setRegistryPassword(Base64Utils.encode(encodePassword));
                return registry;
            }
        }
        catch (Exception e)
        {
            log.error("registry exception", e);
        }

        // Third-party registry
        try {

            TblImsRegistry3rd registry3rd = imsRegistry3rdFacade.selectByUrlAndAccessKey(registryUrl, userName);

            if (Objects.nonNull(registry3rd))
            {
                String url = registry3rd.getUrl();
                String accessSecret = registry3rd.getAccessSecret();
                String encodePassword = "";
                if (StringUtils.isNotBlank(accessSecret))
                {
                    encodePassword = AesCryptoUtils.cbcDecryptStr(accessSecret);
                }

                registry.setRegistryId(registry3rd.getRegistryId());
                registry.setRegistryUrl(url);
                registry.setRegistryUserName(userName);
                registry.setRegistryPassword(Base64Utils.encode(encodePassword));
                return registry;
            }
        }
        catch (Exception e)
        {
            log.error("registry3rd exception", e);
        }

        return null;

    }

    public String getRegistryUrl(@ApiParam(name = "registryId")@NotNull String registryId)
    {
        String registryUrl = imsRegistryFacade.getRegistryUrlById(registryId);
        if (StringUtils.isNotBlank(registryUrl))
        {
            return registryUrl;
        }

        registryUrl = imsRegistry3rdFacade.getRegistryUrlById(registryId);
        if (StringUtils.isNotBlank(registryUrl))
        {
            return registryUrl;
        }

        return "";
    }
}
