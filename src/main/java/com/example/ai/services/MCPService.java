package com.example.ai.services;

import com.example.ai.models.Answer;
import com.example.ai.models.Question;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MCPService {

    private final ChatClient chatClient;

    public MCPService(ChatClient.Builder chatClientBuilder, List<McpSyncClient> mcpClients) {
        this.chatClient = chatClientBuilder
                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpClients))
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    public Answer getAnswerFromWeatherTool(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatClient.ChatClientRequestSpec chatClientRequestSpec = chatClient.prompt(prompt);
        ChatClient.CallResponseSpec callResponseSpec = chatClientRequestSpec.call();
        Generation result = callResponseSpec.chatResponse().getResult();

        String answerText = result.getOutput().getText();
        return new Answer(answerText);
    }
}
