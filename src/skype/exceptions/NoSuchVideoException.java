package skype.exceptions;

public class NoSuchVideoException extends Exception {

	private static final long serialVersionUID = -7856742875701504453L;

	public NoSuchVideoException() {
	}

	public NoSuchVideoException(String message) {
		super(message);
	}

	public NoSuchVideoException(Throwable cause) {
		super(cause);
	}

	public NoSuchVideoException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchVideoException(String message, Throwable cause, boolean enableSuppression,
								boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
