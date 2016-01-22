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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The Class DateUtil. This class provides utilities for the date managements. Uses
 * the latest JDK8 date features
 * 
 * @author Thanasis Argyroudis
 * @since 1.0
 */
public class DateUtil {

	/**
	 * No need for instances. Static access
	 */
	private DateUtil() {

	}

	/**
	 * Gets the midnight of the following day.
	 *
	 * @return Date object of the following day at 00:00
	 */
	static public Date getMidnight() {
		final LocalTime midnight = LocalTime.MIDNIGHT;
		final LocalDate today = LocalDate.now(ZoneId.systemDefault());
		final LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		final LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);
		return Date.from(tomorrowMidnight.atZone(ZoneId.systemDefault()).toInstant());
	}

}
