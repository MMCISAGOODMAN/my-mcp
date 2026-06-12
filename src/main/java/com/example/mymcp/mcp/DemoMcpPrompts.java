package com.example.mymcp.mcp;

import com.example.mymcp.service.KnowledgeService;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MCP Prompts for Inspector testing. Registered via Spring AI annotation scanner.
 */
@Component
public class DemoMcpPrompts {

    private final KnowledgeService knowledgeService;

    public DemoMcpPrompts(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @McpPrompt(
            name = "book-recommendation",
            description = "Build a prompt that asks the model to recommend books")
    public GetPromptResult bookRecommendation(
            @McpArg(name = "topic", description = "Topic, genre, or interest", required = true) String topic,
            @McpArg(name = "style", description = "Response style, e.g. concise or detailed", required = false)
            String style) {
        String tone = style == null || style.isBlank() ? "concise" : style.trim();
        String userMessage = """
                Recommend 3 books about "%s".
                Keep the answer %s.
                For each book include title, author, and one sentence on why it fits.
                """.formatted(topic, tone);
        return prompt("Book Recommendation", userMessage);
    }

    @McpPrompt(
            name = "rag-answer",
            description = "Build a grounded RAG prompt using mock knowledge retrieval")
    public GetPromptResult ragAnswer(
            @McpArg(name = "question", description = "User question", required = true) String question,
            @McpArg(name = "topK", description = "Number of knowledge chunks to include", required = false)
            Integer topK) {
        int limit = topK == null ? 2 : Math.max(1, topK);
        List<KnowledgeService.KnowledgeChunk> chunks = knowledgeService.search(question, limit);
        String context = chunks.stream()
                .map(chunk -> "- [%s] %s (source: %s, score: %.2f)".formatted(
                        chunk.id(), chunk.content(), chunk.source(), chunk.score()))
                .collect(Collectors.joining("\n"));

        String userMessage = """
                Answer the question using ONLY the context below.
                If the context is insufficient, say you do not know.

                Question:
                %s

                Context:
                %s
                """.formatted(question.trim(), context.isBlank() ? "(no matching chunks)" : context);

        return prompt("RAG Answer", userMessage);
    }

    @McpPrompt(
            name = "inspector-smoke-test",
            description = "Minimal prompt for verifying prompts/get in MCP Inspector")
    public GetPromptResult inspectorSmokeTest(
            @McpArg(name = "name", description = "Name to greet", required = true) String name) {
        String userMessage = "Say hello to %s and confirm MCP prompts are working."
                .formatted(name.trim());
        return prompt("Inspector Smoke Test", userMessage);
    }

    private static GetPromptResult prompt(String description, String userMessage) {
        return new GetPromptResult(
                description,
                List.of(new PromptMessage(Role.USER, new TextContent(userMessage))));
    }
}
