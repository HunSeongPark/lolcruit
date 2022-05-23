package com.hunseong.lolcruit.config;

import com.hunseong.lolcruit.domain.user.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Hunseong on 2022/05/23
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/api/posts/**").hasRole(Role.USER.name())
                .antMatchers("/posts/add/**").hasRole(Role.USER.name())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/loginForm")
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/");
    }
}
