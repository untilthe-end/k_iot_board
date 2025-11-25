package org.example.boardback.config;

import lombok.RequiredArgsConstructor;
import org.example.boardback.security.filter.JwtAuthenticationFilter;
import org.example.boardback.security.handler.JsonAccessDeniedHandler;
import org.example.boardback.security.handler.JsonAuthenticationEntryPoint;
import org.example.boardback.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import org.example.boardback.security.oauth2.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.exposed-headers:Authorization,Set-Cookie}")
    private String exposedHeaders;

    @Value("${security.h2-console:true}")
    private boolean h2ConsoleEnabled;

    /* ============================
     * PasswordEncoder / AuthManager
     * ============================ */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    /* ============================ */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);   // 쿠키 전송 허용
        config.setAllowedOriginPatterns(splitToList(allowedOrigins));
        config.setAllowedHeaders(splitToList(allowedHeaders));
        config.setAllowedMethods(splitToList(allowedMethods));
        config.setExposedHeaders(splitToList(exposedHeaders));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/favicon.ico",
                "/error"
        ));
    }

    /* ============================ */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        if (h2ConsoleEnabled) {
            http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
        }

        http.authorizeHttpRequests(auth -> {

                    if (h2ConsoleEnabled) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    auth
                            // .permitAll(): 인증/인가 없이 모두 가능
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                            /**
                             * 인증 불필요
                             * : permitAll
                             * */
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/oauth2/**",
                                    "/login/**",
                                    "/login/oauth2/code/**",
                                    "/favicon.ico",
                                    "/error").permitAll()

                            .requestMatchers(HttpMethod.GET, "/api/v1/boards/**").permitAll() // 게시판 조회 기능

                            // 인증된 사용자만 사용 가능 (인가, 권한 X)
                            // : HttpMethod는 선택값, URL 경로는 필수
                            // .requestMatchers(HttpMethod.GET, "/api/v1/~~").authenticated()

                            // 인가(특정 권한)된 사용자만 사용 가능
                            // : 역할에 따른 분리
                            // : HttpMethod는 선택값, URL 경로는 필수
                            // .requestMatchers(HttpMethod.GET, "/api/v1/~~").hasAnyRole("A권한", "B권한")
                            // .requestMatchers(HttpMethod.GET, "/api/v1/~~").hasRole("단일권한")

                            /**
                             * 인증 필요
                             * */
                            .anyRequest().authenticated(); // 그 외에는 인증 필요
                })
                // OAuth2 로그인 설정
//                .oauth2Login(oauth2 -> oauth2
//                        // OAuth2 로그인 시 사용자 정보를 가져올 때 사용할 서비스 지정
//                        .userInfoEndpoint(userinfo ->
//                                    userinfo.userService(customOAuth2UserService)
//                                )
//                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                );
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userinfo ->
                                userinfo.userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json; charset=UTF-8");
                            response.getWriter().write(
                                    "{\"success\":false, \"message\":\"OAuth2 로그인 실패\", \"code\":\"OAUTH2_FAILURE\"}"
                            );
                        })
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private List<String> splitToList(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}