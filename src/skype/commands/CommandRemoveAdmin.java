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

	/** The output chat. */
	private Chat outputChat = null;

	/** HashSet containing admins. */
	private HashSet<String> admins = null;

	/** The id of the user to be removed. */
	private String id = null;

	/**
	 * Instantiates a new command remove admin.
	 */
	public CommandRemoveAdmin() {
		name = "removeadmin";
		description = "Removes one admin.";
		usage = "!removeadmin <skype_id>";
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
	public CommandRemoveAdmin(Chat outputChat, String userId, HashSet<String> admins) {
		this.outputChat = outputChat;
		this.admins = admins;
		id = userId;
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

			if(admins.remove(id))
				outputChat.send("Admin removed.");
			else
				outputChat.send("Can not find that user.");
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}
	
	/**
	 * Checks if is user that is about to be removed is the bot owner.
	 *
	 * @return true, if user is the bot owner.
	 */
	private boolean isUserBotOwner(){
		if (getUserSkypeID().equalsIgnoreCase(id)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the command can be executed
	 *
	 * @return true, if command can be executed
	 */
	private boolean canBeExecuted() throws NullOutputChatException, SkypeException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		if (isUserBotOwner()) {
			outputChat.send("Can not remove bot owner.");
			return false;
		}

		return true;
	}

}
