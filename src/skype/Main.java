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
package skype;

import javax.swing.JFrame;
import javax.swing.JLabel;

import skype.gui.popups.ErrorPopup;
import skype.listeners.GroupChatAdderListener;
import skype.utils.Config;

import com.skype.Skype;
import com.skype.SkypeException;

/**
 * The Class Main has the {@code public static void main(String[] args)} method.
 */
public class Main {

	/**
	 * The main method is responsible for finding the desired group and add the
	 * listener for this group. Please note the {@link Skype#getAllBookmarkedChats()
	 * }
	 * . That means the desired group must be on user's "Favorites".
	 *
	 */
	public static void main(String[] args) {
		Skype.setDaemon(false);
		Config.initate();
		try {
			Skype.addChatMessageListener(new GroupChatAdderListener());
		} catch (SkypeException e1) {
			new ErrorPopup("Can not connect with skype.");
		}

		JLabel label = new JLabel("SKYPE CRACKING SINCE 1999");
		JFrame frame = new JFrame("Skype cracker.");
		frame.add(label);
		frame.setSize(666, 333);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
 