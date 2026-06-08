package com.swp391.autowashpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 🔥 BẮT BUỘC PHẢI CÓ: Để kích hoạt @PreAuthorize hoạt động
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Mở công khai hoàn toàn các cổng Auth và tài liệu Swagger công nghệ
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 2. Mở sẵn cổng lấy danh sách dịch vụ active cho Customer (Né bộ lọc chặn đầu của /api/admin/** sau này)
                        .requestMatchers("/api/admin/wash-services/active").permitAll()

                        // 3. Tạm thời mở các request khác (sau này nhóm ông có thể đổi thành .authenticated() tùy cấu hình dự án)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}