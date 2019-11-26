
import java.util.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map.*;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.SortOrder;

import FileIOManupilation.FileStatistics;

public class MainForTesting {

	public static void main(String[] args) throws FileNotFoundException,IOException {
		File file = new File("Assets/Shenanigans.txt");
		System.out.println(file.exists());
		FileInputStream fis = new FileInputStream(file);
		
		FileChannel fc = fis.getChannel();
		int size =  (int) fc.size();
		System.out.println(size/1024);
		ByteBuffer buffer = ByteBuffer.allocate(size);
		fc.read(buffer);
		
		System.out.println("=======>TESTS START<======");
		System.out.println(" ".getBytes()[0]);
		System.out.println(buffer.capacity());
		System.out.println(buffer.array().length);
		System.out.println("Hello".getBytes().length);
	//	System.out.printf("%s",Character.);
		System.out.println("=======>TESTS END<======");
		FileStatistics FS = new FileStatistics("Assets/Shenanigans.txt");
	//System.out.println(FS.getCharCount(false));
		//System.exit(0);
		byte[] buff = buffer.array();
	
		//System.out.println(" ".getBytes()[0]);

		int test = 0;
		
		StringBuilder str = new StringBuilder();
		for (byte b : buff) {
			
			str.append((char)b);
			//if (b== 32) test++; //continue;
			
		}
		
		//System.out.println("\n"+test);
		List<String> strConstants = new ArrayList<String>();
		
		//String[] s = {"and","you","in","the","on","a","as"};
		strConstants.add("and");
		strConstants.add("you");
		strConstants.add("in");
		strConstants.add("the");
		strConstants.add("on");
		strConstants.add("a");
		strConstants.add("are");
		strConstants.add("to");
		strConstants.add("as");
		strConstants.add("your");
		strConstants.add("of");
		strConstants.add("he");
		strConstants.add("she");
		strConstants.add("it");
		strConstants.add("we");
		strConstants.add("is");
		strConstants.add("that");
		strConstants.add("has");
		strConstants.add("our");
		strConstants.add("who");
		strConstants.add("or");
		
		//strConstants.forEach(d->System.out.println(d));;
		//System.out.println(str.toString().split("[\\s+.]"));
		String[] strArr = str.toString().split("[\\s\\d.?!:,%]+");
		
		System.out.println("TIME TES START START::");
		List<String> Words = new ArrayList<String>();
		long time= System.currentTimeMillis();
		//
//		for (int i = 0; i < strArr.length; i++) {
//			boolean state = false;
//			
//			for (String s2 : strConstants) {
//				//System.out.println(strArr[i].toLowerCase()+ " == " + s2 + "=" +( strArr[i].toLowerCase().equals(s2)));
//				if(strArr[i].trim().toLowerCase().equals(s2))  {
//					//System.out.println(strArr[i]+" = " + s2);
//					state = true; 
//					break;
//				}
//			}
//			if(!state) {
//				Words.add(strArr[i]);
//			}
//		}
		//this way is poor in huge files but good in litle files (meeh)

//		System.out.printf("Found %d Words in %d ms",Words.size(),System.currentTimeMillis()-time);
//		System.out.println("=====>"+strArr.length);

		
		System.out.println(FS.getCharCount(false));
		System.out.println(FS.getWordCount(false));
		System.out.println(FS.getSentenceCount());
		System.out.println(FS.getParagraphCount());
		FS.SaveCounts("Statistics_Shenanigans.txt");
		System.out.println("===> Length "+Words.size());
		Words.clear();
		time= System.currentTimeMillis();
		for (String s : strArr) {
				Words.add(s);
		}
		//this way is best in huge files 
		Object[] c=   Words.stream().filter(d->!strConstants.contains(d.toLowerCase())).toArray();
		System.out.printf("Found %d Words in %d ms",c.length,System.currentTimeMillis()-time);
		System.out.println("=====>"+strArr.length);
		
//		FileWriter fw = new FileWriter(new File("Assets/Statics.txt"));
//		fw.write("\t\tStatics for file Shinenagans.txt");
//		fw.write("");
//		fw.write("\t\tStatics for file Shinenagans.txt");
//		fw.flush();
//		fw.close();
		//System.out.println("===> Length "+words2.length);
	//	for (int i = 0; i < words2.length; i++) {
	//		System.out.println(words2[i]);
	//	}
		
		
		HashMap<String, Integer> wordsAccurance = new HashMap<>();
		time= System.currentTimeMillis();
		for (Object obj : c) {
			if(wordsAccurance.containsKey(obj)) {
				wordsAccurance.replace((String)obj,wordsAccurance.get(obj)+1);
				continue;
			}
			wordsAccurance.put((String)obj,1);
		}
		
		System.out.printf("\nHash map is Filled in %dms",System.currentTimeMillis()-time);
		Comparator<Integer> C1= (x,y)-> x<y ? 1:-1;
//		//if you want to show words Sorted By Occurrence + words
//		TreeMap<String, Integer> sortedTree = new TreeMap<String, Integer>();
//		//after adding HashMap Keys are sorted "Only Keys"
//		sortedTree.putAll(wordsAccurance);
		
		Collection<Integer> Vals =   wordsAccurance.values();
		Set<Entry<String, Integer>> setEntry = wordsAccurance.entrySet();
		//if i didn't use distinct i should use these "x==y ? 0:x<y ? 1:-1"
	    Vals.stream().distinct().sorted(C1).forEach(x->{
	    	setEntry.forEach(y->{
	    		if(x==y.getValue()) {
	    			System.out.printf("\n%s ==> %d",y.getKey(),y.getValue());
	    		}
	    	});
	    });
	    FS = new FileStatistics("Assets/Shenanigans.txt");
	    FS.SaveCounts("Dictionary.txt", FS.getDictionary());
		System.exit(0);
		Iterator<Entry<String, Integer>> itEntry = wordsAccurance.entrySet().iterator();
		while(itEntry.hasNext()) {
			 Entry<String, Integer> val = itEntry.next();
			System.out.printf("\n%s ==> %d",val.getKey(),val.getValue());
		}
		
		
	

	}

}
