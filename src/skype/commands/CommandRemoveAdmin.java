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

import static skype.utils.users.BotUserInfo.getBotUserID;

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

	private HashSet<String> currentAdmins = null;

	private String userToRemovedID = null;

	public CommandRemoveAdmin() {
		super("removeadmin", "Removes one admin.", "!removeadmin <skype_id>");
	}

	public CommandRemoveAdmin(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		try{
			if(!canBeExecuted()){
				return;
			}

			if (currentAdmins.remove(userToRemovedID))
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

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		this.currentAdmins = data.getAdmins();
		String options[] = data.getCommandOptions();

		if (options.length != 0)
			userToRemovedID = options[USER_ID_POSITION];
		else
			userToRemovedID = "";
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

	private boolean isUserToBeRemovedBotOwner() {
		if (getBotUserID().equalsIgnoreCase(userToRemovedID)) {
			return true;
		}
		return false;
	}

}
