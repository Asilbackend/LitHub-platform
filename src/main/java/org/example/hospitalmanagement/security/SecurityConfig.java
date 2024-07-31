package org.example.hospitalmanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(m -> {
            m
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/home/patient/info", "/admin/show/patient")
                    .hasAnyRole("ADMIN", "PATIENT")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/doctor/**").hasRole("DOCTOR")
                    .requestMatchers("/patient/**").hasRole("PATIENT")
                    .requestMatchers("/users/**").hasRole("SUPER_ADMIN")
                    .anyRequest().authenticated();
        });
        httpSecurity.userDetailsService(customUserDetailsService);
        httpSecurity.formLogin(m -> {
            m
                    .defaultSuccessUrl("/loginSuccessUrl");
        });
        /*httpSecurity.logout(m -> {
            m.logoutUrl("/auth/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));
        });*/
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}