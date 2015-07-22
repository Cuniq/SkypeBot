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
 * The exception UnknownUserException is throwed when someone tries to have recieve information about one user who doesn't exist.
 */
public class UnknownUserException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8132318039598766705L;

	/**
	 * Instantiates a new wrong user exception.
	 */
	public UnknownUserException() {
		super();
	}

	/**
	 * Instantiates a new wrong user exception.
	 *
	 * @param message the message
	 */
	public UnknownUserException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new wrong user exception.
	 *
	 * @param cause the cause
	 */
	public UnknownUserException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new wrong user exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public UnknownUserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new wrong user exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public UnknownUserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
