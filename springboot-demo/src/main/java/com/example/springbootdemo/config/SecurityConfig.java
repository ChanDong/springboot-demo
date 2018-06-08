package com.example.springbootdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by shixi03 on 2018/6/8.
 */
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    public static class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.authorizeRequests()
                .antMatchers("/test")
                .hasRole("ADMIN")
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic();
        }
    }
}
