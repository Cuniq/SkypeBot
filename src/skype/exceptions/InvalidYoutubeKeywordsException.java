package skype.exceptions;

public class InvalidYoutubeKeywordsException extends Exception {

	private static final long serialVersionUID = -3087714116803551031L;

	public InvalidYoutubeKeywordsException() {
	}

	public InvalidYoutubeKeywordsException(String message) {
		super(message);
	}

	public InvalidYoutubeKeywordsException(Throwable cause) {
		super(cause);
	}

	public InvalidYoutubeKeywordsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidYoutubeKeywordsException(	String message, Throwable cause, boolean enableSuppression,
											boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
