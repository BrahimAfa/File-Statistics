package FileIOManupilation;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;


public class FileStatistics {
	private String _FileName;
	private File _File;
	private FileChannel _FileChannel;
	private StringBuilder _strbuilder;
	private FileInputStream _FIS;
	private ByteBuffer _buffer;
	private Object[] Words;
	// To keep Tracking File Statistics To save Them Easily
	private int _WordsCount;
	private int _CharactersCount;
	private int _SentencesCount;
	private int _ParagraphCount;
	//Patterns For Splitting Strings
	private final String WORD_SPLIT_PATTERN = "[\\s\\d.?!:,%]+";
	private final String SENTENCE_SPLIT_PATTERN = "[.?!,:]+";
	private final Comparator<Integer> SORTING_COMARATOR = (x,y)-> x<y ? 1:-1;
	
	//byte code for Space : ==> " "
	//private final byte SPACE_SEPERATOR =32;

	public FileStatistics(String FileName) throws IOException {
		//this.setFileName(FileName);
		this._FileName = FileName;
		this._File = new File(this._FileName);	
		//if file does not exist this function exits the Application with NotFound Exception
		CheckFileExist();
		ReadFile();
	}
	
	public int getCharCount(boolean countCharOnly) {
		_CharactersCount = countCharOnly ? get_Char_Count_With_No_WhiteSpace(_buffer.array()) : _buffer.capacity();
		return _CharactersCount;
	}
	
	public int getWordCount(boolean countLinkingWords) {
		_WordsCount =  countLinkingWords ? _strbuilder.toString().split(WORD_SPLIT_PATTERN).length : get_Words_Count_With_No_Linking_Words();
		return _WordsCount;
	}
	
	public int getSentenceCount() {
		_SentencesCount = _strbuilder.toString().split(SENTENCE_SPLIT_PATTERN).length;
		return _SentencesCount;
	}
	
	public int getParagraphCount() {
		_ParagraphCount =  _strbuilder.toString().split("\n").length;
		return _ParagraphCount;
	}
	
	public void SaveCounts(String FileName) throws IOException {
		FileWriter fw = new FileWriter(new File("Assets/"+FileName));
		fw.write(String.format("Saved at : %s",new Date().toString()));
		fw.write(Character.LINE_SEPARATOR);
		fw.write(String.format("File Statistics For : \" %s \"",_FileName));
		fw.write(Character.LINE_SEPARATOR);
		fw.write(String.format("There is %d Characters.",_CharactersCount));
		fw.write(Character.LINE_SEPARATOR);
		fw.write(String.format("There is %d Words.",_WordsCount));
		fw.write(Character.LINE_SEPARATOR);
		fw.write(String.format("There is %d Sentences.",_SentencesCount));
		fw.write(Character.LINE_SEPARATOR);
		fw.write(String.format("There is %d Paragraphes.",_ParagraphCount));
		fw.flush();
		fw.close();
	}
	
	public void SaveCounts(String FileName,HashMap<String,Integer> words) throws IOException{
		FileWriter fw = new FileWriter(new File("Assets/"+FileName));
		fw.write(String.format("Saved at : %s",new Date().toString()));
		fw.write(Character.LINE_SEPARATOR);
		Collection<Integer> Vals =   words.values();
		Set<Entry<String, Integer>> setEntry = words.entrySet();
		//if i didn't use distinct i should use these "x==y ? 0:x<y ? 1:-1"
	    Vals.stream().distinct().sorted(SORTING_COMARATOR).forEach(value->{
	    	setEntry.forEach(entry->{
	    		if(value==entry.getValue()) {
						try {
							fw.write(String.format("%d : %s",entry.getValue(),entry.getKey()));
							fw.write(Character.LINE_SEPARATOR);
						} catch (IOException e) {
							e.printStackTrace();
						}
	    		}
	    	});
	    });
		fw.flush();
		fw.close();
	}
	
	public HashMap<String, Integer> getDictionary() {
		if (Words==null ) {
			get_Words_Count_With_No_Linking_Words();
		}
		HashMap<String, Integer> wordsAccurance = new HashMap<>();
		for (Object obj : Words) {
			if(wordsAccurance.containsKey(obj)) {
				wordsAccurance.replace((String)obj,wordsAccurance.get(obj)+1);
				continue;
			}
			wordsAccurance.put((String)obj,1);
		}
		return wordsAccurance;
	} 
	
	// ===============> Private Functions <===============
	
	private int get_Char_Count_With_No_WhiteSpace (byte[] buffer) 
	{
		int count = 0;
		for (byte b : buffer) {
			if (Character.isWhitespace((char)b)) continue;
			count++;
		}
		return count;
	}
	
	private int get_Words_Count_With_No_Linking_Words() 
	{
		//String[] s = {"and","you","in","the","on","a","as"};
		List<String> linkingWords = new ArrayList<String>();
		linkingWords.add("and");linkingWords.add("you");
		linkingWords.add("in");linkingWords.add("the");
		linkingWords.add("on");linkingWords.add("a");
		linkingWords.add("are");linkingWords.add("to");
		linkingWords.add("as");linkingWords.add("your");
		linkingWords.add("of");linkingWords.add("he");
		linkingWords.add("she");linkingWords.add("it");
		linkingWords.add("we");linkingWords.add("is");
		linkingWords.add("that");linkingWords.add("has");
		linkingWords.add("our");linkingWords.add("who");
		linkingWords.add("or");
		String[] wordArray = _strbuilder.toString().split(WORD_SPLIT_PATTERN);
		List<String> WordsList= Arrays.asList(wordArray);
		Words = WordsList.stream().filter(d->!linkingWords.contains(d.toLowerCase())).toArray();
		return Words.length ;
	}
	
	private void ReadFile() throws IOException {
		this._FIS = new FileInputStream(this._File);
		_FileChannel = _FIS.getChannel();
		_buffer = ByteBuffer.allocate((int)_FileChannel.size());
		_FileChannel.read(_buffer);
		 _strbuilder = new StringBuilder();
		for (byte b : _buffer.array())  _strbuilder.append((char)b);
		
		_FileChannel.close();
		_FIS.close();
	}
	
	private void CheckFileExist() throws FileNotFoundException {
		if(!_File.exists()) throw new FileNotFoundException(String.format("==> File %s does not exist Pleas look somewhere else", _FileName));
		
	}

	// ===============> GETTERS AND SETTERS <===============
	public String getFileName() {
		return _FileName;
	}

	public void setFileName(String FileName) {

	}

	public File getFile() {
		return _File;
	}
}

