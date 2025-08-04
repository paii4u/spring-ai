package com.example.ai.controllers;

import com.example.ai.models.Answer;
import com.example.ai.models.GetCapitalResponse;
import com.example.ai.models.Question;
import com.example.ai.services.ChatAIService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/ask")
public class ChatController {

    private ChatAIService chatAIService;

    @PostMapping
    public Answer answer(@RequestBody Question question) {
        return chatAIService.getAnswer(question);
    }

    @PostMapping("system-reply")
    public Answer answerWithCustomReply(@RequestBody Question question) {
        return chatAIService.getAnswerFromSystemUser(question);
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody Question question) {
        return chatAIService.getCapital(question);
    }

    @PostMapping("/capital/json-format")
    public GetCapitalResponse getCapitalInJsonResponse(@RequestBody Question question) {
        return chatAIService.getCapitalInJson(question);
    }

    @PostMapping("/capital/with-info")
    public Answer getCapitalWithInfo(@RequestBody Question question) {
        return chatAIService.getCapitalWithInfo(question);
    }
}
