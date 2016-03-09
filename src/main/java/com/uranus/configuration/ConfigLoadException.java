package com.uranus.configuration;

/**
 * Exception of configure loader
 * 
 * There is some useful error cdoe information overwrite 
 * {@link Exception} getMessage() before {@link Exception}'s message.
 * it can be used for java logical statement 
 * 
 * @author Michael xixuan.lx
 *
 */
public class ConfigLoadException extends Exception {

	private static final long serialVersionUID = -7800681594596184363L;

	/**
	 * Error Code
	 * 
	 * @author Michael
	 *
	 */
	public enum ExceptionCode {
		ARGUMENT_INVALID, KEY_ACCESS_INVALID, KEY_NOT_FOUND, VALUE_PARSE_FAILED
	}
	
	private final ExceptionCode error;

	public ConfigLoadException(ExceptionCode error, String msg) {
		super(msg);
		this.error = error;
	}
	
	public ExceptionCode getExceptionCode() {
		return error;
	}

	@Override
	public String getMessage() {
		return String.format("error : %s, msg : %s", error.name(), super.getMessage());
	}
	
	
}
