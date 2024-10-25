package com.eugene.aiwebtester.ai.model;

public class AIQuestion {

    private String id;
    private String question;
    private String voice;
    private String conversationId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
