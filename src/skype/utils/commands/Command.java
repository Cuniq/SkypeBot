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
package skype.utils.commands;

import skype.exceptions.CommandException;
import skype.handlers.CommandHandler;

/**
 * The Class Command is the upperclass of all available commands. It implements the
 * basic prototype that every command should and must follow. It has 3 very basic
 * function:
 * <p>
 * <ul>
 * <li>The static function {@link #execute()} is basically the one which executes the
 * command
 * <li>The static function {@link #getDescription()} which prints useful information
 * about what does and how works a specific command
 * <li>The protected static function {@link #getUsage()} which in case of receiving
 * wrong arguments will called by Execute to print the correct usage
 * </ul>
 * <p>
 * Every class which implements this class must have the default constructor which
 * gives values at protected fields of this class. We need that in order to
 * {@link CommandHelp} help command works properly.
 * <p>
 * In order to add a new command you will need to make the following changes
 * <p>
 * <ul>
 * <li>Increase by one <code>{@link #TOTAL_COMMANDS}.
 * <li>Edit properly the {@link CommandHelp}. ( Read its javadoc for further
 * informations )
 * <li>Edit properly the {@link CommandGetAllCommands}. ( Read its javadoc for
 * further informations )
 * <li>Add handler for this command at {@link CommandHandler}
 * </ul>
 *
 * @author Thanasis Argyroudis
 * @since v1.0
 */

public abstract class Command {

	/** The Constant TOTAL_COMMANDS. */
	private final static int TOTAL_COMMANDS = 8;

	/** The name of command. */
	protected String name = "Empty name";

	/** The description of command. */
	protected String description = "Empty description";

	/** The usage of command. */
	protected String usage = "Empty usage";

	protected Command() {

	}
	
	/**
	 * Executes the command.
	 * 
	 * @throws CommandException
	 */
	abstract public void execute() throws CommandException;

	/**
	 * Gets the description.
	 *
	 * @return The String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the usage.
	 *
	 * @return The String usage
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Gets the total commands.
	 *
	 * @return the total commands
	 */
	public static int getTotalCommands() {
		return TOTAL_COMMANDS;
	}

 } 