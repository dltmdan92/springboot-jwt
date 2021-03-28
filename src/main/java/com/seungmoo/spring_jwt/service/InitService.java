package com.seungmoo.spring_jwt.service;

import com.seungmoo.spring_jwt.entity.Authority;
import com.seungmoo.spring_jwt.entity.User;
import com.seungmoo.spring_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void init() {
        User admin = User.builder()
                .userId(1l)
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .nickname("admin")
                .activated(true)
                .authorities(new HashSet<>())
                .build();
        admin.getAuthorities().add(Authority.ROLE_ADMIN);
        admin.getAuthorities().add(Authority.ROLE_USER);

        User user = User.builder()
                .userId(2l)
                .username("user")
                .password(passwordEncoder.encode("user"))
                .nickname("user")
                .activated(true)
                .authorities(new HashSet<>())
                .build();
        user.getAuthorities().add(Authority.ROLE_USER);

        userRepository.save(admin);
        userRepository.save(user);
    }

}
