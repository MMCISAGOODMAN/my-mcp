# my-mcp

演示 [mcp-plus-spring-boot-starter](https://github.com/MMCISAGOODMAN/mcp-plus-spring-boot-starter) 的 Spring Boot 示例：无需 `@McpTool`，`@Service` / `@RestController` 的 public 方法自动注册为 MCP Tool。

## 业务说明

| 类 | 作用 |
|----|------|
| `BookService` | `@Service`，`findById` / `search` 自动暴露；`listAll` 用 `@McpExpose` 自定义工具名 |
| `BookController` | `@RestController`，`getBook` 同时作为 REST 和 MCP Tool |
| `KnowledgeService` | **RAG 演示**，`search_knowledge` 模拟知识库检索 Tool |
| `InternalMetricsService` | `@McpExclude`，整类不暴露 |

## 启动

```bash
cd /Users/simon.ma/ownproject/ai/my-mcp
mvn spring-boot:run
```

启动后访问：**http://127.0.0.1:8091/** 查看演示入口。

## 看效果

### 1. Actuator 工具列表

```bash
curl http://127.0.0.1:8091/actuator/mcpplus | jq .
```

### 2. REST API（对比用）

```bash
curl http://127.0.0.1:8091/api/books/1
```

### 3. 命令行 MCP 演示

```bash
chmod +x scripts/demo-mcp.sh
./scripts/demo-mcp.sh
```

### 4. MCP Inspector（图形界面，含 RAG 测试）

```bash
npx @modelcontextprotocol/inspector
```

| 字段 | 值 |
|------|-----|
| Transport | **Streamable HTTP** |
| URL | `http://127.0.0.1:8091/mcp` |

连接后在 **Tools** 中可测试：

- `bookService_findById` — 参数 `{"id": 1}`
- `list_all_books` — 参数 `{}`
- **`search_knowledge`** — RAG 演示，参数 `{"query": "MCP", "topK": 2}`

> 若出现 **Failed to fetch**，是浏览器 CORS 问题，本项目已配置 `McpCorsConfiguration`，重启服务后重试。

### 5. Cursor

项目已含 `.cursor/mcp.json`：

```json
{
  "mcpServers": {
    "my-mcp": {
      "url": "http://127.0.0.1:8091/mcp"
    }
  }
}
```

## 预期暴露的工具

- `bookService_findById`
- `list_all_books`（`@McpExpose` 自定义名）
- `bookService_search`
- `bookController_getBook`
- `search_knowledge`（RAG mock 检索）

`InternalMetricsService` 不会出现。

## 依赖

- Java 21
- `io.github.mmcisagoodman:mcp-plus-spring-boot-starter:0.1.0`（Maven Central）
- Spring AI MCP Server WebMVC 1.1.2
