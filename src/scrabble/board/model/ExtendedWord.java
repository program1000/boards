package scrabble.board.model;

public class ExtendedWord {

	int preLen;
	int postLen;
	String word;
	boolean containExist;
	
	
	public ExtendedWord(int newPreLen, int newPostLen, String newWord, boolean newContainExist) {
		word = newWord;
		preLen = newPreLen;
		postLen = newPostLen;
		containExist = newContainExist;
	}
	
	public int getPreLen() {
		return preLen;
	}

	public int getPostLen() {
		return postLen;
	}
	
	public String getWord() {
		return word;
	}
	
	public boolean hasExist() {
		return containExist;
	}
}
