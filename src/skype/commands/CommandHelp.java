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

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;
import skype.utils.ClassFinder;


/**
 * The Class CommandHelp. This class is responsible for printing all informations
 * about a command. In order for this class to know the existence of other commands
 * it uses the {@link ClassFinder} to scan the source code, and more specific the
 * commands package in order to find the other commands. Once it founds them it
 * creates one hashmap with holds the name of the command and information about how
 * the command is used. In order to get usage and description from a command we need
 * to instance one object. We are doing that using reflection. For more information
 * see {@link #initiateHelpForCommands}
 *
 * @author Thanasis Argyroudis
 * @see ClassFinder
 * @see Command
 * @since 1.0
 */
public class CommandHelp extends Command {

	/** The commands. */
	private static HashMap<String, String> commands = null;
	
	/** The output chat. */
	private Chat outputChat = null;

	/** The search command. */
	private String searchCommand = null;

	/**
	 * Instantiates a new command help.
	 */
	public CommandHelp() {
		name = "Help";
		description = "This command will provide informations about commands and how to use them";
		usage = "!help <command_name>";
	}

	/**
	 * Instantiates a new command help.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param commandName
	 *            the command name
	 */
	public CommandHelp(Chat outputChat, String commandName) {
		this();
		this.outputChat = outputChat;
		this.searchCommand = commandName;
		initiateHelpForCommands();
	}

	/**
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {
			String result = commands.get(searchCommand.toLowerCase());

			if (result == null)
				outputChat.send("Unknown command");
			else
				outputChat.send(result);

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	/**
	 * Initiates help for commands. Basically it initiates the HashMap. The hashmap
	 * takes the name of the command and one concatenated string of description and
	 * usage of one command. We are taking those informations by instantiating one
	 * object of each command class.
	 */
	private void initiateHelpForCommands() {
		if (commands != null)
			return;
		commands = new HashMap<String, String>(getTotalCommands());

		for (Class<?> commandClass : ClassFinder.findCommandClasses()) {
			try {
				Command commandInstace = (Command) commandClass.newInstance();
				commands.put(commandInstace.getName(), getBoth(commandInstace));
			} catch (InstantiationException e) {
				new ErrorPopup(e.getMessage());
			} catch (IllegalAccessException e) {
				new ErrorPopup(e.getMessage());
			}
		}
	}

	/**
	 * Gets the both description and usage string from a command and concatenates
	 * them.
	 *
	 * @param command
	 *            the command to get the fields
	 * @return One string having both fields together.
	 */
	private String getBoth(Command command) {
		return "Description: " + command.getDescription() + "." + "\r\n" + "Usage: "
				+ command.getUsage();
	}
}
