package com.remediationtracker.config; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http 
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
    .requestMatchers("/register/**").permitAll()
    .requestMatchers("/vulnerabilities/**").authenticated()  

    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/analyst/**").hasAnyRole("ADMIN", "ANALYST")
    .requestMatchers("/dev/**").hasRole("DEVELOPER")
    //Test
    .requestMatchers("/test/admin").hasRole("ADMIN")
    .requestMatchers("/test/analyst").hasAnyRole("ADMIN", "ANALYST")
    .requestMatchers("/test/dev").hasRole("DEVELOPER")
   
    .anyRequest().authenticated()
            )
        .httpBasic(httpBasic -> {}) // for auth in Postman
        .formLogin(form -> form.disable())
        .logout(logout -> logout.permitAll());
        http.headers(headers -> headers.frameOptions(frameOptionsConfig -> {
            frameOptionsConfig.disable();
            frameOptionsConfig.sameOrigin(); 
        }));// Fix for H2 console access
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Admin only";
    }
}