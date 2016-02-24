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

	/** The warnings. */
	private MutableLong warnings = new MutableLong(0);
	
	/** The messages today. */
	private MutableLong messagesToday = new MutableLong(0);
	
	/** The number of commands which this user executed */
	private MutableLong commandsToday = new MutableLong(0);

	/** The last message. */
	private ChatMessage lastMessage = null;
	
	/**
	 * When the last message was sent. We use that field because
	 * {@link ChatMessage#getTime()} has low resolution.
	 */
	private Long lastMessageTime = null;

	/** The user. */
	private final User user;

	/** The warnings reseter. */
	private final WarningsReseters warningsReseter = new WarningsReseters(warnings);
	
	/** The messages daily reseter. */
	private final DailyReseter messagesReseter = new DailyReseter(messagesToday, getMidnight());
	
	/** The commands daily reseter. */
	private final DailyReseter commandsReseter = new DailyReseter(commandsToday, getMidnight());

	/**
	 * Instantiates a new user properties.
	 *
	 * @param user
	 *            the skype user
	 */
	public UserInformation(User user) {
		this.user = user;
		warningsReseter.startTimer();
		messagesReseter.startTimer();
		commandsReseter.startTimer();
	}
	
	/**
	 * Increase warnings.
	 */
	public void increaseWarning(){
		warnings.increment();
	}
	
	/**
	 * Increase total messages today.
	 */
	public void increaseTotalMessagesToday(){
		messagesToday.increment();
	}
	
	/**
	 * Increase total commands today.
	 */
	public void increaseTotalCommandsToday() {
		commandsToday.increment();
	}

	/**
	 * decrease total commands today.
	 */
	public void decreaseTotalCommandsToday() {
		commandsToday.decrement();
	}

	/**
	 * Gets the total commands.
	 *
	 * @return the total commands
	 */
	public Long getTotalCommands() {
		return commandsToday.toLong();
	}

	/**
	 * Gets the current amount warnings.It may be reseted every 10 minutes.
	 *
	 * @return the warnings.
	 */
	public Long getWarnings() {
		return warnings.toLong();
	}
	
	/**
	 * Manually resets the warnings. Usually used after a warning action is taken.
	 */
	public void ResetWarnings() {
		warnings.setValue(0);
	}

	/**
	 * Gets the total messages today.
	 *
	 * @return the total messages today
	 */
	public Long getTotalMessagesToday() {
		return messagesToday.toLong();
	}
	
	/**
	 * Gets the last message which user sent.
	 *
	 * @return the last message
	 */
	public ChatMessage getLastMessage() {
		return lastMessage;
	}

	/**
	 * Sets the last message.
	 *
	 * @param msg
	 *            the new last message.
	 */
	public void setLastMessage(ChatMessage msg, Long sentTime) {
		lastMessage = msg;
		lastMessageTime = sentTime;
	}
	
	/**
	 * Gets the Date of last item.
	 *
	 * @return the last message's send time.
	 */
	public Long getLastMessageTime() {
		return lastMessageTime;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser(){
		return user;
	}

}
