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
package skype.commands;

import skype.exceptions.CommandException;
import skype.utils.ClassFinder;
import skype.utils.CommandInvoker;

/**
 * The Class Command is the upperclass of all available commands. It implements the
 * basic prototype that every command should and must follow. It has 3 very basic
 * function:
 * <p>
 * <ul>
 * <li>The static function {@link #execute()} is basically the one which gets
 * executed from the {@link CommandInvoker}
 * <li>The static function {@link #getDescription()} which prints useful information
 * about what does and how works a specific command
 * <li>The static function {@link #getUsage()} which in case of receiving wrong
 * arguments will called by Execute to print the correct usage
 * </ul>
 * <p>
 * Every class which implements this class must have the default constructor which
 * gives values at protected fields of this class. We need that in order to
 * {@link CommandHelp help} command work properly.
 * <p>
 * In order to add a new command you just add the new command inside commands package
 * and the system automatically finds and registers the new command at runtime.
 * Please <b>note</b> that the new command must be inside the commands package or
 * else it won't be detected. The way which commands are getting registered it is
 * rather simple. First the {@link #TOTAL_COMMANDS} gets updated based on
 * {@link ClassFinder} findings. Then we need to update {@link CommandHelp} and
 * {@link CommandGetAllCommands} because those two need to know the existence of
 * other commands in order to work. Again based on {@link ClassFinder} findings we
 * update them and make them work. For further information how these two are working
 * you can see there classes.
 * <p>
 * If you want to add a new command somewhere else then you can do two things.
 * <ul>
 * <li>Change the {@link ClassFinder} and make him search and other packages too. Now
 * he just searches inside commands package only.
 * <li>Or add them in one more static way like hard-code them.
 * </ul>
 * <p>
 * Don't try initiating commands in a static blocks because all static blocks inside
 * Commands are initiating in random (maybe?) order we are getting errors. So we drop
 * static blocks and we initiate fields on the creation of the first object.
 * 
 * @author Thanasis Argyroudis
 * @see CommandInvoker
 * @see ClassFinder
 * @since v1.0
 */

public abstract class Command {

	/** The Constant TOTAL_COMMANDS. */
	private final static int TOTAL_COMMANDS = ClassFinder.findCommandClasses().size();

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
	 * Gets the description of command.
	 *
	 * @return The String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the usage of command.
	 *
	 * @return The String usage
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Gets the name of command.
	 *
	 * @return The name of command.
	 */
	public String getName() {
		return name;
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