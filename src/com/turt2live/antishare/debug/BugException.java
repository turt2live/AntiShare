package com.turt2live.antishare.debug;

public class BugException extends Exception {

	private static final long serialVersionUID = -6007168863217187302L;
	private Exception exception;

	public BugException(String message, Exception participant){
		super(message);
		this.exception = participant;
	}

	@Override
	public void printStackTrace(){
		super.printStackTrace();
		if(exception != null){
			System.out.println("PARTICIPATING EXCEPTION: ");
			exception.printStackTrace();
		}
	}

}
