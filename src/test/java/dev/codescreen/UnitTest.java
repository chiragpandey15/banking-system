package dev.codescreen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UnitTest {

    TransactionController underTest;
    
    

    Error expectedError;
    AuthorizationResponse expectedAuthorizationResponse;
    LoadResponse expectedLoadResponse;
    TransactionRequest transactionRequest;
    Amount amount;
    

    @BeforeEach
    void setUp(){
        underTest = new TransactionController();
        expectedError = new Error();
        expectedAuthorizationResponse = new AuthorizationResponse();
        expectedLoadResponse = new LoadResponse();
        transactionRequest = new TransactionRequest();
        amount=new Amount();
    }

    @Test
    void testNULLMessageID(){

        // Given
        String messageId = null;
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);
        transactionRequest.setMessageId("messageId");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testMisMatchMessageID(){

        // Given
        String messageId = "1234";
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);
        transactionRequest.setMessageId("messageId");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());
        
        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLMessageIDInTransaction(){

        // Given
        String messageId = "1234";
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);
        
        transactionRequest.setMessageId(null);
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLUserId(){

        // Given
        String messageId = "1234";
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);
        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId(null);
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLTransactionAmount(){

        // Given
        String messageId = "1234";
        
        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(null);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLAmount(){

        // Given
        String messageId = "1234";
        
        amount.setAmount(null);
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLCurrency(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency(null);
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNULLDebitOrCredit(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(null);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testWrongDebitOrCredit(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.DEBIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    @Test
    void testNegativeAmount(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("-500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.loadMoney(messageId, transactionRequest);

        expectedError = new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }


    @Test
    void testMisMatchCurrency(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        underTest.loadMoney(messageId, transactionRequest);

        Amount transactionAmount = new Amount("500","£",DebitCredit.CREDIT);
        transactionRequest.setTransactionAmount(transactionAmount);

        Error actual = (Error)underTest.loadMoney(messageId, transactionRequest);


        expectedError = new Error("CURRENCY MISMATCH", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }


    @Test
    void testLoadForCorrectInput(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        //when
        LoadResponse actual = (LoadResponse) underTest.loadMoney(messageId, transactionRequest);

        expectedLoadResponse.setUserId("userId");
        expectedLoadResponse.setMessageId("1234");
        expectedLoadResponse.setBalance(amount);

        assertEquals(expectedLoadResponse.getMessageId(), actual.getMessageId());
        assertEquals(expectedLoadResponse.getUserId(), actual.getUserId());
        assertEquals(expectedLoadResponse.getBalance().getAmount(), actual.getBalance().getAmount());
        assertEquals(expectedLoadResponse.getBalance().getCurrency(), actual.getBalance().getCurrency());
        assertEquals(expectedLoadResponse.getBalance().getDebitOrCredit(), actual.getBalance().getDebitOrCredit());
    }
    
    @Test
    void testAuthorizationForUserNotFound(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.DEBIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("4839r");
        transactionRequest.setTransactionAmount(amount);

        //when
        Error actual = (Error) underTest.authorization(messageId, transactionRequest);


        expectedError = new Error("USER NOT FOUND", HttpStatus.BAD_REQUEST.value());

        assertEquals(expectedError.getMessage(), actual.getMessage());
        assertEquals(expectedError.getCode(), actual.getCode());
    }

    
    @Test
    void testAuthorizationForInsufficientBalance(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("500");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        underTest.loadMoney(messageId, transactionRequest);

        Amount authorizationAmount = new Amount("600","$",DebitCredit.DEBIT);

        
        transactionRequest.setTransactionAmount(authorizationAmount);

        
        AuthorizationResponse actual = (AuthorizationResponse) underTest.authorization(messageId, transactionRequest);
        
        Amount expectedAmount = new Amount(amount.getAmount(),"$",DebitCredit.CREDIT);
        expectedAuthorizationResponse.setMessageId("1234");
        expectedAuthorizationResponse.setBalance(expectedAmount);
        expectedAuthorizationResponse.setUserId("userId");
        expectedAuthorizationResponse.setResponseCode(ResponseCode.DECLINED);

        assertEquals(expectedAuthorizationResponse.getMessageId(), actual.getMessageId());
        assertEquals(expectedAuthorizationResponse.getUserId(), actual.getUserId());
        assertEquals(expectedAuthorizationResponse.getBalance().getAmount(), actual.getBalance().getAmount());
        assertEquals(expectedAuthorizationResponse.getBalance().getCurrency(), actual.getBalance().getCurrency());
        assertEquals(expectedAuthorizationResponse.getBalance().getDebitOrCredit(), actual.getBalance().getDebitOrCredit());
        assertEquals(expectedAuthorizationResponse.getResponseCode(), actual.getResponseCode());
        
    }

    
    @Test
    void testAuthorizationForCorrectInput(){

        // Given
        String messageId = "1234";
        
        amount.setAmount("50");
        amount.setCurrency("$");
        amount.setDebitOrCredit(DebitCredit.CREDIT);

        transactionRequest.setMessageId("1234");
        transactionRequest.setUserId("userId");
        transactionRequest.setTransactionAmount(amount);

        underTest.loadMoney(messageId, transactionRequest);
        
        amount.setDebitOrCredit(DebitCredit.DEBIT);
        amount.setAmount("50");
        transactionRequest.setTransactionAmount(amount);

        AuthorizationResponse actual = (AuthorizationResponse) underTest.authorization(messageId, transactionRequest);

        Amount expectedAmount = new Amount("0.0","$",DebitCredit.DEBIT);

        expectedAuthorizationResponse.setMessageId("1234");
        expectedAuthorizationResponse.setBalance(expectedAmount);
        expectedAuthorizationResponse.setUserId("userId");
        expectedAuthorizationResponse.setResponseCode(ResponseCode.APPROVED);

        assertEquals(expectedAuthorizationResponse.getMessageId(), actual.getMessageId());
        assertEquals(expectedAuthorizationResponse.getUserId(), actual.getUserId());
        assertEquals(expectedAuthorizationResponse.getBalance().getAmount(), actual.getBalance().getAmount());
        assertEquals(expectedAuthorizationResponse.getBalance().getCurrency(), actual.getBalance().getCurrency());
        assertEquals(expectedAuthorizationResponse.getBalance().getDebitOrCredit(), actual.getBalance().getDebitOrCredit());
        assertEquals(expectedAuthorizationResponse.getResponseCode(), actual.getResponseCode());
    }
}
