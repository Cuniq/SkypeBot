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
package skype.utils.commands;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;
/**
 * The Class CommandSpam.
 * <p>
 * Read <b>description</b> declared in constructor for further informations.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandSpam extends Command {

	/** The output chat. */
	private Chat outputChat = null;

	/** The text. */
	private String text = null;

	/** The receiver. */
	private User receiver = null;

	/** The times. */
	private int times = 0;

	/**
	 * This constructor is used only for initiate information. Eg usage or
	 * description
	 */
	public CommandSpam() {
		name = "Spam";
		description = "Repeats the <text> for <times> times. " //
				+ "If an user from the chat was given it repeats <text> in user's private chat. " //
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
	 *            the receiver
	 */
	public CommandSpam(Chat outputChat, String text, String times, User receiver) {
		this();
		this.outputChat = outputChat;
		this.receiver = receiver;
		this.text = text;
		try{
			this.times = Integer.parseInt(times);
		} catch (NumberFormatException e) {
			try {
				outputChat.send("Invalid number format.");
				this.times = 0;
			} catch (SkypeException e1) {
				new WarningPopup(e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skype.utils.commands.Command#execute()
	 */
	@Override
	public void execute() throws CommandException {
		try {
			if (outputChat == null)
				throw new NullOutputChatException("Empty output chat.");
			if (times >= 20) {
				outputChat.send("Big number of times.");
				return;
			}
			if (times < 0)
				outputChat.send("Invalid number of times.");
			if (receiver != null) // change output chat.
				outputChat = receiver.chat();

			for (int i = 0; i < times; i++)
				outputChat.send(text);


		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}
	}

}
