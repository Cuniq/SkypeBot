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
package skype.utils;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import skype.gui.popups.WarningPopup;
import skype.handlers.CommandHandler;
import skype.handlers.NormalChatHandler;
import skype.listeners.GroupChatListener;
import skype.utils.users.BotUserInfo;
import skype.utils.users.UserInformation;

import com.skype.ChatMessage;
import com.skype.SkypeException;


/**
 * The Class Consumer. The consumer will remove one message at time and will process
 * it. You can have more than one consumers but you may process messages
 * out-of-order. If receive time is not important in your application you may use
 * more than one consumer.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class Consumer implements Runnable {

	/** The list. */
	private final LinkedList<Pair<ChatMessage, Long>> list;

	/** The command messages handler. */
	private final CommandHandler commandHandler;

	/** The normal messages handler. */
	private final NormalChatHandler normalHandler;

	/** Reference at messages received from {@link GroupChatListener}. */
	ConcurrentHashMap<ChatMessage, String> messages = null;

	/**
	 * Instantiates a new consumer.
	 *
	 * @param list
	 *            the list
	 */
	public Consumer(LinkedList<Pair<ChatMessage, Long>> list, ConcurrentHashMap<String, UserInformation> users, ConcurrentHashMap<ChatMessage, String> msg) {
		this.list = list;
		this.messages = msg;

		commandHandler = new CommandHandler(users);
		normalHandler = new NormalChatHandler(users);
	}

	/**
	 * Consume one message and process it.
	 */
	public void consume() throws NoSuchElementException {
		Pair<ChatMessage, Long> pair = null;
		try {
			pair = list.removeFirst();
		} catch (NoSuchElementException e) {
			//Ignore
		}

		ChatMessage msg = pair.getFirst();

		try{
	
			if (msg.getContent().startsWith("!")) {
				commandHandler.handleCommand(msg);
			} else {
				messages.put(msg, msg.getContent());

				if (!Config.EnableSelfWarnings && msg.getId().equals(BotUserInfo.getUserSkypeID()))
					return;

				normalHandler.handleNormalChat(msg, pair.getSecond());
			}
		} catch(SkypeException e){
			new WarningPopup(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			synchronized (list) {
				while (list.isEmpty()) {
					try {
						list.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			consume();
		}
	}

}
