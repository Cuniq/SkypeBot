/*
 *    Copyright [2015] [Thanasis Argyroudis]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package skype.exceptions;

/**
 * The Class UnknownSkypeUserException. This exception is thrown then someone
 * searches for one invalid user.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class UnknownSkypeUserException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3083015571588530808L;

	/**
	 * Instantiates a new unknown skype user exception.
	 */
	public UnknownSkypeUserException() {

	}

	/**
	 * Instantiates a new unknown skype user exception.
	 *
	 * @param message
	 *            the message
	 */
	public UnknownSkypeUserException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new unknown skype user exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public UnknownSkypeUserException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new unknown skype user exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public UnknownSkypeUserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new unknown skype user exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 * @param enableSuppression
	 *            the enable suppression
	 * @param writableStackTrace
	 *            the writable stack trace
	 */
	public UnknownSkypeUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
