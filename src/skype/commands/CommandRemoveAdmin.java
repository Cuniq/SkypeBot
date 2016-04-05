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

import static skype.utils.users.BotUserInfo.getUserSkypeID;

import java.util.HashSet;

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

/**
 * The Class CommandRemoveAdmin. Removes one admin.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandRemoveAdmin extends Command {

	private static final int USER_ID_POSITION = 0;

	private Chat outputChat = null;

	/** HashSet containing admins. */
	private HashSet<String> admins = null;

	private String userToRemovedID = null;

	/**
	 * Instantiates a new command remove admin.
	 */
	public CommandRemoveAdmin() {
		super("removeadmin", "Removes one admin.", "!removeadmin <skype_id>");
	}

	/**
	 * Instantiates a new command remove admin.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param userId
	 *            The id of the user to be removed
	 * @param admins
	 *            the admins of the bot
	 */
	public CommandRemoveAdmin(CommandData data) {
		this();
		initializeCommand(data);
	}

	/**
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		try{
			if(!canBeExecuted()){
				return;
			}

			if (admins.remove(userToRemovedID))
				outputChat.send("Admin removed.");
			else
				outputChat.send("Can not find that user.");

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}
	
	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private boolean isUserToBeRemovedBotOwner() {
		if (getUserSkypeID().equalsIgnoreCase(userToRemovedID)) {
			return true;
		}
		return false;
	}

	private boolean canBeExecuted() throws NullOutputChatException, SkypeException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		if (isUserToBeRemovedBotOwner()) {
			outputChat.send("Can not remove bot owner.");
			return false;
		}

		if (userToRemovedID.equals("")) {
			outputChat.send(getSyntax());
			return false;
		}

		return true;
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		this.admins = data.getAdmins();
		String options[] = data.getCommandOptions();

		if (options.length != 0)
			userToRemovedID = options[USER_ID_POSITION];
		else
			userToRemovedID = "";
	}

}
