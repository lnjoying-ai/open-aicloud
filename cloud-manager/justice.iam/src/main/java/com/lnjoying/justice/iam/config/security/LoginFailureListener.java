package com.lnjoying.justice.iam.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Autowired
    private MecUserDetailsService mecUserDetailsService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event.getException().getClass().equals(BadCredentialsException.class)) {
            String userId = event.getAuthentication().getName();
            mecUserDetailsService.addFailedCount(userId);
        }
    }
}
