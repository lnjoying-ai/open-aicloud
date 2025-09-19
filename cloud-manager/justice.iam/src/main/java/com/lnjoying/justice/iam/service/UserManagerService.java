package com.lnjoying.justice.iam.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.binarywang.spring.starter.wxjava.miniapp.properties.WxMaProperties;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.commonweb.util.JwtUtils;
import com.lnjoying.justice.iam.config.CommonProperties;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.db.repo.AuthzRepository;
import com.lnjoying.justice.iam.domain.dto.request.user.*;
import com.lnjoying.justice.iam.utils.VerificationCodeUtil;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.constant.UserKindEnum;
import com.lnjoying.justice.iam.common.constant.UMStatus;
import com.lnjoying.justice.iam.common.constant.UserLevel;
import com.lnjoying.justice.iam.config.SmsConfig;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.lnjoying.justice.iam.domain.dto.request.bp.BpContactsInfo;
import com.lnjoying.justice.iam.domain.dto.request.wx.UserInfoReq;
import com.lnjoying.justice.iam.domain.dto.response.role.ComponentRoleDto;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleDto;
import com.lnjoying.justice.iam.domain.dto.response.user.QueryUsersRsp;
import com.lnjoying.justice.iam.domain.dto.response.user.UniquenessRsp;
import com.lnjoying.justice.iam.domain.dto.response.user.UserRsp;
import com.lnjoying.justice.iam.domain.model.UserContactInfo;
import com.lnjoying.justice.iam.domain.model.search.UserSearchCritical;
import com.lnjoying.justice.schema.constant.WebConstants;
import com.micro.core.common.Pair;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.iam.common.constant.WeixinConsts.WA_USER_SESSION;


@Service("userManagerService")
public class UserManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagerService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Pbkdf2PasswordEncoder passwordEncoder;

    @Autowired
    private SmsConfig smsConfig;

    @Autowired
    private BpRepository bpRepository;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxMaProperties wxMaProperties;

    @Autowired
    private AuthzRepository authzRepository;

    @Autowired
    private AuthzService authzService;

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private ProjectService projectService;

    @Value("${reset-pwd-max-attempt:3}")
    private int RESET_PWD_MAX_ATTEMPT;

    @Value("${reset-pwd-lock-sec:300}")
    private int RESET_PWD_LOCK_SEC;

    public static Pattern rolePattern = Pattern.compile("([^_]*)_(.*)");

    /**
     * register user info by telephone, verification code and so on.
     *
     * @param reqParam reqParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object addUser(UserAddReq reqParam)
    {
        try
        {
            LOGGER.info("Begin register user");
            checkKind(reqParam);
            checkIfBpExists(reqParam);
            UserContactInfo userContactInfo = reqParam.getContact_info();
            if (userContactInfo != null )
            {
                if (!StringUtils.isEmpty(userContactInfo.getPhone())
                        && userRepository.getUserByPhone(userContactInfo.getPhone()) != null)
                {
                    throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.INFO);
                }

                if (!StringUtils.isEmpty(userContactInfo.getEmail())
                        && userRepository.getUserByEmail(userContactInfo.getEmail()) != null)
                {
                    throw new WebSystemException(ErrorCode.EmailOccupied, ErrorLevel.INFO);
                }
            }

            if (StringUtils.isEmpty(reqParam.getName()))
            {
                throw new WebSystemException(ErrorCode.InvalidUsername, ErrorLevel.INFO);
            }

            if (!StringUtils.isEmpty(reqParam.getName())
                    && userRepository.getUserByUserName(reqParam.getName()) != null)
            {
                throw new WebSystemException(ErrorCode.DuplicateUser, ErrorLevel.INFO);
            }

            TblUserInfo tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId(Utils.assignUUId());
            if (!StringUtils.isEmpty(reqParam.getBp_id()))
            {
                if (UserKindEnum.BP_ADMIN.getCode() == reqParam.getKind() || UserKindEnum.BP_SUB_USER.getCode() == reqParam.getKind())
                {
                    tblUserInfo.setBpId(reqParam.getBp_id());
                }
            }
            tblUserInfo.setUserName(reqParam.getName());
            //tblUserInfo.setIsAllowed(reqParam.getIs_allowed() == 0 ? false : true);
            // default true, there is no place to set up from the front end
            tblUserInfo.setIsAllowed(true);
            tblUserInfo.setStatus(reqParam.getStatus());
            tblUserInfo.setKind(reqParam.getKind());
            tblUserInfo.setLevel(reqParam.getLevel());
            tblUserInfo.setAccessFrom(TblUserInfo.AccessFromType.ADMINADDED.value());
            tblUserInfo.setInvitationCode(genInvitationCode());

            if (userContactInfo != null)
            {
                if (!StringUtils.isEmpty(userContactInfo.getEmail()))
                {
                    tblUserInfo.setEmail(userContactInfo.getEmail());
                }

                if (!StringUtils.isEmpty(userContactInfo.getPhone()))
                {
                    tblUserInfo.setPhone(userContactInfo.getPhone());
                }

                if (!StringUtils.isEmpty(userContactInfo.getAddress()))
                {
                    tblUserInfo.setAddress(userContactInfo.getAddress());
                }
            }

            int result = 0;

            tblUserInfo.setGender(reqParam.getGender());
            tblUserInfo.setPassword(passwordEncoder.encode(reqParam.getPassword()));
            tblUserInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
            tblUserInfo.setUpdateTime(tblUserInfo.getCreateTime());
            result = userRepository.insertUser(tblUserInfo);

            // init tenant role
            initTenantRoleV2(tblUserInfo);
            setBpMasterIfNeed(tblUserInfo, false);

            if (result > 0)
            {
                Map<String, String> retValue = new HashMap<>();
                retValue.put("id", tblUserInfo.getUserId());
                return retValue;
            }
            else
            {
                LOGGER.error("User register failed");
                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
            }
        }
        catch (DuplicateKeyException e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public Object register(UserRegReq reqParam)
    {
        try
        {
            LOGGER.info("Begin register user");

            UserContactInfo userContactInfo = reqParam.getContact_info();
            if (userContactInfo != null )
            {
                if (!StringUtils.isEmpty(userContactInfo.getPhone())
                        && userRepository.getUserByPhone(userContactInfo.getPhone()) != null)
                {
                    throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.INFO);
                }

                if (!StringUtils.isEmpty(userContactInfo.getEmail())
                        && userRepository.getUserByEmail(userContactInfo.getEmail()) != null)
                {
                    throw new WebSystemException(ErrorCode.EmailOccupied, ErrorLevel.INFO);
                }
            }

            if (StringUtils.isEmpty(reqParam.getName()))
            {
                throw new WebSystemException(ErrorCode.InvalidUsername, ErrorLevel.INFO);
            }

            if (!StringUtils.isEmpty(reqParam.getName())
                    && userRepository.getUserByUserName(reqParam.getName()) != null)
            {
                throw new WebSystemException(ErrorCode.DuplicateUser, ErrorLevel.INFO);
            }

            String vrfc = getVrfc();
            String inviterId = getInviterId(vrfc, reqParam.getInvitation_code());

            TblUserInfo tblUserInfo = new TblUserInfo();
            tblUserInfo.setUserId(Utils.assignUUId());
            tblUserInfo.setUserName(reqParam.getName());
            tblUserInfo.setIsAllowed(true);

            if (userContactInfo != null)
            {
                if (!StringUtils.isEmpty(userContactInfo.getEmail()))
                {
                    tblUserInfo.setEmail(userContactInfo.getEmail());
                }

                if (!StringUtils.isEmpty(userContactInfo.getPhone()))
                {
                    tblUserInfo.setPhone(userContactInfo.getPhone());
                }

                if (!StringUtils.isEmpty(userContactInfo.getAddress()))
                {
                    tblUserInfo.setAddress(userContactInfo.getAddress());
                }
            }

            tblUserInfo.setGender(reqParam.getGender());
            tblUserInfo.setKind(UserKindEnum.BP_ADMIN.getCode());

            if (commonProperties.getRegisterMode().equalsIgnoreCase("admin-verify") && StringUtils.isEmpty(vrfc))
            {
                tblUserInfo.setStatus(UMStatus.INACTIVE);
            }
            else
            {
                tblUserInfo.setStatus(UMStatus.ACTIVE);
            }

            if (StringUtils.isEmpty(inviterId))
            {
                tblUserInfo.setAccessFrom(TblUserInfo.AccessFromType.REGISTER.value());
            }
            else
            {
                tblUserInfo.setAccessFrom(TblUserInfo.AccessFromType.INVITE.value());
                tblUserInfo.setInviter(inviterId);
            }
            tblUserInfo.setInvitationCode(genInvitationCode());

            tblUserInfo.setLevel(UserLevel.LEVEL1);
            tblUserInfo.setPassword(passwordEncoder.encode(reqParam.getPassword()));
            tblUserInfo.setCreateTime(Utils.buildDate(System.currentTimeMillis()));
            tblUserInfo.setUpdateTime(tblUserInfo.getCreateTime());

            TblBpInfo tblBpInfo = createTemporaryBp(tblUserInfo);
            tblUserInfo.setBpId(tblBpInfo.getBpId());
            insertBpAndUser(tblBpInfo, tblUserInfo);
            projectService.insertDefaultProject(tblBpInfo.getBpId(), tblBpInfo.getBpName());

            // Initialize user role
            initTenantRoleV2(tblUserInfo);

            Map<String, String> retValue = new HashMap<>();
            retValue.put("id", tblUserInfo.getUserId());
            return retValue;
        }
        catch (DuplicateKeyException e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    /**
     * whether the verification code is correct.
     *
     * @param verificationCode verificationCode
     * @param telephone telephone
     * @return
     */
    private boolean verifyVerificationCode(String verificationCode, String telephone)
    {
        String code = RedisUtil.get(RedisCacheField.AUTH_VER_CODE, telephone);
        if (code != null && verificationCode.equals(code))
        {
            return true;
        }
        return false;
    }

    public void retrievePassword(RetrievePasswordReq retrievePasswordReq)
    {
        LOGGER.info("Begin retrieve password");
        String telephone = retrievePasswordReq.getPhone();
        String email = retrievePasswordReq.getEmail();
        String verificationCode = retrievePasswordReq.getVerification_code();
        String method = Optional.ofNullable(telephone).orElse(email);

        if (StringUtils.isBlank(method))
        {
            LOGGER.error("both email and phone are all empty.");
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        } else if (StringUtils.isBlank(verificationCode))
        {
            LOGGER.error("verificationCode is empty");
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }

        boolean resetPwdLocked = VerificationCodeUtil.checkIfResetPwdLocked(telephone, email);
        if (resetPwdLocked)
        {
            // 因为多次输入错误，重置密码操作已被锁定一段时间
            LOGGER.error("The reset password operation is temporarily locked due to multiple input errors");
            throw new WebSystemException(OPERATION_TEMPORARILY_LOCKED, ErrorLevel.ERROR);
        }

        if (!verifyVerificationCode(verificationCode, method))
        {
            LOGGER.error("verification code is error ");
            recordUserInputErrorTimes(method);
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }

        TblUserInfo tblUserInfo = null;
        if (telephone != null)
        {
            //username is not exit
            tblUserInfo = userRepository.getUserByPhone(telephone);

            if (tblUserInfo == null)
            {
                recordUserInputErrorTimes(telephone);
                LOGGER.error("Telephone not exist");
                throw new WebSystemException(ErrorCode.PHONE_NOT_EXIST, ErrorLevel.ERROR);
            }
        }
        else if (email != null)
        {
            tblUserInfo = userRepository.getUserByEmail(email);
            if (tblUserInfo == null)
            {
                LOGGER.error("email not exist");
                recordUserInputErrorTimes(email);
                throw new WebSystemException(ErrorCode.EMAIL_NOT_EXIST, ErrorLevel.ERROR);
            }
        }

        int result = 0;
        try
        {
            tblUserInfo.setPassword(passwordEncoder.encode(retrievePasswordReq.getNew_password()));
            result += userRepository.updateUserInfo(tblUserInfo);
        } catch (Exception e) {
            LOGGER.error("Database Operate Exception: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }

        if (result > 0)
        {
            LOGGER.info("Modify password success");
            RedisUtil.delete(RedisCacheField.RESET_PWD_LOCK_SEC + method,
                    RedisCacheField.RESET_PWD_ERROR_TIMES + method
            );
        }
        else
        {
            LOGGER.error("Modify password failed");
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    private void recordUserInputErrorTimes(String userIdentity)
    {
        // 记录错误次数
        Long inputErrorTimes = RedisUtil.incrByWithTtl(RedisCacheField.RESET_PWD_ERROR_TIMES + userIdentity, 1, RESET_PWD_LOCK_SEC);
        if (inputErrorTimes != null && inputErrorTimes.longValue() >= RESET_PWD_MAX_ATTEMPT)
        {
            RedisUtil.set(RedisCacheField.RESET_PWD_LOCK_SEC + userIdentity, "reset_pwd_locked", RESET_PWD_LOCK_SEC);
            // 因为多次输入错误，重置密码操作已被锁定一段时间
            LOGGER.error("The reset password operation is temporarily locked due to multiple input errors");
            throw new WebSystemException(OPERATION_TEMPORARILY_LOCKED, ErrorLevel.ERROR);
        }
    }

    public void updatePassword(String userName, UpdatePasswordReq updatePasswordReq)
    {
        LOGGER.info("Begin retrieve password");
        TblUserInfo tblUserInfo = userRepository.getUserByUserName(userName);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        if (! passwordEncoder.matches(updatePasswordReq.getOld_password(), tblUserInfo.getPassword()))
        {
            throw new WebSystemException(ErrorCode.InvalidOldPasswd, ErrorLevel.INFO);
        }

        int result = 0;
        try
        {
            tblUserInfo.setPassword(passwordEncoder.encode(updatePasswordReq.getNew_password()));
            result += userRepository.updateUserInfo(tblUserInfo);
        } catch (Exception e) {
            LOGGER.error("Database Operate Exception: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }

        if (result > 0)
        {
            LOGGER.info("Modify password success");
            return;
        }
        else
        {
            LOGGER.error("Modify password failed");
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    /**
     * Verify that the username and telephone number and mail address exist.
     *
     * @param uniqueRequest UniqueReq
     * @return
     */
    public UniquenessRsp uniqueness(UniqueReq uniqueRequest)
    {
        String mailAddress = uniqueRequest.getEmail();
        String telephone = uniqueRequest.getPhone();
        String username = uniqueRequest.getUsername();
        UniquenessRsp uniquenessResponse = new UniquenessRsp();

        if (mailAddress != null && mailAddress.length() > 1 && userRepository.getUserByEmail(mailAddress) != null)
        {
            uniquenessResponse.setMailAddress(true);
        }

        if (telephone != null && telephone.length() > 1 && userRepository.getUserByPhone(telephone) != null)
        {
            uniquenessResponse.setTelephone(true);
        }

        if (username != null && username.length() > 1 && userRepository.getUserByUserName(username) != null)
        {
            uniquenessResponse.setUsername(true);
        }

        if (username != null && username.length() > 1 && userRepository.getUserByEmail(username) != null)
        {
            uniquenessResponse.setUsername(true);
        }

        if (username != null && username.length() > 1 && userRepository.getUserByPhone(username) != null)
        {
            uniquenessResponse.setUsername(true);
        }

        return uniquenessResponse;
    }

    /**
     * delete user by id.
     *
     * @param userId
     */
    public boolean deleteUser(String userId)
    {
        try
        {
            TblUserInfo tblUserInfo = userRepository.getUserById(userId);
            if (tblUserInfo == null)
            {
                throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
            }
            tblUserInfo.setStatus(UMStatus.REMOVED);
            String userName = tblUserInfo.getUserId() + tblUserInfo.getUserName();
            if (userName.length()>63) userName = userName.substring(0,63);
            tblUserInfo.setUserName(userName);

            if (tblUserInfo.getPhone() != null)
            {
                String phone = tblUserInfo.getUserId() + tblUserInfo.getPhone();
                if (phone.length() > 31) phone = phone.substring(0, 31);
                tblUserInfo.setPhone(phone);
            }

            if (tblUserInfo.getEmail() != null)
            {
                String email = tblUserInfo.getUserId() + tblUserInfo.getEmail();
                if (email.length() > 63) email = email.substring(0, 63);
                tblUserInfo.setEmail(email);
            }
            userRepository.updateUserInfo(tblUserInfo);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateUser(String userId, UserUpdateReq req)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.ERROR);
        }

        if (!isAdmin())
        {
            if (tblUserInfo.getKind() != req.getKind())
            {
                throw new WebSystemException(ErrorCode.MODIFICATION_OF_KIND_IS_NOT_ALLOWED, ErrorLevel.ERROR);
            }
        }

        UserContactInfo userContactInfo = req.getContact_info();

        if (userContactInfo != null )
        {
            TblUserInfo tblUserInfoT = null;
            if (!StringUtils.isEmpty(userContactInfo.getPhone()) && ! StringUtils.equals(tblUserInfo.getPhone(), userContactInfo.getPhone()))
            {
                tblUserInfoT = userRepository.getUserByPhone(userContactInfo.getPhone());
            }

            if (tblUserInfoT != null && ! tblUserInfoT.getUserId().equals(userId))
            {
                throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.INFO);
            }

            if (!StringUtils.isEmpty(userContactInfo.getEmail()) && ! StringUtils.equals(tblUserInfo.getEmail(), userContactInfo.getEmail()))
            {
                tblUserInfoT = userRepository.getUserByEmail(userContactInfo.getEmail());
            }

            if (tblUserInfoT != null && ! tblUserInfoT.getUserId().equals(userId))
            {
                throw new WebSystemException(ErrorCode.EmailOccupied, ErrorLevel.INFO);
            }
        }

        if (req.getName() != null && ! StringUtils.equals(tblUserInfo.getUserName(), req.getName()))
        {
            TblUserInfo tblUserInfoT2 = userRepository.getUserByUserName(req.getName());
            if (tblUserInfoT2 != null && !tblUserInfoT2.getUserId().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
            }
            tblUserInfo.setUserName(req.getName());
        }

        tblUserInfo.setBpId(req.getBp_id());
        tblUserInfo.setIsAllowed(req.getIs_allowed() == 0 ? false : true);

        if (userContactInfo != null)
        {
            if (!StringUtils.isEmpty(userContactInfo.getEmail()))
            {
                tblUserInfo.setEmail(userContactInfo.getEmail());
            }

            if (!StringUtils.isEmpty(userContactInfo.getPhone()))
            {
                tblUserInfo.setPhone(userContactInfo.getPhone());
            }

            if (!StringUtils.isEmpty(userContactInfo.getAddress()))
            {
                tblUserInfo.setAddress(userContactInfo.getAddress());
            }
        }

        int result = 0;

        tblUserInfo.setGender(req.getGender());
        Integer kind = req.getKind();
        boolean needUpdateRole = false;
        if (Objects.nonNull(kind) && Objects.isNull(tblUserInfo.getKind()))
        {
            needUpdateRole = true;
        }
        else if (Objects.nonNull(kind) && Objects.nonNull(tblUserInfo.getKind()) && kind != tblUserInfo.getKind())
        {
            needUpdateRole = true;
        }
        tblUserInfo.setKind(kind);
        tblUserInfo.setLevel(req.getLevel());
        tblUserInfo.setStatus(req.getStatus());
        if (! StringUtils.isEmpty(req.getPassword()))
        {
            tblUserInfo.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (StringUtils.isNotEmpty(req.getInviter()))
        {
            tblUserInfo.setInviter(req.getInviter());
        }

        result = userRepository.updateUserInfo(tblUserInfo);

        if (needUpdateRole) {
            //userRepository.deleteUserRoleByUserId(tblUserInfo.getUserId());
            //initTenantRoleV2(tblUserInfo);
        }
        setBpMasterIfNeed(tblUserInfo, true);
        if (result > 0)
        {
            return ErrorCode.SUCCESS.getCode();
        }
        else
        {
            LOGGER.error("User register failed");
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
        }

    }

    public int updateCurrentUser(String userId, CurrentUserUpdateReq req)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        int result = 0;

        tblUserInfo.setGender(req.getGender());
        tblUserInfo.setAddress(req.getAddress());
        result = userRepository.updateUserInfo(tblUserInfo);

        if (result > 0)
        {
            return ErrorCode.SUCCESS.getCode();
        }
        else
        {
            LOGGER.error("User register failed");
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
        }

    }
    /**
     * update user status.
     *
     * @param userId user id
     * @param allowFlag allow flag
     * @return String or FormatRespDto
     */
    public int updateUserStatus(String userId, boolean allowFlag)
    {
        return userRepository.updateUserStatus(userId, allowFlag);
    }

    public UserRsp getUserDtoInfo(String userId)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);

        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        List<TblIamRole> tblRoleInfoList = userRepository.getRolesByUserId(userId);

        UserRsp userRsp = new UserRsp();
        userRsp.setPermission(tblRoleInfoList);

        String bpId = tblUserInfo.getBpId();
        if (StringUtils.isNotBlank(bpId))
        {
            TblBpInfo bpInfo = bpRepository.getBpInfo(bpId);
            if (Objects.nonNull(bpInfo))
            {
                userRsp.setBpName(bpInfo.getBpName());
            }
        }

        userRsp.setResponse(tblUserInfo);
        return userRsp;
    }

    TblUserInfoExample setUserInfoExample(UserSearchCritical critical)
    {
        TblUserInfoExample example = new TblUserInfoExample();
        TblUserInfoExample.Criteria criteria = example.createCriteria();
        if (isAdmin() && StringUtils.isNotBlank(critical.getQueryBpId()))
        {
            criteria.andBpIdEqualTo(critical.getQueryBpId());
        }
        if (critical.getName() != null) criteria.andUserNameLike(critical.getName() + "%");
        if (critical.getBpId() != null) criteria.andBpIdEqualTo(critical.getBpId());
        if (critical.getUserId() != null) criteria.andUserIdEqualTo(critical.getUserId());
        criteria.andStatusNotEqualTo(UMStatus.REMOVED);
        example.setOrderByClause("create_time desc");
        return example;
    }

    public Object getUserDtoInfos(UserSearchCritical critical)
    {
    	try
		{
			TblUserInfoExample example = setUserInfoExample(critical);
			QueryUsersRsp queryUserRsp = new QueryUsersRsp();

			Long total_num = userRepository.countUsersByExample(example);
			queryUserRsp.setTotal_num(total_num.intValue());
			if (total_num < 1)
			{
				return queryUserRsp;
			}

			int begin = ((critical.getPageNum()-1) * critical.getPageSize());
            example.setOrderByClause("create_time desc");
			//example.setOrderByClause("update_time desc");
			example.setStartRow(begin);
			example.setPageSize(critical.getPageSize());

			List<TblUserInfo> userInfoList = userRepository.getUsersByExample(example);
			if (userInfoList == null)
			{
				return queryUserRsp;
			}

			List<UserRsp> userRspList = new ArrayList<>();
            userInfoList.parallelStream().sequential().forEach(userInfo -> {
                UserRsp userRsp = new UserRsp();
                userRsp.setResponse(userInfo);

                userRsp.setBpName(getBpNameByBpId(userInfo.getBpId()));
                userRspList.add(userRsp);
            });

			queryUserRsp.setUsers(userRspList);
			return queryUserRsp;
		}
    	catch (Exception e)
		{
			throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.ERROR);
		}

    }

    public List<RoleDto> getRolesByUserId(String userId)
    {
        List<TblIamRole> tblRoleInfoList = userRepository.getRolesByUserId(userId);
        return getRoleDtos(tblRoleInfoList);
    }

    public List<String> getUserRolesByUserId(String userId)
    {
        List<TblIamRole> tblRoleInfoList = userRepository.getRolesByUserId(userId);

        return getUserRoleList(tblRoleInfoList);
    }


    public List<String> getUserRolesByUserIdAndServiceCode(String userId, String serviceCode)
    {
        List<TblIamRole> tblRoleInfoList = userRepository.getUserRolesByUserIdAndServiceCode(userId, serviceCode);

        return getUserRoleList(tblRoleInfoList);
    }

    public List<TblUserInfo> getUserInfosByRoleAndServiceCode(String role, String serviceCode)
    {
        return userRepository.getUserInfosByRoleAndServiceCode(role, serviceCode);
    }

    public List<ComponentRoleDto> getRoles()
    {
        List<TblIamRole> tblRoleInfoList = userRepository.getWholeRoles();
        List<ComponentRoleDto> roleDtoList = new ArrayList<>();
        if (tblRoleInfoList == null)
        {
            return roleDtoList;
        }

        Map<String, ComponentRoleDto> componentRoleDtoMap = new HashMap<>();

        for (TblIamRole tblRoleInfo : tblRoleInfoList)
        {
            ComponentRoleDto componentRoleDto = componentRoleDtoMap.get(tblRoleInfo.getPlatform());
            if (componentRoleDto != null)
            {
                componentRoleDto.getRoles().add(tblRoleInfo.getRole());
                continue;
            }
            componentRoleDto = new ComponentRoleDto();
            componentRoleDto.setPlatform(tblRoleInfo.getPlatform());
            List<String> roles = new ArrayList<>();
            roles.add(tblRoleInfo.getRole());
            componentRoleDto.setRoles(roles);
            componentRoleDtoMap.put(tblRoleInfo.getPlatform(), componentRoleDto);
        }
        return new ArrayList<ComponentRoleDto>(componentRoleDtoMap.values());
    }

    public void updateUserPhone(String userId, PhoneRawInfo phoneRawInfo)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        String patchVerCode = RedisUtil.get(RedisCacheField.PATCH_VER_CODE, phoneRawInfo.getPhone());
        if (StringUtils.isBlank(patchVerCode) || !patchVerCode.equals(phoneRawInfo.getVerification_code()))
        {
            throw new WebSystemException(ErrorCode.Invalid_validateCode, ErrorLevel.INFO);
        }

        if (!passwordEncoder.matches(phoneRawInfo.getPassword(), tblUserInfo.getPassword()))
        {
            throw new WebSystemException(ErrorCode.InvalidOldPasswd, ErrorLevel.INFO);
        }

        TblUserInfo tblExistUser = userRepository.getUserByPhone(phoneRawInfo.getPhone());
        if (tblExistUser != null)
        {
            throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.INFO);
        }
        tblUserInfo.setPhone(phoneRawInfo.getPhone());
        userRepository.updateUserInfo(tblUserInfo);
        RedisUtil.delete(RedisCacheField.PATCH_VER_CODE + phoneRawInfo.getPhone());
    }

    public void updateUserEmail(String userId, EmailRawInfo emailRawInfo)
    {
        TblUserInfo tblUserInfo = userRepository.getUserById(userId);
        if (tblUserInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        String patchVerCode = RedisUtil.get(RedisCacheField.PATCH_VER_CODE, emailRawInfo.getEmail());
        if (Strings.isBlank(patchVerCode) || !patchVerCode.equals(emailRawInfo.getVerification_code()))
        {
            throw new WebSystemException(ErrorCode.Invalid_validateCode, ErrorLevel.INFO);
        }

        if (!passwordEncoder.matches(emailRawInfo.getPassword(), tblUserInfo.getPassword()))
        {
            throw new WebSystemException(ErrorCode.InvalidOldPasswd, ErrorLevel.INFO);
        }

        TblUserInfo tblExistUser = userRepository.getUserByEmail(emailRawInfo.getEmail());
        if (tblExistUser != null)
        {
            throw new WebSystemException(ErrorCode.EmailOccupied, ErrorLevel.INFO);
        }
        tblUserInfo.setEmail(emailRawInfo.getEmail());
        userRepository.updateUserInfo(tblUserInfo);
        RedisUtil.delete(RedisCacheField.PATCH_VER_CODE + emailRawInfo.getEmail());
    }

    public void addRolesByUserId(String userId, List<RoleDto> roleRawReq)
    {
        userRepository.addRolesByUserId(userId, roleRawReq);
    }

    public Pair<String, Boolean> updateUserPhoneViaWeixin(String code)
    {
        String userId = getUserId();
        TblUserInfo tblUserInfo = getUserById(userId);
        if (Objects.nonNull(tblUserInfo))
        {
            try
            {
                WxMaPhoneNumberInfo newPhoneNoInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
                if (Objects.nonNull(newPhoneNoInfo))
                {
                    String phone = newPhoneNoInfo.getPhoneNumber();
                    if (StringUtils.isNotBlank(phone))
                    {
                        TblUserInfo tblExistUser = userRepository.getUserByPhone(phone);
                        if (tblExistUser != null && !userId.equals(tblExistUser.getUserId()))
                        {
                            //throw new WebSystemException(ErrorCode.PhoneOccupied, ErrorLevel.INFO);
                            // todo use transaction
                            tblExistUser.setWeixin(tblUserInfo.getWeixin());
                            updateExistUser(tblExistUser, tblUserInfo);
                            RedisUtil.delete(String.format(WA_USER_SESSION, wxMaProperties.getConfigStorage().getKeyPrefix(),userId));
                            return new Pair<>("user_exist", true);
                        }


                        tblUserInfo.setPhone(phone);
                        setBpMasterIfNeed(tblUserInfo, true);
                        userRepository.updateUserInfo(tblUserInfo);
                        return new Pair<>("user_exist", false);
                    }
                }
            }
            catch (WxErrorException e)
            {
                LOGGER.error("get phone from weixin server failed:{}", e);
                throw new RuntimeException(e);
            }

        }
        LOGGER.error("user not exist, login in to the wx first");
        throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateExistUser(TblUserInfo tblExistUser, TblUserInfo tblUserInfo)
    {
        userRepository.deleteUserRoleByUserId(tblUserInfo.getUserId());
        userRepository.deleteUserInfo(tblUserInfo.getUserId());
        bpRepository.deleteBp(tblUserInfo.getBpId());
        userRepository.updateUserInfo(tblExistUser);
    }

    private TblUserInfo getUserById(String userId)
    {
        TblUserInfo userInfo = userRepository.getUserById(userId);
        if (userInfo == null)
        {
            throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
        }

        return userInfo;
    }

    public void initTenantRole(TblUserInfo tblUserInfo)
    {
        List<TblIamRole> wholeRoles = userRepository.getWholeRoles();
        if (! CollectionUtils.isEmpty(wholeRoles))
        {
            List<TblIamRole> tblRoleInfos = new ArrayList<>();

            String kind = "";
            if (tblUserInfo.getKind().intValue() == UserKindEnum.PERSONAL.getCode()
                || tblUserInfo.getKind().intValue() == UserKindEnum.BP_SUB_USER.getCode())
            {
               kind = "TENANT";
            }
            else if (tblUserInfo.getKind().intValue() == UserKindEnum.BP_ADMIN.getCode())
            {
                kind = "TENANT_ADMIN";
            }
            else if (tblUserInfo.getKind().intValue() == UserKindEnum.ADMIN.getCode() || tblUserInfo.getKind().intValue() == UserKindEnum.SYSTEM.getCode())
            {
                kind = "ADMIN";
            }
            
            String finalKind = kind;
            tblRoleInfos = wholeRoles.stream().filter(role -> role.getRole().equalsIgnoreCase(finalKind))
                    .collect(Collectors.toList());

            if (! CollectionUtils.isEmpty(tblRoleInfos))
            {
                List<RoleDto> collect = tblRoleInfos.stream().map(x -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setRole(x.getRole());
                    roleDto.setPlatform(x.getPlatform());
                    return roleDto;
                }).collect(Collectors.toList());
                userRepository.addRolesByUserId(tblUserInfo.getUserId(), collect);
            }
        }
    }


    public void initTenantRoleV2(TblUserInfo tblUserInfo)
    {
        String userId = tblUserInfo.getUserId();
        List<TblIamRole> wholeRoles = userRepository.getWholeRoles();
        if (! CollectionUtils.isEmpty(wholeRoles))
        {
            Set<TblIamRole> tblRoleInfos = new HashSet<>();

            String kind = "";
            if (tblUserInfo.getKind().intValue() == UserKindEnum.PERSONAL.getCode()
                    || tblUserInfo.getKind().intValue() == UserKindEnum.BP_SUB_USER.getCode())
            {
                kind = "TENANT";
            }
            else if (tblUserInfo.getKind().intValue() == UserKindEnum.BP_ADMIN.getCode())
            {
                kind = "TENANT_ADMIN";
            }
            else if (tblUserInfo.getKind().intValue() == UserKindEnum.ADMIN.getCode() || tblUserInfo.getKind().intValue() == UserKindEnum.SYSTEM.getCode())
            {
                kind = "ADMIN";
            }

            String finalKind = kind;
            tblRoleInfos = wholeRoles.stream().filter(role -> role.getRole().equalsIgnoreCase(finalKind) && role.getPlatform().equalsIgnoreCase("all"))
                    .collect(Collectors.toSet());

            if (tblUserInfo.getKind().intValue() == UserKindEnum.BP_ADMIN.getCode())
            {
                String bpAdminDefaultRoles = commonProperties.getBpAdminDefaultRoles();
                initDefaultRoleByConfig(wholeRoles, tblRoleInfos, bpAdminDefaultRoles);
            }
            if (tblUserInfo.getKind().intValue() == UserKindEnum.PERSONAL.getCode()
                    || tblUserInfo.getKind().intValue() == UserKindEnum.BP_SUB_USER.getCode())
            {
                String bpUserDefaultRoles = commonProperties.getBpUserDefaultRoles();
                initDefaultRoleByConfig(wholeRoles, tblRoleInfos, bpUserDefaultRoles);
            }

            if (!(tblUserInfo.getKind().intValue() == UserKindEnum.ADMIN.getCode() || tblUserInfo.getKind().intValue() == UserKindEnum.SYSTEM.getCode()))
            {
                String defaultRolesForAll = commonProperties.getDefaultRolesForAll();
                initDefaultRoleByConfig(wholeRoles, tblRoleInfos, defaultRolesForAll);
            }

            if (! CollectionUtils.isEmpty(tblRoleInfos))
            {
                List<TblIamAttachment> tblIamAttachments = tblRoleInfos.stream().map(x -> {
                    TblIamAttachment tblIamAttachment = new TblIamAttachment();
                    tblIamAttachment.setId(Utils.assignUUId());
                    tblIamAttachment.setPrincipalType(TblIamAttachment.PrincipalType.USER.value());
                    tblIamAttachment.setPrincipalId(userId);
                    String roleId = String.valueOf(x.getRoleId());
                    tblIamAttachment.setTargetId(roleId);
                    tblIamAttachment.setTargetType(TblIamAttachment.TargetType.ROLE.value());
                    tblIamAttachment.setProjectId(null);
                    tblIamAttachment.setProjectName(null);
                    tblIamAttachment.setDescription(null);
                    return tblIamAttachment;
                }).collect(Collectors.toList());

                List<Long> roleIdList = tblRoleInfos.stream().map(role -> role.getRoleId()).collect(Collectors.toList());;
                authzService.publishRoleData(null, userId, roleIdList);
                authzRepository.batchInsert(tblIamAttachments);
            }
        }
    }

    private void initDefaultRoleByConfig(List<TblIamRole> wholeRoles, Set<TblIamRole> tblRoleInfos, String defaultRoles)
    {

        try
        {
            if (StringUtils.isNotBlank(defaultRoles))
            {
                String[] bpAdminDefaultRoleArr = StringUtils.split(defaultRoles, ",");
                if (bpAdminDefaultRoleArr != null && bpAdminDefaultRoleArr.length > 0)
                {
                    for (String roleStr : bpAdminDefaultRoleArr)
                    {
                        String trimRole = StringUtils.trim(roleStr);
                        if (trimRole.startsWith("role"))
                        {
                            trimRole = trimRole.replaceAll("role_", "");
                        }

                        if (trimRole.startsWith("ROLE_"))
                        {
                            trimRole = trimRole.replaceAll("ROLE_", "");
                        }

                        Matcher matcher = rolePattern.matcher(trimRole);
                        if (matcher.matches())
                        {
                            String platform = matcher.group(1);
                            String roleName = matcher.group(2);
                            List<TblIamRole> defaultIamRoles = wholeRoles.stream().filter(role -> role.getRole().equalsIgnoreCase(roleName) && role.getPlatform().equalsIgnoreCase(platform)).collect(Collectors.toList());
                            if (! CollectionUtils.isEmpty(defaultIamRoles))
                            {
                                tblRoleInfos.addAll(defaultIamRoles);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("initDefaultRoleByConfig error", e);
        }

    }

    private void setBpMasterIfNeed(TblUserInfo tblUserInfo, boolean update)
    {
        if (tblUserInfo.getKind().intValue() == UserKindEnum.BP_ADMIN.getCode())
        {
            String bpId = tblUserInfo.getBpId();
            TblBpInfo bpInfo = bpRepository.getBpInfo(bpId);
            if (Objects.nonNull(bpInfo))
            {
                String masterUser = bpInfo.getMasterUser();
                if (StringUtils.isNotBlank(masterUser) && !update)
                {
                    throw new WebSystemException(BP_MASTER_USER_EXIST, ErrorLevel.ERROR);
                }

                masterUser = tblUserInfo.getUserName();
                bpInfo.setMasterUser(masterUser);
                //gson.toJson(
                BpContactsInfo bpContactsInfo = new BpContactsInfo();

                String email = StringUtils.isNotBlank(tblUserInfo.getEmail()) ? tblUserInfo.getEmail() : "";
                String phone = StringUtils.isNotBlank(tblUserInfo.getPhone()) ? tblUserInfo.getPhone() : "";
                String address = StringUtils.isNotBlank(tblUserInfo.getAddress()) ? tblUserInfo.getAddress() : "";
                bpContactsInfo.setName(masterUser);
                bpContactsInfo.setEmail(email);
                bpContactsInfo.setPhone(phone);
                bpContactsInfo.setAddress(address);
                bpInfo.setContactInfo(JsonUtils.toJson(bpContactsInfo));

                bpRepository.updateBp(bpInfo);
            }
        }
    }


    private void checkIfBpExists(UserAddReq req)
    {
        String bpId = req.getBp_id();
        int kind = req.getKind();
        if (UserKindEnum.BP_ADMIN.getCode() == kind || UserKindEnum.BP_SUB_USER.getCode() == kind)
        {
            TblBpInfo bpInfo = bpRepository.getBpInfo(bpId);
            if (Objects.isNull(bpInfo))
            {
                throw new WebSystemException(BP_ID_INCORRECT, ErrorLevel.ERROR);
            }
        }
    }

    private void checkKind(UserAddReq req)
    {
        if (Objects.isNull(req.getKind()))
        {
            if (isAdmin() && StringUtils.isBlank(req.getBp_id()))
            {
                req.setKind(UserKindEnum.ADMIN.getCode());

            }
            else if (isBpAdmin() || isBpUser() || (isAdmin() && StringUtils.isNotBlank(req.getBp_id())))
            {
                req.setKind(UserKindEnum.BP_SUB_USER.getCode());
            }
            else
            {
                req.setKind(UserKindEnum.PERSONAL.getCode());
            }
        }
    }


    public WxMaUserInfo updateUserInfoViaWeixin(UserInfoReq userInfoReq)
    {
        String userId = getUserId();
        // get User Session key
        String sessionInfo = RedisUtil.get(String.format(WA_USER_SESSION, wxMaProperties.getConfigStorage().getKeyPrefix(),userId));
        if (StringUtils.isNotBlank(sessionInfo))
        {
            WxMaJscode2SessionResult sessionResult = JsonUtils.fromJson(sessionInfo, WxMaJscode2SessionResult.class);
            String sessionKey = sessionResult.getSessionKey();


            // user information verification
            if (!wxMaService.getUserService().checkUserInfo(sessionKey, userInfoReq.getRawData(), userInfoReq.getSignature())) {
                throw new WebSystemException(WX_REPORT_INCORRECT_USER_INFO, ErrorLevel.ERROR);
            }

            // decrypt user info
            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, userInfoReq.getEncryptedData(), userInfoReq.getIv());
            TblUserInfo tblUserInfo = getUserById(userId);
            tblUserInfo.setAddress(userInfo.getCountry() + "-" + userInfo.getProvince() + "-" + userInfo.getCity());
            tblUserInfo.setGender(Integer.valueOf(userInfo.getGender()));
            tblUserInfo.setUpdateTime(new Date());
            userRepository.updateUserInfo(tblUserInfo);
            return userInfo;
        }

        return null;
    }

    public void loadUserAttributesToOpa()
    {

    }

    public String getBpNameByBpId(String bpId)
    {
        if (StringUtils.isNotBlank(bpId))
        {
            TblBpInfo tblBpInfo = bpRepository.getBpInfo(bpId);
            if (Objects.nonNull(tblBpInfo))
            {
                return StringUtils.isNotBlank(tblBpInfo.getBpName()) ? tblBpInfo.getBpName() : "";
            }
        }

        return "";
    }

    public Map<String, Object> getInvitationLink()
    {
        Map<String, Object> refMap = new HashMap<>();

        if (!isAdmin() && commonProperties.getRegisterMode() != null && commonProperties.getRegisterMode().equalsIgnoreCase("admin-invite"))
        {
            throw new WebSystemException(User_Not_Grant, ErrorLevel.ERROR);
        }

        try
        {
            String userId = getUserId();

            Long exists = 1L;
            String vrfc = null;
            String redisKey = null;
            while (exists != null && exists == 1)
            {
                vrfc = RandomStringUtils.randomAlphanumeric(16);
                redisKey = String.format("%s:%s", RedisCacheField.INVITATION_CODE, vrfc);
                exists = RedisUtil.exists(redisKey);
            }

            RedisUtil.set(redisKey, userId, commonProperties.getInvitationUrlEffectiveSec());

            refMap.put("link", String.format("%s?ref=%s", commonProperties.getInvitationUrl(), vrfc));
            refMap.put("effective_sec", commonProperties.getInvitationUrlEffectiveSec());
        }
        catch (Exception e)
        {
            LOGGER.error("get invitation link failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
        return refMap;
    }

    public ResponseEntity<Object> invite(String ref)
    {
        String redisKey = String.format("%s:%s", RedisCacheField.INVITATION_CODE, ref);
        Long exists = RedisUtil.exists(redisKey);
        if (exists == null || exists == 0)
        {
            if (commonProperties.getRegisterMode().equalsIgnoreCase("open") || commonProperties.getRegisterMode().equalsIgnoreCase("admin-verify"))
            {
                HttpHeaders headers = new HttpHeaders();
                headers.add("location", "/register");
                return new ResponseEntity<>("", headers, HttpStatus.FOUND);
            }
            else
            {
                HttpHeaders headers = new HttpHeaders();
                headers.add("location", "/register?invite=true&code=" + INVITATION_LINK_INVALID.getCode());
                return new ResponseEntity<>("", headers, HttpStatus.FOUND);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/register?invite=true");
        Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
        String cookie = cookieProcessor.generateHeader(JwtUtils.getNewCookie(WebConstants.LNJOYING_VRFC, ref, WebConstants.LNJOYING_VRFCTOKEN_INDATE, "/api/ums/v1/users/registration"));
        headers.add("Set-Cookie", cookie);
        return new ResponseEntity<>("", headers, HttpStatus.FOUND);
    }

    private String getVrfc()
    {
        String vrfc = (String)getHttpServletRequest().getAttribute(WebConstants.LNJOYING_VRFC);

        if (vrfc != null)
        {
            vrfc = vrfc.replace(JwtUtils.getAuthorizationHeaderPrefix(),"");
        }

        return vrfc;
    }

    private String getInviterId(String vrfc, String invitationCode)
    {
        if (StringUtils.isNotEmpty(vrfc))
        {
            String redisKey = String.format("%s:%s", RedisCacheField.INVITATION_CODE, vrfc);
            String inviterId = RedisUtil.get(redisKey);
            RedisUtil.delete(redisKey);

            if (StringUtils.isEmpty(inviterId) && commonProperties.getRegisterMode() != null
                    && !commonProperties.getRegisterMode().equalsIgnoreCase("open"))
            {
                LOGGER.info("invitation code invalid: {}", vrfc);
                throw new WebSystemException(ErrorCode.INVITATION_LINK_INVALID, ErrorLevel.INFO);
            }
            else
            {
                return inviterId;
            }
        }

        if (StringUtils.isNotEmpty(invitationCode))
        {
            TblUserInfoExample example = new TblUserInfoExample();
            TblUserInfoExample.Criteria criteria = example.createCriteria();
            criteria.andInvitationCodeEqualTo(invitationCode);
            TblUserInfo userInfo = userRepository.getUserByExample(example);
            if (userInfo == null && commonProperties.getRegisterMode() != null
                    && !commonProperties.getRegisterMode().equalsIgnoreCase("open"))
            {
                LOGGER.info("invitation code invalid: {}", vrfc);
                throw new WebSystemException(ErrorCode.INVITATION_CODE_INVALID, ErrorLevel.INFO);
            }
            else
            {
                return  userInfo.getUserId();
            }
        }

        if (commonProperties.getRegisterMode().equalsIgnoreCase("invite")
                || commonProperties.getRegisterMode().equalsIgnoreCase("admin-invite"))
        {
            throw new WebSystemException(ErrorCode.UNSUPPORT_SELF_REGISTRATION, ErrorLevel.INFO);
        }

        return null;
    }

    public Map<String, Object> getInvitationCode()
    {
        Map<String, Object> refMap = new HashMap<>();

        if (!isAdmin() && commonProperties.getRegisterMode() != null && commonProperties.getRegisterMode().equalsIgnoreCase("admin-invite"))
        {
            throw new WebSystemException(User_Not_Grant, ErrorLevel.ERROR);
        }

        try
        {
            String userId = getUserId();

            TblUserInfo userInfo = userRepository.getUserById(userId);
            if (userInfo == null)
            {
                throw new WebSystemException(ErrorCode.User_Not_Exist, ErrorLevel.INFO);
            }

            if (StringUtils.isNotEmpty(userInfo.getInvitationCode()))
            {
                refMap.put("invitation-code", userInfo.getInvitationCode());
            }
            else
            {
                userInfo.setInvitationCode(genInvitationCode());
                userRepository.updateByPrimaryKeySelective(userInfo);
                refMap.put("invitation-code", userInfo.getInvitationCode());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("get invitation code failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
        return refMap;
    }

    private String genInvitationCode()
    {
        Long exists = 1L;
        String invitationCode = null;

        while (exists == 1)
        {
            invitationCode = RandomStringUtils.randomNumeric(8);

            TblUserInfoExample example = new TblUserInfoExample();
            TblUserInfoExample.Criteria criteria = example.createCriteria();
            criteria.andInvitationCodeEqualTo(invitationCode);

            Long count = userRepository.countUsersByExample(example);
            if (count == 0)
            {
                exists = 0L;
            }
        }
        return  invitationCode;
    }

    public TblBpInfo createTemporaryBp(TblUserInfo tblUserInfo)
    {
        TblBpInfo tblBpInfo = new TblBpInfo();
        tblBpInfo.setBpId(Utils.assignUUId());
        tblBpInfo.setBpName(Utils.buildStr("bp-", tblUserInfo.getUserName()));
        tblBpInfo.setDescription("");
        tblBpInfo.setLicenseId("");
        tblBpInfo.setStatus(0);
        tblBpInfo.setMasterUser(tblUserInfo.getUserName());
        tblBpInfo.setWebsite("");
        tblBpInfo.setCreateTime(new Date());
        tblBpInfo.setUpdateTime(tblBpInfo.getCreateTime());
        return tblBpInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertBpAndUser(TblBpInfo tblBpInfo, TblUserInfo tblUserInfo)
    {
        bpRepository.insertBp(tblBpInfo);
        userRepository.insertUser(tblUserInfo);
    }


    private static List<RoleDto> getRoleDtos(List<TblIamRole> tblRoleInfoList)
    {
        List<RoleDto> roleDtoList = new ArrayList<>();
        if (tblRoleInfoList == null || tblRoleInfoList.isEmpty())
        {
            return roleDtoList;
        }

        for (TblIamRole tblRoleInfo : tblRoleInfoList)
        {
            RoleDto roleDto = new RoleDto();
            roleDto.setPlatform(tblRoleInfo.getPlatform());
            roleDto.setRole(tblRoleInfo.getRole());
            roleDtoList.add(roleDto);
        }
        return roleDtoList;
    }


    private static List getUserRoleList(List<TblIamRole> tblRoleInfoList)
    {
        List<RoleDto> rolesByUserId = getRoleDtos(tblRoleInfoList);
        ;
        if (! CollectionUtils.isEmpty(rolesByUserId)) {
            List<String> roles = rolesByUserId.stream().map(roleDto ->
                            org.bouncycastle.util.Strings.toUpperCase("role" + "_" + roleDto.getPlatform() + "_" + roleDto.getRole()))
                    .collect(Collectors.toList());
            return roles;
        }
        return Collections.EMPTY_LIST;
    }
}
