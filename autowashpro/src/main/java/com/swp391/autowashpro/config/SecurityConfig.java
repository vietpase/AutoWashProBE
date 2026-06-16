package com.swp391.autowashpro.config;

import com.swp391.autowashpro.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/**",
                                "/error"
                        ).permitAll()

                        // Admin
                        .requestMatchers("/api/admin/**").permitAll()

                        // Loyalty Tier
                        .requestMatchers(HttpMethod.GET, "/api/loyalty-tiers/active").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/loyalty-tiers/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/loyalty-tiers/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/loyalty-tiers/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/loyalty-tiers/**").hasRole("MANAGER")

                        //WashService
                        .requestMatchers(HttpMethod.GET,"/api/wash-services/active").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/wash-services/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/wash-services/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/wash-services/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/wash-services/**").hasRole("MANAGER")

                        //Promotion
                        .requestMatchers(HttpMethod.GET,"/api/promotions/active").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/promotions/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/promotions/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/promotions/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/promotions/**").hasRole("MANAGER")

                        // TimeSlots
                        .requestMatchers("/api/time-slots/active").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/time-slots/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/time-slots/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/time-slots/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/time-slots/**").hasRole("MANAGER")


                        //Vehicle
                        .requestMatchers(HttpMethod.GET,"/api/vehicles/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/api/vehicles/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicles/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicles/**").hasRole("CUSTOMER")

                        //reward
                        .requestMatchers("/api/rewards/active").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/rewards/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/rewards/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/rewards/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/rewards/**").hasRole("MANAGER")

                        // Others
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}