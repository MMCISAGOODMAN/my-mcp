package com.example.mymcp.service;

import io.mcpplus.autoconfigure.annotation.McpExclude;
import org.springframework.stereotype.Service;

@Service
@McpExclude
public class InternalMetricsService {

    public String rebuildCache() {
        return "cache rebuilt";
    }
}
