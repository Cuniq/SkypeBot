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
import skype.utils.CommandClassFinder;
import skype.utils.CommandInvoker;

/**
 * The Class Command is the upperclass of all available commands. It implements the
 * basic prototype that every command should and must follow. Every class which
 * implements this class must have the default constructor which gives values at
 * private fields of this class. We need that in order to {@link CommandHelp help}
 * command work properly.
 * <p>
 * In order to add a new command you just add the new command class inside commands
 * package and the system automatically finds and registers the new command at
 * runtime. Please <b>note</b> that the new command must be inside the commands
 * package or else it won't be detected. The way which commands are getting
 * registered it is rather simple. First the {@link #TOTAL_COMMANDS} gets updated
 * based on {@link CommandClassFinder} findings. Then we need to update
 * {@link CommandHelp} and {@link CommandGetAllCommands} because those two need to
 * know the existence of other commands in order to work. Again based on
 * {@link CommandClassFinder} findings we update them and make them work. For further
 * information how these two are working you can see there classes.
 * <p>
 * If you want to add a new command in a different package then you can do either of
 * these two things.
 * <ul>
 * <li>Change the {@link CommandClassFinder} and make him search and other packages
 * too. Now he just searches inside commands package only.
 * <li>Or hard-code them inside {@link CommandHelp} and {@link CommandGetAllCommands}
 * command and increase {@link #TOTAL_COMMANDS}. I do not recommend that way.
 * </ul>
 * <p>
 * 
 * @author Thanasis Argyroudis
 * @see CommandInvoker
 * @see CommandClassFinder
 * @since v1.0
 */

public abstract class Command {
	
	private static final int TOTAL_COMMANDS = CommandClassFinder
		.findCommandClasses().size();

	private final String name;

	private final String description;

	private final String syntax;

	public Command(	String commandName, String commandDescription,
					String commandSyntax) {
		name = commandName;
		description = commandDescription;
		syntax = commandSyntax;
	}
	
	/**
	 * Executes the command. If user gave wrong/invalid arguments the methods throws
	 * an exception.
	 * 
	 * @throws CommandException
	 */
	abstract public void execute() throws CommandException;

	/**
	 * The needed information for one command in order to be executed is passed to it
	 * with a {@link CommandData} object. So each Command must have a setter for this
	 * data.
	 */
	abstract public void setData(CommandData data);

	public String getDescription() {
		return description;
	}

	public String getSyntax() {
		return syntax;
	}

	public String getName() {
		return name;
	}

	public int getTotalCommands() {
		return TOTAL_COMMANDS;
	}

 } 