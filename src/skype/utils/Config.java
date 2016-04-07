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
package skype.utils;

import static skype.utils.FileStreamUtil.closeInputStream;

import java.io.File;
import java.nio.file.Path;

import skype.Main;
import skype.gui.popups.ErrorPopup;

/**
 * The Class Config. This class holds all constants given from user in properties
 * files.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
//TODO: Make that class abstract and create subclass for each "config-family" of options. Eg. SPAM HANDLING config
public final class Config {
	
	/** The Constant CONGIF_PATH. */
	static final private String CONFIG_PATH;
	
	/** The properties parser. */
	private static PropertiesParser parser;
	
	//------ SPAM HANDLING
	/** Enable warnings. */
	static public final boolean EnableWarnings;

	/** Enable warnings for bot user. */
	static public final boolean EnableSelfWarnings;

	/** Interval between messages in order to increase warnings. */
	static public final Long WarningInterval;

	/** Action to take when warnings reach {@link #WarningNumber}. */
	static public final int WarningAction;

	/* Action that a spam (normal chat) handler can use */
	/** Set to listener action */
	static public final int WARNING_ACTION_SET_LISTENER = 2;
	/** Kick from chat action */
	static public final int WARNING_ACTION_KICK = 3;
	/** kickban from chat action */
	static public final int WARNING_ACTION_KICKBAN = 4;

	/** The amount of warnings that is needed in order to take order. */
	static public final int WarningNumber;

	/** The minutes period that needs to pass in order to reset warnings. */
	static public final int WarningResetTime;
	//------ END SPAM

	//------ COMMAND HANDLING
	/** Enable commands for users. */
	static public final boolean EnableUserCommands;

	/** Maximum number of commands per day for users. */
	static public final int MaximumNumberCommands;

	/** Excluded users from using commands. */
	static public final String[] ExcludedUsers;

	/** Exclude commands. */
	static public final String[] ExcludedCommands;
	//------ END COMMANDS

	//------ EDIT HANDLING
	/** Enable edit tracking. */
	static public final boolean EnableEdits;
	
	/** Enable edit tracking for bot user. */
	static public final boolean EnableSelfEdits;

	/** Where you want original messages to be displayed */
	static public final int EditOutput;

	/* Methods that an edit handler can use to display edited messages */
	/** Print original message in same chat which the edit happened */
	static public final int EDIT_OUTPUT_SAME_CHAT = 1;
	/** Print original message in the user's chat who made the edit */
	static public final int EDIT_OUTPUT_PRIVATE_CHAT = 2;
	/** Write original message to a file */
	static public final int EDIT_OUTPUT_TO_FILE = 3;

	/** Path of logger file for edits. */
	static public final String EditPath;
	//------ END EDITS

	static { //Initiating all final values.

		CONFIG_PATH = findConfigPath();
		parser = new PropertiesParser(CONFIG_PATH);

		EnableWarnings = Boolean.parseBoolean(parser.getProperty("EnableWarnings", "false"));
		EnableSelfWarnings = Boolean.parseBoolean(parser.getProperty("EnableSelfWarnings", "false"));

		EnableUserCommands = Boolean.parseBoolean(parser.getProperty("EnableUserCommands", "false"));

		EnableEdits = Boolean.parseBoolean(parser.getProperty("EnableEdits", "false"));
		EnableSelfEdits = Boolean.parseBoolean(parser.getProperty("EnableSelfEdits", "false"));

		try {
			
			Long.parseUnsignedLong(parser.getProperty("WarningInterval", "820"));
			Byte.parseByte(parser.getProperty("WarningAction", "1"));
			Integer.parseInt(parser.getProperty("WarningNumber", "1"));
			Integer.parseInt(parser.getProperty("WarningReset", "10"));

			Integer.parseInt(parser.getProperty("MaximumNumberCommands", "10"));
			Byte.parseByte(parser.getProperty("EditOutput", "2"));

		} catch (NumberFormatException e) {
			new ErrorPopup("Invalid value " + e.getMessage().toLowerCase());
		}

		WarningInterval = Long.parseUnsignedLong(parser.getProperty("WarningInterval", "100"));
		WarningAction = Byte.parseByte(parser.getProperty("WarningAction", "1"));
		WarningNumber = Integer.parseInt(parser.getProperty("WarningNumber", "1"));
		WarningResetTime = Integer.parseInt(parser.getProperty("WarningResetTime", "10"));
		
		MaximumNumberCommands = Integer.parseInt(parser.getProperty("MaximumNumberCommands", "10"));
		ExcludedUsers = parser.getProperty("ExcludedUsers", "").split(",");
		ExcludedCommands = parser.getProperty("ExcludedCommands", "").split(",");

		EditOutput = Byte.parseByte(parser.getProperty("EditOutput", "2"));
		EditPath = parser.getProperty("EditPath", "");
		if (EditPath.contains("//"))
			EditPath.replaceAll("//", "////");

		closeInputStream(parser.getInputStream());
	}

	/**
	 * The purpose of this method is just to execute the static block.
	 */
	public static void initate() {

	}

	/**
	 * No need of instances.
	 */
	private Config() {

	}

	/**
	 * Searches if the config file is at the same folder with jar. If not opens a
	 * file chooser and waits input from user.
	 *
	 * @return a string containing config path.
	 */
	private static final String findConfigPath() {
		if (new File("Config.txt").exists())
			return "Config.txt";

		Path path = new ConfigFileChooser().showOpenDialog(Main.getMainFrame());

		if (path == null) {
			new ErrorPopup("Invalid file.");
		}

		return path.toString();
	}

}
