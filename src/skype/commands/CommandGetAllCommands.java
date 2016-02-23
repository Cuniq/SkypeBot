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

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;
import skype.utils.ClassFinder;

/**
 * The Class CommandGetAllCommands. This class is responsible for printing all
 * available commands in bot. In order for this class to know the existence of other
 * commands it uses the {@link ClassFinder} to scan the source code, and more
 * specific the commands package in order to find the other commands. Then we
 * instantiate each command once and we take its name. We are not taking name from
 * class name because we don't know if everyone follows our command-naming
 * conventions. For more information see {@link #initiateArrayWithCommands()}.
 * 
 * @author Thanasis Argyroudis
 * @see ClassFinder
 * @see Command
 * @since 1.0
 */
public class CommandGetAllCommands extends Command {

	private Chat outputChat = null;

	/** The Constant commands. */
	private static String[] commands = null;

	/**
	 * Instantiates a new command get all commands.
	 */
	public CommandGetAllCommands() {
		name = "getallcommands";
		description = "This command will provide a full list of all available commands.";
		usage = "!getallcommands";
		initiateArrayWithCommands();
	}

	/**
	 * Instantiates a new command get all commands.
	 *
	 * @param outputChat
	 *            the output chat
	 */
	public CommandGetAllCommands(Chat outputChat) {
		this();
		this.outputChat = outputChat;
	}

	/**
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {
			for (String command : commands) {
				outputChat.send(command);
			}
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	/**
	 * Initiate commands. Commands are being initiated on the first creation of the
	 * first object.
	 */
	private void initiateArrayWithCommands() {
		if (commands != null)
			return;
		commands = new String[getTotalCommands()];

		int i = 0;
		for (Class<?> commandClass : ClassFinder.findCommandClasses()) {
			try {
				Command commandInstace = (Command) commandClass.newInstance();
				//We could just take the command from class name,
				//but we are not sure that everyone going to follow our command-naming conventions.
				commands[i++] = commandInstace.getName();
			} catch (InstantiationException e) {
				new ErrorPopup(e.getMessage());
			} catch (IllegalAccessException e) {
				new ErrorPopup(e.getMessage());
			}
		}
	}
}
