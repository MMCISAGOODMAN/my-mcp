package com.example.mymcp.controller;

import io.mcpplus.autoconfigure.annotation.McpExclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@McpExclude
public class DemoController {

    @Value("${server.port:8091}")
    private int port;

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("app", "my-mcp demo");
        body.put("description", "BookService/BookController methods are auto-exposed as MCP tools via mcp-plus-spring-boot-starter");
        body.put("endpoints", Map.of(
                "mcp", "http://localhost:" + port + "/mcp",
                "actuator_tools", "http://localhost:" + port + "/actuator/mcpplus",
                "health", "http://localhost:" + port + "/actuator/health",
                "rest_api", "http://localhost:" + port + "/api/books/{id}",
                "demo_script", "Run: ./scripts/demo-mcp.sh"));
        body.put("try_curl", List.of(
                "curl http://localhost:" + port + "/actuator/mcpplus",
                "curl http://localhost:" + port + "/api/books/1"));
        return body;
    }
}
