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

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageEditListener;
import com.skype.SkypeException;
import com.skype.User;

import skype.handlers.MessageEditHandler;


/**
 * 
 * The Class GroupChatEditListener. This class is responsible for receiving any edits
 * that may happen at messages. It will receive the edited message and it will pass
 * it to {@link MessageEditHandler} to handle the edit based on user's config.
 * 
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class GroupChatEditListener implements ChatMessageEditListener {

	/** The group to listen to. */
	private Chat group = null;

	/** The edit handler. */
	private final MessageEditHandler editHandler;

	/**
	 * Instantiates a new group chat edit listener.
	 *
	 * @param groupChat
	 *            The group chat
	 * @param messages
	 *            A reference to messages from the given chat. ( It gets updated from
	 *            {@link GroupChatListener} )
	 */
	public GroupChatEditListener(Chat groupChat, ConcurrentHashMap<ChatMessage, String> messages) {
		group = groupChat;
		editHandler = new MessageEditHandler(messages);
	}

	/**
	 * If a message is edited then this method will be invoked. If the edit was made
	 * from this group then it will call its {@link #editHandler handler} to handle
	 * the edit.
	 * 
	 * @see com.skype.ChatMessageEditListener#chatMessageEdited(com.skype.ChatMessage,
	 *      java.util.Date, com.skype.User)
	 */
	@Override
	public void chatMessageEdited(ChatMessage editedMessage, Date eventDate, User who) {
		try {

			if (!editedMessage.getChat().equals(group))
				return;

			editHandler.handleEdit(editedMessage);

		} catch (SkypeException e) {
			System.out.println(e.getMessage());
			//If users chose method 2 then the self edits will throw an error, that why we ignore them.
		}
	}

}
