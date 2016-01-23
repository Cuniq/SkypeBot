package skype.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilTest {

	@Test
	public final void testGetMidnightShouldNotFail() {
		final LocalTime midnight = LocalTime.MIDNIGHT;
		final LocalDate today = LocalDate.now(ZoneId.systemDefault());
		final LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		final LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		Assert.assertEquals(
				"Get Midnight should return today + 1 day at 00:00",
				Date.from(tomorrowMidnight.atZone(ZoneId.systemDefault()).toInstant()),
				DateUtil.getMidnight());

	}

}
