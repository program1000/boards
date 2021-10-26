package scrabble.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import framework.Player;
import scrabble.board.model.Bag;
import scrabble.game.GameEvent;
import scrabble.game.GameEventProducer;
import scrabble.input.model.Word;
import scrabble.player.model.ScrabblePlayer;

public class PlayerProcessor extends GameEventProducer {
	public static final String ERR_INPUT_WORD_LARGER_THAN_HAND = "Word is larger than hand";
	
	private List<ScrabblePlayer> players;
	private final int handSize=7;

	public void setPlayers(List<Player> sessionPlayers) {
		players = new ArrayList<ScrabblePlayer>();
		for(Player player : sessionPlayers) {
			ScrabblePlayer sp = new ScrabblePlayer();
			sp.setPlayer(player);
			players.add(sp);
		}
	}
	
	public void fillHand(Bag bag) {
		for( ScrabblePlayer player : players) {
			player.addLetters(bag.get(handSize));
			send(GameEvent.FILL_HAND, new Object[] {player});
		}
	}
	
	public Map<String,Integer> checkInput( int currentPlayerIndex, Word word ) throws PlayerException {
		if( word.size() > handSize ) {
			throw new PlayerException(ERR_INPUT_WORD_LARGER_THAN_HAND);
		}
		//match letters in hand
		return players.get(currentPlayerIndex).checkWithHand( word );
	}

	public int getNextPlayerId(int currentPlayerId) {
		int result = currentPlayerId+1;
		if (result==players.size()) {
			return 0;
		}
		return result;
	}
	
	public void changeLettersInHand( int currentPlayerIndex, Word word, Map<String,Integer> newLetterMap, Bag bag ) {
		int amount = word.size();
		players.get(currentPlayerIndex).newHand( newLetterMap, bag.get(amount) );
	}

	public void addPoints(int currentPlayerIndex, int points) {
		ScrabblePlayer p = players.get(currentPlayerIndex);
		int score = p.addPoints( points );
		send(GameEvent.ADD_POINTS, new Object[] { points, score });
	}
	


}
