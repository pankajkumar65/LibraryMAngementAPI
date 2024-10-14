package com.Library.Mangement.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
    public enum Role {

        USER,
        AUTHOR;

        // Simplifying the logic to return the role as the only authority
        public Set<SimpleGrantedAuthority> getAuthorities() {
            return Set.of(new SimpleGrantedAuthority("ROLE_" + this.name())); // Adding the role itself as authority
        }
    }
