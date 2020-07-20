package com.epam.esm.config;

import com.epam.esm.security.jwt.JwtConfigurer;
import com.epam.esm.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String REGISTRATION = "/users/registration";
    private static final String CERTIFICATE_READ_OPERATIONS = "/certificates/info/**";
    private static final String ALL_OPERATION = "/**";
    private static final String ALL_READ_OPERATIONS = "/**/info/**";
    private static final String NEW_ORDER = "/orders";
    private static final String LOGIN_PAGE = "/auth/login";

    @Autowired
    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(REGISTRATION, LOGIN_PAGE, CERTIFICATE_READ_OPERATIONS).permitAll()
                    .antMatchers(ALL_READ_OPERATIONS, NEW_ORDER).hasAnyRole(USER, ADMIN)
                    .antMatchers(ALL_OPERATION).hasRole(ADMIN)
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
