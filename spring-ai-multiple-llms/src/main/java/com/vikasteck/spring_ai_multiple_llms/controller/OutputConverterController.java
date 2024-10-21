package com.vikasteck.spring_ai_multiple_llms.controller;

import com.vikasteck.spring_ai_multiple_llms.controller.response.ActorsFilms;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.converter.StructuredOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OutputConverterController {

    private final OllamaChatModel chatModel;

    public OutputConverterController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/api/youtubers")
    public List<String> generateJokes(@RequestParam(value = "youtuber", defaultValue = "tech") String youtuber) {
        System.out.println("Generating random jokes about "+youtuber+"...");
        String message = """
                List 10 of the most popular youtubers in {youtuber} along with their subscribers count. If you don't know the Answer,
                simple say "I don't know".
                {format}
                """;
        StructuredOutputConverter<List<String>> converter = new ListOutputConverter(new DefaultConversionService());
        var template = new PromptTemplate(message, Map.of("youtuber", youtuber, "format", converter.getFormat()));
        var prompt = template.create();
        String content = chatModel.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
        System.out.println("Content: "+content);
        return converter.convert(content);
    }

    @GetMapping("/api/movie")
    public Map<String, Object> getMovie() {
        System.out.println("Fetching movies...");
        StructuredOutputConverter<Map<String, Object>> converter = new MapOutputConverter();
        String message = """
                list out 10 most popular indian actor and their recent releases movie.
                {format}
                """;
        PromptTemplate template = new PromptTemplate(message, Map.of("format", converter.getFormat()));
        Prompt prompt = template.create();
        String content = chatModel.call(prompt).getResult().getOutput().getContent();
        System.out.println("Content: " +content);
        return converter.convert(content);
    }

    @GetMapping("/api/movies")
    public List<ActorsFilms> getMovieList() {
        System.out.println("Fetching movies...");
        StructuredOutputConverter<List<ActorsFilms>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorsFilms>>() {});
        String message = """
                list out 10 most popular indian actor and their recent releases movie.
                {format}
                """;
        PromptTemplate template = new PromptTemplate(message, Map.of("format", converter.getFormat()));
        Prompt prompt = template.create();
        String content = chatModel.call(prompt).getResult()
                .getOutput()
                .getContent();

        System.out.println(content);
        return converter.convert(content);
    }

}
