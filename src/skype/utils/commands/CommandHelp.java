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

import java.util.HashMap;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

import com.skype.Chat;
import com.skype.SkypeException;


/**
 * The Class CommandHelp. This class is responsible for printing all informations
 * about a command. If you ever want to add a new command you must add one new line
 * inside static block and increase total commands in {@link Command}.
 * 
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandHelp extends Command {

	/** The commands. */
	private final static HashMap<String, String> commands = new HashMap<String, String>(getTotalCommands());
	
	/** The output chat. */
	private Chat outputChat = null;

	/** The search command. */
	private String searchCommand = null;

	static {
		commands.put("help", getBoth(new CommandHelp()));
		commands.put("info", getBoth(new CommandInfo()));
		commands.put("spam", getBoth(new CommandSpam()));
		commands.put("getallcommands", getBoth(new CommandGetAllCommands()));
		commands.put("choosepoll", getBoth(new CommandChoosePoll()));
		commands.put("addadmin", getBoth(new CommandAddAdmin()));
		commands.put("showadmins", getBoth(new CommandShowAdmins()));
		commands.put("removeadmin", getBoth(new CommandRemoveAdmin()));
	}

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
	}

	/**
	 * Execute.
	 *
	 * @throws CommandException
	 *             the command exception
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
	 * Gets the both.
	 *
	 * @param command
	 *            the command
	 * @return the both
	 */
	private static String getBoth(Command command) {
		return "Description: " + command.getDescription() + "." + "\r\n" + "Usage: " + command.getUsage();
	}

}
