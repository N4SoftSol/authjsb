package com.example.demo.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class JwtConfig {

    @Value("${jwt.keystore.location:NOT_FOUND}")
    private String keystoreLocation;

    @Value("${jwt.keystore.password:NOT_FOUND}")
    private String keystorePassword;

    @Value("${jwt.key.alias:NOT_FOUND}")
    private String keyAlias;

    @Value("${jwt.key.password:NOT_FOUND}")
    private String keyPassword;

    @Bean
    JWKSource<SecurityContext> jwkSource()
            throws Exception {

        KeyStore keyStore =
                KeyStore.getInstance("PKCS12");

        try (InputStream is =
                     new ClassPathResource(
                             keystoreLocation)
                             .getInputStream()) {

            keyStore.load(
                    is,
                    keystorePassword
                            .toCharArray());
        }

        RSAKey rsaKey =
                RSAKey.load(
                        keyStore,
                        keyAlias,
                        keyPassword.toCharArray());


        JWKSet jwkSet =
                new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(
                jwkSet);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}