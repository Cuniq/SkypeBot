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

import static skype.utils.users.BotUserInfo.getUserSkypeID;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

import skype.exceptions.UnknownCommandException;
import skype.exceptions.UnknownSkypeUserException;
import skype.gui.popups.WarningPopup;
import skype.utils.CommandInvoker;
import skype.utils.Config;
import skype.utils.StringUtil;
import skype.utils.commands.Command;
import skype.utils.commands.CommandAddAdmin;
import skype.utils.commands.CommandChoosePoll;
import skype.utils.commands.CommandGetAllCommands;
import skype.utils.commands.CommandHelp;
import skype.utils.commands.CommandInfo;
import skype.utils.commands.CommandRemoveAdmin;
import skype.utils.commands.CommandShowAdmins;
import skype.utils.commands.CommandSpam;
import skype.utils.users.UserInformation;

/**
 * The Class CommandsHandler. This class will receive one command message, then it
 * will process it and then will pass it to {@link CommandInvoker} to execute the
 * command.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class CommandHandler {

	/**
	 * A set of administrators. Some commands need administrator's level in order to
	 * be executed.
	 */
	private final HashSet<String> admins = new HashSet<String>(4);
	
	/** The set of excluded users. */
	private final HashSet<String> excludedUsers = new HashSet<String>(Arrays.asList(Config.ExcludedUsers));

	/** The set of excluded commands. */
	private final HashSet<String> excludedCommands = new HashSet<String>(Arrays.asList(Config.ExcludedCommands));

	/** users' informations. */
	private ConcurrentHashMap<String, UserInformation> users = null;
	
	/** The commands. */
	private String[] command = null;

	/** Holder for a poll command */
	private Command pollCommand = null;

	/** The timer for poll command. One poll command lasts 10 minutes. */
	private final Timer pollTimer = new Timer();

	/**
	 * Instantiates a new commands handler.
	 *
	 * @param usr
	 *            references to users' hashmap which contains informations.
	 */
	public CommandHandler(ConcurrentHashMap<String, UserInformation> usr) {
		users = usr;
		admins.add(getUserSkypeID());
	}
	
	/**
	 * This methods handles the command process and execution. It does all necessary
	 * checking before executing the command. Checking if commands are enabled if
	 * this user can execute a command and more. (check
	 * {@link #canExecuteCommand(String, ChatMessage)}
	 * <p>
	 * If the user is administrator he can execute the command no matter what.
	 *
	 * @param msg
	 *            the command message.
	 * @throws SkypeException
	 *             the skype exception
	 */
	public void handleCommand(ChatMessage msg) throws SkypeException {
		final String senderId = msg.getSenderId();

		// Usage !Command [parameters]
		command = StringUtil.splitIngoringQuotes(msg.getContent());
		command[0] = command[0].substring(1).toLowerCase(); //Remove ! from the beginning of command 
		
		if (senderId.equals(getUserSkypeID()) // We can delete only bot's user commands
				&& !command[0].equalsIgnoreCase("vote")) // Don't delete votes!
			msg.setContent("");

		if (!admins.contains(senderId)) //Check only if he is not admin.
			if (!canExecuteCommand(senderId, command[0]))
				return;

		try{

			switch (command[0]) {

			case "info": //!info <user_id>
				CommandInvoker.execute(new CommandInfo(msg.getChat(), findUserInformation(command[1])));
				break;

			case "spam": //!spam <text> <times> <optional_user>
				if (command.length == 3)
					CommandInvoker.execute(new CommandSpam(msg.getChat(), command[1], command[2], null));
				else
					CommandInvoker.execute(new CommandSpam(msg.getChat(), command[1], command[2], findUser(command[3])));
				break;

			case "help": //!help <command_name>
				CommandInvoker.execute(new CommandHelp(msg.getChat(), command[1]));
				break;

			case "getallcommands": //!getallcommands
				CommandInvoker.execute(new CommandGetAllCommands(msg.getChat()));
				break;

			case "choosepoll": //!choosepoll <question> [<choice>,<choice>,...]
				if (pollCommand != null) //There is a poll already going on. Ignore this.
					return;
				//TODO: Create a timer wrapper for this. It isn't CommandHandler job to set the timer but just to start it.
				pollCommand = new CommandChoosePoll(msg.getChat(), command[1], Arrays.copyOfRange(command, 2, command.length));
				CommandInvoker.execute(pollCommand);
				pollTimerSchedule();
				break;

			case "vote":
				if (pollCommand == null) // No ongoing poll.
					return;
				((CommandChoosePoll) pollCommand).addVote(command[1], msg.getSender());
				break;

			case "addadmin": //!addadmin <user_id>
				CommandInvoker.execute(new CommandAddAdmin(msg.getChat(), command[1], admins));
				break;

			case "removeadmin":
				CommandInvoker.execute(new CommandRemoveAdmin(msg.getChat(), command[1], admins));
				break;

			case "showadmins":
				CommandInvoker.execute(new CommandShowAdmins(msg.getChat(), admins));
				break;

			default:
				throw new UnknownCommandException();

			}

			//We reach here if no exception occurred
			findUserInformation(senderId).increaseTotalCommandsToday(); //increase commands

		}catch(ArrayIndexOutOfBoundsException e){
			//Arrays.copyOfRange can throw this exception
			//This can only happen if command.length < 2
			//Which means wrong format.
			msg.getChat().send("Wrong format.");
		}catch(UnknownCommandException e ){
			msg.getChat().send("Unknown command.");
		}catch(UnknownSkypeUserException e){
			msg.getChat().send("Unknown user.");
		} catch (Exception e) {
			new WarningPopup(e.getMessage());
		}
		
	}

	/**
	 * Check if the sender has permission to execute the
	 *
	 * @param senderId
	 *            the sender id
	 * 
	 * @return true, if successful
	 */
	private boolean canExecuteCommand(String senderId, String cmd) {

		if (!Config.EnableUserCommands)
			return false; //Check if commands are enabled.

		if (Config.MaximumNumberCommands <= findUserInformationWithoutException(senderId)
				.getTotalCommands())
			return false; //Check if user reached max commands per day.

		if (excludedUsers.contains(senderId))
			return false; //Check if user is excluded.

		if (excludedCommands.contains(cmd))
			return false; //Check if command is excluded.

		return true;
	}
			
	/**
	 * Gets the users properties.
	 *
	 * @param id
	 *            the user's id
	 * @throws UnknownSkypeUserException
	 *             the unknown skype user exception
	 * @return the users properties
	 */
	private UserInformation findUserInformation(String id) throws UnknownSkypeUserException {
		UserInformation temp = users.get(id);
		if(temp == null)
			throw new UnknownSkypeUserException();
		return temp;
	}

	/**
	 * Find user information without exception.
	 *
	 * @param id
	 *            the id
	 * @return the user information
	 */
	private UserInformation findUserInformationWithoutException(String id) {
		return users.get(id);
	}

	/**
	 * Find user.
	 *
	 * @param id
	 *            the user's id
	 * @throws UnknownSkypeUserException
	 *             the unknown skype user exception
	 * @return the user
	 */
	private User findUser(String id) throws UnknownSkypeUserException {
		UserInformation temp = users.get(id);
		if (temp != null)
			return temp.getUser();
		throw new UnknownSkypeUserException();

	}


	/**
	 * This method will schedule the 10min duration for the poll. After that is will
	 * clear the poll and print the results.
	 */
	private void pollTimerSchedule() {
		pollTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (pollCommand instanceof CommandChoosePoll) {
					CommandChoosePoll poll = (CommandChoosePoll) pollCommand;
					poll.printResults();
				}
				pollCommand = null;

			}
		}, 600 * 1000);

	}

}
