package com.example.stoicopenai_backend.api;
import com.example.stoicopenai_backend.dtos.MyResponse;
import com.example.stoicopenai_backend.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(MockitoExtension.class)
class StoicControllerTest {

    @Mock
    private OpenAiService openAiService;

    @InjectMocks
    private StoicController stoicController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stoicController).build();
    }

    @Test
    void getQuotes_ReturnsQuotes_WhenGivenValidPrompt() throws Exception {
        // Arrange
        String about = "courage";
        MyResponse mockResponse = new MyResponse();
        mockResponse.setAnswer("Some Stoic Quote");
        when(openAiService.makeRequest(about, StoicController.SYSTEM_MESSAGE)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/").param("about", about))
                .andExpect(status().isOk())
                // Use jsonPath to assert the value of the "answer" key
                .andExpect(jsonPath("$.answer", is("Some Stoic Quote")));
    }
}
