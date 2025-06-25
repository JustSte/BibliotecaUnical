package com.Stefan.BibliotecaUnical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        //PUBLIC
                        .requestMatchers("/api/public/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()

                        //USER
                        .requestMatchers("/api/mapLayout/**","/api/occupy/**", "/api/shelf/**", "/api/book/**", "/api/reservation/**").hasAnyRole("USER","STAFF", "ADMIN")

                        //STAFF
                        .requestMatchers("/api/book/**", "/api/chair/**", "/api/mapLayout/**", "/api/libraryTable/**", "/api/locker/**").hasAnyRole("STAFF", "ADMIN")

                        //ADMIN
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        ));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null && resourceAccess.containsKey("BibliotecaUnical")) {
                Map<String, Object> bibliotecaRoles = (Map<String, Object>) resourceAccess.get("BibliotecaUnical");
                List<String> roles = (List<String>) bibliotecaRoles.get("roles");

                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }

            return authorities;
        });

        return jwtAuthenticationConverter;
    }
}
