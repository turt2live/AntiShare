/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
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
