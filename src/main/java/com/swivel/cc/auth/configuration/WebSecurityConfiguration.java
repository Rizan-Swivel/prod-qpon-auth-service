package com.swivel.cc.auth.configuration;

import com.swivel.cc.auth.service.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailService userDetailService;

    @Autowired
    public WebSecurityConfiguration(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //obtain Authentication Manager / authenticate the user
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());

    }

//    Todo: commit to be removed and check csrf
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/api/v1/users").permitAll().
//                antMatchers("/api/v1/users/users").authenticated().anyRequest().authenticated().and()
//                .csrf().disable();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
