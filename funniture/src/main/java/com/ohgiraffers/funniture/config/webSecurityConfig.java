package com.ohgiraffers.funniture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.ohgiraffers.funniture")
public class webSecurityConfig {

    /**
     * 1. 정적 자원에 대한 인증된 사용자의 접근을 설정하는 메서드
     *
     * @return WebSeruciryCusomizer
     * */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**",
                "/lib/**", "/productimgs/**");
    }

    /**
     * security filter chain 설정
     *
     * @return SecurityFilterChain
     * */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(AbstractHttpConfigurer::disable)
//                .addFilterBeforejwtAuthorizationFilter(), BasicAuthenticationFilter.class)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(form -> form.disable())
//                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .httpBasic(basic -> basic.disable())
//        ;
//
//        return http.build();
//    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())// CSRF 보안 비활성화 (Postman 테스트 가능)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/v1/auth/signup","/api/v1/product/*","/api/v1/product","/api/v1/rental/*","/api/v1/rental").permitAll() // 회원가입 API는 인증 없이 허용
                            .anyRequest().authenticated()
                    );

            return http.build();
        }

    /**
     * 3. Authentization의 인증 메서드를 제공하는 매니저로 Provider의 인터페이스를 의미한다.
     * @return AuthenticationManager
     * */
//    @Bean
//    public AuthenticationManager authenticationManager(){
//        return new ProviderManager(customAuthenticationProvider());
//    }
//
//    /**
//     * 4. 사용자의 아이디와 패스워드를 DB와 검증하는 handler이다.
//     * @return CustomAuthenticationProvider
//     * */
//    @Bean
//    public CustomAuthenticationProvider customAuthenticationProvider(){
//        return new CustomAuthenticationProvider();
//    }

    /**
     * 5. 비밀번호를 암호화 하는 인코더
     *
     * @return BCryptPasswordEncoder
     * */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 6. 사용자의 인증 요청을 가로채서 로그인 로직을 수행하는 필터
     * @return CustomAuthenticationFilter
     * */
//    @Bean
//    public CustomAuthenticationFilter customAuthenticationFilter(){
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
//        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
//        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthLoginSuccessHandler());
//        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailUserHandler());
//        customAuthenticationFilter.afterPropertiesSet();
//        return customAuthenticationFilter;
//    }

    /**
     * 7. spring security 기반의 사용자의 정보가 맞을 경우 결과를 수행하는 handler
     *
     * @return customAuthLoginSuccessHandler
     * */
//    @Bean
//    public CustomAuthSuccessHandler customAuthLoginSuccessHandler(){
//        System.out.println("동작함");
//        return new CustomAuthSuccessHandler();
//    }


    /**
     * 8. Spring security의 사용자 정보가 맞지 않은 경우 행되는 메서드
     *
     * @return CustomAuthFailUreHandler
     * */
//    @Bean
//    public CustomAuthFailUserHandler customAuthFailUserHandler(){
//        return new CustomAuthFailUserHandler();
//    }
//
//    /**
//     * 9. 사용자 요청시 수행되는 메소드
//     * @return JwtAuthorizationFilter
//     * */
//    public JwtAuthorizationFilter jwtAuthorizationFilter(){
//        return new JwtAuthorizationFilter(authenticationManager());
//    }


}
