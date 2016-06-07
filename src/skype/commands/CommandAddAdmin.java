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

	private HashSet<String> currentAdmins = null;

	private String id = null;

	public CommandAddAdmin() {
		super(
				"addadmin",
				"Adds one more admin for the bot. Some commands require admin privileges in order to work",
				"Syntax: !addadmin <skype_id>");

	}

	public CommandAddAdmin(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {

			if (id.equals("")) {
				outputChat.send(getSyntax());
				return;
			}

			if (currentAdmins.add(id))
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
		this.currentAdmins = data.getAdmins();
		String options[] = data.getCommandOptions();

		if (options.length != 0)
			id = options[USER_ID_POSITION];
		else
			id = "";
	}

}
