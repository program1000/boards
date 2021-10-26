package scrabble.game;

import java.util.List;
import java.util.Map;

import scrabble.board.model.BoardException;
import scrabble.board.model.BoardWord;
import scrabble.board.model.ExtendedWord;
import scrabble.board.model.WordList;
import scrabble.input.model.InputException;
import scrabble.input.model.Word;
import scrabble.player.PlayerException;

public interface ControlActionInterface {
	
	public void checkInput(Word word) throws InputException;

	public void checkInputWithBoard(Word word) throws BoardException;
	
	public void checkStartPositionWithBoard(Word word) throws BoardException;
	
	public Map<String, Integer> checkLetterWithHand(Word word) throws PlayerException;
	
	public WordList playWord(Word word);
	
	public void checkConnected(ExtendedWord exWord, List<BoardWord> allWords) throws BoardException;
	
	public void changeLetters(Word word, Map<String, Integer> letterMap);
	
	public void score(Word word, ExtendedWord exWord, List<BoardWord> allWords);
	
	public void nextPlayer();
	
	public void changeToNormalTurn();
	
	public void setQuitFlag();

}
