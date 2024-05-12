package dev.codescreen;


public class TransactionRequest {
    private String userId;
    private String messageId;
    private Amount transactionAmount;

    public TransactionRequest(){}
    TransactionRequest(String userId,String messageId,Amount transactionAmount){
        this.userId=userId;
        this.messageId=messageId;
        this.transactionAmount=transactionAmount;
    }

    public void setTransactionAmount(Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Amount getTransactionAmount() {
        return transactionAmount;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }




}
