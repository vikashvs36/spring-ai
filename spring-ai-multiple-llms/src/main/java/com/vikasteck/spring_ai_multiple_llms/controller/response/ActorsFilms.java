package com.vikasteck.spring_ai_multiple_llms.controller.response;

import java.util.List;

public record ActorsFilms(String actor, List<String> movies) { }