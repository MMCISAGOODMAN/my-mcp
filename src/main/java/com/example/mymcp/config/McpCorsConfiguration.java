package com.example.mymcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * MCP Inspector runs in the browser on a different port (e.g. 6274).
 * Without CORS, Inspector shows "Failed to fetch".
 */
@Configuration
public class McpCorsConfiguration {

    @Bean
    public CorsFilter mcpCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Mcp-Session-Id"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/mcp", config);
        source.registerCorsConfiguration("/mcp/**", config);
        return new CorsFilter(source);
    }
}
