import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	static int arrSize = 0;
	static Scanner sc = new Scanner(System.in);
	static int points=0;
	
	public static int getLines(File dictionary) {
		Scanner fileReader = null;
		int numberOfLines = 0;
		try {
			fileReader = new Scanner(dictionary, "UTF-8");
			while (fileReader.hasNextLine()) {
				numberOfLines++;
				fileReader.nextLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			fileReader.close();
		}

		return numberOfLines;
	}

	public static String getCategory(File dictionary) {
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(dictionary, "UTF-8");
			System.out.println("Please choose a category:");
			String categoryInitializer = "_";
			String str = fileReader.nextLine().substring(1);

			while (fileReader.hasNextLine()) {
				if (str.startsWith(categoryInitializer)) {
					System.out.println(str.substring(1));
				}
				str = fileReader.nextLine();
			}
			if (str.startsWith(categoryInitializer)) {
				System.out.println(str.substring(1));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			fileReader.close();
		}
		String category = sc.nextLine();
		return category;
	}

	public static String[] getElems(File dictionary, String category, int numberOfLines) {
		String[] elements = new String[numberOfLines];
		Boolean flag = false;
		Boolean correctCategory = false;
		Scanner fileReader = null;
		arrSize=0;
		try {
			fileReader = new Scanner(dictionary, "UTF-8");
			String categoryInitializer = "_";
			String str = fileReader.nextLine().substring(1);

			while (fileReader.hasNextLine()) {
				if (str.startsWith(categoryInitializer) && !flag) {
					if(str.substring(1).toLowerCase().equals(category.toLowerCase())) {
						correctCategory=true;
						flag=true;
					}
				}else if(!str.startsWith(categoryInitializer) && flag) {
					elements[arrSize]=str;
					arrSize++;
				}else if(str.startsWith(categoryInitializer) && flag && !str.substring(1).equals(category)) {
					flag=false;
				}
				str = fileReader.nextLine();
			}
			if (!str.startsWith(categoryInitializer) && flag) {
				elements[arrSize]=str;
			}
			
			if(!correctCategory) {
				System.out.println("Incorrect category. Please choose another one.");
				play(dictionary, numberOfLines);
				
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			fileReader.close();
		}
		return elements;
	}
	
	public static Boolean isSolved(char[] currentWord) {
		Boolean solved = true;
		for(int i = 0;i<currentWord.length;i++) {
			if(currentWord[i]=='_')
				solved=false;
		}
		return solved;
	}
	
	public static char[] getTemplate(char[] word) {
		char[] currentWord = new char[word.length*2];
		for(int i = 0;i<word.length;i++) {
			if(Character.isWhitespace(word[i]))
				currentWord[2*i]=word[i];
			else 
				currentWord[2*i]='_';
			currentWord[2*i+1]=' ';
		}
		return currentWord;
	}
	
	public static Boolean hasSpecialSymbol(char letter, char x) {
		x=Character.toLowerCase(x);
		return (letter=='u' && x=='ü') || (letter=='a' && x=='ä') || (letter=='o' && x=='ö');
	}
	
	public static Boolean contains(char[] arr, char letter) {
		letter=Character.toLowerCase(letter);
		for (char x : arr) {
			x=Character.toLowerCase(x);
	        if (x == letter || hasSpecialSymbol(letter,x)){
	            return true;
	        }
	    }
	    return false;
	}
	
	public static char[] fillWord(char[] word, char letter, char[] currentWord) {
		letter=Character.toLowerCase(letter);
		for(int i = 0;i<word.length;i++) {
			if(Character.toLowerCase(word[i])==letter || hasSpecialSymbol(letter,Character.toLowerCase(word[i])))
				currentWord[2*i]=word[i];
		}
		return currentWord;
	}
	
	public static void game(char[] word) {
		char[] currentWord=getTemplate(word);
		int attempts=10;
		char guess;
		
		while(!isSolved(currentWord) && attempts>0) {
			System.out.println("Attempts left:" + attempts);
			System.out.print("Current word/phrase:");
			System.out.println(currentWord);
			System.out.println("Please enter a letter:");
			guess=sc.next().charAt(0);
			sc.nextLine();
			if(!contains(word,guess))
				attempts--;
			else
				currentWord=fillWord(word,guess,currentWord);
		}
		if(attempts==0)
			System.out.println("Sorry, you ran out of attempts.");
		else {
			System.out.println("Congratulations you have revealed the word/phrase:");
			System.out.println(currentWord);
			points++;
		}
		System.out.println("Current score: " + points);
		
	}
	
	public static void play(File dictionary,int numberOfLines) {
		String category=getCategory(dictionary);
		String[] elements = getElems(dictionary,category, numberOfLines);
		int randomNum = ThreadLocalRandom.current().nextInt(0, arrSize+1);
		char[] word = elements[randomNum].toCharArray();
		game(word);
		play(dictionary,numberOfLines);
	}
	
	public static void main(String[] args) {
		
		File dictionary = new File("dictionary.txt");
		int numberOfLines = getLines(dictionary);
		
		play(dictionary,numberOfLines);
		
		sc.close();

	}

}