package com.ohgiraffers.funniture.config;

import com.ohgiraffers.funniture.auth.filter.CustomAuthenticationFilter;
import com.ohgiraffers.funniture.auth.handler.CustomAuthFailUserHandler;
import com.ohgiraffers.funniture.auth.handler.CustomAuthSuccessHandler;
import com.ohgiraffers.funniture.auth.handler.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan("com.ohgiraffers.funniture")
public class WebSecurityConfig {

    /**
     * 1. 정적 자원에 대한 인증된 사용자의 접근을 설정하는 메서드
     *
     * @return WebSeruciryCusomizer
     * */
    // 우선적으로 회원가입 할 때 활성화
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

    // formLogin은 세션 방식일 때 사용, jwt 사용한다면 formLogin 필요 없음

    // 로그인 되지 않은 상태에서 회원가입 할 때 아래와 같이 csrf disable()로 해야 함.
    // 람다 식으로 변경되어 AbstractHttpConfigurer::disable가 아닌, csrf -> csrf.disable() 이렇게 해야 함.
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf(csrf -> csrf.disable())// CSRF 보안 비활성화 (Postman 테스트 가능)
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers("/api/v1/auth/signup","/api/v1/product/*","/api/v1/product","/api/v1/rental/*","/api/v1/rental", "/api/v1/auth/login").permitAll() // 회원가입 API는 인증 없이 허용
//                            .anyRequest().authenticated()
//                    );
//
//            return http.build();
//        }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login"); // ✅ 로그인 엔드포인트 설정

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/signup","/api/v1/product/*","/api/v1/product","/api/v1/rental/*","/api/v1/rental", "/api/v1/auth/login").permitAll() // ✅ 로그인 및 회원가입 허용
                        .anyRequest().authenticated()
                )
                .addFilter(customAuthenticationFilter) // 🔥 커스텀 로그인 필터 추가
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // ✅ JWT 사용 시 세션 비활성화

        return http.build();
    }



    /**
     * 3. Authentization의 인증 메서드를 제공하는 매니저로 Provider의 인터페이스를 의미한다.
     * @return AuthenticationManager
     * */
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(customAuthenticationProvider());
    }
//
//    /**
//     * 4. 사용자의 아이디와 패스워드를 DB와 검증하는 handler이다.
//     * @return CustomAuthenticationProvider
//     * */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        System.out.println("사용자 아이디, 비번을 db와 검증하는 CustomAuthenticationProvider = ");
        return new CustomAuthenticationProvider();
    }

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
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(){
                                                                                        // 3번 authenticationManager 전달
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
                                                                // 7번 customAuthLoginSuccessHandler 전달
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthLoginSuccessHandler());
                                                                // 8번 customAuthFailUserHandler 전달
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailUserHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 7. spring security 기반의 사용자의 정보가 맞을 경우 결과를 수행하는 handler
     *
     * @return customAuthLoginSuccessHandler
     * */
    @Bean
    public CustomAuthSuccessHandler customAuthLoginSuccessHandler(){
        System.out.println("사용자 정보 맞을 때 동작하는 CustomAuthSuccessHandler");
        return new CustomAuthSuccessHandler();
    }


    /**
     * 8. Spring security의 사용자 정보가 맞지 않은 경우 행되는 메서드
     *
     * @return CustomAuthFailUreHandler
     * */
    @Bean
    public CustomAuthFailUserHandler customAuthFailUserHandler(){
        System.out.println("사용자 정보 맞지 않을 때 동작하는 CustomAuthFailUserHandler");
        return new CustomAuthFailUserHandler();
    }
//
//    /**
//     * 9. 사용자 요청시 수행되는 메소드
//     * @return JwtAuthorizationFilter
//     * */
//    public JwtAuthorizationFilter jwtAuthorizationFilter(){
//        return new JwtAuthorizationFilter(authenticationManager());
//    }


}
