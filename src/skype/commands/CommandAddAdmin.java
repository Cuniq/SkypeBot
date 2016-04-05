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

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

/**
 * The Class CommandAddAdmin. This command adds one admin for the bot. Please not
 * that the bot if not checking if the given id exists. It just adds it. An invalid
 * id is not safety threatening (because the user don't exist).
 * 
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandAddAdmin extends Command {

	private static final int USER_ID_POSITION = 0;

	private Chat outputChat = null;

	/** Reference to admins hashset. */
	private HashSet<String> admins = null;

	private String id = null;

	/**
	 * Instantiates a new command add admin.
	 */
	public CommandAddAdmin() {
		super(
				"addadmin",
				"Adds one more admin for the bot. Some commands require admin privileges in order to work",
				"!addadmin <skype_id>");

	}

	/**
	 * Instantiates a new command add admin.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param userId
	 *            the user id of user to added as admin
	 * @param admins
	 *            a reference to hashset which holds current admins.
	 */
	public CommandAddAdmin(CommandData data) {
		this();
		initializeCommand(data);
	}

	/**
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {

			if (id.equals("")) {
				outputChat.send(getSyntax());
				return;
			}

			if (admins.add(id))
				outputChat.send("Admin successfully added");
			else
				outputChat.send("All ready exists.");
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		this.admins = data.getAdmins();
		String options[] = data.getCommandOptions();
		if (options.length != 0)
			id = options[USER_ID_POSITION];
		else
			id = "";
	}

}
