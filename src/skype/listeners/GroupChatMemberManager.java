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
package skype.listeners;

import java.util.concurrent.ConcurrentHashMap;

import com.skype.ChatListener;
import com.skype.User;

import skype.utils.users.UserInformation;

/**
 * The Class GroupChatMemberManager. This class is responsible for updating the users
 * of one registered chat. It modifies {@link #users} based on if an user left or
 * entered the chat.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class GroupChatMemberManager implements ChatListener {

	/** A reference to {@link skype.listeners.GroupChatListener#users}. */
	private final ConcurrentHashMap<String, UserInformation> users;

	/**
	 * Instantiates a new group chat member manager.
	 *
	 * @param users
	 *            reference to users of a group.
	 */
	public GroupChatMemberManager(ConcurrentHashMap<String, UserInformation> users) {
		this.users = users;
	}

	@Override
	public void userAdded(User u) {
		users.put(u.getId(), new UserInformation(u));
	}

	@Override
	public void userLeft(User u) {
		users.remove(u.getId());
	}

}
