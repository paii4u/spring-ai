package com.example.ai.configuration;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class CFAVectorStoreConfigurer {

    @Bean
    public SimpleVectorStore setUpCFAVectorStore(EmbeddingModel embeddingModel, CFAVectorStoreConfigurationProperties cfaVectorStoreConfigurationProperties) {
        SimpleVectorStore cfaSimpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();

        File cfaVectorStoreFile = new File(cfaVectorStoreConfigurationProperties.getVectorStorePath());
        if (cfaVectorStoreFile.exists())
            cfaSimpleVectorStore.load(cfaVectorStoreFile);
        else {
            cfaVectorStoreConfigurationProperties.getDocumentsToLoad().forEach(filePath -> {
                Resource resource = new PathResource(filePath);
                TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);

                TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
                List<Document> documents = tokenTextSplitter.split(tikaDocumentReader.get());
                cfaSimpleVectorStore.add(documents);
            });

            cfaSimpleVectorStore.save(cfaVectorStoreFile);
        }

        return cfaSimpleVectorStore;
    }
}
