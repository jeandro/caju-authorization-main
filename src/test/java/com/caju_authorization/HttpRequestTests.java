package com.caju_authorization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HttpRequestTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void nonExistingRouteReturns404() throws Exception {
        mockMvc.perform(get("/non-existent-route")).andExpect(status().isNotFound());
    }

}
