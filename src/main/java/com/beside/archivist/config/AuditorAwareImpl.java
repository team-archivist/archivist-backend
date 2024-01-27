package com.beside.archivist.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //System.out.println("authentication ====="+authentication.getAuthorities());

        if (authentication == null || !authentication.isAuthenticated()) { // 인증이 안되어있다면
            return Optional.empty();
        }
        return Optional.of(authentication.getName()); // 인증이 되었다면 회원 email return
    }
}
