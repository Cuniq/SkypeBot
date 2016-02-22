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
 * The Class CommandException. This is the upper exception for commands.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7581690869513491833L;

	/**
	 * Instantiates a new command exception.
	 */
	public CommandException() {
	}

	/**
	 * Instantiates a new command exception.
	 *
	 * @param message
	 *            the message
	 */
	public CommandException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new command exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public CommandException(Throwable cause) {
		super(cause);

	}

	/**
	 * Instantiates a new command exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public CommandException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * Instantiates a new command exception.
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
	public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
