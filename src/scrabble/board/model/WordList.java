package scrabble.board.model;

import java.util.List;

public class WordList {
	
	private ExtendedWord exWord;
	private List<BoardWord> allwords;
	
	public WordList(ExtendedWord newExWord, List<BoardWord> newAllwords) {
		exWord = newExWord;
		allwords = newAllwords;
	}
	
	public ExtendedWord getExWord() {
		return exWord;
	}
	
	public List<BoardWord> getAllWords() {
		return allwords;
	}

}
