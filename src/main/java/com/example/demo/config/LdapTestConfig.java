package com.example.demo.config;/*
package com.demo.authserver.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

@Configuration
public class LdapTestConfig {

    @Bean
    CommandLineRunner ldapTest(LdapTemplate ldapTemplate) {

        return args -> {

            System.out.println("LDAP Test Started");

            ldapTemplate.search("", "(objectClass=person)", (AttributesMapper<String>) attrs -> {

                if (attrs.get("cn") != null) {
                    String cn = attrs.get("cn").get().toString();

                    System.out.println("Found: " + cn);
                }

                return "";
            });

//            Option 1: Search that OU only
            ldapTemplate.search("OU=Users,OU=edug", "(objectClass=user)", (AttributesMapper<String>) attrs -> {

                if (attrs.get("cn") != null) {
                    System.out.println(attrs.get("cn").get());
                }

                return "";
            });

//            Option 2: Get ABANDA Only
            ldapTemplate.search("OU=Users,OU=edug", "(cn=ABANDA)", (AttributesMapper<String>) attrs -> {

                System.out.println("Found: " + attrs.get("cn").get());

                return "";
            });


//            Option 3: Search by Login ID (Recommended)
            ldapTemplate.search("OU=Users,OU=edug", "(sAMAccountName=ABANDA)", (AttributesMapper<String>) attrs -> {

                System.out.println(attrs.get("distinguishedName").get());

                return "";
            });

            System.out.println("LDAP Test Completed");
        };
    }
}

*/