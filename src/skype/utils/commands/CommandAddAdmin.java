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

import java.util.HashSet;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

import com.skype.Chat;
import com.skype.SkypeException;

/**
 * The Class CommandAddAdmin. This command adds one admin for the bot.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandAddAdmin extends Command {

	/** The output chat. */
	private Chat outputChat = null;

	/** The admins. */
	private HashSet<String> admins = null;

	/** The id. */
	private String id = null;

	/**
	 * Instantiates a new command add admin.
	 */
	public CommandAddAdmin() {
		name = "addadmin";
		description = "Adds one more admin for the bot. Some commands require admin privileges in order to work";
		usage = "!addadmin <skype_id>";
	}

	/**
	 * Instantiates a new command add admin.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param userId
	 *            the user id
	 * @param admins
	 *            the admins
	 */
	public CommandAddAdmin(Chat outputChat, String userId, HashSet<String> admins) {
		this();
		this.outputChat = outputChat;
		this.admins = admins;
		id = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {
			if (admins.add(id))
				outputChat.send("Admin successfully added");
			else
				outputChat.send("All ready exists.");
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

}
