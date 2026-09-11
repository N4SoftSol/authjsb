package com.example.demo;

import com.example.demo.service.AuditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("${test.profile:NOT_FOUND}")
	private String profileTest;

/*
    @Bean
    CommandLineRunner verifyTrustStore() {
        return args -> {
            System.out.println("TrustStore = " +
                    System.getProperty("javax.net.ssl.trustStore"));
        };
    }
*/

/*
    @Bean
    CommandLineRunner verifyLdapAccounts(LdapGroupService ldapGroupService) {
        return args -> {

            System.out.println( "Groups for user: asmith " +
                    ldapGroupService.getGroups(
                            "asmith"));

            System.out.println( "Groups for user: ewatson " +
                    ldapGroupService.getGroups(
                            "ewatson"));

            System.out.println( "Groups for user: itaylor " +
                    ldapGroupService.getGroups(
                            "itaylor"));
        };
    }
*/

	@Bean
	LdapTemplate ldapTemplate(LdapContextSource contextSource) {

		LdapTemplate ldapTemplate = new LdapTemplate(contextSource);

		ldapTemplate.setIgnorePartialResultException(true);

		return ldapTemplate;
	}

	@Bean
	CommandLineRunner auditTest(
			AuditService auditService) {

		return args -> {

			auditService.log(
					"SYSTEM",
					"SYSTEM SERVICE",
					"sys@domain.com",
					"TEST",
					"AUTH",
					"SUCCESS",
					"Audit logging initialized");
		};
	}

	@Bean
	CommandLineRunner dbDebug(
			Environment env) {

		return args -> {

//            System.out.println(
//                    "spring.datasource.url = " +
//                            env.getProperty(
//                                    "spring.datasource.url"));
//
//            System.out.println(
//                    "spring.datasource.username = " +
//                            env.getProperty(
//                                    "spring.datasource.username"));
//
//            System.out.println(
//                    "spring.datasource.password = " +
//                            env.getProperty(
//                                    "spring.datasource.password"));

			System.out.println(
					"PROFILE TEST = " + profileTest);
		};
	}

	@Bean
	Converter<Jwt, AbstractAuthenticationToken>
	jwtAuthenticationConverter() {

		JwtGrantedAuthoritiesConverter converter =
				new JwtGrantedAuthoritiesConverter();

		converter.setAuthoritiesClaimName(
				"ldapScopes");

		converter.setAuthorityPrefix(
				"SCOPE_");

		return jwt ->
				new JwtAuthenticationToken(
						jwt,
						converter.convert(jwt));
	}
}
