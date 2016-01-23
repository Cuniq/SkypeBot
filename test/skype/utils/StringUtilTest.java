package skype.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public final void testSplitIngoringQuotesShouldNotFail() {
		String testString = "    split this    but \"NOT   THIS\"  ";
		String except[] = { "split", "this", "but","NOT   THIS" };
		String result[] = StringUtil.splitIngoringQuotes(testString);
		
		for(int i =0;i<result.length;i++){
			System.out.println(result[i]);
			assertEquals(
					"Each result must be the equal with each single word inside the testString unless if it is inside quotes",
					except[i], result[i]);
		}
	}

}
