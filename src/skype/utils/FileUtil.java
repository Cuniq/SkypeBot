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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import skype.gui.popups.WarningPopup;

/**
 * The Class FileUtil. This class provides basic utilities for files.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class FileUtil {

	/**
	 * Static access only
	 */
	private FileUtil() {

	}
	
	/**
	 * Open read file.
	 *
	 * @param path
	 *            the path
	 * @return the buffered reader
	 */
	public static BufferedReader openReadFile(Path path) {
		try {
			return Files.newBufferedReader(path);
		} catch (IOException e) {
			new WarningPopup(e.toString());
		}
		return null;
	}

	/**
	 * Open's a file at given path with the specified options.
	 *
	 * @param path
	 *            the path
	 * @return the buffered writer
	 */
	public static BufferedWriter openWriteFile(Path path, OpenOption... options) {
		try {
			return Files.newBufferedWriter(path, options);
		} catch (IOException e) {
			new WarningPopup(e.toString());
		}
		return null;
	}

	/**
	 * Open's a file at given path with the specified options.
	 *
	 * @param path
	 *            the path
	 * @return the buffered writer
	 */
	public static BufferedWriter openWriteFile(Path path) {
		try {
			return Files.newBufferedWriter(path);
		} catch (IOException e) {
			new WarningPopup(e.toString());
		}
		return null;
	}

}
