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

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
import skype.utils.users.UserInformation;

/**
 * The Class CommandInfo. Finds and returns the information for the given user.
 * 
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandInfo extends Command {

	private static final int USER_ID_POSITION = 0;

	private Chat outputChat = null;
	
	private UserInformation userInfo = null;

	private User user = null;

	public CommandInfo() {
		super("info", "Prints useful information about the given user", "Syntax: !info <user_id>");
	}

	public CommandInfo(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws NullOutputChatException {

		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {

			if (userInfo != null)
				outputChat.send(printInfo());
			else
				outputChat.send(getSyntax());

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		String[] options = data.getCommandOptions();

		if (options.length != 0) {
			String userID = options[USER_ID_POSITION];
			userInfo = data.getUserInformation(userID);
			if (userInfo != null)
				user = userInfo.getUser();
		} else {
			userInfo = null;
			user = null;
		}
	}

	private String printInfo() throws SkypeException {
		//@formatter:off
		return "Skype id: " + user.getId() + "\r\n" + 
				"Displayed name: " + user.getFullName() + "\r\n" + 
				"Status: " + user.getStatus() + "\r\n" + 
				"Last time online: " + user.getLastOnlineTime().toString() + "\r\n" + 
				"Total messages today: " + userInfo.getTotalMessagesToday() + "\r\n" + 
				"Current amount of warnings: " + userInfo.getWarnings(); 
	}

}
