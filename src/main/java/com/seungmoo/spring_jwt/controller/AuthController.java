package com.seungmoo.spring_jwt.controller;

import com.seungmoo.spring_jwt.dto.LoginDto;
import com.seungmoo.spring_jwt.dto.TokenDto;
import com.seungmoo.spring_jwt.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 로그인 API
     * @param loginDto
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        // username과 password를 통해 auth Token 객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // auth Token 객체를 이용해서 authenticate 메소드가 실행될 때
        // UserDetailsService의 loadUserByUsername이 실행된다 --> 그 결과 값을 갖고 Authentication 객체가 생성된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 생성된 Authentication 객체를 SecurityContextHolder에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 생성된 Authentication 객체를 통해 jwtToken을 만든다.
        String jwtToken = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

        return new ResponseEntity<>(new TokenDto(jwtToken), httpHeaders, HttpStatus.OK);
    }

}
