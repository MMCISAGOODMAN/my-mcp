#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8091}"
MCP_URL="${BASE_URL}/mcp"

echo "==> 1) Home page"
curl -s "${BASE_URL}/" | python3 -m json.tool 2>/dev/null || curl -s "${BASE_URL}/"

echo
echo "==> 2) MCP Plus registered tools (Actuator)"
curl -s "${BASE_URL}/actuator/mcpplus" | python3 -m json.tool 2>/dev/null | head -40

echo
echo "==> 3) MCP initialize"
HEADERS=$(curl -s -D - -o /tmp/mcp-init.json -X POST "${MCP_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"demo-script","version":"1.0"}}}')

SESSION=$(echo "$HEADERS" | grep -i Mcp-Session-Id | awk '{print $2}' | tr -d '\r')
echo "Session: ${SESSION}"
cat /tmp/mcp-init.json | python3 -m json.tool 2>/dev/null || cat /tmp/mcp-init.json

echo
echo "==> 4) MCP tools/list"
curl -s -X POST "${MCP_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: ${SESSION}" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/list","params":{}}'

echo
echo
echo "==> 5) MCP tools/call bookService_findById(id=1)"
curl -s -X POST "${MCP_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: ${SESSION}" \
  -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"bookService_findById","arguments":{"id":1}}}'

echo
echo
echo "==> 6) MCP tools/call list_all_books"
curl -s -X POST "${MCP_URL}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json, text/event-stream" \
  -H "Mcp-Session-Id: ${SESSION}" \
  -d '{"jsonrpc":"2.0","id":4,"method":"tools/call","params":{"name":"list_all_books","arguments":{}}}'

echo
echo
echo "==> Done. Compare with REST: curl ${BASE_URL}/api/books/1"
