package com.seungmoo.spring_jwt.service;

import com.seungmoo.spring_jwt.dto.UserDto;
import com.seungmoo.spring_jwt.entity.Authority;
import com.seungmoo.spring_jwt.entity.User;
import com.seungmoo.spring_jwt.repository.UserRepository;
import com.seungmoo.spring_jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     * @param userDto
     * @return
     */
    @Transactional
    public User signup(UserDto userDto) {
        userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).ifPresent(user -> {
            throw new RuntimeException("이미 가입되어 있는 유저 입니다.");
        });

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(Authority.ROLE_USER))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

}
