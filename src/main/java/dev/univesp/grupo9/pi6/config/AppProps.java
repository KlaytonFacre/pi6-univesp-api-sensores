package dev.univesp.grupo9.pi6.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProps {
    /**
     * URL externa da API em produção (ex.: https://pi6.logafacil.com.br)
     */
    private String externalUrl;

    /**
     * Origens permitidas no CORS (ex.: https://pi6.logafacil.com.br, http://localhost:5173)
     */
    private List<String> corsAllowedOrigins;

    // getters/setters
    public String getExternalUrl() { return externalUrl; }
    public void setExternalUrl(String externalUrl) { this.externalUrl = externalUrl; }
    public List<String> getCorsAllowedOrigins() { return corsAllowedOrigins; }
    public void setCorsAllowedOrigins(List<String> corsAllowedOrigins) { this.corsAllowedOrigins = corsAllowedOrigins; }
}

