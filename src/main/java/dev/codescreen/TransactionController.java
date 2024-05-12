package dev.codescreen;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
public class TransactionController {

	/* hashmap to store transaction and balance
	 * userId: transaction
	 */
	HashMap<String,List<TransactionRequest>> userTransactions= new HashMap<String,List<TransactionRequest>>();
	HashMap<String,Amount> userBalance = new HashMap<String,Amount>();
	
	boolean validateTransactionRequest(String messageId,TransactionRequest transactionRequest,String transactionType){
		if(messageId==null || transactionRequest.getMessageId()==null 
		|| transactionRequest.getUserId()==null || transactionRequest.getTransactionAmount() == null
		|| transactionRequest.getTransactionAmount().getAmount()==null || transactionRequest.getTransactionAmount().getCurrency()==null
		|| transactionRequest.getTransactionAmount().getDebitOrCredit() == null
		){
			return false;
		}else if(!messageId.equals(transactionRequest.getMessageId())){
			return false;
		}else if(transactionType.equals("authorization") 
		&& transactionRequest.getTransactionAmount().getDebitOrCredit()!=DebitCredit.DEBIT){
			return false;
		}else if(transactionType.equals("load") 
		&& transactionRequest.getTransactionAmount().getDebitOrCredit()!=DebitCredit.CREDIT){
			return false;
		}else if(transactionRequest.getTransactionAmount().amountToFloat()<0){
			return false;
		}
		return true;
	}


	void saveTransaction(TransactionRequest transactionRequest){
		
		if(userTransactions.containsKey(transactionRequest.getUserId())){
			List<TransactionRequest>transactions = userTransactions.get(transactionRequest.getUserId());
			transactions.add(transactionRequest);
			userTransactions.put(transactionRequest.getUserId(), transactions);
		}else{

			List<TransactionRequest>transactions = new ArrayList<TransactionRequest>();
			transactions.add(
				new TransactionRequest(transactionRequest.getUserId(),
				transactionRequest.getMessageId(),transactionRequest.getTransactionAmount()));
			userTransactions.put(transactionRequest.getUserId(), transactions);
		}
	}


	@GetMapping("/ping")
	public String ping() {
		DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd — HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return datetimeFormatter.format(now);
	}

	@PutMapping("authorization/{messageId}")
	public Object authorization(@PathVariable String messageId, @RequestBody TransactionRequest transactionRequest) {
		
		//  Check if user is present in our data or not
		if(!userBalance.containsKey(transactionRequest.getUserId())){
			return new Error("USER NOT FOUND", HttpStatus.BAD_REQUEST.value());
		}

		// Validate Transaction Request : Check if all the fields are provided and are correct or not
		if(!validateTransactionRequest(messageId,transactionRequest,"authorization")){
			return new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());
		}		

		// Get user's current balance
		Amount balance = userBalance.get(transactionRequest.getUserId());
		// Get transaction amount 
		Amount transactionAmount = transactionRequest.getTransactionAmount();
		
		/*
		Check currency: If the currency of the previous load is different from current transaction; discard
			the request
		*/
		if(!balance.getCurrency().equals(transactionAmount.getCurrency())){
			return new Error("CURRENCY MISMATCH", HttpStatus.BAD_REQUEST.value());
		}

		// Save transaction as per the instruction
		saveTransaction(transactionRequest);

		// Check if the user has sufficient balance amount
		if(balance.amountToFloat()<transactionAmount.amountToFloat()){
			
			// Create response as per the given schema
			AuthorizationResponse newTransaction =
		 	new AuthorizationResponse(transactionRequest.getUserId(), messageId,balance,ResponseCode.DECLINED);
			 
			return newTransaction;
		}
		
		// Deduct amount from the user balance
		balance.debit(transactionAmount.amountToFloat());
		balance.setDebitOrCredit(DebitCredit.DEBIT);
		
		// Update user balance for convenient calculation
		userBalance.put(transactionRequest.getUserId(), balance);

		// Create response as per the given schema
		AuthorizationResponse newTransaction =
		 new AuthorizationResponse(transactionRequest.getUserId(), messageId,balance,ResponseCode.APPROVED);

		return newTransaction;

	}

	@PutMapping("load/{messageId}")
	public Object loadMoney(@PathVariable String messageId, @RequestBody TransactionRequest transactionRequest) {
		
		// Validate Transaction Request : Check if all the fields are provided and are correct or not
		if(!validateTransactionRequest(messageId,transactionRequest,"load")){
			return new Error("INVALID REQUEST", HttpStatus.BAD_REQUEST.value());
		}

		// If this is the first time user
		if(!userBalance.containsKey(transactionRequest.getUserId())){
			// Save the transaction
			saveTransaction(transactionRequest);

			Amount balance = transactionRequest.getTransactionAmount();
			
			// add user to the user balance for convenient calculation
			userBalance.put(transactionRequest.getUserId(), balance);
			
			// Create response as per the given schema
			LoadResponse newTransaction =
			new LoadResponse(transactionRequest.getUserId(), messageId,balance);
			
			return newTransaction;
		}
		
		// if user already exist

		// Get user's current balance
		Amount balance = userBalance.get(transactionRequest.getUserId());
		// Get transaction amount 
		Amount transactionAmount = transactionRequest.getTransactionAmount();
		
		/*
		Check currency: If the currency of the previous load is different from current transaction; discard
			the request
		*/
		
		if(!balance.getCurrency().equals(transactionAmount.getCurrency())){
			return new Error("CURRENCY MISMATCH", HttpStatus.BAD_REQUEST.value());
		}
		
		// Save transaction as per the instruction
		saveTransaction(transactionRequest);

		// Add amount in the user balance
		balance.credit(transactionAmount.amountToFloat());
		balance.setDebitOrCredit(DebitCredit.CREDIT);

		// Update user balance for convenient calculation
		userBalance.put(transactionRequest.getUserId(), balance);
		
		// Create response as per the given schema
		LoadResponse newTransaction =
			new LoadResponse(transactionRequest.getUserId(), messageId,balance);

		return newTransaction;
	}

	@RequestMapping("/**")
	public Error error() {
		return new Error("API ENDPOINT NOT FOUND",HttpStatus.BAD_REQUEST.value());
	}

}