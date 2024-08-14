package com.caju_authorization;

import com.caju_authorization.model.dto.CreateTransactionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authorizeReturnsSuccessCode() throws Exception {
        CreateTransactionDto transaction = new CreateTransactionDto();

        transaction.setAccountId("2");
        transaction.setMcc("00");
        transaction.setAmount(new BigDecimal("1"));
        transaction.setMerchant("ZÉ DA ESQUINA");
        transaction.setIdempotencyKey(UUID.randomUUID());

        mockMvc.perform(post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":\"01\"}"));
    }

    @Test
    void authorizeReturnsInsufficientBalance() throws Exception {
        CreateTransactionDto transaction = new CreateTransactionDto();

        transaction.setAccountId("1");
        transaction.setMcc("00");
        transaction.setAmount(new BigDecimal("200"));
        transaction.setMerchant("ZÉ DA ESQUINA");
        transaction.setIdempotencyKey(UUID.randomUUID());

        mockMvc.perform(post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":\"51\"}"));
    }

    @Test
    void authorizeReturnsErrorWhenNegativeAmount() throws Exception {
        CreateTransactionDto transaction = new CreateTransactionDto();

        transaction.setAccountId("1");
        transaction.setMcc("00");
        transaction.setAmount(new BigDecimal("-20"));
        transaction.setMerchant("ZÉ DA ESQUINA");
        transaction.setIdempotencyKey(UUID.randomUUID());

        mockMvc.perform(post("/authorize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":\"07\"}"));
    }

}
