package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class LdapGroupScopeMapper {

    public Set<String> mapGroupToScopes(String groupName) {

        return switch (groupName.toLowerCase()) {

            case "it-api_admin" ->
                    Set.of(
                            "read",
                            "write",
                            "admin");

            case "it-api_contributors" ->
                    Set.of(
                            "read",
                            "write");

            case "it-api_read" -> Set.of("read");


            default -> Set.of();
        };
    }

}