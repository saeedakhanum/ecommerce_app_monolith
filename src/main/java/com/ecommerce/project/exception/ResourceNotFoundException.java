package com.ecommerce.project.exception;

public class ResourceNotFoundException extends RuntimeException {
	String resourceName;
	String fieldName;
	Long fieldId;
	
	public ResourceNotFoundException() {
			
	}
	
	
	public ResourceNotFoundException(String resourceName,String fieldName,Long fieldId) {
		super(String.format("%s not found with %s : %d", resourceName,fieldName,fieldId));
		this.resourceName=resourceName;
		this.fieldName=fieldName;
		this.fieldId=fieldId;
	}
}
