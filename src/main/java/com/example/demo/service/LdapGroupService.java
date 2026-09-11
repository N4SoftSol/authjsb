package com.example.demo.service;

import com.example.demo.dto.LdapUserInfo;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LdapGroupService {

    private final LdapTemplate ldapTemplate;

    public LdapGroupService(LdapTemplate ldapTemplate) {

        this.ldapTemplate = ldapTemplate;
    }

    public List<String> getGroups(String username) {

        String userDn = "CN=" + username + ",OU=Users,OU=edug,DC=n4softsol,DC=com";

        return ldapTemplate.search("OU=Groups,OU=edug", "(member:1.2.840.113556.1.4.1941:=" + userDn + ")", (AttributesMapper<String>) attrs -> attrs.get("cn").get().toString());
    }

    public LdapUserInfo getUserInfo(
            String username) {

        List<LdapUserInfo> users =
                ldapTemplate.search(

                        "",

                        "(sAMAccountName=" +
                                username +
                                ")",

                        (AttributesMapper<LdapUserInfo>)
                                attrs ->

                                        new LdapUserInfo(

                                                username,

                                                attrs.get(
                                                        "displayName")
                                                        != null
                                                        ? attrs.get(
                                                                "displayName")
                                                        .get()
                                                        .toString()
                                                        : username,

                                                attrs.get(
                                                        "mail")
                                                        != null
                                                        ? attrs.get(
                                                                "mail")
                                                        .get()
                                                        .toString()
                                                        : "N/A"));

        return users.isEmpty()
                ? new LdapUserInfo(
                username,
                username,
                "N/A")
                : users.getFirst();
    }
}