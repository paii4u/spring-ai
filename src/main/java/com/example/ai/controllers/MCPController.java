package com.example.ai.controllers;

import com.example.ai.models.Answer;
import com.example.ai.models.Question;
import com.example.ai.services.MCPService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/mcp")
public class MCPController {

    private final MCPService mcpService;

    public MCPController(MCPService mcpService) {
        this.mcpService = mcpService;
    }

    @PostMapping("/mcp")
    public Answer answerWithMCP(@RequestBody Question question) {
        return mcpService.getAnswerFromWeatherTool(question);
    }
}
