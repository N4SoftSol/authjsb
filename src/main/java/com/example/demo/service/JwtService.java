package com.example.demo.service;


import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.demo.dto.LdapUserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;


@Service
public class JwtService {

    private final LdapGroupService ldapGroupService;
    private final LdapGroupScopeMapper ldapGroupScopeMapper;
    @Value("${jwt.keystore.location}")
    private String keystoreLocation;
    @Value("${jwt.keystore.password}")
    private String keystorePassword;
    @Value("${jwt.key.alias}")
    private String keyAlias;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.access-token-seconds}")
    private long tokenLifetime;

    public JwtService(LdapGroupService ldapGroupService, LdapGroupScopeMapper ldapGroupScopeMapper) {

        this.ldapGroupService = ldapGroupService;
        this.ldapGroupScopeMapper = ldapGroupScopeMapper;
    }

    private RSAPrivateKey loadPrivateKey() {

        try {

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            try (InputStream is = new ClassPathResource(keystoreLocation).getInputStream()) {

                keyStore.load(is, keystorePassword.toCharArray());
            }

            return (RSAPrivateKey) keyStore.getKey("authserver", keystorePassword.toCharArray());

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        LdapUserInfo userInfo = ldapGroupService.getUserInfo(username);

        List<String> groups = ldapGroupService.getGroups(username);

        Set<String> scopes = new HashSet<>();

        for (String group : groups) {

            scopes.addAll(ldapGroupScopeMapper.mapGroupToScopes(group));
        }

        Instant now = Instant.now();
//        System.out.println(
//                "Token lifetime = " + tokenLifetime);
//        return Jwts.builder()
//                .header().add("kid", keyAlias).and().subject(username).issuer(issuer).issuedAt(Date.from(now)).expiration(Date.from(now.plusSeconds(tokenLifetime))).claim("groups", groups).claim("ldapScopes", scopes).signWith(loadPrivateKey(), Jwts.SIG.RS256).compact();
//    }
        return Jwts.builder()

                .header()
                .add("kid", keyAlias)
                .and()
                .subject(username)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(
                        Date.from(
                                now.plusSeconds(tokenLifetime)))
                .claim(
                        "displayName",
                        userInfo.displayName())
                .claim(
                        "email",
                        userInfo.email())
                .claim(
                        "groups",
                        groups)
                .claim(
                        "ldapScopes",
                        scopes)
                .signWith(
                        loadPrivateKey(),
                        Jwts.SIG.RS256)
                .compact();
    }

    public String generateRefreshToken() {

        return java.util.UUID
                .randomUUID()
                .toString();
    }
}