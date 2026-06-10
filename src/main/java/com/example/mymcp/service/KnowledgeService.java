package com.example.mymcp.service;

import io.mcpplus.autoconfigure.annotation.McpExpose;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Mock knowledge base for demonstrating RAG-style MCP tools in Inspector.
 */
@Service
public class KnowledgeService {

    private static final List<KnowledgeChunk> CHUNKS = List.of(
            new KnowledgeChunk("doc-1", "Spring AI supports MCP Server with Streamable HTTP transport.",
                    "spring-ai-mcp.md", 0.95),
            new KnowledgeChunk("doc-2", "RAG combines vector retrieval with LLM generation for grounded answers.",
                    "rag-guide.md", 0.92),
            new KnowledgeChunk("doc-3", "MCP Plus auto-exposes @Service methods as MCP tools without @McpTool.",
                    "mcp-plus-readme.md", 0.90),
            new KnowledgeChunk("doc-4", "Use MCP Inspector to debug tools/list and tools/call before connecting Cursor.",
                    "testing.md", 0.88));

    @McpExpose(name = "search_knowledge", description = "Search knowledge base by semantic keyword (mock RAG retrieval)")
    @Operation(summary = "Search knowledge base", description = "Returns top-K document chunks matching the query")
    public List<KnowledgeChunk> search(
            @Parameter(description = "User question or search query") String query,
            @Parameter(description = "Maximum number of chunks to return") int topK) {
        String q = query.toLowerCase(Locale.ROOT);
        return CHUNKS.stream()
                .filter(chunk -> chunk.content().toLowerCase(Locale.ROOT).contains(q)
                        || chunk.source().toLowerCase(Locale.ROOT).contains(q))
                .sorted(Comparator.comparingDouble(KnowledgeChunk::score).reversed())
                .limit(Math.max(1, topK))
                .toList();
    }

    public record KnowledgeChunk(String id, String content, String source, double score) {
    }
}
