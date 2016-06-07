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

import java.util.HashSet;

import com.skype.Chat;
import com.skype.SkypeException;

import skype.exceptions.CommandException;
import skype.exceptions.NullOutputChatException;
import skype.gui.popups.WarningPopup;

/**
 * The Class CommandShowAdmins. Shows all registered admins.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandShowAdmins extends Command {

	private Chat outputChat = null;

	private HashSet<String> currentAdmins = null;

	public CommandShowAdmins() {
		super("showadmins", "Prints all bot's admins.", "Syntax: !showadmins");
	}

	public CommandShowAdmins(CommandData data) {
		this();
		initializeCommand(data);
	}

	@Override
	public void execute() throws CommandException {
		if (outputChat == null)
			throw new NullOutputChatException();
		
		try {
			for (String admin : currentAdmins) {
				outputChat.send(admin + "\r\n");
			}
		} catch (SkypeException e) {
			new WarningPopup(e.getMessage());
		}

	}

	public void setData(CommandData data) {
		initializeCommand(data);
	}

	private void initializeCommand(CommandData data) {
		this.outputChat = data.getOutputChat();
		this.currentAdmins = data.getAdmins();
	}

}