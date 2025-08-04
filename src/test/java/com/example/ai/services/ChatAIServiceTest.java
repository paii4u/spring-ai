package com.example.ai.services;

import com.example.ai.models.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatAIServiceTest {

    @Autowired
    private ChatAIService chatAIService;

    @Test
    void getAnswer() {
        Question question = new Question("What do you know about paii4u");
        chatAIService.getAnswer(question);
    }

    @Test
    void getCapital() {
        Question question = new Question("Oregon");
        chatAIService.getCapital(question);
    }
}