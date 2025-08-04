package com.example.ai.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("app.cfa")
@Data
public class CFAVectorStoreConfigurationProperties {
    private List<String> documentsToLoad;
    private String vectorStorePath;
}
