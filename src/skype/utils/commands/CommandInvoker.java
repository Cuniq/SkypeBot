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
package skype.utils.commands;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandInvoker. This is the invoker class. It is responsible for
 * executing the giving command. Please not that the execution of a command is not
 * necessary only made from this class. A command sometimes may execute itself.
 * 
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandInvoker {

	// Logger

	private CommandInvoker() {

	}

	/**
	 * Execute.
	 *
	 * @param cmd
	 *            the command to executed
	 */
	public static void execute(Command cmd) {
		cmd.execute();
	}

}
