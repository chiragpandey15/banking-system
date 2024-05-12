package dev.codescreen;

enum ResponseCode{
    APPROVED,
    DECLINED
}

public class AuthorizationResponse {
    private String userId;
    private String messageId;
    private Amount balance;
    private String responseCode;

    AuthorizationResponse(){}
    AuthorizationResponse(String userId,String messageId,Amount transactionAmount,ResponseCode responseCode){
        this.userId=userId;
        this.messageId=messageId;
        this.balance=transactionAmount;
        this.responseCode = responseCode==ResponseCode.APPROVED?"APPROVED":"DECLINED";
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

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode==ResponseCode.APPROVED?"APPROVED":"DECLINED";
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

    public String getResponseCode() {
        return responseCode;
    }

}
