package com.road_friends.everycar.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("security config ...");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/admin/login", "/admin").permitAll()
                        .requestMatchers("/admin/user/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/admin/login")
                        .usernameParameter("userId")
                        .passwordParameter("userPassword")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .sessionManagement(session -> session
                        // 로그인 중복방지, 세션 저장
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요시 세션 생성
                        .invalidSessionUrl("/admin/login") // 세션 만료 시 이동할 페이지
                        .maximumSessions(1) // 중복 로그인 방지
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/admin")
                        .permitAll()
                );

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
