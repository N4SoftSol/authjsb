package com.example.demo.config;/*
package com.demo.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
    public InMemoryUserDetailsManager users() {

        UserDetails user = User.withUsername("naveen").password("{noop}password").roles("USER").build();

        return new InMemoryUserDetailsManager(user);
    }
}
*/