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

import java.util.HashSet;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

import com.skype.Chat;
import com.skype.SkypeException;

/**
 * The Class CommandShowAdmins. Shows all registered admins.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandShowAdmins extends Command {

	/** The output chat. */
	private Chat outputChat = null;

	/** The admins. */
	private HashSet<String> admins = null;

	/**
	 * Instantiates a new command show admins.
	 */
	public CommandShowAdmins() {
		name = "showadmins";
		description = "Prints all bot's admins.";
		usage = "!showadmins";
	}

	/**
	 * Instantiates a new command show admins.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param admins
	 *            the admins
	 */
	public CommandShowAdmins(Chat outputChat, HashSet<String> admins) {
		this.outputChat = outputChat;
		this.admins = admins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException();
		
		try {
			for (String admin : admins) {
				outputChat.send(admin + "\r\n");
			}
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

}
