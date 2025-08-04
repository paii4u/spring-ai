package com.example.ai.services;

import com.example.ai.models.Answer;
import com.example.ai.models.GetCapitalResponse;
import com.example.ai.models.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatAIService {

    private final ChatModel chatModel;

    private final ChatClient chatClient;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource capitalPromptTemplate;

    @Value("classpath:templates/get-capital-system-response-prompt.st")
    private Resource capitalSystemResponsePromptTemplate;

    @Value("classpath:templates/get-capital-prompt-json-response.st")
    private Resource capitalPromptJsonResponseTemplate;

    @Value("classpath:templates/get-capital-prompt-with-info.st")
    private Resource capitalWithInfoPromptTemplate;

    public ChatAIService(ChatModel chatModel, ChatClient.Builder chatClientBuilder) {
        this.chatModel = chatModel;
        this.chatClient = chatClientBuilder.build();
    }

    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatResponse promptResponse = chatModel.call(prompt);

        Generation result = promptResponse.getResult();
        String answerText = result.getOutput().getText();
        return new Answer(answerText);
    }

    public Answer getAnswerUsingChatClient(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatClient.ChatClientRequestSpec chatClientRequestSpec = chatClient.prompt(prompt);
        ChatClient.CallResponseSpec callResponseSpec = chatClientRequestSpec.call();
        Generation result = callResponseSpec.chatResponse().getResult();

        String answerText = result.getOutput().getText();
        return new Answer(answerText);
    }

    public Answer getAnswerFromSystemUser(Question question) {
        PromptTemplate userRequestPromptTemplate = new PromptTemplate(question.question());
        Message userMessage = userRequestPromptTemplate.createMessage();

        PromptTemplate systemMessageTemplate = new PromptTemplate(capitalSystemResponsePromptTemplate);
        Message systemMessage = systemMessageTemplate.createMessage();

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ChatResponse promptResponse = chatModel.call(prompt);
        String answerText = promptResponse.getResult().getOutput().getText();
        return new Answer(answerText);
    }

    public Answer getCapital(Question stateOrCountryQuestion) {
        PromptTemplate promptTemplate = new PromptTemplate(capitalPromptTemplate);
//        promptTemplate.add("stateOrCountry", stateOrCountryQuestion.question()); //this or below can be used.
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountryQuestion.question()));
        System.out.println("Question : " + prompt.getContents());
        ChatResponse promptResponse = chatModel.call(prompt);
        String answerText = promptResponse.getResult().getOutput().getText();
        System.out.println("Answer : " + answerText);
        return new Answer(answerText);
    }

    public GetCapitalResponse getCapitalInJson(Question stateOrCountryQuestion) {
        BeanOutputConverter<GetCapitalResponse> beanOutputConverter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = beanOutputConverter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(capitalPromptJsonResponseTemplate);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountryQuestion.question(), "format", format));
        System.out.println("Question : " + prompt.getContents());

        ChatResponse promptResponse = chatModel.call(prompt);
        String answerText = promptResponse.getResult().getOutput().getText();
        System.out.println("Answer : " + answerText);

        return beanOutputConverter.convert(answerText);
    }

    public Answer getCapitalWithInfo(Question stateOrCountryQuestion) {
        PromptTemplate promptTemplate = new PromptTemplate(capitalWithInfoPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", stateOrCountryQuestion.question()));
        System.out.println("Question : " + prompt.getContents());
        ChatResponse promptResponse = chatModel.call(prompt);
        String answerText = promptResponse.getResult().getOutput().getText();
        System.out.println("Answer : " + answerText);
        return new Answer(answerText);
    }
}
