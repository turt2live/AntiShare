package com.turt2live.antishare.money;

/**
 * For easy returning
 * 
 * @author turt2live
 */
public class TransactionResult {

	public static final TransactionResult NO_VAULT = new TransactionResult("You do not have Vault installed.", false);
	public static final TransactionResult NO_TAB = new TransactionResult("Tab feature disabled", false);

	/**
	 * Message applied to the result
	 */
	public final String message;
	/**
	 * State of completion
	 */
	public final boolean completed;

	/**
	 * Creates a new Transaction Result
	 * 
	 * @param message the message
	 * @param completed true if completed
	 */
	public TransactionResult(String message, boolean completed){
		this.message = message;
		this.completed = completed;
	}
}
