package com.example.demo.config;

import com.example.demo.service.LdapGroupScopeMapper;
import com.example.demo.service.LdapGroupService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class JwtCustomizerConfig {

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(
            LdapGroupService ldapGroupService,
            LdapGroupScopeMapper ldapGroupScopeMapper) {

        return context -> {

            String username = context.getPrincipal().getName();
            List<String> groups =
                    ldapGroupService.getGroups(username);

            Set<String> scopes = new HashSet<>();

            for (String group : groups) {

                scopes.addAll(
                        ldapGroupScopeMapper
                                .mapGroupToScopes(group));
            }

            context.getClaims().subject(username);

            context.getClaims().claim(
                    "groups",
                    groups);

            context.getClaims().claim(
                    "ldapScopes",
                    scopes);
        };
    }

}
