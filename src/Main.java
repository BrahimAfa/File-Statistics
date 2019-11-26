import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import FileIOManupilation.FileStatistics;

public class Main {

	public static void main(String[] args) throws IOException {

		FileStatistics FS = new FileStatistics("Assets/Shenanigans.txt");
		
		System.out.println(FS.getCharCount(false));
		System.out.println(FS.getWordCount(false));
		System.out.println(FS.getSentenceCount());
		System.out.println(FS.getParagraphCount());
		FS.SaveCounts("Statistics_Shenanigans.txt");
		
	}

}
