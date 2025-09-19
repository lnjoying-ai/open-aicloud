package com.lnjoying.justice.iam.config.security;

import com.lnjoying.justice.iam.config.CommonProperties;
import com.lnjoying.justice.iam.config.filter.CustomAuthenticationFilter;
import com.lnjoying.justice.iam.config.filter.EmailAuthenticationFilter;
import com.lnjoying.justice.iam.config.filter.JwtAuthenticationFilter;
import com.lnjoying.justice.iam.config.filter.SmsAuthenticationFilter;
import com.lnjoying.justice.schema.service.iam.AuthzService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@DependsOn("nacosConfiguration")
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
//
    private static final String[] ADMIN_ROLES = {
        //"CIS_ADMIN", "UMS_ADMIN", "ECRM_ADMIN"
        "ALL_ADMIN"
    };

    @Autowired
    private CommonProperties commonProperties;

    @Value("${jwtkey}")
    private String jwtkey;

    @Resource
    private FormAuthenticationConfig formAuthenticationConfig;


    @Autowired
    private MecUserDetailsService mecUserDetailsService;

    @Autowired
    private LoginFailHandler loginFailHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private SmsLoginSuccessHandler smsloginSuccessHandler;

    @Autowired
    private EmailLoginSuccessHandler emailloginSuccessHandler;

    @Autowired
    private AuthzService authzService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        formAuthenticationConfig.configure(httpSecurity);
        httpSecurity.exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/AuthImpl/**", "/login", "/login.html","/css/**", "/fonts/**", "/api/iam/v1/health/**","/health/**","/img/**","/exception/**",
                        "/js/**", "/favicon.ico", "/inspector/**", "/UmsServiceImpl/**","/AuthzServiceImpl/**",
                        "/CommonResourceFeederServiceImpl/**", "/api/iam/v1/users/invite",
                        "/index.html", "/user-privacy.md", "/user-agreement.md","/error/**")
                .permitAll()
                .antMatchers("/api/iam/v1/verification/**").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/iam/v1/users/registration").permitAll()
                .antMatchers(HttpMethod.PATCH,  "/api/iam/v1/users/retrieved-password").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/iam/v1/auth/wx/tokens").permitAll()
                .anyRequest()
                .authenticated()
                .and().cors().and().csrf().disable()
                .headers()
                .frameOptions().sameOrigin();

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterAt(customAuthenticationFilter(),  UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAt(smsAuthenticationFilter(),     UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAt(emailAuthenticationFilter(),   UsernamePasswordAuthenticationFilter.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), jwtkey, authzService);

        httpSecurity.addFilter(jwtAuthenticationFilter);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(mecUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * CorsFilter solve cross-domain issues for logout api.
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        String corsAllowOrigins = commonProperties.getCorsAllowOrigins();
        if (StringUtils.isBlank(corsAllowOrigins))
        {
            corsAllowOrigins = "*";
        }
        corsConfiguration.setAllowedOrigins(Arrays.asList(corsAllowOrigins.split(",")));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
     * Define the PBKDF2 encoder with sha256.
     *
     * @return
     */
    @Bean
    public Pbkdf2PasswordEncoder passwordEncoder() {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        return encoder;
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception
    {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailHandler);
        filter.setFilterProcessesUrl("/api/iam/v1/auth/password/tokens");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    SmsAuthenticationFilter smsAuthenticationFilter() throws Exception
    {
        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(smsloginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailHandler);
        filter.setFilterProcessesUrl("/api/iam/v1/auth/sms/tokens");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    EmailAuthenticationFilter emailAuthenticationFilter() throws Exception
    {
        EmailAuthenticationFilter filter = new EmailAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(emailloginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailHandler);
        filter.setFilterProcessesUrl("/api/iam/v1/auth/email/tokens");
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}