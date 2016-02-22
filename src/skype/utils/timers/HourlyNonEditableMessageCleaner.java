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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import skype.gui.popups.WarningPopup;

import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * The Class HourlyTimer. This class will search the messages <code>HashSet</code>
 * for messages which are no longer editable and will remove them.
 *
 * @author Thanasis Argyroudis
 * @see java.util.Timer
 * @since 1.0
 */
public class HourlyNonEditableMessageCleaner {

	/** The Constant ONE_HOUR. */
	private static final long ONE_HOUR = 60 * 60 * 1000;

	/** The timer. */
	private final Timer hourlyTimer = new Timer();

	/** The messages. */
	private final ConcurrentHashMap<ChatMessage, String> messages;
	
	/**
	 * Instantiates a new hourly timer.
	 *
	 * @param msg
	 *            Reference to messages
	 */
	public HourlyNonEditableMessageCleaner(ConcurrentHashMap<ChatMessage, String> msg) {
		messages = msg;
		startTimer();
	}
	
	/**
	 * Stops the task.
	 */
	public void stopTask() {
		hourlyTimer.cancel();
	}
	
	/**
	 * Start timer.
	 */
	private final void startTimer() {
		hourlyTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				clearMessages();
			}
		}, 0, ONE_HOUR);
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
