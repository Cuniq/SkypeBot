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
package skype.gui.popups;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Class ErrorPopup. This class will display an error message and will shutdown
 * the program.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class ErrorPopup {

	/**
	 * Show popup error.
	 *
	 * @param message
	 *            the message
	 */
	public ErrorPopup(String message) {
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
		final JDialog dialog = new JDialog((JFrame) null, "Fatal Error");

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

		dialog.setAlwaysOnTop(true);
		dialog.setLocationRelativeTo(null);
		dialog.setContentPane(optionPane);
		dialog.pack();
		dialog.setVisible(true);

	}

}