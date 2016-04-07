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
package skype.handlers;

import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

import skype.listeners.GroupChatListener;
import skype.utils.Config;
import skype.utils.users.BotUserInfo;
import skype.utils.users.UserInformation;

/**
 * The Class NormalChatHandler. This class is responsible for handling all normal
 * chat and taking proper actions on chat illegalities.
 */
public class NormalChatHandler {

	/** Reference at {@link GroupChatListener#users} */
	private ConcurrentHashMap<String, UserInformation> users;

	private final static int WARNING_ACTION = Config.WarningAction;

	/**
	 * Instantiates a new normal chat handler.
	 *
	 * @param users
	 *            Reference to users of the group
	 */
	public NormalChatHandler(ConcurrentHashMap<String, UserInformation> users) {
		this.users = users;
	}
	
	public void handleNormalChat(ChatMessage message, Long timeSend) throws SkypeException {
		UserInformation userInfo = users.get((message.getSender().getId()));
		userInfo.increaseTotalMessagesToday();

		if (userInfo.getLastMessage() == null) {
			userInfo.setLastMessage(message, timeSend);
			return;
		}
			
		if (Config.EnableWarnings) {
			if (!canHandleWarning(message))
				return;

			if (timeSend - userInfo.getLastMessageTime() < Config.WarningInterval) {
				userInfo.increaseWarning();
			}

			if (userInfo.getWarnings() >= Config.WarningNumber) {
				takeWarningAction(message);
				userInfo.ResetWarnings();
			}

			userInfo.setLastMessage(message, timeSend);
		}

	}

	private boolean canHandleWarning(ChatMessage message) throws SkypeException {
		if (message.getChat().getAllMembers().length <= 2) //Spam handling only for groups
			return false;
		if (!Config.EnableSelfWarnings && message.getId().equals(BotUserInfo.getBotUserID()))
			return false;
		return true;
	}

	private void takeWarningAction(ChatMessage message) throws SkypeException {
		final Chat outputChat = message.getChat();
		final String senderID = message.getSender().getId();

		if (WARNING_ACTION == Config.WARNING_ACTION_SET_LISTENER)
			outputChat.send("/setrole " + senderID + " LISTENER");
		else if (WARNING_ACTION == Config.WARNING_ACTION_KICK)
			outputChat.send("/kick " + senderID);
		else if (WARNING_ACTION == Config.WARNING_ACTION_KICKBAN)
			outputChat.send("/kickban " + senderID);
	}

}
