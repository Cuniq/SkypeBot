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
package skype.commands;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;
import com.skype.User;

import skype.utils.users.UserInformation;

/**
 * The Class CommandData. A object which holds all the information that a command may
 * need.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandData {

	/** The output chat for the result of command. */
	private Chat outputChat = null;

	/** The sender of the command. */
	private User sender = null;

	/** More informations for the receiver. */
	private ConcurrentHashMap<String, UserInformation> userInformation = null;

	/** A set holding all bot's admins. */
	private HashSet<String> admins;

	/** More options which many one command needs. */
	private String[] commandOptions = null;

	/** Some commands may need reference to other commands. */
	private Command command = null;

	/**
	 * Instantiates a new command data.
	 *
	 * @param outputChat
	 *            The output chat for the result of command
	 * @param receiver
	 *            The receiver command's result
	 * @param receiverUserInformations
	 *            informations for the receiver
	 * @param admins
	 *            set holding all bot's admins
	 * @param commandOptions
	 *            options which many one command needs
	 */
	public CommandData(	Chat outputChat, User sender,
						ConcurrentHashMap<String, UserInformation> userInformation,
						HashSet<String> admins, String[] commandOptions) {

		this.outputChat = outputChat;
		this.sender = sender;
		this.userInformation = userInformation;
		this.admins = admins;
		this.commandOptions = commandOptions;
	}

	public Chat getOutputChat() {
		return outputChat;
	}

	public User getSender() {
		return sender;
	}

	public UserInformation getSenderInformation() {
		return userInformation.get(sender.getId());
	}

	public Map<String, UserInformation> getUserInformationHashMap() {
		return Collections.unmodifiableMap(userInformation);
	}

	public UserInformation getUserInformation(String userID) {
		return userInformation.get(userID);
	}

	public HashSet<String> getAdmins() {
		return admins;
	}

	public String[] getCommandOptions() {
		return commandOptions;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

}
