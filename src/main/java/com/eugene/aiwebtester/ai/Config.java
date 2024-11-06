package com.eugene.aiwebtester.ai;

import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.ai.ollama.api.OllamaApi;

@Configuration
class Config {

    @Bean
    InMemoryChatMemory InMemoryChatMemory() {
        return new InMemoryChatMemory();
    }

}