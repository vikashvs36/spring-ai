package com.vikasteck.spring_ai_multiple_llms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @GetMapping("/joke")
    public String getJoke(@RequestParam String joke) {
        // Logic to fetch a joke from a Joke API
        String content = chatClient.prompt()
                .user(joke.isEmpty() ? "Tell me a joke" : joke)
                .call()
                .content();
        log.info(content);
        return content;
    }

}
