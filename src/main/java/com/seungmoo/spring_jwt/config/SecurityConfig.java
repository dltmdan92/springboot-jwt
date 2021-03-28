package com.seungmoo.spring_jwt.config;

import com.seungmoo.spring_jwt.jwt.JwtAccessDeniedHandler;
import com.seungmoo.spring_jwt.jwt.JwtAuthenticationEntryPoint;
import com.seungmoo.spring_jwt.jwt.JwtSecurityConfig;
import com.seungmoo.spring_jwt.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // PreAuthorize라는 annotation을 사용하기 위해 추가
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 여기서 ignore할 경우 스프링 시큐리티를 아예 안탈 수 있다.
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // h2 콘솔 페이지와, favicon 리소스는 스프링 시큐리티에서 제외 시킨다.
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // token 방식을 사용할 땐 보통 csrf 꺼준다.
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용 : STATELESS

                .and()
                .authorizeRequests()// HttpServletRequest들에 대한 접근 제한을 설정하겠다.
                .antMatchers("/api/hello").permitAll()
                .antMatchers("/api/authenticate").permitAll() // 토큰을 발급 받는 API
                .antMatchers("/api/signup").permitAll() // 회원 가입 API
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

    }
}
