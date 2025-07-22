package com.management.hr_managemnet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.management.hr_managemnet.jwt.JwtAuthFilter;
import com.management.hr_managemnet.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
                http
                    .csrf(csrf -> csrf.disable())
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/authenticate").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/api/users/register").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/users").hasRole("ADMIN")
                                .requestMatchers("/api/users/role/**").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/employees/register").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/employees/allEmployee").hasRole("ADMIN")
                                .requestMatchers("api/employees/department/**").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/employees/updateEmployee").hasAnyRole("ADMIN", "HR")
                                .requestMatchers("/api/attandences/register").hasAnyRole("ADMIN","HR")
                                .requestMatchers("/api/users/addUsers").hasAnyRole("ADMIN", "HR")

    
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().denyAll()                              
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                    
            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Your custom UserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder());     // Your password encoder
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }
}
