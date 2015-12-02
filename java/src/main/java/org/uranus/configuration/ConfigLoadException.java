package org.uranus.configuration;

public class ConfigLoadException extends Exception {

	private static final long serialVersionUID = -7800681594596184363L;

	public static enum ExceptionCode {
		CONFIG_ARGUMENT_INVALID, CONFIG_KEY_ACCESS_INVALID, CONFIG_KEY_NOT_FOUND, CONFIG_KEY_PARSE_FAILED
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
