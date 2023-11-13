package com.example.stoicopenai_backend.service;

import com.example.stoicopenai_backend.dtos.ChatCompletionRequest;
import com.example.stoicopenai_backend.dtos.ChatCompletionResponse;
import com.example.stoicopenai_backend.dtos.DalleResponse;
import com.example.stoicopenai_backend.dtos.MyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@Service
public class OpenAiService {

    public static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

    @Value("${app.api-key}")
    private String API_KEY;

    //See here for a decent explanation of the parameters send to the API via the requestBody
    //https://platform.openai.com/docs/api-reference/completions/create

    public final static String URL = "https://api.openai.com/v1/chat/completions";
    public final static String MODEL = "gpt-4";
    public final static double TEMPERATURE = 0.8;
    public final static int MAX_TOKENS = 300;
    public final static double FREQUENCY_PENALTY = 0.0;
    public final static double PRESENCE_PENALTY = 0.0;
    public final static double TOP_P = 1.0;

    private WebClient client;

    public OpenAiService() {
        this.client = WebClient.create();
    }
    //Use this constructor for testing, to inject a mock client
    public OpenAiService(WebClient client) {
        this.client = client;
    }

    public MyResponse makeRequest(String userPrompt, String _systemMessage) {

        ChatCompletionRequest requestDto = new ChatCompletionRequest();
        requestDto.setModel(MODEL);
        requestDto.setTemperature(TEMPERATURE);
        requestDto.setMax_tokens(MAX_TOKENS);
        requestDto.setTop_p(TOP_P);
        requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
        requestDto.setPresence_penalty(PRESENCE_PENALTY);
        requestDto.getMessages().add(new ChatCompletionRequest.Message("system", _systemMessage));
        requestDto.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String err =  null;
        try {
            json = mapper.writeValueAsString(requestDto);
            System.out.println(json);
            ChatCompletionResponse response = client.post()
                    .uri(new URI(URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(json))
                    .retrieve()
                    .bodyToMono(ChatCompletionResponse.class)
                    .block();
            String responseMsg = response.getChoices().get(0).getMessage().getContent();
            int tokensUsed = response.getUsage().getTotal_tokens();
            System.out.print("Tokens used: " + tokensUsed);
            System.out.print(". Cost ($0.0015 / 1K tokens) : $" + String.format("%6f",(tokensUsed * 0.0015 / 1000)));
            System.out.println(". For 1$, this is the amount of similar requests you can make: " + Math.round(1/(tokensUsed * 0.0015 / 1000)));
            return new MyResponse(responseMsg);
        }
        catch (WebClientResponseException e){
            //This is how you can get the status code and message reported back by the remote API
            logger.error("Error response status code: " + e.getRawStatusCode());
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            err = "Internal Server Error, due to a failed request to external service. You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
        catch (Exception e) {
            logger.error("Exception", e);
            err = "Internal Server Error - You could try again" +
                    "( While you develop, make sure to consult the detailed error message on your backend)";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, err);
        }
    }
// FIND BETTER PROMPT
    public MyResponse getFiveQuotes(String userInput) {
        String quotesSystemMessage = "You are an assistant that provides stoic quotes to help someone navigate" +
                " a difficult situation or emotion. Your task is to understand the context of the user's emotion" +
                " or experience and provide 5 relevant stoic quotes from diverse sources like" +
                " Marcus Aurelius, Seneca, or Epictetus. If 5 quotes are generated they should be the only thing given back" +
                "and no extra text and no numbers in front each quote.  Be sensitive to the user's emotional state," +
                " ensuring that your responses are supportive and empathetic." +
                " If the user's prompt is unclear, vague, or seems nonsensical," +
                " politely ask for clarification to better understand their needs.";

        //TODO make sure it doesnt go to explanation mode if a faulty prompt is given

        // Use the existing makeRequest method to send the quote request to OpenAI
        return makeRequest(userInput, quotesSystemMessage);
    }
    public MyResponse getExplanationForQuote(String quote) {
        String explanationPrompt = "Explain this quote: \"" + quote + "\"";
        String systemMessageForExplanation = "You are an assistant that provides insightful explanations of Stoic quotes."
               + " When explaining the quote \"" + quote + "\""
        + ", focus on its universal relevance, illustrating how it can be applied to various life situations. "
              +  "Offer multiple interpretations, if applicable, to provide a richer understanding."
               + " Include historical and philosophical context to highlight how the quote reflects Stoic principles."
               + " Encourage reflection by posing questions that help the user connect with the quote's meaning,"
               + " and offer practical suggestions for its application in real-life scenarios."
               + " Ensure your explanation is motivational, clear, and easily understandable, catering to a wide";


        // Use the existing makeRequest method to send the explanation prompt to OpenAI
        return makeRequest(explanationPrompt, systemMessageForExplanation);
    }
    // Add a method for generating an image using DALL·E 3
    // Method to generate an image from a quote using DALL·E 3

    //TODO FIND A BETTER PROMPT
    //TODO FIND A FITTING SIZE
    public MyResponse generateImageFromQuote(String quote) {
        // Endpoint for DALL·E 3 image generation
        String quoteMeaning = "Create a clear and interpretative drawing that visually represents the essence of this quote: \""
                + quote + "\". The drawing should be straightforward and easily understandable." +
                " No text, messages nor symbols are allowed in the images. " +
                "Instead, focus on visual symbols or scenes that convey the quote's meaning in a way that is easy" +
                " to understand and helps the viewer grasp the concept of the quote.";

        String dalleEndpoint = "https://api.openai.com/v1/images/generations";

        // Construct the request body according to DALL·E 3 API specification
        Map<String, Object> requestBody = Map.of(
                "model", "dall-e-3",
                "prompt", quoteMeaning,
                "n", 1,
                "size", "1024x1024"
                // Add other parameters as needed
        );

        try {
            // Make the POST request to DALL·E 3 API
            DalleResponse dalleResponse = client.post()
                    .uri(new URI(dalleEndpoint))
                    .header("Authorization", "Bearer " + API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(DalleResponse.class)
                    .block();

            // Assuming the response contains a list of generated images
            String imageUrl = null;
            if (dalleResponse != null && !dalleResponse.getData().isEmpty()) {
                imageUrl = dalleResponse.getData().get(0).getUrl();
            }
            return new MyResponse(imageUrl);
        } catch (WebClientResponseException e) {
            // Handle exceptions from WebClient
            logger.error("Error response status code: " + e.getRawStatusCode());
            logger.error("Error response body: " + e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.valueOf(e.getRawStatusCode()), "Error generating image: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions
            logger.error("Exception", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating image from DALL·E API.");
        }
    }

}