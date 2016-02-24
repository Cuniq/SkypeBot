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
package skype.utils.timers;

import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.ChatMessage;
import com.skype.SkypeException;

import skype.gui.popups.WarningPopup;

/**
 * The Class HourlyNonEditableMessageCleaner. This class will search for messages
 * which are no longer editable and will remove them. The search process is being
 * repeated every hour.
 *
 * @author Thanasis Argyroudis
 * @see RepetitiveTask
 * @see TimerTask
 * @since 1.0
 */
public class HourlyNonEditableMessageCleaner extends RepetitiveTask {

	/** The messages which we search if are no longer editable. */
	private final ConcurrentHashMap<ChatMessage, String> messages;
	
	/**
	 * Instantiates a new hourly timer.
	 *
	 * @param msg
	 *            Reference to messages
	 */
	public HourlyNonEditableMessageCleaner(ConcurrentHashMap<ChatMessage, String> messagesRef) {
		messages = messagesRef;
	}
	
	/**
	 * Starts the timer instantly and removes the messages that are not editable any
	 * more every hour.
	 * 
	 * @see skype.utils.timers.RepetitiveTask#startTimer()
	 */
	@Override
	public void startTimer() {
		getTimer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				clearMessages();
			}

		}, 0, HOUR_TO_MILLS);
	}

	/**
	 * Stop the timer after that iteration.
	 * 
	 * @see skype.utils.timers.RepetitiveTask#stopTimer()
	 */
	@Override
	public void stopTimer() {
		getTimer().cancel();
	}

	/**
	 * This method is responsible for removing messages which are no longer editable.
	 */
	private void clearMessages() {
		Set<ChatMessage> keys = messages.keySet();

		for (ChatMessage message : keys) {
			try {

				if (!message.isEditable())
					messages.remove(message);

			} catch (SkypeException e) {
				new WarningPopup(e.getMessage());
			}
		}
	}

}
