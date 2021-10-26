package scrabble.game;

import java.util.Map;

import scrabble.board.model.WordList;
import scrabble.input.model.Word;

public class ActionContext {
	
	private Word word;
	private boolean isCheckSuccess;
	private Map<String,Integer> letterMap;
	private WordList wordList;
	
	public ActionContext( Word newWord ) {
		word = newWord;
		isCheckSuccess=true;
	}
	
	public Word getWord() {
		return word;
	}
	
	public void setCheckSucces(boolean isSuccess) {
		isCheckSuccess = isSuccess;
	}
	
	public boolean isCheckSuccess() {
		return isCheckSuccess;
	}
	
	public void setLetterMap( Map<String, Integer> map ) {
		letterMap = map;
	}
	
	public Map<String,Integer> getLetterMap() {
		return letterMap;
	}

	public void setWordList(WordList newWordList) {
		if(newWordList==null) {
			setCheckSucces(false);
			return;
		}
		wordList = newWordList;
	}
	
	public WordList getWordList() {
		return wordList;
	}

}
