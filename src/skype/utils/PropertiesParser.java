/*
 *    Copyright [2015] [Thanasis Argyroudis]

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

import static skype.utils.FileStreamUtil.fileAsInputStream;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import skype.gui.popups.ErrorPopup;

/**
 * The Class PropertiesParser. Simple properties parser from a path or file.
 *
 * @see java.util.Properties
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class PropertiesParser {

	/** The properties. */
	private Properties properties = new Properties();

	/**
	 * Instantiates a new properties parser.
	 *
	 * @param path
	 *            the path
	 */
	public PropertiesParser(String path) {
		try {
			properties.load(fileAsInputStream(path));
		} catch (IOException e) {
			new ErrorPopup(e.getMessage());
		} catch (IllegalArgumentException e) {
			new ErrorPopup(e.getMessage());
		}
	}
	
	/**
	 * Instantiates a new properties parser.
	 *
	 * @param file
	 *            the file
	 */
	public PropertiesParser(File file) {
		try {
			properties.load(fileAsInputStream(file));
		} catch (IOException e) {
			new ErrorPopup(e.getMessage());
		} catch (IllegalArgumentException e) {
			new ErrorPopup(e.getMessage());
		}
	}

	/**
	 * Gets the property.
	 *
	 * @param key
	 *            the key
	 * @return the property
	 * 
	 * @see {@link Properties#getProperty(String)}
	 */
	public String getProperty(String key) {
		return properties.getProperty(key).trim();
	}
	
	/**
	 * Gets the property. If it can not find the key it returns the default value.
	 *
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value if no key found
	 * @return the property
	 * 
	 * @see {@link Properties#getProperty(String, String)}
	 */
	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue).trim();
	}

}
