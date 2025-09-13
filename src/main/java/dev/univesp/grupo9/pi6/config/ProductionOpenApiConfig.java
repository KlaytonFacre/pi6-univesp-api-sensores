package dev.univesp.grupo9.pi6.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ProductionUrlConfig.class)
@Profile("prod")
public class ProductionOpenApiConfig {

    @Bean
    public OpenAPI openAPI(ProductionUrlConfig props, @Value("${app.build.version}") String buildVersion) {
        OpenAPI api = new OpenAPI();

        if (props.getExternalUrl() != null && !props.getExternalUrl().isBlank()) {
            api.setServers(List.of(new Server().url(props.getExternalUrl()).description("Produção")));
        }

        api.info(new Info()
                .title("PI6 API")
                .description("Backend para monitoramento de sensores")
                .version(buildVersion)
        );
        return api;
    }
}
