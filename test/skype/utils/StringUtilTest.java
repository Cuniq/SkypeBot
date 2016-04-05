package skype.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringUtilTest {

	@Test
	public final void bigStringSplitIngoringQuotesShouldNotFail() {
		String testString = "    split this    but \"NOT   THIS\"  ";
		String except[] = { "split", "this", "but","NOT   THIS" };
		String result[] = StringUtil.splitIgoringQuotes(testString);
		
		for(int i =0;i<result.length;i++){
			assertEquals(
					"Each result must be the equal with each single word inside the testString unless if it is inside quotes",
					except[i], result[i]);
		}
	}

	@Test
	public final void smallStringSplitIngoringQuotesShouldNotFail() {
		String testString = "!      ";
		String except[] = { "!" };
		String result[] = StringUtil.splitIgoringQuotes(testString);

		for (int i = 0; i < result.length; i++) {
			assertEquals(
				"Each result must be the equal with each single word inside the testString unless if it is inside quotes",
				except[i], result[i]);
		}
	}

	@Test
	public final void emptyStringSplitIngoringQuotesShouldNotFail() {
		String testString = "";
		String except[] = { "" };
		String result[] = StringUtil.splitIgoringQuotes(testString);

		for (int i = 0; i < result.length; i++) {
			assertEquals(
				"Each result must be the equal with each single word inside the testString unless if it is inside quotes",
				except[i], result[i]);
		}
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public final void nullStringSplitIngoringQuotesShouldFail() {
		String testString = null;

		exception.expect(NullPointerException.class);
		StringUtil.splitIgoringQuotes(testString);

	}

}
