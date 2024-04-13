package com.example.stoicopenai_backend.api;

import com.example.stoicopenai_backend.dtos.MyResponse;
import com.example.stoicopenai_backend.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoicController.class)
class StoicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAiService openAiService;

    @Test
    void getQuotes_ReturnsQuotes_WhenGivenValidInput() throws Exception {
        String userInput = "courage";
        MyResponse mockResponse = new MyResponse("Some Stoic Quote");
        when(openAiService.getFiveQuotes(userInput)).thenReturn(mockResponse);

        mockMvc.perform(get("/quotes").param("userInput", userInput))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Some Stoic Quote")));
    }

    @Test
    void getExplanation_ReturnsExplanation_WhenGivenValidQuote() throws Exception {
        String quote = "The only way to achieve the impossible is to believe it is possible.";
        MyResponse mockResponse = new MyResponse("Explanation for the quote");
        when(openAiService.getExplanationForQuote(quote)).thenReturn(mockResponse);

        mockMvc.perform(get("/explanation").param("quote", quote))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Explanation for the quote")));
    }

    // You would add a similar test for the /generate-image endpoint
}


