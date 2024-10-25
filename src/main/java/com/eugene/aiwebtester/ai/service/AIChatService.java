package com.eugene.aiwebtester.ai.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eugene.aiwebtester.ai.model.Question;

import org.springframework.ai.autoconfigure.retry.SpringAiRetryAutoConfiguration;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.OpenAiChatModel;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

@Service
public class AIChatService {

    private static final Logger LOG = LogManager.getLogger(AIChatService.class);
    private final static String DEFAULT_NAME = "Normal";
    private final ChatClient chatClient;
    private Map<String, String> responseTracker = new HashMap<String, String>();
    private Map<Integer, Question> responseQuestionTracker = new HashMap<Integer, Question>();
    private String voice = DEFAULT_NAME;
    private ChatMemory chatMemory;

    @Autowired
    public AIChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    // @GetMapping("/ai")
    // ResponseEntity<String> completion(
    // @RequestParam(value = "message", defaultValue = "Tell me a joke") String
    // message) {
    // LOG.info("------------- completion");
    // String content = chatClient.prompt(message).call().content();
    // LOG.info("------------- " + content);
    // return ResponseEntity.ok(content);
    // }

    // @Autowired
    // public OllamaChatService(ChatClient.Builder chatClientBuilder) {
    // this.chatClient = chatClientBuilder.build();
    // }

    @Autowired
    public void setChatMemory(InMemoryChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    public String generate(String message, String conversationId, int chatHistoryWindowSize) {
        String resp = chatClient.prompt()
                .system("You are a friendly chat bot that answers question in the voice of " + voice)
                .advisors(new MessageChatMemoryAdvisor(chatMemory, conversationId, chatHistoryWindowSize))
                .user(message)
                .call()
                .content();
        int size = responseTracker.size();
        if (size == 0) {
            LOG.info("- Size is: " + size);
            responseTracker.put(responseTracker.size() + "", resp);
        } else {
            LOG.info("Size is: " + size);
            responseTracker.put(responseTracker.size() + "", resp);
        }
        return resp;
    }

    public Map<Integer, Question> generateAndTrack(String message, String conversationId, int chatHistoryWindowSize) {
        String resp = chatClient.prompt()
                .system("You are a friendly chat bot that answers question in the voice of " + voice)
                .advisors(new MessageChatMemoryAdvisor(chatMemory, conversationId, chatHistoryWindowSize))
                .user(message)
                .call()
                .content();

        Question q = new Question();

        q.setQuestion(message);
        q.setAnswer(resp);
        q.setId(responseQuestionTracker.size());
        responseQuestionTracker.put(responseQuestionTracker.size(), q);
        LOG.info("- Size after is: " + responseQuestionTracker.size());

        return responseQuestionTracker;
    }

    public void clearMemoryAndHistory(String conversationId) {
        chatMemory.clear(conversationId);
        responseTracker.clear();
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getVoice() {
        return voice;
    }

    public String resetVoice() {
        voice = DEFAULT_NAME;
        return voice;
    }

    public void writeToFile(String path) throws IOException {
        int size = responseTracker.values().size();
        File f = new File(path);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(f);

        for (int i = 0; i < size; i++) {
            String op = responseTracker.get(i + "");
            LOG.info(op);
            outputStream.write(op.getBytes());
            outputStream.write(
                    "\n\n ----------------------------------------------------------------------------------------- \n\n"
                            .getBytes());

        }
        outputStream.close();
    }

}
