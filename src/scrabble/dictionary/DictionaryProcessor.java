package scrabble.dictionary;

import scrabble.board.model.ExtendedWord;

public class DictionaryProcessor {
	private DictonaryDb db;

	public void setDb(DictonaryDb newDb) {
		db = newDb;
	}
	
	public boolean checkWord(ExtendedWord word) {
		return db.testWord(word.getWord());
	}

}
