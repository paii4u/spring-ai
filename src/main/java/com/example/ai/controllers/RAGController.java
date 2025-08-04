package com.example.ai.controllers;

import com.example.ai.models.Answer;
import com.example.ai.models.Question;
import com.example.ai.services.ChatAIService;
import com.example.ai.services.RAGService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cfa")
public class RAGController {

    private final ChatAIService chatAIService;
    private final RAGService ragService;

    public RAGController(ChatAIService chatAIService, RAGService ragService) {
        this.chatAIService = chatAIService;
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public Answer answer(@RequestBody Question question) {
        return ragService.getAnswer(question);
    }
}
