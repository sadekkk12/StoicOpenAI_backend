package com.example.stoicopenai_backend.api;

import com.example.stoicopenai_backend.dtos.ChatCompletionRequest;
import com.example.stoicopenai_backend.dtos.MyResponse;
import com.example.stoicopenai_backend.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*")
public class StoicController {

    private OpenAiService service;

    //@Autowired
    public StoicController(OpenAiService service) {
        this.service = service;
    }

    @GetMapping("/quotes")
    public MyResponse getQuotes(@RequestParam String userInput) {

        return service.getFiveQuotes(userInput);
    }
    // Endpoint to get an explanation for a quote
    @GetMapping("/explanation")
    public MyResponse getExplanation(@RequestParam String quote) {
        // Delegate the call to the service layer
        return service.getExplanationForQuote(quote);
    }
}


