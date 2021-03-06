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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import skype.commands.Command;
import skype.gui.popups.WarningPopup;

/**
 * The Class CommandClassFinder. This class is used to find command classes inside
 * our code at runtime in both IDE and when running from jar. It will search for
 * classes only inside the commands package and will return <b>only</b> subclass of
 * {@link Command Command} class. It will not return the upper class Command. This
 * class is useful for making bot commands more dynamic. Program can find commands
 * itself than programmer have to hard code every single command in different places.
 * ( Like help command )
 *
 * @author Thanasis Argyroudis
 * @see JarFile
 * @see JarEntry
 * @since 1.0
 */
public class CommandClassFinder {

	/** The package's relative path as it is in disk. */
	private static final String RELATIVE_PACKAGE_NAME = "skype/commands";

	/** The package's path as it is inside java environment. */
	private static final String PACKAGE_NAME = "skype.commands";

	/**
	 * The list containing all classes. The classes won't change at running so one
	 * time initialization its fine.
	 */
	private static final List<Class<Command>> classes = new ArrayList<>();

	/**
	 * Searches for command classes inside the project. The command file must be
	 * inside the "commands" package. If you move them please change this class too
	 * in order some commands to work.
	 *
	 * @return the list from Command classes except from the upper class
	 *         {@link Command}
	 */
	public static synchronized List<Class<Command>> findCommandClasses() {
		if (!classes.isEmpty())
			return classes;

		try {
			findCommandsFromRunningJar();
		} catch (IOException IDERunning) {
			findCommandsInsideIDE();
		}

		return classes;
	}

	/**
	 * Private constructor because this a utility class. We don't need instances.
	 */
	private CommandClassFinder() {

	}

	/**
	 * Finds all commands when running the program from IDE. Please note that command
	 * must be inside the commands package inside classpath.
	 * 
	 * @return the list
	 */
	private static void findCommandsInsideIDE() {
		final URL scannedUrl = Thread
				.currentThread().getContextClassLoader().getResource(RELATIVE_PACKAGE_NAME);

		if (scannedUrl == null) {
			System.out.println("INVALID COMMANDS_PACKET PATH");
		}

		final File scannedDir = new File(scannedUrl.getFile());
		for (File file : scannedDir.listFiles()) {
			final int endIndex = file.getName().length() - ".class".length(); //Remove .class prefix
			final String className = file.getName().substring(0, endIndex);

			final Class<Command> command = getCommand(className);
			if (command != null)
				classes.add(command);
		}

	}

	/**
	 * Finds all commands when running the program from jar file. Please note that
	 * command must be inside the commands package inside classpath.
	 *
	 * @return the list with classes
	 * @throws IOException
	 */
	private static void findCommandsFromRunningJar() throws IOException {
		String urlWithClasses = Thread
				.currentThread().getContextClassLoader().getResource(RELATIVE_PACKAGE_NAME)
				.toString();
		//Jar path is like "jar:file:PATHTOJAR.jar!"
		//With 10 we remove the first 2 words and then we remove !
		String jarPath = urlWithClasses.substring(10).replaceFirst(".jar!", ".jar");
		JarFile jarFile = null;
		
		try {
			jarFile = new JarFile(jarPath);
			classes.addAll(findCommandEntries(jarFile));
			jarFile.close();
		} catch (IOException runningFromIDE) {
			throw runningFromIDE; //We just run the code from IDE
		} catch (SecurityException e) {
			new WarningPopup("Security can't let us process the jar file");
		}

	}


	/**
	 * Finds all commands inside the PACKAGE_NAME package.
	 *
	 * @param jar
	 *            the jar
	 * @return the list
	 */
	private static List<Class<Command>> findCommandEntries(JarFile jar) {
		final List<Class<Command>> clazzes = new ArrayList<>();
		final Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();

			if (entry.isDirectory()) // We only need classes not directories
				continue;
			
			Class<Command> command = extractCommandFromEntry(entry);
			
			if (command != null)
				clazzes.add(command);
		}
		return clazzes;
	}

	/**
	 * 
	 * Extract the command from the given jar entry. Example of class name found
	 * inside the jar: skype/commands/Classname.java
	 * 
	 * @param entry
	 *            the entry from jar
	 * @return the command if it meets the preconditions null otherway.
	 */
	private static Class<Command> extractCommandFromEntry(JarEntry entry) {
		if (entry.getName().contains(RELATIVE_PACKAGE_NAME)) {

			final String className = entry
					.getName().substring(RELATIVE_PACKAGE_NAME.length() + 1)
					.replaceFirst(".class", "");

			return getCommand(className);

		}
		return null;
	}
	
	/**
	 * Checks if the given entry is a Command's subclass and not the upperclass.
	 *
	 * @param className
	 *            Just the class name without the whole package.
	 * @return the command if it meet the preconditions null otherway.
	 */
	@SuppressWarnings("unchecked")
	private static Class<Command> getCommand(String className) {
		try {
			final Class<?> commandClass = Class.forName(
				PACKAGE_NAME + "." + className, false,
				ClassLoader.getSystemClassLoader());

			if (Command.class.isAssignableFrom(commandClass)
					&& !Command.class.equals(commandClass)) {
				return (Class<Command>) commandClass;
			}
		} catch (ClassNotFoundException e) {
			new WarningPopup("Found a class that not exist?");
		}
		return null;
	}

}