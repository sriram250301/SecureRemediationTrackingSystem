package com.remediationtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disabled for now 
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Role-restricted paths
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/analyst/**").hasAnyRole("ADMIN", "ANALYST")
                .requestMatchers("/dev/**").hasRole("DEVELOPER")
                .requestMatchers("/audit/**").hasAnyRole("ADMIN", "ANALYST")

                // Everything else requires login
                .anyRequest().authenticated()
            )
            // LOGIN FORM  
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            // LOGOUT 
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")  // Implemented "You have been logged out" message
                .permitAll()
            )
            // Keep httpBasic for Postman 
            .httpBasic(httpBasic -> {})
            // H2 console iframe fix
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}