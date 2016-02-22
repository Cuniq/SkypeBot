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
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

import com.skype.Chat;
import com.skype.SkypeException;

/**
 * The Class CommandGetAllCommands. This class is responsible for printing all
 * available commands in bot. If you ever want to add a new command you must add one
 * new line inside static block and increase total commands in {@link Command}.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandGetAllCommands extends Command {

	private Chat outputChat = null;

	/** The Constant commands. */
	private final static String[] commands = new String[getTotalCommands()];

	static {
		commands[0] = "help";
		commands[1] = "info";
		commands[2] = "spam";
		commands[3] = "getallcommands";
		commands[4] = "choosepoll";
		commands[5] = "addadmin";
		commands[6] = "removeadmin";
		commands[7] = "showadmins";
	}

	/**
	 * Instantiates a new command get all commands.
	 */
	public CommandGetAllCommands() {
		name = "getallcommands";
		description = "This command will provide a full list of all available commands.";
		usage = "!getallcommands";
	}

	public CommandGetAllCommands(Chat outputChat) {
		this();
		this.outputChat = outputChat;
	}

	/*
	 * (non-Javadoc)
	 * 
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
}
