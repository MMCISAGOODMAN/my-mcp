package com.example.mymcp.mcp;

import com.example.mymcp.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MCP Resources for Inspector testing. Registered via Spring AI annotation scanner,
 * not via mcp-plus (which only auto-exposes tools).
 */
@Component
public class BookMcpResources {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookMcpResources(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @McpResource(
            uri = "books://catalog",
            name = "book-catalog",
            description = "Full book catalog as JSON")
    public String bookCatalog() {
        return toJson(bookService.listAll());
    }

    @McpResource(
            uri = "book://{id}",
            name = "book-detail",
            description = "Single book details by id")
    public String bookById(String id) {
        return toJson(bookService.findById(Long.parseLong(id)));
    }

    @McpResource(
            uri = "readme://getting-started",
            name = "getting-started",
            description = "Quick start guide for this MCP demo server",
            mimeType = "text/markdown")
    public String gettingStarted() {
        return """
                # my-mcp Getting Started

                1. Start the server: `mvn spring-boot:run`
                2. Open MCP Inspector with Streamable HTTP → `http://127.0.0.1:8091/mcp`
                3. Try **Tools**, **Resources**, and **Prompts** tabs

                Example resource URIs:
                - `books://catalog`
                - `book://1`
                - `readme://getting-started`
                """;
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        }
        catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize resource content", ex);
        }
    }
}
