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
package skype.utils.users;

import static skype.utils.DateUtil.getMidnight;

import org.apache.commons.lang.mutable.MutableLong;

import com.skype.ChatMessage;
import com.skype.User;

import skype.utils.timers.DailyReseter;
import skype.utils.timers.WarningsReseters;

/**
 * The Class UserInformation. This class keeps useful informations about the given
 * user. Information like the total number of messages today, the current number of
 * warnings and more.
 */
public class UserInformation {

	private MutableLong warnings = new MutableLong(0);
	
	private MutableLong messagesToday = new MutableLong(0);
	
	private MutableLong commandsToday = new MutableLong(0);

	private ChatMessage lastMessage = null;
	
	/**
	 * When the last message was sent. We use that field because
	 * {@link ChatMessage#getTime()} has low resolution.
	 */
	private Long lastMessageTime = null;

	/** The user for whom we keep all that informations. */
	private final User user;

	private final WarningsReseters warningsReseter = new WarningsReseters(warnings);
	
	private final DailyReseter messagesReseter = new DailyReseter(messagesToday, getMidnight());
	
	private final DailyReseter commandsReseter = new DailyReseter(commandsToday, getMidnight());

	public UserInformation(User user) {
		this.user = user;
		warningsReseter.startTimer();
		messagesReseter.startTimer();
		commandsReseter.startTimer();
	}
	
	public void increaseWarning(){
		warnings.increment();
	}
	
	public void increaseTotalMessagesToday(){
		messagesToday.increment();
	}
	
	public void increaseTotalCommandsToday() {
		commandsToday.increment();
	}

	public void decreaseTotalCommandsToday() {
		commandsToday.decrement();
	}

	public Long getTotalCommands() {
		return commandsToday.toLong();
	}

	public Long getWarnings() {
		return warnings.toLong();
	}
	
	public void ResetWarnings() {
		warnings.setValue(0);
	}

	public Long getTotalMessagesToday() {
		return messagesToday.toLong();
	}
	
	public ChatMessage getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(ChatMessage msg, Long sentTime) {
		lastMessage = msg;
		lastMessageTime = sentTime;
	}
	
	public Long getLastMessageTime() {
		return lastMessageTime;
	}

	public User getUser(){
		return user;
	}

}
