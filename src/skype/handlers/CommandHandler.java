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
package skype.handlers;

import static skype.utils.StringUtil.copyStringArray;
import static skype.utils.users.BotUserInfo.getUserSkypeID;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

import skype.commands.Command;
import skype.commands.CommandData;
import skype.exceptions.UnknownCommandException;
import skype.gui.popups.WarningPopup;
import skype.listeners.GroupChatListener;
import skype.utils.CommandClassFinder;
import skype.utils.CommandInvoker;
import skype.utils.Config;
import skype.utils.StringUtil;
import skype.utils.users.UserInformation;

/**
 * The Class CommandsHandler. This class will receive one command message, it will
 * process it and then will pass it to {@link CommandInvoker} to execute the command.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandHandler {

	private static final int COMMAND_NAME_POS = 0;

	private static final int EXCLAMATION_POSITION = 1;

	private static final HashMap<String, Command> commands = createCommandsMap();

	private final HashSet<String> botAdmins = new HashSet<String>(4);

	private final HashSet<String> excludedUsers = new HashSet<String>(
			Arrays.asList(Config.ExcludedUsers));

	private final HashSet<String> excludedCommands = new HashSet<String>(
			Arrays.asList(Config.ExcludedCommands));

	/** Reference at messages received from {@link GroupChatListener}. */
	private ConcurrentHashMap<String, UserInformation> users = null;

	/**
	 * For each Command class found it instantiate them and put them in the
	 * {@link commands}
	 */
	private static HashMap<String, Command> createCommandsMap() {
		List<Class<Command>> commandClasses = CommandClassFinder
			.findCommandClasses();

		HashMap<String, Command> commandsMap = new HashMap<>(
			commandClasses.size());

		try {
			for (Class<Command> command : commandClasses) {
				Command cmd = command.newInstance();
				commandsMap.put(cmd.getName(), cmd);
			}
		} catch (Exception e) {
			new WarningPopup(e.getMessage());
		}
		return commandsMap;
	}

	/**
	 * Instantiates a new commands handler.
	 *
	 * @param usr
	 *            references to users' hashmap which contains informations.
	 */
	public CommandHandler(ConcurrentHashMap<String, UserInformation> usr) {
		users = usr;
		botAdmins.add(getUserSkypeID());
	}

	/**
	 * This methods handles the command process and execution. It does all necessary
	 * checking before executing the command. Checking if commands are enabled if
	 * this user can execute a command and more. (check
	 * {@link #canExecuteCommand(String, ChatMessage)}
	 * <p>
	 * If the user is administrator he can execute the command no matter what.
	 *
	 * @param commandMessage
	 *            the command message.
	 * @throws SkypeException
	 *             the skype exception
	 */
	public void handleCommand(ChatMessage commandMessage) throws SkypeException {
		final String senderId = commandMessage.getSenderId();
		final String[] splittedCommand = preprocessCommand(commandMessage,commandMessage.getSenderId());
		final CommandData data = createCommandData(commandMessage, splittedCommand);
		Command command = null;

		try {
			command = getCommand(splittedCommand);
		} catch (UnknownCommandException e) {
			commandMessage.getChat().send("Unknown command.");
			return;
		}

		if (!canExecuteCommand(senderId, command.getName()))
			return;

		extraHandling(splittedCommand, data);

		command.setData(data);
		CommandInvoker.execute(command);
		findSenderInformation(senderId).increaseTotalCommandsToday(); //increase commands
	}

	/**
	 * Some commands may need extra handling in order to be able to executed. This
	 * extra handling may be changes to their data or some extra preprocessing.
	 */
	private void extraHandling(String[] splittedCommand, CommandData data) {
		String commandName = splittedCommand[COMMAND_NAME_POS];

		switch (commandName) {

		case "vote":
			data.setCommand(commands.get("choosepoll"));
			break;

		}
	}

	/**
	 * Receives the splittedCommand which contains the command name and tries to find
	 * it inside the command's hashmap
	 * 
	 * @return The command which (if) found
	 * @throws UnknownCommandException
	 *             If the command can not be found
	 */
	private Command getCommand(String[] splittedCommand)
		throws UnknownCommandException, SkypeException {
		
		final Command command = commands.get(splittedCommand[COMMAND_NAME_POS]);

		if (command == null)
			throw new UnknownCommandException();

		return command;
	}

	private CommandData createCommandData(ChatMessage commandMessage,
		String[] splittedCommand) throws SkypeException {

		final Chat commandChat = commandMessage.getChat();

		return new CommandData(commandChat, commandMessage.getSender(), users,
			botAdmins, copyArrayWithoutCommand(splittedCommand));
	}

	private boolean canExecuteCommand(String senderId, String command) {
		if (botAdmins.contains(senderId))
			return true; //Admins can always execute commands.

		if (!Config.EnableUserCommands)
			return false; //Check if commands are enabled.

		if (excludedUsers.contains(senderId))
			return false; //Check if user is excluded.

		if (excludedCommands.contains(command))
			return false; //Check if command is excluded.

		if (Config.MaximumNumberCommands <= findSenderInformation(senderId).getTotalCommands())
			return false; //Check if user reached max commands per day.

		if (command.equals(""))
			return false;

		return true;
	}

	/**
	 * Receives a command and the id of sender. The command format is
	 * "!CommandName [list_of_parameters]". It splits the command and each one of
	 * parameters to a String array and removes the exclamation mark from the
	 * beginning of command. Also checks if the sender of command is the owner of the
	 * bot, if yes then we delete the command from skype client.
	 * 
	 * @param commandMessage
	 *            The message with hold the command as a big string
	 * @param senderId
	 *            The id of the user who sent the message.
	 * @return the commandMessage text splitted.
	 * @throws SkypeException
	 */
	private String[] preprocessCommand(ChatMessage commandMessage,
			String senderId) throws SkypeException {

		final String[] splittedCommand = StringUtil
			.splitIgoringQuotes(commandMessage.getContent());

		//Remove ! from the beginning of command 
		splittedCommand[COMMAND_NAME_POS] = splittedCommand[COMMAND_NAME_POS]
			.substring(EXCLAMATION_POSITION).toLowerCase();

		if (isSenderBotsOwner(senderId))
			commandMessage.setContent("");

		return splittedCommand;

	}

	/**
	 * Receives a string array with the command name and its arguments. Returns a new
	 * allocated array with only the arguments.
	 * 
	 * @param commandSplittedMessage
	 * @return The newly allocated array without the command name, null if empty
	 */
	private String[] copyArrayWithoutCommand(String[] commandSplittedMessage) {
		return copyStringArray(commandSplittedMessage, COMMAND_NAME_POS + 1);
	}

	private UserInformation findSenderInformation(String id) {
		return users.get(id);
	}

	private boolean isSenderBotsOwner(String senderId) {
		return senderId.equals(getUserSkypeID());
	}

}
