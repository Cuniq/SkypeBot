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
package skype.listeners;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import skype.gui.popups.WarningPopup;
import skype.handlers.CommandHandler;
import skype.handlers.NormalChatHandler;
import skype.utils.Consumer;
import skype.utils.Pair;
import skype.utils.timers.HourlyNonEditableMessageClean;
import skype.utils.users.UserInformation;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;
import com.skype.User;


/**
 * 
 * The GroupChatListener class is responsible for receiving messages from the
 * specific {@link GroupChatListener#group} and handling them properly. Messages can
 * be split up in two main categories
 * <p>
 * <ul>
 * <li> {@link skype.utils.commands.Command Commands}
 * <li>Normal messages
 * </ul>
 * <p>
 * This class works with producer-consumer design. The two main methods of this class
 * {@link #chatMessageReceived(ChatMessage)} and
 * {@link #chatMessageSent(ChatMessage)} are working like producers. They put the
 * received messages in a {@link #list} and then notify the consumer(s) to wake up
 * and process the messages.
 * <p>
 * Also this class is responsible for handling messages edits. In order to add an
 * edit listener for a chat that you have already added a
 * <code>ChatmessageListener</code> use the
 * 
 * <pre>
 * <code>{@link #getGroupChatEditListenerInstance()}
 * instead of Skype.addChatMessageListener(new EditListener())</code>
 * </pre>
 * <p>
 * Despite class name this listener will work also for chats with two persons. We
 * handle them like groups.
 * <p>
 * <code>GroupChatListener</code> will handle message from group users and the user.
 * 
 * @see GroupChatEditListener
 * @see CommandHandler
 * @see NormalChatHandler
 * @see Consumer
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class GroupChatListener implements ChatMessageListener{

	/** The total number of group members. */
	private int totalMembers = 0;

	/** The group to listen to. */
	private final Chat group;
	
	/** Informations for the users of the group. <User's Id, User's Informations> */
	private ConcurrentHashMap<String, UserInformation> users = null;
	
	/** The handler for message edits. */
	private GroupChatEditListener editListener = null;

	@SuppressWarnings("unused")
	/** The member manager. */
	private final GroupChatMemberManager memberManager;

	/** The list for producer-consumer. */
	private final LinkedList<Pair<ChatMessage, Long>> list;

	/** The consumer thread for processing messages */
	private final Thread consumer;

	/**
	 * Keep last messages here. Mainly used to keep track of editable messages. If a
	 * message stops being editable it will be deleted.
	 */
	private final ConcurrentHashMap<ChatMessage, String> messages = new ConcurrentHashMap<ChatMessage, String>(30);

	@SuppressWarnings("unused")
	/** The hourly message clean. */
	private final HourlyNonEditableMessageClean hourlyMessageClean = new HourlyNonEditableMessageClean(messages);

	/**
	 * Instantiates a new group listener. Registers the handlers and creates the
	 * information classes for the users of chat.
	 *
	 * @param groupChat
	 *            The group chat for which we added the listener
	 */
	public GroupChatListener( Chat groupChat ){
		
		group = groupChat;

		try {
			totalMembers = group.getAllMembers().length;
			users = new ConcurrentHashMap<String, UserInformation>(totalMembers + 1);
			initiateUserInformations();

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

		memberManager = new GroupChatMemberManager(users);

		list = new LinkedList<Pair<ChatMessage, Long>>();
		consumer = new Thread(new Consumer(list, users, messages), "Consumer");
		consumer.setPriority(Thread.MAX_PRIORITY);
		consumer.start();

	}
	
	/**
	 * Gets the group chat edit listener instance.
	 *
	 * @return the group chat edit listener instance for the specific group.
	 */
	public synchronized GroupChatEditListener getGroupChatEditListenerInstance() {
		if (editListener == null)
			editListener = new GroupChatEditListener(group, messages);
		return editListener;
	}

	/**
	 * Initiates each user's Informations.
	 *
	 * @throws SkypeException
	 *             If something will go wrong skype will throw exception.
	 */
	private final void initiateUserInformations() throws SkypeException {
		for (User u : group.getAllMembers()) {
			users.put(u.getId(), new UserInformation(u));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.skype.ChatMessageListener#chatMessageReceived(com.skype.ChatMessage)
	 */
	@Override
	public void chatMessageReceived(ChatMessage rec) throws SkypeException {
		if (!rec.getChat().equals(group))
			return;

		Pair<ChatMessage, Long> pair = new Pair<ChatMessage, Long>(rec, System.currentTimeMillis());
		list.add(pair);
		synchronized (list) {
			list.notify();
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.skype.ChatMessageListener#chatMessageSent(com.skype.ChatMessage)
	 */
	@Override
	public void chatMessageSent(ChatMessage sent) throws SkypeException {
		if (!sent.getChat().equals(group))
			return;

		Pair<ChatMessage, Long> pair = new Pair<ChatMessage, Long>(sent, System.currentTimeMillis());
		list.add(pair);
		synchronized (list) {
			list.notify();			
		}

	}


}
