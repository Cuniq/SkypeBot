/*
 *    Copyright [2016] [Thanasis Argyroudis]

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
 * The Class NullOutputChatException.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class NullOutputChatException extends CommandException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2502967741322212915L;

	/**
	 * Instantiates a new null output chat exception.
	 */
	public NullOutputChatException() {

	}

	/**
	 * Instantiates a new null output chat exception.
	 *
	 * @param message
	 *            the message
	 */
	public NullOutputChatException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new null output chat exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public NullOutputChatException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a new null output chat exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public NullOutputChatException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new null output chat exception.
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
	public NullOutputChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
