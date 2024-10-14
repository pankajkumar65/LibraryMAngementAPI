package com.Library.Mangement.config;

import com.Library.Mangement.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers("/api/v1/auth/**").permitAll()  // Permit auth-related requests

                                // Book endpoints:
//                                .requestMatchers(GET, "/api/v1/book/**").hasAnyRole(Role.USER.name(), Role.AUTHOR.name())// Both users and authors can read books
                                .requestMatchers(GET, "/api/v1/author/books").hasRole(Role.AUTHOR.name())
                                .requestMatchers(POST, "/api/v1/book/add").hasRole(Role.AUTHOR.name())  // Only authors can add books
                                .requestMatchers(DELETE, "/api/v1/book/delete/**").hasRole(Role.AUTHOR.name()) // Only authors can delete books
                                .requestMatchers(GET,  "/api/v1/borrowed-books","/api/v1/book/list").hasRole(Role.USER.name()) // Only authors can delete books
                                .requestMatchers(POST, "/api/v1/borrow/**", "/api/v1/advance-booking" ,"api/v1/notify-users/**").hasRole(Role.USER.name())
                                // Any other request requires authentication
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))  // Stateless session management
                .authenticationProvider(authenticationProvider)  // Set the authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter before username/password filter
                .logout(logout ->  // Configure logout
                        logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
