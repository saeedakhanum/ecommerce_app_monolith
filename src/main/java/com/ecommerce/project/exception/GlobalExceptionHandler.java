package com.ecommerce.project.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		 Map<String,String> errorResponse=new HashMap<>();
		 List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
		 errorList.forEach(error->{
			 String fieldName=((FieldError)error).getField();
			 String message=error.getDefaultMessage();
			 errorResponse.put(fieldName, message);
		 });
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
		String message=ex.getMessage();
		return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<String> handleAPIException(APIException e){
		String message=e.getMessage();
		return new ResponseEntity<String>(message,HttpStatus.NOT_FOUND);
	}
}
