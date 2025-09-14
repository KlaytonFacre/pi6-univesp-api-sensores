package dev.univesp.grupo9.pi6.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ProductionUrlConfig.class)
@Profile("prod")
public class ProductionSecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(ProductionUrlConfig props) {
        CorsConfiguration cfg = new CorsConfiguration();
        List<String> origins = props.getCorsAllowedOrigins();

        if (origins != null && !origins.isEmpty()) {
            cfg.setAllowedOrigins(origins);
        } else {
            cfg.setAllowedOrigins(List.of()); // Se allowed origins nao estiver setada do application-prod nenhuma url estara liberada
        }

        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setExposedHeaders(List.of("Location"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L); // cache do preflight

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger aberto
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Coleta de ruído aberta (ajuste conforme seu path real)
                        .requestMatchers(HttpMethod.POST, "/noise").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/noise/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // demais endpoints
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}