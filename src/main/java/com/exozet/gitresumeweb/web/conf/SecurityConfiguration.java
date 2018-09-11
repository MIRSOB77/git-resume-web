package com.exozet.gitresumeweb.web.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import static org.springframework.http.HttpMethod.GET;

/**
 * This config is a replacement for the old spring-web-security.xml
 */

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {



    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
            
            .csrf().disable()
                .authorizeRequests().antMatchers(GET, "/health").permitAll() .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
