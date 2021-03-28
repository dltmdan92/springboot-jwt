package com.seungmoo.spring_jwt.controller;

import com.seungmoo.spring_jwt.dto.UserDto;
import com.seungmoo.spring_jwt.entity.User;
import com.seungmoo.spring_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        Optional<User> myUserWithAuthorities = userService.getMyUserWithAuthorities();
        return myUserWithAuthorities.map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        Optional<User> userWithAuthorities = userService.getUserWithAuthorities(username);
        return userWithAuthorities.map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
