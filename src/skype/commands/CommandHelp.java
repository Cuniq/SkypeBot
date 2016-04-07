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

import java.util.HashMap;
import java.util.List;

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;
import skype.utils.CommandClassFinder;


/**
 * The Class CommandHelp. This class is responsible for printing all informations
 * about a command. In order for this class to know the existence of other commands
 * it uses the {@link CommandClassFinder}. In order to get usage and description from
 * a command we need to instance one object. We are doing that using reflection. For
 * more information see {@link #initiateHelpForCommands}
 *
 * @author Thanasis Argyroudis
 * @see CommandClassFinder
 * @see Command
 * @since 1.0
 */
public class CommandHelp extends Command {

	private static final int COMMAND_NAME_POSITION = 0;

	/**
	 * Holds the name of the command and its description and syntax of command
	 * concatenated.
	 */
	private static HashMap<String, String> commandInformations = initiateHelpForCommands();
	
	private Chat outputChat = null;

	private String commandNameToFindHelp = null;

	/**
	 * Initiates help for commands. Basically it initiates the HashMap. The hashmap
	 * takes the name of the command and one concatenated string of description and
	 * usage of one command. We are taking those informations by instantiating one
	 * object of each command class.
	 */
	private static HashMap<String, String> initiateHelpForCommands() {
		final List<Class<Command>> classList = CommandClassFinder
			.findCommandClasses();
		HashMap<String, String> commands = new HashMap<>(classList.size());

		for (Class<?> commandClass : classList) {
			try {
				Command commandInstace = (Command) commandClass.newInstance();
				commands.put(commandInstace.getName(), getBoth(commandInstace));
			} catch (InstantiationException e) {
				new ErrorPopup(e.getMessage());
			} catch (IllegalAccessException e) {
				new ErrorPopup(e.getMessage());
			}
		}
		return commands;
	}

	private static String getBoth(Command command) {
		return "Description: " + command.getDescription() + "." + "\r\n" +
			"Usage: " + command.getSyntax();
	}

	public CommandHelp() {
		super(
				"help",
				"This command will provide informations about commands and how to use them",
				"!help <command_name>");
	}

	public CommandHelp(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {
			String result = commandInformations
				.get(commandNameToFindHelp.toLowerCase());

			if (result == null)
				outputChat.send(getSyntax());
			else
				outputChat.send(result);

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private void initializeCommand(CommandData data) {
		String options[] = data.getCommandOptions();

		this.outputChat = data.getOutputChat();
		if (options.length != 0)
			this.commandNameToFindHelp = options[COMMAND_NAME_POSITION];
		else
			this.commandNameToFindHelp = "";
	}

}
