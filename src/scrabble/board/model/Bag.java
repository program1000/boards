package scrabble.board.model;

import java.util.ArrayList;
import java.util.List;

public class Bag {
	private List<Letter> letters;
	
	public Bag() {
		letters = new ArrayList<Letter>();
	}

	public void add(Letter letter) {
		letters.add(letter);
	}
	
	public int size() {
		return letters.size();
	}
	
	public List<Letter> get(int amount) {
		List<Letter> result = new ArrayList<Letter>();
		int takeAmount = Math.min(letters.size(), amount);
		for( int i=0; i<takeAmount; i++) {
	      result.add(letters.remove(0));
		}
		return result;
	}
	
	public void shuffle() {
		int l = letters.size();
		int index=0;
		for( Letter letter : letters) {
			int pos = (int)(Math.random() * l);
			Letter tmp = letters.get(pos);
			letters.set(pos, letter);
			letters.set(index, tmp);
			index++;
		}
	}

}
