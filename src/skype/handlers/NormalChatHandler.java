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
package skype.handlers;

import java.util.concurrent.ConcurrentHashMap;

import skype.utils.Config;
import skype.utils.users.UserInformation;

import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * The Class NormalChatHandler. This class is responsible for handling all normal
 * chat messages (not commands) and setting all restriction for chat.
 */
public class NormalChatHandler {

	/** users' properties. */
	private ConcurrentHashMap<String, UserInformation> users;

	/**
	 * Instantiates a new normal chat handler.
	 *
	 * @param grp
	 *            the group
	 */
	public NormalChatHandler(ConcurrentHashMap<String, UserInformation> users) {
		this.users = users;
	}
	
	/**
	 * Handle normal group chat.
	 * @param msg
	 *            the message
	 * 
	 * @throws SkypeException
	 *             the skype exception
	 */
	public void handleNormalChat(ChatMessage msg, Long timeSend) throws SkypeException {
		UserInformation userInfo = users.get((msg.getSender().getId()));

		if (userInfo.getLastMessage() == null) {

			userInfo.setLastMessage(msg, timeSend);

		} else {
			
			userInfo.increaseTotalMessagesToday();

			if (Config.EnableWarnings) {
				if (msg.getChat().getAllMembers().length <= 2) //Spam handling only for groups
					return;

				if (timeSend - userInfo.getLastMessageTime() < Config.WarningInterval) {
					userInfo.increaseWarning();
				}

				if (userInfo.getWarnings() >= Config.WarningNumber) {
					takeWarningAction(Config.WarningAction, msg);
					userInfo.ResetWarnings();
				}

				//System.out.println(msg.getSender().getFullName() + " " + msg.getContent() + " : " + timeSend);
				userInfo.setLastMessage(msg, timeSend);
			}

		}
	}

	/**
	 * Take an action based on user's config option.
	 *
	 * @param action
	 *            the action
	 * @param msg
	 *            the message
	 * @throws SkypeException
	 *             the skype exception
	 */
	private void takeWarningAction(Byte action, ChatMessage msg) throws SkypeException{
		if(action == 2)
			msg.getChat().send("/setrole " + msg.getSender().getId() + " LISTENER");
		else if (action == 3)
			msg.getChat().send("/kick " + msg.getSender().getId());
		else if (action == 4)
			msg.getChat().send("/kickban " + msg.getSender().getId());
	}


}
