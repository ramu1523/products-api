package com.tgt.myretail.products.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);
	
	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorResponse> appExp(AppException ex) {
		LOGGER.error("<Exception>"+ ex.getMessage());
		ErrorResponse advice = new ErrorResponse();
		if(ex.getMessage()!=null && ex.getMessage().contains("404 Not Found"))
		{
			advice.setMessage("Redsky API Error: Product Not Found");
			return new ResponseEntity<>(advice, HttpStatus.NOT_FOUND);
		}else if(ex.getMessage()!=null  && ex.getMessage().contains("Product not found"))
		{
			advice.setMessage("Database Error: Product Not Found");
			return new ResponseEntity<>(advice, HttpStatus.NOT_FOUND);
		}
		advice.setMessage(ex.getMessage());
		return new ResponseEntity<>(advice, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
