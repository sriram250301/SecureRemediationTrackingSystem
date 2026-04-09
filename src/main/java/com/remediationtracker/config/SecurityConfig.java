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

    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/analyst/**").hasAnyRole("ADMIN", "ANALYST")
    .requestMatchers("/dev/**").hasRole("DEVELOPER")
    //Test
    .requestMatchers("/test/admin").hasRole("ADMIN")
    .requestMatchers("/test/analyst").hasAnyRole("ADMIN", "ANALYST")
    .requestMatchers("/test/dev").hasRole("DEVELOPER")
   
    .anyRequest().authenticated()
            )

            .formLogin(form -> form.permitAll()
            )

            .logout(logout -> logout.permitAll()
            );

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