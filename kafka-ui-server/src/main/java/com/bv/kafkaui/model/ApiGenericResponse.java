package com.bv.kafkaui.model;

public class ApiGenericResponse {

	private boolean error;
	private String status;
	private String errorDescription;
	private String exception;
	private String errors;
	private String message;

	public ApiGenericResponse() {
		super();
	}
	
	public ApiGenericResponse(boolean error, String message) {
		super();
		this.error = error;
		this.message = message;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiGenericResponse [error=" + error + ", status=" + status + ", errorDescription=" + errorDescription
				+ ", exception=" + exception + ", errors=" + errors + ", message=" + message + "]";
	}

	
}