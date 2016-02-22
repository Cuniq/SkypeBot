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
package skype.utils.commands;

import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
import skype.utils.users.UserInformation;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

/**
 * The Class CommandInfo.
 * <p>
 * Read <b>description</b> declared in constructor for further informations.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandInfo extends Command {

	/**
	 * The output chat for command's result.
	 */
	private Chat outputChat = null;
	
	/** Informations about user. */
	private UserInformation userInfo = null;

	private User user = null;

	/**
	 * This constructor is used only for initiate information. Eg usage or
	 * description
	 */
	public CommandInfo() {
		name = "Info";
		description = "Prints useful information about the given user";
		usage = "!info <user_id>";
	}

	/**
	 * Instantiates a new info command.
	 */
	public CommandInfo(Chat outputChat, UserInformation userInfo) {
		this();
		this.outputChat = outputChat;
		this.userInfo = userInfo;
		user = userInfo.getUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.commands.Command#execute()
	 */
	@Override
	public void execute() throws NullOutputChatException {

		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		try {

			outputChat.send(printInfo());

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	/**
	 * Prints the info for the given user.
	 *
	 * @param u
	 *            the user
	 * @return the string with informations.
	 * @throws SkypeException
	 *             the skype exception.
	 */
	private String printInfo() throws SkypeException {
		return "Skype id: " + user.getId() + "\r\n" + "Displayed name: " + user.getFullName() + "\r\n" + //
				"Status: " + user.getStatus() + "\r\n" + "Last time online: " + user.getLastOnlineTime().toString() //
				+ "\r\n" + "Total messages today: " + userInfo.getTotalMessagesToday() + "\r\n" +  //
				"Current amount of warnings: " + userInfo.getWarnings(); //
	}

}
