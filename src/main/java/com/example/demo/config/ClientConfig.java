package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Configuration
public class ClientConfig {

    @Bean
    RegisteredClientRepository registeredClientRepository() {


        RegisteredClient client =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("dataapp")
                        .clientSecret("{noop}secret")
                        .authorizationGrantType(
                                AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .scope("admin")
                        .tokenSettings(
                                TokenSettings.builder()
                                        .accessTokenTimeToLive(
                                                Duration.ofHours(1))
                                        .build())
                        .build();

        return new InMemoryRegisteredClientRepository(client);
    }
}