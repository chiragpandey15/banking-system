package dev.codescreen;


enum DebitCredit{
    DEBIT,
    CREDIT
}

public class Amount {
    private String amount;
    private String currency;
    private DebitCredit debitOrCredit;

    Amount(){}

    Amount(String amount,String currency, DebitCredit debitOrCredit){
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public DebitCredit getDebitOrCredit() {
        return debitOrCredit;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    /***********/

    public Float amountToFloat(){
        return Float.valueOf(amount);
    }
    
    public void setAmountFloat(Float amount){
        this.amount = Float.toString(amount);
    }

    public void debit(Float debitAmount){
        Float updatedAmount = amountToFloat()-debitAmount;
        setAmountFloat(updatedAmount);
    }

    public void credit(Float creditAmount){
        Float updatedAmount = amountToFloat() + creditAmount;
        setAmountFloat(updatedAmount);
    }

}
