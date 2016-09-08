package com.adm.web.controllers;

import com.adm.exceptions.NotLoggedInException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.hibernate.exception.ConstraintViolationException;
import com.adm.exceptions.DuplicateEntryException;

@ControllerAdvice
public class AppWideExceptionHandler {

	// Duplicate Entries
	@ExceptionHandler({DuplicateEntryException.class, ConstraintViolationException.class})
	public String duplicateEntryFound() {
		return "error/duplicaat";
	}


	// Not Logged In
	@ExceptionHandler(NotLoggedInException.class)
	public String notLoggedIn() {
		return "error/notLoggedIn";
	}
}
