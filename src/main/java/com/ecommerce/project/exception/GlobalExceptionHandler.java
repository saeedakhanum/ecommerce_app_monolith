package com.ecommerce.project.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.project.payload.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		 Map<String,String> errorsMap=new HashMap<>();
		 /*
		 List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
		 errorList.forEach(error->{
			 String fieldName=((FieldError)error).getField();
			 String message=error.getDefaultMessage();
			 errorResponse.put(fieldName, message);
		 });
		 */
		 
		  List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		  fieldErrors.forEach(error->{
			  errorsMap.put(error.getField(), error.getDefaultMessage());
		  });
		 
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsMap);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,HttpServletRequest request){
		ErrorResponse errorResponse= new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage(),Instant.now(),request.getRequestURI());
		return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<ErrorResponse> handleAPIException(APIException ex,HttpServletRequest request){
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST.value(),ex.getMessage(),Instant.now(),request.getRequestURI());
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.BAD_REQUEST);
	}
}
