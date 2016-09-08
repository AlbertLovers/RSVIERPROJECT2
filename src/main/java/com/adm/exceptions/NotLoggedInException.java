package com.adm.exceptions;

/**
 * Created by Milan_Verheij on 26-08-16.
 *
 * Error for when not logged in and trying to order etc.
 */
public class NotLoggedInException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8708572352660085880L;

	public NotLoggedInException() {}

    public NotLoggedInException(String message) {
        super(message);
    }
}
