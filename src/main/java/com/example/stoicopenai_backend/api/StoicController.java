package com.example.stoicopenai_backend.api;

import com.example.stoicopenai_backend.dtos.ChatCompletionRequest;
import com.example.stoicopenai_backend.dtos.MyResponse;
import com.example.stoicopenai_backend.service.OpenAiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*")
public class StoicController {

    private OpenAiService service;
    final static String SYSTEM_MESSAGE = "You are an assistant that provides stoic quotes to help someone overcome a difficult situation\n" +
            "\n" +
            "I want you to provide 5 stoic quotes to the given prompt.\n" +
            "\n" +
            "if the the prompt given seems to not make sense, ask for another prompt. ";

    public StoicController(OpenAiService service) {
        this.service = service;
    }

    @GetMapping
    public MyResponse getQuotes(@RequestParam String about) {

        return service.makeRequest(about,SYSTEM_MESSAGE);
    }
    // New POST endpoint
    // FOR LATER
  /*  @PostMapping("/chat")
    public MyResponse handleChatRequest(@RequestBody ChatCompletionRequest chatRequest) {
        // You should validate the request here as needed

        // Assuming SYSTEM_MESSAGE is a static field in OpenAiService
        String systemMessage = OpenAiService.SYSTEM_MESSAGE;
        // Use the first user message to get quotes
        String userPrompt = chatRequest.getMessages().get(0).getContent();

        return service.makeRequest(userPrompt, systemMessage);
    }

   */
}

