package com.lnjoying.justice.iam.controller;

import com.lnjoying.justice.commonweb.controller.RestWebController;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.config.CommonProperties;
import com.lnjoying.justice.iam.utils.VerificationCodeUtil;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.schema.common.TemplateID;
import com.lnjoying.justice.iam.config.DescriptionConfig;
import com.lnjoying.justice.iam.config.ServiceConfig;
import com.lnjoying.justice.iam.service.CombRpcSerice;
import com.lnjoying.justice.iam.service.IdentityService;
import com.lnjoying.justice.iam.utils.ValidatorUtil;
import com.lnjoying.justice.schema.service.apiserver.TipMessageService;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static com.lnjoying.justice.commonweb.common.SwaggerConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.OPERATION_TEMPORARILY_LOCKED;

@RestSchema(schemaId = "verification")
@RequestMapping("/api/iam/v1/verification")
@Controller
@ApiOperation(value = "", extensions = {@Extension(name = SWAGGER_X_RESOURCE,
        properties = {@ExtensionProperty(name = SWAGGER_RESOURCE_SINGULAR_NAME, value = "verification"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_PLURAL_NAME, value = "verifications"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_DESCRIPTION, value = "验证码"),
                @ExtensionProperty(name = SWAGGER_RESOURCE_MODEL, value = "")})})
public class VerificationController extends RestWebController
{
    private ReentrantLock emailLock = new ReentrantLock();
    private ReentrantLock smsLock = new ReentrantLock();

    @Autowired
    private IdentityService identityService;

    int VER_CODE_LEN = 6;

    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    CombRpcSerice combRpcSerice;

    @GetMapping(value = "/auth/sms/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "verification", response = Object.class, notes =
            DescriptionConfig.VERIFICATION_SMS_MSG)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void authVerificationSms(
            @ApiParam(value = "phone", required = true, name = "phone") @PathVariable String phone)  throws IOException
    {
        verificationSms(phone, TemplateID.AUTH_VER_CODE, RedisCacheField.AUTH_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }



    @GetMapping(value = "/auth/email/{emailAddr}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "verification", response = Object.class, notes =
            DescriptionConfig.VERIFICATION_EMAIL_MSG)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void authVerificationEmail(
            @ApiParam(value = "emailAddr", required = true, name = "emailAddr") @PathVariable String emailAddr)  throws IOException
    {
        verificationEmail(emailAddr, TemplateID.AUTH_VER_CODE, RedisCacheField.AUTH_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }

    @GetMapping(value = "/registration/sms/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "registration", response = Object.class, notes =
            DescriptionConfig.SMS_REGISTRATION_MSG)
    public void regVerificationSms(
            @ApiParam(value = "phone", required = true, name = "phone") @PathVariable String phone)  throws IOException
    {
        verificationSms(phone, TemplateID.REG_VER_CODE, RedisCacheField.REG_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }

    @GetMapping(value = "/registration/email/{emailAddr}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "registration", response = Object.class, notes =
            DescriptionConfig.EMAIL_REGISTRATION_MSG)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void regVerificationEmail(
            @ApiParam(value = "emailAddr", required = true, name = "emailAddr") @PathVariable String emailAddr)  throws IOException
    {
        verificationEmail(emailAddr, TemplateID.REG_VER_CODE, RedisCacheField.REG_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }

    @GetMapping(value = "/patch/sms/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch", response = Object.class, notes =
            DescriptionConfig.SMS_REGISTRATION_MSG)
    public void patchVerificationSms(
            @ApiParam(value = "phone", required = true, name = "phone") @PathVariable String phone)  throws IOException
    {
        verificationSms(phone, TemplateID.PATCH_VER_CODE, RedisCacheField.PATCH_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }

    @GetMapping(value = "/patch/email/{emailAddr}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "patch", response = Object.class, notes =
            DescriptionConfig.EMAIL_REGISTRATION_MSG)
    @ResponseBody    @ResponseStatus(HttpStatus.OK)
    public void patchVerificationEmail(
            @ApiParam(value = "emailAddr", required = true, name = "emailAddr") @PathVariable String emailAddr)  throws IOException
    {
        verificationEmail(emailAddr, TemplateID.PATCH_VER_CODE, RedisCacheField.PATCH_VER_CODE, commonProperties.getValidateCodeEffectiveSec());
    }

    void verificationSms(String phone, String template, String redisKey, Integer validateCodeEffectiveSec)
    {
        if (null == phone || phone.isEmpty())
        {
            throw new WebSystemException(ErrorCode.VER_Params_Error, ErrorLevel.ERROR);
        }

        checkResetPwdVerificationCodeErrorTimes(phone, null);

        if (! ValidatorUtil.validateStr(ServiceConfig.PATTERN_TELEPHONE, phone))
        {
            throw new WebSystemException(ErrorCode.VER_Params_Error, ErrorLevel.ERROR);
        }

        String verCode = Utils.getRandomStr(VER_CODE_LEN);
        if (null == verCode || verCode.isEmpty())
        {
            LOGGER.error("validate code is empty.");
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.ERROR);
        }

        smsLock.lock();
        try
        {
            if (checkIfCodeAvailable(redisKey + phone, validateCodeEffectiveSec))
            {
                return;
            }
            RedisUtil.set(redisKey, phone, verCode, validateCodeEffectiveSec);
        }
        finally
        {
            smsLock.unlock();
        }

        try
        {
            List<TipMessageService.NotifyParam> notifyParams = new ArrayList<>();
            notifyParams.add(new TipMessageService.NotifyParam("code", verCode));
            notifyParams.add(new TipMessageService.NotifyParam("time", String.valueOf(validateCodeEffectiveSec)));
            combRpcSerice.getTipMessageService().sendSingleSms(notifyParams, phone, template);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.SystemError ,ErrorLevel.CRITICAL);
        }
        LOGGER.info("log validate code {} to {}", verCode, phone);
    }

    private void checkResetPwdVerificationCodeErrorTimes(String phone, String emailAddr)
    {
        boolean resetPwdLocked = VerificationCodeUtil.checkIfResetPwdLocked(phone, emailAddr);
        if (resetPwdLocked)
        {
            // 因为多次输入错误，重置密码操作已被锁定一段时间
            LOGGER.error("The reset password operation is temporarily locked due to multiple input errors");
            throw new WebSystemException(OPERATION_TEMPORARILY_LOCKED, ErrorLevel.ERROR);
        }
    }

    void verificationEmail(String emailAddr, String template, String redisKey, Integer validateCodeEffectiveSec)
    {
        if (null == emailAddr || emailAddr.isEmpty()) {
            throw new WebSystemException(ErrorCode.VER_Params_Error, ErrorLevel.ERROR);
        }

        checkResetPwdVerificationCodeErrorTimes(null, emailAddr);

        if (!ValidatorUtil.validateStr(ServiceConfig.PATTERN_MAILADDRESS, emailAddr)) {
            throw new WebSystemException(ErrorCode.VER_Params_Error, ErrorLevel.ERROR);
        }

        String verCode = Utils.getRandomStr(VER_CODE_LEN);
        if (null == verCode || verCode.isEmpty())
        {
            LOGGER.error("validate code is empty.");
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.ERROR);
        }

        emailLock.lock();
        try
        {
            if (checkIfCodeAvailable(redisKey + emailAddr, validateCodeEffectiveSec))
            {
                return;
            }
            RedisUtil.set(redisKey + emailAddr, verCode, validateCodeEffectiveSec);
        }
        finally
        {
            emailLock.unlock();
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("code", verCode);
        paramMap.put("time", String.valueOf(validateCodeEffectiveSec));
        try
        {
            combRpcSerice.getTipMessageService().sendEmail(emailAddr, paramMap, template);
            LOGGER.info("log validate code {} to {}", verCode, emailAddr);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.CRITICAL);
        }
    }

    private Boolean checkIfCodeAvailable(String key, Integer validateCodeEffectiveSec)
    {
        Long ttl = RedisUtil.ttl(key);

        //time=null表示redis连接出错
        if (ttl == null)
        {
            return true;
        }

        long elapsedSeconds = validateCodeEffectiveSec - ttl.longValue();
        if (elapsedSeconds < 60)
        {
            //距上次发送的验证码间隔时间不到1分钟
            return true;
        }

        return false;
    }
}
