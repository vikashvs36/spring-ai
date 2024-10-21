package com.vikasteck.spring_ai_multiple_llms.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class JokeController {

    @Value("classpath:/message.st")
    private Resource resource;

    private final OllamaChatModel chatModel;

    @Autowired
    public JokeController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "msg", defaultValue = "Tell me a joke") String message) {
        Map<String, String> generation = Map.of("generation", chatModel.call(message));
        System.out.println(generation);
        return generation;
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "msg", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

    @GetMapping("/ai/youtube")
    public String listOfTopYoutubers(@RequestParam(value = "genre", defaultValue = "tech") String genre) {
        String message = """
                List 10 of the most popular youtubers in {genre} along with their subscribers count. If you don't know the Answer,
                simple say "I don't know".
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
    }

    @GetMapping("/ai/jokes")
    public String getRandomJoke() {
        System.out.println("Generating random jokes...");
        var system = new SystemMessage( "Don't tell any jokes except dog");
        var message = new UserMessage("Tell me a cat joke");
        Prompt prompt = new Prompt(List.of(message, system));
        String content = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
        System.out.println("Content: "+content);
        return content;
    }

    @GetMapping("/api/jokes")
    public String generateJokesaaa(@RequestParam(value = "subject", defaultValue = "dog") String subject) {


        System.out.println("Generating random jokes...");
        var template = new PromptTemplate(resource);
        var prompt = template.create(Map.of("subject", subject));
        String content = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
        System.out.println("Content: "+content);
        return content;
    }


}