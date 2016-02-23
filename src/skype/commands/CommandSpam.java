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

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;
/**
 * The Class CommandSpam.
 * <p>
 * Read <b>description</b> declared in constructor for further informations.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandSpam extends Command {

	/**
	 * Maximum times of spam text that can be repeated. I recommend keeping that
	 * number low because it can crush skype or keep the text being spammed for
	 * hours.
	 */
	private static int MAX_REPEATED_TIMES = 20;

	/** The output chat. */
	private Chat outputChat = null;

	/** The text to be repeated. */
	private String text = null;

	/**
	 * The receiver of the spam. It can be empty. If it is empty then the output chat
	 * is the chat from with the command was called.
	 */
	private User receiver = null;

	/** How many times the text is going me to be repeated. */
	private int times = 0;

	/**
	 * This constructor is used only for initiate information. E.g. usage or
	 * description
	 */
	public CommandSpam() {
		name = "Spam";
		description = "Repeats the <text> for <times> times. "
				+ "If an user from the chat was given it repeats <text> in user's private chat. "
				+ "If no user or invalid user was given then it repeats <text> in the chat from which the command was called.";
		usage = "!spam <text> <times> <optional_user>";
	}

	/**
	 * Instantiates a new command spam.
	 *
	 * @param outputChat
	 *            the output chat
	 * @param text
	 *            the text
	 * @param times
	 *            the times
	 * @param receiver
	 *            the receiver of the spam. It can be empty. If it is empty then the
	 *            output chat is the chat from with the command was called.
	 */
	public CommandSpam(Chat outputChat, String text, String times, User receiver) {
		this();
		this.outputChat = outputChat;
		this.receiver = receiver;
		this.text = text;
		this.times = parseTimesFromString(times);
		defineOutputChat();
	}

	/**
	 * @see skype.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		try {

			if (!canBeExecuted())
				return;

			for (int i = 0; i < times; i++)
				outputChat.send(text);

		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

	/**
	 * Checks if all parameters have proper values.
	 * 
	 * @throws NullOutputChatException
	 * @throws SkypeException
	 */
	private boolean canBeExecuted() throws NullOutputChatException, SkypeException {
		if (outputChat == null)
			throw new NullOutputChatException("Empty output chat.");

		if (times >= MAX_REPEATED_TIMES) {
			outputChat.send("Big number of times.");
			return false;
		}

		if (times < 0){
			outputChat.send("Invalid number of times.");
			return false;
		}

		return true;
	}

	/**
	 * Parses the times number from the string. It checks if string contains a proper
	 * formated number and it not it return 0 as no times are defined.
	 *
	 * @param times
	 *            the times in string
	 * @return the number of times given from user. 0 if the gave invalid number.
	 */
	private int parseTimesFromString(String times) {
		try {
			return Integer.parseInt(times);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Defines the output chat based on if user gave an specific user for output or
	 * not.
	 */
	private void defineOutputChat() {
		try {
			if (receiver != null)
				outputChat = receiver.chat();
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

}
