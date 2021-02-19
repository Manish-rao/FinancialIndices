package com.sol.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonParseException;
@ControllerAdvice
public class MyExceptionHandler {
		/*
		 * @ExceptionHandler(EntityNotFoundException.class) public final
		 * ResponseEntity<ExceptionMessage>
		 * EntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		 * ExceptionMessage error = new ExceptionMessage("Not found", ex.getMessage());
		 * return new ResponseEntity<ExceptionMessage>(error, HttpStatus.BAD_REQUEST); }
		 */
	    
	   
	    
		/*
		 * @ExceptionHandler(NumberFormatException.class) public final
		 * ResponseEntity<ExceptionMessage> NumberFormatException(NumberFormatException
		 * ex, WebRequest request) { ExceptionMessage error = new
		 * ExceptionMessage("Not a valid number", ex.getMessage()); return new
		 * ResponseEntity<ExceptionMessage>(error, HttpStatus.BAD_REQUEST); }
		 */
	    
	    @ExceptionHandler(JsonParseException.class)
	    public final ResponseEntity<ExceptionMessage> JsonParserException(JsonParseException ex, WebRequest request) {
	        ExceptionMessage error = new ExceptionMessage("Error parsing JSON", ex.getMessage());
	        return new ResponseEntity<ExceptionMessage>(error, HttpStatus.BAD_REQUEST);
	    }
	    

	    @ExceptionHandler(ConstraintViolationException.class)
	    public final ResponseEntity<ExceptionMessage> Constra(ConstraintViolationException ex, WebRequest request) {
	        ExceptionMessage error = new ExceptionMessage("Please check input values", ex.getMessage());
	        return new ResponseEntity<ExceptionMessage>(error, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    @ResponseBody
	    public Map<String,String> handleValidationFailure(MethodArgumentNotValidException e) {

	        Map<String, String> errors = new HashMap<>();

	        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
	            errors.put(fieldError.getObjectName() + fieldError.getField(),
	                       fieldError.getDefaultMessage());
	        }

	        return errors;
	    }
	    
	    
		
	
}
