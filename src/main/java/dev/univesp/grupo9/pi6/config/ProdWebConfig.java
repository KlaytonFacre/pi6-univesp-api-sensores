package dev.univesp.grupo9.pi6.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableConfigurationProperties(AppProps.class)
@Profile("prod")
public class ProdWebConfig {

    @Bean
    public OpenAPI openAPI(AppProps props) {
        OpenAPI api = new OpenAPI();

        if (props.getExternalUrl() != null && !props.getExternalUrl().isBlank()) {
            api.setServers(List.of(new Server().url(props.getExternalUrl()).description("Produção")));
        }
        return api;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(AppProps props) {
        CorsConfiguration cfg = new CorsConfiguration();
        List<String> origins = props.getCorsAllowedOrigins();

        if (origins != null && !origins.isEmpty()) {
            cfg.setAllowedOrigins(origins);
        } else {
            cfg.setAllowedOrigins(List.of());
        }

        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Habilita a configuração de CORS definida no bean 'corsConfigurationSource'.
                .cors(Customizer.withDefaults())
                // Define a política de criação de sessão como 'stateless'.
                // Isso é crucial para APIs RESTful, pois não mantêm estado de sessão entre requisições, o que melhora a escalabilidade.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso irrestrito aos endpoints do Swagger UI e da documentação.
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Restringe todas as outras requisições para que sejam autenticadas.
                        // Esta é a mudança mais importante para o ambiente de produção, garantindo que o resto da sua API esteja protegida.
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}