package com.example.stoicopenai_backend.api;

import com.example.stoicopenai_backend.dtos.ChatCompletionRequest;
import com.example.stoicopenai_backend.dtos.MyResponse;
import com.example.stoicopenai_backend.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "https://blue-meadow-0677de303.4.azurestaticapps.net")
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
    // Endpoint to generate an image from a quote using DALL·E
    @PostMapping("/generate-image")
    public MyResponse generateImage(@RequestBody String quote) {
        // Delegate the call to the service layer to generate the image
        return service.generateImageFromQuote(quote);
    }

}


