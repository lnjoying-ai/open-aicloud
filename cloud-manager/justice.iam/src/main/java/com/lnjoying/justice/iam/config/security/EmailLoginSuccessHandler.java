package com.lnjoying.justice.iam.config.security;

import com.lnjoying.justice.schema.common.RedisCacheField;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.repo.UserRepository;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmailLoginSuccessHandler extends LoginSuccessHandler
{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailLoginSuccessHandler.class);

    @Autowired
    private MecUserDetailsService mecUserDetailsService;

    @Value("${jwtkey}")
    private String jwtkey;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException
    {
        String email = authentication.getName();
        TblUserInfo tblUserInfo = userRepository.getUserByEmail(email);

        TblBpInfo tblBpInfo = null;
        if ( !StringUtils.isEmpty(tblUserInfo.getBpId()))
        {
            tblBpInfo = bpRepository.getBpInfo(tblUserInfo.getBpId());
        }

        RedisUtil.delete(RedisCacheField.AUTH_VER_CODE + email);

        mecUserDetailsService.clearFailedCount(email);
        User user= (User) authentication.getPrincipal();

        buildHttpRsp(user, request, response, tblUserInfo, tblBpInfo);
    }
}