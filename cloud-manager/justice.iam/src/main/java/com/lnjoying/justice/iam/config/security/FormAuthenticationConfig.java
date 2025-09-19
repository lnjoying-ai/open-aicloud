package com.lnjoying.justice.iam.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class FormAuthenticationConfig
{
    @Autowired
    private LoginFailHandler loginFailHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    public void configure(HttpSecurity http) throws Exception
    {
        http.formLogin()
//            .loginPage("/#/")
            .loginProcessingUrl("/api/iam/v1/auth/password/tokens")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailHandler);
    }

}
