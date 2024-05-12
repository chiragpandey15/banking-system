package dev.codescreen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPing() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.get("/ping")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.matchesPattern("\\d{4}/\\d{2}/\\d{2} — \\d{2}:\\d{2}:\\d{2}")));
    }


    @Test
    public void testInvalidPath() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/invalidPath"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("API ENDPOINT NOT FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()));
    }


    @Test
    public void testLoadMoneyEndpoint() throws Exception {
        
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUserId("user123");
        transactionRequest.setMessageId("123");
        transactionRequest.setTransactionAmount(new Amount("100", "USD",DebitCredit.CREDIT));

        
        String requestJson = objectMapper.writeValueAsString(transactionRequest);

        
        mockMvc.perform(MockMvcRequestBuilders.put("/load/{messageId}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("user123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("100"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.currency").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.debitOrCredit").value("CREDIT"));
    }


    @Test
    public void testAuthorizationEndpoint() throws Exception {
        
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUserId("user123");
        transactionRequest.setMessageId("123");
        transactionRequest.setTransactionAmount(new Amount("100", "USD",DebitCredit.CREDIT));

        
        String requestJson = objectMapper.writeValueAsString(transactionRequest);


        //Create User
        mockMvc.perform(MockMvcRequestBuilders.put("/load/{messageId}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        transactionRequest.setTransactionAmount(new Amount("50", "USD",DebitCredit.DEBIT));
        requestJson = objectMapper.writeValueAsString(transactionRequest);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/authorization/{messageId}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("user123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("150.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.currency").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.debitOrCredit").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("APPROVED"));
    }


    @Test
    public void testAuthorizationForInsufficientBalance() throws Exception {
        
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUserId("user123");
        transactionRequest.setMessageId("123");
        transactionRequest.setTransactionAmount(new Amount("100", "USD",DebitCredit.CREDIT));

        
        String requestJson = objectMapper.writeValueAsString(transactionRequest);


        //Create User
        mockMvc.perform(MockMvcRequestBuilders.put("/load/{messageId}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        transactionRequest.setTransactionAmount(new Amount("350", "USD",DebitCredit.DEBIT));
        requestJson = objectMapper.writeValueAsString(transactionRequest);
        

        mockMvc.perform(MockMvcRequestBuilders.put("/authorization/{messageId}", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("user123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.amount").value("250.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.currency").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance.debitOrCredit").value("CREDIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("DECLINED"));
    }

}
