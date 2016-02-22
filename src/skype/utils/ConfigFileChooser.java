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

import java.awt.Component;
import java.io.File;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Class ConfigFileChooser. This class will create the <code>JFileChooser</code>
 * for the config file.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class ConfigFileChooser {

	/** The file chooser. */
	private JFileChooser fileChooser = new JFileChooser(".");

	/**
	 * Normal constructor will create a file chooser for the config file.
	 */
	public ConfigFileChooser() {
		fileChooser.setFileFilter(new FileNameExtensionFilter("Config file", "txt", "config"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setApproveButtonText("Accept config");
		fileChooser.setDialogTitle("Config file chooser");
	}

	/**
	 * Show open dialog.
	 *
	 * @param parent
	 *            the parent
	 * @return the path
	 * 
	 * @see javax.swing.JFileChooser#showOpenDialog(Component)
	 */
	public Path showOpenDialog(Component parent) {
		int result = fileChooser.showOpenDialog(parent);

		if (result == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();

			if (acceptFile(file.getName()))
				return file.toPath();

		}

		return null;
	}

	/**
	 * Checks if the chosen file is a config file.
	 *
	 * @param name
	 *            the name
	 * @return true, if successful
	 */
	private boolean acceptFile(String name) {
		String extention = null;
		
		int last = name.lastIndexOf(".");
		if(last < 0)
			return false;
		
		extention = name.substring(last + 1);

		if (extention.equalsIgnoreCase("txt") || extention.equalsIgnoreCase("config"))
			return true;
		return false;
	}

}
