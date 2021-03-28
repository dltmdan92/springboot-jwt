package com.seungmoo.spring_jwt.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.error("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            username = principal.getUsername();
        }
        else if (authentication.getPrincipal() instanceof String || authentication.getPrincipal().equals("anonymousUser")) {
            username = authentication.getName();
        }
        return Optional.ofNullable(username);
    }

}
