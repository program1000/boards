package scrabble.player.model;

import java.util.List;
import java.util.Map;

import framework.Player;
import scrabble.board.model.Letter;
import scrabble.input.model.Word;
import scrabble.player.PlayerException;

public class ScrabblePlayer {
	
	private Hand hand;
	private Player player;
	
	public ScrabblePlayer() {
		hand = new Hand();
	}
	
	public void setPlayer( Player newPlayer ) {
		player = newPlayer;
	}
	
	public String getName() {
		return player.getName();
	}
	
	public void addLetters( List<Letter> letters ) {
		hand.add(letters);
	}
	
	public String getHandStr() {
		return hand.toString();
	}

	public int addPoints(int points) {
		player.addPoints(points);
		return player.getScore();
	}

	public Map<String,Integer> checkWithHand(Word word) throws PlayerException {
		return hand.checkWord( word );
	}

	public void newHand(Map<String,Integer> newLetterMap, List<Letter> newLetters) {
		hand.newHand(newLetterMap, newLetters);
	}

}
