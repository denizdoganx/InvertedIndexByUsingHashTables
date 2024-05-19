


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

public class Test {
	//constantly used structures are written as static
	static Scanner scan;
	static ArrayList<String> stopWords = new ArrayList<>();
	static ArrayList<String> searchWords = new ArrayList<>();
	static HashedDictionary<String, Node> hashTable = new HashedDictionary<>();
	public static boolean readAllFilesAndAddHashTable() {
		//here reading files in bbc folder
		double startTime = System.currentTimeMillis();
		File dir=new File("bbc");
		File[] listOfFolder = dir.listFiles();
		File[] listOfTXT;
		String text;
		String[] splittedText;
		String tempWord;
		
		if(!dir.exists()) {
			System.out.println("(bbc) Folder not found !!!");
			return false;
		}
		else {
			System.out.println("Please wait, Files are uploading to the system....\n");
			for(File folder : listOfFolder) {
				listOfTXT = folder.listFiles();
				for(File file : listOfTXT) {
					text = "";
					try {
						scan = new Scanner(file);
						while(scan.hasNext()) {
							tempWord = scan.next().toLowerCase(Locale.ENGLISH);
							text += tempWord + " ";
						}
						scan.close();
					}
					catch(Exception e) {  }
					splittedText = text.split(DELIMITERS);
					for(int i = 0;i < splittedText.length; i++) {
						if(!inStopWords(splittedText[i])) {
							if(splittedText[i] != null && splittedText[i] != "") {
								hashTable.put(splittedText[i],new Node(folder.getName() + "_" + file.getName()));
							}
						}
					}
				}	
			}
			double endTime = System.currentTimeMillis();
			double indexingTime = (endTime - startTime)/1000;
			System.out.println("Indexing Time : " + indexingTime + " seconds.");
			return true;
		}
	}
	//here reading words in stop words file
	public static boolean readStopWords() throws Exception {
		
		File f = new File("stop_words_en.txt");
		if(!f.exists()) {
			System.out.println("(stop_words_en.txt) File not found !!! ");
			return false;
		}
		else {
			scan = new Scanner(f);
			while(scan.hasNext())
				stopWords.add(scan.next());
			return true;
		}
	}
	public static boolean readSearchTxt() throws Exception {
		File f = new File("search.txt");
		if(!f.exists()) {
			System.out.println("(search.txt) File not found !!!");
			return false;
		}
		else {
			scan = new Scanner(f);
			while(scan.hasNext())
				searchWords.add(scan.next());
			return true;
		}
	}
	public static boolean inStopWords(String word) {
		if(stopWords.contains(word))
			return true;
		return false;
	}
	public static int chooseHashFunctionCollisionHandlingLoadFactor() {
		boolean found = false;
		int usersChoice = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
			try {
				usersChoice = Integer.parseInt(br.readLine());
				if(usersChoice == 1 || usersChoice == 2)
					found = true;
			}
			catch(Exception e) {  }
			if(!found)
				System.out.println("ERROR !!! Please press 1 or 2");
		}while(!found);
		return usersChoice;
	}
	public static void searchForSearchWordsInHashtable() {
		/*I listed all keys using an iterator
		and searched for words from search.txt in it*/
		Iterator<String> keyIterator;
		double totalTime = 0;
		double startTime, endTime, maxSearchTime = 0, minSearchTime = 1000000000;
		String wordOfMaxSearchTime= "", wordOfMinSearchTime= "";
		double timeToBeFound;
		for(String searchWord : searchWords) {
			startTime = System.nanoTime();
			keyIterator = hashTable.getKeyIterator();
			while(keyIterator.hasNext()) {
				if(searchWord.equals(keyIterator.next()))
					break;
			}
			endTime = System.nanoTime();
			timeToBeFound = (endTime - startTime) / 1000;
			totalTime += timeToBeFound;
			if(timeToBeFound < minSearchTime) {
				minSearchTime = timeToBeFound;
				wordOfMinSearchTime = searchWord;
			}
			if(timeToBeFound > maxSearchTime) {
				maxSearchTime = timeToBeFound;
				wordOfMaxSearchTime = searchWord;
			}
		}
		totalTime = totalTime / 1000;
		System.out.println("Collision count : " + (int)hashTable.getCollisionCount());
		System.out.println("Average Search Time : " + (totalTime / 1000) + " milisecond.");
		System.out.println("Max Search Time : " + (maxSearchTime / 1000) + " milisecond.  " + "Word : " + wordOfMaxSearchTime);
		System.out.println("Min Search Time : " + (minSearchTime / 1000)+ " milisecond.  " + "Word : " + wordOfMinSearchTime);
	}
	public static void MENU() {
		System.out.println("Press 1 -> For Simple Summation Function (SSF)");
		System.out.println("Press 2 -> For Polynomial Accumulation Function (PAF)");
		HashedDictionary.SSF_PAF = chooseHashFunctionCollisionHandlingLoadFactor();
		
		System.out.println("Press 1 -> For Linear Probing (LP)");
		System.out.println("Press 2 -> For Double Hashing (DP)");
		HashedDictionary.LinearProbe_DoubleHashing = chooseHashFunctionCollisionHandlingLoadFactor();
		
		System.out.println("Press 1 -> For LOAD_FACTOR = 0.5");
		System.out.println("Press 2 -> For LOAD_FACTOR = 0.8");
		int loadFactor = chooseHashFunctionCollisionHandlingLoadFactor();
		if(loadFactor == 1)
			HashedDictionary.LOAD_FACTOR = 0.5;
		else
			HashedDictionary.LOAD_FACTOR = 0.8;
	}
	public static void mainFunctions() throws Exception {
		boolean found = false;
		int usersChoice = 0;
		String inputOfUser;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Press 1 -> For Put Command");
		System.out.println("Press 2 -> For Value Command");
		System.out.println("Press 3 -> For Remove Command");
		while(!found) {
			try {
				usersChoice = Integer.parseInt(br.readLine());
				if(usersChoice == 1 || usersChoice == 2 || usersChoice == 3)
					found = true;
			}
			catch(Exception e) { }
			if(!found) {
				System.out.println("ERROR !!! Please press 1 or 2 or 3");
				continue;
			}
			else {
				if(usersChoice == 1) {
					String word, fileReferance;
					System.out.print("Put : ");
					word = br.readLine();
					System.out.print("Enter a source file : ");
					fileReferance = br.readLine();
					hashTable.put(word, new Node(fileReferance));
					found = false;
				}
				else if(usersChoice == 2) {
					System.out.print("Search : ");
					inputOfUser = br.readLine();
					hashTable.getValue(inputOfUser);
					found = false;
				}
				else {
					System.out.print("Remove : ");
					inputOfUser = br.readLine();
					hashTable.remove(inputOfUser);
					found = false;
				}
				System.out.println("Press 1 -> For Put Command");
				System.out.println("Press 2 -> For Value Command");
				System.out.println("Press 3 -> For Remove Command");
			}
		}
	}
	public static void main(String[] args) throws Exception {
		MENU();
		if(readStopWords() && readAllFilesAndAddHashTable() && readSearchTxt()) {
			searchForSearchWordsInHashtable();
			mainFunctions();
		}
	}
	static String DELIMITERS = "[-+=" +

		        " " +        //space

		        "\r\n " +    //carriage return line fit

				"1234567890" + //numbers

				"’'\"" +       // apostrophe

				"(){}<>\\[\\]" + // brackets

				":" +        // colon

				"," +        // comma

				"‒–—―" +     // dashes

				"…" +        // ellipsis

				"!" +        // exclamation mark

				"." +        // full stop/period

				"«»" +       // guillemets

				"-‐" +       // hyphen

				"?" +        // question mark

				"‘’“”" +     // quotation marks

				";" +        // semicolon

				"/" +        // slash/stroke

				"⁄" +        // solidus

				"␠" +        // space?   

				"·" +        // interpunct

				"&" +        // ampersand

				"@" +        // at sign

				"*" +        // asterisk

				"\\" +       // backslash

				"•" +        // bullet

				"^" +        // caret

				"¤¢$€£¥₩₪" + // currency

				"†‡" +       // dagger

				"°" +        // degree

				"¡" +        // inverted exclamation point

				"¿" +        // inverted question mark

				"¬" +        // negation

				"#" +        // number sign (hashtag)

				"№" +        // numero sign ()

				"%‰‱" +      // percent and related signs

				"¶" +        // pilcrow

				"′" +        // prime

				"§" +        // section sign

				"~" +        // tilde/swung dash

				"¨" +        // umlaut/diaeresis

				"_" +        // underscore/understrike

				"|¦" +       // vertical/pipe/broken bar

				"⁂" +        // asterism

				"☞" +        // index/fist

				"∴" +        // therefore sign

				"‽" +        // interrobang

				"※" +          // reference mark

		        "]";
}
