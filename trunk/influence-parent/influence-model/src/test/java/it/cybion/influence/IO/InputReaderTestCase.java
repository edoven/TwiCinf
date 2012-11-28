package it.cybion.influence.IO;

import static org.testng.Assert.assertEquals;

import it.cybion.influence.IO.InputReader;

import java.io.IOException;

import org.testng.annotations.Test;

public class InputReaderTestCase {

	@Test
	public void fileContentToSingleLineTEST() {
		String ls = System.getProperty("line.separator");
		String fileContent = "riga1"+ls+"riga2"+ls+"riga3"+ls;
		String readedContent;
		try {
			readedContent = InputReader.fileContentToSingleLine("src/test/resources/text.txt");
			assertEquals(fileContent, readedContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
