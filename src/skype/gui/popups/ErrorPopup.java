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
package skype.gui.popups;

import static skype.Main.getMainFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * The Class ErrorPopup. This class will display an error message and will shutdown
 * the program.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class ErrorPopup {

	/** The option pane. */
	private final JOptionPane optionPane;

	/** The dialog. */
	private final JDialog dialog = new JDialog(getMainFrame(), "Fatal Error");

	/**
	 * Show popup error.
	 *
	 * @param message
	 *            the message
	 */
	public ErrorPopup(String message) {
		optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);

		dialog.setAlwaysOnTop(true);
		dialog.setLocationRelativeTo(getMainFrame());
		dialog.setContentPane(optionPane);
		dialog.pack();
		dialog.setVisible(true);

		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (e.getSource() == optionPane && prop.equals(JOptionPane.VALUE_PROPERTY)) {
					dialog.setVisible(false);
					dialog.dispose();
					System.exit(-5);
				}
			}
		});

	}

}
