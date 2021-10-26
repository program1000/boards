package scrabble.util;

import java.util.HashMap;
import java.util.Map;

import scrabble.board.model.Letter;

public class CharToLetterMap {
	
	public static final String STR_BLANCO = "Blanco";
	
	private Map<String,Letter> map;
	private int maxLength=0;
	
	public CharToLetterMap() {
		map = new HashMap<String,Letter>();
	}
	
	public void add(Letter letter) {
		map.put(letter.getChar(),letter);
		int len=letter.getChar().length();
		if( len> maxLength) {
			maxLength=len;
		}
	}
	
	public boolean hasLetter( String chr) {
		if(chr.length()<=maxLength) {
			return map.containsKey(chr);
		}
		return false;
	}
	
	public Letter get(String chr) {
		if(chr.length()<=maxLength) {
		    return map.get(chr);
		}
		return null;
	}

	public Letter getBlanco(String chr) {
		if(chr.length()<=maxLength) {
		   Letter letter = get(STR_BLANCO);
		   letter.setChar(chr);
		   return letter;
		}
		return null;
	}

}
