package it.cybion.influence.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ListFileFacade {
	
	private Scanner scanner;
	private String currentLine;
	private String commentString;
	private int currentLineNumber = 0;
	private String filePath;
	
	public ListFileFacade(String filePath, String commentString) throws FileNotFoundException, EmptyFileException, NoUncommentedLinesException {
		this.scanner = new Scanner(new FileInputStream(filePath));
		this.commentString = commentString;
		this.filePath = filePath;
		/*
		if (! scanner.hasNextLine()) 
			throw new EmptyFileException("File is empty.");
		
		String line = scanner.nextLine();
		while (line.startsWith(commentString) && scanner.hasNextLine()) {
			line =  scanner.nextLine();
			currentLineNumber++;
		}
	
		if (line.startsWith(commentString) && !scanner.hasNextLine()) 
			throw new NoUncommentedLinesException("All lines of the file are commented.");
		
		currentLine = line;
		*/
	}
	
	public String getFirstUncommentedLine() throws FileFinishedException {
		/*
		if (currentLine==null)
			throw new FileFinishedException("File has no more lines");
		String exCurrentLine = currentLine;
		if (scanner.hasNextLine())
			currentLine = scanner.nextLine();
		else
			currentLine = null;
		return exCurrentLine;
		*/
		if (this.scanner.hasNext()) {
			currentLineNumber++;
			String line = this.scanner.nextLine();
			if (line.startsWith(commentString))
				return getFirstUncommentedLine();
			else 
				return line;
		}
		else
			throw new FileFinishedException("File has no more lines");			
	}
	
	public void commentCurrentLine() {
		Writer out = new OutputStreamWriter(new FileOutputStream(fFileName), fEncoding);
		
	}
	
}
