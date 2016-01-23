package skype.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FileUtilTest {

	private final String fileName = "test.txt";

	private final Path filePath = Paths.get(fileName);

	private final String testText = "Test write";

	private final void testOpenReadFile() throws IOException {

		BufferedReader reader = FileUtil.openReadFile(filePath);

		assertEquals(
				"Reading what was written previously on file. Must be the same.", testText,
				reader.readLine());
		reader.close();
	}

	private final void testOpenWriteFile() throws IOException {

		BufferedWriter writer = FileUtil.openWriteFile(filePath);

		writer.write(testText);
		writer.close();

	}

	@Test
	public final void testWritingAndReadingFileShouldNotFail() {
		try {
			testOpenWriteFile();
			testOpenReadFile();
		} catch (IOException e) {
			fail("A exception happened while reading or writing to fail" + "\r\n" + e.getMessage());
		}

		//Clean up
		File file = new File(filePath.toUri());
		file.delete();
	}

}
