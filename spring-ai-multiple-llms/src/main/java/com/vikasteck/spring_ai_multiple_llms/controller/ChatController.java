package com.vikasteck.spring_ai_multiple_llms.controller;

import com.vikasteck.spring_ai_multiple_llms.controller.response.ActorsFilms;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/ai/actor")
    public ActorsFilms actorsFilm() {
        System.out.println("Fetching Actors movies...");
        return chatClient.prompt()
                .user("Generate the filmography for a random actor.")
                .call()
                .entity(ActorsFilms.class);
    }

    @GetMapping("/ai/actors")
    public List<ActorsFilms> actorsFilmsList() {
        System.out.println("Fetching Random Actors upcoming 5 movies...");
        return chatClient.prompt()
                .user("Generate the upcoming filmographies for a random 5 actor.")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorsFilms>>() {});
    }

}
