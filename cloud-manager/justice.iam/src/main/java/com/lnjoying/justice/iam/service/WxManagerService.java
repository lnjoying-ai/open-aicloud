package com.lnjoying.justice.iam.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.lnjoying.justice.iam.common.constant.UMStatus;
import com.lnjoying.justice.iam.common.constant.UserLevel;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblRoleInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.iam.domain.dto.response.user.UserRsp;
import com.micro.core.common.Pair;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.lnjoying.justice.schema.common.ErrorCode.GEN_USER_NAME_ERROR;
import static com.lnjoying.justice.schema.common.ErrorCode.WX_LOGIN_VALIDATION_EXCEPTION;
import static com.lnjoying.justice.iam.common.constant.WeixinConsts.WA_USER_SESSION;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.getHttpServletRequest;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/19 15:01
 */

@Service("wxManagerService")
@Slf4j
public class WxManagerService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BpRepository bpRepository;


    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private Pbkdf2PasswordEncoder passwordEncoder;

    @Value("${jwtkey}")
    private String jwtkey;

    @Value("${default-weixin-password:joying@365}")
    private String defaultWeixinPassword;

    @Autowired
    private WxMaProperties wxMaProperties;

    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private ProjectService projectService;

    public Pair<WeixinUserStatus, Pair<String, UserRsp>> getTokenByCode(String wxLoginCode)
    {

        WxMaJscode2SessionResult sessionInfo = null;
        try
        {
             sessionInfo = wxMaService.getUserService().getSessionInfo(wxLoginCode);
            //wxMaService.getWxMaConfig()
            log.info("session info: {}", sessionInfo);
        }
        catch (WxErrorException e)
        {
            log.error("get session info from code:{} error:{}", wxLoginCode, e);
            throw new WebSystemException(WX_LOGIN_VALIDATION_EXCEPTION, ErrorLevel.ERROR);
        }

        Pair<WeixinUserStatus, Pair<String, UserRsp>> res = new Pair<>();

        if (Objects.nonNull(sessionInfo))
        {
            WeixinUserStatus weixinUserStatus = null;
            TblUserInfo tblUserInfo = userRepository.selectByOpenId(sessionInfo.getOpenid());
            if (Objects.nonNull(tblUserInfo))
            {
                weixinUserStatus = WeixinUserStatus.UPDATE;
            }
            else
            {
                // create temporary user

                tblUserInfo = createTemporaryUser(sessionInfo);
                TblBpInfo tblBpInfo = createTemporaryBp(sessionInfo, tblUserInfo);
                tblUserInfo.setBpId(tblBpInfo.getBpId());
                insertBpAndUser(tblBpInfo, tblUserInfo);
                projectService.insertDefaultProject(tblBpInfo.getBpId(), tblBpInfo.getBpName());
                // init tenant role
                userManagerService.initTenantRoleV2(tblUserInfo);
                weixinUserStatus = WeixinUserStatus.NEW;
            }

            storageSession(sessionInfo, tblUserInfo.getUserId());
            String token = buildToken(tblUserInfo);
            res.setKey(weixinUserStatus);
            Pair<String, UserRsp> userInfo = new Pair<>();
            userInfo.setKey(token);
            UserRsp userRsp  = new UserRsp();
            userRsp.setResponse(tblUserInfo);
            userInfo.setValue(userRsp);
            res.setValue(userInfo);
            return res;
        }

        log.error("get session info from code:{} fail, no session info", wxLoginCode);
        throw new WebSystemException(WX_LOGIN_VALIDATION_EXCEPTION, ErrorLevel.ERROR);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertBpAndUser(TblBpInfo tblBpInfo, TblUserInfo tblUserInfo)
    {
        bpRepository.insertBp(tblBpInfo);
        userRepository.insertUser(tblUserInfo);
    }


    private void storageSession(WxMaJscode2SessionResult sessionInfo, String userId)
    {
        RedisUtil.set(String.format(WA_USER_SESSION, wxMaProperties.getConfigStorage().getKeyPrefix(), userId), JsonUtils.toJson(sessionInfo));
    }

    private TblUserInfo createTemporaryUser(WxMaJscode2SessionResult sessionInfo)
    {
        TblUserInfo tblUserInfo;
        tblUserInfo = new TblUserInfo();
        tblUserInfo.setUserId(Utils.assignUUId());
        tblUserInfo.setUserName(genUserName(10));
        tblUserInfo.setWeixin(sessionInfo.getOpenid());
        tblUserInfo.setAccessFrom(TblUserInfo.AccessFromType.WEIXIN.value());
        tblUserInfo.setPassword(passwordEncoder.encode(defaultWeixinPassword));
        tblUserInfo.setStatus(UMStatus.ACTIVE);
        tblUserInfo.setIsAllowed(true);
        tblUserInfo.setKind(UserKindEnum.BP_ADMIN.getCode());
        tblUserInfo.setLevel(UserLevel.LEVEL1);
        tblUserInfo.setCreateTime(new Date());
        tblUserInfo.setUpdateTime(new Date());
        return tblUserInfo;
    }

    private TblBpInfo createTemporaryBp(WxMaJscode2SessionResult sessionInfo, TblUserInfo tblUserInfo)
    {
        TblBpInfo tblBpInfo = new TblBpInfo();
        //tblBpInfo.setBpId(bpRawReq.getLicense_id());
        tblBpInfo.setBpId(Utils.assignUUId());
        tblBpInfo.setBpName(genBpName(10));
        tblBpInfo.setDescription("");
        tblBpInfo.setLicenseId("");
        tblBpInfo.setStatus(0);
        tblBpInfo.setMasterUser(tblUserInfo.getUserName());
        tblBpInfo.setWebsite("");
        tblBpInfo.setCreateTime(new Date());
        tblBpInfo.setUpdateTime(tblBpInfo.getCreateTime());
        return tblBpInfo;
    }


    private String genUserName(int count)
    {
        String userName = RandomName.getRandomName(count);
        TblUserInfo tblUserInfo = userRepository.getUserByUserName(userName);

        if (Objects.nonNull(tblUserInfo))
        {
            if (count < 64)
            {
                return genUserName(count + 1);
            }
            else
            {
                log.info("unable to generate appropriate username");
                throw new WebSystemException(GEN_USER_NAME_ERROR, ErrorLevel.ERROR);
            }
        }
        else
        {
            return userName;
        }

    }


    private String genBpName(int count)
    {
        String bpName = RandomName.getRandomName(count);
        TblBpInfo tblBpInfo = bpRepository.selectByBpName(bpName);

        if (Objects.nonNull(tblBpInfo))
        {
            if (count < 64)
            {
                return genBpName(count + 1);
            }
            else
            {
                log.info("unable to generate appropriate username");
                throw new WebSystemException(GEN_USER_NAME_ERROR, ErrorLevel.ERROR);
            }
        }
        else
        {
            return bpName;
        }

    }

    public String buildToken(TblUserInfo userInfo)
    {
        Map<String, Object> headerInfo = new HashMap<>();
        String userAgent = getHttpServletRequest().getHeader("user-agent");
        headerInfo.put("user-agent", userAgent);
        //headerInfo.put("remote",  request.getRemoteHost());
        headerInfo.put(UserHeadInfo.USERNAME, userInfo.getUserName());
        headerInfo.put(UserHeadInfo.USERID, userInfo.getUserId());
        //
        List<TblIamRole> rolePos = userRepository.getRolesByUserId(userInfo.getUserId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        rolePos.forEach(rolePo -> authorities.add(new SimpleGrantedAuthority("ROLE_" + rolePo.getPlatform() + "_" + rolePo.getRole())));

        headerInfo.put(UserHeadInfo.AUTIORITIES, authorities);
        headerInfo.put(UserHeadInfo.USERKIND, UserKindEnum.fromCode(userInfo.getKind()).getMessage());

        String bpId = userInfo.getBpId();
        String bpName  = "";
        if (StringUtils.isNotBlank(bpId))
        {
            TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);
            bpId = (tblBpInfo != null)   ? tblBpInfo.getBpId() : "";
            bpName = (tblBpInfo != null) ? tblBpInfo.getBpName() : "";
        }
        headerInfo.put(UserHeadInfo.BPID, bpId);
        headerInfo.put(UserHeadInfo.BpName, bpName);

        String token = JwtUtils.getNewJwtToken(userInfo.getUserName(),
                headerInfo,
                WebConstants.LNJOYING_TOKEN_INDATE,
                jwtkey);

        return token;
    }

    public enum WeixinUserStatus
    {
        /**
         * newly created weixin user
         */
        NEW,

        /**
         * weixin user status to be updated
         */
        UPDATE;
    }
}
