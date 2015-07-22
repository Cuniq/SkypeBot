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
package skype.utils.users;

import skype.gui.popups.ErrorPopup;

import com.skype.Skype;
import com.skype.SkypeException;

/**
 * The Class BotAdminInfo. This class will contain information about the user of the
 * bot. Information like ID, name, etc.
 *
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class BotAdminInfo {

	/** Admin's name. */
	static private String name;

	/** Admin's id. */
	static private String id;

	static {
		try {
			name = Skype.getProfile().getFullName();
			id = Skype.getProfile().getId();
		} catch (SkypeException e) {
			new ErrorPopup(e.getMessage());
		}
	}

	private BotAdminInfo() {

	}

	public static String getAdminName() {
		return name;
	}

	public static String getAdminID() {
		return id;
	}

}
