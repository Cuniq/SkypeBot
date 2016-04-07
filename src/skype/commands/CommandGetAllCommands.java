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

import java.util.List;

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.ErrorPopup;
import skype.gui.popups.WarningPopup;
import skype.utils.CommandClassFinder;

/**
 * The Class CommandGetAllCommands. This class is responsible for printing all
 * available commands in bot. In order for this class to know the existence of other
 * commands it uses the {@link CommandClassFinder} to scan the source code, and more
 * specific the commands package in order to find the other commands. Then we
 * instantiate each command once and we take its name. For more information see
 * {@link #initiateCommandsName()}.
 * 
 * @author Thanasis Argyroudis
 * @see CommandClassFinder
 * @see Command
 * @since 1.0
 */
public class CommandGetAllCommands extends Command {

	private Chat outputChat = null;

	private static String[] commandsName = initiateCommandsName();

	/**
	 * It is basically instantiate each command class once and retrieves its name.
	 */
	private static String[] initiateCommandsName() {
		final List<Class<Command>> commandsList = CommandClassFinder
			.findCommandClasses();
		String[] commands = new String[commandsList.size()];

		int i = 0;
		for (Class<Command> commandClass : commandsList) {
			try {
				Command commandInstace = commandClass.newInstance();
				commands[i++] = commandInstace.getName();
			} catch (InstantiationException e) {
				new ErrorPopup(e.getMessage());
			} catch (IllegalAccessException e) {
				new ErrorPopup(e.getMessage());
			}
		}
		return commands;
	}

	public CommandGetAllCommands() {
		super(
				"getallcommands",
				"This command will provide a full list of all available commands.",
				"!getallcommands");
	}

	public CommandGetAllCommands(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {
			for (String command : commandsName) {
				outputChat.send(command);
			}
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
	}
}
