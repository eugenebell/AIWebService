package com.eugene.aiwebtester.ai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eugene.aiwebtester.ai.model.Question;
import com.eugene.aiwebtester.ai.service.AIChatService;
import org.springframework.ui.Model;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class AIController {

    private static final Logger LOG = LogManager.getLogger(AIController.class);
    private AIChatService aiChatService;
    private String conversationId = "genericIDForNow";

    AIController(AIChatService acs) {
        this.aiChatService = acs;
    }

    @GetMapping("/question")
    public String questionForm(Model model) {
        LOG.info("Setting form");
        model.addAttribute("question", new Question());
        return "question";
    }

    @PostMapping("/question")
    public String question(@ModelAttribute Question question, Model model) {
        LOG.info("Question asked is :" + question.getQuestion());
        String resp = aiChatService.generate(question.getQuestion(), conversationId, 10);
        question.setAnswer(resp);
        model.addAttribute("question", question);
        // model.addAttribute("resp", resp);
        LOG.info("Response is " + resp);
        return "question";
    }

    @PostMapping("/questions")
    public String questions(@ModelAttribute Question question, Model model) {
        LOG.info("Question asked is :" + question.getQuestion());
        Map<Integer, Question> questions = aiChatService.generateAndTrack(question.getQuestion(), conversationId, 10);
        // question.setAnswer(resp);
        model.addAttribute("questions", questions);
        model.addAttribute("listOfKeys", questions.keySet());
        // model.addAttribute("resp", resp);
        LOG.info("Response is " + questions);
        return "question";
    }

    @GetMapping("/clear")
    public void clearMemoryAndHistory() {
        LOG.info("Clearing Memory and History");
        aiChatService.clearMemoryAndHistory(conversationId);
    }

    @GetMapping("/voice")
    public void setVoice(@RequestParam(value = "voice", defaultValue = "Normal") String voice) {
        LOG.info("Set voice to : " + voice);
        aiChatService.setVoice(formatStringToSentence(voice));
    }

    @GetMapping("/resetvoice")
    public void resetVoice(String arg) {
        aiChatService.resetVoice();
        LOG.info("Voice is reset to : " + aiChatService.getVoice());
    }

    @GetMapping("/getvoice")
    public String getVoice() {
        LOG.info("Voice is set to : " + aiChatService.getVoice());
        return aiChatService.getVoice();
    }

    @GetMapping("/directquestion")
    public String directQuestion(
            @RequestParam(value = "question", defaultValue = "What are you") String question) {
        // String question = formatStringToSentence(question);
        LOG.info("direct question is :" + question);
        String resp = aiChatService.generate(question, conversationId, 10);
        LOG.info("------------- " + resp);
        return resp;
    }

    public void writeToFile(String path) {
        String file = formatStringToSentence(path);
        LOG.info("Output path is :" + file);
        try {
            aiChatService.writeToFile(file);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String formatStringToSentence(String args) {
        return args.replaceAll(",", " ");
    }
}