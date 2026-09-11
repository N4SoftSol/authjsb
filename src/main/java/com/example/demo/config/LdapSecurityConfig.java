package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;

@Configuration
public class LdapSecurityConfig {

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {

        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);

        factory.setUserSearchFilter("(sAMAccountName={0})");

        return factory.createAuthenticationManager();
    }

}