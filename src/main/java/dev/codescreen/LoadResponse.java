package dev.codescreen;

public class LoadResponse {
    private String userId;
    private String messageId;
    private Amount balance;

    public LoadResponse(){}
    public LoadResponse(String userId,String messageId,Amount transactionAmount){
        this.userId=userId;
        this.messageId=messageId;
        this.balance=transactionAmount;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Amount getBalance() {
        return balance;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserId() {
        return userId;
    }

}
