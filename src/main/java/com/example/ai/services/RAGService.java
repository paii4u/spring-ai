package com.example.ai.services;

import com.example.ai.models.Answer;
import com.example.ai.models.Question;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
//import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RAGService {

    @Value("classpath:templates/cfa-rag-prompt-template.st")
    private Resource cfaRagPromptTemplate;

    private final ChatModel chatModel;
    private final SimpleVectorStore simpleVectorStore;

    public RAGService(ChatModel chatModel, SimpleVectorStore simpleVectorStore) {
        this.chatModel = chatModel;
        this.simpleVectorStore = simpleVectorStore;
    }

    public Answer getAnswer(Question question) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question.question())
                .topK(5)
                .build();
        List<Document> documents = simpleVectorStore.doSimilaritySearch(searchRequest);

        PromptTemplate promptTemplate = new PromptTemplate(cfaRagPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("input", question.question(),
                "documents", documents));

        ChatResponse chatResponse = chatModel.call(prompt);
        Answer answer = new Answer(chatResponse.getResult().getOutput().getText());
        System.out.println(answer);
        return answer;
    }
}
