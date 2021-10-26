package scrabble.input.model;

import java.util.ArrayList;
import java.util.List;

public class Word {

	List<InputLetter> letters;
	boolean isHorizontal=true;
	InputLetter head,tail;
	
	public Word( boolean newIsHorizontal) {
		letters = new ArrayList<InputLetter>();
		isHorizontal = newIsHorizontal;
	}
	
	public void addLetter(InputLetter letter) {
		if(letters.isEmpty()) {
			head=letter;
		}
		tail=letter;
		letters.add(letter);
	}
	
	public List<InputLetter> getLetters(){
	    return letters;
	}
	
	public int size() {
		int amount=0;
		for( InputLetter l : letters) {
			if( l.isExists()==false) {
				amount++;
			}
		}
		return amount;
	}
	
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public InputLetter getHead() {
		return head;
	}
	
	public InputLetter getTail() {
		return tail;
	}
}
