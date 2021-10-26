package scrabble.player.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scrabble.board.BoardProcessor;
import scrabble.board.model.Letter;
import scrabble.input.model.InputLetter;
import scrabble.input.model.Word;
import scrabble.player.PlayerException;
import scrabble.util.CharToLetterMap;

public class Hand {
	
	public static final String ERR_LETTER_DOES_NOT_MATCH_HAND=" not in hand";
	
	private Map<String,Integer> letterMap;
	
	public Hand() {
		letterMap = new HashMap<String, Integer>();
	}

	public void add(List<Letter> newLetters) {
		for(Letter letter: newLetters) {
			Integer amount = letterMap.get(letter.getChar());
			if ( amount==null ) {
				amount=1;
			} else {
				amount++;
			}
			letterMap.put(letter.getChar(), amount);
		}
	}
	
	public String toString() {
		return letterMap.entrySet().stream().flatMap( entry -> {
			List<String> r=new ArrayList<String>();
			for(int i=0;i<entry.getValue();i++) {
				r.add(entry.getKey());
			}
			return r.stream();	
		}).reduce( (a,b) -> a+" "+b).orElse("");
	}
	
	public Map<String,Integer> checkWord(Word word) throws PlayerException {
		Map<String,Integer> newletterMap =  new HashMap<String, Integer>(letterMap);
		String key=null;
		for(InputLetter letter : word.getLetters() ) {
			if( letter.isExists() ) {
				continue;
			}
			if( letter.isBlanco() ) {
				key = CharToLetterMap.STR_BLANCO;
			}else {
			    key = letter.getChar();
			}
			Integer amount = newletterMap.get(key);
			if (amount==null) {
				throw new PlayerException(BoardProcessor.ERR_LETTER + key + ERR_LETTER_DOES_NOT_MATCH_HAND);
			} else {
				amount--;
				if( amount==0) {
					newletterMap.remove(key);
				} else {
					newletterMap.put(key,amount);
				}
			}
		}
		return newletterMap;
	}
	

	public void newHand(Map<String,Integer> newLetterMap, List<Letter> newLetters) {
		letterMap = newLetterMap;
		add( newLetters );
	}

}
