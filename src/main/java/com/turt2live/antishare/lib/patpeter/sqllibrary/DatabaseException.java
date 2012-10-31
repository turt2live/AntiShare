/**
 * 
 */
package com.turt2live.antishare.lib.patpeter.sqllibrary;

/**
 * DatabaseException
 * Created: 2012-03-21 21:51
 * 
 * @author Solin
 * 
 */
public class DatabaseException extends Exception {
	private static final long serialVersionUID = 3063547825200154629L;

	public DatabaseException(){
		super("Unknown database error");
	}

	public DatabaseException(String message){
		super(message);
	}
}
